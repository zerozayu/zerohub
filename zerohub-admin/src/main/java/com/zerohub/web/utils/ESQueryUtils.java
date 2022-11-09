package com.zerohub.web.utils;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 对 es 执行完查询的数据进行解析
 *
 * @author zhangyu
 * @date 2022/10/9 10:16
 */
public class ESQueryUtils {
    /**
     * 将 SearchResponse 转换为 List
     *
     * @param searchResponse 查询结果
     * @param <T>            实体类
     * @return hits 的 source 数据集合 list
     */
    public static <T> List<T> parseResponse(SearchResponse<T> searchResponse) {
        return searchResponse.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }

    /**
     * 将 SearchResponse 转换为 List
     *
     * @param searchResponse 查询结果
     * @param <T>            实体类
     * @return hits 的 source 数据集合 list
     */
    public static <T> List<Map<String, List<String>>> getHighlight(SearchResponse<T> searchResponse) {
        return searchResponse.hits().hits().stream().map(Hit::highlight).collect(Collectors.toList());
    }


}
