package com.houkunlin.system.common.aop;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import org.springframework.lang.NonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 参照 org.springframework.web.servlet.function.DefaultServerRequestBuilder.BodyInputStream 实现
 *
 * @author HouKunLin
 */
public class BodyInputStream extends ServletInputStream {
    private final InputStream delegate;
    private boolean finished = false;

    public BodyInputStream(byte[] body) {
        this.delegate = new ByteArrayInputStream(body);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
        int data = this.delegate.read();
        if (data == -1) {
            this.finished = true;
        }
        return data;
    }

    @Override
    public int read(@NonNull byte[] b, int off, int len) throws IOException {
        return this.delegate.read(b, off, len);
    }

    @Override
    public int read(@NonNull byte[] b) throws IOException {
        return this.delegate.read(b);
    }

    @Override
    public long skip(long n) throws IOException {
        return this.delegate.skip(n);
    }

    @Override
    public int available() throws IOException {
        return this.delegate.available();
    }

    @Override
    public void close() throws IOException {
        this.delegate.close();
    }

    @Override
    public synchronized void mark(int readLimit) {
        this.delegate.mark(readLimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        this.delegate.reset();
    }

    @Override
    public boolean markSupported() {
        return this.delegate.markSupported();
    }
}
