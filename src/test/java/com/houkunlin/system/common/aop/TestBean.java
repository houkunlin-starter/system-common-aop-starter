package com.houkunlin.system.common.aop;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author HouKunLin
 */
@Component
public class TestBean {
    public String now() {
        return "2024-01-01 00:00:00";
    }

    public String now(String format) {
        return DateTimeFormatter.ofPattern(format).format(LocalDateTime.now());
    }
}
