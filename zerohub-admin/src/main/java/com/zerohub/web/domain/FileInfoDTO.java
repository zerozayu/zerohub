package com.zerohub.web.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 返回给前端的文件查询结果信息
 *
 * @author zhangyu
 * @date 2022/10/10 17:09
 */
@Data
@Accessors(chain = true)
public class FileInfoDTO {

    /**
     * 原始文件名
     */
    private String filename;

    /**
     * 文件内容
     */
    private String content;

    /**
     * 原始文件扩展名
     */
    private String extension;

    /**
     * 原始文件地址
     */
    private String url;

    /**
     * 文件大小
     */
    private String filesize;

    /**
     * 创建日期
     */
    private String created;
}
