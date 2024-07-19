package com.houkunlin.system.common.aop;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.houkunlin.system.common.aop.bean.ExcelDownloadBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ContentDisposition;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DownloadExcelControllerTest {
    private static final ZoneId GMT = ZoneId.of("GMT");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).withZone(GMT);
    private static final String expires = DATE_FORMATTER.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(0), GMT));
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DownloadExcelController downloadExcelController;
    @Autowired
    private TestBean testBean;

    @Test
    void m11() throws Exception {
        ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
        MvcResult mvcResult = mockMvc.perform(get("/DownloadExcel/m11"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("用户信息" + excelType.getValue(), StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        List<ExcelDownloadBean> data = downloadExcelController.getData();
        List<Map<Object, Object>> objectList = EasyExcel.read(new ByteArrayInputStream(contentAsByteArray), new PageReadListener<Map<Object, Object>>(objects -> {
                    assertEquals(data.size(), objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        assertEquals(data.get(i).getName(), objects.get(i).get(0));
                        assertEquals(String.valueOf(data.get(i).getAge()), objects.get(i).get(1));
                        assertEquals(data.get(i).getAddress(), objects.get(i).get(2));
                        assertEquals(DATE_TIME_FORMATTER.format(data.get(i).getTime()), objects.get(i).get(3));
                    }
                }))
                .headRowNumber(0)
                .excelType(excelType)
                .charset(StandardCharsets.UTF_8)
                .use1904windowing(false)
                .sheet("Sheet1")
                .doReadSync();
        assertEquals(data.size(), objectList.size());
        for (int i = 0; i < objectList.size(); i++) {
            assertEquals(data.get(i).getName(), objectList.get(i).get(0));
            assertEquals(String.valueOf(data.get(i).getAge()), objectList.get(i).get(1));
            assertEquals(data.get(i).getAddress(), objectList.get(i).get(2));
            assertEquals(DATE_TIME_FORMATTER.format(data.get(i).getTime()), objectList.get(i).get(3));
        }
    }

    @Test
    void m12() throws Exception {
        ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
        MvcResult mvcResult = mockMvc.perform(get("/DownloadExcel/m12"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("用户信息" + excelType.getValue(), StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        List<ExcelDownloadBean> data = downloadExcelController.getData();
        List<ExcelDownloadBean> objectList = EasyExcel.read(new ByteArrayInputStream(contentAsByteArray), ExcelDownloadBean.class, new PageReadListener<ExcelDownloadBean>(objects -> {
                    assertEquals(data.size(), objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        assertEquals(data.get(i).getName(), objects.get(i).getName());
                        assertEquals(data.get(i).getAge(), objects.get(i).getAge());
                        assertEquals(data.get(i).getAddress(), objects.get(i).getAddress());
                        assertEquals(data.get(i).getTime(), objects.get(i).getTime());
                    }
                }))
                .headRowNumber(1)
                .excelType(excelType)
                .charset(StandardCharsets.UTF_8)
                .use1904windowing(false)
                .sheet("Sheet1")
                .doReadSync();
        assertEquals(data.size(), objectList.size());
        for (int i = 0; i < objectList.size(); i++) {
            assertEquals(data.get(i).getName(), objectList.get(i).getName());
            assertEquals(data.get(i).getAge(), objectList.get(i).getAge());
            assertEquals(data.get(i).getAddress(), objectList.get(i).getAddress());
            assertEquals(data.get(i).getTime(), objectList.get(i).getTime());
        }
    }

    @Test
    void m13() throws Exception {
        ExcelTypeEnum excelType = ExcelTypeEnum.XLS;
        MvcResult mvcResult = mockMvc.perform(get("/DownloadExcel/m13"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("用户信息" + excelType.getValue(), StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        List<ExcelDownloadBean> data = downloadExcelController.getData();
        List<Map<Object, Object>> objectList = EasyExcel.read(new ByteArrayInputStream(contentAsByteArray), new PageReadListener<Map<Object, Object>>(objects -> {
                    assertEquals(data.size(), objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        assertEquals(data.get(i).getName(), objects.get(i).get(0));
                        assertEquals(String.valueOf(data.get(i).getAge()), objects.get(i).get(1));
                        assertEquals(data.get(i).getAddress(), objects.get(i).get(2));
                        assertEquals(DATE_TIME_FORMATTER.format(data.get(i).getTime()), objects.get(i).get(3));
                    }
                }))
                .headRowNumber(0)
                .excelType(excelType)
                .charset(StandardCharsets.UTF_8)
                .use1904windowing(false)
                .sheet("Sheet1")
                .doReadSync();
        assertEquals(data.size(), objectList.size());
        for (int i = 0; i < objectList.size(); i++) {
            assertEquals(data.get(i).getName(), objectList.get(i).get(0));
            assertEquals(String.valueOf(data.get(i).getAge()), objectList.get(i).get(1));
            assertEquals(data.get(i).getAddress(), objectList.get(i).get(2));
            assertEquals(DATE_TIME_FORMATTER.format(data.get(i).getTime()), objectList.get(i).get(3));
        }
    }

    @Test
    void m14() throws Exception {
        ExcelTypeEnum excelType = ExcelTypeEnum.CSV;
        MvcResult mvcResult = mockMvc.perform(get("/DownloadExcel/m14"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("用户信息" + excelType.getValue(), StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        List<ExcelDownloadBean> data = downloadExcelController.getData();
        List<Map<Object, Object>> objectList = EasyExcel.read(new ByteArrayInputStream(contentAsByteArray), new PageReadListener<Map<Object, Object>>(objects -> {
                    assertEquals(data.size(), objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        assertEquals(data.get(i).getName(), objects.get(i).get(0));
                        assertEquals(String.valueOf(data.get(i).getAge()), objects.get(i).get(1));
                        assertEquals(data.get(i).getAddress(), objects.get(i).get(2));
                        assertEquals(DATE_TIME_FORMATTER.format(data.get(i).getTime()), objects.get(i).get(3));
                    }
                }))
                .headRowNumber(0)
                .excelType(excelType)
                .charset(StandardCharsets.UTF_8)
                .use1904windowing(false)
                .sheet()
                .doReadSync();
        assertEquals(data.size(), objectList.size());
        for (int i = 0; i < objectList.size(); i++) {
            assertEquals(data.get(i).getName(), objectList.get(i).get(0));
            assertEquals(String.valueOf(data.get(i).getAge()), objectList.get(i).get(1));
            assertEquals(data.get(i).getAddress(), objectList.get(i).get(2));
            assertEquals(DATE_TIME_FORMATTER.format(data.get(i).getTime()), objectList.get(i).get(3));
        }
    }

    @Test
    void m15() throws Exception {
        ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
        MvcResult mvcResult = mockMvc.perform(get("/DownloadExcel/m15"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("用户信息" + excelType.getValue(), StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        List<ExcelDownloadBean> data = downloadExcelController.getData();
        List<Map<Object, Object>> objectList = EasyExcel.read(new ByteArrayInputStream(contentAsByteArray), new PageReadListener<Map<Object, Object>>(objects -> {
                    assertEquals(data.size(), objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        assertEquals(data.get(i).getName(), objects.get(i).get(0));
                        assertEquals(String.valueOf(data.get(i).getAge()), objects.get(i).get(1));
                        assertEquals(data.get(i).getAddress(), objects.get(i).get(2));
                        assertEquals(DATE_TIME_FORMATTER.format(data.get(i).getTime()), objects.get(i).get(3));
                    }
                }))
                .headRowNumber(0)
                .excelType(excelType)
                .charset(StandardCharsets.UTF_8)
                .use1904windowing(false)
                .sheet("用户信息")
                .doReadSync();
        assertEquals(data.size(), objectList.size());
        for (int i = 0; i < objectList.size(); i++) {
            assertEquals(data.get(i).getName(), objectList.get(i).get(0));
            assertEquals(String.valueOf(data.get(i).getAge()), objectList.get(i).get(1));
            assertEquals(data.get(i).getAddress(), objectList.get(i).get(2));
            assertEquals(DATE_TIME_FORMATTER.format(data.get(i).getTime()), objectList.get(i).get(3));
        }
    }

    @Test
    void m16() throws Exception {
        ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
        MvcResult mvcResult = mockMvc.perform(get("/DownloadExcel/m16"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("用户信息" + excelType.getValue(), StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        List<ExcelDownloadBean> data = downloadExcelController.getData();
        List<ExcelDownloadBean> objectList = EasyExcel.read(new ByteArrayInputStream(contentAsByteArray), ExcelDownloadBean.class, new PageReadListener<ExcelDownloadBean>(objects -> {
                    assertEquals(data.size(), objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        assertEquals(data.get(i).getName(), objects.get(i).getName());
                        assertEquals(data.get(i).getAge(), objects.get(i).getAge());
                        assertEquals(data.get(i).getAddress(), objects.get(i).getAddress());
                        assertEquals(data.get(i).getTime(), objects.get(i).getTime());
                    }
                }))
                .headRowNumber(0)
                .excelType(excelType)
                .charset(StandardCharsets.UTF_8)
                .use1904windowing(false)
                .sheet("Sheet1")
                .doReadSync();
        assertEquals(data.size(), objectList.size());
        for (int i = 0; i < objectList.size(); i++) {
            assertEquals(data.get(i).getName(), objectList.get(i).getName());
            assertEquals(data.get(i).getAge(), objectList.get(i).getAge());
            assertEquals(data.get(i).getAddress(), objectList.get(i).getAddress());
            assertEquals(data.get(i).getTime(), objectList.get(i).getTime());
        }
    }

    @Test
    void m17() throws Exception {
        ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
        MvcResult mvcResult = mockMvc.perform(get("/DownloadExcel/m17"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("用户信息" + excelType.getValue(), StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        List<ExcelDownloadBean> data = downloadExcelController.getData();
        List<Map<Object, Object>> objectList = EasyExcel.read(new ByteArrayInputStream(contentAsByteArray), new PageReadListener<Map<Object, Object>>(objects -> {
                    assertEquals(data.size(), objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        assertEquals(data.get(i).getName(), objects.get(i).get(0));
                        assertEquals(String.valueOf(data.get(i).getAge()), objects.get(i).get(1));
                        assertEquals(data.get(i).getAddress(), objects.get(i).get(2));
                        assertEquals(DATE_TIME_FORMATTER.format(data.get(i).getTime()), objects.get(i).get(3));
                    }
                }))
                .headRowNumber(0)
                .excelType(excelType)
                .charset(StandardCharsets.UTF_8)
                .use1904windowing(false)
                .sheet("Sheet1")
                .doReadSync();
        assertEquals(data.size(), objectList.size());
        for (int i = 0; i < objectList.size(); i++) {
            assertEquals(data.get(i).getName(), objectList.get(i).get(0));
            assertEquals(String.valueOf(data.get(i).getAge()), objectList.get(i).get(1));
            assertEquals(data.get(i).getAddress(), objectList.get(i).get(2));
            assertEquals(DATE_TIME_FORMATTER.format(data.get(i).getTime()), objectList.get(i).get(3));
        }
    }

    @Test
    void m18() throws Exception {
        ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
        MvcResult mvcResult = mockMvc.perform(get("/DownloadExcel/m18"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("用户信息" + excelType.getValue(), StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        List<ExcelDownloadBean> data = downloadExcelController.getData();
        List<Map<Object, Object>> objectList = EasyExcel.read(new ByteArrayInputStream(contentAsByteArray), new PageReadListener<Map<Object, Object>>(objects -> {
                    assertEquals(data.size(), objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        assertEquals(data.get(i).getName(), objects.get(i).get(0));
                        assertEquals(String.valueOf(data.get(i).getAge()), objects.get(i).get(1));
                        assertEquals(data.get(i).getAddress(), objects.get(i).get(2));
                        assertEquals(DATE_TIME_FORMATTER.format(data.get(i).getTime().plusYears(4).plusDays(1)), objects.get(i).get(3));
                    }
                }))
                .headRowNumber(0)
                .excelType(excelType)
                .charset(StandardCharsets.UTF_8)
                .use1904windowing(true)
                .sheet("Sheet1")
                .doReadSync();
        assertEquals(data.size(), objectList.size());
        for (int i = 0; i < objectList.size(); i++) {
            assertEquals(data.get(i).getName(), objectList.get(i).get(0));
            assertEquals(String.valueOf(data.get(i).getAge()), objectList.get(i).get(1));
            assertEquals(data.get(i).getAddress(), objectList.get(i).get(2));
            assertEquals(DATE_TIME_FORMATTER.format(data.get(i).getTime().plusYears(4).plusDays(1)), objectList.get(i).get(3));
        }
    }

    @Test
    void post() {
    }

    @Test
    void m19() throws Exception {
        ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
        MvcResult mvcResult = mockMvc.perform(get("/DownloadExcel/m19"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("用户信息" + excelType.getValue(), StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        List<ExcelDownloadBean> data = downloadExcelController.getData();
        List<ExcelDownloadBean> objectList = EasyExcel.read(new ByteArrayInputStream(contentAsByteArray), ExcelDownloadBean.class, new PageReadListener<ExcelDownloadBean>(objects -> {
                    assertEquals(data.size(), objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        assertEquals(data.get(i).getName(), objects.get(i).getName());
                        assertEquals(data.get(i).getAge(), objects.get(i).getAge());
                        assertEquals(data.get(i).getAddress(), objects.get(i).getAddress());
                        assertEquals(data.get(i).getTime(), objects.get(i).getTime());
                    }
                }))
                .headRowNumber(1)
                .excelType(excelType)
                .charset(StandardCharsets.UTF_8)
                .use1904windowing(false)
                .sheet("Sheet1")
                .doReadSync();
        assertEquals(data.size(), objectList.size());
        for (int i = 0; i < objectList.size(); i++) {
            assertEquals(data.get(i).getName(), objectList.get(i).getName());
            assertEquals(data.get(i).getAge(), objectList.get(i).getAge());
            assertEquals(data.get(i).getAddress(), objectList.get(i).getAddress());
            assertEquals(data.get(i).getTime(), objectList.get(i).getTime());
        }
    }

    @Test
    void m20() throws Exception {
        ExcelTypeEnum excelType = ExcelTypeEnum.XLS;
        MvcResult mvcResult = mockMvc.perform(get("/DownloadExcel/m20"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("用户信息" + excelType.getValue(), StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        List<ExcelDownloadBean> data = downloadExcelController.getData();
        List<ExcelDownloadBean> objectList = EasyExcel.read(new ByteArrayInputStream(contentAsByteArray), ExcelDownloadBean.class, new PageReadListener<ExcelDownloadBean>(objects -> {
                    assertEquals(data.size(), objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        assertEquals(data.get(i).getName(), objects.get(i).getName());
                        assertEquals(data.get(i).getAge(), objects.get(i).getAge());
                        assertEquals(data.get(i).getAddress(), objects.get(i).getAddress());
                        assertEquals(data.get(i).getTime(), objects.get(i).getTime());
                    }
                }))
                .headRowNumber(1)
                .excelType(excelType)
                .charset(StandardCharsets.UTF_8)
                .use1904windowing(false)
                .password("123456A")
                .sheet()
                .doReadSync();
        assertEquals(data.size(), objectList.size());
        for (int i = 0; i < objectList.size(); i++) {
            assertEquals(data.get(i).getName(), objectList.get(i).getName());
            assertEquals(data.get(i).getAge(), objectList.get(i).getAge());
            assertEquals(data.get(i).getAddress(), objectList.get(i).getAddress());
            assertEquals(data.get(i).getTime(), objectList.get(i).getTime());
        }
    }

    @Test
    void m21() throws Exception {
        ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
        MvcResult mvcResult = mockMvc.perform(get("/DownloadExcel/m21"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("用户信息 - " + testBean.now() + excelType.getValue(), StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        List<ExcelDownloadBean> data = downloadExcelController.getData();
        List<ExcelDownloadBean> objectList = EasyExcel.read(new ByteArrayInputStream(contentAsByteArray), ExcelDownloadBean.class, new PageReadListener<ExcelDownloadBean>(objects -> {
                    assertEquals(data.size(), objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        assertEquals(data.get(i).getName(), objects.get(i).getName());
                        assertEquals(data.get(i).getAge(), objects.get(i).getAge());
                        assertEquals(data.get(i).getAddress(), objects.get(i).getAddress());
                        assertEquals(data.get(i).getTime(), objects.get(i).getTime());
                    }
                }))
                .headRowNumber(1)
                .excelType(excelType)
                .charset(StandardCharsets.UTF_8)
                .use1904windowing(false)
                .sheet("Sheet1")
                .doReadSync();
        assertEquals(data.size(), objectList.size());
        for (int i = 0; i < objectList.size(); i++) {
            assertEquals(data.get(i).getName(), objectList.get(i).getName());
            assertEquals(data.get(i).getAge(), objectList.get(i).getAge());
            assertEquals(data.get(i).getAddress(), objectList.get(i).getAddress());
            assertEquals(data.get(i).getTime(), objectList.get(i).getTime());
        }
    }

    @Test
    void m22() throws Exception {
        ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
        MvcResult mvcResult = mockMvc.perform(get("/DownloadExcel/m22"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("用户信息 - " + downloadExcelController.getData().size() + " 条数据" + excelType.getValue(), StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        List<ExcelDownloadBean> data = downloadExcelController.getData();
        List<ExcelDownloadBean> objectList = EasyExcel.read(new ByteArrayInputStream(contentAsByteArray), ExcelDownloadBean.class, new PageReadListener<ExcelDownloadBean>(objects -> {
                    assertEquals(data.size(), objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        assertEquals(data.get(i).getName(), objects.get(i).getName());
                        assertEquals(data.get(i).getAge(), objects.get(i).getAge());
                        assertEquals(data.get(i).getAddress(), objects.get(i).getAddress());
                        assertEquals(data.get(i).getTime(), objects.get(i).getTime());
                    }
                }))
                .headRowNumber(1)
                .excelType(excelType)
                .charset(StandardCharsets.UTF_8)
                .use1904windowing(false)
                .sheet("Sheet1")
                .doReadSync();
        assertEquals(data.size(), objectList.size());
        for (int i = 0; i < objectList.size(); i++) {
            assertEquals(data.get(i).getName(), objectList.get(i).getName());
            assertEquals(data.get(i).getAge(), objectList.get(i).getAge());
            assertEquals(data.get(i).getAddress(), objectList.get(i).getAddress());
            assertEquals(data.get(i).getTime(), objectList.get(i).getTime());
        }
    }

    @Test
    void getData() {
        List<ExcelDownloadBean> data = downloadExcelController.getData();
        assertEquals(6, data.size());
        for (int i = 1; i <= 6; i++) {
            assertEquals("姓名 " + i, data.get(i - 1).getName());
            assertEquals(20 + i, data.get(i - 1).getAge());
            assertEquals("地址 " + i, data.get(i - 1).getAddress());
            assertEquals(DATE_TIME_FORMATTER.format(LocalDate.now().atTime(0, 0, 0, 0)), DATE_TIME_FORMATTER.format(data.get(i - 1).getTime()));
        }
    }
}
