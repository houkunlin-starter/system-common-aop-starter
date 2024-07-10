package com.houkunlin.system.common.aop;

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

    @PreventRepeatSubmit
    @PostMapping("m2")
    public Object m2(@RequestBody Map<String, Object> map) {
        return map;
    }
}
