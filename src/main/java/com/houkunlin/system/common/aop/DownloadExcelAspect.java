package com.houkunlin.system.common.aop;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.WriteDirectionEnum;
import cn.idev.excel.write.builder.ExcelWriterBuilder;
import cn.idev.excel.write.handler.WriteHandler;
import cn.idev.excel.write.metadata.WriteSheet;
import cn.idev.excel.write.metadata.fill.FillConfig;
import cn.idev.excel.write.metadata.fill.FillWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

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
    private final HttpServletResponse response;
    private final ApplicationContext applicationContext;

    @Around("@annotation(annotation)")
    public Object doAround(ProceedingJoinPoint pjp, DownloadExcel annotation) throws Throwable {
        try {
            Object object = pjp.proceed();
            if (object instanceof Collection<?> collection) {
                String filename = getFilename(pjp, annotation, object);
                ResponseUtil.writeDownloadHeaders(response, filename + annotation.excelType().getValue(), annotation.contentType(), false);
                renderExcel(response.getOutputStream(), pjp, annotation, collection);
                return null;
            } else if (object instanceof Map<?, ?> map) {
                String filename = getFilename(pjp, annotation, object);
                ResponseUtil.writeDownloadHeaders(response, filename + annotation.excelType().getValue(), annotation.contentType(), false);
                renderExcel(response.getOutputStream(), pjp, annotation, map);
                return null;
            }
            return object;
        } catch (Throwable e) {
            log.error("下载 Excel 文件失败，发生了异常：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 渲染 Excel 内容
     *
     * @param outputStream 数据写入对象
     * @param pjp          切点对象
     * @param annotation   注解内容
     * @param data         返回的数据对象
     */
    private void renderExcel(OutputStream outputStream, ProceedingJoinPoint pjp, DownloadExcel annotation, Collection<?> data) {
        ExcelWriterBuilder excelWriterBuilder = getExcelWriterBuilder(outputStream, pjp, annotation);

        boolean isNotTemplate = !useTemplate(excelWriterBuilder, annotation);

        try (ExcelWriter excelWriter = excelWriterBuilder.build()) {
            WriteSheet writeSheet;
            if (!annotation.sheetName().isBlank()) {
                writeSheet = FastExcel.writerSheet(annotation.sheetName()).build();
            } else {
                writeSheet = FastExcel.writerSheet("Sheet1").build();
            }

            if (isNotTemplate) {
                excelWriter.write(data, writeSheet);
            } else {
                excelWriter.fill(data, writeSheet);
            }
            excelWriter.finish();
        }
    }

    /**
     * 渲染 Excel 内容
     *
     * @param outputStream 数据写入对象
     * @param pjp          切点对象
     * @param annotation   注解内容
     * @param map          返回的数据对象
     */
    private void renderExcel(OutputStream outputStream, ProceedingJoinPoint pjp, DownloadExcel annotation, Map<?, ?> map) {
        ExcelWriterBuilder excelWriterBuilder = getExcelWriterBuilder(outputStream, pjp, annotation);

        useTemplate(excelWriterBuilder, annotation);

        try (ExcelWriter excelWriter = excelWriterBuilder.build()) {
            WriteSheet writeSheet;
            if (!annotation.sheetName().isBlank()) {
                writeSheet = FastExcel.writerSheet(annotation.sheetName()).build();
            } else {
                writeSheet = FastExcel.writerSheet("Sheet1").build();
            }

            map.forEach((k, v) -> {
                if (v instanceof FillWrapper fillWrapper) {
                    if (fillWrapper.getName() == null) {
                        fillWrapper.setName(ObjectUtils.getDisplayString(k));
                    }
                    FillConfig fillConfig = getFillConfig(map, k);
                    excelWriter.fill(fillWrapper, fillConfig, writeSheet);
                } else if (v instanceof Collection<?> collection) {
                    FillConfig fillConfig = getFillConfig(map, k);
                    excelWriter.fill(new FillWrapper(ObjectUtils.getDisplayString(k), collection), fillConfig, writeSheet);
                } else {
                    excelWriter.fill(Map.of(k, v), writeSheet);
                }
            });
            excelWriter.finish();
        }
    }

    /**
     * 获取填充配置（主要判断该 KEY 对应的列表数据是否需要水平方向填充）
     *
     * @param map 返回的map数据
     * @param key 当前key值
     * @return 当前key数据对应的填充配置
     */
    private FillConfig getFillConfig(Map<?, ?> map, Object key) {
        Object horizontal = map.get(key + DownloadExcel.HORIZONTAL_SUFFIX);
        if (horizontal == null) {
            return null;
        }
        if (horizontal instanceof Boolean b) {
            if (b) {
                return FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            }
        } else if (horizontal instanceof String s) {
            if ("true".equalsIgnoreCase(s)) {
                return FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            }
        }
        return null;
    }

    /**
     * 获取下载文件名（文件名可能是模板字符串）
     *
     * @param pjp        切点对象
     * @param annotation 注解对象
     * @param data       返回值的数据
     * @return 文件名（经过模板处理后的字符串）
     */
    @SuppressWarnings({"unchecked"})
    private String getFilename(ProceedingJoinPoint pjp, DownloadExcel annotation, Object data) {
        String filename = annotation.filename();
        if (templateParser.isTemplate(filename)) {
            Object context = templateParser.createContext(pjp, data, null);
            filename = templateParser.parseTemplate(filename, context);
        }
        return filename;
    }

    /**
     * 获取 Excel 写入对象
     *
     * @param outputStream 数据写入对象
     * @param pjp          切点对象
     * @param annotation   注解内容
     * @return Excel 写入对象
     */
    private ExcelWriterBuilder getExcelWriterBuilder(OutputStream outputStream, ProceedingJoinPoint pjp, DownloadExcel annotation) {
        Class<?> dataClass = annotation.dataClass();
        if (dataClass == Object.class) {
            dataClass = null;
        }

        ExcelWriterBuilder writerBuilder = FastExcel.write(outputStream, dataClass)
                .excelType(annotation.excelType())
                .inMemory(annotation.inMemory())
                .charset(Charset.forName(annotation.charset()))
                .use1904windowing(annotation.use1904windowing())
                .useDefaultStyle(annotation.useDefaultStyle())
                .needHead(annotation.needHead());
        if (!annotation.password().isBlank()) {
            writerBuilder.password(annotation.password());
        }

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        if (method.isAnnotationPresent(DownloadExcelWriteHandler.class)) {
            DownloadExcelWriteHandler downloadExcelWriteHandler = method.getAnnotation(DownloadExcelWriteHandler.class);
            loadWriteHandler(downloadExcelWriteHandler.value(), writerBuilder::registerWriteHandler);
        }

        if (method.isAnnotationPresent(DownloadExcelConverter.class)) {
            DownloadExcelConverter downloadExcelConverter = method.getAnnotation(DownloadExcelConverter.class);
            loadConverter(downloadExcelConverter.value(), writerBuilder::registerConverter);
        }

        return writerBuilder;
    }

    /**
     * 使用模板文件
     *
     * @param writerBuilder Excel 写入构建对象
     * @param annotation    注解内容
     * @return 是否使用模板文件
     */
    private boolean useTemplate(ExcelWriterBuilder writerBuilder, DownloadExcel annotation) {
        boolean isTemplate = false;
        String withTemplate = annotation.withTemplate();
        if (!withTemplate.isBlank()) {
            try {
                InputStream templateInputStream = downloadPoiHandler.getTemplate(withTemplate);
                if (templateInputStream == null && log.isWarnEnabled()) {
                    log.warn("有传入模板文件名称，但并未正常得到模板文件内容：{}", withTemplate);
                }
                writerBuilder.withTemplate(templateInputStream);
                isTemplate = true;
            } catch (IOException e) {
                log.warn("读取 Excel 模板文件 {} 失败", withTemplate);
            }
        }
        return isTemplate;
    }

    /**
     * 加载写处理器
     *
     * @param writeHandlers 处理器类列表
     * @param function      添加处理器方法
     */
    private void loadWriteHandler(Class<? extends WriteHandler>[] writeHandlers, Function<WriteHandler, ExcelWriterBuilder> function) {
        for (Class<? extends WriteHandler> writeHandler : writeHandlers) {
            WriteHandler instance = getInstance(writeHandler);
            if (instance != null) {
                function.apply(instance);
            }
        }
    }

    /**
     * 加载转换器
     *
     * @param converters 转换器类列表
     * @param function   添加转换器方法
     */
    @SuppressWarnings({"rawtypes"})
    private void loadConverter(Class<? extends Converter>[] converters, Function<Converter<?>, ExcelWriterBuilder> function) {
        for (Class<? extends Converter> converter : converters) {
            Converter instance = getInstance(converter);
            if (instance != null) {
                function.apply(instance);
            }
        }
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
