/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.orion.office.excel.reader;

import com.orion.lang.define.collect.MutableHashMap;
import com.orion.lang.define.collect.MutableLinkedHashMap;
import com.orion.lang.define.collect.MutableMap;
import com.orion.office.excel.Excels;
import com.orion.office.excel.option.ImportFieldOption;
import com.orion.office.excel.type.ExcelReadType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;
import java.util.function.Consumer;

/**
 * excel map 读取器
 * <p>
 * 支持高级数据类型
 * <p>
 * {@link Excels#getCellValue(Cell, ExcelReadType, com.orion.office.excel.option.CellOption)}
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/5 11:52
 */
public class ExcelMapReader<K, V> extends BaseExcelReader<K, MutableMap<K, V>> {

    /**
     * 默认值
     * key: 列
     * value: 默认值
     */
    private final Map<K, V> defaultValue;

    /**
     * 为 null 是否插入 kay
     */
    private boolean nullPutKey;

    /**
     * 是否使用 linkedMap
     */
    private boolean linked;

    public ExcelMapReader(Workbook workbook, Sheet sheet) {
        this(workbook, sheet, new ArrayList<>(), null);
    }

    public ExcelMapReader(Workbook workbook, Sheet sheet, List<MutableMap<K, V>> store) {
        this(workbook, sheet, store, null);
    }

    public ExcelMapReader(Workbook workbook, Sheet sheet, Consumer<MutableMap<K, V>> consumer) {
        this(workbook, sheet, null, consumer);
    }

    private ExcelMapReader(Workbook workbook, Sheet sheet, List<MutableMap<K, V>> store, Consumer<MutableMap<K, V>> consumer) {
        super(workbook, sheet, store, consumer);
        this.init = false;
        this.nullPutKey = true;
        this.options = new HashMap<>();
        this.defaultValue = new HashMap<>();
    }

    public static <K, V> ExcelMapReader<K, V> create(Workbook workbook, Sheet sheet) {
        return new ExcelMapReader<>(workbook, sheet);
    }

    public static <K, V> ExcelMapReader<K, V> create(Workbook workbook, Sheet sheet, List<MutableMap<K, V>> store) {
        return new ExcelMapReader<>(workbook, sheet, store);
    }

    public static <K, V> ExcelMapReader<K, V> create(Workbook workbook, Sheet sheet, Consumer<MutableMap<K, V>> consumer) {
        return new ExcelMapReader<>(workbook, sheet, consumer);
    }

    public ExcelMapReader<K, V> option(K key, ImportFieldOption option) {
        this.addOption(key, option, null);
        return this;
    }

    public ExcelMapReader<K, V> option(K key, ImportFieldOption option, V defaultValue) {
        this.addOption(key, option, defaultValue);
        return this;
    }

    public ExcelMapReader<K, V> option(int column, K key, ExcelReadType type) {
        this.addOption(key, new ImportFieldOption(column, type), null);
        return this;
    }

    /**
     * 添加配置
     *
     * @param column       列
     * @param key          key
     * @param type         类型
     * @param defaultValue 默认值
     * @return this
     */
    public ExcelMapReader<K, V> option(int column, K key, ExcelReadType type, V defaultValue) {
        this.addOption(key, new ImportFieldOption(column, type), defaultValue);
        return this;
    }

    /**
     * 添加配置
     *
     * @param key          key
     * @param option       配置
     * @param defaultValue 默认值
     */
    protected void addOption(K key, ImportFieldOption option, V defaultValue) {
        super.addOption(key, option);
        // 默认值
        if (defaultValue != null) {
            this.defaultValue.put(key, defaultValue);
        }
    }

    /**
     * 设置默认值
     *
     * @param key   key
     * @param value 默认值
     * @return this
     */
    public ExcelMapReader<K, V> defaultValue(K key, V value) {
        defaultValue.put(key, value);
        return this;
    }

    /**
     * 使用 linkedMap
     *
     * @return this
     */
    public ExcelMapReader<K, V> linked() {
        this.linked = true;
        this.options = new LinkedHashMap<>(options);
        return this;
    }

    /**
     * 如果为null是否插入key
     *
     * @param nullPutKey ignore
     * @return this
     */
    public ExcelMapReader<K, V> nullPutKey(boolean nullPutKey) {
        this.nullPutKey = nullPutKey;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected MutableMap<K, V> parserRow(Row row) {
        if (row == null) {
            return null;
        }
        MutableMap<K, V> map;
        if (linked) {
            map = new MutableLinkedHashMap<>();
        } else {
            map = new MutableHashMap<>();
        }
        options.forEach((key, option) -> {
            int column = option.getIndex();
            Cell cell = row.getCell(column);
            ExcelReadType type = option.getType();
            Object value = null;
            if (cell != null) {
                if (type.equals(ExcelReadType.PICTURE)) {
                    // 获取图片
                    value = this.getPicture(column, row);
                } else {
                    // 获取值
                    value = Excels.getCellValue(cell, type, option.getCellOption());
                }
            }
            if (value == null) {
                // 默认值
                V defaultValue = this.defaultValue.get(key);
                if (defaultValue != null || nullPutKey) {
                    map.put(key, defaultValue);
                } else {
                    map.put(key, null);
                }
            } else {
                if (trim && value instanceof String) {
                    value = ((String) value).trim();
                }
                map.put(key, (V) value);
            }
        });
        return map;
    }

}
