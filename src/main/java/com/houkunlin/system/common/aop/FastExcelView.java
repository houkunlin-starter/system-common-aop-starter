package com.houkunlin.system.common.aop;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.WriteDirectionEnum;
import cn.idev.excel.metadata.AbstractParameterBuilder;
import cn.idev.excel.support.ExcelTypeEnum;
import cn.idev.excel.write.builder.AbstractExcelWriterParameterBuilder;
import cn.idev.excel.write.builder.ExcelWriterBuilder;
import cn.idev.excel.write.handler.WriteHandler;
import cn.idev.excel.write.metadata.WriteSheet;
import cn.idev.excel.write.metadata.fill.FillConfig;
import cn.idev.excel.write.metadata.fill.FillWrapper;
import cn.idev.excel.write.style.DefaultStyle;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.ContentDisposition;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.view.AbstractView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

/**
 * Excel 导出下载
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FastExcelView extends AbstractView {
    public static final String EXCELTYPE = "_EXCEL_TYPE";
    public static final String FILENAME = "_FILENAME";
    public static final String SHEETNAME = "_SHEET_NAME";
    public static final String INMEMORY = "_IN_MEMORY";
    public static final String CHARSET = "_CHARSET";
    public static final String PASSWORD = "_PASSWORD";
    public static final String USE1904WINDOWING = "_USE1904WINDOWING";
    public static final String USEDEFAULTSTYLE = "_USE_DEFAULT_STYLE";
    public static final String NEEDHEAD = "_NEED_HEAD";
    public static final String TEMPLATE = "_TEMPLATE";
    public static final String WRITEHANDLERS = "_WRITE_HANDLERS";
    public static final String CONVERTERS = "_CONVERTERS";
    public static final String DATA = "_DATA";
    public static final String DATA_CLASS = "_DATA_CLASS";
    public static final String IS_WITH_TEMPLATE = "__FAST_WITH_TEMPLATE";
    /**
     * 导出 Excel 格式类型
     */
    protected ExcelTypeEnum excelType = ExcelTypeEnum.XLS;
    /**
     * 文件名
     *
     * @see ExcelTypeEnum
     */
    protected String filename;
    /**
     * 工作表名
     *
     * @see ExcelWriterBuilder#sheet(String)
     */
    protected String sheetName;
    /**
     * 是否在内存中完成。false：写入临时文件，true：在内存中完成
     *
     * @see ExcelWriterBuilder#inMemory(Boolean)
     */
    protected boolean inMemory;
    /**
     * 只有在写入为 CSV 文件时有效
     *
     * @see ExcelWriterBuilder#charset(Charset)
     */
    protected Charset charset = StandardCharsets.UTF_8;
    /**
     * 文件加密密码
     *
     * @see ExcelWriterBuilder#password(String)
     */
    protected String password;
    /**
     * excel中时间是存储1900年起的一个双精度浮点数，但是有时候默认开始日期是1904，所以设置这个值改成默认1904年开始。
     * 如果 Date 使用 1904 窗口化，则为 true，如果使用 1900 日期窗口化，则为 false。
     *
     * @see AbstractParameterBuilder#use1904windowing(Boolean)
     */
    protected boolean use1904windowing;
    /**
     * 是否使用默认的样式
     *
     * @see AbstractExcelWriterParameterBuilder#useDefaultStyle(Boolean)
     * @see DefaultStyle
     */
    protected boolean useDefaultStyle = true;
    /**
     * 是否需要写入头到excel
     *
     * @see AbstractExcelWriterParameterBuilder#needHead(Boolean)
     */
    protected boolean needHead = true;
    /**
     * 模板文件，{@link #templateInputStream} 和 {@link #templateFile} 只能二选一，优先使用 {@link #templateInputStream}
     *
     * @see ExcelWriterBuilder#withTemplate(String)
     * @see ExcelWriterBuilder#withTemplate(File)
     * @see ExcelWriterBuilder#withTemplate(InputStream)
     */
    protected InputStream templateInputStream;
    /**
     * 模板文件，{@link #templateInputStream} 和 {@link #templateFile} 只能二选一，优先使用 {@link #templateInputStream}
     *
     * @see ExcelWriterBuilder#withTemplate(String)
     * @see ExcelWriterBuilder#withTemplate(File)
     * @see ExcelWriterBuilder#withTemplate(InputStream)
     */
    protected File templateFile;
    /**
     * 写入处理器
     *
     * @see WriteHandler
     */
    protected WriteHandler[] writeHandlers;
    /**
     * 转换器
     *
     * @see Converter
     */
    protected Converter<?>[] converters;
    /**
     * 数据
     */
    protected Object data;
    /**
     * 数据的类型对象
     *
     * @see AbstractParameterBuilder#head(Class)
     */
    protected Class<?> dataClass;

    public FastExcelView() {
        this(ExcelTypeEnum.XLSX);
    }

    public FastExcelView(ExcelTypeEnum excelType) {
        switch (excelType) {
            case XLSX -> setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            case XLS -> setContentType("application/vnd.ms-excel");
            case CSV -> setContentType("text/csv");
        }
        setExcelType(excelType);
    }

    public FastExcelView(String filename, Object data) {
        this(ExcelTypeEnum.XLSX);
        this.filename = filename;
        this.data = data;
    }

    public FastExcelView(String filename, Object data, Class<?> dataClass) {
        this(ExcelTypeEnum.XLSX);
        this.filename = filename;
        this.data = data;
        this.dataClass = dataClass;
    }

    public FastExcelView(String filename, Object data, ExcelTypeEnum excelType) {
        this(excelType);
        this.filename = filename;
        this.data = data;
    }

    public FastExcelView(String filename, Object data, Class<?> dataClass, ExcelTypeEnum excelType) {
        this(excelType);
        this.filename = filename;
        this.data = data;
        this.dataClass = dataClass;
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    /**
     * Renders the Excel view, given the specified model.
     */
    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        sendHeader(model, request, response);

        // Create a fresh workbook instance for this render step.
        ExcelWriter workbook = createExcelWriter(model, request, response);

        // Delegate to application-provided document code.
        buildExcelDocument(model, workbook, request, response);
    }

    protected void sendHeader(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(getContentType());
        String filename = getFilename(model, request);
        ExcelTypeEnum excelType = getExcelType(model, request);
        ContentDisposition contentDisposition = ContentDisposition.builder(!generatesDownloadContent() ? "inline" : "attachment")
                .filename(filename + excelType.getValue(), StandardCharsets.UTF_8)
                .build();
        response.setHeader("Content-Disposition", contentDisposition.toString());
    }

    /**
     * Template method for creating the POI {@link Workbook} instance.
     * <p>The default implementation creates a traditional {@link HSSFWorkbook}.
     * Spring-provided subclasses are overriding this for the OOXML-based variants;
     * custom subclasses may override this for reading a workbook from a file.
     *
     * @param model   the model Map
     * @param request current HTTP request (for taking the URL or headers into account)
     * @return the new {@link Workbook} instance
     */
    protected ExcelWriter createExcelWriter(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Class<?> dataClass = getDataClass(model, request);
        if (dataClass == Object.class) {
            dataClass = null;
        }

        ExcelWriterBuilder writerBuilder = FastExcel.write(response.getOutputStream(), dataClass)
                .excelType(getExcelType(model, request))
                .inMemory(isInMemory(model, request))
                .charset(getCharset(model, request))
                .use1904windowing(isUse1904windowing(model, request))
                .useDefaultStyle(isUseDefaultStyle(model, request))
                .needHead(isNeedHead(model, request));
        String password = getPassword(model, request);
        if (password != null && !password.isBlank()) {
            writerBuilder.password(password);
        }

        WriteHandler[] writeHandlers = getWriteHandlers(model, request);
        if (writeHandlers != null) {
            for (WriteHandler writeHandler : writeHandlers) {
                writerBuilder.registerWriteHandler(writeHandler);
            }
        }

        Converter<?>[] converters = getConverters(model, request);
        if (converters != null) {
            for (Converter<?> converter : converters) {
                writerBuilder.registerConverter(converter);
            }
        }

        InputStream templateInputStream = getTemplateInputStream(model, request);
        if (templateInputStream != null) {
            writerBuilder.withTemplate(templateInputStream);
            model.put(IS_WITH_TEMPLATE, true);
        } else {
            File templateFile = getTemplateFile(model, request);
            if (templateFile != null) {
                writerBuilder.withTemplate(templateFile);
                model.put(IS_WITH_TEMPLATE, true);
            }
        }

        return writerBuilder.build();
    }

    protected WriteSheet createWriteSheet(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = getSheetName(model, request);
        if (sheetName != null && !sheetName.isBlank()) {
            return FastExcel.writerSheet(sheetName).build();
        } else {
            return FastExcel.writerSheet("Sheet1").build();
        }
    }

    /**
     * Application-provided subclasses must implement this method to populate
     * the Excel workbook document, given the model.
     *
     * @param model       the model Map
     * @param excelWriter the Excel workbook to populate
     * @param request     in case we need locale etc. Shouldn't look at attributes.
     * @param response    in case we need to set cookies. Shouldn't write to it.
     */
    protected void buildExcelDocument(Map<String, Object> model, ExcelWriter excelWriter, HttpServletRequest request, HttpServletResponse response) throws Exception {
        WriteSheet writeSheet = createWriteSheet(model, request, response);

        Object data = getData(model, request);
        if (data instanceof Collection<?> collection) {
            writeCollection(model, excelWriter, request, response, writeSheet, collection);
        } else if (data instanceof Map<?, ?> map) {
            writeMap(model, excelWriter, request, response, writeSheet, map);
        }

        excelWriter.finish();
    }

    protected void writeCollection(Map<String, Object> model, ExcelWriter excelWriter, HttpServletRequest request, HttpServletResponse response, WriteSheet writeSheet, Collection<?> data) {
        if (model.containsKey(IS_WITH_TEMPLATE)) {
            excelWriter.fill(data, writeSheet);
        } else {
            excelWriter.write(data, writeSheet);
        }
    }

    protected void writeMap(Map<String, Object> model, ExcelWriter excelWriter, HttpServletRequest request, HttpServletResponse response, WriteSheet writeSheet, Map<?, ?> data) {
        data.forEach((k, v) -> {
            if (v instanceof FillWrapper fillWrapper) {
                if (fillWrapper.getName() == null) {
                    fillWrapper.setName(ObjectUtils.getDisplayString(k));
                }
                FillConfig fillConfig = getFillConfig(data, k);
                excelWriter.fill(fillWrapper, fillConfig, writeSheet);
            } else if (v instanceof Collection<?> collection) {
                FillConfig fillConfig = getFillConfig(data, k);
                excelWriter.fill(new FillWrapper(ObjectUtils.getDisplayString(k), collection), fillConfig, writeSheet);
            } else {
                excelWriter.fill(Map.of(k, v), writeSheet);
            }
        });
    }

    /**
     * 获取填充配置（主要判断该 KEY 对应的列表数据是否需要水平方向填充）
     *
     * @param map 返回的map数据
     * @param key 当前key值
     * @return 当前key数据对应的填充配置
     */
    protected FillConfig getFillConfig(Map<?, ?> map, Object key) {
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

    public ExcelTypeEnum getExcelType(Map<String, Object> model, HttpServletRequest request) {
        if (model.get(EXCELTYPE) instanceof ExcelTypeEnum _value) {
            return _value;
        }
        return excelType;
    }

    public String getFilename(Map<String, Object> model, HttpServletRequest request) {
        if (model.get(FILENAME) instanceof String _value) {
            return _value;
        }
        return filename;
    }

    public String getSheetName(Map<String, Object> model, HttpServletRequest request) {
        if (model.get(SHEETNAME) instanceof String _value) {
            return _value;
        }
        return sheetName;
    }

    public boolean isInMemory(Map<String, Object> model, HttpServletRequest request) {
        if (model.get(INMEMORY) instanceof Boolean _value) {
            return _value;
        }
        return inMemory;
    }

    public Charset getCharset(Map<String, Object> model, HttpServletRequest request) {
        Object _charset = model.get(CHARSET);
        if (_charset instanceof Charset _value) {
            return _value;
        } else if (_charset instanceof String _value) {
            return Charset.forName(_value);
        }
        return charset;
    }

    public String getPassword(Map<String, Object> model, HttpServletRequest request) {
        if (model.get(PASSWORD) instanceof String _value) {
            return _value;
        }
        return password;
    }

    public boolean isUse1904windowing(Map<String, Object> model, HttpServletRequest request) {
        if (model.get(USE1904WINDOWING) instanceof Boolean _value) {
            return _value;
        }
        return use1904windowing;
    }

    public boolean isUseDefaultStyle(Map<String, Object> model, HttpServletRequest request) {
        if (model.get(USEDEFAULTSTYLE) instanceof Boolean _value) {
            return _value;
        }
        return useDefaultStyle;
    }

    public boolean isNeedHead(Map<String, Object> model, HttpServletRequest request) {
        if (model.get(NEEDHEAD) instanceof Boolean _value) {
            return _value;
        }
        return needHead;
    }

    public InputStream getTemplateInputStream(Map<String, Object> model, HttpServletRequest request) {
        if (model.get(TEMPLATE) instanceof InputStream _value) {
            return _value;
        }
        return templateInputStream;
    }

    public File getTemplateFile(Map<String, Object> model, HttpServletRequest request) {
        if (model.get(TEMPLATE) instanceof File _value) {
            return _value;
        }
        return templateFile;
    }

    public WriteHandler[] getWriteHandlers(Map<String, Object> model, HttpServletRequest request) {
        if (model.get(WRITEHANDLERS) instanceof WriteHandler[] _value) {
            return _value;
        }
        return writeHandlers;
    }

    public Converter<?>[] getConverters(Map<String, Object> model, HttpServletRequest request) {
        if (model.get(CONVERTERS) instanceof Converter<?>[] _value) {
            return _value;
        }
        return converters;
    }

    public Object getData(Map<String, Object> model, HttpServletRequest request) {
        Object _data = model.get(DATA);
        if (_data != null) {
            return _data;
        }
        return data;
    }

    public Class<?> getDataClass(Map<String, Object> model, HttpServletRequest request) {
        Object _dataClass = model.get(DATA_CLASS);
        if (_dataClass instanceof Class<?> _value) {
            return _value;
        }
        return dataClass;
    }
}
