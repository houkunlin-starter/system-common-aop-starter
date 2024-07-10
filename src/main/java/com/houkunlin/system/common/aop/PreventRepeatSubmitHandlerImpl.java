package com.houkunlin.system.common.aop;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayOutputStream;
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
    private final String authorizationHeaderName;
    private String prefix = "system:aop:repeat-submit:";

    @Override
    public String getSignatureKey(JoinPoint point, PreventRepeatSubmit annotation) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        HttpServletRequest request = RequestUtil.getRequest();
        if (request != null) {
            String authorization = request.getHeader(authorizationHeaderName);
            String requestURI = request.getRequestURI();
            if (authorization != null) {
                baos.writeBytes(authorization.getBytes(StandardCharsets.UTF_8));
            }
            baos.writeBytes(requestURI.getBytes(StandardCharsets.UTF_8));
            boolean useBody = writeRequestBody(baos, request);
            if (!useBody && log.isWarnEnabled()) {
                log.warn("【防止重复提交】无法读取请求体内容，请求体对象：{}", request.getClass());
            }
        } else {
            MethodSignature signature = (MethodSignature) point.getSignature();
            baos.writeBytes(signature.getDeclaringType().getName().getBytes(StandardCharsets.UTF_8));
            baos.write('.');
            baos.writeBytes(signature.getMethod().getName().getBytes(StandardCharsets.UTF_8));
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

    private boolean writeRequestBody(ByteArrayOutputStream baos, HttpServletRequest request) {
        if (request instanceof RepeatReadRequestWrapper wrapper) {
            baos.writeBytes(wrapper.getBodyBytes());
            return true;
        } else if (request instanceof HttpServletRequestWrapper wrapper) {
            ServletRequest servletRequest = wrapper.getRequest();
            if (servletRequest instanceof HttpServletRequest httpServletRequest) {
                // log.info("writeRequestBody: {} -> {}", wrapper.getClass(), servletRequest.getClass());
                return writeRequestBody(baos, httpServletRequest);
            }
        }
        return false;
    }
}
