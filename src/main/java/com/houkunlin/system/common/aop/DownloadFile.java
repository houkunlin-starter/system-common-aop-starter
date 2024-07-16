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
     * 强制打包压缩
     */
    boolean forceCompress() default false;

    /**
     * 打包压缩类型
     */
    String compressFormat() default "zip";

    /**
     * 下载文件的文件内容类型
     */
    String contentType() default MediaType.APPLICATION_OCTET_STREAM_VALUE;

    /**
     * 浏览器预览，需要与 {@link #contentType()} 配合使用。
     */
    boolean inline() default false;

    /**
     * 浏览器预览，需要与 {@link #contentType()} 配合使用。
     * <p>自动识别请求参数 {@link #inlineParam()} 的值来设置 {@link #inline()} 参数。
     * <p>设置了该参数时，该参数的效果将比 {@link #inline()} 参数设置优先级高，如果请求中不存在该参数，则使用 {@link #inline()} 值。
     * <p><code>@DownloadFile(inlineParam = "inline")</code> 效果如下：
     * <p>请求 <code>/api/?inline</code> 时 inline 值为 true<br/>
     * 请求 <code>/api/?inline=true</code> 时 inline 值为 true<br/>
     * 请求 <code>/api/?inline=</code> 时 inline 值为 true<br/>
     * 请求 <code>/api/?inline=false</code> 时 inline 值为 false<br/>
     * 请求 <code>/api/?inline=1</code> 时 inline 值为 false<br/>
     * 请求 <code>/api/?inline=0</code> 时 inline 值为 false
     * </p>
     */
    String inlineParam() default "";

    /**
     * 响应头信息，
     * headers[0] 为请求头KEY， headers[1] 为请求头VALUE，两个为一组，以此类推
     */
    String[] headers() default {};
}
