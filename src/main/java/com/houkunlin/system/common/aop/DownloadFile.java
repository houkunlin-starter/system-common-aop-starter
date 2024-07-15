package com.houkunlin.system.common.aop;

import org.springframework.http.MediaType;

import java.lang.annotation.*;

/**
 * 文件下载
 *
 * @author HouKunLin
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DownloadFile {
    /**
     * 单个文件名称，或者压缩包名称
     * <p>可自行实现 {@link TemplateParser} 接口来解析字符串模板，默认提供 {@link TemplateParserDefaultImpl} 来支持 SpEL 模板表达式解析
     */
    String filename() default "";

    /**
     * 下载资源来源
     * 支持写法格式：
     * <ol>
     *     <li>默认实现：classpath:template.xlsx</li>
     *     <li>需自行实现：file:template.xlsx</li>
     *     <li>需自行实现：oss:template.xlsx</li>
     * </ol>
     * 具体支持的写法格式请参考 {@link DownloadFileHandler} 实现细节
     */
    String source() default "";

    /**
     * 下载文件的文件内容类型
     */
    String contentType() default MediaType.APPLICATION_OCTET_STREAM_VALUE;

    /**
     * 强制打包压缩
     */
    boolean forceCompress() default false;

    /**
     * 打包压缩类型
     */
    String compressFormat() default "zip";
}
