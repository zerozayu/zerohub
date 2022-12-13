package com.zerohub.temp.listener;

import com.zerohub.temp.domain.PaymentInfo;
import com.zerohub.temp.event.PaymentStatusUpdateEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 库存服务监听器
 * 在有序监听器中可以看到xxx#getOrder()获取他们的执行顺序，数字越小执行的优先级越高。
 * 也就是当事件被触发后MailPaymentStatusUpdateListener会优于SmsPaymentStatusUpdateListener被执行。
 *
 * @author zhangyu26
 * @date 2022/10/21
 */
@Component
public class SmsPaymentStatusUpdateListener implements SmartApplicationListener {
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
        System.out.println("短信服务, 收到支付状态更新的通知. " + event + " - Thread: " + Thread.currentThread().getName());
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
