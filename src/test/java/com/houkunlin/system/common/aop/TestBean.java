package com.houkunlin.system.common.aop;

import org.springframework.stereotype.Component;

/**
 * @author HouKunLin
 */
@Component
public class TestBean {
    public String now() {
        return "2024-01-01 00:00:00";
    }
}
