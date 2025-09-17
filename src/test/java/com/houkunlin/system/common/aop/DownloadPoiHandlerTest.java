package com.houkunlin.system.common.aop;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.houkunlin.system.common.aop.poi.DownloadPoiHandler;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DownloadPoiHandlerTest {
    @Autowired
    private DownloadPoiHandler downloadPoiHandler;
    public static final String FILE_TEXT = "test DownloadPoiHandler";

    @Test
    void getTemplate() throws IOException {
        InputStream inputStream = downloadPoiHandler.getTemplate("classpath:test-file.txt");
        assertNotNull(inputStream);
        byte[] byteArray = StreamUtils.copyToByteArray(inputStream);
        assertTrue(new String(byteArray).startsWith(FILE_TEXT));

        // 创建日志记录器实例和 ListAppender 以捕获日志事件
        Logger fooLogger = (Logger) LoggerFactory.getLogger(DownloadPoiHandler.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        // 将 ListAppender 附加到日志记录器
        fooLogger.addAppender(listAppender);

        inputStream = downloadPoiHandler.getTemplate("test-file.txt");
        assertNotNull(inputStream);
        byteArray = StreamUtils.copyToByteArray(inputStream);
        assertTrue(new String(byteArray).startsWith(FILE_TEXT));

        inputStream = downloadPoiHandler.getTemplate("test-file1.txt");
        assertNull(inputStream);

        // 从 ListAppender 提取捕获的日志事件
        List<ILoggingEvent> list = listAppender.list;
        assertEquals(1, list.size());
        assertEquals("使用默认的 Excel/Word 模板处理器，不支持读取 ClassPath 之外的文件模板，需要自行实现 DownloadPoiHandler 接口功能。当前读取模板：{}", list.get(list.size() - 1).getMessage());
        assertEquals("使用默认的 Excel/Word 模板处理器，不支持读取 ClassPath 之外的文件模板，需要自行实现 DownloadPoiHandler 接口功能。当前读取模板：test-file1.txt", list.get(list.size() - 1).getFormattedMessage());
    }

    @Test
    void getTemplateByClassPath() throws IOException {
        InputStream inputStream = downloadPoiHandler.getTemplate("classpath:test-file.txt");
        assertNotNull(inputStream);
        byte[] byteArray = StreamUtils.copyToByteArray(inputStream);
        assertTrue(new String(byteArray).startsWith(FILE_TEXT));

        inputStream = downloadPoiHandler.getTemplate("test-file.txt");
        assertNotNull(inputStream);
        byteArray = StreamUtils.copyToByteArray(inputStream);
        assertTrue(new String(byteArray).startsWith(FILE_TEXT));
    }
}
