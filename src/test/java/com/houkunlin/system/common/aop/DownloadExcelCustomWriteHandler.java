package com.houkunlin.system.common.aop;

import com.alibaba.excel.constant.OrderConstant;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.context.RowWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.*;

public class DownloadExcelCustomWriteHandler extends HorizontalCellStyleStrategy implements RowWriteHandler {

    @Override
    public int order() {
        return OrderConstant.DEFAULT_DEFINE_STYLE;
    }

    public DownloadExcelCustomWriteHandler() {
        super();
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setWrapped(true);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setLocked(true);
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("宋体");
        headWriteFont.setFontHeightInPoints((short)14);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);

        setHeadWriteCellStyle(headWriteCellStyle);
    }

    @Override
    public void afterRowDispose(RowWriteHandlerContext context) {
        if (context.getHead() == null) {
            return;
        }
        if (context.getHead()) {
            context.getRow().setHeightInPoints(30);
        } else {
            context.getRow().setHeightInPoints(25);
        }
    }
}
