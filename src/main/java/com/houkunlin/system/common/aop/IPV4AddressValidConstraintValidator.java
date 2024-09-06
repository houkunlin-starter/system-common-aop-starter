package com.houkunlin.system.common.aop;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * IP地址解析验证。忽略空字符串数据。
 *
 * @author HouKunLin
 * @see IpUtil#ip2long(String)
 */
public class IPV4AddressValidConstraintValidator implements ConstraintValidator<IPV4AddressValid, Object> {

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value instanceof String ip) {
            if (ip.isBlank()) {
                return true;
            }
            return IpUtil.ip2long(ip) >= 0;
        }
        return IpUtil.ip2long(value.toString()) >= 0;
    }

    @Override
    public void initialize(final IPV4AddressValid constraintAnnotation) {
    }
}
