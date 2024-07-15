package com.houkunlin.system.common.aop;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * 类路径工具：主要用来读取类路径的文件内容
 *
 * @author HouKunLin
 */
public class ClassPathUtil {
    private ClassPathUtil() {
    }

    /**
     * 获取当前 ClassPath 路径的资源
     *
     * @param path 文件名称、文件路径
     * @return 文件输入流
     * @throws IOException 打开文件异常
     */
    public static InputStream getResourceAsStream(String path) throws IOException {
        ClassPathResource resource;
        if (path.startsWith("classpath:")) {
            resource = new ClassPathResource(path.substring(10));
        } else if (path.startsWith("classpath*:")) {
            resource = new ClassPathResource(path.substring(11));
        } else {
            resource = new ClassPathResource(path);
        }
        if (resource.exists()) {
            return resource.getInputStream();
        }
        return null;
    }
}
