package com.orion.excel.exporting;

import com.orion.excel.annotation.ExportField;
import com.orion.excel.annotation.ExportFont;
import com.orion.excel.annotation.ExportSheet;
import com.orion.lang.wrapper.Arg;
import com.orion.utils.*;
import com.orion.utils.io.Files1;
import com.orion.utils.reflect.Fields;
import com.orion.utils.reflect.Methods;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel 导出器 仅支持注解
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/28 11:15
 */
public class ExcelExport<T> {

    private Workbook workbook;

    private Sheet sheet;

    /**
     * bean class
     */
    private Class<T> targetClass;

    /**
     * bean setter methods
     */
    private Map<Integer, Method> getMethods = new HashMap<>();

    /**
     * 行信息
     */
    private Map<Integer, Arg.Two<ExportFieldStyle, CellStyle>> rowStyles = new HashMap<>();

    /**
     * 表头信息
     */
    private Map<Integer, Arg.Two<ExportFieldStyle, CellStyle>> headerStyles = new HashMap<>();

    /**
     * sheetStyle
     */
    private ExportSheetStyle sheetStyle;

    /**
     * 添加头之后跳过的行数
     */
    private int afterSkip;

    /**
     * 表头
     */
    private String[] headers = new String[0];

    /**
     * 当前位置
     */
    private int rowIndex;

    /**
     * 列数
     */
    private int columnSize;

    /**
     * 数据
     */
    private List<T> rows;

    /**
     * 合并单元格
     */
    private List<CellRangeAddress> merges;

    /**
     * 2003版本 调色板自定义颜色索引(可能会覆盖预设颜色), 最大只能有 64-32个自定义颜色
     */
    private short colorIndex = 32;

    /**
     * 头使用行的样式
     */
    private boolean headUseRowStyle;

    /**
     * 是否跳过空行 (row == null)
     * 空行没有颜色及边框
     */
    private boolean skipNullRow = true;

    /**
     * 是否跳过表头
     */
    private boolean skipHeader;

    public ExcelExport(Class<T> targetClass) {
        this(targetClass, null, new SXSSFWorkbook());
    }

    public ExcelExport(Class<T> targetClass, List<T> rows) {
        this(targetClass, rows, new SXSSFWorkbook());
    }

    public ExcelExport(Class<T> targetClass, Workbook workbook) {
        this(targetClass, null, workbook);
    }

    public ExcelExport(Class<T> targetClass, List<T> rows, Workbook workbook) {
        Valid.notNull(targetClass, "TargetClass is null");
        Valid.notNull(workbook, "Workbook is null");
        this.targetClass = targetClass;
        this.rows = rows;
        this.workbook = workbook;
        analysisClass();
    }

    /**
     * 跳过一多 在表头之前之前
     *
     * @return this
     */
    public ExcelExport<T> skip() {
        rowIndex += 1;
        return this;
    }

    /**
     * 跳过行多 在表头之前之前
     *
     * @param i 行
     * @return this
     */
    public ExcelExport<T> skip(int i) {
        rowIndex += i;
        return this;
    }

    /**
     * 跳过一多 在表头之前之后
     *
     * @return this
     */
    public ExcelExport<T> skipAfter() {
        afterSkip += 1;
        return this;
    }

    /**
     * 跳过行多 在表头之前之后
     *
     * @param i 行
     * @return this
     */
    public ExcelExport<T> skipAfter(int i) {
        afterSkip += i;
        return this;
    }

    /**
     * 跳过空行
     *
     * @param skipNullRow true跳过
     * @return this
     */
    public ExcelExport<T> skipNullRow(boolean skipNullRow) {
        this.skipNullRow = skipNullRow;
        return this;
    }

    /**
     * 跳过表头
     *
     * @return this
     */
    public ExcelExport<T> skipHeader() {
        this.skipHeader = true;
        return this;
    }

    /**
     * 跳过表头
     *
     * @param skipHeader true跳过
     * @return this
     */
    public ExcelExport<T> skipHeader(boolean skipHeader) {
        this.skipHeader = skipHeader;
        return this;
    }

    /**
     * 设置表头
     *
     * @param headers 头
     * @return this
     */
    public ExcelExport<T> headers(String... headers) {
        if (headers == null || headers.length == 0) {
            this.headers = null;
        } else {
            this.headers = headers;
        }
        return this;
    }

    /**
     * 清空列样式
     *
     * @param row 列
     * @return this
     */
    public ExcelExport<T> cleanStyle(int row) {
        Arg.Two<ExportFieldStyle, CellStyle> rowStyle = rowStyles.get(row);
        Arg.Two<ExportFieldStyle, CellStyle> headerStyle = headerStyles.get(row);
        if (rowStyle != null) {
            rowStyles.put(row, Arg.two());
        }
        if (headerStyle != null) {
            headerStyles.put(row, Arg.two());
        }
        return this;
    }

    /**
     * 清空数据列样式
     *
     * @param row 列
     * @return this
     */
    public ExcelExport<T> cleanRowStyle(int row) {
        Arg.Two<ExportFieldStyle, CellStyle> rowStyle = rowStyles.get(row);
        if (rowStyle != null) {
            rowStyles.put(row, Arg.two());
        }
        return this;
    }

    /**
     * 清空表头列样式
     *
     * @param row 列
     * @return this
     */
    public ExcelExport<T> cleanHeaderStyle(int row) {
        Arg.Two<ExportFieldStyle, CellStyle> headerStyle = headerStyles.get(row);
        if (headerStyle != null) {
            headerStyles.put(row, Arg.two());
        }
        return this;
    }

    /**
     * 表头使用数据列样式
     *
     * @return this
     */
    public ExcelExport<T> headUseRowStyle() {
        headerStyles.putAll(rowStyles);
        return this;
    }

    /**
     * 表头使用数据列样式
     *
     * @param row row
     * @return this
     */
    public ExcelExport<T> headUseRowStyle(int row) {
        Arg.Two<ExportFieldStyle, CellStyle> style = rowStyles.get(row);
        if (style != null) {
            headerStyles.put(row, style);
        }
        return this;
    }

    /**
     * 数据使用表头列样式
     *
     * @return this
     */
    public ExcelExport<T> rowUseHeadStyle() {
        rowStyles.putAll(headerStyles);
        return this;
    }

    /**
     * 数据使用表头列样式
     *
     * @param row row
     * @return this
     */
    public ExcelExport<T> rowUseHeadStyle(int row) {
        Arg.Two<ExportFieldStyle, CellStyle> style = headerStyles.get(row);
        if (style != null) {
            rowStyles.put(row, style);
        }
        return this;
    }

    /**
     * 设置列样式
     *
     * @param row   列
     * @param style 样式
     * @return this
     */
    public ExcelExport<T> setStyle(int row, CellStyle style) {
        if (style == null) {
            return this;
        }
        Arg.Two<ExportFieldStyle, CellStyle> rowStyle = rowStyles.get(row);
        Arg.Two<ExportFieldStyle, CellStyle> headerStyle = headerStyles.get(row);
        if (rowStyle != null) {
            rowStyle.setArg2(style);
        }
        if (headerStyle != null) {
            headerStyle.setArg2(style);
        }
        return this;
    }

    /**
     * 设置表头样式
     *
     * @param row   列
     * @param style 样式
     * @return this
     */
    public ExcelExport<T> setHeaderStyle(int row, CellStyle style) {
        if (style == null) {
            return this;
        }
        Arg.Two<ExportFieldStyle, CellStyle> headerStyle = headerStyles.get(row);
        if (headerStyle != null) {
            headerStyle.setArg2(style);
        }
        return this;
    }

    /**
     * 设置数据样式
     *
     * @param row   列
     * @param style 样式
     * @return this
     */
    public ExcelExport<T> setRowStyle(int row, CellStyle style) {
        if (style == null) {
            return this;
        }
        Arg.Two<ExportFieldStyle, CellStyle> rowStyle = rowStyles.get(row);
        if (rowStyle != null) {
            rowStyle.setArg2(style);
        }
        return this;
    }

    /**
     * 设置列样式
     *
     * @param row   列
     * @param style 样式
     * @return this
     */
    public ExcelExport<T> setStyle(int row, ExportFieldStyle style) {
        if (style == null) {
            return this;
        }
        Arg.Two<ExportFieldStyle, CellStyle> rowStyle = rowStyles.get(row);
        Arg.Two<ExportFieldStyle, CellStyle> headerStyle = headerStyles.get(row);
        CellStyle cellStyle = this.parseStyle(style);
        if (rowStyle != null) {
            rowStyle.setArg1(style);
            rowStyle.setArg2(cellStyle);
        }
        if (headerStyle != null) {
            headerStyle.setArg1(style);
            headerStyle.setArg2(cellStyle);
        }
        return this;
    }

    /**
     * 设置表头样式
     *
     * @param row   列
     * @param style 样式
     * @return this
     */
    public ExcelExport<T> setHeaderStyle(int row, ExportFieldStyle style) {
        if (style == null) {
            return this;
        }
        Arg.Two<ExportFieldStyle, CellStyle> headerStyle = headerStyles.get(row);
        if (headerStyle != null) {
            headerStyle.setArg1(style);
            headerStyle.setArg2(this.parseStyle(style));
        }
        return this;
    }

    /**
     * 设置数据样式
     *
     * @param row   列
     * @param style 样式
     * @return this
     */
    public ExcelExport<T> setRowStyle(int row, ExportFieldStyle style) {
        if (style == null) {
            return this;
        }
        Arg.Two<ExportFieldStyle, CellStyle> rowStyle = rowStyles.get(row);
        if (rowStyle != null) {
            rowStyle.setArg1(style);
            rowStyle.setArg2(this.parseStyle(style));
        }
        return this;
    }

    /**
     * 设置数据
     *
     * @param rows rows
     * @return this
     */
    public ExcelExport<T> setRows(List<T> rows) {
        this.rows = rows;
        return this;
    }

    /**
     * 添加数据
     *
     * @param rows rows
     * @return this
     */
    public ExcelExport<T> addRows(List<T> rows) {
        if (this.rows == null) {
            this.rows = rows;
        } else {
            this.rows.addAll(rows);
        }
        return this;
    }

    /**
     * 合并单元格
     *
     * @param firstRow  合并开始行
     * @param lastRow   合并结束行
     * @param firstCell 合并开始单元格
     * @param lastCell  合并结束单元格
     * @return this
     */
    public ExcelExport<T> merge(int firstRow, int lastRow, int firstCell, int lastCell) {
        if (merges == null) {
            merges = new ArrayList<>();
        }
        merges.add(new CellRangeAddress(firstRow, lastRow, firstCell, lastCell));
        return this;
    }

    /**
     * 合并单元格
     *
     * @param row       合并行
     * @param firstCell 合并开始单元格
     * @param lastCell  合并结束单元格
     * @return this
     */
    public ExcelExport<T> merge(int row, int firstCell, int lastCell) {
        if (merges == null) {
            merges = new ArrayList<>();
        }
        merges.add(new CellRangeAddress(row, row, firstCell, lastCell));
        return this;
    }

    /**
     * 解析 class
     */
    private void analysisClass() {
        // 扫描class
        analysisSheet();
        // 注解field
        List<Field> fieldList = Fields.getFieldList(targetClass);
        // 注解method
        List<Method> methodList = Methods.getAllGetterMethod(targetClass);
        for (Field field : fieldList) {
            analysisAnnotatedHandler(field.getAnnotation(ExportField.class), field.getAnnotation(ExportFont.class), Methods.getGetterMethodByField(targetClass, field));
        }
        for (Method method : methodList) {
            analysisAnnotatedHandler(method.getAnnotation(ExportField.class), method.getAnnotation(ExportFont.class), method);
        }
        if (headUseRowStyle) {
            headerStyles.putAll(rowStyles);
        }
    }

    /**
     * 解析 sheet
     */
    private void analysisSheet() {
        ExportSheet sheet = targetClass.getAnnotation(ExportSheet.class);
        sheetStyle = new ExportSheetStyle();
        if (sheet != null) {
            String sheetName = sheet.value();
            if (!Strings.isEmpty(sheetName)) {
                sheetStyle.setName(sheetName);
            }
            if (sheet.headerUseRowStyle()) {
                headUseRowStyle = true;
                sheetStyle.setHeaderUseRowStyle(true);
            }
            int width = sheet.rowWidth();
            if (width != -1) {
                sheetStyle.setRowWidth(width);
            }
            int hh = sheet.headerHeight();
            if (hh != -1) {
                sheetStyle.setHeaderHeight(hh);
            }
            int rh = sheet.rowHeight();
            if (rh != -1) {
                sheetStyle.setRowHeight(rh);
            }
        }
    }

    /**
     * 注解处理
     *
     * @param f    ignore
     * @param font font
     * @param m    ignore
     */
    private void analysisAnnotatedHandler(ExportField f, ExportFont font, Method m) {
        if (f == null || m == null) {
            return;
        }
        int index = f.value();
        columnSize = Math.max(columnSize, index);
        getMethods.put(index, m);
        Arg.Two<ExportFieldStyle, CellStyle> arg = Arg.two();
        rowStyles.put(index, arg);
        ExportFieldStyle fieldStyle = analysisField(f);
        arg.setArg1(fieldStyle);
        if (font != null) {
            ExportFontStyle fontStyle = this.analysisFont(font);
            fieldStyle.setFontStyle(fontStyle);
        }
        this.parseStyle(index);
    }

    /**
     * 解析样式
     *
     * @param f annotation
     * @return ExportAnnotatedStyle
     */
    private ExportFieldStyle analysisField(ExportField f) {
        ExportFieldStyle style = new ExportFieldStyle();
        int align = f.align();
        if (align != -1) {
            style.setAlign(align);
        }
        int verticalAlign = f.verticalAlign();
        if (verticalAlign != -1) {
            style.setVerticalAlign(verticalAlign);
        }
        int width = f.width();
        if (width != -1) {
            style.setWidth(width);
        }
        String backgroundColor = f.backgroundColor();
        if (!Strings.isEmpty(backgroundColor)) {
            style.setBackgroundColor(backgroundColor);
        }
        if (f.wrapText()) {
            style.setWrapText(true);
        }
        int border = f.border();
        if (border != -1) {
            style.setBorder(border);
            String borderColor = f.borderColor();
            if (!Strings.isEmpty(borderColor)) {
                style.setBorderColor(borderColor);
            }
        }
        String datePattern = f.datePattern();
        if (!Strings.isEmpty(datePattern)) {
            style.setDatePattern(datePattern);
        }
        int index = f.value();
        String header = f.header();
        if (!Strings.isEmpty(header) && index != -1) {
            if (headers == null) {
                headers = new String[1];
            }
            if (index > headers.length - 1) {
                headers = Arrays1.resize(headers, index + 1);
            }
            headers[index] = header;
        }
        return style;
    }

    /**
     * 解析字体
     *
     * @param f annotation
     * @return ExportAnnotatedStyle
     */
    private ExportFontStyle analysisFont(ExportFont f) {
        ExportFontStyle style = new ExportFontStyle();
        if (f.bold()) {
            style.setBold(true);
        }
        if (f.italic()) {
            style.setItalic(true);
        }
        if (f.under()) {
            style.setUnder(true);
        }
        String name = f.fontName();
        if (!Strings.isEmpty(name)) {
            style.setFontName(name);
        }
        int s = f.fontSize();
        if (s != -1) {
            style.setFontSize(s);
        }
        String c = f.fontColor();
        if (!c.isEmpty()) {
            style.setFontColor(c);
        }
        return style;
    }

    /**
     * 解析样式
     *
     * @param info info
     * @return CellStyle
     */
    private CellStyle parseStyle(ExportFieldStyle info) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        if (info != null) {
            Integer align = info.getAlign();
            if (align != null) {
                style.setAlignment(HorizontalAlignment.forInt(align));
            }
            Integer verticalAlign = info.getVerticalAlign();
            if (verticalAlign != null) {
                style.setVerticalAlignment(VerticalAlignment.forInt(verticalAlign));
            }
            if (info.isWrapText()) {
                style.setWrapText(true);
            }
            String backgroundColor = info.getBackgroundColor();
            if (!Strings.isEmpty(backgroundColor)) {
                if (style instanceof XSSFCellStyle) {
                    ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(Colors.toRgb(backgroundColor), null));
                } else if (workbook instanceof HSSFWorkbook) {
                    style.setFillForegroundColor(this.paletteColor(backgroundColor));
                }
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            int border = info.getBorder();
            if (border != -1) {
                style.setBorderTop(BorderStyle.valueOf((short) border));
                style.setBorderLeft(BorderStyle.valueOf((short) border));
                style.setBorderBottom(BorderStyle.valueOf((short) border));
                style.setBorderRight(BorderStyle.valueOf((short) border));
                String borderColor = info.getBorderColor();
                if (!Strings.isEmpty(borderColor)) {
                    if (style instanceof XSSFCellStyle) {
                        XSSFColor bc = new XSSFColor(Colors.toRgb(borderColor), null);
                        ((XSSFCellStyle) style).setTopBorderColor(bc);
                        ((XSSFCellStyle) style).setLeftBorderColor(bc);
                        ((XSSFCellStyle) style).setBottomBorderColor(bc);
                        ((XSSFCellStyle) style).setRightBorderColor(bc);
                    } else if (workbook instanceof HSSFWorkbook) {
                        short bc = this.paletteColor(borderColor);
                        style.setTopBorderColor(bc);
                        style.setLeftBorderColor(bc);
                        style.setBottomBorderColor(bc);
                        style.setRightBorderColor(bc);
                    }
                }
            }
            ExportFontStyle fontStyle = info.getFontStyle();
            if (fontStyle != null) {
                String fontName = fontStyle.getFontName();
                if (fontName != null) {
                    font.setFontName(fontName);
                }
                Integer fontSize = fontStyle.getFontSize();
                if (fontSize != null) {
                    font.setFontHeightInPoints(fontSize.shortValue());
                }
                String fontColor = fontStyle.getFontColor();
                if (fontColor != null) {
                    if (font instanceof XSSFFont) {
                        ((XSSFFont) font).setColor(new XSSFColor(Colors.toRgb(fontColor), null));
                    } else if (font instanceof HSSFFont) {
                        font.setColor(this.paletteColor(fontColor));
                    }
                }
                if (fontStyle.isBold()) {
                    font.setBold(true);
                }
                if (fontStyle.isItalic()) {
                    font.setItalic(true);
                }
                if (fontStyle.isUnder()) {
                    font.setUnderline((byte) 1);
                }
            }
        }
        style.setFont(font);
        return style;
    }

    /**
     * 解析样式
     */
    private void parseStyle(int i) {
        Arg.Two<ExportFieldStyle, CellStyle> ms = rowStyles.get(i);
        if (ms != null) {
            ms.setArg2(this.parseStyle(ms.getArg1()));
        }
    }

    /**
     * HSSF 调色板
     *
     * @param c HexColor
     * @return colorIndex
     */
    private short paletteColor(String c) {
        HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
        byte[] rgb = Colors.toRgb(c);
        if (rgb != null) {
            HSSFColor color = palette.findColor(rgb[0], rgb[1], rgb[2]);
            if (color == null) {
                colorIndex++;
                palette.setColorAtIndex(colorIndex, rgb[0], rgb[1], rgb[2]);
                return colorIndex;
            } else {
                return color.getIndex();
            }
        }
        return 0;
    }

    /**
     * 执行导出
     *
     * @return this
     */
    public ExcelExport<T> execute() {
        if (sheetStyle.getName() != null) {
            sheet = workbook.createSheet(sheetStyle.getName());
        } else {
            sheet = workbook.createSheet();
        }
        // 默认行宽
        Integer rowWidth = sheetStyle.getRowWidth();
        if (rowWidth != null) {
            sheet.setDefaultColumnWidth((int) ((rowWidth + 0.72) * 256));
        }
        // 默认行高
        Integer rowHeight = sheetStyle.getRowHeight();
        if (rowHeight != null) {
            sheet.setDefaultRowHeight((short) (rowHeight * 20));
        }
        // field行宽
        rowStyles.forEach((k, v) -> {
            ExportFieldStyle style1 = v.getArg1();
            if (style1 != null) {
                Integer width = style1.getWidth();
                if (width != null) {
                    sheet.setColumnWidth(k, (int) ((width + 0.72) * 256));
                }
            }
        });
        // 表头
        if (!skipHeader && Arrays1.length(headers) != 0) {
            Row headRow = sheet.createRow(rowIndex++);
            Integer headerHeight = sheetStyle.getHeaderHeight();
            if (headerHeight != null) {
                headRow.setHeightInPoints(headerHeight.floatValue());
            }
            for (int i = 0; i < headers.length; i++) {
                Cell headCell = headRow.createCell(i);
                Arg.Two<ExportFieldStyle, CellStyle> headStyle = headerStyles.get(i);
                if (headStyle != null) {
                    headCell.setCellStyle(headStyle.getArg2());
                }
                headCell.setCellValue(Strings.def(headers[i]));
            }
        }
        // 表格
        rowIndex += afterSkip;
        for (T row : rows) {
            if (row == null) {
                if (!skipNullRow) {
                    rowIndex++;
                }
                continue;
            }
            Row rowRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < columnSize + 1; i++) {
                Arg.Two<ExportFieldStyle, CellStyle> thisRowStyle = rowStyles.get(i);
                if (thisRowStyle == null) {
                    continue;
                }
                Cell cell = rowRow.createCell(i);
                setCellValue(cell, i, row);
            }
        }
        if (merges != null) {
            merges.forEach(sheet::addMergedRegion);
        }
        return this;
    }

    /**
     * 设置cell的值
     *
     * @param cell  cell
     * @param index index
     * @param row   row
     */
    private void setCellValue(Cell cell, int index, T row) {
        Method method = getMethods.get(index);
        Arg.Two<ExportFieldStyle, CellStyle> styles = rowStyles.get(index);
        ExportFieldStyle style1 = styles.getArg1();
        CellStyle style2 = styles.getArg2();
        String datePattern = null;
        if (style1 != null) {
            datePattern = style1.getDatePattern();
        }
        if (row != null) {
            Object o = Methods.invokeMethod(row, method);
            String s;
            if (datePattern != null && o != null) {
                s = Dates.format(Dates.date(o), datePattern);
            } else {
                s = Objects1.toString(o);
            }
            cell.setCellValue(s);
        }
        if (style2 != null) {
            cell.setCellStyle(style2);
        }
    }

    /**
     * 写出到文件
     *
     * @param file 文件
     * @return this
     * @throws IOException IOException
     */
    public ExcelExport<T> write(String file) throws IOException {
        Files1.touch(file);
        return write(Files1.openOutputStream(file), true);
    }

    /**
     * 写出到文件
     *
     * @param file 文件
     * @return this
     * @throws IOException IOException
     */
    public ExcelExport<T> write(File file) throws IOException {
        Files1.touch(file);
        return write(Files1.openOutputStream(file), true);
    }

    /**
     * 写出到流
     *
     * @param out 流
     * @return this
     * @throws IOException IOException
     */
    public ExcelExport<T> write(OutputStream out) throws IOException {
        return write(out, false);
    }

    private ExcelExport<T> write(OutputStream out, boolean close) throws IOException {
        workbook.write(out);
        if (close) {
            Streams.closeQuietly(out);
        }
        return this;
    }

    /**
     * 关闭
     */
    public void close() {
        Streams.closeQuietly(workbook);
    }

    /**
     * 获取总行数
     *
     * @return 总行数
     */
    public int getLines() {
        return sheet.getLastRowNum() + 1;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public Class<T> getTargetClass() {
        return targetClass;
    }

    public List<T> getRows() {
        return rows;
    }

    /**
     * 获取一个单元格样式 用于样式修改
     *
     * @return 单元格样式
     */
    public CellStyle getCellStyle() {
        return workbook.createCellStyle();
    }

    /**
     * 获取一个字体 用于样式修改
     *
     * @return 字体
     */
    public Font getFont() {
        return workbook.createFont();
    }

}
