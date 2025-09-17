package com.houkunlin.system.common.aop.poi;

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
}
