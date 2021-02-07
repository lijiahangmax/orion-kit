package com.orion.excel.writer;

import com.orion.excel.Excels;
import com.orion.excel.option.FooterOption;
import com.orion.excel.option.HeaderOption;
import com.orion.excel.option.PrintOption;
import com.orion.excel.option.WriteFieldOption;
import com.orion.excel.type.ExcelFieldType;
import com.orion.utils.Arrays1;
import com.orion.utils.Objects1;
import com.orion.utils.Valid;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

/**
 * Excel sheet写入器 基类
 * <p>
 * 不支持复杂类型 (图片 超链接 注释)
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/6 23:33
 */
public abstract class BaseExcelSheetWriter<K, V> {

    protected Workbook workbook;

    protected Sheet sheet;

    /**
     * 当前行索引
     */
    protected int cellIndex;

    /**
     * 最大列索引
     */
    protected int columnMaxIndex;

    /**
     * 是否跳过null
     */
    protected boolean skipNullRows = true;

    /**
     * 是否清除空格
     *
     * @see String
     */
    protected boolean trim;

    /**
     * 是否使用头部样式
     */
    protected boolean headerUseRowStyle;

    /**
     * 标题行高
     */
    protected int titleHeight;

    /**
     * 表头行高
     */
    protected int headerHeight;

    /**
     * 行高
     */
    protected int rowHeight;

    /**
     * 选项
     */
    protected Map<K, WriteFieldOption> options = new LinkedHashMap<>();

    /**
     * 标题样式
     */
    protected CellStyle titleStyle;

    /**
     * 表头样式
     */
    protected Map<Integer, CellStyle> headerStyles = new TreeMap<>();

    /**
     * 数据样式
     */
    protected Map<Integer, CellStyle> columnStyles = new TreeMap<>();

    /**
     * 默认值
     */
    private Map<K, Object> defaultValue = new HashMap<>();

    public BaseExcelSheetWriter(Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
    }

    /**
     * 添加标题
     *
     * @param title title
     * @return this
     */
    public BaseExcelSheetWriter<K, V> title(String title) {
        return title(title, 1, columnMaxIndex);
    }

    /**
     * 添加标题
     *
     * @param title title
     * @param row   使用行数
     * @return this
     */
    public BaseExcelSheetWriter<K, V> title(String title, int row) {
        return title(title, row, columnMaxIndex);
    }

    /**
     * 添加标题
     *
     * @param title           title
     * @param row             使用行数
     * @param lastColumnIndex 列数索引
     * @return this
     */
    public BaseExcelSheetWriter<K, V> title(String title, int row, int lastColumnIndex) {
        Valid.gt(row, 0, "title use row must > 0");
        Valid.gte(lastColumnIndex, 0, "title last column index row must >= 0");
        Row titleRow = sheet.createRow(cellIndex++);
        if (titleHeight != 0) {
            titleRow.setHeightInPoints(titleHeight);
        }
        for (int i = 0; i < row - 1; i++) {
            Row ignoreRow = sheet.createRow(cellIndex++);
            if (titleHeight != 0) {
                ignoreRow.setHeightInPoints(titleHeight);
            }
        }
        Cell cell = titleRow.createCell(0);
        if (titleStyle == null) {
            titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }
        cell.setCellStyle(titleStyle);
        if (trim) {
            title = title.trim();
        }
        cell.setCellValue(title);
        this.merge(cellIndex - row, cellIndex - 1, 0, lastColumnIndex, true);
        return this;
    }

    /**
     * 添加表头
     *
     * @param headers 表头
     * @return this
     */
    public BaseExcelSheetWriter<K, V> headers(String... headers) {
        if (Arrays1.isEmpty(headers)) {
            return this;
        }
        Row row = sheet.createRow(cellIndex++);
        if (headerHeight != 0) {
            row.setHeightInPoints(headerHeight);
        }
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            if (headerUseRowStyle) {
                Optional.ofNullable(headerStyles.get(i)).ifPresent(cell::setCellStyle);
            }
            if (trim) {
                cell.setCellValue(headers[i].trim());
            } else {
                cell.setCellValue(headers[i]);
            }
        }
        return this;
    }

    /**
     * 跳过一行
     *
     * @return this
     */
    public BaseExcelSheetWriter<K, V> skip() {
        cellIndex++;
        return this;
    }

    /**
     * 跳过多行
     *
     * @param i 行
     * @return this
     */
    public BaseExcelSheetWriter<K, V> skip(int i) {
        cellIndex += i;
        return this;
    }

    /**
     * 跳过空行
     *
     * @param skip 跳过空行
     * @return this
     */
    public BaseExcelSheetWriter<K, V> skipNullRows(boolean skip) {
        this.skipNullRows = skip;
        return this;
    }

    /**
     * 跳过首尾空格
     *
     * @return this
     */
    public BaseExcelSheetWriter<K, V> trim() {
        this.trim = true;
        return this;
    }

    /**
     * 设置行宽
     *
     * @param column 列索引
     * @param width  行宽
     * @return this
     */
    public BaseExcelSheetWriter<K, V> width(int column, int width) {
        sheet.setColumnWidth(column, (int) ((width + 0.72) * 256));
        return this;
    }

    /**
     * 设置默认行宽
     *
     * @param width 行宽
     * @return this
     */
    public BaseExcelSheetWriter<K, V> width(int width) {
        sheet.setDefaultColumnWidth(width);
        return this;
    }

    /**
     * 设置默认行高
     *
     * @param height 行高
     * @return this
     */
    public BaseExcelSheetWriter<K, V> height(int height) {
        sheet.setDefaultRowHeightInPoints(this.rowHeight = height);
        this.titleHeight = height;
        this.headerHeight = (short) height;
        return this;
    }

    /**
     * 设数据行高
     *
     * @param height 行高
     * @return this
     */
    public BaseExcelSheetWriter<K, V> rowHeight(int height) {
        sheet.setDefaultRowHeightInPoints(this.rowHeight = height);
        return this;
    }

    /**
     * 设置标题行高
     *
     * @param height 行高
     * @return this
     */
    public BaseExcelSheetWriter<K, V> titleHeight(int height) {
        this.titleHeight = height;
        return this;
    }

    /**
     * 设置表头默认行高
     *
     * @param height 行高
     * @return this
     */
    public BaseExcelSheetWriter<K, V> headerHeight(int height) {
        this.headerHeight = (short) height;
        return this;
    }

    /**
     * 表头是否使用行样式
     *
     * @return this
     */
    public BaseExcelSheetWriter<K, V> headerUseRowStyle() {
        this.headerUseRowStyle = true;
        return this;
    }

    /**
     * 设置列 style
     *
     * @param column 列
     * @param style  style
     * @return this
     */
    public BaseExcelSheetWriter<K, V> style(int column, CellStyle style) {
        if (headerUseRowStyle && !headerStyles.containsKey(column)) {
            headerStyles.put(column, style);
        }
        columnStyles.put(column, style);
        return this;
    }

    /**
     * 设置头 style
     *
     * @param column 列
     * @param style  style
     * @return this
     */
    public BaseExcelSheetWriter<K, V> headerStyle(int column, CellStyle style) {
        headerStyles.put(column, style);
        return this;
    }

    /**
     * 设置标题 style
     *
     * @param style style
     * @return this
     */
    public BaseExcelSheetWriter<K, V> titleStyle(CellStyle style) {
        this.titleStyle = style;
        return this;
    }

    /**
     * 设置选中
     *
     * @return this
     */
    public BaseExcelSheetWriter<K, V> selected() {
        workbook.setActiveSheet(workbook.getSheetIndex(sheet));
        return this;
    }

    /**
     * 设置隐藏表格
     *
     * @return this
     */
    public BaseExcelSheetWriter<K, V> hidden() {
        workbook.setSheetHidden(workbook.getSheetIndex(sheet), true);
        return this;
    }

    /**
     * 设置隐藏列
     *
     * @param column 列
     * @return this
     */
    public BaseExcelSheetWriter<K, V> hidden(int column) {
        sheet.setColumnHidden(column, true);
        return this;
    }

    /**
     * 筛选列
     *
     * @return this
     */
    public BaseExcelSheetWriter<K, V> filter() {
        return filter(0, columnMaxIndex);
    }

    /**
     * 筛选列
     *
     * @param rowIndex rowIndex
     * @return this
     */
    public BaseExcelSheetWriter<K, V> filter(int rowIndex) {
        return filter(rowIndex, columnMaxIndex);
    }

    /**
     * 筛选列
     *
     * @param rowIndex   rowIndex
     * @param lastColumn lastColumn
     * @return this
     */
    public BaseExcelSheetWriter<K, V> filter(int rowIndex, int lastColumn) {
        Excels.filterRow(sheet, rowIndex, lastColumn);
        return this;
    }

    /**
     * 固定行
     *
     * @return this
     */
    public BaseExcelSheetWriter<K, V> freeze() {
        return this.freeze(0);
    }

    /**
     * 固定行
     *
     * @param row row
     * @return this
     */
    public BaseExcelSheetWriter<K, V> freeze(int row) {
        Excels.freezeRow(sheet, row);
        return this;
    }

    /**
     * 添加列配置
     *
     * @param k      k
     * @param column column
     * @return this
     */
    public BaseExcelSheetWriter<K, V> option(K k, int column) {
        this.addOption(k, new WriteFieldOption(column));
        return this;
    }

    /**
     * 添加列配置
     *
     * @param k      k
     * @param column column
     * @param type   type
     * @return this
     */
    public BaseExcelSheetWriter<K, V> option(K k, int column, ExcelFieldType type) {
        this.addOption(k, new WriteFieldOption(column, type));
        return this;
    }

    /**
     * 添加列配置
     *
     * @param k      k
     * @param column column
     * @param type   type
     * @param format format
     * @return this
     */
    public BaseExcelSheetWriter<K, V> option(K k, int column, ExcelFieldType type, String format) {
        this.addOption(k, new WriteFieldOption(column, type, format));
        return this;
    }

    /**
     * 添加列配置
     *
     * @param k      k
     * @param option option
     * @return this
     */
    public BaseExcelSheetWriter<K, V> option(K k, WriteFieldOption option) {
        this.addOption(k, option);
        return this;
    }

    /**
     * 设置默认值
     *
     * @param k     K
     * @param value 默认值
     * @return this
     */
    public BaseExcelSheetWriter<K, V> defaultValue(K k, Object value) {
        defaultValue.put(k, value);
        return this;
    }

    /**
     * 添加列配置
     *
     * @param k      k
     * @param option option
     */
    protected void addOption(K k, WriteFieldOption option) {
        Valid.gte(option.getIndex(), 0, "title use row must >= 0");
        this.options.put(k, option);
        this.columnMaxIndex = Math.max(columnMaxIndex, option.getIndex());
    }

    /**
     * 合并单元格
     *
     * @param row      合并行索引
     * @param firstCol 合并开始列索引
     * @param lastCol  合并结束列索引
     * @return this
     */
    public BaseExcelSheetWriter<K, V> merge(int row, int firstCol, int lastCol) {
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
    public BaseExcelSheetWriter<K, V> merge(int row, int firstCol, int lastCol, boolean mergeBorder) {
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
    public BaseExcelSheetWriter<K, V> merge(int firstRow, int lastRow, int firstCol, int lastCol) {
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
    public BaseExcelSheetWriter<K, V> merge(int firstRow, int lastRow, int firstCol, int lastCol, boolean mergeBorder) {
        return merge(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol), mergeBorder);
    }

    /**
     * 合并单元格
     *
     * @param region      region
     * @param mergeBorder 是否合并边框
     * @return this
     */
    public BaseExcelSheetWriter<K, V> merge(CellRangeAddress region, boolean mergeBorder) {
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
     * 添加行
     *
     * @param list 行
     * @return this
     */
    public BaseExcelSheetWriter<K, V> addRows(List<V> list) {
        list.forEach(this::addRow);
        return this;
    }

    /**
     * 添加行
     *
     * @param row 行
     * @return this
     */
    public BaseExcelSheetWriter<K, V> addRow(V row) {
        if (row == null && skipNullRows) {
            return this;
        }
        Row r = sheet.createRow(cellIndex++);
        if (rowHeight != 0) {
            r.setHeightInPoints(rowHeight);
        }
        if (row == null) {
            return this;
        }
        options.forEach((k, v) -> {
            if (v == null) {
                return;
            }
            int columnIndex = v.getIndex();
            Cell cell = r.createCell(columnIndex);
            CellStyle style = columnStyles.get(columnIndex);
            if (style != null) {
                cell.setCellStyle(style);
            }
            Object value = Objects1.def(this.getValue(row, k), defaultValue.get(k));
            if (trim && value instanceof String) {
                value = ((String) value).trim();
            }
            Excels.setCellValue(cell, value, v.getType(), v.getCellOption());
        });
        return this;
    }

    /**
     * 设置页眉
     *
     * @param option option
     * @return this
     */
    public BaseExcelSheetWriter<K, V> header(HeaderOption option) {
        Excels.setHeader(sheet, option);
        return this;
    }

    /**
     * 设置页脚
     *
     * @param option option
     * @return this
     */
    public BaseExcelSheetWriter<K, V> footer(FooterOption option) {
        Excels.setFooter(sheet, option);
        return this;
    }

    /**
     * 设置打印属性
     *
     * @param option option
     * @return this
     */
    public BaseExcelSheetWriter<K, V> print(PrintOption option) {
        Excels.parsePrint(sheet, option);
        return this;
    }

    /**
     * 保护表格
     *
     * @param password password
     * @return this
     */
    public BaseExcelSheetWriter<K, V> protect(String password) {
        sheet.protectSheet(password);
        return this;
    }

    /**
     * 不显示网格线
     *
     * @return this
     */
    public BaseExcelSheetWriter<K, V> displayGridLines() {
        sheet.setDisplayGridlines(false);
        return this;
    }

    /**
     * 不显示行数和列数
     *
     * @return this
     */
    public BaseExcelSheetWriter<K, V> displayRowColHeadings() {
        sheet.setDisplayRowColHeadings(false);
        return this;
    }

    /**
     * 不执行公式 会修改列宽单位
     *
     * @return this
     */
    public BaseExcelSheetWriter<K, V> displayFormulas() {
        sheet.setDisplayFormulas(true);
        return this;
    }

    /**
     * 获取值
     *
     * @param row row
     * @param key keyIndex
     * @return value
     */
    protected abstract Object getValue(V row, K key);

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public int getColumnMaxIndex() {
        return columnMaxIndex;
    }

    public int getColumnSize() {
        return columnMaxIndex + 1;
    }

    public CellStyle createStyle() {
        return workbook.createCellStyle();
    }

    public Font createFont() {
        return workbook.createFont();
    }

    public DataFormat createFormat() {
        return workbook.createDataFormat();
    }

}
