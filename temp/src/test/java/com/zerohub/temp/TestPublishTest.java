package com.zerohub.temp;

import com.zerohub.temp.domain.PaymentInfo;
import com.zerohub.temp.event.PaymentStatusUpdateEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @author zhangyu26
 * @date 2022/10/24
 */
@SpringBootTest
class TestPublishTest {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @Test
    void test(){
        PaymentInfo paymentInfo = new PaymentInfo(1, "1");
        applicationEventPublisher.publishEvent(new PaymentStatusUpdateEvent(paymentInfo));
    }
}
