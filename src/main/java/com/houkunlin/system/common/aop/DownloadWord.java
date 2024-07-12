package com.houkunlin.system.common.aop;

import org.springframework.http.MediaType;

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
     * <p>
     * 文件名不包含后缀名时将使用 {@link #withTemplate()} 的后缀名，假如 {@link #filename()} 和 {@link #withTemplate()}都没有后缀名时则默认 .doc 后缀名
     */
    String filename();

    /**
     * 下载文件的文件内容类型
     */
    String contentType() default MediaType.APPLICATION_OCTET_STREAM_VALUE;

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
