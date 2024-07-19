package com.houkunlin.system.common.aop;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.AbstractParameterBuilder;

import java.lang.annotation.*;

/**
 * 自定义类型转换覆盖默认值。
 *
 * @author HouKunLin
 * @see AbstractParameterBuilder#registerConverter(Converter)
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DownloadExcelConverter {

    /**
     * 自定义类型转换覆盖默认值。
     *
     * @see AbstractParameterBuilder#registerConverter(Converter)
     */
    Class<? extends Converter>[] value() default {};
}
