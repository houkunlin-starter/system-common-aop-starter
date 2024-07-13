package com.houkunlin.system.common.aop;

import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * Excel/Word 导出下载处理器
 *
 * @author HouKunLin
 */
public interface DownloadPoiHandler {
    /**
     * 获取模板文件输入流
     *
     * @param templateName 模板文件
     * @return 文件输入流
     * @throws IOException 打开文件异常
     */
    InputStream getTemplate(@NonNull String templateName) throws IOException;

    /**
     * 获取当前 ClassPath 路径的资源
     *
     * @param templateName 模板名称、模板路径
     * @return 文件输入流
     * @throws IOException 打开文件异常
     */
    default InputStream getTemplateByClassPath(@NonNull String templateName) throws IOException {
        if (templateName.startsWith("classpath:")) {
            return new ClassPathResource(templateName.substring(10)).getInputStream();
        } else if (templateName.startsWith("classpath*:")) {
            return new ClassPathResource(templateName.substring(11)).getInputStream();
        }
        return null;
    }
}