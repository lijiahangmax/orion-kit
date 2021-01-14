package com.orion.excel.writer.exporting;

import com.orion.excel.Excels;
import com.orion.excel.option.*;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Objects1;
import com.orion.utils.Strings;
import com.orion.utils.codec.Base64s;
import com.orion.utils.collect.Sets;
import com.orion.utils.io.Streams;
import com.orion.utils.reflect.Methods;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Export 处理器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/12/31 11:21
 */
public class ExportProcessor<T> {

    private Workbook workbook;

    private Sheet sheet;

    private ExportSheetOption exportSheetOption;

    private Map<Integer, ExportFieldOption> fieldOptions;

    /**
     * 列样式
     */
    protected Map<Integer, CellStyle> columnStyles = new TreeMap<>();

    /**
     * 表头样式
     */
    protected Map<Integer, CellStyle> headerStyles = new TreeMap<>();

    /**
     * 当前位置
     */
    protected int rowIndex;

    /**
     * 2003版本 调色板自定义颜色索引(可能会覆盖预设颜色), 最大只能有 64-32个自定义颜色
     */
    private short colorIndex = 32;

    protected ExportProcessor(Workbook workbook, Sheet sheet, ExportSheetOption exportSheetOption, Map<Integer, ExportFieldOption> fieldOptions) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.exportSheetOption = exportSheetOption;
        this.fieldOptions = fieldOptions;
        this.setup();
    }

    /**
     * 初始化
     */
    protected void init() {
        // 表格
        this.setSheetOption();
        // 页眉
        this.setPageHeader();
        // 页脚
        this.setPageFooter();
        // 注解标题
        this.addTitle();
        // 注解表头
        this.addDefaultHeader();
        // 注解下拉框
        this.addRowSelectOptions();
        // 打印
        this.addPrintSetup();
    }

    /**
     * 预初始化
     */
    private void setup() {
        // sheet
        if (exportSheetOption.getName() != null) {
            if (this.sheet == null) {
                this.sheet = workbook.createSheet(exportSheetOption.getName());
            } else {
                workbook.setSheetName(workbook.getSheetIndex(this.sheet), exportSheetOption.getName());
            }
        } else if (this.sheet == null) {
            this.sheet = workbook.createSheet();
        }
        // 样式
        this.addColumnStyle();
    }

    /**
     * 设置sheet
     */
    private void setSheetOption() {
        if (exportSheetOption.isNameReset()) {
            // 如果修改了sheet名称需要
            workbook.setSheetName(workbook.getSheetIndex(this.sheet), exportSheetOption.getName());
        }
        // 默认行高
        Integer defaultRowHeight = exportSheetOption.getRowHeight();
        if (defaultRowHeight != null) {
            this.sheet.setDefaultRowHeightInPoints(defaultRowHeight);
        }
        // 默认行宽
        Integer defaultRowWidth = exportSheetOption.getColumnWidth();
        if (defaultRowWidth != null) {
            // 默认无需用 (x + 0.72) * 256 接近但不准确
            this.sheet.setDefaultColumnWidth(defaultRowWidth);
        }
        // 缩放
        if (exportSheetOption.getZoom() != null) {
            this.sheet.setZoom(exportSheetOption.getZoom());
        }
        // 选中
        if (exportSheetOption.isSelected()) {
            this.workbook.setActiveSheet(this.workbook.getSheetIndex(this.sheet));
        }
        // 隐藏
        if (exportSheetOption.isHidden()) {
            this.workbook.setSheetHidden(workbook.getSheetIndex(this.sheet), true);
        }
    }

    /**
     * 设置页眉
     */
    private void setPageHeader() {
        HeaderOption headerOption = exportSheetOption.getHeaderOption();
        if (headerOption != null) {
            Excels.setHeader(sheet, headerOption);
        }
    }

    /**
     * 设置页脚
     */
    private void setPageFooter() {
        FooterOption footerOption = exportSheetOption.getFooterOption();
        if (footerOption != null) {
            Excels.setFooter(sheet, footerOption);
        }
    }

    /**
     * 添加列样式
     */
    private void addColumnStyle() {
        fieldOptions.forEach((k, v) -> {
            CellStyle columnStyle = this.parseStyle(k, true, v);
            columnStyles.put(k, columnStyle);
            if (exportSheetOption.isHeaderUseColumnStyle()) {
                CellStyle headerStyle = this.parseStyle(k, false, v);
                headerStyles.put(k, headerStyle);
            }
            if (v.isHidden()) {
                sheet.setColumnHidden(k, true);
            }
        });
    }

    /**
     * 添加标题
     */
    private void addTitle() {
        TitleOption titleOption = exportSheetOption.getTitleOption();
        if (exportSheetOption.isSkipTitle() || titleOption == null) {
            return;
        }
        if (exportSheetOption.getTitle() != null) {
            titleOption.setTitle(exportSheetOption.getTitle());
        }
        titleOption.setPaletteColorIndex(this.colorIndex);
        CellStyle titleStyle = Objects1.def(Excels.parseTitleStyle(workbook, titleOption), workbook.createCellStyle());
        this.colorIndex = titleOption.getPaletteColorIndex();
        FontOption fontOption = titleOption.getFont();
        fontOption.setPaletteColorIndex(this.colorIndex);
        Font font = Objects1.def(Excels.parseFont(workbook, fontOption), workbook.createFont());
        titleStyle.setFont(font);
        this.colorIndex = fontOption.getPaletteColorIndex();
        Row titleRow = sheet.createRow(rowIndex);
        // 防止多个row高度不一致
        for (int i = 1; i < titleOption.getUseRow(); i++) {
            Row temp = sheet.createRow(rowIndex + i);
            if (exportSheetOption.getTitleHeight() != null) {
                temp.setHeightInPoints(exportSheetOption.getTitleHeight());
            }
        }
        if (exportSheetOption.getTitleHeight() != null) {
            titleRow.setHeightInPoints(exportSheetOption.getTitleHeight());
        }
        if (titleOption.getUseColumn() == -1) {
            titleOption.setUseColumn(exportSheetOption.getColumnSize());
        }
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(titleStyle);
        titleCell.setCellValue(titleOption.getTitle());
        CellRangeAddress region = new CellRangeAddress(rowIndex, rowIndex + titleOption.getUseRow() - 1, 0, titleOption.getUseColumn());
        Excels.mergeCell(sheet, region);
        Excels.mergeCellBorder(sheet, titleStyle.getBorderTop().getCode(), titleStyle.getTopBorderColor(), region);
        rowIndex += titleOption.getUseRow();
        exportSheetOption.setAddTitle(true);
    }

    /**
     * 添加默认表头
     */
    private void addDefaultHeader() {
        fieldOptions.forEach((k, v) -> {
            if (v.isSkipHeaderStyle()) {
                headerStyles.remove(k);
            }
        });
        if (exportSheetOption.isSkipFieldHeader()) {
            return;
        }
        Integer size = Sets.max(fieldOptions.keySet());
        String[] h = new String[size + 1];
        fieldOptions.forEach((k, v) -> {
            h[k] = v.getHeader();
        });
        if (Strings.isAllEmpty(h)) {
            return;
        }
        this.exportSheetOption.setAddDefaultHeader(true);
        this.headers(true, h);
        // 冻结首行
        if (exportSheetOption.isFreezeHeader()) {
            Excels.freezeRow(sheet, rowIndex);
        }
        // 筛选首行
        if (exportSheetOption.isFilterHeader()) {
            Excels.filterRow(sheet, rowIndex - 1, 0, exportSheetOption.getColumnSize());
        }
    }

    /**
     * 添加列下拉框
     */
    private void addRowSelectOptions() {
        if (this.exportSheetOption.isSkipSelectOption()) {
            return;
        }
        fieldOptions.forEach((k, v) -> {
            String[] options = v.getSelectOptions();
            if (Arrays1.isEmpty(options)) {
                return;
            }
            Excels.addSelectOptions(sheet, this.exportSheetOption.isAddDefaultHeader() ? 1 : 0, k, options);
        });
    }

    /**
     * 添加打印参数
     */
    private void addPrintSetup() {
        PrintOption printOption = exportSheetOption.getPrintOption();
        if (printOption == null) {
            return;
        }
        Excels.parsePrint(sheet, printOption);
    }

    /**
     * 设置表头
     *
     * @param headers 头
     */
    protected void headers(boolean isDefault, String... headers) {
        if (Arrays1.isEmpty(headers)) {
            return;
        }
        Row headerRow = sheet.createRow(rowIndex);
        Integer headerHeight = exportSheetOption.getHeaderHeight();
        if (headerHeight != null) {
            headerRow.setHeightInPoints(headerHeight.floatValue());
        }
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            CellStyle headStyle = headerStyles.get(i);
            if (headStyle != null) {
                headerCell.setCellStyle(headStyle);
            }
            // 批注
            if (isDefault) {
                CommentOption commentOption = Optional.ofNullable(fieldOptions.get(i))
                        .map(ExportFieldOption::getCommentOption).orElse(null);
                Comment comment = Excels.createComment(sheet, i, rowIndex, commentOption);
                if (comment != null) {
                    headerCell.setCellComment(comment);
                }
            }
            headerCell.setCellValue(Strings.def(headers[i]));
        }
        rowIndex++;
    }

    /**
     * 设置cell的值
     *
     * @param cell        cell
     * @param rowIndex    rowIndex
     * @param columnIndex columnIndex
     * @param row         row
     */
    protected void setCellValue(Cell cell, int rowIndex, int columnIndex, T row, ExportFieldOption option) {
        CellStyle style = columnStyles.get(columnIndex);
        if (style != null) {
            cell.setCellStyle(style);
        }
        if (row == null) {
            return;
        }
        boolean setValue = true;
        // 图片
        if (option.getPictureOption() != null) {
            this.setPicture(cell, row, rowIndex, columnIndex, option);
            setValue = false;
        }
        // 链接
        if (option.getLinkOption() != null) {
            this.setLink(cell, row, option);
            setValue = false;
        }
        if (setValue) {
            // 普通
            Object o = Methods.invokeMethod(row, option.getGetterMethod());
            if (o instanceof String && option.isTrim()) {
                o = ((String) o).trim();
            }
            Excels.setCellValue(cell, o, option.getType(), option.getCellOption());
        }
    }

    /**
     * 设置图片
     *
     * @param cell   cell
     * @param row    row
     * @param option option
     */
    private void setPicture(Cell cell, T row, int rowIndex, int columnIndex, ExportFieldOption option) {
        Method getterMethod = option.getGetterMethod();
        PictureOption pictureOption = option.getPictureOption();
        // 图片
        Method pictureGetterMethod = pictureOption.isOriginImage() ? getterMethod : pictureOption.getImageGetter();
        Object pictureValue = Methods.invokeMethod(row, pictureGetterMethod);
        if (pictureValue != null) {
            try {
                Picture picture = null;
                if (pictureValue instanceof InputStream) {
                    picture = Excels.setPicture(workbook, sheet, (InputStream) pictureValue, rowIndex, columnIndex, pictureOption.getType());
                } else if (pictureValue instanceof byte[]) {
                    picture = Excels.setPicture(workbook, sheet, (byte[]) pictureValue, rowIndex, columnIndex, pictureOption.getType());
                } else if (pictureValue instanceof String && pictureOption.isBase64()) {
                    String type = Base64s.image64Type((String) pictureValue);
                    byte[] bytes = Base64s.img64Decode((String) pictureValue);
                    picture = Excels.setPicture(workbook, sheet, bytes, rowIndex, columnIndex, type, pictureOption.getType());
                }
                if (picture != null) {
                    picture.resize(pictureOption.getScaleX(), pictureOption.getScaleY());
                }
            } catch (Exception e) {
                if (!exportSheetOption.isSkipPictureException()) {
                    throw Exceptions.unchecked(e);
                }
            } finally {
                if (pictureOption.isAutoClose() && pictureValue instanceof InputStream) {
                    Streams.close((InputStream) pictureValue);
                }
            }
        }
        // 文本
        Object textValue = null;
        if (pictureOption.isNoneText()) {
            return;
        } else if (pictureOption.isOriginText()) {
            textValue = Methods.invokeMethod(row, getterMethod);
        } else if (pictureOption.getTextValue() != null) {
            textValue = pictureOption.getTextValue();
        } else if (pictureOption.getTextGetter() != null) {
            textValue = Methods.invokeMethod(row, pictureOption.getTextGetter());
        }
        Excels.setCellValue(cell, textValue, pictureOption.getTextType(), pictureOption.getCellOption());
    }

    /**
     * 设置超链接
     *
     * @param cell   cell
     * @param row    row
     * @param option option
     */
    private void setLink(Cell cell, T row, ExportFieldOption option) {
        Method getterMethod = option.getGetterMethod();
        LinkOption linkOption = option.getLinkOption();
        String address = null;
        Object textValue = null;
        Object methodValue = null;
        // 链接
        if (linkOption.isOriginLink()) {
            methodValue = Methods.invokeMethod(row, getterMethod);
            address = Objects1.toString(methodValue);
        } else if (linkOption.getLinkValue() != null) {
            address = linkOption.getLinkValue();
        } else if (linkOption.getLinkGetterMethod() != null) {
            address = Objects1.toString(Methods.invokeMethod(row, linkOption.getLinkGetterMethod()));
        }
        // 文本
        if (linkOption.isOriginText()) {
            if (methodValue == null) {
                methodValue = Methods.invokeMethod(row, getterMethod);
            }
            textValue = methodValue;
        } else if (linkOption.getTextValue() != null) {
            textValue = linkOption.getTextValue();
        } else if (linkOption.getTextGetterMethod() != null) {
            textValue = Objects1.toString(Methods.invokeMethod(row, linkOption.getTextGetterMethod()));
        }
        // 设置链接
        Excels.setLink(workbook, cell, linkOption.getType(), address, linkOption.getTextType(), linkOption.getCellOption(), textValue);
    }

    /**
     * 解析样式
     *
     * @param columnIndex 列
     * @param isColumn    是否为列样式
     * @param option      option
     * @return CellStyle
     */
    protected CellStyle parseStyle(int columnIndex, boolean isColumn, ExportFieldOption option) {
        // 样式
        option.setPaletteColorIndex(this.colorIndex);
        CellStyle style;
        if (isColumn) {
            style = Objects1.def(Excels.parseColumnStyle(workbook, option), workbook.createCellStyle());
        } else {
            style = Objects1.def(Excels.parseStyle(workbook, option), workbook.createCellStyle());
        }
        this.colorIndex = option.getPaletteColorIndex();
        // 字体
        Font font;
        if (option.getFontOption() != null) {
            option.getFontOption().setPaletteColorIndex(this.colorIndex);
            font = Objects1.def(Excels.parseFont(workbook, option.getFontOption()), workbook.createFont());
            this.colorIndex = option.getFontOption().getPaletteColorIndex();
        } else {
            font = workbook.createFont();
        }
        style.setFont(font);
        // sheet 宽
        Integer width = option.getWidth();
        if (width != null) {
            // 行宽
            sheet.setColumnWidth(columnIndex, (int) ((width + 0.72) * 256));
        }
        return style;
    }

    protected Sheet getSheet() {
        return sheet;
    }

}
