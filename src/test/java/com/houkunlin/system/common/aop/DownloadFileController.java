package com.houkunlin.system.common.aop;

import com.houkunlin.system.common.aop.bean.DownloadFileBean;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@Getter
@RestController
@RequestMapping("/DownloadFile/")
@RequiredArgsConstructor
public class DownloadFileController {

    @DownloadFile
    @GetMapping("/m11")
    public Object m11() {
        return "classpath:test-file.txt";
    }

    @DownloadFile(filename = "测试文件.txt")
    @GetMapping("/m12")
    public Object m12() {
        return "classpath:test-file.txt";
    }

    @DownloadFile
    @GetMapping("/m13")
    public Object m13() {
        return "test-file.txt";
    }

    @DownloadFile(filename = "测试文件.txt")
    @GetMapping("/m14")
    public Object m14() {
        return "test-file.txt";
    }

    @DownloadFile(source = "test-file.txt")
    @GetMapping("/m15")
    public void m15() {
    }

    @DownloadFile(filename = "测试文件.txt", source = "test-file.txt")
    @GetMapping("/m16")
    public void m16() {
    }

    @DownloadFile(filename = "测试文件.txt", source = "test-file.txt")
    @GetMapping("/m17")
    public DownloadFileBean m17() {
        return new DownloadFileBean("测试文件1.txt", "test-file.txt");
    }

    @DownloadFile(filename = "测试文件.txt")
    @GetMapping("/m18")
    public DownloadFileBean m18() {
        return new DownloadFileBean(null, "test-file.txt");
    }

    @DownloadFile(filename = "测试文件压缩包.zip")
    @GetMapping("/m19")
    public List<DownloadFileBean> m19() {
        return List.of(
                new DownloadFileBean("文件1.txt", "test-file.txt")
        );
    }

    @DownloadFile(filename = "测试文件压缩包.zip", forceCompress = true)
    @GetMapping("/m20")
    public List<DownloadFileBean> m20() {
        return List.of(
                new DownloadFileBean("文件1.txt", "test-file.txt")
        );
    }

    @DownloadFile(filename = "测试文件压缩包.zip")
    @GetMapping("/m21")
    public List<DownloadFileBean> m21() {
        return Arrays.asList(
                new DownloadFileBean("文件1.txt", "test-file.txt"),
                new DownloadFileBean("文件1.txt", "test-file.txt")
        );
    }

    @DownloadFile(filename = "测试文件压缩包.zip")
    @GetMapping("/m22")
    public List<DownloadFileBean> m22() {
        return Arrays.asList(
                new DownloadFileBean("文件1.txt", "test-file.txt"),
                new DownloadFileBean("文件2.txt", "test-file.txt")
        );
    }

    @DownloadFile
    @GetMapping("/m23")
    public File m23() {
        return new File("./src/test/resources/test-file.txt");
    }

    @DownloadFile(filename = "测试文件名称.txt")
    @GetMapping("/m24")
    public File m24() {
        return new File("./src/test/resources/test-file.txt");
    }

    @DownloadFile
    @GetMapping("/m25")
    public InputStream m25() throws IOException {
        return new ClassPathResource("test-file.txt").getInputStream();
    }

    @DownloadFile(filename = "测试文件名称.txt")
    @GetMapping("/m26")
    public InputStream m26() throws IOException {
        return new ClassPathResource("test-file.txt").getInputStream();
    }

    @DownloadFile
    @GetMapping("/m27")
    public byte[] m27() throws IOException {
        return IOUtils.toByteArray(new ClassPathResource("test-file.txt").getInputStream());
    }

    @DownloadFile(filename = "测试文件名称.txt")
    @GetMapping("/m28")
    public byte[] m28() throws IOException {
        return IOUtils.toByteArray(new ClassPathResource("test-file.txt").getInputStream());
    }

    @DownloadFile
    @GetMapping("/m29")
    public Resource m29() {
        return new ClassPathResource("test-file.txt");
    }

    @DownloadFile(filename = "测试文件名称.txt")
    @GetMapping("/m30")
    public Resource m30() {
        return new ClassPathResource("test-file.txt");
    }

    @DownloadFile(filename = "path/文件1.txt")
    @GetMapping("/m31-1")
    public List<Object> m31_1() {
        return List.of(
                new DownloadFileBean(null, "test-file.txt")
        );
    }

    @DownloadFile
    @GetMapping("/m31-2")
    public List<Object> m31_2() {
        return List.of(
                new DownloadFileBean("path/文件1.txt", "test-file.txt")
        );
    }

    @DownloadFile(filename = "测试文件压缩包.zip")
    @GetMapping("/m32")
    public List<Object> m32() throws IOException {
        return Arrays.asList(
                "test-file.txt",
                new File("./src/test/resources/test-file.txt"),
                new FileInputStream("./src/test/resources/test-file.txt"),
                IOUtils.toByteArray(new ClassPathResource("test-file.txt").getInputStream()),
                new ClassPathResource("test-file.txt"),
                new DownloadFileMeta("测试文件1.txt", "test-file.txt"),
                new DownloadFileBean("测试文件2.txt", "test-file.txt")
        );
    }

    @DownloadFile(filename = "测试文件压缩包.zip")
    @GetMapping("/m33")
    public List<Object> m33() {
        return Arrays.asList(
                new DownloadFileBean("path/文件1.txt", "test-file.txt"),
                new DownloadFileBean("path/文件2.txt", "test-file.txt"),
                new DownloadFileBean("path/文件3.txt", "test-file.txt"),
                new DownloadFileBean("path/文件4.txt", "test-file.txt"),
                new DownloadFileBean("path/文件5.txt", "test-file.txt")
        );
    }

    @DownloadFile(filename = "测试文件压缩包.zip")
    @GetMapping("/m34")
    public List<Object> m34() throws IOException {
        return Arrays.asList(
                "test-file.txt",
                "test-file.txt",
                new File("./src/test/resources/test-file.txt"),
                new File("./src/test/resources/test-file.txt"),
                new FileInputStream("./src/test/resources/test-file.txt"),
                new FileInputStream("./src/test/resources/test-file.txt"),
                new ClassPathResource("test-file.txt"),
                new ClassPathResource("test-file.txt")
        );
    }

    @DownloadFile(filename = "测试文件压缩包.zip")
    @GetMapping("/m35")
    public List<Object> m35() throws FileNotFoundException {
        return Arrays.asList(
                new DownloadFileBean("path/文件1.txt", "test-file.txt"),
                new DownloadFileBean("path/文件2.txt", "test-file.txt"),
                new DownloadFileBean("path/文件3.txt", new File("./src/test/resources/test-file.txt")),
                new DownloadFileBean("path/文件4.txt", new File("./src/test/resources/test-file.txt")),
                new DownloadFileBean("path/文件5.txt", new FileInputStream("./src/test/resources/test-file.txt")),
                new DownloadFileBean("path/文件6.txt", new FileInputStream("./src/test/resources/test-file.txt")),
                new DownloadFileBean("path/文件7.txt", new ClassPathResource("test-file.txt")),
                new DownloadFileBean("path/文件8.txt", new ClassPathResource("test-file.txt"))
        );
    }

    @DownloadFile(filename = "测试文件压缩包.zip")
    @GetMapping("/m36")
    public List<Object> m36() throws FileNotFoundException {
        return Arrays.asList(
                new DownloadFileBean(null, "test-file.txt"),
                new DownloadFileBean(null, "test-file.txt"),
                new DownloadFileBean(null, new File("./src/test/resources/test-file.txt")),
                new DownloadFileBean(null, new File("./src/test/resources/test-file.txt")),
                new DownloadFileBean(null, new FileInputStream("./src/test/resources/test-file.txt")),
                new DownloadFileBean(null, new FileInputStream("./src/test/resources/test-file.txt")),
                new DownloadFileBean(null, new ClassPathResource("test-file.txt")),
                new DownloadFileBean(null, new ClassPathResource("test-file.txt"))
        );
    }
}
