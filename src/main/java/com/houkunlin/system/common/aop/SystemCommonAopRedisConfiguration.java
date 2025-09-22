package com.houkunlin.system.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.houkunlin.system.common.aop.limit.RequestRateLimiterAspect;
import com.houkunlin.system.common.aop.limit.RequestRateLimiterHandler;
import com.houkunlin.system.common.aop.limit.RequestRateLimiterHandlerImpl;
import com.houkunlin.system.common.aop.repeat.PreventRepeatSubmitAspect;
import com.houkunlin.system.common.aop.repeat.PreventRepeatSubmitHandler;
import com.houkunlin.system.common.aop.repeat.PreventRepeatSubmitHandlerImpl;
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
    public PreventRepeatSubmitAspect preventRepeatSubmitAspect(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper, ObjectProvider<PreventRepeatSubmitHandler> preventRepeatSubmitHandlerObjectProvider) {
        PreventRepeatSubmitHandler preventRepeatSubmitHandler = preventRepeatSubmitHandlerObjectProvider.getIfAvailable();
        if (preventRepeatSubmitHandler == null) {
            preventRepeatSubmitHandler = new PreventRepeatSubmitHandlerImpl(objectMapper, HttpHeaders.AUTHORIZATION);
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
