package com.zerohub.web.service;

/**
 * ffmpeg service
 *
 * @author zhangyu
 * @date 2022/11/28 21:56
 */
public interface FfmpegService {

    public void handleVideo(String sourcePath, String targetPath);
}
