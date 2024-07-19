package com.houkunlin.system.common.aop;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * Excel 导出下载
 *
 * @author HouKunLin
 * @see DownloadExcel
 */
@Slf4j
@Aspect
@Component
@ConditionalOnClass(EasyExcel.class)
@RequiredArgsConstructor
public class DownloadExcelAspect {
    private final TemplateParser templateParser;
    private final DownloadPoiHandler downloadPoiHandler;
    private final HttpServletResponse response;
    private final ApplicationContext applicationContext;

    @Around("@annotation(annotation)")
    public Object doBefore(ProceedingJoinPoint pjp, DownloadExcel annotation) throws Throwable {
        try {
            Object object = pjp.proceed();
            if (object instanceof Collection<?> collection) {
                try {
                    renderExcel(pjp, annotation, collection);
                } catch (IOException e) {
                    log.error("下载 Excel 文件失败，写 Excel 文件流失败", e);
                    return object;
                }
                return null;
            }
            return object;
        } catch (Throwable e) {
            log.error("下载 Excel 文件失败，发生了异常：{}", e.getMessage());
            throw e;
        }
    }

    @SuppressWarnings({"unchecked"})
    private void renderExcel(ProceedingJoinPoint pjp, DownloadExcel annotation, Collection<?> data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Class<?> dataClass = annotation.dataClass();
        if (dataClass == Object.class) {
            dataClass = null;
        }

        ExcelWriterBuilder writerBuilder = EasyExcel.write(byteArrayOutputStream, dataClass)
                .excelType(annotation.excelType())
                .inMemory(annotation.inMemory())
                .charset(Charset.forName(annotation.charset()))
                .use1904windowing(annotation.use1904windowing())
                .useDefaultStyle(annotation.useDefaultStyle())
                .needHead(annotation.needHead());
        if (!annotation.password().isBlank()) {
            writerBuilder.password(annotation.password());
        }
        boolean isNotTemplate = true;
        String withTemplate = annotation.withTemplate();
        if (!withTemplate.isBlank()) {
            try {
                InputStream templateInputStream = downloadPoiHandler.getTemplate(withTemplate);
                if (templateInputStream == null && log.isWarnEnabled()) {
                    log.warn("有传入模板文件名称，但并未正常得到模板文件内容：{}", withTemplate);
                }
                writerBuilder.withTemplate(templateInputStream);
                isNotTemplate = false;
            } catch (IOException e) {
                log.warn("读取 Excel 模板文件 {} 失败", withTemplate);
            }
        }
        ExcelWriterSheetBuilder excelWriterSheetBuilder;
        if (!annotation.sheetName().isBlank()) {
            excelWriterSheetBuilder = writerBuilder.sheet(annotation.sheetName());
        } else {
            excelWriterSheetBuilder = writerBuilder.sheet("Sheet1");
        }

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        if (method.isAnnotationPresent(DownloadExcelWriteHandler.class)) {
            DownloadExcelWriteHandler downloadExcelWriteHandler = method.getAnnotation(DownloadExcelWriteHandler.class);
            loadWriteHandler(excelWriterSheetBuilder, downloadExcelWriteHandler.value());
        }

        if (method.isAnnotationPresent(DownloadExcelConverter.class)) {
            DownloadExcelConverter downloadExcelConverter = method.getAnnotation(DownloadExcelConverter.class);
            loadConverter(excelWriterSheetBuilder, downloadExcelConverter.value());
        }

        if (isNotTemplate) {
            excelWriterSheetBuilder.doWrite(data);
        } else {
            excelWriterSheetBuilder.doFill(data);
        }

        String filename = annotation.filename();

        byte[] byteArray = byteArrayOutputStream.toByteArray();

        if (templateParser.isTemplate(filename)) {
            Object context = templateParser.createContext(pjp, data, null);
            filename = templateParser.parseTemplate(filename, context);
        }

        ResponseUtil.writeDownloadBytes(response, filename + annotation.excelType().getValue(), annotation.contentType(), byteArray);

        // return ResponseEntity.ok()
        //         .headers(headers)
        //         .contentType(MediaType.parseMediaType(annotation.contentType()))
        //         .contentLength(byteArray.length)
        //         .body(byteArray);
        // .body(new InputStreamResource(new ByteArrayInputStream(byteArray)));
    }

    private void loadWriteHandler(ExcelWriterSheetBuilder excelWriterSheetBuilder, Class<? extends WriteHandler>[] writeHandlers) {
        for (Class<? extends WriteHandler> writeHandler : writeHandlers) {
            WriteHandler instance = getInstance(writeHandler);
            if (instance != null) {
                excelWriterSheetBuilder.registerWriteHandler(instance);
            }
        }
    }

    @SuppressWarnings({"rawtypes"})
    private void loadConverter(ExcelWriterSheetBuilder excelWriterSheetBuilder, Class<? extends Converter>[] converters) {
        for (Class<? extends Converter> converter : converters) {
            Converter instance = getInstance(converter);
            if (instance != null) {
                excelWriterSheetBuilder.registerConverter(instance);
            }
        }
    }

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
