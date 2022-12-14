package com.zerohub.web;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.zerohub.web.domain.mappings.FileMapping;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangyu
 * @date 2022/9/28 10:09
 */
@SpringBootTest(classes = ZerohubApplication.class)
class ZerohubApplicationTest {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Test
    void test1() throws IOException {
        SearchResponse<Map> search = elasticsearchClient
                .search(searchRequestBuilder -> searchRequestBuilder
                                .index("idx")
                                .query(queryBuilder -> queryBuilder
                                        .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                                .query("test")))
                        , Map.class);
        System.out.println(search.hits());

    }

    @Test
    void test2() throws IOException {
        SearchResponse<FileMapping> search = elasticsearchClient
                .search(searchRequestBuilder -> searchRequestBuilder
                                .index("idx")
                                .query(queryBuilder -> queryBuilder
                                        .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                                .query("zero")))
                        , FileMapping.class);
        List<FileMapping> collect = search.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
        System.out.println(collect);
    }


    @Test
    public void createIndex() throws IOException {
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(builder -> builder
                .index("test_index")
                .settings(settingsBuilder -> settingsBuilder.numberOfShards("1").numberOfReplicas("1"))
                .mappings(typeMappingBuilder -> typeMappingBuilder
                        .properties("name", propertyBuilder -> propertyBuilder.keyword(keywordBuilder -> keywordBuilder))
                        .properties("age", propertyBuilder -> propertyBuilder.integer(integerBuilder -> integerBuilder))
                        .properties("remarks", propertyBuilder -> propertyBuilder.text(textBuilder -> textBuilder.analyzer("standard"))
                        )
                )
        );
        System.out.println(createIndexResponse);
    }

    @Test
    public void testSystemClass() throws IOException {
        String osName = System.getProperty("os.name");
        System.out.println(osName);

        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("docker images");

        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> processList = new ArrayList<>();

        String line = "";
        while ((line = input.readLine()) != null){
            processList.add(line);
        }

        input.close();
        System.out.println("1");
    }
}