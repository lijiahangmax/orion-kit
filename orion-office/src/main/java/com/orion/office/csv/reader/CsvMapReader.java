package com.orion.office.csv.reader;

import com.orion.lang.collect.MutableHashMap;
import com.orion.lang.collect.MutableLinkedHashMap;
import com.orion.lang.collect.MutableMap;
import com.orion.office.csv.core.CsvReader;

import java.util.*;
import java.util.function.Consumer;

/**
 * Csv Map 读取器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/3 18:04
 */
public class CsvMapReader<K, V> extends BaseCsvReader<MutableMap<K, V>> {

    /**
     * 默认值
     * key: 列
     * value: 默认值
     */
    private Map<K, V> defaultValue = new HashMap<>();

    /**
     * 为null是否插入kay
     */
    private boolean nullPutKey = true;

    /**
     * 是否使用 linkedMap
     */
    private boolean linked;

    /**
     * 映射
     * key: column
     * value: valueKey
     */
    protected Map<Integer, K> mapping = new TreeMap<>();

    public CsvMapReader(CsvReader reader) {
        this(reader, new ArrayList<>(), null);
    }

    public CsvMapReader(CsvReader reader, List<MutableMap<K, V>> rows) {
        this(reader, rows, null);
    }

    public CsvMapReader(CsvReader reader, Consumer<MutableMap<K, V>> consumer) {
        this(reader, null, consumer);
    }

    protected CsvMapReader(CsvReader reader, List<MutableMap<K, V>> rows, Consumer<MutableMap<K, V>> consumer) {
        super(reader, rows, consumer);
    }

    /**
     * 映射
     *
     * @param k      valueKey
     * @param column column
     * @return this
     */
    public CsvMapReader<K, V> mapping(K k, int column) {
        return mapping(column, k);
    }

    /**
     * 映射
     *
     * @param column column
     * @param k      valueKey
     * @return this
     */
    public CsvMapReader<K, V> mapping(int column, K k) {
        mapping.put(column, k);
        return this;
    }

    /**
     * 使用 linkedMap
     *
     * @return this
     */
    public CsvMapReader<K, V> linked() {
        this.linked = true;
        return this;
    }

    /**
     * 如果为null是否插入key
     *
     * @param nullPutKey ignore
     * @return this
     */
    public CsvMapReader<K, V> nullPutKey(boolean nullPutKey) {
        this.nullPutKey = nullPutKey;
        return this;
    }

    /**
     * 设置默认值
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return this
     */
    public CsvMapReader<K, V> defaultValue(K key, V defaultValue) {
        this.defaultValue.put(key, defaultValue);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected MutableMap<K, V> parserRow(String[] row) {
        MutableMap<K, V> map;
        if (linked) {
            map = new MutableLinkedHashMap<>();
        } else {
            map = new MutableHashMap<>();
        }
        mapping.forEach((i, key) -> {
            String value = this.get(row, i);
            if (value == null) {
                V defaultValue = this.defaultValue.get(key);
                if (defaultValue == null) {
                    if (nullPutKey) {
                        map.put(key, null);
                    }
                } else {
                    map.put(key, defaultValue);
                }
            } else {
                map.put(key, (V) value);
            }
        });
        return map;
    }

}
