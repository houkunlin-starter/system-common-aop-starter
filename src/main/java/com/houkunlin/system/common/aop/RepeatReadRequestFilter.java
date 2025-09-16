package com.houkunlin.system.common.aop;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

public class RepeatReadRequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = request;
        String contentType = request.getContentType();
        if (contentType != null && request instanceof HttpServletRequest httpServletRequest && isText(contentType)) {
            requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        }
        chain.doFilter(requestWrapper, response);
    }

    private boolean isText(String contentType) {
        if (contentType.startsWith("text/")) {
            return true;
        }
        if (!contentType.startsWith("application/")) {
            return false;
        }
        if (contentType.indexOf("json", 12) != -1) {
            return true;
        }
        if (contentType.indexOf("xml", 12) != -1) {
            return true;
        }
        if ("application/octet-stream".equals(contentType)) {
            return false;
        }
        return contentType.indexOf("x-www-form-urlencoded", 12) != -1;
    }
}
