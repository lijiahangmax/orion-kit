package com.orion.office.excel.reader;

import com.orion.lang.define.wrapper.Pair;
import com.orion.lang.utils.Valid;
import com.orion.office.excel.Excels;
import com.orion.office.excel.option.ImportFieldOption;
import com.orion.office.excel.type.ExcelReadType;
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
 * {@link Excels#getCellValue(Cell, ExcelReadType, com.orion.office.excel.option.CellOption)}
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
     * 为null是否调用
     */
    protected boolean nullInvoke;

    /**
     * 如果行为null是否添加一个新的实例对象
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

    protected ExcelLambdaReader(Workbook workbook, Sheet sheet, List<T> rows, Consumer<T> consumer, Supplier<T> supplier) {
        super(workbook, sheet, rows, consumer);
        this.supplier = Valid.notNull(supplier, "supplier is null");
        this.options = new HashMap<>();
        this.init = false;
    }

    /**
     * 如果列为null是否调用function consumer
     *
     * @return this
     */
    public ExcelLambdaReader<T> nullInvoke() {
        this.nullInvoke = true;
        return this;
    }

    /**
     * 如果行为null是否添加实例对象
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
