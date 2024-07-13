package com.houkunlin.system.common.aop;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author HouKunLin
 */
@Data
@AllArgsConstructor
public class RootObject {
    /**
     * 当前请求的方法
     */
    private final Method method;
    /**
     * 当前请求方法的参数列表
     */
    private final Object[] args;
    /**
     * 当前请求对象
     */
    private final Object target;
    /**
     * 当前请求对象class
     */
    private final Class<?> targetClass;
    /**
     * 请求结果对象
     */
    private final Object result;
    /**
     * 异常对象。该对象可能为null
     */
    private final Exception e;
}
