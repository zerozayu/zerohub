package com.zerohub.web.controller;

import com.zerohub.web.domain.AjaxResult;
import com.zerohub.web.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 搜索查询
 *
 * @author zhangyu
 * @date 2022/9/29 15:31
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }


    /**
     * 使用 content 字段执行全文搜索
     *
     * @param content 要搜索的内容
     * @return
     * @throws IOException
     */
    @GetMapping("/content")
    public AjaxResult search(@RequestParam(value = "content") String content) throws IOException {

        return AjaxResult.success(searchService.searchByContent(content));
    }


    /**
     * 使用元字段信息-文件原始名称进行模糊查询
     *
     * @return
     * @throws IOException
     */
    @GetMapping("/filename")
    public AjaxResult filename(@RequestParam("filename") String filename) throws IOException {
        return AjaxResult.success(searchService.searchByFilename(filename));
    }

    /**
     * 根据文章或者标题进行模糊查询
     *
     * @param something
     * @return
     */
    @GetMapping("/all")
    public AjaxResult all(@RequestParam("something") String something) throws IOException {
        return AjaxResult.success(searchService.searchBySomething(something));
    }

}
