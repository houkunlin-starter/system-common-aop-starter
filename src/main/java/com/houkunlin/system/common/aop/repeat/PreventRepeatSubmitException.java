package com.houkunlin.system.common.aop.repeat;

import com.houkunlin.system.common.aop.annotation.PreventRepeatSubmit;
import lombok.Getter;

/**
 * 防止表单重复提交的异常
 *
 * @author HouKunLin
 */
@Getter
public class PreventRepeatSubmitException extends RuntimeException {
    private final PreventRepeatSubmit annotation;

    public PreventRepeatSubmitException(PreventRepeatSubmit annotation) {
        super(annotation.message());
        this.annotation = annotation;
    }
}
