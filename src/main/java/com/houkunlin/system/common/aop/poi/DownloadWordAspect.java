package com.houkunlin.system.common.aop.poi;

import com.deepoove.poi.XWPFTemplate;
import com.houkunlin.system.common.aop.ResponseUtil;
import com.houkunlin.system.common.aop.TemplateParser;
import com.houkunlin.system.common.aop.annotation.DownloadWord;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Word 模板渲染导出下载
 *
 * @author HouKunLin
 * @see DownloadWord
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class DownloadWordAspect {
    private final TemplateParser templateParser;
    private final DownloadPoiHandler downloadPoiHandler;
    private final HttpServletResponse response;

    @Around("@annotation(annotation)")
    public Object doAround(ProceedingJoinPoint pjp, DownloadWord annotation) throws Throwable {
        try {
            Object object = pjp.proceed();
            try {
                if (object instanceof WordData wordData) {
                    renderWord(pjp, annotation, wordData.filename(), wordData.data());
                } else {
                    renderWord(pjp, annotation, annotation.filename(), object);
                }
            } catch (IOException e) {
                log.error("下载 Word 文件失败，写 Word 文件流失败", e);
                return object;
            }
        } catch (Throwable e) {
            log.error("下载 Word 文件失败，发生了异常：{}", e.getMessage());
            throw e;
        }
        return null;
    }

    @SuppressWarnings({"unchecked"})
    private void renderWord(ProceedingJoinPoint pjp, DownloadWord annotation, String filename, Object data) throws IOException {
        String withTemplate = annotation.withTemplate();
        if (withTemplate.isBlank()) {
            throw new IOException("请正确配置模板文件");
        }
        InputStream templateInputStream = downloadPoiHandler.getTemplate(withTemplate);
        if (templateInputStream == null) {
            if (log.isWarnEnabled()) {
                log.warn("有传入模板文件名称，但并未正常得到模板文件内容：{}", withTemplate);
            }
            throw new IOException("未找到模板文件" + withTemplate);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XWPFTemplate.compile(templateInputStream).render(data).writeAndClose(byteArrayOutputStream);

        if (templateParser.isTemplate(filename)) {
            Object context = templateParser.createContext(pjp, data, null);
            filename = templateParser.parseTemplate(filename, context);
        }

        String extension = FilenameUtils.getExtension(filename);
        if (extension.isEmpty()) {
            String extension1;
            int adsIndex = withTemplate.indexOf(':');
            if (adsIndex != -1) {
                extension1 = FilenameUtils.getExtension(withTemplate.substring(adsIndex + 1));
            } else {
                extension1 = FilenameUtils.getExtension(withTemplate);
            }
            if (!extension1.isEmpty()) {
                filename = filename + "." + extension1;
            } else {
                filename = filename + ".docx";
            }
        }

        byte[] byteArray = byteArrayOutputStream.toByteArray();

        ResponseUtil.writeDownloadBytes(response, filename, annotation.contentType(), byteArray);
    }
}
