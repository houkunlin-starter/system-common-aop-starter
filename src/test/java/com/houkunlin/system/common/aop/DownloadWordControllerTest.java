package com.houkunlin.system.common.aop;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DownloadWordControllerTest {
    private static final ZoneId GMT = ZoneId.of("GMT");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).withZone(GMT);
    private static final String expires = DATE_FORMATTER.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(0), GMT));
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    @Autowired
    private MockMvc mockMvc;

    @Test
    void m11() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadWord/m11"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("用户信息.docx", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        String string = getDocxString(contentAsByteArray);

        assertTrue(string.contains("姓名：张三"));
        assertTrue(string.contains("年龄：18"));
        assertTrue(string.contains("地址：北京市朝阳区"));
    }

    @Test
    void m12() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadWord/m12"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("用户信息 - 张三 18 岁.docx", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        String string = getDocxString(contentAsByteArray);

        assertTrue(string.contains("姓名：张三"));
        assertTrue(string.contains("年龄：18"));
        assertTrue(string.contains("地址：北京市朝阳区"));
    }

    private String getDocxString(byte[] bytes) throws IOException {
        StringBuilder stringBuffer = new StringBuilder();
        XWPFDocument xwpfDocument = new XWPFDocument(new ByteArrayInputStream(bytes));
        List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();
        if (paragraphs != null) {
            for (XWPFParagraph paragraph : paragraphs) {
                stringBuffer.append(paragraph.getParagraphText());
                stringBuffer.append("\n");
            }
        }
        return stringBuffer.toString();
    }
}
