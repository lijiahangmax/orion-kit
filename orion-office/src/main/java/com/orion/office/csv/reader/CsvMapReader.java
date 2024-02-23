package com.orion.office.csv.reader;

import com.orion.lang.define.collect.MutableHashMap;
import com.orion.lang.define.collect.MutableLinkedHashMap;
import com.orion.lang.define.collect.MutableMap;
import com.orion.office.csv.core.CsvReader;

import java.util.*;
import java.util.function.Consumer;

/**
 * csv map 读取器
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
    private final Map<K, V> defaultValue;

    /**
     * 为 null 是否插入 kay
     */
    private boolean nullPutKey;

    /**
     * 是否使用 linkedMap
     */
    private boolean linked;

    /**
     * 映射
     * key: column
     * value: valueKey
     */
    protected final Map<Integer, K> mapping;

    public CsvMapReader(CsvReader reader) {
        this(reader, new ArrayList<>(), null);
    }

    public CsvMapReader(CsvReader reader, Collection<MutableMap<K, V>> rows) {
        this(reader, rows, null);
    }

    public CsvMapReader(CsvReader reader, Consumer<MutableMap<K, V>> consumer) {
        this(reader, null, consumer);
    }

    protected CsvMapReader(CsvReader reader, Collection<MutableMap<K, V>> rows, Consumer<MutableMap<K, V>> consumer) {
        super(reader, rows, consumer);
        this.nullPutKey = true;
        this.defaultValue = new HashMap<>();
        this.mapping = new TreeMap<>();
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
     * 如果为 null 是否插入 key
     *
     * @param nullPutKey ignore
     * @return this
     */
    public CsvMapReader<K, V> nullPutKey(boolean nullPutKey) {
        this.nullPutKey = nullPutKey;
        return this;
    }

    public CsvMapReader<K, V> mapping(int column, K k) {
        return this.mapping(column, k, null);
    }

    /**
     * 映射
     *
     * @param column       column
     * @param k            valueKey
     * @param defaultValue defaultValue
     * @return this
     */
    public CsvMapReader<K, V> mapping(int column, K k, V defaultValue) {
        mapping.put(column, k);
        if (defaultValue != null) {
            this.defaultValue.put(k, defaultValue);
        }
        return this;
    }

    /**
     * 设置默认值
     *
     * @param key   key
     * @param value 默认值
     * @return this
     */
    public CsvMapReader<K, V> defaultValue(K key, V value) {
        defaultValue.put(key, value);
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
            // 获取值
            String value = this.get(row, i);
            if (value == null) {
                // 默认值
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
