package com.zerohub.web.controller;

import com.zerohub.web.domain.AjaxResult;
import com.zerohub.web.service.FileUploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 上传接口
 *
 * @author zhangyu
 * @date 2022/11/14 15:38
 */
@RestController
@RequestMapping("/backend-api/upload")
public class UploadController {
    private final FileUploadService fileUploadService;

    public UploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @GetMapping("/check")
    public AjaxResult check(@RequestParam("fileHash") String fileHash) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("checkResult", fileUploadService.checkFile(fileHash));

        return AjaxResult.success(result);
    }

    @PostMapping("/chunk")
    public AjaxResult uploadChunk(@RequestParam("fileHash") String fileHash,
                                  @RequestParam("chunkNo") String chunkNo,
                                  @RequestParam("chunk") MultipartFile chunk) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("uploadResult", fileUploadService.saveChunkFile(fileHash, chunkNo, chunk));

        return AjaxResult.success(result);
    }

    @GetMapping("/merge")
    public AjaxResult mergeChunk(@RequestParam("filename") String filename,
                                 @RequestParam("fileHash") String fileHash,
                                 @RequestParam("chunkSize") Long chunkSize) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("mergeResult", fileUploadService.mergeChunkFiles(filename, fileHash, chunkSize));

        return AjaxResult.success(result);
    }

}
