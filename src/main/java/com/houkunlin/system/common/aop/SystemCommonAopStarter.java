package com.houkunlin.system.common.aop;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
