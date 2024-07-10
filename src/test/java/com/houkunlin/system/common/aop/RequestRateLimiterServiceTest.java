package com.houkunlin.system.common.aop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RequestRateLimiterServiceTest {
    @Autowired
    private RequestRateLimiterService requestRateLimiterService;

    @Test
    void doWebRequest() {
        for (int i = 0; i < 120; i++) {
            requestRateLimiterService.doWebRequest();
        }
        RequestRateLimiterException exception = assertThrows(
                RequestRateLimiterException.class,
                () -> requestRateLimiterService.doWebRequest(),
                "预期会抛出一个限流异常，但是并没有抛出异常"
        );
        assertEquals(exception.getMessage(), RequestRateLimiter.DEFAULT_MESSAGE);
    }
}
