package com.houkunlin.system.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 请求工具类（获取当前请求的IP地址）
 *
 * @author HouKunLin
 */
public class RequestUtil {
    public static final Pattern IP_PATTERN = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");
    private static final String[] IP_KEYS = new String[]{"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP"};
    private static final String[] DEFAULT_LOCAL_IP6 = new String[]{"0:0:0:0:0:0:0:1", "::1"};

    private RequestUtil() {
    }

    /**
     * 获得当前请求对象
     *
     * @return 请求对象
     */
    public static HttpServletRequest getRequest() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof final ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    /**
     * 获得当前请求对象
     *
     * @return 请求对象
     */
    public static Optional<HttpServletRequest> getRequestOpt() {
        return Optional.ofNullable(getRequest());
    }

    /**
     * 获取当前请求的IP（X-Forwarded-For 有多个IP时只返回第一个IP）
     *
     * @return IP
     */
    public static String getRequestIp() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return getRequestIp(request);
        }
        return null;
    }

    /**
     * 获取当前请求的IP（X-Forwarded-For 有值时可能存在多个IP信息）
     *
     * @return IP
     */
    public static String getRequestIps() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return getRequestIps(request);
        }
        return null;
    }

    /**
     * 获取当前请求的IP
     *
     * @return IP
     */
    public static Optional<String> getRequestIpOpt() {
        return Optional.ofNullable(getRequestIp());
    }

    /**
     * 获取当前请求的IP（X-Forwarded-For 有值时可能存在多个IP信息）
     *
     * @return IP
     */
    public static Optional<String> getRequestIpsOpt() {
        return Optional.ofNullable(getRequestIps());
    }

    /**
     * 获取请求的IP（X-Forwarded-For 有多个IP时只返回第一个IP）
     *
     * @param request 请求对象
     * @return IP
     */
    public static String getRequestIp(HttpServletRequest request) {
        String ip = getRequestIps(request);
        return obtainIp(ip);
    }

    /**
     * 获取请求的IP（X-Forwarded-For 有值时可能存在多个IP信息）
     *
     * @param request 请求对象
     * @return IP
     */
    public static String getRequestIps(HttpServletRequest request) {
        String ip = null;
        boolean hasIp = false;
        for (final String ipKey : IP_KEYS) {
            ip = request.getHeader(ipKey);
            hasIp = hasIp(ip);
            if (hasIp) {
                break;
            }
        }
        if (!hasIp) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 判断IP是否为空。是否拥有IP
     *
     * @param ip IP内容
     * @return 结果
     */
    private static boolean hasIp(String ip) {
        return StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip);
    }

    /**
     * 获取IP信息（去除本机的IPv6地址），只取第一个IP信息
     *
     * @param ip IP内容（有可能是以逗号分隔的字符串）
     * @return IP信息
     */
    private static String obtainIp(String ip) {
        String realIp = ip;
        if (realIp.contains(",")) {
            realIp = ip.split(",")[0];
        }
        for (final String defaultLocalIp : DEFAULT_LOCAL_IP6) {
            if (defaultLocalIp.equals(realIp)) {
                return "127.0.0.1";
            }
        }
        if (IP_PATTERN.matcher(realIp).matches()) {
            return realIp;
        }
        return "0.0.0.0";
    }
}
