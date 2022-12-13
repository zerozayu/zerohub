package com.zerohub.temp.event;

import com.zerohub.temp.domain.PaymentInfo;
import org.springframework.context.ApplicationEvent;

/**
 * 支付状态更新的事件
 *
 * @author zhangyu26
 * @date 2022/10/21
 */
public class PaymentStatusUpdateEvent extends ApplicationEvent {
    public PaymentStatusUpdateEvent(PaymentInfo source) {
        super(source);
    }
}
