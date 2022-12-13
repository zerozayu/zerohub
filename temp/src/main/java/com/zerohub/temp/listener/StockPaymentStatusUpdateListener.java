package com.zerohub.temp.listener;

import com.zerohub.temp.event.PaymentStatusUpdateEvent;
import lombok.NonNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 库存服务监听器
 * 无序事件监听器
 *
 * @author zhangyu26
 * @date 2022/10/21
 */
@Component
public class StockPaymentStatusUpdateListener implements ApplicationListener<PaymentStatusUpdateEvent> {
    @Override
    public void onApplicationEvent(@NonNull PaymentStatusUpdateEvent event) {
        System.out.println("库存服务，收到支付状态" + event + " - Thread:" + Thread.currentThread().getName());
    }
}
