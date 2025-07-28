package com.houkunlin.system.common.aop;

import com.houkunlin.system.common.aop.bean.ExcelDownloadBean;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;

@Getter
@RestController
@RequestMapping("/DownloadWordView/")
@RequiredArgsConstructor
public class DownloadWordViewController {
    private final ExcelDownloadBean bean = new ExcelDownloadBean("张三", 18, "北京市朝阳区", LocalDate.now().atTime(0, 0, 0, 0));
    private final DownloadPoiHandler downloadPoiHandler;

    @GetMapping("/m11")
    public ModelAndView m11() throws IOException {
        InputStream template = downloadPoiHandler.getTemplate("classpath:template.docx");
        PoiWordView view = new PoiWordView("用户信息", template, bean);
        return new ModelAndView(view);
    }

    @GetMapping("/m12")
    public ModelAndView m12() throws IOException {
        InputStream template = downloadPoiHandler.getTemplate("classpath:template.docx");
        PoiWordView view = new PoiWordView("用户信息 - %s %s 岁".formatted(bean.getName(), bean.getAge()), template);
        return new ModelAndView(view, Map.of(PoiWordView.DATA, bean));
    }

    @DownloadWord(filename = "用户信息 - #{result.name} #{result.age} 岁", withTemplate = "classpath:template.docx")
    @GetMapping("/m13")
    public WordData m13() {
        return new WordData("用户信息 - %s %s 岁".formatted(bean.getName(), bean.getAge()), bean);
    }
}
