package com.zerohub.web.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传 svc
 *
 * @author zhangyu
 * @date 2022/12/14 14:34
 */
public interface FileUploadService {
    /**
     * 校验文件是否已经上传
     *
     * @param fileHash 文件 hash 值
     * @return 是否存在标志
     */
    Boolean checkFile(String fileHash);

    /**
     * 保存分片文件
     *
     * @param fileHash 文件 hash 值
     * @param chunkNo  分片序号
     * @param chunk    文件分片
     * @return 保存成功标志
     */
    Boolean saveChunkFile(String fileHash, String chunkNo, MultipartFile chunk);

    /**
     * 合并分片文件
     *
     * @param filename 目标文件名
     * @param fileHash 文件的 hash 值
     * @param chunkSize 分片大小
     * @return 合并成功标志
     */
    Boolean mergeChunkFiles(String filename, String fileHash, Long chunkSize);
}
