package com.orion.excel.importing;

import com.monitorjbl.xlsx.impl.StreamingSheet;
import com.orion.able.SafeCloseable;
import com.orion.excel.option.ImportFieldOption;
import com.orion.excel.option.ImportSheetOption;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;
import com.orion.utils.reflect.Constructors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Excel 读取器 不支持随机读写 仅支持注解
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/27 13:52
 */
public class ExcelImport<T> implements SafeCloseable {

    private Workbook workbook;

    private Sheet sheet;

    /**
     * sheet option
     */
    private ImportSheetOption<T> sheetOption = new ImportSheetOption<>();

    /**
     * field option
     */
    private Map<Method, ImportFieldOption> fieldOptions = new HashMap<>();

    /**
     * 处理器
     */
    private ImportProcessor<T> processor;

    /**
     * 读取的记录
     */
    private List<T> rows;

    /**
     * 读取的消费器
     */
    private Consumer<T> consumer;

    /**
     * 是否存储数据
     */
    private boolean store;

    public ExcelImport(Workbook workbook, Sheet sheet, Class<T> targetClass) {
        this(workbook, sheet, targetClass, new ArrayList<>(), null);
    }

    public ExcelImport(Workbook workbook, Sheet sheet, Class<T> targetClass, List<T> store) {
        this(workbook, sheet, targetClass, store, null);
    }

    public ExcelImport(Workbook workbook, Sheet sheet, Class<T> targetClass, Consumer<T> consumer) {
        this(workbook, sheet, targetClass, null, consumer);
    }

    private ExcelImport(Workbook workbook, Sheet sheet, Class<T> targetClass, List<T> rows, Consumer<T> consumer) {
        Valid.notNull(sheet, "Sheet is null");
        Valid.notNull(targetClass, "TargetClass is null");
        if (rows == null && consumer == null) {
            throw Exceptions.argument("rows container or row consumer one of them must not be empty");
        }
        this.rows = rows;
        this.consumer = consumer;
        this.store = rows != null;
        this.workbook = workbook;
        this.sheet = sheet;
        Constructor<T> constructor = Constructors.getDefaultConstructor(targetClass);
        if (constructor == null) {
            throw Exceptions.argument("targetClass not found default constructor");
        }
        sheetOption.setConstructor(constructor);
        sheetOption.setStreaming(sheet instanceof StreamingSheet);
        sheetOption.setRowNum(sheet.getLastRowNum() + 1);
        // 解析器
        ImportColumnAnalysis columnAnalysis = new ImportColumnAnalysis(targetClass, sheetOption, fieldOptions);
        columnAnalysis.analysis();
        // 处理器
        this.processor = new ImportProcessor<>(this.workbook, this.sheet, sheetOption, fieldOptions);
    }

    /**
     * 初始化
     *
     * @return this
     */
    public ExcelImport<T> init() {
        this.processor.init();
        return this;
    }

    /**
     * 跳过一行
     *
     * @return this
     */
    public ExcelImport<T> skip() {
        processor.readRow++;
        return this;
    }

    /**
     * 跳过多行
     *
     * @param i 跳过的行数
     * @return this
     */
    public ExcelImport<T> skip(int i) {
        processor.readRow += i;
        return this;
    }

    /**
     * 是否跳过空行
     *
     * @param skip 如果row为null true: continue false: rows.add(null)
     * @return this
     */
    public ExcelImport<T> skipNullRows(boolean skip) {
        sheetOption.setSkipNullRows(skip);
        return this;
    }

    /**
     * 读取所有行
     *
     * @return this
     */
    public ExcelImport<T> read() {
        while (!processor.end) {
            this.readRow();
        }
        return this;
    }

    /**
     * 读取多行
     *
     * @param i 行数
     * @return this
     */
    public ExcelImport<T> read(int i) {
        for (int j = 0; j < i; j++) {
            this.readRow();
        }
        return this;
    }

    /**
     * 读取一行
     */
    private void readRow() {
        T row = processor.read();
        if (row == null && sheetOption.isSkipNullRows()) {
            return;
        }
        if (store) {
            rows.add(row);
        } else {
            consumer.accept(row);
        }
    }

    @Override
    public void close() {
        Streams.close(workbook);
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public ImportSheetOption<T> getSheetOption() {
        return sheetOption;
    }

    public Map<Method, ImportFieldOption> getFieldOptions() {
        return fieldOptions;
    }

    /**
     * @return 读取的行
     */
    public List<T> getRows() {
        return rows;
    }

    /**
     * @return 读取的行数
     */
    public int getRowNum() {
        return processor.readRow;
    }

    /**
     * @return sheet行数
     */
    public int getLines() {
        return sheetOption.getRowNum();
    }

}
