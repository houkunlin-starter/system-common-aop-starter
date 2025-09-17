package com.houkunlin.system.common.aop;

import com.houkunlin.system.common.aop.annotation.DownloadWord;
import com.houkunlin.system.common.aop.bean.ExcelDownloadBean;
import com.houkunlin.system.common.aop.poi.WordData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Getter
@RestController
@RequestMapping("/DownloadWord/")
@RequiredArgsConstructor
public class DownloadWordController {
    private final ExcelDownloadBean bean = new ExcelDownloadBean("张三", 18, "北京市朝阳区", LocalDate.now().atTime(0, 0, 0, 0));

    @DownloadWord(filename = "用户信息", withTemplate = "classpath:template.docx")
    @GetMapping("/m11")
    public ExcelDownloadBean m11() {
        return bean;
    }

    @DownloadWord(filename = "用户信息 - #{result.name} #{result.age} 岁", withTemplate = "classpath:template.docx")
    @GetMapping("/m12")
    public ExcelDownloadBean m12() {
        return bean;
    }

    @DownloadWord(filename = "用户信息 - #{result.name} #{result.age} 岁", withTemplate = "classpath:template.docx")
    @GetMapping("/m13")
    public WordData m13() {
        return new WordData("用户信息 - %s %s 岁".formatted(bean.getName(), bean.getAge()), bean);
    }
}
