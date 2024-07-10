package com.houkunlin.system.common.aop;

import lombok.Getter;

/**
 * 请求限流
 *
 * @author HouKunLin
 */
@Getter
public class RequestRateLimiterException extends RuntimeException {
    private final RequestRateLimiter annotation;

    public RequestRateLimiterException(RequestRateLimiter annotation) {
        super(annotation.message());
        this.annotation = annotation;
    }
}
