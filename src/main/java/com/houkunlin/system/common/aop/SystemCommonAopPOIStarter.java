package com.houkunlin.system.common.aop;

import lombok.RequiredArgsConstructor;
import org.apache.poi.POIDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * Excel 导出下载配置
 *
 * @author HouKunLin
 */
@ConditionalOnClass(POIDocument.class)
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SystemCommonAopPOIStarter {

    @Bean
    @ConditionalOnMissingBean
    public DownloadPoiHandler downloadPoiHandler() {
        return new DownloadPoiHandler() {
            private static final Logger logger = LoggerFactory.getLogger(DownloadPoiHandler.class);

            @Override
            public InputStream getTemplate(@NonNull String templateName) throws IOException {
                if (templateName.isBlank()) {
                    return null;
                }
                InputStream inputStream = getTemplateByClassPath(templateName);
                if (inputStream == null) {
                    logger.warn("使用默认的 Excel/Word 模板处理器，不支持读取 ClassPath 之外的文件模板，需要自行实现 DownloadPoiHandler 接口功能。当前读取模板：{}", templateName);
                }
                return inputStream;
            }
        };
    }
}
