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
package com.orion.office.csv.reader;

import com.orion.lang.define.wrapper.Pair;
import com.orion.lang.utils.Valid;
import com.orion.office.csv.core.CsvReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * csv lambda 读取器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/8 18:47
 */
public class CsvLambdaReader<T> extends BaseCsvReader<T> {

    /**
     * 映射
     * key: column
     * value: convert consumer
     */
    protected final Map<Integer, Pair<Function<String, ?>, BiConsumer<T, ?>>> mapping;

    /**
     * supplier
     */
    protected final Supplier<T> supplier;

    /**
     * 为null是否调用
     */
    protected boolean nullInvoke;

    public CsvLambdaReader(CsvReader reader, Supplier<T> supplier) {
        this(reader, new ArrayList<>(), null, supplier);
    }

    public CsvLambdaReader(CsvReader reader, Collection<T> rows, Supplier<T> supplier) {
        this(reader, rows, null, supplier);
    }

    public CsvLambdaReader(CsvReader reader, Consumer<T> consumer, Supplier<T> supplier) {
        this(reader, null, consumer, supplier);
    }

    protected CsvLambdaReader(CsvReader reader, Collection<T> rows, Consumer<T> consumer, Supplier<T> supplier) {
        super(reader, rows, consumer);
        this.supplier = Valid.notNull(supplier, "supplier is null");
        this.mapping = new TreeMap<>();
    }

    /**
     * 如果列为 null 是否调用 function consumer
     *
     * @return this
     */
    public CsvLambdaReader<T> nullInvoke() {
        this.nullInvoke = true;
        return this;
    }

    public CsvLambdaReader<T> mapping(int column, BiConsumer<T, String> consumer) {
        mapping.put(column, Pair.of(Function.identity(), consumer));
        return this;
    }

    /**
     * 映射
     *
     * @param column   column
     * @param convert  转换器
     * @param consumer 消费者
     * @return this
     */
    public <V> CsvLambdaReader<T> mapping(int column, Function<String, V> convert, BiConsumer<T, V> consumer) {
        mapping.put(column, Pair.of(convert, consumer));
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T parserRow(String[] row) {
        T current = supplier.get();
        mapping.forEach((k, v) -> {
            String value = this.get(row, k);
            if (value != null || nullInvoke) {
                Function<String, ?> convert = v.getKey();
                BiConsumer<T, Object> consumer = (BiConsumer<T, Object>) v.getValue();
                Object val = convert.apply(value);
                consumer.accept(current, val);
            }
        });
        return current;
    }

}
