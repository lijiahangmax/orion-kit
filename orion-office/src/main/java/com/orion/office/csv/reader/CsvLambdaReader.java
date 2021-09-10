package com.orion.office.csv.reader;

import com.orion.lang.wrapper.Pair;
import com.orion.office.csv.core.CsvReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Csv lambda 读取器
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
    protected Map<Integer, Pair<Function<String, ?>, BiConsumer<T, ?>>> mapping;

    /**
     * supplier
     */
    protected Supplier<T> supplier;

    /**
     * 为null是否调用
     */
    protected boolean nullInvoke;

    public CsvLambdaReader(CsvReader reader, Supplier<T> supplier) {
        this(reader, new ArrayList<>(), null, supplier);
    }

    public CsvLambdaReader(CsvReader reader, List<T> rows, Supplier<T> supplier) {
        this(reader, rows, null, supplier);
    }

    public CsvLambdaReader(CsvReader reader, Consumer<T> consumer, Supplier<T> supplier) {
        this(reader, null, consumer, supplier);
    }

    protected CsvLambdaReader(CsvReader reader, List<T> rows, Consumer<T> consumer, Supplier<T> supplier) {
        super(reader, rows, consumer);
        this.supplier = supplier;
        this.mapping = new TreeMap<>();
    }

    /**
     * 如果列为null是否调用consumer
     *
     * @return this
     */
    public CsvLambdaReader<T> nullInvoke() {
        this.nullInvoke = true;
        this.mapping = new TreeMap<>();
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
                BiConsumer<T, Object> setter = (BiConsumer<T, Object>) v.getValue();
                Object val = convert.apply(value);
                setter.accept(current, val);
            }
        });
        return current;
    }

}
