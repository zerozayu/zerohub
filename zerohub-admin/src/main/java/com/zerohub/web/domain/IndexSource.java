package com.zerohub.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * 索引 source
 *
 * @author zhangyu
 * @date 2022/10/8 14:35
 */
@Data
// 忽略多余属性
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexSource implements Serializable {

    private String content;

    private Meta meta;

    private File file;

    private Path path;

    @Data
    private static class Meta {
        private String altitude;
        private String author;
        private String comments;
        private String contributor;
        private String coverage;
        private String created;
        private String creator_tool;
        private String date;
        private String description;
        private String format;
        private String identifier;
        private String keywords;
        private String language;
        private String latitude;
        private String longitude;
        private String metadata_date;
        private String modifier;
        private String print_date;
        private String publisher;
        private String rating;
        private String relation;
        private String rights;
        private String source;
        private String title;
        private String type;
    }

    @Data
    private static class File {
        private String checksum;
        private String content_type;
        private String created;
        private String extension;
        private String filename;
        private String filesize;
        private String indexed_chars;
        private String indexing_date;
        private String last_accessed;
        private String last_modified;
        private String url;
    }

    @Data
    private static class Path {
        private String real;
        private String root;
        private String virtual;
    }
}
