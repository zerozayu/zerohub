package com.zerohub.web.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.zerohub.web.domain.AjaxResult;
import com.zerohub.web.domain.IndexSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 搜索查询
 *
 * @author zhangyu
 * @date 2022/9/29 15:31
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private ElasticsearchClient esClient;

    @GetMapping("")
    public AjaxResult search(@RequestParam(value = "searchContent") String searchContent) throws IOException {
        SearchResponse<IndexSource> search = esClient.search(searchRequestBuilder -> searchRequestBuilder
                        .index("idx")
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .query(searchContent)))
                , IndexSource.class);
        return AjaxResult.success(search.hits().hits().stream().map(Hit::source).collect(Collectors.toList()));
    }


    @GetMapping("/testsearch")
    public AjaxResult testsearch() throws IOException {
        System.out.println("search start");
        SearchResponse<IndexSource> search = esClient.search(searchRequestBuilder -> searchRequestBuilder
                        .index("idx")
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .defaultField("content")
                                        .query("zero")))
                , IndexSource.class);
        return AjaxResult.success(search.hits().hits().stream().map(Hit::source).collect(Collectors.toList()));
    }
}
