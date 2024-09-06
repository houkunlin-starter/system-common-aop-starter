package com.houkunlin.system.common.aop;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * IP地址解析验证。忽略空字符串数据。
 *
 * @author HouKunLin
 * @see IpUtil#ip2long(String)
 */
@Documented
@Constraint(validatedBy = {IPV4AddressValidConstraintValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IPV4AddressValid {
    String message() default "IP地址格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
