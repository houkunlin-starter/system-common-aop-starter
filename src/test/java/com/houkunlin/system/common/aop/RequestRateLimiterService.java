package com.houkunlin.system.common.aop;

import com.houkunlin.system.common.aop.annotation.RequestRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class RequestRateLimiterService {
    private final AtomicInteger requestCount = new AtomicInteger(0);

    @RequestRateLimiter
    public void doWebRequest() {
        log.info("访问次数：{}", requestCount.incrementAndGet());
    }
}
