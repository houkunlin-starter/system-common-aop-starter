package com.houkunlin.system.common.aop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;

/**
 * @author HouKunLin
 */
@AutoConfiguration(after = RedisAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SystemCommonAopRedisConfiguration {

    @Bean
    public PreventRepeatSubmitAspect preventRepeatSubmitAspect(StringRedisTemplate stringRedisTemplate, ObjectProvider<PreventRepeatSubmitHandler> preventRepeatSubmitHandlerObjectProvider) {
        PreventRepeatSubmitHandler preventRepeatSubmitHandler = preventRepeatSubmitHandlerObjectProvider.getIfAvailable();
        if (preventRepeatSubmitHandler == null) {
            preventRepeatSubmitHandler = new PreventRepeatSubmitHandlerImpl(HttpHeaders.AUTHORIZATION);
        }
        return new PreventRepeatSubmitAspect(stringRedisTemplate, preventRepeatSubmitHandler);
    }

    @Bean
    public RequestRateLimiterAspect requestRateLimiterAspect(StringRedisTemplate stringRedisTemplate, ObjectProvider<RequestRateLimiterHandler> requestRateLimiterHandlerObjectProvider) {
        RequestRateLimiterHandler requestRateLimiterHandler = requestRateLimiterHandlerObjectProvider.getIfAvailable();
        if (requestRateLimiterHandler == null) {
            requestRateLimiterHandler = new RequestRateLimiterHandlerImpl(HttpHeaders.AUTHORIZATION);
        }
        return new RequestRateLimiterAspect(stringRedisTemplate, requestRateLimiterHandler);
    }
}
