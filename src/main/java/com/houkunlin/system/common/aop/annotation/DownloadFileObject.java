package com.houkunlin.system.common.aop.annotation;

import java.lang.annotation.*;

/**
 * 标记文件下载资源对象
 *
 * @author HouKunLin
 */
@Inherited
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DownloadFileObject {
}
