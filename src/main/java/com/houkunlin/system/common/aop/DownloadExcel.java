package com.houkunlin.system.common.aop;

import cn.idev.excel.metadata.AbstractParameterBuilder;
import cn.idev.excel.support.ExcelTypeEnum;
import cn.idev.excel.write.builder.AbstractExcelWriterParameterBuilder;
import cn.idev.excel.write.builder.ExcelWriterBuilder;
import cn.idev.excel.write.style.DefaultStyle;

import java.io.File;
import java.io.InputStream;
import java.lang.annotation.*;
import java.nio.charset.Charset;

/**
 * Excel 导出下载
 *
 * @author HouKunLin
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DownloadExcel {
    /**
     * 水平渲染配置的 KEY 后缀。主要用在接口方法返回 map 类型数据中存在 List 列表数据进行模板填充时使用。
     * <p>例如，返回了如下 map 信息</p>
     * <pre><code>
     * Map&lt;String, Object&gt; map = Map.of("name", "姓名",
     *                                  "list1", List.of(1,2,3),
     *                                  "list2", new FillWrapper(List.of(1,2,3))
     *                                  );
     * </code></pre>
     * <p>当我们想要对 list1 和 list2 列表进行水平方向渲染时，则需要做如下配合</p>
     * <pre><code>
     * Map&lt;String, Object&gt; map = Map.of("name", "姓名",
     *                                  "list1", List.of(1,2,3),
     *                                  "list1.direction.horizontal", true,
     *                                  "list2", new FillWrapper(List.of(1,2,3)),
     *                                  "list2.direction.horizontal", "true"
     *                                  );
     * </code></pre>
     */
    String HORIZONTAL_SUFFIX = ".direction.horizontal";

    /**
     * 下载的文件名
     * <p>可自行实现 {@link TemplateParser} 接口来解析字符串模板，默认提供 {@link TemplateParserDefaultImpl} 来支持 SpEL 模板表达式解析
     */
    String filename();

    /**
     * 下载的 Excel 类型
     *
     * @see ExcelTypeEnum
     */
    ExcelTypeEnum excelType() default ExcelTypeEnum.XLSX;

    /**
     * Excel 表格的工作簿名称
     *
     * @see ExcelWriterBuilder#sheet(String)
     */
    String sheetName() default "Sheet1";

    /**
     * 是否在内存中完成。false：写入临时文件，true：在内存中完成
     *
     * @see ExcelWriterBuilder#inMemory(Boolean)
     */
    boolean inMemory() default false;

    /**
     * 数据的类型对象
     *
     * @see AbstractParameterBuilder#head(Class)
     */
    Class<?> dataClass() default Object.class;

    /**
     * 只有在写入为 CSV 文件时有效
     *
     * @see ExcelWriterBuilder#charset(Charset)
     */
    String charset() default "UTF-8";

    /**
     * 文件加密密码
     *
     * @see ExcelWriterBuilder#password(String)
     */
    String password() default "";

    /**
     * 模板文件。
     * 支持写法格式：
     * <ol>
     *     <li>默认实现：classpath:template.xlsx</li>
     *     <li>需自行实现：file:template.xlsx</li>
     *     <li>需自行实现：oss:template.xlsx</li>
     * </ol>
     * 具体支持的写法格式请参考 {@link DownloadPoiHandler} 实现细节
     *
     * @see ExcelWriterBuilder#withTemplate(String)
     * @see ExcelWriterBuilder#withTemplate(File)
     * @see ExcelWriterBuilder#withTemplate(InputStream)
     */
    String withTemplate() default "";

    /**
     * excel中时间是存储1900年起的一个双精度浮点数，但是有时候默认开始日期是1904，所以设置这个值改成默认1904年开始。
     * 如果 Date 使用 1904 窗口化，则为 true，如果使用 1900 日期窗口化，则为 false。
     *
     * @see AbstractParameterBuilder#use1904windowing(Boolean)
     */
    boolean use1904windowing() default false;

    /**
     * 是否使用默认的样式
     *
     * @see AbstractExcelWriterParameterBuilder#useDefaultStyle(Boolean)
     * @see DefaultStyle
     */
    boolean useDefaultStyle() default true;

    /**
     * 是否需要写入头到excel
     *
     * @see AbstractExcelWriterParameterBuilder#needHead(Boolean)
     */
    boolean needHead() default true;
}
