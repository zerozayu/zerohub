package com.zerohub.temp.listener;

import com.zerohub.temp.event.PaymentStatusUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 使用注解定义事件
 *
 * @author zhangyu26
 * @date 2022/10/24
 */
@Component
@Slf4j
public class OtherEventListener {

    @EventListener(classes = {PaymentStatusUpdateEvent.class})
    @Async
    @Order(0)
    public void eventListener(PaymentStatusUpdateEvent event){
        log.info("其他事件监听器触发" + event +" -  Thread: " + Thread.currentThread().getName());
    }
}
