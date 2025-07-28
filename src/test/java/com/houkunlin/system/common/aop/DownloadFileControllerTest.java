package com.houkunlin.system.common.aop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DownloadFileControllerTest {
    private static final ZoneId GMT = ZoneId.of("GMT");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).withZone(GMT);
    private static final String expires = DATE_FORMATTER.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(0), GMT));
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String FILE_TEXT = "test DownloadPoiHandler";
    @Autowired
    private MockMvc mockMvc;

    @Test
    void m11() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m11"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CACHE_CONTROL, "max-age=3600"))
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("test-file.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m12() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m12"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("inline")
                        .filename("测试文件.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m13() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m13"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("test-file.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m14() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m14").param("inline", "true"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("inline")
                        .filename("测试文件.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m15() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m15"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("test-file.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m16() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m16"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m17() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m17"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件1.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m18() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m18"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m19() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m19"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("文件1.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m20() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m20"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件压缩包.zip", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(contentAsByteArray));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("文件1.txt", zipEntry.getName());
    }

    @Test
    void m21() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m21"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件压缩包.zip", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(contentAsByteArray));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("文件1.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("文件1.duplicate-1.txt", zipEntry.getName());
    }

    @Test
    void m22() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m22"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件压缩包.zip", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(contentAsByteArray));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("文件1.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("文件2.txt", zipEntry.getName());
    }

    @Test
    void m23() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m23"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("test-file.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m24() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m24"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件名称.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m25() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m25"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("未知文件.unknown", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m26() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m26"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件名称.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m27() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m27"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("未知文件.unknown", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m28() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m28"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件名称.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m29() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m29"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("test-file.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m30() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m30"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件名称.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m31_1()throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m31-1"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("path/文件1.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m31_2() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m31-2"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("path/文件1.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertTrue(new String(contentAsByteArray).startsWith(FILE_TEXT));
    }

    @Test
    void m32()throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m32"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件压缩包.zip", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(contentAsByteArray));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.duplicate-1.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("未知文件.unknown", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("未知文件.duplicate-1.unknown", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.duplicate-2.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("测试文件1.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("测试文件2.txt", zipEntry.getName());
    }

    @Test
    void m33()throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m33"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件压缩包.zip", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(contentAsByteArray));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("path/文件1.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("path/文件2.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("path/文件3.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("path/文件4.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("path/文件5.txt", zipEntry.getName());
    }

    @Test
    void m34()throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m34"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件压缩包.zip", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(contentAsByteArray));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.duplicate-1.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.duplicate-2.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.duplicate-3.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("未知文件.unknown", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("未知文件.duplicate-1.unknown", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.duplicate-4.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.duplicate-5.txt", zipEntry.getName());
    }

    @Test
    void m35()throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m35"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件压缩包.zip", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(contentAsByteArray));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("path/文件1.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("path/文件2.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("path/文件3.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("path/文件4.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("path/文件5.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("path/文件6.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("path/文件7.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("path/文件8.txt", zipEntry.getName());
    }

    @Test
    void m36()throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m36"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("测试文件压缩包.zip", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(contentAsByteArray));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.duplicate-1.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.duplicate-2.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.duplicate-3.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("未知文件.unknown", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("未知文件.duplicate-1.unknown", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.duplicate-4.txt", zipEntry.getName());

        zipEntry = zipInputStream.getNextEntry();
        assertNotNull(zipEntry);
        assertEquals("test-file.duplicate-5.txt", zipEntry.getName());
    }

    @Test
    void m37() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m37"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("未知文件.unknown", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertEquals(0, contentAsByteArray.length);
    }

    @Test
    void m38() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m38"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("未知文件.unknown", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertEquals(0, contentAsByteArray.length);
    }

    @Test
    void m39() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m39"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("test.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertEquals(0, contentAsByteArray.length);
    }

    @Test
    void m40() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/DownloadFile/m40"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string("pragma", "no-cache"))
                // .andExpect(header().string("expires", expires))
                .andExpect(header().string("Content-Disposition", ContentDisposition.builder("attachment")
                        .filename("test.txt", StandardCharsets.UTF_8)
                        .build().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
        byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        assertEquals(0, contentAsByteArray.length);
    }
}
