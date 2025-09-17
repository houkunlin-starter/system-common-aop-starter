package com.houkunlin.system.common.aop.annotation;

import java.lang.annotation.*;

/**
 * 防止表单重复提交
 *
 * @author HouKunLin
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreventRepeatSubmit {
    String DEFAULT_MESSAGE = "不允许重复提交，请稍候再试";

    /**
     * 在某些个特殊业务场景下二次分类的键名
     *
     * @return key
     */
    String key() default "";

    /**
     * 间隔时间（单位：秒），小于此时间视为重复提交
     */
    int interval() default 5;

    /**
     * 提示消息
     */
    String message() default DEFAULT_MESSAGE;
}
