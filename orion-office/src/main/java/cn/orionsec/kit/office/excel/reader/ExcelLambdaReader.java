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
package cn.orionsec.kit.office.excel.reader;

import cn.orionsec.kit.lang.define.wrapper.Pair;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.office.excel.Excels;
import cn.orionsec.kit.office.excel.option.CellOption;
import cn.orionsec.kit.office.excel.option.ImportFieldOption;
import cn.orionsec.kit.office.excel.type.ExcelReadType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * excel lambda 读取器
 * <p>
 * 支持高级数据类型
 * <p>
 * {@link Excels#getCellValue(Cell, ExcelReadType, CellOption)}
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/13 10:53
 */
public class ExcelLambdaReader<T> extends BaseExcelReader<Pair<Function<?, ?>, BiConsumer<T, ?>>, T> {

    /**
     * 映射
     * key: column
     * value: convert consumer
     */
    protected Map<Integer, Pair<Function<?, ?>, BiConsumer<T, ?>>> mapping;

    /**
     * supplier
     */
    protected Supplier<T> supplier;

    /**
     * 为 null 是否调用
     */
    protected boolean nullInvoke;

    /**
     * 如果行为 null 是否添加一个新的实例对象
     */
    private boolean nullAddEmptyBean;

    public ExcelLambdaReader(Workbook workbook, Sheet sheet, Supplier<T> supplier) {
        this(workbook, sheet, new ArrayList<>(), null, supplier);
    }

    public ExcelLambdaReader(Workbook workbook, Sheet sheet, List<T> store, Supplier<T> supplier) {
        this(workbook, sheet, store, null, supplier);
    }

    public ExcelLambdaReader(Workbook workbook, Sheet sheet, Consumer<T> consumer, Supplier<T> supplier) {
        this(workbook, sheet, null, consumer, supplier);
    }

    protected ExcelLambdaReader(Workbook workbook, Sheet sheet, List<T> store, Consumer<T> consumer, Supplier<T> supplier) {
        super(workbook, sheet, store, consumer);
        this.supplier = Valid.notNull(supplier, "supplier is null");
        this.options = new HashMap<>();
        this.init = false;
    }

    public static <T> ExcelLambdaReader<T> create(Workbook workbook, Sheet sheet, Supplier<T> supplier) {
        return new ExcelLambdaReader<>(workbook, sheet, supplier);
    }

    public static <T> ExcelLambdaReader<T> create(Workbook workbook, Sheet sheet, List<T> store, Supplier<T> supplier) {
        return new ExcelLambdaReader<>(workbook, sheet, store, supplier);
    }

    public static <T> ExcelLambdaReader<T> create(Workbook workbook, Sheet sheet, Consumer<T> consumer, Supplier<T> supplier) {
        return new ExcelLambdaReader<>(workbook, sheet, consumer, supplier);
    }

    /**
     * 如果列为 null 是否调用 function consumer
     *
     * @return this
     */
    public ExcelLambdaReader<T> nullInvoke() {
        this.nullInvoke = true;
        return this;
    }

    /**
     * 如果行为 null 是否添加实例对象
     *
     * @return this
     */
    public ExcelLambdaReader<T> nullAddEmptyBean() {
        this.nullAddEmptyBean = true;
        return this;
    }

    public <V> ExcelLambdaReader<T> option(int column, ExcelReadType type, BiConsumer<T, V> consumer) {
        super.addOption(Pair.of(Function.identity(), consumer), new ImportFieldOption(column, type));
        return this;
    }

    /**
     * 映射
     *
     * @param column   列
     * @param type     类型
     * @param convert  convert
     * @param consumer consumer
     * @param <E>      {@link ExcelReadType}
     * @param <V>      {@link ExcelReadType}
     * @return this
     */
    public <E, V> ExcelLambdaReader<T> option(int column, ExcelReadType type, Function<E, V> convert, BiConsumer<T, V> consumer) {
        super.addOption(Pair.of(convert, consumer), new ImportFieldOption(column, type));
        return this;
    }

    public <V> ExcelLambdaReader<T> option(ImportFieldOption option, BiConsumer<T, V> consumer) {
        super.addOption(Pair.of(Function.identity(), consumer), option);
        return this;
    }

    /**
     * 映射
     *
     * @param option   配置
     * @param convert  convert
     * @param consumer consumer
     * @param <E>      {@link ExcelReadType}
     * @param <V>      {@link ExcelReadType}
     * @return this
     */
    public <E, V> ExcelLambdaReader<T> option(ImportFieldOption option, Function<E, V> convert, BiConsumer<T, V> consumer) {
        super.addOption(Pair.of(convert, consumer), option);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T parserRow(Row row) {
        if (row == null) {
            if (nullAddEmptyBean) {
                return supplier.get();
            }
            return null;
        }
        T current = supplier.get();
        options.forEach((pair, option) -> {
            int index = option.getIndex();
            Cell cell = row.getCell(index);
            Object value;
            if (option.getType().equals(ExcelReadType.PICTURE)) {
                // 图片
                value = super.getPicture(index, row);
            } else {
                // 值
                value = Excels.getCellValue(cell, option.getType(), option.getCellOption());
            }
            if (value != null || nullInvoke) {
                if (trim && value instanceof String) {
                    value = ((String) value).trim();
                }
                // 调用setter
                Function<Object, Object> convert = (Function<Object, Object>) pair.getKey();
                BiConsumer<T, Object> consumer = (BiConsumer<T, Object>) pair.getValue();
                Object val = convert.apply(value);
                consumer.accept(current, val);
            }
        });
        return current;
    }

}
