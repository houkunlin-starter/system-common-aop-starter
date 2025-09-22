package com.houkunlin.system.common.aop;

import com.houkunlin.system.common.aop.annotation.PreventRepeatSubmit;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/PreventRepeatSubmit/")
@RequiredArgsConstructor
public class PreventRepeatSubmitController {

    @PreventRepeatSubmit
    @GetMapping("m1")
    public Object m1() {
        return true;
    }

    @PreventRepeatSubmit(useMethodArgs = true)
    @GetMapping("m11")
    public Object m11() {
        return true;
    }

    @PreventRepeatSubmit
    @PostMapping("m2")
    public Object m2(@RequestBody Map<String, Object> map) {
        return map;
    }

    @PreventRepeatSubmit(useMethodArgs = true)
    @PostMapping("m22")
    public Object m22(@RequestBody Map<String, Object> map) {
        return map;
    }

    @PreventRepeatSubmit(useQueryString = true)
    @PostMapping("m3")
    public Object m3(@RequestBody Map<String, Object> map) {
        return map;
    }

    @PreventRepeatSubmit(useQueryString = true, useMethodArgs = true)
    @PostMapping("m33")
    public Object m33(@RequestBody Map<String, Object> map) {
        return map;
    }

    @PreventRepeatSubmit(tryJson = true)
    @PostMapping("m4")
    public Object m4(@RequestBody Map<String, Object> map) {
        return map;
    }

    @PreventRepeatSubmit(tryJson = true, useMethodArgs = true)
    @PostMapping("m44")
    public Object m44(@RequestBody Map<String, Object> map) {
        return map;
    }
}
