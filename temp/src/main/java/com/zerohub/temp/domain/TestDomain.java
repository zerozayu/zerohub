package com.zerohub.temp.domain;

import lombok.Data;

/**
 * @author zhangyu26
 * @date 2022/10/26
 */
@Data
public class TestDomain {
    private String name;
    private final String remark = "123";

    public void setRemark(String remark){
    }
}
