package com.orion.excel.builder;

import com.orion.excel.Excels;
import com.orion.utils.collect.Lists;
import com.orion.utils.reflect.Methods;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Excel sheet封装
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/6 23:33
 */
@SuppressWarnings("ALL")
public class ExcelSheet<T> {

    private Workbook workbook;

    private Sheet sheet;

    /**
     * 当前行索引
     */
    private int index;

    /**
     * 表头默认行高
     */
    private short headerDefaultHeight;

    /**
     * 是否跳过null
     */
    private boolean skipNullRow = true;

    /**
     * 选项
     */
    private List<ExcelFieldOption> options;

    /**
     * 列class
     *
     * @see #addRow
     * @see #addRows
     */
    private Class<? extends T> elementClass;

    public ExcelSheet(Workbook workbook, Sheet sheet) {
        this(workbook, sheet, null);
    }

    public ExcelSheet(Workbook workbook, Sheet sheet, Class<? extends T> elementClass) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.elementClass = elementClass;
    }

    /**
     * 添加表头
     *
     * @param header 表头
     * @return this
     */
    public ExcelSheet<T> addHeaders(String... header) {
        Row row = sheet.createRow(index++);
        if (headerDefaultHeight != 0) {
            row.setHeight((short) (headerDefaultHeight * 20));
        }
        for (int i = 0; i < header.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(header[i]);
        }
        return this;
    }

    /**
     * 添加表头
     *
     * @param height 行高
     * @param header 表头
     * @return this
     */
    public ExcelSheet<T> addHeaders(int height, String... header) {
        Row row = sheet.createRow(index++);
        row.setHeight((short) (height * 20));
        for (int i = 0; i < header.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(header[i]);
        }
        return this;
    }

    /**
     * 添加表头
     *
     * @param headerMap 表头信息
     * @param height    行高
     * @return this
     */
    public ExcelSheet<T> addHeaders(int height, List<ExcelHeaderOption> headerMap) {
        Row row = sheet.createRow(index++);
        if (height != 0) {
            row.setHeight((short) (height * 20));
        }
        for (int i = 0; i < headerMap.size(); i++) {
            ExcelHeaderOption map = headerMap.get(i);
            Cell cell = row.createCell(i);
            cell.setCellValue(map.getValue());
            CellStyle cellStyle = map.getCellStyle();
            if (cellStyle != null) {
                cell.setCellStyle(cellStyle);
            }
            Font font = map.getFont();
            if (font != null) {
                if (cellStyle == null) {
                    cellStyle = workbook.createCellStyle();
                    cell.setCellStyle(cellStyle);
                }
                cellStyle.setFont(font);
            }
        }
        return this;
    }

    /**
     * 设置属性
     *
     * @param options options
     * @return this
     */
    public ExcelSheet<T> options(List<ExcelFieldOption> options) {
        this.options = options;
        return this;
    }

    /**
     * 跳过一行
     *
     * @return this
     */
    public ExcelSheet<T> skip() {
        index++;
        return this;
    }

    /**
     * 跳过多行
     *
     * @param i 行
     * @return this
     */
    public ExcelSheet<T> skip(int i) {
        index += i;
        return this;
    }

    /**
     * 跳过空行
     *
     * @param skip 跳过空行
     * @return this
     */
    public ExcelSheet<T> skipNullRow(boolean skip) {
        this.skipNullRow = skip;
        return this;
    }

    /**
     * 设置行宽
     *
     * @param column 列索引
     * @param width  行宽
     * @return this
     */
    public ExcelSheet<T> width(int column, int width) {
        sheet.setColumnWidth(column, (int) ((width + 0.72) * 256));
        return this;
    }

    /**
     * 设置默认行宽
     *
     * @param width 行宽
     * @return this
     */
    public ExcelSheet<T> width(int width) {
        sheet.setDefaultColumnWidth((int) ((width + 0.72) * 256));
        return this;
    }

    /**
     * 设置默认行高
     *
     * @param height 行高
     * @return this
     */
    public ExcelSheet<T> height(int height) {
        sheet.setDefaultRowHeight((short) (height * 20));
        return this;
    }

    /**
     * 设置表头默认行高
     *
     * @param height 行高
     * @return this
     */
    public ExcelSheet<T> headerHeight(int height) {
        headerDefaultHeight = (short) height;
        return this;
    }

    /**
     * 合并单元格
     *
     * @param firstRow  合并开始行
     * @param lastRow   合并结束行
     * @param firstCell 合并开始单元格
     * @param lastCell  合并结束单元格
     */
    public ExcelSheet<T> mergeCell(int firstRow, int lastRow, int firstCell, int lastCell) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCell, lastCell));
        return this;
    }

    /**
     * 合并单元格
     *
     * @param row       合并行
     * @param firstCell 合并开始单元格
     * @param lastCell  合并结束单元格
     */
    public ExcelSheet<T> mergeCell(int row, int firstCell, int lastCell) {
        sheet.addMergedRegion(new CellRangeAddress(row, row, firstCell, lastCell));
        return this;
    }

    /**
     * 添加列 根据bean的getter方法 空行有样式
     *
     * @param row 列
     * @return this
     */
    public ExcelSheet<T> addRow(T row) {
        return this.addRows(Lists.singleton(row));
    }

    /**
     * 添加列 根据bean的getter方法 空行有样式
     *
     * @param list 列
     * @return this
     */
    public ExcelSheet<T> addRows(List<T> list) {
        if (Lists.isEmpty(list)) {
            return this;
        }
        if (Lists.isEmpty(options)) {
            return this;
        }

        for (ExcelFieldOption option : options) {
            String fieldName = option.getFieldName();
            if (option.getIndex() == null || fieldName == null) {
                continue;
            }
            option.setGetterMethod(Methods.getGetterMethodByCache(elementClass.getClass(), fieldName));
        }

        for (T t : list) {
            if (t == null && skipNullRow) {
                continue;
            }
            Row row = sheet.createRow(index++);
            for (ExcelFieldOption option : options) {
                Integer index = option.getIndex();
                Method method = option.getGetterMethod();
                if (index == null || method == null) {
                    continue;
                }
                Cell cell = row.createCell(index);
                CellStyle style = option.getStyle();
                if (style != null) {
                    cell.setCellStyle(style);
                }
                Font font = option.getFont();
                if (font != null) {
                    if (style == null) {
                        style = workbook.createCellStyle();
                    }
                    style.setFont(font);
                    cell.setCellStyle(style);
                }
                if (t == null) {
                    // !skipNullRow 为null的row会有样式
                    continue;
                }
                Object v = Methods.invokeMethod(t, method);
                Excels.setCellValue(cell, v, option.getType());
            }
        }
        return this;
    }

    /**
     * 添加列 空行无样式
     *
     * @param r 列
     * @return this
     */
    public ExcelSheet<T> addMap(Map<Integer, ?> r) {
        if (r == null) {
            return this.addArray(null);
        }
        int max = 0;
        for (Map.Entry<Integer, ?> e : r.entrySet()) {
            Integer index = e.getKey();
            if (index != null) {
                if (index.compareTo(max) > 0) {
                    max = index;
                }
            }
        }
        Object[] arr = new Object[++max];
        for (int i = 0; i < max; i++) {
            arr[i] = r.get(i);
        }
        return this.addArray(arr);
    }

    /**
     * 添加列 空行无样式
     *
     * @param list 列
     * @return this
     */
    public ExcelSheet<T> addMaps(List<Map<Integer, ?>> list) {
        for (Map<Integer, ?> t : list) {
            addMap(t);
        }
        return this;
    }

    /**
     * 添加列 空行无样式
     *
     * @param r r
     * @return this
     */
    public ExcelSheet<T> addArray(Object[] r) {
        if (r == null) {
            if (!skipNullRow) {
                index++;
            }
            return this;
        }
        Row row = sheet.createRow(index++);
        for (int i = 0; i < r.length; i++) {
            Object value = r[i];
            if (value == null) {
                continue;
            }
            Cell cell = row.createCell(i);
            boolean set = false;
            for (ExcelFieldOption option : options) {
                if (!option.getIndex().equals(i)) {
                    continue;
                }
                CellStyle style = option.getStyle();
                if (style != null) {
                    cell.setCellStyle(style);
                }
                Font font = option.getFont();
                if (font != null) {
                    if (style == null) {
                        style = workbook.createCellStyle();
                    }
                    style.setFont(font);
                    cell.setCellStyle(style);
                }
                Excels.setCellValue(cell, value, option.getType());
                set = true;
                break;
            }
            // 未配置
            if (!set) {
                Excels.setCellValue(cell, value);
            }
        }
        return this;
    }

    /**
     * 添加列 空行无样式
     *
     * @param list 列
     * @return this
     */
    public ExcelSheet<T> addArrays(List<Object[]> list) {
        for (Object[] t : list) {
            addArray(t);
        }
        return this;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public int getIndex() {
        return index;
    }

}
