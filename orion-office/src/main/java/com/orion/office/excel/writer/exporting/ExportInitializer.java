package com.orion.office.excel.writer.exporting;

import com.orion.office.excel.Excels;
import com.orion.office.excel.option.*;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Objects1;
import com.orion.utils.Strings;
import com.orion.utils.collect.Sets;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Optional;

/**
 * Export 初始化器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/31 17:25
 */
public class ExportInitializer<T> {

    protected Workbook workbook;

    protected Sheet sheet;

    private Class<T> targetClass;

    private SheetConfig sheetConfig;

    /**
     * 当前行索引
     */
    protected int rowIndex;

    /**
     * 是否初始化
     */
    private boolean init;

    protected ExportInitializer(Workbook workbook, Sheet sheet, Class<T> targetClass, SheetConfig sheetConfig) {
        this.workbook = workbook;
        this.targetClass = targetClass;
        this.sheetConfig = sheetConfig;
        this.sheet = sheet;
        this.setup();
    }

    /**
     * 预初始化
     */
    private void setup() {
        // 初始化sheet
        if (sheet == null) {
            if (sheetConfig.sheetOption.getName() != null) {
                this.sheet = workbook.createSheet(sheetConfig.sheetOption.getName());
            } else {
                this.sheet = workbook.createSheet();
            }
        } else if (sheetConfig.sheetOption.getName() != null) {
            // 改名
            workbook.setSheetName(workbook.getSheetIndex(sheet), sheetConfig.sheetOption.getName());
        }
        // 解析sheet
        SheetAnalysis sheetAnalysis = new SheetAnalysis(targetClass, sheetConfig);
        sheetAnalysis.analysis();
        // 解析列
        SheetColumnAnalysis sheetColumnAnalysis = new SheetColumnAnalysis(targetClass, sheetConfig);
        sheetColumnAnalysis.analysis();
        // 样式
        this.addColumnStyle();
    }

    /**
     * 检查是否初始化
     */
    protected void checkInit() {
        if (!init) {
            throw Exceptions.init("excel export uninitialized");
        }
    }

    /**
     * 初始化
     */
    protected void init() {
        this.init = true;
        // 表格
        this.setSheetOption();
        // 页眉
        this.setPageHeader();
        // 页脚
        this.setPageFooter();
        // 标题
        this.addTitle();
        // 表头
        this.addDefaultHeader();
        // 打印
        this.addPrintSetup();
    }

    /**
     * 完成后回调
     */
    protected void finished() {
        // 下拉选择框
        this.addRowSelectOptions();
    }

    /**
     * 设置sheet
     */
    private void setSheetOption() {
        if (sheetConfig.sheetOption.isNameReset()) {
            // 改名
            workbook.setSheetName(workbook.getSheetIndex(sheet), sheetConfig.sheetOption.getName());
        }
        // 默认行高
        Integer defaultRowHeight = sheetConfig.sheetOption.getRowHeight();
        if (defaultRowHeight != null) {
            sheet.setDefaultRowHeightInPoints(defaultRowHeight);
        }
        // 默认行宽
        Integer defaultRowWidth = sheetConfig.sheetOption.getColumnWidth();
        if (defaultRowWidth != null) {
            // 默认无需用 (x + 0.72) * 256 接近但不准确
            sheet.setDefaultColumnWidth(defaultRowWidth);
        }
        // 缩放
        if (sheetConfig.sheetOption.getZoom() != null) {
            sheet.setZoom(sheetConfig.sheetOption.getZoom());
        }
        // 选中
        if (sheetConfig.sheetOption.isSelected()) {
            workbook.setActiveSheet(workbook.getSheetIndex(sheet));
        }
        // 隐藏
        if (sheetConfig.sheetOption.isHidden()) {
            workbook.setSheetHidden(workbook.getSheetIndex(sheet), true);
        }
        // 不显示网格线
        if (sheetConfig.sheetOption.isDisplayGridLines()) {
            sheet.setDisplayGridlines(false);
        }
        // 不显示行数和列数
        if (sheetConfig.sheetOption.isDisplayRowColHeadings()) {
            sheet.setDisplayRowColHeadings(false);
        }
        // 不执行公式
        if (sheetConfig.sheetOption.isDisplayFormulas()) {
            sheet.setDisplayFormulas(true);
        }
    }

    /**
     * 设置页眉
     */
    private void setPageHeader() {
        HeaderOption headerOption = sheetConfig.sheetOption.getHeaderOption();
        if (headerOption != null) {
            Excels.setHeader(sheet, headerOption);
        }
    }

    /**
     * 设置页脚
     */
    private void setPageFooter() {
        FooterOption footerOption = sheetConfig.sheetOption.getFooterOption();
        if (footerOption != null) {
            Excels.setFooter(sheet, footerOption);
        }
    }

    /**
     * 添加列样式
     */
    private void addColumnStyle() {
        sheetConfig.fieldOptions.forEach((k, v) -> {
            // 列样式
            CellStyle columnStyle = this.parseStyle(k, true, v);
            sheetConfig.columnStyles.put(k, columnStyle);
            // 是否设置为默认样式
            if (sheetConfig.sheetOption.isColumnUseDefaultStyle()) {
                sheet.setDefaultColumnStyle(k, columnStyle);
            }
            // 表头样式
            if (sheetConfig.sheetOption.isHeaderUseColumnStyle() && !v.isSkipHeaderStyle()) {
                // 如果表头复用列样式 设置表头样式
                CellStyle headerStyle = this.parseStyle(k, false, v);
                sheetConfig.headerStyles.put(k, headerStyle);
            } else {
                // 默认样式
                sheetConfig.headerStyles.put(k, workbook.createCellStyle());
            }
            // 设置隐藏列
            if (v.isHidden()) {
                sheet.setColumnHidden(k, true);
            }
        });
    }

    /**
     * 添加标题
     */
    private void addTitle() {
        TitleOption titleOption = sheetConfig.sheetOption.getTitleOption();
        // 是否跳过标题
        if (sheetConfig.sheetOption.isSkipTitle() || titleOption == null) {
            return;
        }
        if (sheetConfig.sheetOption.getTitle() != null) {
            titleOption.setTitle(sheetConfig.sheetOption.getTitle());
        }
        // 标题样式
        titleOption.setPaletteColorIndex(sheetConfig.colorIndex);
        CellStyle titleStyle = Objects1.def(Excels.parseTitleStyle(workbook, titleOption), workbook::createCellStyle);
        sheetConfig.colorIndex = titleOption.getPaletteColorIndex();
        // 标题字体
        FontOption fontOption = titleOption.getFont();
        fontOption.setPaletteColorIndex(sheetConfig.colorIndex);
        Font font = Objects1.def(Excels.parseFont(workbook, fontOption), workbook::createFont);
        titleStyle.setFont(font);
        sheetConfig.colorIndex = fontOption.getPaletteColorIndex();
        // 标题row
        Row titleRow = sheet.createRow(rowIndex);
        int useRow = titleOption.getUseRow();
        for (int i = 1; i < useRow; i++) {
            // 如果标题使用多行row 创建临时row
            Row temp = sheet.createRow(rowIndex + i);
            // 标题行高
            if (sheetConfig.sheetOption.getTitleHeight() != null) {
                temp.setHeightInPoints(sheetConfig.sheetOption.getTitleHeight());
            }
        }
        // 标题行高
        if (sheetConfig.sheetOption.getTitleHeight() != null) {
            titleRow.setHeightInPoints(sheetConfig.sheetOption.getTitleHeight());
        }
        // 标题使用列数
        if (titleOption.getUseColumn() == -1) {
            titleOption.setUseColumn(sheetConfig.sheetOption.getColumnMaxIndex());
        }
        // 标题cell
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(titleStyle);
        titleCell.setCellValue(titleOption.getTitle());
        // 合并单元格以及边框
        CellRangeAddress region = new CellRangeAddress(rowIndex, rowIndex + useRow - 1, 0, titleOption.getUseColumn());
        Excels.mergeCell(sheet, region);
        Excels.mergeCellBorder(sheet, titleStyle.getBorderTop().getCode(), titleStyle.getTopBorderColor(), region);
        this.rowIndex += useRow;
        sheetConfig.sheetOption.setTitleAndHeaderLastRowIndex(rowIndex);
    }

    /**
     * 添加默认表头
     */
    private void addDefaultHeader() {
        // 是否跳过表头
        if (sheetConfig.sheetOption.isSkipFieldHeader()) {
            return;
        }
        // 初始化表头数据
        Integer size = Sets.max(sheetConfig.fieldOptions.keySet());
        String[] header = new String[size + 1];
        sheetConfig.fieldOptions.forEach((k, v) -> {
            header[k] = v.getHeader();
        });
        if (Strings.isAllEmpty(header)) {
            return;
        }
        // 添加表头
        this.headers(true, header);
        sheetConfig.sheetOption.setTitleAndHeaderLastRowIndex(rowIndex);
        // 冻结首行
        if (sheetConfig.sheetOption.isFreezeHeader()) {
            Excels.freezeRow(sheet, rowIndex);
        }
        // 筛选首行
        if (sheetConfig.sheetOption.isFilterHeader()) {
            Excels.filterRow(sheet, rowIndex - 1, 0, sheetConfig.sheetOption.getColumnMaxIndex());
        }
    }

    /**
     * 添加列下拉选择框
     */
    private void addRowSelectOptions() {
        // 是否跳过下拉选择框
        if (sheetConfig.sheetOption.isSkipSelectOption()) {
            return;
        }
        sheetConfig.fieldOptions.forEach((k, v) -> {
            String[] options = v.getSelectOptions();
            if (Arrays1.isEmpty(options)) {
                return;
            }
            // 添加下拉框
            Excels.addSelectOptions(sheet, sheetConfig.sheetOption.getTitleAndHeaderLastRowIndex(), rowIndex - 1, k, options);
        });
    }

    /**
     * 添加打印参数
     */
    private void addPrintSetup() {
        PrintOption printOption = sheetConfig.sheetOption.getPrintOption();
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
        // 创建行
        Row headerRow = sheet.createRow(rowIndex);
        Integer headerHeight = sheetConfig.sheetOption.getHeaderHeight();
        // 行高
        if (headerHeight != null) {
            headerRow.setHeightInPoints(headerHeight.floatValue());
        }
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            CellStyle headStyle = sheetConfig.headerStyles.get(i);
            if (headStyle != null) {
                headerCell.setCellStyle(headStyle);
            }
            // 批注
            if (isDefault) {
                CommentOption commentOption = Optional.ofNullable(sheetConfig.fieldOptions.get(i))
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
     * 解析样式
     *
     * @param columnIndex 列
     * @param isColumn    是否为列样式
     * @param option      option
     * @return CellStyle
     */
    protected CellStyle parseStyle(int columnIndex, boolean isColumn, ExportFieldOption option) {
        // 样式
        option.setPaletteColorIndex(sheetConfig.colorIndex);
        CellStyle style;
        if (isColumn) {
            style = Objects1.def(Excels.parseColumnStyle(workbook, option), workbook::createCellStyle);
        } else {
            style = Objects1.def(Excels.parseStyle(workbook, option), workbook::createCellStyle);
        }
        sheetConfig.colorIndex = option.getPaletteColorIndex();
        // 字体
        Font font;
        if (option.getFontOption() != null) {
            option.getFontOption().setPaletteColorIndex(sheetConfig.colorIndex);
            font = Objects1.def(Excels.parseFont(workbook, option.getFontOption()), workbook::createFont);
            sheetConfig.colorIndex = option.getFontOption().getPaletteColorIndex();
        } else {
            font = workbook.createFont();
        }
        style.setFont(font);
        // sheet 宽
        Integer width = option.getWidth();
        if (width != null) {
            // 行宽
            sheet.setColumnWidth(columnIndex, Excels.getWidth(width));
        }
        return style;
    }

}
