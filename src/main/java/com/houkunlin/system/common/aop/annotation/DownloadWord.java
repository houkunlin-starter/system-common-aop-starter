package com.houkunlin.system.common.aop.annotation;

import com.houkunlin.system.common.aop.TemplateParser;
import com.houkunlin.system.common.aop.TemplateParserDefaultImpl;
import com.houkunlin.system.common.aop.poi.DownloadPoiHandler;

import java.lang.annotation.*;

/**
 * Word 模板渲染导出下载
 *
 * @author HouKunLin
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DownloadWord {
    /**
     * 下载的文件名。
     * <p>文件名不包含后缀名时将使用 {@link #withTemplate()} 的后缀名，假如 {@link #filename()} 和 {@link #withTemplate()}都没有后缀名时则默认 .doc 后缀名
     * <p>可自行实现 {@link TemplateParser} 接口来解析字符串模板，默认提供 {@link TemplateParserDefaultImpl} 来支持 SpEL 模板表达式解析
     */
    String filename();

    /**
     * 下载文件的文件内容类型
     */
    String contentType() default "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    /**
     * 模板文件。
     * 支持写法格式：
     * <ol>
     *     <li>默认实现：classpath:template.xlsx</li>
     *     <li>需自行实现：file:template.xlsx</li>
     *     <li>需自行实现：oss:template.xlsx</li>
     * </ol>
     * 具体支持的写法格式请参考 {@link DownloadPoiHandler} 实现细节
     */
    String withTemplate();
}
