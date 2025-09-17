package com.houkunlin.system.common.aop.repeat;

import com.houkunlin.system.common.aop.annotation.PreventRepeatSubmit;
import org.aspectj.lang.JoinPoint;

/**
 * 防止表单重复提交
 *
 * @author HouKunLin
 */
public interface PreventRepeatSubmitHandler {
    /**
     * 生成 Redis 唯一 KEY。
     * 根据当前请求内容、或者某些参数来生成唯一 KEY ，如果内容相同，则KEY相同，以此来判断是否重复请求
     *
     * @param point      切入点
     * @param annotation 注解
     * @return Redis 唯一 KEY
     */
    String getSignatureKey(JoinPoint point, PreventRepeatSubmit annotation);
}
