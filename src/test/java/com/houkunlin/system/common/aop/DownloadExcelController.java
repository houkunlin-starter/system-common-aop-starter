package com.houkunlin.system.common.aop;

import cn.idev.excel.support.ExcelTypeEnum;
import com.houkunlin.system.common.aop.bean.ExcelDownloadBean;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@RestController
@RequestMapping("/DownloadExcel/")
@RequiredArgsConstructor
public class DownloadExcelController {
    public static final List<ExcelDownloadBean> data = new ArrayList<>();

    static {
        LocalDateTime localDateTime = LocalDate.now().atTime(0, 0, 0, 0);
        for (int i = 1; i <= 6; i++) {
            data.add(new ExcelDownloadBean("姓名 " + i, 20 + i, "地址 " + i, localDateTime));
        }
    }

    @DownloadExcel(filename = "用户信息")
    @GetMapping("/m11")
    public List<ExcelDownloadBean> m11() {
        return data;
    }

    @DownloadExcel(filename = "用户信息", dataClass = ExcelDownloadBean.class)
    @GetMapping("/m12")
    public List<ExcelDownloadBean> m12() {
        return data;
    }

    @DownloadExcel(filename = "用户信息", excelType = ExcelTypeEnum.XLS)
    @GetMapping("/m13")
    public List<ExcelDownloadBean> m13() {
        return data;
    }

    @DownloadExcel(filename = "用户信息", excelType = ExcelTypeEnum.CSV)
    @GetMapping("/m14")
    public List<ExcelDownloadBean> m14() {
        return data;
    }

    @DownloadExcel(filename = "用户信息", sheetName = "用户信息")
    @GetMapping("/m15")
    public List<ExcelDownloadBean> m15() {
        return data;
    }

    @DownloadExcel(filename = "用户信息", dataClass = ExcelDownloadBean.class, needHead = false)
    @GetMapping("/m16")
    public List<ExcelDownloadBean> m16() {
        return data;
    }

    @DownloadExcel(filename = "用户信息", useDefaultStyle = false)
    @GetMapping("/m17")
    public List<ExcelDownloadBean> m17() {
        return data;
    }

    @DownloadExcel(filename = "用户信息", use1904windowing = true)
    @GetMapping("/m18")
    public List<ExcelDownloadBean> m18() {
        return data;
    }

    @DownloadExcel(filename = "用户信息", dataClass = ExcelDownloadBean.class, withTemplate = "classpath:template-default-list.xlsx")
    @GetMapping("/m19")
    public List<ExcelDownloadBean> m19() {
        return data;
    }

    @DownloadExcel(filename = "用户信息", dataClass = ExcelDownloadBean.class, password = "123456A", excelType = ExcelTypeEnum.XLS)
    @GetMapping("/m20")
    public List<ExcelDownloadBean> m20() {
        return data;
    }

    @DownloadExcel(filename = "用户信息 - #{@testBean.now()}", dataClass = ExcelDownloadBean.class)
    @GetMapping("/m21")
    public List<ExcelDownloadBean> m21() {
        return data;
    }

    @DownloadExcel(filename = "用户信息 - #{result.size} 条数据", dataClass = ExcelDownloadBean.class)
    @GetMapping("/m22")
    public List<ExcelDownloadBean> m22() {
        return data;
    }

    @DownloadExcelWriteHandler(DownloadExcelCustomWriteHandler.class)
    @DownloadExcel(filename = "用户信息", useDefaultStyle = false, dataClass = ExcelDownloadBean.class)
    @GetMapping("/m23")
    public List<ExcelDownloadBean> m23() {
        return data;
    }

    @DownloadExcel(filename = "用户信息", withTemplate = "classpath:template-map-default.xlsx")
    @GetMapping("/m24")
    public Map<String, Object> m24() {
        return Map.of("data", data, "year", "2024");
    }

    @DownloadExcel(filename = "用户信息", withTemplate = "classpath:template-map-horizontal.xlsx")
    @GetMapping("/m25")
    public Map<String, Object> m25() {
        return Map.of("data", data, "data" + DownloadExcel.HORIZONTAL_SUFFIX, true);
    }

    @DownloadExcel(filename = "用户信息", withTemplate = "classpath:template-map-horizontal.xlsx")
    @GetMapping("/m26")
    public Map<String, Object> m26() {
        return Map.of("data", data, "data" + DownloadExcel.HORIZONTAL_SUFFIX, "true");
    }
}
