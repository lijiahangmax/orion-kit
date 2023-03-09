package com.orion.office.excel.writer.exporting;

import com.orion.lang.function.select.Selector;
import com.orion.office.excel.Excels;
import com.orion.office.excel.option.ExportFieldOption;
import com.orion.office.excel.option.ExportSheetOption;
import org.apache.poi.ss.usermodel.CellStyle;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * 列配置持有者
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/30 17:16
 */
public class SheetConfig<T> implements Serializable {

    /**
     * 2003版本 调色板自定义颜色索引(可能会覆盖预设颜色), 最大只能有 64-32个自定义颜色
     */
    protected short colorIndex;

    /**
     * sheet配置
     */
    protected ExportSheetOption sheetOption;

    /**
     * 列样式
     * key: index
     * value: style
     */
    protected Map<Integer, CellStyle> columnStyles;

    /**
     * 表头样式
     * key: index
     * value: style
     */
    protected Map<Integer, CellStyle> headerStyles;

    /**
     * 列样式选择器
     * <p>
     * 有选择到的将会覆盖原有样式
     */
    protected Map<Integer, Function<T, Selector<T, CellStyle>>> columnStyleSelector;

    /**
     * 列配置
     * key: index
     * value: option
     */
    protected Map<Integer, ExportFieldOption> fieldOptions;

    protected ExportInitializer<?> initializer;

    protected SheetConfig() {
        this.colorIndex = 32;
        this.sheetOption = new ExportSheetOption();
        this.columnStyles = new TreeMap<>();
        this.headerStyles = new TreeMap<>();
        this.columnStyleSelector = new TreeMap<>();
        this.fieldOptions = new TreeMap<>();
    }

    /**
     * 清空列样式 不包括已设置过的
     *
     * @param column 列
     * @return this
     */
    public SheetConfig<T> cleanStyle(int column) {
        initializer.checkInit();
        columnStyles.remove(column);
        headerStyles.remove(column);
        Integer rowWidth = sheetOption.getColumnWidth();
        if (rowWidth != null) {
            initializer.sheet.setColumnWidth(column, Excels.getWidth(rowWidth));
        }
        return this;
    }

    /**
     * 清空表头列样式 不包括已设置过的
     *
     * @param column 列
     * @return this
     */
    public SheetConfig<T> cleanHeaderStyle(int column) {
        initializer.checkInit();
        headerStyles.remove(column);
        return this;
    }

    /**
     * 清空数据列样式 不包括已设置过的
     *
     * @param column 列
     * @return this
     */
    public SheetConfig<T> cleanColumnStyle(int column) {
        initializer.checkInit();
        columnStyles.remove(column);
        return this;
    }

    /**
     * 表头使用数据列样式 不包括已设置过的
     *
     * @return this
     */
    public SheetConfig<T> headUseColumnStyle() {
        initializer.checkInit();
        fieldOptions.forEach((k, v) -> headerStyles.put(k, initializer.parseStyle(k, false, v)));
        return this;
    }

    /**
     * 表头使用数据列样式 不包括已设置过的
     *
     * @param column column
     * @return this
     */
    public SheetConfig<T> headUseColumnStyle(int column) {
        initializer.checkInit();
        headerStyles.put(column, initializer.parseStyle(column, false, fieldOptions.get(column)));
        return this;
    }

    /**
     * 数据使用表头列样式 不包括已设置过的
     *
     * @return this
     */
    public SheetConfig<T> columnUseHeadStyle() {
        initializer.checkInit();
        fieldOptions.forEach((k, v) -> columnStyles.put(k, initializer.parseStyle(k, true, v)));
        return this;
    }

    /**
     * 数据使用表头列样式 不包括已设置过的
     *
     * @param column column
     * @return this
     */
    public SheetConfig<T> columnUseHeadStyle(int column) {
        initializer.checkInit();
        columnStyles.put(column, initializer.parseStyle(column, true, fieldOptions.get(column)));
        return this;
    }

    /**
     * 设置列样式 不包括已设置过的
     *
     * @param column 列
     * @param style  样式
     * @return this
     */
    public SheetConfig<T> setStyle(int column, CellStyle style) {
        initializer.checkInit();
        headerStyles.put(column, style);
        columnStyles.put(column, style);
        return this;
    }

    /**
     * 设置表头样式 不包括已设置过的
     *
     * @param column 列
     * @param style  样式
     * @return this
     */
    public SheetConfig<T> setHeaderStyle(int column, CellStyle style) {
        initializer.checkInit();
        headerStyles.put(column, style);
        return this;
    }

    /**
     * 设置数据样式 不包括已设置过的
     *
     * @param column 列
     * @param style  样式
     * @return this
     */
    public SheetConfig<T> setColumnStyle(int column, CellStyle style) {
        initializer.checkInit();
        columnStyles.put(column, style);
        return this;
    }

    /**
     * 设置列样式 不包括已设置过的
     *
     * @param column 列
     * @param option 样式
     * @return this
     */
    public SheetConfig<T> setStyle(int column, ExportFieldOption option) {
        initializer.checkInit();
        if (option == null) {
            return this;
        }
        headerStyles.put(column, initializer.parseStyle(column, false, option));
        columnStyles.put(column, initializer.parseStyle(column, true, option));
        return this;
    }

    /**
     * 设置表头样式 不包括已设置过的
     *
     * @param column 列
     * @param option 样式
     * @return this
     */
    public SheetConfig<T> setHeaderStyle(int column, ExportFieldOption option) {
        initializer.checkInit();
        if (option == null) {
            return this;
        }
        headerStyles.put(column, initializer.parseStyle(column, false, option));
        return this;
    }

    /**
     * 设置数据样式 不包括已设置过的
     *
     * @param column 列
     * @param option 样式
     * @return this
     */
    public SheetConfig<T> setColumnStyle(int column, ExportFieldOption option) {
        initializer.checkInit();
        if (option == null) {
            return this;
        }
        columnStyles.put(column, initializer.parseStyle(column, true, option));
        return this;
    }

    /**
     * 设置数据样式选择器
     *
     * @param column   column
     * @param selector selector
     * @return this
     */
    public SheetConfig<T> setColumnStyle(int column, Function<T, Selector<T, CellStyle>> selector) {
        columnStyleSelector.put(column, selector);
        return this;
    }

    public ExportSheetOption getSheetOption() {
        initializer.checkInit();
        return sheetOption;
    }

    public Map<Integer, CellStyle> getColumnStyles() {
        initializer.checkInit();
        return columnStyles;
    }

    public Map<Integer, CellStyle> getHeaderStyles() {
        initializer.checkInit();
        return headerStyles;
    }

    public Map<Integer, Function<T, Selector<T, CellStyle>>> getColumnStyleSelector() {
        initializer.checkInit();
        return columnStyleSelector;
    }

    public Map<Integer, ExportFieldOption> getFieldOptions() {
        initializer.checkInit();
        return fieldOptions;
    }

}
