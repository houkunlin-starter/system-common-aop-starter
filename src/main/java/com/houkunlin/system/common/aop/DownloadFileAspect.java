package com.houkunlin.system.common.aop;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

/**
 * 文件下载
 *
 * @author HouKunLin
 * @see DownloadFile
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DownloadFileAspect {
    private final TemplateParser templateParser;
    private final DownloadFileHandler downloadFileHandler;
    private final HttpServletResponse response;
    public static final String unknownFilename = "未知文件.unknown";
    public static final String ERROR_TEXT = "文件下载失败，此文件可能从服务器丢失，请联系管理员核查。文件信息：";
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    @Around("@annotation(annotation)")
    public Object doBefore(ProceedingJoinPoint pjp, DownloadFile annotation) throws Throwable {
        try {
            Object object = pjp.proceed();
            List<DownloadFileOutput> list = getFileOutputs(null, object, false);
            if (list.isEmpty() && !annotation.source().isBlank()) {
                list = new ArrayList<>(getFileOutputs(null, annotation.source(), false));
            }
            if (list.isEmpty()) {
                writeEmpty(pjp, annotation, object);
            } else if (list.size() == 1 && !annotation.forceCompress()) {
                writeOne(pjp, annotation, object, list.get(0));
            } else {
                writeZip(pjp, annotation, object, list);
            }
            return null;
        } catch (Throwable e) {
            log.error("下载文件失败，发生了异常：{}", e.getMessage(), e);
            String filename = defaultIfBlank(annotation.filename(), unknownFilename) + ".error.txt";
            writeOne(pjp, annotation, null, new DownloadFileOutput(filename, ERROR_TEXT + e.getMessage()));
            return null;
        }
    }

    /**
     * 写入空下载文件
     *
     * @param pjp        切点
     * @param annotation 注解
     * @param object     调用方法返回值对象
     * @throws IOException 异常
     */
    @SuppressWarnings({"unchecked"})
    public void writeEmpty(ProceedingJoinPoint pjp, DownloadFile annotation, Object object) throws IOException {
        String filename = defaultIfBlank(annotation.filename(), unknownFilename);
        if (templateParser.isTemplate(filename)) {
            Object context = templateParser.createContext(pjp, object, null);
            filename = templateParser.parseTemplate(filename, context);
        }
        ResponseUtil.writeDownloadBytes(response, filename, annotation.contentType(), EMPTY_BYTE_ARRAY);
    }

    /**
     * 写入一个下载文件
     *
     * @param pjp        切点
     * @param annotation 注解
     * @param object     调用方法返回值对象
     * @param fileOutput 文件对象
     * @throws IOException 异常
     */
    @SuppressWarnings({"unchecked"})
    public void writeOne(ProceedingJoinPoint pjp, DownloadFile annotation, Object object, DownloadFileOutput fileOutput) throws IOException {
        String filename;
        if (fileOutput.isUseDefaultFilename()) {
            filename = defaultIfBlank(annotation.filename(), () -> defaultIfBlank(fileOutput.getFilename(), unknownFilename));
        } else {
            filename = defaultIfBlank(fileOutput.getFilename(), () -> defaultIfBlank(annotation.filename(), unknownFilename));
        }
        if (templateParser.isTemplate(filename)) {
            Object context = templateParser.createContext(pjp, object, null);
            filename = templateParser.parseTemplate(filename, context);
        }
        ResponseUtil.writeDownloadHeaders(response, filename, annotation.contentType());
        fileOutput.write(response.getOutputStream());
    }

    /**
     * 写入压缩包流
     *
     * @param pjp         切点
     * @param annotation  注解
     * @param object      调用方法返回值对象
     * @param fileOutputs 文件列表
     * @throws IOException 异常
     */
    @SuppressWarnings({"unchecked"})
    public void writeZip(ProceedingJoinPoint pjp, DownloadFile annotation, Object object, List<DownloadFileOutput> fileOutputs) throws IOException {
        String filename = defaultIfBlank(annotation.filename(), () -> "压缩包." + annotation.compressFormat());
        if (templateParser.isTemplate(filename)) {
            Object context = templateParser.createContext(pjp, object, null);
            filename = templateParser.parseTemplate(filename, context);
        }

        String extension = FilenameUtils.getExtension(filename);
        if (extension.isBlank()) {
            filename += "." + annotation.compressFormat();
        }

        downloadFileHandler.compressFiles(response, annotation, filename, fileOutputs);
    }

    /**
     * 获取文件列表
     *
     * @param filename      文件名称
     * @param object        文件对象
     * @param isModelObject 是否是模型文件对象
     * @return 文件列表
     * @throws IOException 异常
     */
    public List<DownloadFileOutput> getFileOutputs(final String filename, final Object object, boolean isModelObject) throws IOException {
        if (object == null) {
            return Collections.emptyList();
        }
        if (object.getClass().isAnnotationPresent(DownloadFileModel.class)) {
            try {
                DownloadFileMeta fileModel = getFileModel(object);
                return getFileOutputs(defaultIfBlank(fileModel.getFilename(), filename), fileModel.getSource(), true);
            } catch (Exception e) {
                return Collections.singletonList(new DownloadFileOutput(filename + ".error.txt", ERROR_TEXT + object));
            }
        } else if (object instanceof DownloadFileMeta fileModel) {
            return getFileOutputs(defaultIfBlank(fileModel.getFilename(), filename), fileModel.getSource(), true);
        }
        DownloadFileOutput fileOutput = null;
        if (object instanceof String string) {
            String zipEntryName = defaultIfBlank(filename, () -> downloadFileHandler.getFilename(string));
            InputStream inputStream = downloadFileHandler.getFileInputStream(string);
            if (inputStream != null) {
                fileOutput = new DownloadFileOutput(zipEntryName, inputStream);
            } else {
                fileOutput = new DownloadFileOutput(zipEntryName + ".error.txt", ERROR_TEXT + string);
            }
            if (!isModelObject || filename == null) {
                fileOutput.setUseDefaultFilename();
            }
        } else if (object instanceof File file) {
            String zipEntryName = defaultIfBlank(filename, file::getName);
            if (!file.exists() || !file.isFile()) {
                fileOutput = new DownloadFileOutput(zipEntryName + ".error.txt", ERROR_TEXT + zipEntryName);
            } else {
                fileOutput = new DownloadFileOutput(zipEntryName, new FileInputStream(file));
            }
            if (!isModelObject || filename == null) {
                fileOutput.setUseDefaultFilename();
            }
        } else if (object instanceof InputStream inputStream) {
            fileOutput = new DownloadFileOutput(filename, inputStream);
        } else if (object instanceof byte[] bytes) {
            fileOutput = new DownloadFileOutput(filename, bytes);
        } else if (object instanceof Resource resource) {
            String zipEntryName = defaultIfBlank(filename, resource::getFilename);
            fileOutput = new DownloadFileOutput(zipEntryName, resource.getInputStream());
            if (!isModelObject || filename == null) {
                fileOutput.setUseDefaultFilename();
            }
        } else if (object instanceof Collection<?> collection) {
            List<DownloadFileOutput> fileOutputs = new ArrayList<>();
            for (Object o : collection) {
                fileOutputs.addAll(getFileOutputs(filename, o, false));
            }
            return fileOutputs;
        } else if (object instanceof DownloadFileOutput fileOutput1) {
            fileOutput = fileOutput1;
        }
        return fileOutput == null ? Collections.emptyList() : Collections.singletonList(fileOutput);
    }

    /**
     * 使用默认值
     *
     * @param value            值
     * @param defaultValueFunc 默认值
     * @return 值
     */
    public static String defaultIfBlank(String value, Supplier<String> defaultValueFunc) {
        if (value == null || value.isBlank()) {
            return defaultValueFunc.get();
        }
        return value;
    }

    /**
     * 使用默认值
     *
     * @param value        值
     * @param defaultValue 默认值
     * @return 值
     */
    public static String defaultIfBlank(String value, String defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 获取一个不会重复的文件名称
     *
     * @param filenameSets 已存在的文件名称列表
     * @param filename     文件名称
     * @return 不会重复的文件名称
     */
    public static String getFilename(Set<String> filenameSets, String filename) {
        String newFilename = filename;
        int lastDot = newFilename.lastIndexOf('.');
        String prefix = newFilename.substring(0, lastDot);
        String extension = newFilename.substring(lastDot + 1);
        int index = 1;
        while (filenameSets.contains(newFilename)) {
            newFilename = prefix + ".duplicate-" + index++ + "." + extension;
        }
        filenameSets.add(newFilename);
        return newFilename;
    }

    /**
     * 获取文件模型信息
     *
     * @param object 文件对象
     * @return 文件模型
     * @throws IllegalAccessException    异常
     * @throws InvocationTargetException 异常
     */
    public DownloadFileMeta getFileModel(Object object) throws IllegalAccessException, InvocationTargetException {
        String filename = null;
        Object o = null;
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (filename == null) {
                DownloadFileName annotation1 = declaredField.getAnnotation(DownloadFileName.class);
                if (annotation1 != null && declaredField.trySetAccessible()) {
                    filename = ObjectUtils.getDisplayString(declaredField.get(object));
                }
            }
            if (o == null) {
                DownloadFileObject annotation2 = declaredField.getAnnotation(DownloadFileObject.class);
                if (annotation2 != null && declaredField.trySetAccessible()) {
                    o = declaredField.get(object);
                }
            }
            if (filename != null && o != null) {
                break;
            }
        }
        if (filename == null || o == null) {
            Method[] declaredMethods = object.getClass().getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if (filename == null) {
                    DownloadFileName annotation1 = declaredMethod.getAnnotation(DownloadFileName.class);
                    if (annotation1 != null && declaredMethod.trySetAccessible()) {
                        filename = ObjectUtils.getDisplayString(declaredMethod.invoke(object));
                    }
                }
                if (o == null) {
                    DownloadFileObject annotation2 = declaredMethod.getAnnotation(DownloadFileObject.class);
                    if (annotation2 != null && declaredMethod.trySetAccessible()) {
                        o = declaredMethod.invoke(object);
                    }
                }
                if (filename != null && o != null) {
                    break;
                }
            }
        }
        return new DownloadFileMeta(filename, o == null ? new byte[0] : o);
    }

}
