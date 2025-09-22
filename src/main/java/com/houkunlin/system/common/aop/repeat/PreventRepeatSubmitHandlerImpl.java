package com.houkunlin.system.common.aop.repeat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.houkunlin.system.common.aop.RequestUtil;
import com.houkunlin.system.common.aop.annotation.PreventRepeatSubmit;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.DigestUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 防止表单重复提交
 *
 * @author HouKunLin
 */
@Slf4j
@Data
@RequiredArgsConstructor
public class PreventRepeatSubmitHandlerImpl implements PreventRepeatSubmitHandler {
    private final ObjectMapper objectMapper;
    private final String authorizationHeaderName;
    private String prefix = "system:aop:repeat-submit:";

    @Override
    public String getSignatureKey(JoinPoint point, PreventRepeatSubmit annotation) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        HttpServletRequest request = RequestUtil.getRequest();
        if (request != null) {
            appendAuthorization(baos, request);
        }
        if (!annotation.useMethodArgs() && request != null) {
            String requestURI = request.getRequestURI();
            baos.writeBytes(requestURI.getBytes(StandardCharsets.UTF_8));
            appendQueryString(baos, request, annotation);
            byte[] requestBody = getRequestBody(request);
            if (requestBody != null) {
                if (annotation.tryJson() && request.getContentType().indexOf("json", 12) != -1) {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(requestBody);
                        objectMapper.writeValue(baos, jsonNode);
                    } catch (IOException e) {
                        baos.writeBytes(requestBody);
                        log.error("【防止重复提交】尝试解析JSON内容时失败", e);
                    }
                } else {
                    baos.writeBytes(requestBody);
                }
            } else if (log.isWarnEnabled()) {
                log.warn("【防止重复提交】无法读取请求体内容，请求体对象：{}", request.getClass());
            }
        } else {
            if (request != null) {
                appendQueryString(baos, request, annotation);
            }
            MethodSignature signature = (MethodSignature) point.getSignature();
            baos.writeBytes(signature.getDeclaringTypeName().getBytes(StandardCharsets.UTF_8));
            baos.write('.');
            baos.writeBytes(signature.getName().getBytes(StandardCharsets.UTF_8));
            Object[] args = point.getArgs();
            if (args != null) {
                baos.write('(');
                for (Object arg : args) {
                    baos.write(arg.hashCode());
                    baos.write(',');
                }
                baos.write(')');
            }
        }

        String hex = DigestUtils.md5DigestAsHex(baos.toByteArray());

        String key = annotation.key();
        if (key.isBlank()) {
            return prefix + hex;
        }
        return prefix + key + ":" + hex;
    }

    protected void appendAuthorization(ByteArrayOutputStream baos, HttpServletRequest request) {
        String authorization = request.getHeader(authorizationHeaderName);
        if (authorization != null) {
            baos.writeBytes(authorization.getBytes(StandardCharsets.UTF_8));
        }
    }

    protected void appendQueryString(ByteArrayOutputStream baos, HttpServletRequest request, PreventRepeatSubmit annotation) {
        if (!annotation.useQueryString()) {
            return;
        }
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            baos.writeBytes(queryString.getBytes(StandardCharsets.UTF_8));
        }
    }

    protected byte[] getRequestBody(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper wrapper) {
            return wrapper.getContentAsByteArray();
        } else if (request instanceof RepeatReadRequestWrapper wrapper) {
            return wrapper.getBodyBytes();
        } else if (request instanceof HttpServletRequestWrapper wrapper) {
            ServletRequest servletRequest = wrapper.getRequest();
            if (servletRequest instanceof HttpServletRequest httpServletRequest) {
                // log.info("writeRequestBody: {} -> {}", wrapper.getClass(), servletRequest.getClass());
                return getRequestBody(httpServletRequest);
            }
        }
        return null;
    }
}
