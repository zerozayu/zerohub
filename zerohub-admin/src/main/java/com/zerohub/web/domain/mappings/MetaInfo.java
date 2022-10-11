package com.zerohub.web.domain.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * mappings 元属性信息
 *
 * @author zhangyu
 * @date 2022/10/11 09:34
 */
@Data
// 忽略多余属性
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaInfo {
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
