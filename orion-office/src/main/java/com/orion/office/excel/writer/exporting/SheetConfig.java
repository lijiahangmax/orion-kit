package com.orion.office.excel.writer.exporting;

import com.orion.office.excel.Excels;
import com.orion.office.excel.option.ExportFieldOption;
import com.orion.office.excel.option.ExportSheetOption;
import org.apache.poi.ss.usermodel.CellStyle;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * 列配置持有者
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/30 17:16
 */
public class SheetConfig implements Serializable {

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
        this.fieldOptions = new TreeMap<>();
    }

    protected void setInitializer(ExportInitializer<?> initializer) {
        this.initializer = initializer;
    }

    /**
     * 清空列样式 不包括已设置过的
     *
     * @param column 列
     * @return this
     */
    public SheetConfig cleanStyle(int column) {
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
    public SheetConfig cleanHeaderStyle(int column) {
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
    public SheetConfig cleanColumnStyle(int column) {
        initializer.checkInit();
        columnStyles.remove(column);
        return this;
    }

    /**
     * 表头使用数据列样式 不包括已设置过的
     *
     * @return this
     */
    public SheetConfig headUseColumnStyle() {
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
    public SheetConfig headUseColumnStyle(int column) {
        initializer.checkInit();
        headerStyles.put(column, initializer.parseStyle(column, false, fieldOptions.get(column)));
        return this;
    }

    /**
     * 数据使用表头列样式 不包括已设置过的
     *
     * @return this
     */
    public SheetConfig columnUseHeadStyle() {
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
    public SheetConfig columnUseHeadStyle(int column) {
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
    public SheetConfig setStyle(int column, CellStyle style) {
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
    public SheetConfig setHeaderStyle(int column, CellStyle style) {
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
    public SheetConfig setColumnStyle(int column, CellStyle style) {
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
    public SheetConfig setStyle(int column, ExportFieldOption option) {
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
    public SheetConfig setHeaderStyle(int column, ExportFieldOption option) {
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
    public SheetConfig setColumnStyle(int column, ExportFieldOption option) {
        initializer.checkInit();
        if (option == null) {
            return this;
        }
        columnStyles.put(column, initializer.parseStyle(column, true, option));
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

    public Map<Integer, ExportFieldOption> getFieldOptions() {
        initializer.checkInit();
        return fieldOptions;
    }

}
