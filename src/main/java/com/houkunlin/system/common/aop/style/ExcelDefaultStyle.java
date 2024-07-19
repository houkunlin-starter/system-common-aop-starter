package com.houkunlin.system.common.aop.style;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.RowWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.DefaultStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 固定标题行。重写标题行的样式，设置默认行高度 25
 *
 * @author HouKunLin
 * @see DefaultStyle
 */
@Component
@ConditionalOnClass(EasyExcel.class)
public class ExcelDefaultStyle extends HorizontalCellStyleStrategy implements SheetWriteHandler, RowWriteHandler {
    public ExcelDefaultStyle() {
        super();
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setWrapped(true);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setLocked(true);

        headWriteCellStyle.setFillPatternType(FillPatternType.NO_FILL);
        headWriteCellStyle.setFillForegroundColor(IndexedColors.AUTOMATIC.getIndex());
        headWriteCellStyle.setBorderTop(BorderStyle.NONE);
        headWriteCellStyle.setBorderBottom(BorderStyle.NONE);
        headWriteCellStyle.setBorderLeft(BorderStyle.NONE);
        headWriteCellStyle.setBorderRight(BorderStyle.NONE);

        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("宋体");
        headWriteFont.setFontHeightInPoints((short) 11);
        headWriteFont.setBold(false);
        headWriteCellStyle.setWriteFont(headWriteFont);

        setHeadWriteCellStyle(headWriteCellStyle);
        setContentWriteCellStyleList(Collections.singletonList(headWriteCellStyle));
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        sheet.createFreezePane(0, 1, 0, 1);
    }

    @Override
    public void afterRowDispose(RowWriteHandlerContext context) {
        context.getRow().setHeightInPoints(25);
    }
}
