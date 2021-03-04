package com.orion.office.excel.split;

import com.orion.office.excel.Excels;
import org.apache.poi.ss.usermodel.*;

/**
 * Excel 列拆分
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/20 18:30
 */
class ExcelColumnSplitSupport {

    /**
     * 拆分
     *
     * @param sourceSheet    原sheet
     * @param targetWorkbook 目标workbook
     * @param targetSheet    目标sheet
     * @param columns        列
     * @param headers        表头
     * @param skip           跳过的行
     * @param streaming      是否为流式读取
     */
    protected static void split(Sheet sourceSheet, Workbook targetWorkbook, Sheet targetSheet,
                                int[] columns, String[] headers, int skip, boolean streaming) {
        int index = 0, rowIndex = 0;
        if (!streaming) {
            targetSheet.setDefaultColumnWidth(sourceSheet.getDefaultColumnWidth());
            targetSheet.setDefaultRowHeightInPoints(sourceSheet.getDefaultRowHeightInPoints());
            for (int column : columns) {
                targetSheet.setColumnWidth(column, sourceSheet.getColumnWidth(column));
                targetSheet.setDefaultColumnStyle(column, sourceSheet.getColumnStyle(column));
            }
        }
        // 表头
        if (headers != null) {
            Row headerRow = targetSheet.createRow(rowIndex++);
            for (int headerIndex = 0; headerIndex < headers.length; headerIndex++) {
                Cell headerCell = headerRow.createCell(headerIndex);
                headerCell.setCellValue(headers[headerIndex]);
            }
        }
        for (Row sourceRow : sourceSheet) {
            // skip
            if (index++ < skip) {
                continue;
            }
            int cellIndex = 0;
            Row targetRow = targetSheet.createRow(rowIndex++);
            for (int column : columns) {
                Cell targetCell = targetRow.createCell(cellIndex++);
                Cell sourceCell = sourceRow.getCell(column);
                Excels.copyCellValue(sourceCell, targetCell);
                if (!streaming) {
                    CellStyle targetStyle = targetWorkbook.createCellStyle();
                    targetStyle.cloneStyleFrom(sourceCell.getCellStyle());
                    targetCell.setCellStyle(targetStyle);
                }
            }
        }
    }

}
