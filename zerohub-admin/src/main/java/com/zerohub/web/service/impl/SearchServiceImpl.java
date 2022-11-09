package com.zerohub.web.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.zerohub.web.domain.FileInfoDTO;
import com.zerohub.web.domain.mappings.FileMapping;
import com.zerohub.web.service.SearchService;
import com.zerohub.web.utils.ESQueryUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * es 查询
 *
 * @author zhangyu
 * @date 2022/10/10 10:14
 */
@Service
public class SearchServiceImpl implements SearchService {
    private final ElasticsearchClient esClient;

    public SearchServiceImpl(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    @Value("${elasticsearch.zerohub.index-name}")
    private String indexName;

    /**
     * 根据全文内容进行详尽搜索
     *
     * @return
     */
    @Override
    public List<FileInfoDTO> searchByContent(String content) throws IOException {
        SearchResponse<FileMapping> search = esClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .match(m -> m
                                        .field("content")
                                        .query(content)
                                )
                        )
                        // 设置结果高亮
                        .highlight(highlightBuilder -> highlightBuilder
                                // .preTags("<p style=\"background-color:#FFFF00\">")
                                // .postTags("</p>")
                                .preTags("<span color='red'>")
                                .postTags("</span>")
                                .fields("content", highlightFiled -> highlightFiled)
                        )
                , FileMapping.class);
        List<FileMapping> fileMappings = ESQueryUtils.parseResponse(search);
        return getFileInfoDTOS(fileMappings);
    }

    /**
     * 根据文章标题进行模糊搜索
     *
     * @return
     */
    @Override
    public List<FileInfoDTO> searchByFilename(String filename) throws IOException {
        // 全文查询时使用模糊参数，先分词再计算模糊选项。
        // todo (zhangyu, 2022-10-10, 17:32:28) : 最大 Levenshtein 编辑距离为 1 的话是否还需要分词需要进行测试验证
        SearchResponse<FileMapping> search = esClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .fuzzy(f -> f
                                        .field("file.filename")
                                        .value(field -> field.stringValue(filename))
                                        .fuzziness("0.5")
                                )
                        )
                , FileMapping.class);
        return null/*getFileInfoDTOS(ESQueryUtils.parseResponse(search), highlight)*/;
    }

    @Override
    public List<FileInfoDTO> searchAll(String params) throws IOException {
        SearchResponse<FileMapping> search = esClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .bool(b -> b
                                        .should(queryBuilder -> queryBuilder
                                                .match(m -> m
                                                        .field("content")
                                                        .query(params)
                                                )
                                        )
                                        .should(queryBuilder -> queryBuilder
                                                .fuzzy(f -> f
                                                        .field("file.filename")
                                                        .value(field -> field.stringValue(params))
                                                        .fuzziness("0.5")
                                                )
                                        )
                                )
                        )
                        // 设置结果高亮
                        .highlight(highlightBuilder -> highlightBuilder
                                // .preTags("<p style=\"background-color:#FFFF00\">")
                                // .postTags("</p>")
                                .preTags("<span style=\"color: red;\">")
                                .postTags("</span>")
                                .fields(new HashMap<>() {{
                                    put("content", HighlightField.of(h -> h));
                                    put("file.filename", HighlightField.of(h -> h));
                                }})
                        )
                , FileMapping.class);

        return handleResponse(search);
    }

    /**
     * 将FileMapping转换为前端所需数据FileInfoDTO
     *
     * @param fileMappings
     * @return
     */
    private static List<FileInfoDTO> getFileInfoDTOS(List<FileMapping> fileMappings) {
        return fileMappings.stream().map(fileMapping -> {
                    FileInfoDTO fileInfoDTO = new FileInfoDTO();
                    fileInfoDTO.setFilename(fileMapping.getFile().getFilename())
                            .setExtension(fileMapping.getFile().getExtension())
                            .setUrl(fileMapping.getFile().getUrl())
                            .setFilesize(fileMapping.getFile().getFilesize())
                            .setCreated(fileMapping.getFile().getCreated())
                    ;
                    return fileInfoDTO;
                })
                .collect(Collectors.toList());
    }

    private static List<FileInfoDTO> handleResponse(SearchResponse<FileMapping> search) {
        List<FileInfoDTO> fileList = search.hits().hits()
                .stream().map(tHit -> {
                    FileInfoDTO fileInfoDTO = new FileInfoDTO();
                    Map<String, List<String>> highlight = tHit.highlight();


                    FileMapping fileMapping = tHit.source();
                    fileInfoDTO.setFilename(fileMapping != null ? fileMapping.getFile().getFilename() : null)
                            .setExtension(fileMapping != null ? fileMapping.getFile().getExtension() : null)
                            .setUrl(fileMapping != null ? fileMapping.getFile().getUrl() : null)
                            .setFilesize(fileMapping != null ? fileMapping.getFile().getFilesize() : null)
                            .setCreated(fileMapping != null ? fileMapping.getFile().getCreated() : null);

                    if (ObjectUtils.isNotEmpty(highlight)) {
                        // todo (zhangyu, 2022-11-08, 11:13:59) : 改成自动获取 key 名并赋值
                        if (highlight.containsKey("content") && ObjectUtils.isNotEmpty(highlight.get("content"))) {
                            fileInfoDTO.setContent(Arrays.toString(highlight.get("content").toArray()));
                        }
                        if (highlight.containsKey("file.filename") && ObjectUtils.isNotEmpty(highlight.get("file.filename"))) {
                            fileInfoDTO.setFilename(Arrays.toString(highlight.get("file.filename").toArray()));
                        }
                    }
                    return fileInfoDTO;
                })
                .collect(Collectors.toList());
        return fileList;
    }

}
