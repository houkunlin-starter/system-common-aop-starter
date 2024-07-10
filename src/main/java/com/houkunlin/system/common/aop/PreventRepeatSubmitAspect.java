package com.houkunlin.system.common.aop;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 防止表单重复提交
 *
 * @author HouKunLin
 * @see PreventRepeatSubmit
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PreventRepeatSubmitAspect {
    private final StringRedisTemplate redisTemplate;
    private final PreventRepeatSubmitHandler preventRepeatSubmitHandler;

    @Before("@annotation(annotation)")
    public void doBefore(JoinPoint point, PreventRepeatSubmit annotation) {
        String signatureKey = preventRepeatSubmitHandler.getSignatureKey(point, annotation);

        Boolean b = redisTemplate.opsForValue().setIfAbsent(signatureKey, "1", Duration.ofSeconds(annotation.interval()));
        if (b == null || !b) {
            throw new PreventRepeatSubmitException(annotation);
        }
    }

    @PostConstruct
    public void post() {
        log.info("{} 准备就绪", getClass());
    }
}
