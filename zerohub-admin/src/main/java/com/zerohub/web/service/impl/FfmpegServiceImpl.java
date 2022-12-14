package com.zerohub.web.service.impl;

import com.zerohub.web.service.FfmpegService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ffmpeg service 实现类
 *
 * @author zhangyu
 * @date 2022/11/28 21:57
 */
@Service
@Slf4j
public class FfmpegServiceImpl implements FfmpegService {


    @Value("${video.ffmpeg.path.win}")
    private String ffmpegWinPath;
    @Value("${video.ffmpeg.path.linux}")
    private String ffmpegLinuxPath;
    @Value("#{'${video.fileSuffix:}'.empty ? null : '${video.fileSuffix:}'.split(',')}")
    private List<String> fileSuffix;

    @Override
    public void handleVideo(String sourcePath, String targetPath) {
        log.info("CPU --> {}", Runtime.getRuntime().availableProcessors());

        // 检查 ffmpeg 命令是否存在
        String osName = System.getProperty("os.name");
        File ffmpegCmd;
        boolean isWindows = false;

        if (osName.toLowerCase().startsWith("win")) {
            log.info("<<Windows 版本>>");
            isWindows = true;
            ffmpegCmd = new File(ffmpegWinPath + File.separator + "ffmpeg.exe");
        } else {
            log.info("<<非 Windows 版本>>");
            ffmpegCmd = new File(ffmpegLinuxPath + File.separator + "ffmpeg");

        }

        // 判断 docker 环境是否存在 linuxserver/ffmpeg 镜像
        String dockerImage = getDockerImage();

        // 检查源目录和目标目录是否存在
        if (invalidPath(sourcePath, targetPath)) {
            return;
        }

        // 获取符合条件的文件
        File[] files = new File(sourcePath).listFiles((dir, name) -> {
            String[] suffix = name.split(",");
            if (fileSuffix.contains(suffix[suffix.length - 1])) {
                log.info("<<符合的文件>> {}", name);
                return true;
            } else {
                log.warn("<<不符合的文件或目录>> {}", name);
                return false;
            }
        });

        // String ffmpegCmdPref = null == dockerImage ? ffmpegCmd.getAbsolutePath() : "docker run --rm -it  -v $(pwd):/config " + dockerImage + " ";
        for (File file : files) {
            execVideoTranscode(ffmpegCmd.getAbsolutePath(), isWindows, file.getAbsolutePath(), getTargetFile(file, targetPath));
        }
    }

    private void execVideoTranscode(String ffmpegCmdPref, boolean isWindows, String sourceFile, String targetFile) {
        Runtime runtime = null;
        try {
            log.info("<<开始视频转码>> 文件名：{}", sourceFile);
            runtime = Runtime.getRuntime();
            long startTime = System.currentTimeMillis();
            String cmd1 = ffmpegCmdPref + " -y -i " + sourceFile + " -vcodec libx264 -vf scale=\"iw/1:ih/1\" " + targetFile;
            String cmd = ffmpegCmdPref + " -re -i " + sourceFile + " -vcodec libx264 -acodec aac -strict -2 -f flv " + "rtmp://localhost:1935/rtmp/room";

            log.info("<<命令>> {}", cmd);
            Process process = null;
            if (isWindows) {
                process = runtime.exec(cmd);
            } else {
                process = runtime.exec(new String[]{"sh", "-c", cmd});
            }
            // 通过读取进程的流信息，可以看到视频转码的相关执行信息，并且使得下面的视频转码时间贴近实际的情况
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                log.debug("<<视频执行信息>> {}", line);
            }
            br.close();
            log.info("<<开始关闭进程相关流>>");
            process.getOutputStream().close();
            process.getInputStream().close();
            process.getErrorStream().close();
            long endTime = System.currentTimeMillis();
            log.info("<<视频转码完成>> 耗时 {}ms", (endTime - startTime));
        } catch (IOException e) {
            log.error("<<视频转码失败，原因：发生IO异常>>");
        } finally {
            if (Objects.nonNull(runtime)) {
                runtime.freeMemory();
            }
        }
    }

    private String getTargetFile(File file, String targetPath) {
        String absolutePath = file.getAbsolutePath();
        return targetPath + absolutePath.substring(absolutePath.lastIndexOf(File.separatorChar));
    }


    public String getDockerImage() {
        Runtime runtime = null;
        try {
            runtime = Runtime.getRuntime();
            Process process = runtime.exec("docker images");
            List<String> processResult = getProcessResult(process);
            if (null != processResult) {
                // 返回第一个 ffmpeg 镜像的
                String result = processResult.parallelStream().filter(str -> str.contains("linuxserver/ffmpeg")).collect(Collectors.toList()).get(0);
                return "linuxserver/ffmpeg:" + result.split(" +")[1];
            }
        } catch (IOException e) {
            log.error("校验是否存在 docker 环境出现异常");
            // throw new RuntimeException(e);
        } finally {
            if (Objects.nonNull(runtime)) {
                runtime.freeMemory();
            }
        }

        return null;
    }

    public List<String> getProcessResult(Process process) {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line = "";
            List<String> processList = new ArrayList<>();
            while ((line = input.readLine()) != null) {
                processList.add(line);
            }
            return processList;
        } catch (Exception e) {
            log.error("获取脚本执行结果异常:{}", e.getMessage());
            return null;
        }
    }

    /**
     * 检查源目录和目标目录是否存在
     *
     * @param sourcePath
     * @param targetPath
     */
    private boolean invalidPath(String sourcePath, String targetPath) {
        File srcPath = new File(sourcePath);
        if (!srcPath.exists()) {
            log.error("<<源目录不存在>>");
            return true;
        }
        File tgtPath = new File(targetPath);
        if (!tgtPath.exists()) {
            tgtPath.mkdirs();
        }
        return false;
    }

}
