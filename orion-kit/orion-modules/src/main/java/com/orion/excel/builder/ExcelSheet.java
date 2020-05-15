package com.orion.excel.builder;

import com.orion.utils.Converts;
import com.orion.utils.Objects1;
import com.orion.utils.Strings;
import com.orion.utils.reflect.Methods;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

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

    private Sheet sheet;

    /**
     * 当前行索引
     */
    private int index;

    public ExcelSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * 添加头
     *
     * @param header 头
     * @return this
     */
    public ExcelSheet addHeader(String... header) {
        Row row = sheet.createRow(index++);
        for (int i = 0; i < header.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(header[i]);
        }
        return this;
    }

    /**
     * 跳过一行
     *
     * @return this
     */
    public ExcelSheet skip() {
        index++;
        return this;
    }

    /**
     * 跳过多行
     *
     * @param i 行
     * @return this
     */
    public ExcelSheet skip(int i) {
        index += i;
        return this;
    }

    /**
     * 添加列 根据bean的getter方法
     *
     * @param list     list
     * @param fieldMap key: bean字段 value: cell索引
     * @param typeMap  key: bean字段 value: cell类型
     * @return this
     */
    public ExcelSheet addRows(List<T> list, Map<String, Integer> fieldMap, Map<String, ExcelFieldType> typeMap) {
        for (T t : list) {
            addRow(t, fieldMap, typeMap);
        }
        return this;
    }

    /**
     * 添加列 根据bean的getter方法
     *
     * @param list     list
     * @param fieldMap key: bean字段 value: cell索引
     * @return this
     */
    public ExcelSheet addRows(List<T> list, Map<String, Integer> fieldMap) {
        for (T t : list) {
            addRow(t, fieldMap, null);
        }
        return this;
    }

    /**
     * 添加列
     *
     * @param maps    key: cell索引 value: 值
     * @param typeMap key: cell索引 value: cell类型
     * @return this
     */
    public ExcelSheet addMaps(List<Map<Integer, ?>> maps, Map<Integer, ExcelFieldType> typeMap) {
        for (Map<Integer, ?> map : maps) {
            addMap(map, typeMap);
        }
        return this;
    }

    /**
     * 添加列
     *
     * @param maps key: cell索引 value: 值
     * @return this
     */
    public ExcelSheet addMaps(List<Map<Integer, ?>> maps) {
        for (Map<Integer, ?> map : maps) {
            addMap(map, null);
        }
        return this;
    }

    /**
     * 添加列
     *
     * @param arrays   array
     * @param fieldMap key: cell索引 value: array索引
     * @param typeMap  key: cell索引 value: cell类型
     * @return this
     */
    public ExcelSheet addArrays(List<Object[]> arrays, Map<Integer, Integer> fieldMap, Map<Integer, ExcelFieldType> typeMap) {
        for (Object[] arr : arrays) {
            addArray(arr, fieldMap, typeMap);
        }
        return this;
    }

    /**
     * 添加列
     *
     * @param arrays   array
     * @param fieldMap key: cell索引 value: array索引
     * @return this
     */
    public ExcelSheet addArrays(List<Object[]> arrays, Map<Integer, Integer> fieldMap) {
        for (Object[] arr : arrays) {
            addArray(arr, fieldMap, null);
        }
        return this;
    }

    /**
     * 添加列
     *
     * @param arrays array
     * @return this
     */
    public ExcelSheet addArrays(List<Object[]> arrays) {
        for (Object[] arr : arrays) {
            addArray(arr, null, null);
        }
        return this;
    }

    /**
     * 添加列 根据bean的getter方法
     *
     * @param t        bean
     * @param fieldMap key: bean字段 value: cell索引
     * @return this
     */
    public ExcelSheet addRow(T t, Map<String, Integer> fieldMap) {
        return addRow(t, fieldMap, null);
    }

    /**
     * 添加列 根据bean的getter方法
     *
     * @param t        bean
     * @param fieldMap key: bean字段 value: cell索引
     * @param typeMap  key: bean字段 value: cell类型
     * @return this
     */
    public ExcelSheet addRow(T t, Map<String, Integer> fieldMap, Map<String, ExcelFieldType> typeMap) {
        if (t == null) {
            index++;
            return this;
        }
        Row row = sheet.createRow(index++);
        List<Method> allGetterMethod = getAllGetterMethod(t.getClass());
        for (Method method : allGetterMethod) {
            String methodName = method.getName();
            String fieldName = Strings.firstLower(methodName.substring(3, methodName.length()));
            Integer cellIndex = fieldMap.get(fieldName);
            if (cellIndex != null) {
                Cell cell = row.createCell(cellIndex);
                ExcelFieldType type = null;
                if (typeMap != null) {
                    ExcelFieldType excelFieldType = typeMap.get(fieldName);
                    if (excelFieldType != null) {
                        type = excelFieldType;
                    }
                }
                Object v = Methods.invokeMethod(t, method);
                this.setCellValue(cell, v, type);
            }
        }
        return this;
    }

    /**
     * 添加列
     *
     * @param map key: cell索引 value: 值
     * @return this
     */
    public ExcelSheet addMap(Map<Integer, ?> map) {
        return addMap(map, null);
    }

    /**
     * 添加列
     *
     * @param map     key: cell索引 value: 值
     * @param typeMap key: cell索引 value: cell类型
     * @return this
     */
    public ExcelSheet addMap(Map<Integer, ?> map, Map<Integer, ExcelFieldType> typeMap) {
        if (map == null) {
            index++;
            return this;
        }
        Row row = sheet.createRow(index++);
        for (Map.Entry<Integer, ?> entry : map.entrySet()) {
            Integer cellIndex = entry.getKey();
            if (cellIndex != null) {
                Cell cell = row.createCell(cellIndex);
                ExcelFieldType type = null;
                if (typeMap != null) {
                    ExcelFieldType excelFieldType = typeMap.get(cellIndex);
                    if (excelFieldType != null) {
                        type = excelFieldType;
                    }
                }
                this.setCellValue(cell, entry.getValue(), type);
            }
        }
        return this;
    }

    /**
     * 添加列
     *
     * @param arr array
     * @return this
     */
    public ExcelSheet addArray(Object[] arr) {
        return addArray(arr, null, null);
    }

    /**
     * 添加列
     *
     * @param arr      array
     * @param fieldMap key: cell索引 value: array索引
     * @return this
     */
    public ExcelSheet addArray(Object[] arr, Map<Integer, Integer> fieldMap) {
        return addArray(arr, fieldMap, null);
    }

    /**
     * 添加列
     *
     * @param arr      array
     * @param fieldMap key: cell索引 value: array索引
     * @param typeMap  key: cell索引 value: cell类型
     * @return this
     */
    public ExcelSheet addArray(Object[] arr, Map<Integer, Integer> fieldMap, Map<Integer, ExcelFieldType> typeMap) {
        if (arr == null) {
            index++;
            return this;
        }
        Row row = sheet.createRow(index++);
        if (fieldMap == null) {
            for (int i = 0; i < arr.length; i++) {
                Cell cell = row.createCell(i);
                ExcelFieldType type = null;
                if (typeMap != null) {
                    ExcelFieldType excelFieldType = typeMap.get(i);
                    if (excelFieldType != null) {
                        type = excelFieldType;
                    }
                }
                this.setCellValue(cell, arr[i], type);
            }
        } else {
            for (Map.Entry<Integer, Integer> entry : fieldMap.entrySet()) {
                Integer cellIndex = entry.getKey();
                if (cellIndex != null) {
                    Cell cell = row.createCell(cellIndex);
                    ExcelFieldType type = null;
                    if (typeMap != null) {
                        ExcelFieldType excelFieldType = typeMap.get(cellIndex);
                        if (excelFieldType != null) {
                            type = excelFieldType;
                        }
                    }
                    this.setCellValue(cell, arr[entry.getValue()], type);
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
    private void setCellValue(Cell cell, Object value, ExcelFieldType type) {
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
                    cell.setCellValue(Converts.toDate(value));
                    break;
                default:
                    cell.setCellValue(Objects1.toString(value));
                    break;
            }
        }
    }

    /**
     * 获取当前表格
     *
     * @return sheet
     */
    public Sheet getSheet() {
        return sheet;
    }

}
