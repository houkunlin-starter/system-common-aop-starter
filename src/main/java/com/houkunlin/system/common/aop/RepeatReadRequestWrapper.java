package com.houkunlin.system.common.aop;

import com.google.common.io.ByteStreams;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 可重复读取 Body 数据的请求包装器
 *
 * @author HouKunLin
 */
@Getter
public class RepeatReadRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] bodyBytes;

    public RepeatReadRequestWrapper(HttpServletRequest request, ServletResponse response) throws IOException {
        super(request);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        this.bodyBytes = ByteStreams.toByteArray(request.getInputStream());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bodyBytes)));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new BodyInputStream(bodyBytes);
    }
}