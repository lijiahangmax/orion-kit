package com.orion.excel.exporting;

import com.orion.able.SafeCloseable;
import com.orion.excel.Excels;
import com.orion.excel.option.FieldOption;
import com.orion.excel.option.SheetOption;
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
 * Excel 导出器 仅支持注解 支持样式
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
    private SheetOption sheetOption = new SheetOption();

    /**
     * 列配置
     */
    private Map<Integer, FieldOption> fieldOptions = new TreeMap<>();

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
        Valid.notNull(targetClass, "TargetClass is null");
        Valid.notNull(workbook, "Workbook is null");
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
        SheetAnalysis sheetAnalysis = new SheetAnalysis(targetClass, sheetOption);
        sheetAnalysis.analysis();
        // 解析列
        ColumnAnalysis columnAnalysis = new ColumnAnalysis(targetClass, sheetOption, fieldOptions);
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
     * @param skipNullRow true跳过
     * @return this
     */
    public ExcelExport<T> skipNullRow(boolean skipNullRow) {
        sheetOption.setSkipNullRow(skipNullRow);
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
     * 合并单元格
     *
     * @param row       合并行
     * @param firstCell 合并开始单元格
     * @param lastCell  合并结束单元格
     * @return this
     */
    public ExcelExport<T> merge(int row, int firstCell, int lastCell) {
        return merge(new CellRangeAddress(row, row, firstCell, lastCell), true);
    }

    /**
     * 合并单元格
     *
     * @param row         合并行
     * @param firstCell   合并开始单元格
     * @param lastCell    合并结束单元格
     * @param mergeBorder 是否合并边框
     * @return this
     */
    public ExcelExport<T> merge(int row, int firstCell, int lastCell, boolean mergeBorder) {
        return merge(new CellRangeAddress(row, row, firstCell, lastCell), mergeBorder);
    }

    /**
     * 合并单元格
     *
     * @param firstRow  合并开始行
     * @param lastRow   合并结束行
     * @param firstCell 合并开始单元格
     * @param lastCell  合并结束单元格
     * @return this
     */
    public ExcelExport<T> merge(int firstRow, int lastRow, int firstCell, int lastCell) {
        return merge(new CellRangeAddress(firstRow, lastRow, firstCell, lastCell), true);
    }

    /**
     * 合并单元格
     *
     * @param firstRow    合并开始行
     * @param lastRow     合并结束行
     * @param firstCell   合并开始单元格
     * @param lastCell    合并结束单元格
     * @param mergeBorder 是否合并边框
     * @return this
     */
    public ExcelExport<T> merge(int firstRow, int lastRow, int firstCell, int lastCell, boolean mergeBorder) {
        return merge(new CellRangeAddress(firstRow, lastRow, firstCell, lastCell), mergeBorder);
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
            if (data == null && sheetOption.isSkipNullRow()) {
                continue;
            }
            // !skipNullRow 为null的row会有样式
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

    public Class<T> getTargetClass() {
        return targetClass;
    }

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

    public Map<Integer, FieldOption> getFieldOptions() {
        return fieldOptions;
    }

    public SheetOption getSheetOption() {
        return sheetOption;
    }

    public int getRowIndex() {
        return processor.rowIndex;
    }

    public int getRows() {
        return rows;
    }

}
