package com.houkunlin.system.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 允许指定IP访问接口
 *
 * @author HouKunLin
 * @see AllowIP
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AllowIPAspect implements InitializingBean {
    private final HttpServletRequest request;
    private final AllowIPConfigurationProperties allowIPConfigurationProperties;
    /**
     * 内部定义的IP列表
     */
    private final Map<String, List<long[]>> innerIp = new HashMap<>();
    /**
     * 用户自定义的IP列表
     */
    private final Map<String, List<long[]>> customIp = new HashMap<>();

    @Before("@annotation(AllowIP) || @within(AllowIP)")
    public void doBefore(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        AllowIP annotation = signature.getMethod().getAnnotation(AllowIP.class);
        if (annotation == null) {
            Class<?> declaringType = signature.getDeclaringType();
            annotation = declaringType.getAnnotation(AllowIP.class);
        }
        String requestIp = RequestUtil.getRequestIp(request);
        matchIp(requestIp, annotation);
    }

    private void matchIp(String requestIp, AllowIP annotation) {
        String key = annotation.key();
        if (!key.isBlank()) {
            if (!matchIp(requestIp, key)) {
                throw new AllowIPException("不在访问IP白名单中").setCurrentIp(requestIp).setHasAllowIp(true);
            }
        } else if (annotation.ipList().length > 0) {
            String[] ipList = annotation.ipList();
            if (!matchIp(requestIp, ipList)) {
                throw new AllowIPException("不在访问IP白名单中").setCurrentIp(requestIp).setHasAllowIp(true);
            }
        } else {
            throw new AllowIPException("未设置访问IP白名单中").setCurrentIp(requestIp).setHasAllowIp(false);
        }
    }

    private boolean matchIp(String requestIp, String key) {
        List<long[]> ipRangeList = customIp.get(key);
        if (ipRangeList == null) {
            ipRangeList = innerIp.get(key);
        }
        if (ipRangeList == null || ipRangeList.isEmpty()) {
            throw new AllowIPException("未设置访问IP白名单中").setCurrentIp(requestIp).setHasAllowIp(false);
        }
        long currIp = IpUtil.ip2long(requestIp);
        if (currIp < 0) {
            return false;
        }
        for (long[] ipRange : ipRangeList) {
            if (IpUtil.isIpAtNetwork(currIp, ipRange)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchIp(String requestIp, String[] ipList) {
        if (ipList == null || ipList.length == 0) {
            throw new AllowIPException("未设置访问IP白名单中").setCurrentIp(requestIp).setHasAllowIp(false);
        }
        long currIp = IpUtil.ip2long(requestIp);
        if (currIp < 0) {
            return false;
        }
        for (String ipStr : ipList) {
            if (IpUtil.isIpAtNetwork(currIp, ipStr)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        long[] ipA = IpUtil.parseIpRange("10.0.0.0/8");
        long[] ipB = IpUtil.parseIpRange("172.16.0.0/12");
        long[] ipC = IpUtil.parseIpRange("192.168.0.0/16");
        long[] ipLocal = IpUtil.parseIpRange("127.0.0.0/8");

        assert ipA != null;
        assert ipB != null;
        assert ipC != null;
        assert ipLocal != null;

        innerIp.put(AllowIP.INNER_IP_A, List.of(ipA));
        innerIp.put(AllowIP.INNER_IP_B, List.of(ipB));
        innerIp.put(AllowIP.INNER_IP_C, List.of(ipC));
        innerIp.put(AllowIP.INNER_IP_LOCAL, List.of(ipLocal));
        innerIp.put(AllowIP.INNER_IP, List.of(ipA, ipB, ipC, ipLocal));

        Map<String, Set<String>> allowIp = allowIPConfigurationProperties.getAllowIp();
        if (allowIp == null || allowIp.isEmpty()) {
            return;
        }
        allowIp.forEach((key, ipValues) -> {
            List<long[]> list = customIp.computeIfAbsent(key, s -> new ArrayList<>());
            for (String ipValue : ipValues) {
                List<long[]> innerIps = innerIp.get(ipValue);
                if (innerIps != null) {
                    list.addAll(innerIps);
                } else if (StringUtils.hasText(ipValue)) {
                    long[] ipRange = IpUtil.parseIpRange(ipValue);
                    if (ipRange != null) {
                        list.add(ipRange);
                    } else if (log.isWarnEnabled()) {
                        log.warn("无法解析 {} -> {} IP地址", key, ipValue);
                    }
                }
            }
        });
    }
}
