package com.zerohub.web.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.zerohub.web.domain.FileInfoDTO;
import com.zerohub.web.domain.mappings.FileMapping;
import com.zerohub.web.service.SearchService;
import com.zerohub.web.utils.ESQueryUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
                                        .fuzziness("1")
                                )
                        )
                , FileMapping.class);
        return getFileInfoDTOS(ESQueryUtils.parseResponse(search));
    }

    @Override
    public List<FileInfoDTO> searchBySomething(String something) throws IOException {
        SearchResponse<FileMapping> search = esClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .bool(b -> b
                                        .should(queryBuilder -> queryBuilder
                                                .match(m -> m
                                                        .field("content")
                                                        .query(something)
                                                )
                                        )
                                        .should(queryBuilder -> queryBuilder
                                                .fuzzy(f -> f
                                                        .field("file.filename")
                                                        .value(field -> field.stringValue(something))
                                                        .fuzziness("1")
                                                )
                                        )
                                )
                        )
                , FileMapping.class);

        return getFileInfoDTOS(ESQueryUtils.parseResponse(search));
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

}
