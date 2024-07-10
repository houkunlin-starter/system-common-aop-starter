package com.houkunlin.system.common.aop;

import org.aspectj.lang.JoinPoint;

/**
 * 请求限流
 *
 * @author HouKunLin
 */
public interface RequestRateLimiterHandler {
    /**
     * 生成 Redis 唯一 KEY。
     * 根据当前请求内容、或者某些参数来生成限流 KEY ，用该 KEY 来作为限流判断依据
     *
     * @param point      切入点
     * @param annotation 注解
     * @return Redis 唯一 KEY
     */
    String getSignatureKey(JoinPoint point, RequestRateLimiter annotation);
}
