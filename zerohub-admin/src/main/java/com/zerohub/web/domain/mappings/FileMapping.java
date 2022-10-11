package com.zerohub.web.domain.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 索引 source
 *
 * @author zhangyu
 * @date 2022/10/8 14:35
 */
@Data
// 忽略多余属性
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileMapping {
    // attachment   BASE64 编码的二进制文件

    /**
     * 提取内容
     * "This is my text!"
     */
    private String content;

    /**
     * 元文件信息
     */
    private MetaInfo meta;

    /**
     * 文件相关信息
     */
    private FileInfo file;

    /**
     * 路径相关信息
     */
    private PathInfo path;

    /**
     * 文件属性信息
     */
    private AttributesInfo attributes;

    /**
     * 附加标签
     * { "tenantId": 22, "projectId": 33 }
     */
    private String external;
}
