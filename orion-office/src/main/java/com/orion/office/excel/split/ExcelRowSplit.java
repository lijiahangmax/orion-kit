package com.orion.office.excel.split;

import com.orion.constant.Const;
import com.orion.office.excel.Excels;
import com.orion.office.support.DestinationGenerator;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.Iterator;

/**
 * excel 行拆分器 能拆分多个文件一个sheet 不支持复杂类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/8/21 16:01
 */
public class ExcelRowSplit extends DestinationGenerator {

    /**
     * sheet
     */
    public Sheet sheet;

    /**
     * 拆分文件最大行数
     */
    private int limit;

    /**
     * 头部跳过行数
     */
    private int skip;

    /**
     * 是否是流式读取 (样式)
     */
    private boolean streaming;

    /**
     * 表头
     */
    private String[] header;

    /**
     * 列
     */
    private int[] columns;

    /**
     * 列数
     */
    private int columnSize;

    private String password;

    private Workbook currentWorkbook;

    private Sheet currentSheet;

    private int currentIndex;

    private boolean end;

    public ExcelRowSplit(Sheet sheet, int limit) {
        this.sheet = Valid.notNull(sheet, "split sheet is null");
        Valid.lte(0, limit, "limit not be lte 0");
        this.columnSize = 32;
        this.limit = limit;
        this.streaming = Excels.isStreamingSheet(sheet);
        super.suffix = Const.SUFFIX_XLSX;
    }

    /**
     * 保护表格
     *
     * @param password password
     * @return this
     */
    public ExcelRowSplit protect(String password) {
        this.password = password;
        return this;
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public ExcelRowSplit skip() {
        this.skip += 1;
        return this;
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public ExcelRowSplit skip(int skip) {
        this.skip += skip;
        return this;
    }

    /**
     * 设置读取的列
     *
     * @param columns 列
     * @return this
     */
    public ExcelRowSplit columns(int... columns) {
        if (!Arrays1.isEmpty(columns)) {
            this.columns = columns;
            this.columnSize = Arrays1.max(columns) + 1;
        }
        return this;
    }

    /**
     * 设置列数 用于设置列宽
     *
     * @param columnSize 列数
     * @return this
     */
    public ExcelRowSplit columnSize(int columnSize) {
        if (Arrays1.isEmpty(columns)) {
            this.columnSize = columnSize;
        }
        return this;
    }

    /**
     * 设置表头
     *
     * @param header 表头
     * @return ignore
     */
    public ExcelRowSplit header(String... header) {
        this.header = header;
        return this;
    }

    /**
     * 执行拆分
     *
     * @return this
     */
    public ExcelRowSplit split() {
        Iterator<Row> iterator = sheet.iterator();
        for (int j = 0; j < skip; j++) {
            if (iterator.hasNext()) {
                // skip
                iterator.next();
            } else {
                this.end = true;
                return this;
            }
        }
        do {
            if (!super.hasNext()) {
                this.end = true;
                return this;
            }
            // 创建下一个工作簿
            this.nextWorkbook();
            int border = limit;
            if (!Arrays1.isEmpty(header)) {
                border = limit + 1;
            }
            // 读取
            while (iterator.hasNext()) {
                this.addRow(currentIndex++, iterator.next());
                if (currentIndex == border) {
                    break;
                }
            }
            // 未读取到则结束
            if (currentIndex == 0) {
                this.end = true;
                break;
            }
            super.next();
            // 如果小于边界 则证明无数据
            if (currentIndex < border) {
                this.end = true;
            }
            this.write();
        } while (!end);
        return this;
    }

    /**
     * 设置列
     *
     * @param rowIndex index
     * @param row      row
     */
    private void addRow(int rowIndex, Row row) {
        Row target = currentSheet.createRow(rowIndex);
        // 设置行高
        if (!streaming) {
            target.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
        }
        int i = 0;
        if (Arrays1.isEmpty(columns)) {
            // 设置所有列
            for (Cell cell : row) {
                this.setCellValue(cell, target.createCell(i++));
            }
        } else {
            // 设置部分列
            for (int col : columns) {
                this.setCellValue(row.getCell(col), target.createCell(i++));
            }
        }
    }

    /**
     * 设置单元格的值
     *
     * @param sourceCell source
     * @param targetCell target
     */
    private void setCellValue(Cell sourceCell, Cell targetCell) {
        // 设置样式
        if (!streaming) {
            CellStyle targetStyle = currentWorkbook.createCellStyle();
            targetStyle.cloneStyleFrom(sourceCell.getCellStyle());
            targetCell.setCellStyle(targetStyle);
        }
        // 设置值
        Excels.copyCellValue(sourceCell, targetCell);
    }

    /**
     * 设置下一个workbook
     */
    private void nextWorkbook() {
        this.currentIndex = 0;
        this.currentWorkbook = new SXSSFWorkbook();
        this.currentSheet = currentWorkbook.createSheet(sheet.getSheetName());
        // 非流式设置样式
        if (!streaming) {
            for (int i = 0; i < columnSize; i++) {
                currentSheet.setColumnWidth(i, sheet.getColumnWidth(i));
                currentSheet.setDefaultColumnStyle(i, sheet.getColumnStyle(i));
            }
            currentSheet.setDefaultColumnWidth(sheet.getDefaultColumnWidth());
            currentSheet.setDefaultRowHeightInPoints(sheet.getDefaultRowHeightInPoints());
        }
        // 设置表头
        if (!Arrays1.isEmpty(header)) {
            Row headerRow = currentSheet.createRow(0);
            for (int headerIndex = 0; headerIndex < header.length; headerIndex++) {
                Cell headerRowCell = headerRow.createCell(headerIndex);
                headerRowCell.setCellValue(header[headerIndex]);
            }
            this.currentIndex = 1;
        }
    }

    private void write() {
        try {
            if (password != null && !streaming) {
                currentSheet.protectSheet(password);
            }
            currentWorkbook.write(currentOutputStream);
            Streams.close(currentWorkbook);
            if (super.autoClose) {
                Streams.close(currentOutputStream);
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public Sheet getSheet() {
        return sheet;
    }

    public int getLimit() {
        return limit;
    }

}
