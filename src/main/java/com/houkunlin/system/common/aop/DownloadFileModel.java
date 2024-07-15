package com.houkunlin.system.common.aop;

import java.lang.annotation.*;

/**
 * 标记文件下载资源类
 *
 * @author HouKunLin
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DownloadFileModel {
}
