package com.houkunlin.system.common.aop;

/**
 * 限流类型
 *
 * @author HouKunLin
 */
public enum LimitType {
    /**
     * 默认策略全局限流
     */
    DEFAULT,

    /**
     * 根据请求者IP进行限流
     */
    IP,

    /**
     * 根据请求用户进行限流
     */
    USER,
}
