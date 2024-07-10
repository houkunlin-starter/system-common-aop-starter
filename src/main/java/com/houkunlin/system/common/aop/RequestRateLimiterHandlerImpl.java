package com.houkunlin.system.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 请求限流
 *
 * @author HouKunLin
 */
@Slf4j
@Data
@RequiredArgsConstructor
public class RequestRateLimiterHandlerImpl implements RequestRateLimiterHandler {
    private final String authorizationHeaderName;
    private String prefix = "system:aop:rate-limiter:";

    @Override
    public String getSignatureKey(JoinPoint point, RequestRateLimiter annotation) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        MethodSignature signature = (MethodSignature) point.getSignature();
        baos.writeBytes(signature.getDeclaringType().getName().getBytes(StandardCharsets.UTF_8));
        baos.write('.');
        baos.writeBytes(signature.getMethod().getName().getBytes(StandardCharsets.UTF_8));

        HttpServletRequest request = RequestUtil.getRequest();
        if (request != null) {
            switch (annotation.limitType()) {
                case IP:
                    baos.writeBytes(RequestUtil.getRequestIp(request).getBytes(StandardCharsets.UTF_8));
                    break;
                case USER:
                    String authorization = request.getHeader(authorizationHeaderName);
                    if (authorization != null) {
                        baos.writeBytes(authorization.getBytes(StandardCharsets.UTF_8));
                    }
                    break;
                default:
                    break;
            }
        }

        String hex = DigestUtils.md5DigestAsHex(baos.toByteArray());

        String key = annotation.key();
        if (key.isBlank()) {
            return prefix + hex;
        }
        return prefix + key + ":" + hex;
    }
}
