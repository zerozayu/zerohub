package com.zerohub.web.controller;

import com.zerohub.web.config.NonStaticResourceHttpRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 视频播放
 *
 * @author zhangyu
 * @date 2022/11/28 17:15
 */
@Slf4j
@RestController
@RequestMapping("/backend-api/video")
public class VideoController {

    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    @Value("${video.path}")
    private String videoPath;


    public VideoController(NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler) {
        this.nonStaticResourceHttpRequestHandler = nonStaticResourceHttpRequestHandler;
    }

    /**
     * 通过 ResourceHttpRequestHandler
     * ResourceHttpRequestHandler是springboot加载静态资源的一个类，平时是用来从resources/statics等目录加载文件的。所以，这个类本身就是支持range请求数据的。
     *
     * @return
     */
    @GetMapping("/resource")
    public void resource(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //假如我把视频1.mp4放在了static下的video文件夹里面
        //sourcePath 是获取resources文件夹的绝对地址
        //realPath 即是视频所在的磁盘地址
        // String sourcePath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);
        // String realPath = sourcePath + "static/video/1.mp4";

        Path filePath = Paths.get(videoPath);
        if (Files.exists(filePath)) {
            String mimeType = Files.probeContentType(filePath);
            if (!ObjectUtils.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

    @GetMapping("/hls")
    public void hls(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {




        // // 转发请求到本地视频
        // RequestDispatcher requestDispatcher = request.getRequestDispatcher("/backend-api/video/resource");
        // requestDispatcher.forward(request, response);
    }


}
