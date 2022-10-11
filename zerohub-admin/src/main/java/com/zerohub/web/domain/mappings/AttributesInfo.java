package com.zerohub.web.domain.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * mappings 属性信息
 *
 * @author zhangyu
 * @date 2022/10/11 09:33
 */
@Data
// 忽略多余属性
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttributesInfo {
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
