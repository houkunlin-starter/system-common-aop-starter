package com.houkunlin.system.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 表达式（模板）解析器
 *
 * @author HouKunLin
 */
public interface TemplateParser<CONTEXT> {
    /**
     * 创建表达式（模板）上下文对象；构建变量内容
     *
     * @param pjp       AOP切点对象
     * @param result    实际执行方法的返回值
     * @param exception 实际执行方法的抛出异常对象
     * @return 表达式（模板）上下文对象
     */
    CONTEXT createContext(ProceedingJoinPoint pjp, Object result, Exception exception);

    /**
     * 判断字符串是否是表达式（模板）内容
     *
     * @param template 字符串
     * @return 结果
     */
    boolean isTemplate(String template);

    /**
     * 解析表达式（模板）内容
     *
     * @param template 表达式（模板）内容
     * @param context  上下文对象
     * @return 解析结果
     */
    String parseTemplate(String template, CONTEXT context);
}
