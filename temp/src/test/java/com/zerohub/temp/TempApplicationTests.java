package com.zerohub.temp;

import com.zerohub.temp.domain.TestDomain;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TempApplicationTests {
    @Test
    void contextLoads() {
        TestDomain testDomain = new TestDomain();
        testDomain.setName("zhangyu");

    }

}
