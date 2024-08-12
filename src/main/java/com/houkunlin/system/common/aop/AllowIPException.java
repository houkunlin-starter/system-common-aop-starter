package com.houkunlin.system.common.aop;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 允许指定IP访问接口
 *
 * @author HouKunLin
 */
@Accessors(chain = true)
@Setter
@Getter
public class AllowIPException extends RuntimeException {
    /**
     * 当前请求IP。当前用来匹配的IP。
     */
    private String currentIp;
    /**
     * <p>是否设置了白名单IP。</p>
     * <p>true：有白名单IP，但是当前请求IP未在白名单IP内</p>
     * <p>false：没有白名单IP，直接失败</p>
     */
    private boolean hasAllowIp = false;

    public AllowIPException() {
    }

    public AllowIPException(String message) {
        super(message);
    }

    public AllowIPException(String message, Throwable cause) {
        super(message, cause);
    }

    public AllowIPException(Throwable cause) {
        super(cause);
    }

    public AllowIPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
