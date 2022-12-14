package com.zerohub.temp.listener;

import com.zerohub.temp.domain.PaymentInfo;
import com.zerohub.temp.event.PaymentStatusUpdateEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 邮件服务监听器
 * 有序
 *
 * @author zhangyu26
 * @date 2022/10/21
 */
@Component
public class MailPaymentStatusUpdateListener implements SmartApplicationListener {
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType == PaymentStatusUpdateEvent.class;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType == PaymentInfo.class;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("邮件服务, 收到支付状态更新的通知." + event + " - Thread:" + Thread.currentThread().getName());
    }

    // 排序
    @Override
    public int getOrder() {
        return 1;
    }
}
