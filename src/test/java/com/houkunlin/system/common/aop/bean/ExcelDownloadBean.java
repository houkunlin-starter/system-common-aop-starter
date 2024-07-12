package com.houkunlin.system.common.aop.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelDownloadBean {
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("年龄")
    private int age;
    @ExcelProperty("地址")
    private String address;
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("注册日期")
    private LocalDateTime time;
}
