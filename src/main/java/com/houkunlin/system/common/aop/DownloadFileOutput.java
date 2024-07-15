package com.houkunlin.system.common.aop;

import lombok.Getter;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * 文件输出
 *
 * @author HouKunLin
 */
@Getter
public class DownloadFileOutput implements Serializable {
    /**
     * 文件名
     */
    private final String filename;
    /**
     * 文件流。文件流和文件字节数组只能存在一个
     */
    private final InputStream inputStream;
    /**
     * 文件字节数组。文件流和文件字节数组只能存在一个
     */
    private final byte[] bytes;
    /**
     * 在下载单个文件时，用来判断是否使用注解的默认文件名
     */
    private boolean useDefaultFilename;

    public DownloadFileOutput(String filename, InputStream inputStream) {
        this.filename = filename;
        this.inputStream = inputStream;
        this.bytes = DownloadFileAspect.EMPTY_BYTE_ARRAY;
    }

    public DownloadFileOutput(String filename, byte[] bytes) {
        this.filename = filename;
        this.inputStream = null;
        this.bytes = bytes;
    }

    public DownloadFileOutput(String filename, String text) {
        this.filename = filename;
        this.inputStream = null;
        this.bytes = text.getBytes(StandardCharsets.UTF_8);
    }

    public void write(OutputStream outputStream) throws IOException {
        if (inputStream != null) {
            IOUtils.copy(inputStream, outputStream);
        } else if (bytes != null) {
            outputStream.write(bytes);
        }
    }

    public void setUseDefaultFilename() {
        this.useDefaultFilename = true;
    }
}
