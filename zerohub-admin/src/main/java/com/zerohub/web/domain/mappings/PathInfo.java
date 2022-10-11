package com.zerohub.web.domain.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * mappings 路径信息
 *
 * @author zhangyu
 * @date 2022/10/11 09:37
 */
@Data
// 忽略多余属性
@JsonIgnoreProperties(ignoreUnknown = true)
public class PathInfo {

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
