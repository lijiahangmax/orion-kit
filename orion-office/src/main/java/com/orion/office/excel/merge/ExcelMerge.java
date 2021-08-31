package com.orion.office.excel.merge;

import com.orion.office.excel.Excels;
import com.orion.office.excel.writer.BaseExcelWriteable;
import com.orion.utils.Valid;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * Excel 行合并器 不支持复杂操作
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/9 1:07
 */
public class ExcelMerge extends BaseExcelWriteable {

    /**
     * source workbook
     */
    private Workbook sourceWorkbook;

    /**
     * source sheet
     */
    private Sheet sourceSheet;

    private int rowIndex;

    /**
     * 跳过合并sheet的行
     */
    private int skipRows;

    /**
     * 行高
     */
    private int height;

    public ExcelMerge() {
        this(new SXSSFWorkbook(), null);
    }

    public ExcelMerge(Workbook sourceWorkbook, Sheet sourceSheet) {
        super(sourceWorkbook);
        Valid.isFalse(Excels.isStreamingSheet(sourceSheet), "origin is not be streaming");
        this.sourceWorkbook = sourceWorkbook;
        if (sourceSheet == null) {
            sourceSheet = sourceWorkbook.createSheet();
        }
        this.sourceSheet = sourceSheet;
        this.rowIndex = sourceSheet.getLastRowNum() + 1;
    }

    /**
     * 跳过行
     *
     * @return this
     */
    public ExcelMerge skip() {
        rowIndex += 1;
        return this;
    }

    /**
     * 跳过多行
     *
     * @param skip skip
     * @return this
     */
    public ExcelMerge skip(int skip) {
        rowIndex += skip;
        return this;
    }

    /**
     * 跳过一行合并sheet
     *
     * @return this
     */
    public ExcelMerge skipRows() {
        skipRows += 1;
        return this;
    }

    /**
     * 跳过多行合并sheet
     *
     * @param skip skip
     * @return this
     */
    public ExcelMerge skipRows(int skip) {
        skipRows += skip;
        return this;
    }

    /**
     * 设置行宽
     *
     * @param column 列索引
     * @param width  行宽
     * @return this
     */
    public ExcelMerge width(int column, int width) {
        sourceSheet.setColumnWidth(column, Excels.getWidth(width));
        return this;
    }

    /**
     * 设置默认行宽
     *
     * @param width 行宽
     * @return this
     */
    public ExcelMerge width(int width) {
        sourceSheet.setDefaultColumnWidth(width);
        return this;
    }

    /**
     * 行高
     *
     * @param height 行高
     * @return this
     */
    public ExcelMerge height(int height) {
        this.height = height;
        sourceSheet.setDefaultRowHeightInPoints(height);
        return this;
    }

    /**
     * 执行合并
     *
     * @param sheet 合并sheet
     * @return this
     */
    public ExcelMerge merge(Sheet sheet) {
        boolean streaming = Excels.isStreamingSheet(sheet);
        int i = 0;
        for (Row sheetRow : sheet) {
            if (i++ < skipRows) {
                continue;
            }
            Row originRow = sourceSheet.createRow(rowIndex++);
            if (!streaming && height != 0) {
                originRow.setHeightInPoints(height);
            }
            int cellIndex = 0;
            for (Cell cell : sheetRow) {
                Cell originCell = originRow.createCell(cellIndex++);
                if (!streaming) {
                    CellStyle originStyle = sourceWorkbook.createCellStyle();
                    originStyle.cloneStyleFrom(cell.getCellStyle());
                    originCell.setCellStyle(originStyle);
                }
                Excels.copyCellValue(cell, originCell);
            }
        }
        skipRows = 0;
        return this;
    }

    /**
     * 保护表格
     *
     * @param password password
     * @return this
     */
    public ExcelMerge protect(String password) {
        sourceSheet.protectSheet(password);
        return this;
    }

    public Workbook getSourceWorkbook() {
        return sourceWorkbook;
    }

    public Sheet getSourceSheet() {
        return sourceSheet;
    }

}
