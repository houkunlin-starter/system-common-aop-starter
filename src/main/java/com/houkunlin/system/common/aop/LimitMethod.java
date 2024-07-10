package com.houkunlin.system.common.aop;

/**
 * 限流方法
 *
 * @author HouKunLin
 */
public enum LimitMethod {
    /**
     * 基于时间范围内记录请求日志的数量统计。使用 Redis ZSet 实现
     */
    METHOD1,

    /**
     * 基于滑动窗口的请求数量统计（时间分片统计请求数量）。使用 Redis ZSet 实现
     */
    METHOD2,

    /**
     * 基于滑动窗口的请求数量统计（时间分片统计请求数量）。使用 Redis Hash 实现
     */
    METHOD3,
}
