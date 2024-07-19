package com.houkunlin.system.common.aop;

import com.alibaba.excel.write.builder.AbstractExcelWriterParameterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;

import java.lang.annotation.*;

/**
 * 拦截处理 Excel 写入。
 *
 * @author HouKunLin
 * @see AbstractExcelWriterParameterBuilder#registerWriteHandler(WriteHandler)
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DownloadExcelWriteHandler {

    /**
     * 拦截处理 Excel 写入。
     *
     * @see AbstractExcelWriterParameterBuilder#registerWriteHandler(WriteHandler)
     */
    Class<? extends WriteHandler>[] value() default {};
}
