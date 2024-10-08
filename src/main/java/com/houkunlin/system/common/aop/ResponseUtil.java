package com.houkunlin.system.common.aop;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 请求响应工具
 *
 * @author HouKunLin
 */
class ResponseUtil {
    public static final ZoneId GMT = ZoneId.of("GMT");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).withZone(GMT);
    public static final String EXPIRES = DATE_FORMATTER.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(0), GMT));
    public static final String NO_CACHE = CacheControl.noCache().mustRevalidate().getHeaderValue();

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
        writeDownloadHeaders(response, filename, contentType, inline, new String[0]);
    }

    /**
     * 写入下载文件响应头
     *
     * @param response    请求响应对象
     * @param filename    文件名
     * @param contentType 文件类型
     * @param headers     自定义请求头参数列表
     */
    public static void writeDownloadHeaders(HttpServletResponse response, String filename, String contentType, boolean inline, String... headers) {
        ContentDisposition contentDisposition = ContentDisposition.builder(inline ? "inline" : "attachment")
                .filename(filename, StandardCharsets.UTF_8)
                .build();
        response.setStatus(200);
        response.setContentType(contentType);
        response.setHeader("Cache-Control", NO_CACHE);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", EXPIRES);
        response.setHeader("Content-Disposition", contentDisposition.toString());
        for (int i = 1; i < headers.length; i += 2) {
            response.setHeader(headers[i - 1], headers[i]);
        }
    }
}
