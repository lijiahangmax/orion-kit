package com.orion.excel.builder;

import com.orion.utils.Converts;
import com.orion.utils.Objects1;
import com.orion.utils.Strings;
import com.orion.utils.reflect.Methods;
import com.orion.utils.time.Dates;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.orion.excel.builder.ExcelBuilder.getAllGetterMethod;

/**
 * Excel sheet封装
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/6 23:33
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
    private short headerDefalutHeigth;

    /**
     * 是否跳过null
     */
    private boolean skipNullRow = true;

    public ExcelSheet(Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
    }

    /**
     * 添加表头
     *
     * @param header 表头
     * @return this
     */
    public ExcelSheet<T> addHeaders(String... header) {
        Row row = sheet.createRow(index++);
        if (headerDefalutHeigth != 0) {
            row.setHeight((short) (headerDefalutHeigth * 20));
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
     * @param heigth 行高
     * @param header 表头
     * @return this
     */
    public ExcelSheet<T> addHeaders(int heigth, String... header) {
        Row row = sheet.createRow(index++);
        row.setHeight((short) (heigth * 20));
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
     * @return this
     */
    public ExcelSheet<T> addHeaders(List<ExcelHeaderMap> headerMap, int heigth) {
        Row row = sheet.createRow(index++);
        if (heigth != 0) {
            row.setHeight((short) (heigth * 20));
        }
        for (int i = 0; i < headerMap.size(); i++) {
            ExcelHeaderMap map = headerMap.get(i);
            Cell cell = row.createCell(i);
            cell.setCellValue(map.getName());
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
     * 添加表头
     *
     * @param headerMap 表头信息
     * @return this
     */
    public ExcelSheet<T> addHeaders(List<ExcelHeaderMap> headerMap) {
        return this.addHeaders(headerMap, headerDefalutHeigth);
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
     * @param heigth 行高
     * @return this
     */
    public ExcelSheet<T> heigth(int heigth) {
        sheet.setDefaultRowHeight((short) (heigth * 20));
        return this;
    }

    /**
     * 设置表头默认行高
     *
     * @param heigth 行高
     * @return this
     */
    public ExcelSheet<T> headerHeigth(int heigth) {
        headerDefalutHeigth = (short) heigth;
        return this;
    }

    /**
     * 合并单元格
     *
     * @param sheet     sheet
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
     * @param sheet     sheet
     * @param row       合并行
     * @param firstCell 合并开始单元格
     * @param lastCell  合并结束单元格
     */
    public ExcelSheet<T> mergeCell(int row, int firstCell, int lastCell) {
        sheet.addMergedRegion(new CellRangeAddress(row, row, firstCell, lastCell));
        return this;
    }

    /**
     * 添加列 根据bean的getter方法
     *
     * @param list      列
     * @param fieldMaps 配置
     * @return this
     */
    public ExcelSheet<T> addRows(List<T> list, List<ExcelFieldMap> fieldMaps) {
        List<Method> allGetterMethod = null;
        for (T t : list) {
            if (t == null) {
                if (skipNullRow) {
                    index++;
                }
                return this;
            }
            if (allGetterMethod == null) {
                allGetterMethod = getAllGetterMethod(t.getClass());
            }
            Row row = sheet.createRow(index++);
            for (ExcelFieldMap fieldMap : fieldMaps) {
                Integer index = fieldMap.getColumnIndex();
                if (index == null) {
                    continue;
                }
                Cell cell = row.createCell(index);
                String fieldName = fieldMap.getFieldName();
                if (fieldName == null) {
                    continue;
                }
                for (Method method : allGetterMethod) {
                    String methodName = method.getName();
                    String getterFieldName;
                    if (method.getReturnType() == boolean.class) {
                        getterFieldName = Strings.firstLower(methodName.substring(2, methodName.length()));
                    } else {
                        getterFieldName = Strings.firstLower(methodName.substring(3, methodName.length()));
                    }
                    if (!fieldName.equals(getterFieldName)) {
                        continue;
                    }
                    CellStyle style = fieldMap.getCellStyle();
                    if (style != null) {
                        cell.setCellStyle(style);
                    }
                    Font font = fieldMap.getFont();
                    if (font != null) {
                        if (style == null) {
                            style = workbook.createCellStyle();
                        }
                        style.setFont(font);
                        cell.setCellStyle(style);
                    }

                    Object v = Methods.invokeMethod(t, method);
                    this.setCellValue(cell, v, fieldMap.getFieldType(), fieldMap.getDatePattern());
                    break;
                }
            }
        }
        return this;
    }

    /**
     * 添加列 根据bean的getter方法
     *
     * @param list      列
     * @param fieldMaps 配置
     * @return this
     */
    public ExcelSheet<T> addMaps(List<Map<Integer, ?>> list, List<ExcelFieldMap> fieldMaps) {
        for (Map<Integer, ?> t : list) {
            if (t == null) {
                if (skipNullRow) {
                    index++;
                }
                return this;
            }
            Row row = sheet.createRow(index++);
            for (Map.Entry<Integer, ?> entry : t.entrySet()) {
                Integer columnIndex = entry.getKey();
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }
                Cell cell = row.createCell(columnIndex);
                boolean set = false;
                for (ExcelFieldMap fieldMap : fieldMaps) {
                    if (!fieldMap.getFieldIndex().equals(columnIndex)) {
                        continue;
                    }
                    CellStyle style = fieldMap.getCellStyle();
                    if (style != null) {
                        cell.setCellStyle(style);
                    }
                    Font font = fieldMap.getFont();
                    if (font != null) {
                        if (style == null) {
                            style = workbook.createCellStyle();
                        }
                        style.setFont(font);
                        cell.setCellStyle(style);
                    }
                    this.setCellValue(cell, value, fieldMap.getFieldType(), fieldMap.getDatePattern());
                    set = true;
                    break;
                }
                if (!set) {
                    this.setCellValue(cell, value, null, null);
                }
            }
        }
        return this;
    }

    /**
     * 添加列 根据bean的getter方法
     *
     * @param list      列
     * @param fieldMaps 配置
     * @return this
     */
    public ExcelSheet<T> addArrays(List<Object[]> list, List<ExcelFieldMap> fieldMaps) {
        for (Object[] t : list) {
            if (t == null) {
                if (skipNullRow) {
                    index++;
                }
                return this;
            }
            Row row = sheet.createRow(index++);
            for (int i = 0; i < t.length; i++) {
                Cell cell = row.createCell(i);
                Object value = t[i];
                if (value == null) {
                    continue;
                }
                boolean set = false;
                for (ExcelFieldMap fieldMap : fieldMaps) {
                    if (!fieldMap.getFieldIndex().equals(i)) {
                        continue;
                    }
                    CellStyle style = fieldMap.getCellStyle();
                    if (style != null) {
                        cell.setCellStyle(style);
                    }
                    Font font = fieldMap.getFont();
                    if (font != null) {
                        if (style == null) {
                            style = workbook.createCellStyle();
                        }
                        style.setFont(font);
                        cell.setCellStyle(style);
                    }
                    this.setCellValue(cell, value, fieldMap.getFieldType(), fieldMap.getDatePattern());
                    set = true;
                    break;
                }
                if (!set) {
                    this.setCellValue(cell, value, null, null);
                }
            }
        }
        return this;
    }

    /**
     * 设置cell的值
     *
     * @param cell  cell
     * @param value value
     * @param type  type
     */
    private void setCellValue(Cell cell, Object value, ExcelFieldType type, String datePattern) {
        if (type == null) {
            cell.setCellValue(Objects1.toString(value));
        } else {
            switch (type) {
                case TEXT:
                    cell.setCellValue(Objects1.toString(value));
                    break;
                case NUMBER:
                    if (value == null) {
                        cell.setCellValue(0D);
                    } else {
                        cell.setCellValue(Converts.toDouble(value));
                    }
                    break;
                case DATE:
                    if (datePattern != null) {
                        cell.setCellValue(Dates.format(Converts.toDate(value), datePattern));
                    } else {
                        cell.setCellValue(Converts.toDate(value));
                    }
                    break;
                default:
                    cell.setCellValue(Objects1.toString(value));
                    break;
            }
        }
    }

    public Sheet getSheet() {
        return sheet;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public int getIndex() {
        return index;
    }

}
