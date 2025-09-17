package com.houkunlin.system.common.aop;

import cn.idev.excel.support.ExcelTypeEnum;
import cn.idev.excel.write.handler.WriteHandler;
import com.houkunlin.system.common.aop.annotation.DownloadExcel;
import com.houkunlin.system.common.aop.bean.ExcelDownloadBean;
import com.houkunlin.system.common.aop.poi.DownloadPoiHandler;
import com.houkunlin.system.common.aop.poi.ExcelData;
import com.houkunlin.system.common.aop.poi.FastExcelView;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@RestController
@RequestMapping("/DownloadExcelView/")
@RequiredArgsConstructor
public class DownloadExcelViewController {
    public static final List<ExcelDownloadBean> data = new ArrayList<>();

    static {
        LocalDateTime localDateTime = LocalDate.now().atTime(0, 0, 0, 0);
        for (int i = 1; i <= 6; i++) {
            data.add(new ExcelDownloadBean("姓名 " + i, 20 + i, "地址 " + i, localDateTime));
        }
    }

    private final DownloadPoiHandler downloadPoiHandler;
    private final TestBean testBean;

    @GetMapping("/m11")
    public ModelAndView m11() {
        FastExcelView view = new FastExcelView("用户信息", data);
        return new ModelAndView(view);
    }

    @GetMapping("/m12")
    public ModelAndView m12() {
        FastExcelView view = new FastExcelView("用户信息", data, ExcelDownloadBean.class);
        return new ModelAndView(view);
    }

    @GetMapping("/m13")
    public ModelAndView m13() {
        FastExcelView view = new FastExcelView("用户信息", data, ExcelTypeEnum.XLS);
        return new ModelAndView(view);
    }

    @GetMapping("/m14")
    public ModelAndView m14() {
        FastExcelView view = new FastExcelView("用户信息", data, ExcelTypeEnum.CSV);
        return new ModelAndView(view);
    }

    @GetMapping("/m15")
    public ModelAndView m15() {
        FastExcelView view = new FastExcelView("用户信息", data);
        view.setSheetName("用户信息");
        return new ModelAndView(view);
    }

    @GetMapping("/m16")
    public ModelAndView m16() {
        FastExcelView view = new FastExcelView("用户信息", data, ExcelDownloadBean.class);
        view.setNeedHead(false);
        return new ModelAndView(view);
    }

    @GetMapping("/m17")
    public ModelAndView m17() {
        FastExcelView view = new FastExcelView("用户信息", data);
        view.setUseDefaultStyle(false);
        return new ModelAndView(view);
    }

    @GetMapping("/m18")
    public ModelAndView m18() {
        FastExcelView view = new FastExcelView("用户信息", data);
        view.setUse1904windowing(false);
        return new ModelAndView(view);
    }

    @GetMapping("/m19")
    public ModelAndView m19() throws IOException {
        FastExcelView view = new FastExcelView("用户信息", data, ExcelDownloadBean.class);
        InputStream templateInputStream = downloadPoiHandler.getTemplate("classpath:template-default-list.xlsx");
        view.setTemplateInputStream(templateInputStream);
        return new ModelAndView(view);
    }

    @GetMapping("/m20")
    public ModelAndView m20() {
        FastExcelView view = new FastExcelView("用户信息", data, ExcelDownloadBean.class, ExcelTypeEnum.XLS);
        view.setPassword("123456A");
        return new ModelAndView(view);
    }

    @GetMapping("/m21")
    public ModelAndView m21() {
        FastExcelView view = new FastExcelView("用户信息 - " + testBean.now(), data, ExcelDownloadBean.class);
        return new ModelAndView(view);
    }

    @GetMapping("/m22")
    public ModelAndView m22() {
        FastExcelView view = new FastExcelView("用户信息 - %s 条数据".formatted(data.size()), data, ExcelDownloadBean.class);
        return new ModelAndView(view);
    }

    @DownloadExcel(filename = "用户信息 - #{result.size} 条数据", dataClass = ExcelDownloadBean.class)
    @GetMapping("/m221")
    public ExcelData m221() {
        return new ExcelData("用户信息 - %s 条数据".formatted(data.size()), data);
    }

    @GetMapping("/m23")
    public ModelAndView m23() {
        FastExcelView view = new FastExcelView("用户信息", data, ExcelDownloadBean.class);
        view.setUseDefaultStyle(false);
        view.setWriteHandlers(new WriteHandler[]{new DownloadExcelCustomWriteHandler()});
        return new ModelAndView(view);
    }

    @GetMapping("/m24")
    public ModelAndView m24() throws IOException {
        Object map = Map.of("data", data, "year", "2024");
        FastExcelView view = new FastExcelView("用户信息", map);
        InputStream templateInputStream = downloadPoiHandler.getTemplate("classpath:template-map-default.xlsx");
        view.setTemplateInputStream(templateInputStream);
        return new ModelAndView(view);
    }

    @GetMapping("/m25")
    public ModelAndView m25() throws IOException {
        Object map = Map.of("data", data, "data" + DownloadExcel.HORIZONTAL_SUFFIX, true);
        FastExcelView view = new FastExcelView("用户信息", map);
        InputStream templateInputStream = downloadPoiHandler.getTemplate("classpath:template-map-horizontal.xlsx");
        view.setTemplateInputStream(templateInputStream);
        return new ModelAndView(view);
    }

    @GetMapping("/m26")
    public ModelAndView m26() throws IOException {
        Object map = Map.of("data", data, "data" + DownloadExcel.HORIZONTAL_SUFFIX, "true");
        FastExcelView view = new FastExcelView("用户信息", map);
        InputStream templateInputStream = downloadPoiHandler.getTemplate("classpath:template-map-horizontal.xlsx");
        view.setTemplateInputStream(templateInputStream);
        return new ModelAndView(view);
    }

    @DownloadExcel(filename = "", withTemplate = "classpath:template-map-horizontal.xlsx")
    @GetMapping("/m27")
    public ExcelData m27() {
        return new ExcelData("用户信息", Map.of("data", data, "data" + DownloadExcel.HORIZONTAL_SUFFIX, "true"));
    }
}
