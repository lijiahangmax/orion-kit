package com.orion.office.excel.reader;

import com.monitorjbl.xlsx.impl.StreamingSheet;
import com.orion.lang.able.SafeCloseable;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Streams;
import com.orion.office.excel.option.ImportFieldOption;
import com.orion.office.excel.picture.PictureParser;
import com.orion.office.excel.type.ExcelReadType;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * excel 读取器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/4 17:09
 */
public abstract class BaseExcelReader<K, T> implements SafeCloseable, Iterable<T> {

    protected Workbook workbook;

    protected Sheet sheet;

    /**
     * 行迭代器
     */
    protected Iterator<Row> iterator;

    /**
     * 迭代器索引
     */
    protected int currentIndex;

    /**
     * 行索引
     */
    protected int rowIndex;

    /**
     * 读取的行
     */
    protected int rowNum;

    /**
     * 总行数
     */
    protected int lines;

    /**
     * 是否已经读取完毕
     */
    protected boolean end;

    /**
     * 是否跳过空行
     */
    protected boolean skipNullRows;

    /**
     * 是否清除空格
     *
     * @see String
     */
    protected boolean trim;

    /**
     * 是否为流式读取
     */
    protected boolean streaming;

    /**
     * 读取的记录
     */
    protected List<T> rows;

    /**
     * 读取的消费器
     */
    protected Consumer<T> consumer;

    /**
     * 是否存储数据
     */
    protected boolean store;

    /**
     * 配置信息
     * key: key
     * value: 配置
     */
    protected Map<K, ImportFieldOption> options;

    /**
     * 图片解析器
     */
    protected PictureParser pictureParser;

    /**
     * 是否已经初始化
     */
    protected boolean init;

    protected BaseExcelReader(Workbook workbook, Sheet sheet, List<T> rows, Consumer<T> consumer) {
        Valid.notNull(workbook, "workbook is null");
        Valid.notNull(sheet, "sheet is null");
        if (rows == null && consumer == null) {
            throw Exceptions.argument("rows container or row consumer one of them must not be empty");
        }
        this.workbook = workbook;
        this.sheet = sheet;
        this.skipNullRows = true;
        this.rows = rows;
        this.consumer = consumer;
        this.store = rows != null;
        this.streaming = sheet instanceof StreamingSheet;
        this.lines = sheet.getLastRowNum() + 1;
        this.iterator = sheet.rowIterator();
        this.init = true;
    }

    /**
     * excel 迭代器 不会存储也不会消费
     *
     * @return 迭代器
     */
    @Override
    public ExcelReaderIterator<T> iterator() {
        return new ExcelReaderIterator<>(this);
    }

    /**
     * 初始化 默认实现
     *
     * @return this
     */
    public BaseExcelReader<K, T> init() {
        if (init) {
            // 已初始化
            return this;
        }
        this.init = true;
        boolean havePicture = this.checkHasPicture();
        if (havePicture) {
            this.pictureParser = new PictureParser(workbook, sheet);
            pictureParser.analysis();
        }
        return this;
    }

    /**
     * 跳过一行
     *
     * @return this
     */
    public BaseExcelReader<K, T> skip() {
        rowIndex++;
        return this;
    }

    /**
     * 跳过多行
     *
     * @param i 行
     * @return this
     */
    public BaseExcelReader<K, T> skip(int i) {
        rowIndex += i;
        return this;
    }

    /**
     * 跳过空行
     *
     * @param skip 是否跳过空行
     * @return this
     */
    public BaseExcelReader<K, T> skipNullRows(boolean skip) {
        this.skipNullRows = skip;
        return this;
    }

    /**
     * 是否清除空格
     *
     * @return this
     */
    public BaseExcelReader<K, T> trim() {
        this.trim = true;
        return this;
    }

    /**
     * 重新计算所有公式
     *
     * @return this
     */
    public BaseExcelReader<K, T> recalculationFormula() {
        sheet.setForceFormulaRecalculation(true);
        return this;
    }

    /**
     * 读取所有行
     *
     * @return this
     */
    public BaseExcelReader<K, T> read() {
        this.checkInit();
        while (!end) {
            this.readRow();
        }
        return this;
    }

    /**
     * 读取多行
     *
     * @param i 行
     * @return this
     */
    public BaseExcelReader<K, T> read(int i) {
        this.checkInit();
        for (int j = 0; j < i && !end; j++) {
            this.readRow();
        }
        return this;
    }

    /**
     * 清空读取的行
     *
     * @return this
     */
    public BaseExcelReader<K, T> clear() {
        if (store && rows != null) {
            this.rows.clear();
        }
        return this;
    }

    /**
     * 读取一行
     */
    protected void readRow() {
        this.checkInit();
        T row = this.nextRow();
        if (end || (row == null && skipNullRows)) {
            return;
        }
        if (store) {
            rows.add(row);
        } else {
            consumer.accept(row);
        }
        rowNum++;
    }

    /**
     * 读取一行
     *
     * @return row
     */
    protected T nextRow() {
        this.checkInit();
        if (end) {
            return null;
        }
        if (!iterator.hasNext()) {
            end = true;
            return null;
        }
        Row row = iterator.next();
        if (currentIndex++ == rowIndex) {
            rowIndex++;
            return this.parserRow(row);
        }
        return this.nextRow();
    }

    /**
     * 解析行
     *
     * @param row row
     * @return row
     */
    protected abstract T parserRow(Row row);

    /**
     * 检查是否初始化
     */
    protected void checkInit() {
        if (!init) {
            // 包含图片切未初始化则抛出异常
            if (this.checkHasPicture()) {
                throw Exceptions.init("excel reader uninitialized");
            }
            this.init = true;
        }
    }

    /**
     * 检查是否包含图片
     *
     * @return true包含
     */
    protected boolean checkHasPicture() {
        if (options == null) {
            return false;
        }
        return options.values().stream()
                .map(ImportFieldOption::getType)
                .anyMatch(ExcelReadType.PICTURE::equals);
    }

    /**
     * 检查是否为streaming支持的类型
     *
     * @param type type
     */
    protected void checkStreamingSupportType(ExcelReadType type) {
        if (streaming && (type.equals(ExcelReadType.LINK_ADDRESS)
                || type.equals(ExcelReadType.COMMENT)
                || type.equals(ExcelReadType.PICTURE))) {
            throw Exceptions.parse("streaming just support read value");
        }
    }

    /**
     * 添加配置
     *
     * @param k      k
     * @param option 配置
     */
    protected void addOption(K k, ImportFieldOption option) {
        Valid.notNull(option, "field option is null");
        ExcelReadType type = option.getType();
        if (type == null) {
            throw Exceptions.init("type must not be null");
        }
        // 检查是否为streaming支持的类型
        this.checkStreamingSupportType(type);
        options.put(k, option);
    }

    /**
     * 获取图片
     *
     * @param col 列
     * @param row 行
     * @return 图片
     */
    protected byte[] getPicture(int col, Row row) {
        if (pictureParser == null) {
            return null;
        }
        return Optional.ofNullable(pictureParser.getPicture(row.getRowNum(), col))
                .map(PictureData::getData)
                .orElse(null);
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

    public List<T> getRows() {
        return rows;
    }

    /**
     * @return 读取的行数
     */
    public int getRowNum() {
        return rowNum;
    }

    /**
     * @return 总行数
     */
    public int getLines() {
        return lines;
    }

}
