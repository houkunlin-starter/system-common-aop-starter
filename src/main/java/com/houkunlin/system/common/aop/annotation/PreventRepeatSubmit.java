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

    /**
     * 使用方法参数对象的 hashCode 来做重复判断。如果为 true ，则 {@link #tryJson()} 参数设置失效
     *
     * @return Default：false
     */
    boolean useMethodArgs() default false;

    /**
     * 请求路径的查询参数字符串是否加入重复内容判断。如果启用，前端可以通过传递时间戳来绕过重复内容判断
     *
     * @return Default：false
     */
    boolean useQueryString() default false;

    /**
     * 当请求内容为 JSON 内容时，是否尝试反序列化后再序列化来进行重复内容判断，防止 JSON 内容加入无意义空格内容等情况，不过还是可以通过插入无意义的key来绕过重复内容判断
     *
     * @return Default：false
     */
    boolean tryJson() default false;
}
