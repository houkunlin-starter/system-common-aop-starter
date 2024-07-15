package com.houkunlin.system.common.aop;

import java.lang.annotation.*;

/**
 * 标记文件下载资源名称
 *
 * @author HouKunLin
 */
@Inherited
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DownloadFileName {
}
