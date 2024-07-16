package com.houkunlin.system.common.aop;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.io.InputStream;
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
        writeDownloadBytes(response, filename, contentType, false, bytes);
    }

    /**
     * 写入下载文件数据到请求响应对象中
     *
     * @param response    请求响应对象
     * @param filename    文件名
     * @param contentType 文件类型
     * @param inline      是否浏览器预览
     * @param bytes       文件字节
     * @throws IOException IO异常
     */
    public static void writeDownloadBytes(HttpServletResponse response, String filename, String contentType, boolean inline, byte[] bytes) throws IOException {
        writeDownloadHeaders(response, filename, contentType, inline);

        response.setContentLengthLong(bytes.length);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        response.flushBuffer();
    }

    /**
     * 写入下载文件数据到请求响应对象中
     *
     * @param response    请求响应对象
     * @param filename    文件名
     * @param contentType 文件类型
     * @param inputStream 文件输入流
     * @throws IOException IO异常
     */
    public static void writeDownloadBytes(HttpServletResponse response, String filename, String contentType, InputStream inputStream) throws IOException {
        writeDownloadBytes(response, filename, contentType, false, inputStream);
    }

    /**
     * 写入下载文件数据到请求响应对象中
     *
     * @param response    请求响应对象
     * @param filename    文件名
     * @param contentType 文件类型
     * @param inline      是否浏览器预览
     * @param inputStream 文件输入流
     * @throws IOException IO异常
     */
    public static void writeDownloadBytes(HttpServletResponse response, String filename, String contentType, boolean inline, InputStream inputStream) throws IOException {
        writeDownloadHeaders(response, filename, contentType, inline);

        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(inputStream, outputStream);
        outputStream.flush();
        response.flushBuffer();
    }

    /**
     * 写入下载文件响应头
     *
     * @param response    请求响应对象
     * @param filename    文件名
     * @param contentType 文件类型
     */
    public static void writeDownloadHeaders(HttpServletResponse response, String filename, String contentType, boolean inline) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
        headers.setPragma("no-cache");
        headers.setExpires(0);
        headers.setContentDisposition(ContentDisposition.builder(inline ? "inline" : "attachment")
                .filename(filename, StandardCharsets.UTF_8)
                .build());

        response.setStatus(200);
        response.setContentType(contentType);
        headers.forEach((key, values) -> {
            for (String value : values) {
                response.addHeader(key, value);
            }
        });
    }
}
