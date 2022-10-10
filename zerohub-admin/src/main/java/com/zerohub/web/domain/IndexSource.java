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
    // attachment   BASE64 编码的二进制文件

    /**
     * 提取内容
     * "This is my text!"
     */
    private String content;

    /**
     * 元文件信息
     */
    private Meta meta;

    /**
     * 文件相关信息
     */
    private File file;

    /**
     * 路径相关信息
     */
    private Path path;

    private Attributes attributes;

    /**
     * 附加标签
     * { "tenantId": 22, "projectId": 33 }
     */
    private String external;

    @Data
    private static class Meta {

        /**
         * 该点的WGS84高度
         * ""
         */
        private String altitude;

        /**
         * 作者
         * "David Pilato"
         */
        private String author;

        /**
         * 注释
         * "Comments"
         */
        private String comments;

        /**
         * 贡献者
         * "foo bar"
         */
        private String contributor;

        /**
         * 覆盖范围
         * "FOOBAR"
         */
        private String coverage;

        /**
         * 创建日期
         * "2013-04-04T15:21:35"
         */
        private String created;

        /**
         * 用于创建资源的工具
         * "HTML2PDF- TCPDF"
         */
        private String creator_tool;

        /**
         * 上次修改日期
         * "2013-04-04T15:21:35"
         */
        private String date;

        /**
         * 内容说明
         * "This is a description"
         */
        private String description;

        /**
         * 媒体格式
         * "application/pdf; version=1.6"
         */
        private String format;

        /**
         * 标识符-例如 URL/DOI/ISBN
         * "FOOBAR"
         */
        private String identifier;

        /**
         * 文档元数据中的关键字（如果有）
         * ["fs","elasticsearch"]
         */
        private String keywords;

        /**
         * 语言（可检测）
         * "fr"
         */
        private String language;

        /**
         * 该点的WGS84纬度
         * "N 48° 51' 45.81''"
         */
        private String latitude;

        /**
         * 该点的WGS84经度
         * "E 2° 17'15.331''"
         */
        private String longitude;

        /**
         * 元数据的最后修改
         * "2013-04-04T15:21:35"
         */
        private String metadata_date;

        /**
         * 最后作者
         * "David Pilato"
         */
        private String modifier;

        /**
         * 文档最后一次打印是什么时候
         * "2013-04-04T15:21:35"
         */
        private String print_date;

        /**
         * 发布者：个人、组织、服务
         *
         */
        private String publisher;

        /**
         * 用户分配的评级 -1，[0..5]
         * 0
         */
        private String rating;

        /**
         * 相关资源
         * "FOOBAR"
         */
        private String relation;

        /**
         * 权利信息
         * "CC-BY-ND"
         */
        private String rights;

        /**
         * 当前文档的来源（派生的）
         * "FOOBAR"
         */
        private String source;

        /**
         * 文档元数据中的标题
         * "My document title"
         */
        private String title;

        /**
         * 内容的性质或类型
         * "Image"
         */
        private String type;

        // raw - 具有所有原始元数据的对象 - "meta.raw.channels": "2"
    }

    @Data
    private static class File {

        /**
         * 校验和
         * "c32eafae2587bef4b3b32f73743c3c61"
         */
        private String checksum;

        /**
         * 内容类型
         * "application/vnd.oasis.opendocument.text"
         */
        private String content_type;

        /**
         * 创建时间
         * "2018-07-30T11:19:23.000+0000"
         */
        private String created;

        /**
         * 原始文件扩展名
         * "pdf"
         */
        private String extension;

        /**
         * 原始文件名
         * "mydocument.pdf"
         */
        private String filename;

        /**
         * 文件大小（以字节为单位）
         * 1256362
         */
        private String filesize;

        /**
         * 提取的字符
         * 100000
         */
        private String indexed_chars;

        /**
         * 索引日期
         * "2018-07-30T11:19:23.000+0000"
         */
        private String indexing_date;

        /**
         * 上次访问日期
         * "2018-07-30T11:19:23.000+0000"
         */
        private String last_accessed;

        /**
         * 上次修改时间
         * "2018-07-30T11:19:23.000+0000"
         */
        private String last_modified;

        /**
         * 原始文件地址
         * "file://tmp/otherdir/mydocument.pdf"
         */
        private String url;
    }

    @Data
    private static class Path {

        /**
         * 真实路径名
         * "/tmp/otherdir/mydocument.pdf"
         */
        private String real;

        /**
         * MD5编码的父路径（内部使用）
         * "112aed83738239dbfe4485f024cd4ce1"
         */
        private String root;

        /**
         * 相对路径
         * "/otherdir/mydocument.pdf"
         */
        private String virtual;
    }

    @Data
    private static class Attributes{
        /**
         * 所有者名字
         * "david"
         */
        private String owner;

        /**
         * 团队名字
         * "staff"
         */
        private String group;

        /**
         * 权限
         * 	764
         */
        private String permissions;
    }
}
