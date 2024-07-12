package com.houkunlin.system.common.aop;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 请求响应工具
 *
 * @author HouKunLin
 */
class ResponseUtil {
    private ResponseUtil() {
    }

    /**
     * 写入下载文件数据到请求响应对象中
     *
     * @param response    请求响应对象
     * @param filename    文件名
     * @param contentType 文件类型
     * @param bytes       文件字节
     * @throws IOException IO异常
     */
    public static void writeDownloadBytes(HttpServletResponse response, String filename, String contentType, byte[] bytes) throws IOException {
        final HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
        headers.setPragma("no-cache");
        headers.setExpires(0);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(filename, StandardCharsets.UTF_8)
                .build());

        response.setStatus(200);
        response.setContentType(contentType);
        headers.forEach((key, values) -> {
            for (String value : values) {
                response.addHeader(key, value);
            }
        });
        response.setContentLengthLong(bytes.length);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        response.flushBuffer();
    }
}
