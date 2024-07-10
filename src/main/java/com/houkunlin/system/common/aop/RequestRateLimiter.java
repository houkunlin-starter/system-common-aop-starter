package com.houkunlin.system.common.aop;

import java.lang.annotation.*;

/**
 * 请求限流
 *
 * @author HouKunLin
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestRateLimiter {
    String DEFAULT_MESSAGE = "访问量太多，服务器繁忙，请稍候再试";

    /**
     * 限流key：在某些个特殊业务场景下二次分类的键名
     */
    String key() default "";

    /**
     * 限流时间，单位：秒
     */
    int interval() default 60;

    /**
     * 限流次数
     */
    int limit() default 120;

    /**
     * 是否使用 Redis 锁，
     * 未使用 Redis 锁的情况下，遇到并发场景时实际访问量可能会超出 {@link #limit()} 值（具体会超出多少由多种因素决定：服务器性能、网络IO速度等等）。
     * 使用锁时可以限制最大访问次数为 {@link #limit()}，请求并发数可能会降低，但是会相对均匀的允许请求通过（能拿到锁才有机会成功访问）。
     */
    boolean useLock() default false;

    /**
     * 提示消息
     */
    String message() default DEFAULT_MESSAGE;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;

    /**
     * 限流方法
     */
    LimitMethod method() default LimitMethod.METHOD1;
}
