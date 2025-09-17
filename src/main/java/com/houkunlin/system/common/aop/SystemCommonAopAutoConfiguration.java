package com.houkunlin.system.common.aop;

import com.houkunlin.system.common.aop.file.DownloadFileAspect;
import com.houkunlin.system.common.aop.file.DownloadFileHandler;
import com.houkunlin.system.common.aop.ip.AllowIPAspect;
import com.houkunlin.system.common.aop.repeat.RepeatReadRequestFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author HouKunLin
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SystemCommonAopAutoConfiguration {

    @Bean
    public FilterRegistrationBean<RepeatReadRequestFilter> repeatReadRequestFilterRegistration() {
        FilterRegistrationBean<RepeatReadRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RepeatReadRequestFilter());
        registration.addUrlPatterns("/*");
        registration.setName("repeatableFilter");
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registration;
    }

    /**
     * 提供一个默认的字符串模板处理器
     *
     * @param parserContextObjectProvider SpEL 的解析上下文对象
     * @return 模板处理器
     */
    @ConditionalOnMissingBean
    @Bean
    public TemplateParserDefaultImpl aopTemplateParser(ObjectProvider<ParserContext> parserContextObjectProvider) {
        ParserContext parserContext = parserContextObjectProvider.getIfAvailable();
        if (parserContext == null) {
            parserContext = new TemplateParserContext();
        }
        return new TemplateParserDefaultImpl(parserContext);
    }

    @Bean
    public AllowIPAspect allowIPAspect(HttpServletRequest request, AllowIPConfigurationProperties allowIPConfigurationProperties) {
        return new AllowIPAspect(request, allowIPConfigurationProperties);
    }

    @Bean
    public DownloadFileAspect downloadFileAspect(
            TemplateParser templateParser,
            ObjectProvider<DownloadFileHandler> downloadFileHandlerObjectProvider,
            HttpServletRequest request,
            HttpServletResponse response) {
        DownloadFileHandler downloadFileHandler = downloadFileHandlerObjectProvider.getIfAvailable();
        if (downloadFileHandler == null) {
            downloadFileHandler = new DownloadFileHandler() {
                private static final Logger logger = LoggerFactory.getLogger(DownloadFileHandler.class);

                @Override
                public InputStream getFileInputStream(@NonNull String filename) throws IOException {
                    if (filename.isBlank()) {
                        return null;
                    }
                    InputStream inputStream = ClassPathUtil.getResourceAsStream(filename);
                    if (inputStream == null) {
                        logger.warn("使用默认的文件下载处理器，不支持读取 ClassPath 之外的文件，需要自行实现 DownloadFileHandler 接口功能。当前读取文件：{}", filename);
                    }
                    return inputStream;
                }
            };
        }
        return new DownloadFileAspect(templateParser, downloadFileHandler, request, response);
    }
}
