package com.houkunlin.system.common.aop;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件下载处理器
 *
 * @author HouKunLin
 */
public interface DownloadFileHandler {
    /**
     * 获取文件名。
     * 当遇到 字符串 类型的文件源时，会调用该方法来获取文件名称
     *
     * @param filename 文件名（或文件协议路径）
     * @return 文件输入流
     * @see DownloadFile#source()
     */
    default String getFilename(@NonNull String filename) {
        int adsIndex = filename.indexOf(':');
        if (adsIndex != -1) {
            return FilenameUtils.getName(filename.substring(adsIndex + 1));
        } else {
            return FilenameUtils.getName(filename);
        }
    }

    /**
     * 获取文件输入流
     *
     * @param filename 文件名（或文件协议路径）
     * @return 文件输入流
     * @throws IOException 打开文件异常
     * @see DownloadFile#source()
     */
    @Nullable
    InputStream getFileInputStream(@NonNull String filename) throws IOException;

    /**
     * 打包、压缩文件
     *
     * @param response    请求响应对象
     * @param annotation  注解对象
     * @param filename    下载的文件名
     * @param fileOutputs 文件列表
     * @throws IOException 异常
     */
    default void compressFiles(HttpServletResponse response, DownloadFile annotation, String filename, List<DownloadFileOutput> fileOutputs) throws IOException {
        ResponseUtil.writeDownloadHeaders(response, filename, annotation.contentType(), annotation.inline(), annotation.headers());

        final OutputStream cos = new CheckedOutputStream(response.getOutputStream(), new CRC32());
        final ZipOutputStream zos = new ZipOutputStream(cos);

        final Set<String> filenameSets = new HashSet<>();

        for (DownloadFileOutput fileOutput : fileOutputs) {
            String useFilename = DownloadFileAspect.defaultIfBlank(fileOutput.getFilename(), DownloadFileAspect.unknownFilename);
            String zipEntryName = DownloadFileAspect.getFilename(filenameSets, useFilename);
            zos.putNextEntry(new ZipEntry(zipEntryName));
            fileOutput.write(zos);
            zos.closeEntry();
        }

        zos.flush();
        zos.close();
    }
}
