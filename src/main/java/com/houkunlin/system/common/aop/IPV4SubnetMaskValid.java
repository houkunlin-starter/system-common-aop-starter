package com.houkunlin.system.common.aop;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * IP掩码地址解析验证。忽略空字符串数据。
 *
 * @author HouKunLin
 * @see IpUtil#ip2maskInt(String)
 */
@Documented
@Constraint(validatedBy = {IPV4SubnetMaskValidConstraintValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IPV4SubnetMaskValid {
    String message() default "子网掩码格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
