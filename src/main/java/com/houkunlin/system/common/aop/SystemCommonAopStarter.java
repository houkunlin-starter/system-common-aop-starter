package com.houkunlin.system.common.aop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.http.HttpHeaders;

/**
 * @author HouKunLin
 */
@ComponentScan
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SystemCommonAopStarter {
    // private final HttpServletRequest request;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RepeatReadRequestFilter());
        registration.addUrlPatterns("/*");
        registration.setName("repeatableFilter");
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registration;
    }

    /**
     * 提供一个默认的字符串模板处理器
     *
     * @param parserContext SpEL 的解析上下文对象
     * @return 模板处理器
     */
    @ConditionalOnMissingBean
    @Bean
    public TemplateParserDefaultImpl aopTemplateParser(@Autowired(required = false) ParserContext parserContext) {
        return new TemplateParserDefaultImpl(parserContext == null ? new TemplateParserContext() : parserContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public PreventRepeatSubmitHandler preventRepeatSubmitHandler() {
        return new PreventRepeatSubmitHandlerImpl(HttpHeaders.AUTHORIZATION);
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestRateLimiterHandler requestRateLimiterHandler() {
        return new RequestRateLimiterHandlerImpl(HttpHeaders.AUTHORIZATION);
    }
}
