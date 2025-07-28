package com.houkunlin.system.common.aop;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.write.handler.WriteHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Excel 导出下载
 *
 * @author HouKunLin
 * @see DownloadExcel
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class DownloadExcelAspect {
    private final TemplateParser templateParser;
    private final DownloadPoiHandler downloadPoiHandler;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ApplicationContext applicationContext;

    @Around("@annotation(annotation)")
    public Object doAround(ProceedingJoinPoint pjp, DownloadExcel annotation) throws Throwable {
        try {
            Object object = pjp.proceed();
            FastExcelView view;
            if (object instanceof ExcelData excelData) {
                String filename = excelData.filename();
                Object data = excelData.data();
                String _filename = getFilename(pjp, filename, data);
                view = new FastExcelView(_filename, data, annotation.excelType());
            } else {
                String filename = getFilename(pjp, annotation.filename(), object);
                view = new FastExcelView(filename, object, annotation.excelType());
            }
            view.setSheetName(annotation.sheetName());
            view.setInMemory(annotation.inMemory());
            view.setCharset(Charset.forName(annotation.charset()));
            view.setPassword(annotation.password());
            view.setUse1904windowing(annotation.use1904windowing());
            view.setUseDefaultStyle(annotation.useDefaultStyle());
            view.setNeedHead(annotation.needHead());
            String withTemplate = annotation.withTemplate();
            if (!withTemplate.isBlank()) {
                try {
                    InputStream templateInputStream = downloadPoiHandler.getTemplate(withTemplate);
                    if (templateInputStream == null && log.isWarnEnabled()) {
                        log.warn("有传入模板文件名称，但并未正常得到模板文件内容：{}", withTemplate);
                    }
                    view.setTemplateInputStream(templateInputStream);
                } catch (IOException e) {
                    log.warn("读取 Excel 模板文件 {} 失败", withTemplate);
                }
            }

            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();

            if (method.isAnnotationPresent(DownloadExcelWriteHandler.class)) {
                DownloadExcelWriteHandler downloadExcelWriteHandler = method.getAnnotation(DownloadExcelWriteHandler.class);
                if (downloadExcelWriteHandler != null) {
                    Class<? extends WriteHandler>[] classes = downloadExcelWriteHandler.value();
                    WriteHandler[] writeHandlers = new WriteHandler[classes.length];
                    for (int i = 0; i < classes.length; i++) {
                        writeHandlers[i] = getInstance(classes[i]);
                    }
                    view.setWriteHandlers(writeHandlers);
                }
            }

            if (method.isAnnotationPresent(DownloadExcelConverter.class)) {
                DownloadExcelConverter downloadExcelConverter = method.getAnnotation(DownloadExcelConverter.class);
                if (downloadExcelConverter != null) {
                    Class<? extends Converter>[] classes = downloadExcelConverter.value();
                    Converter<?>[] converters = new Converter[classes.length];
                    for (int i = 0; i < classes.length; i++) {
                        converters[i] = getInstance(classes[i]);
                    }
                    view.setConverters(converters);
                }
            }

            view.setDataClass(annotation.dataClass());
            view.render(new HashMap<>(), request, response);
            return null;
        } catch (Throwable e) {
            log.error("下载 Excel 文件失败，发生了异常：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 获取下载文件名（文件名可能是模板字符串）
     *
     * @param pjp        切点对象
     * @param filename   文件名
     * @param data       返回值的数据
     * @return 文件名（经过模板处理后的字符串）
     */
    @SuppressWarnings({"unchecked"})
    private String getFilename(ProceedingJoinPoint pjp, String filename, Object data) {
        if (templateParser.isTemplate(filename)) {
            Object context = templateParser.createContext(pjp, data, null);
            filename = templateParser.parseTemplate(filename, context);
        }
        return filename;
    }

    /**
     * 获得一个对象的实例
     *
     * @param clazz class 对象
     * @param <T>   对象类型
     * @return 实例
     */
    private <T> T getInstance(Class<T> clazz) {
        String[] beanNamesForType = applicationContext.getBeanNamesForType(clazz);
        if (beanNamesForType.length > 0) {
            return clazz.cast(applicationContext.getBean(beanNamesForType[0]));
        }
        try {
            Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();
            return declaredConstructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            log.error("实例化 {} 对象出现异常", clazz, e);
        }
        return null;
    }
}
