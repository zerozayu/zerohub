package com.zerohub.temp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 支付实体类
 *
 * @author zhangyu26
 * @date 2022/10/21
 */

@Data
@AllArgsConstructor
public class PaymentInfo {

    private int id;
    private String status;
}
