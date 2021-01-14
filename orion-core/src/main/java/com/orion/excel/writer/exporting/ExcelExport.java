package com.orion.excel.writer.exporting;

import com.orion.able.SafeCloseable;
import com.orion.excel.Excels;
import com.orion.excel.option.ExportFieldOption;
import com.orion.excel.option.ExportSheetOption;
import com.orion.utils.Valid;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Excel 导出器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/28 11:15
 */
public class ExcelExport<T> implements SafeCloseable {

    private Workbook workbook;

    private Sheet sheet;

    /**
     * bean class
     */
    private Class<T> targetClass;

    /**
     * sheet配置
     */
    private ExportSheetOption sheetOption = new ExportSheetOption();

    /**
     * 列配置
     */
    private Map<Integer, ExportFieldOption> fieldOptions = new TreeMap<>();

    /**
     * 初始化器
     */
    private ExportProcessor<T> processor;

    /**
     * 写入行数
     */
    private int rows;

    public ExcelExport(Class<T> targetClass) {
        this(targetClass, new SXSSFWorkbook(), null);
    }

    public ExcelExport(Class<T> targetClass, Workbook workbook) {
        this(targetClass, workbook, null);
    }

    public ExcelExport(Class<T> targetClass, Sheet sheet) {
        this(targetClass, sheet.getWorkbook(), sheet);
    }

    public ExcelExport(Class<T> targetClass, Workbook workbook, Sheet sheet) {
        Valid.notNull(targetClass, "target class is null");
        Valid.notNull(workbook, "workbook is null");
        this.targetClass = targetClass;
        this.workbook = workbook;
        this.sheet = sheet;
        this.analysisClass();
        this.processor = new ExportProcessor<>(workbook, sheet, sheetOption, fieldOptions);
    }

    /**
     * 解析 class
     */
    private void analysisClass() {
        // 解析sheet
        ExportSheetAnalysis exportSheetAnalysis = new ExportSheetAnalysis(targetClass, sheetOption);
        exportSheetAnalysis.analysis();
        // 解析列
        ExportColumnAnalysis columnAnalysis = new ExportColumnAnalysis(targetClass, sheetOption, fieldOptions);
        columnAnalysis.analysis();
    }

    /**
     * 初始化
     *
     * @return this
     */
    public ExcelExport<T> init() {
        this.processor.init();
        this.sheet = processor.getSheet();
        return this;
    }

    /**
     * 跳过一多 在表头之前
     *
     * @return this
     */
    public ExcelExport<T> skip() {
        processor.rowIndex += 1;
        return this;
    }

    /**
     * 跳过行多 在表头之前
     *
     * @param i 行
     * @return this
     */
    public ExcelExport<T> skip(int i) {
        processor.rowIndex += i;
        return this;
    }

    /**
     * 跳过空行
     *
     * @param skipNullRows true跳过
     * @return this
     */
    public ExcelExport<T> skipNullRows(boolean skipNullRows) {
        sheetOption.setSkipNullRows(skipNullRows);
        return this;
    }

    /**
     * 跳过title
     *
     * @return this
     */
    public ExcelExport<T> skipTitle() {
        sheetOption.setSkipTitle(true);
        return this;
    }

    /**
     * 设置sheet的名称
     *
     * @param sheetName sheetName
     * @return this
     */
    public ExcelExport<T> sheet(String sheetName) {
        if (sheetName != null) {
            sheetOption.setName(sheetName);
            sheetOption.setNameReset(true);
        }
        return this;
    }

    /**
     * 设置title的名称
     *
     * @param title title
     * @return this
     */
    public ExcelExport<T> title(String title) {
        if (title != null) {
            sheetOption.setTitle(title);
        }
        return this;
    }

    /**
     * 保护表格
     *
     * @param password password
     * @return this
     */
    public ExcelExport<T> protect(String password) {
        sheet.protectSheet(password);
        return this;
    }

    /**
     * 清空列样式 不包括已设置过的
     *
     * @param column 列
     * @return this
     */
    public ExcelExport<T> cleanStyle(int column) {
        processor.columnStyles.remove(column);
        processor.headerStyles.remove(column);
        Integer rowWidth = sheetOption.getColumnWidth();
        if (rowWidth != null) {
            sheet.setColumnWidth(column, (int) ((rowWidth + 0.72) * 256));
        }
        return this;
    }

    /**
     * 清空表头列样式 不包括已设置过的
     *
     * @param column 列
     * @return this
     */
    public ExcelExport<T> cleanHeaderStyle(int column) {
        processor.headerStyles.remove(column);
        return this;
    }

    /**
     * 清空数据列样式 不包括已设置过的
     *
     * @param column 列
     * @return this
     */
    public ExcelExport<T> cleanColumnStyle(int column) {
        processor.columnStyles.remove(column);
        return this;
    }

    /**
     * 表头使用数据列样式 不包括已设置过的
     *
     * @return this
     */
    public ExcelExport<T> headUseColumnStyle() {
        fieldOptions.forEach((k, v) -> {
            processor.headerStyles.put(k, processor.parseStyle(k, false, v));
        });
        return this;
    }

    /**
     * 表头使用数据列样式 不包括已设置过的
     *
     * @param column column
     * @return this
     */
    public ExcelExport<T> headUseColumnStyle(int column) {
        processor.headerStyles.put(column, processor.parseStyle(column, false, fieldOptions.get(column)));
        return this;
    }

    /**
     * 数据使用表头列样式 不包括已设置过的
     *
     * @return this
     */
    public ExcelExport<T> columnUseHeadStyle() {
        fieldOptions.forEach((k, v) -> {
            processor.columnStyles.put(k, processor.parseStyle(k, true, v));
        });
        return this;
    }

    /**
     * 数据使用表头列样式 不包括已设置过的
     *
     * @param column column
     * @return this
     */
    public ExcelExport<T> columnUseHeadStyle(int column) {
        processor.columnStyles.put(column, processor.parseStyle(column, true, fieldOptions.get(column)));
        return this;
    }

    /**
     * 设置列样式 不包括已设置过的
     *
     * @param column 列
     * @param style  样式
     * @return this
     */
    public ExcelExport<T> setStyle(int column, CellStyle style) {
        processor.headerStyles.put(column, style);
        processor.columnStyles.put(column, style);
        return this;
    }

    /**
     * 设置表头样式 不包括已设置过的
     *
     * @param column 列
     * @param style  样式
     * @return this
     */
    public ExcelExport<T> setHeaderStyle(int column, CellStyle style) {
        processor.headerStyles.put(column, style);
        return this;
    }

    /**
     * 设置数据样式 不包括已设置过的
     *
     * @param column 列
     * @param style  样式
     * @return this
     */
    public ExcelExport<T> setColumnStyle(int column, CellStyle style) {
        processor.columnStyles.put(column, style);
        return this;
    }

    /**
     * 设置列样式 不包括已设置过的
     *
     * @param column 列
     * @param option 样式
     * @return this
     */
    public ExcelExport<T> setStyle(int column, ExportFieldOption option) {
        if (option == null) {
            return this;
        }
        processor.headerStyles.put(column, processor.parseStyle(column, false, option));
        processor.columnStyles.put(column, processor.parseStyle(column, true, option));
        return this;
    }

    /**
     * 设置表头样式 不包括已设置过的
     *
     * @param column 列
     * @param option 样式
     * @return this
     */
    public ExcelExport<T> setHeaderStyle(int column, ExportFieldOption option) {
        if (option == null) {
            return this;
        }
        processor.headerStyles.put(column, processor.parseStyle(column, false, option));
        return this;
    }

    /**
     * 设置数据样式 不包括已设置过的
     *
     * @param column 列
     * @param option 样式
     * @return this
     */
    public ExcelExport<T> setColumnStyle(int column, ExportFieldOption option) {
        if (option == null) {
            return this;
        }
        processor.columnStyles.put(column, processor.parseStyle(column, true, option));
        return this;
    }

    /**
     * 合并单元格
     *
     * @param row      合并行索引
     * @param firstCol 合并开始列索引
     * @param lastCol  合并结束列索引
     * @return this
     */
    public ExcelExport<T> merge(int row, int firstCol, int lastCol) {
        return merge(new CellRangeAddress(row, row, firstCol, lastCol), true);
    }

    /**
     * 合并单元格
     *
     * @param row         合并行索引
     * @param firstCol    合并开始列索引
     * @param lastCol     合并结束列索引
     * @param mergeBorder 是否合并边框
     * @return this
     */
    public ExcelExport<T> merge(int row, int firstCol, int lastCol, boolean mergeBorder) {
        return merge(new CellRangeAddress(row, row, firstCol, lastCol), mergeBorder);
    }

    /**
     * 合并单元格
     *
     * @param firstRow 合并开始行索引
     * @param lastRow  合并结束行索引
     * @param firstCol 合并开始列索引
     * @param lastCol  合并结束列索引
     * @return this
     */
    public ExcelExport<T> merge(int firstRow, int lastRow, int firstCol, int lastCol) {
        return merge(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol), true);
    }

    /**
     * 合并单元格
     *
     * @param firstRow    合并开始行索引
     * @param lastRow     合并结束行索引
     * @param firstCol    合并开始列索引
     * @param lastCol     合并结束列索引
     * @param mergeBorder 是否合并边框
     * @return this
     */
    public ExcelExport<T> merge(int firstRow, int lastRow, int firstCol, int lastCol, boolean mergeBorder) {
        return merge(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol), mergeBorder);
    }

    /**
     * 合并单元格
     *
     * @param region      region
     * @param mergeBorder 是否合并边框
     * @return this
     */
    public ExcelExport<T> merge(CellRangeAddress region, boolean mergeBorder) {
        Excels.mergeCell(sheet, region);
        if (mergeBorder) {
            Optional.ofNullable(Excels.getCell(sheet, region.getFirstRow(), 0))
                    .map(Cell::getCellStyle)
                    .ifPresent(s -> {
                        Excels.mergeCellBorder(sheet, s.getBorderTop().getCode(), s.getTopBorderColor(), region);
                    });
        }
        return this;
    }

    /**
     * 设置表头
     *
     * @param headers 头
     * @return this
     */
    public ExcelExport<T> headers(String... headers) {
        processor.headers(false, headers);
        return this;
    }

    /**
     * 添加数据
     *
     * @param row row
     * @return this
     */
    public ExcelExport<T> addRow(T row) {
        return addRows(Lists.singleton(row));
    }

    /**
     * 添加数据
     *
     * @param rows rows
     * @return this
     */
    public ExcelExport<T> addRows(List<T> rows) {
        Integer rowHeight = sheetOption.getRowHeight();
        for (T data : rows) {
            this.rows++;
            if (data == null && sheetOption.isSkipNullRows()) {
                continue;
            }
            // !skipNullRows 为null的row会有样式
            Row dataRow = sheet.createRow(processor.rowIndex);
            // 行高
            if (rowHeight != null) {
                dataRow.setHeightInPoints(rowHeight);
            }
            fieldOptions.forEach((k, v) -> {
                processor.setCellValue(dataRow.createCell(k), processor.rowIndex, k, data, v);
            });
            processor.rowIndex++;
        }
        return this;
    }

    /**
     * 写出到文件
     *
     * @param file 文件
     * @return this
     */
    public ExcelExport<T> write(String file) {
        Valid.notNull(file, "file is null");
        Excels.write(workbook, file);
        return this;
    }

    /**
     * 写出到文件
     *
     * @param file 文件
     * @return this
     */
    public ExcelExport<T> write(File file) {
        Valid.notNull(file, "file is null");
        Excels.write(workbook, file);
        return this;
    }

    /**
     * 写出到流
     *
     * @param out 流
     * @return this
     */
    public ExcelExport<T> write(OutputStream out) {
        Valid.notNull(out, "file is null");
        Excels.write(workbook, out);
        return this;
    }

    /**
     * 写出到文件
     *
     * @param file     文件
     * @param password 密码
     * @return this
     */
    public ExcelExport<T> write(String file, String password) {
        Valid.notNull(file, "file is null");
        Excels.write(workbook, file, password);
        return this;
    }

    /**
     * 写出到文件
     *
     * @param file     文件
     * @param password 密码
     * @return this
     */
    public ExcelExport<T> write(File file, String password) {
        Valid.notNull(file, "file is null");
        Excels.write(workbook, file, password);
        return this;
    }

    /**
     * 写出到流
     *
     * @param out      流
     * @param password 密码
     * @return this
     */
    public ExcelExport<T> write(OutputStream out, String password) {
        Valid.notNull(out, "file is null");
        Excels.write(workbook, out, password);
        return this;
    }

    /**
     * 关闭
     */
    @Override
    public void close() {
        Streams.close(workbook);
    }

    /**
     * 获取总行数
     *
     * @return 总行数
     */
    public int getLines() {
        return sheet.getLastRowNum() + 1;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    /**
     * 获取最大列索引
     *
     * @return 最大列索引
     */
    public int getColumnMaxIndex() {
        return sheetOption.getColumnMaxIndex();
    }

    /**
     * 获取列数
     *
     * @return 列数
     */
    public int getColumnSize() {
        return sheetOption.getColumnSize();
    }

    /**
     * 获取一个单元格样式 用于样式修改
     *
     * @return 单元格样式
     */
    public CellStyle getCellStyle() {
        return workbook.createCellStyle();
    }

    /**
     * 获取一个字体 用于样式修改
     *
     * @return 字体
     */
    public Font getFont() {
        return workbook.createFont();
    }

    /**
     * 获取一个格式 用于样式修改
     *
     * @return 格式
     */
    public DataFormat getFormat() {
        return workbook.createDataFormat();
    }

    public Map<Integer, ExportFieldOption> getFieldOptions() {
        return fieldOptions;
    }

    public ExportSheetOption getSheetOption() {
        return sheetOption;
    }

    /**
     * @return 当前行数
     */
    public int getRowIndex() {
        return processor.rowIndex;
    }

    /**
     * @return 写入的数据行数
     */
    public int getRows() {
        return rows;
    }

}
