package com.houkunlin.system.common.aop;

import com.deepoove.poi.XWPFTemplate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.web.servlet.view.AbstractView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Word 模板渲染导出下载
 *
 * @author HouKunLin
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class PoiWordView extends AbstractView {
    public static final String FILENAME = "__FILENAME";
    public static final String TEMPLATE = "__TEMPLATE";
    public static final String DATA = "__DATA";
    /**
     * 文件名
     */
    protected String filename;
    /**
     * 模板文件，{@link #templateInputStream} 和 {@link #templateFile} 只能二选一，优先使用 {@link #templateInputStream}
     *
     * @see XWPFTemplate#compile(String)
     * @see XWPFTemplate#compile(File)
     * @see XWPFTemplate#compile(InputStream)
     */
    protected InputStream templateInputStream;
    /**
     * 模板文件，{@link #templateInputStream} 和 {@link #templateFile} 只能二选一，优先使用 {@link #templateInputStream}
     *
     * @see XWPFTemplate#compile(String)
     * @see XWPFTemplate#compile(File)
     * @see XWPFTemplate#compile(InputStream)
     */
    protected File templateFile;
    /**
     * 数据
     */
    protected Object data;

    public PoiWordView() {
        setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    }

    public PoiWordView(String filename) {
        this();
        this.filename = filename;
    }

    public PoiWordView(String filename, InputStream templateInputStream) {
        this();
        this.filename = filename;
        this.templateInputStream = templateInputStream;
    }

    public PoiWordView(String filename, InputStream templateInputStream, Object data) {
        this();
        this.filename = filename;
        this.templateInputStream = templateInputStream;
        this.data = data;
    }

    public PoiWordView(String filename, File templateFile) {
        this();
        this.filename = filename;
        this.templateFile = templateFile;
    }

    public PoiWordView(String filename, File templateFile, Object data) {
        this();
        this.filename = filename;
        this.templateFile = templateFile;
        this.data = data;
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        XWPFTemplate template = null;
        InputStream templateInputStream = getTemplateInputStream(model, request);
        if (templateInputStream != null) {
            template = XWPFTemplate.compile(templateInputStream);
        } else {
            File templateFile = getTemplateFile(model, request);
            if (templateFile != null) {
                template = XWPFTemplate.compile(templateFile);
            }
        }
        if (template == null) {
            throw new IOException("请正确配置模板文件，未找到模板文件");
        }

        sendHeader(model, request, response);

        model.remove(FILENAME);
        model.remove(TEMPLATE);

        Object data = getData(model, request);
        template.render(data).writeAndClose(response.getOutputStream());
    }

    protected void sendHeader(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(getContentType());
        String filename = getFilename(model, request);
        ContentDisposition contentDisposition = ContentDisposition.builder(!generatesDownloadContent() ? "inline" : "attachment")
                .filename(filename + ".docx", StandardCharsets.UTF_8)
                .build();
        response.setHeader("Content-Disposition", contentDisposition.toString());
    }

    public String getFilename(Map<String, Object> model, HttpServletRequest request) {
        if (model.get(FILENAME) instanceof String _value) {
            return _value;
        }
        return filename;
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

    public Object getData(Map<String, Object> model, HttpServletRequest request) {
        Object _data = model.get(DATA);
        if (_data != null) {
            return _data;
        }
        if (data != null) {
            return data;
        }
        return model;
    }
}
