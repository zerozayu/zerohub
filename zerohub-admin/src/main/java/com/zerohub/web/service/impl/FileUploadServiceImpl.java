package com.zerohub.web.service.impl;

import com.zerohub.web.service.FileUploadService;
import com.zerohub.web.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 文件上传 svc 实现类
 *
 * @author zhangyu
 * @date 2022/12/14 14:34
 */
@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${upload.file.path}")
    private String dirPath;

    private final RedisUtil redisUtil;

    public FileUploadServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 校验文件是否已经上传
     *
     * @param fileHash 文件 hash 值
     * @return 是否存在标志
     */
    @Override
    public Boolean checkFile(String fileHash) {
        // todo (zhangyu, 2022-12-14, 15:45:40) : 如果缓存失效去数据库查询
        return redisUtil.hHasKey("upload-file", fileHash);
    }

    /**
     * 保存分片文件
     *
     * @param fileHash 文件 hash 值
     * @param chunkNo  分片序号
     * @param chunk    文件分片
     * @return 保存成功标志
     */
    @Override
    public Boolean saveChunkFile(String fileHash, String chunkNo, MultipartFile chunk) {
        File fileDirPath = new File(dirPath + File.separator + fileHash);
        if (!fileDirPath.exists() || !fileDirPath.isDirectory()) {
            log.info("文件夹[{}]不存在, 新建目标文件夹", fileDirPath.getAbsolutePath());
            fileDirPath.mkdirs();
        }

        File chunkFile = new File(dirPath + File.separator + fileHash + File.separator + fileHash + "_" + chunkNo);

        try {
            chunk.transferTo(chunkFile);
            log.info("分片文件{}保存成功", chunkFile.getAbsolutePath());
            try (FileInputStream fis = new FileInputStream(chunkFile.getAbsoluteFile())) {
                String md5 = DigestUtils.md5Hex(fis);
                log.info("chunkMd5:{}", md5);
            } catch (IOException e) {
                log.error("计算目标文件 hash 值出错", e);
                throw new RuntimeException("计算目标文件 hash 值出错", e);
            }
        } catch (IOException e) {
            log.error("分片文件保存时出现异常", e);
        }

        return false;
    }

    /**
     * 合并分片文件
     *
     * @param filename 目标文件名
     * @param fileHash 文件的 hash 值
     * @return 合并成功标志
     */
    @Override
    public Boolean mergeChunkFiles(String filename, String fileHash, Long chunkSize) {
        File fileDirPath = new File(dirPath + File.separator + fileHash);

        if (!fileDirPath.exists() || !fileDirPath.isDirectory()) {
            log.info("文件夹[{}]不存在, 新建目标文件夹", fileDirPath.getAbsolutePath());
            fileDirPath.mkdirs();
        }

        File targetFile = new File(dirPath + File.separator + fileHash + File.separator + filename);

        File[] chunkFiles = fileDirPath.listFiles(chunkName -> chunkName.getName().startsWith(fileHash));

        // 合并文件
        boolean mergeComplete = this.mergeFiles(chunkFiles, targetFile, chunkSize);

        if (!mergeComplete) {
            return null;
        }

        // 校验 hash
        log.info("分片文件合并成功,开始校验目标文件 hash 值");
        boolean equals = this.verifyMd5(fileHash, targetFile);
        if (equals) {
            // todo (zhangyu, 2022-12-14, 15:44:24) : 存储到数据库中,插数据库成功则插入 redis,并设置过期策略,还可以设定最大上限
            redisUtil.hset("upload-file", fileHash, targetFile.getAbsoluteFile());

            return true;
        }

        return false;
    }

    /**
     * 合并分片文件
     *
     * @param chunkFiles 分片文件对象数组
     * @param targetFile 要保存的文件
     * @return 合并成功标志
     */
    private boolean mergeFiles(File[] chunkFiles, File targetFile, Long chunkSize) {
        if (ArrayUtils.isEmpty(chunkFiles)) {
            log.info("没有任何分片文件");
            return false;
        }

        log.info("分片文件[{}]开始合并成目标文件[{}]", Arrays.stream(chunkFiles).toArray(), targetFile.getAbsoluteFile());
        List<File> chunkFileList = Arrays.stream(chunkFiles)
                .sorted(Comparator.comparing(tmp -> tmp.getName().split("_")[1]))
                .collect(Collectors.toList());

        List<Future<Boolean>> rwResult = new ArrayList<>();

        for (File chunkFile : chunkFileList) {
            int chunkNo = Integer.parseInt(chunkFile.getName().split("_")[1]);
            Future<Boolean> rwFlag = readAndWriteFile(chunkFile, targetFile, chunkNo * chunkSize);
            rwResult.add(rwFlag);
        }
        return rwResult.stream().allMatch(flag -> {
            try {
                return flag.get();
            } catch (ExecutionException | InterruptedException e) {
                log.error("文件合并出现问题", e);
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 异步多线程任务执行分片文件读写操作
     *
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     * @param skip       RandomAccessFile跳过的写位置
     * @return 读写成功标志
     */
    @Async("asyncExecutor")
    public Future<Boolean> readAndWriteFile(File sourceFile, File targetFile, long skip) {
        try (RandomAccessFile reader = new RandomAccessFile(sourceFile, "r");
             RandomAccessFile writer = new RandomAccessFile(targetFile, "rw")) {
            writer.seek(skip);

            int len;
            byte[] buffer = new byte[1024];
            while ((len = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, len);
            }
            return new AsyncResult<>(true);
        } catch (IOException e) {
            log.error("读写文件出错");
            return new AsyncResult<>(false);
        }
    }


    /**
     * 校验 hash
     *
     * @param fileHash   上传的文件 hash
     * @param targetFile 生成的文件
     * @return 校验成功标志
     */
    private boolean verifyMd5(String fileHash, File targetFile) {
        String targetFileMd5 = null;
        try (FileInputStream fis = new FileInputStream(targetFile)) {
            targetFileMd5 = DigestUtils.md5Hex(fis);
        } catch (IOException e) {
            log.error("计算目标文件 hash 值出错", e);
            throw new RuntimeException("计算目标文件 hash 值出错", e);
        }

        log.info("目标文件 hash 值[{}], 上传 hash 值[{}]", targetFileMd5, fileHash);
        return Objects.equals(targetFileMd5, fileHash);
    }


}
