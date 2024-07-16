package com.houkunlin.system.common.aop;

import com.houkunlin.system.common.aop.bean.DownloadFileBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DownloadFileAspectTest {
    @Autowired
    private DownloadFileAspect downloadFileAspect;

    @Test
    void getFileOutputs() {
    }

    @Test
    void defaultIfBlank() {
        assertEquals("文件名.txt", DownloadFileAspect.defaultIfBlank(null, () -> "文件名.txt"));
        assertEquals("文件名.txt", DownloadFileAspect.defaultIfBlank("", () -> "文件名.txt"));
        assertEquals("文件名.txt", DownloadFileAspect.defaultIfBlank("  ", () -> "文件名.txt"));
        assertEquals("文件名.txt", DownloadFileAspect.defaultIfBlank("文件名.txt", () -> "文件名1.txt"));
        assertEquals("文件名.txt", DownloadFileAspect.defaultIfBlank(null, "文件名.txt"));
        assertEquals("文件名.txt", DownloadFileAspect.defaultIfBlank("", "文件名.txt"));
        assertEquals("文件名.txt", DownloadFileAspect.defaultIfBlank("  ", "文件名.txt"));
        assertEquals("文件名.txt", DownloadFileAspect.defaultIfBlank("文件名.txt", "文件名1.txt"));
    }

    @Test
    void getFilename() {
        Set<String> filenameSets = new HashSet<>();
        // assertEquals("文件名.txt", downloadFileAspect.getFilename(filenameSets, "文件名.txt"));
        // assertEquals("文件名.txt.duplicate-1", downloadFileAspect.getFilename(filenameSets, "文件名.txt"));
        // assertEquals("文件名.txt.duplicate-2", downloadFileAspect.getFilename(filenameSets, "文件名.txt"));
        // assertEquals("文件名.txt.duplicate-3", downloadFileAspect.getFilename(filenameSets, "文件名.txt"));

        assertEquals("文件名.txt", DownloadFileAspect.getFilename(filenameSets, "文件名.txt"));
        assertEquals("文件名.duplicate-1.txt", DownloadFileAspect.getFilename(filenameSets, "文件名.txt"));
        assertEquals("文件名.duplicate-2.txt", DownloadFileAspect.getFilename(filenameSets, "文件名.txt"));
        assertEquals("文件名.duplicate-3.txt", DownloadFileAspect.getFilename(filenameSets, "文件名.txt"));

        assertEquals("path/文件名.txt", DownloadFileAspect.getFilename(filenameSets, "path/文件名.txt"));
        assertEquals("path/文件名.duplicate-1.txt", DownloadFileAspect.getFilename(filenameSets, "path/文件名.txt"));
        assertEquals("path/文件名.duplicate-2.txt", DownloadFileAspect.getFilename(filenameSets, "path/文件名.txt"));
        assertEquals("path/文件名.duplicate-3.txt", DownloadFileAspect.getFilename(filenameSets, "path/文件名.txt"));
    }

    @Test
    void getFileModelMetadata() throws InvocationTargetException, IllegalAccessException {
        DownloadFileBean testFileModel = new DownloadFileBean("文件名.txt", "http://127.0.0.1/a.txt");
        DownloadFileModelMetadata fileModel = downloadFileAspect.getFileModelMetadata(testFileModel);
        assertEquals("文件名.txt", fileModel.getFilename());
        assertEquals("http://127.0.0.1/a.txt", fileModel.getSource());
    }
}
