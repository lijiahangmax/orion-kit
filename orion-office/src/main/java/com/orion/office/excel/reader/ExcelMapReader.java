package com.orion.office.excel.reader;

import com.orion.lang.collect.MutableHashMap;
import com.orion.lang.collect.MutableLinkedHashMap;
import com.orion.lang.collect.MutableMap;
import com.orion.office.excel.Excels;
import com.orion.office.excel.option.CellOption;
import com.orion.office.excel.option.ImportFieldOption;
import com.orion.office.excel.picture.PictureParser;
import com.orion.office.excel.type.ExcelReadType;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import org.apache.poi.ss.usermodel.*;

import java.util.*;
import java.util.function.Consumer;

/**
 * Excel Map 读取器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/5 11:52
 */
public class ExcelMapReader<K, V> extends BaseExcelReader<MutableMap<K, V>> {

    /**
     * 配置信息
     * key: mapKey
     * value: 配置
     */
    private Map<K, ImportFieldOption> options = new HashMap<>();

    /**
     * 默认值
     * key: 列
     * value: 默认值
     */
    private Map<K, V> defaultValue = new HashMap<>();

    /**
     * 图片解析器
     */
    private PictureParser pictureParser;

    /**
     * 为null是否插入kay
     */
    private boolean nullPutKey = true;

    /**
     * 是否使用 linkedMap
     */
    private boolean linked;

    public ExcelMapReader(Workbook workbook, Sheet sheet) {
        this(workbook, sheet, new ArrayList<>(), null);
    }

    public ExcelMapReader(Workbook workbook, Sheet sheet, List<MutableMap<K, V>> rows) {
        this(workbook, sheet, rows, null);
    }

    public ExcelMapReader(Workbook workbook, Sheet sheet, Consumer<MutableMap<K, V>> consumer) {
        this(workbook, sheet, null, consumer);
    }

    private ExcelMapReader(Workbook workbook, Sheet sheet, List<MutableMap<K, V>> rows, Consumer<MutableMap<K, V>> consumer) {
        super(workbook, sheet, rows, consumer);
        this.init = false;
    }

    /**
     * 添加配置
     *
     * @param key    key
     * @param option 配置
     * @return this
     */
    public ExcelMapReader<K, V> option(K key, ImportFieldOption option) {
        this.addOption(key, option);
        return this;
    }

    /**
     * 添加配置
     *
     * @param key    key
     * @param column 列
     * @param type   类型
     * @return this
     */
    public ExcelMapReader<K, V> option(K key, int column, ExcelReadType type) {
        this.addOption(key, new ImportFieldOption(column, type));
        return this;
    }

    /**
     * 添加配置
     *
     * @param key    key
     * @param option 配置
     */
    protected void addOption(K key, ImportFieldOption option) {
        Valid.notNull(option, "field option is null");
        ExcelReadType type = option.getType();
        if (option.getIndex() == 0 && type == null) {
            throw Exceptions.init("the index and type must not be null");
        }
        if (!Strings.isEmpty(option.getParseFormat())) {
            option.setCellOption(new CellOption(option.getParseFormat()));
        }
        // 检查类型
        if (streaming && (type.equals(ExcelReadType.LINK_ADDRESS) ||
                type.equals(ExcelReadType.COMMENT) ||
                type.equals(ExcelReadType.PICTURE))) {
            throw Exceptions.parse("streaming just support read value");
        }
        options.put(key, option);
    }

    /**
     * 使用 linkedMap
     *
     * @return this
     */
    public ExcelMapReader<K, V> linked() {
        this.linked = true;
        this.options = new LinkedHashMap<>(this.options);
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

    /**
     * 设置默认值
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return this
     */
    public ExcelMapReader<K, V> defaultValue(K key, V defaultValue) {
        this.defaultValue.put(key, defaultValue);
        return this;
    }

    @Override
    public ExcelMapReader<K, V> init() {
        this.init = true;
        boolean havePicture = options.values().stream()
                .map(ImportFieldOption::getType)
                .anyMatch(ExcelReadType.PICTURE::equals);
        if (havePicture) {
            pictureParser = new PictureParser(workbook, sheet);
            pictureParser.analysis();
        }
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
                    value = this.getPicture(column, row);
                } else {
                    value = Excels.getCellValue(cell, type, option.getCellOption());
                }
            }
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
                if (trim && value instanceof String) {
                    value = ((String) value).trim();
                }
                map.put(key, (V) value);
            }
        });
        return map;
    }

    /**
     * 获取图片
     *
     * @param col 列
     * @param row 行
     * @return 图片
     */
    private byte[] getPicture(int col, Row row) {
        if (pictureParser == null) {
            return null;
        }
        return Optional.ofNullable(pictureParser.getPicture(row.getRowNum(), col))
                .map(PictureData::getData)
                .orElse(null);
    }

}
