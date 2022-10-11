package com.zerohub.web.domain.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 文件内容信息
 *
 * @author zhangyu
 * @date 2022/10/11 09:36
 */
@Data
// 忽略多余属性
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileInfo {

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
