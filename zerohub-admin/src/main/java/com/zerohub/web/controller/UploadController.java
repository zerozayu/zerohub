package com.zerohub.web.controller;

import com.zerohub.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 上传接口
 *
 * @author zhangyu
 * @date 2022/11/14 15:38
 */
@RestController
@RequestMapping("/backend-api/upload")
public class UploadController {

    @Value("${upload.file.path}")
    private String dirPath;

    @PostMapping("/chunk")
    public AjaxResult uploadChunk(@RequestParam("chunk") MultipartFile chunk,
                                  @RequestParam("filename") String filename) {
        File folder = new File(dirPath);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }

        // 文件分片路径
        String filePath = dirPath + File.separator + filename;

        File saveFile = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(saveFile)) {
            fos.write(chunk.getBytes());
            chunk.transferTo(saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return AjaxResult.success();
    }

    @GetMapping("/merge")
    public AjaxResult mergeChunk(@RequestParam("hash") String hash, @RequestParam("filename") String filename) {
        // 文件分片所在的文件夹
        File chunkFileFolder = new File(dirPath);
        // 合并后的文件路径
        File mergeFile = new File(dirPath + File.separator + filename);
        // 得到文件分片所在的文件夹下的所有文件
        File[] chunks = chunkFileFolder.listFiles();
        assert chunks != null;
        // 按照 hash 值过滤出对应的文件分片
        // 排序
        File[] files = Arrays.stream(chunks)
                .filter(file -> file.getName().startsWith(hash))
                // 分片文件命名为"hash值_id.文件后缀名"
                // 按照 id 值排序
                .sorted(Comparator.comparing(o -> Integer.valueOf(o.getName().split("\\.")[0].split("_")[1])))
                .toArray(File[]::new);

        try (RandomAccessFile randomAccessFileWriter = new RandomAccessFile(mergeFile, "rw")) {
            byte[] bytes = new byte[1024];
            for (File chunk : files) {
                try (RandomAccessFile randomAccessFileReader = new RandomAccessFile(chunk, "r")) {
                    int len;
                    while ((len = randomAccessFileReader.read(bytes)) != -1) {
                        randomAccessFileWriter.write(bytes, 0, len);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(hash);
        return AjaxResult.success(mergeFile);
    }

}
