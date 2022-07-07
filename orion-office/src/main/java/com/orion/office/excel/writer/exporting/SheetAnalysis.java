package com.orion.office.excel.writer.exporting;

import com.orion.lang.able.Analysable;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.reflect.Annotations;
import com.orion.office.excel.annotation.*;
import com.orion.office.excel.option.*;

/**
 * export sheet 解析器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/29 17:49
 */
public class SheetAnalysis implements Analysable {

    private Class<?> targetClass;

    private SheetConfig sheetConfig;

    public SheetAnalysis(Class<?> targetClass, SheetConfig sheetConfig) {
        this.targetClass = targetClass;
        this.sheetConfig = sheetConfig;
    }

    @Override
    public void analysis() {
        // sheet
        this.analysisSheet();
        // 标题
        this.analysisTitle();
        // 页眉
        this.analysisHeader();
        // 页脚
        this.analysisFooter();
        // 打印
        this.analysisPrint();
    }

    /**
     * 解析sheet
     *
     * @see ExportSheet
     */
    private void analysisSheet() {
        ExportSheet sheet = Annotations.getAnnotation(targetClass, ExportSheet.class);
        if (sheet == null) {
            throw Exceptions.parse("the exported class cannot be parsed because an @ExportSheet was not found");
        }

        String sheetName = sheet.name();
        if (!Strings.isEmpty(sheetName)) {
            sheetConfig.sheetOption.setName(sheetName);
        }
        // 复用style
        sheetConfig.sheetOption.setHeaderUseColumnStyle(sheet.headerUseColumnStyle());
        // 列是否使用默认样式
        sheetConfig.sheetOption.setColumnUseDefaultStyle(sheet.columnUseDefaultStyle());
        // 是否将index作为排序字段
        sheetConfig.sheetOption.setIndexToSort(sheet.indexToSort());
        // 默认行宽
        int width = sheet.columnWidth();
        if (width != -1) {
            sheetConfig.sheetOption.setColumnWidth(width);
        }
        // 默认行高
        int defaultHeight = sheet.height();
        if (defaultHeight != -1) {
            sheetConfig.sheetOption.setTitleHeight(defaultHeight);
            sheetConfig.sheetOption.setHeaderHeight(defaultHeight);
            sheetConfig.sheetOption.setRowHeight(defaultHeight);
        }
        // 标题行高
        int titleHeight = sheet.titleHeight();
        if (titleHeight != -1) {
            sheetConfig.sheetOption.setTitleHeight(titleHeight);
        }
        // 头部行高
        int headerHeight = sheet.headerHeight();
        if (headerHeight != -1) {
            sheetConfig.sheetOption.setHeaderHeight(headerHeight);
        }
        // 数据行高
        int rowHeight = sheet.rowHeight();
        if (rowHeight != -1) {
            sheetConfig.sheetOption.setRowHeight(rowHeight);
        }
        // 缩放
        if (sheet.zoom() != -1) {
            sheetConfig.sheetOption.setZoom(sheet.zoom());
        }
        sheetConfig.sheetOption.setSkipFieldHeader(sheet.skipFieldHeader());
        sheetConfig.sheetOption.setSkipComment(sheet.skipComment());
        sheetConfig.sheetOption.setSkipLink(sheet.skipLink());
        sheetConfig.sheetOption.setSkipPicture(sheet.skipPicture());
        sheetConfig.sheetOption.setSkipPictureException(sheet.skipPictureException());
        sheetConfig.sheetOption.setSkipSelectOption(sheet.skipSelectOption());
        sheetConfig.sheetOption.setFreezeHeader(sheet.freezeHeader());
        sheetConfig.sheetOption.setFilterHeader(sheet.filterHeader());
        sheetConfig.sheetOption.setSelected(sheet.selected());
        sheetConfig.sheetOption.setHidden(sheet.hidden());
        sheetConfig.sheetOption.setDisplayGridLines(sheet.displayGridLines());
        sheetConfig.sheetOption.setDisplayRowColHeadings(sheet.displayRowColHeadings());
        sheetConfig.sheetOption.setDisplayFormulas(sheet.displayFormulas());
    }

    /**
     * 解析标题
     *
     * @see ExportTitle
     */
    private void analysisTitle() {
        ExportTitle title = Annotations.getAnnotation(targetClass, ExportTitle.class);
        if (title == null) {
            return;
        }
        TitleOption titleOption = new TitleOption();
        titleOption.setTitle(title.title());
        // 占用大小
        titleOption.setUseRow(title.useRow());
        titleOption.setUseColumn(title.useColumn());
        // 对齐
        titleOption.setVerticalAlign(title.verticalAlign());
        titleOption.setAlign(title.align());
        // 背景色
        String backgroundColor = title.backgroundColor();
        if (!Strings.isEmpty(backgroundColor)) {
            titleOption.setBackgroundColor(backgroundColor);
        }
        // 边框
        titleOption.setBorder(title.border());
        String borderColor = title.borderColor();
        if (!Strings.isEmpty(borderColor)) {
            titleOption.setBorderColor(borderColor);
        }
        FontOption fontOption = SheetColumnAnalysis.parseFont(title.font());
        titleOption.setFont(fontOption);
        sheetConfig.sheetOption.setTitleOption(titleOption);
    }

    /**
     * 解析页眉
     *
     * @see ExportHeader
     */
    private void analysisHeader() {
        ExportHeader header = Annotations.getAnnotation(targetClass, ExportHeader.class);
        if (header == null) {
            return;
        }
        HeaderOption headerOption = new HeaderOption();
        headerOption.setLeft(header.left());
        headerOption.setCenter(header.center());
        headerOption.setRight(header.right());
        sheetConfig.sheetOption.setHeaderOption(headerOption);
    }

    /**
     * 解析页脚
     *
     * @see ExportFooter
     */
    private void analysisFooter() {
        ExportFooter footer = Annotations.getAnnotation(targetClass, ExportFooter.class);
        if (footer == null) {
            return;
        }
        FooterOption footerOption = new FooterOption();
        footerOption.setLeft(footer.left());
        footerOption.setCenter(footer.center());
        footerOption.setRight(footer.right());
        sheetConfig.sheetOption.setFooterOption(footerOption);
    }

    /**
     * 解析打印
     *
     * @see ExportPrint
     */
    private void analysisPrint() {
        ExportPrint print = Annotations.getAnnotation(targetClass, ExportPrint.class);
        if (print == null) {
            return;
        }
        PrintOption printOption = new PrintOption();
        printOption.setPrintGridLines(print.printGridLines());
        printOption.setPrintRowHeading(print.printHeading());
        printOption.setFit(print.fit());
        printOption.setHorizontallyCenter(print.horizontallyCenter());
        printOption.setVerticallyCenter(print.verticallyCenter());
        printOption.setPaper(print.paper());
        printOption.setColor(print.color());
        printOption.setLandScapePrint(print.landScapePrint());
        printOption.setSetPrintOrientation(print.setPrintOrientation());
        printOption.setScale(print.scale());
        printOption.setNotes(print.notes());
        printOption.setUsePage(print.usePage());
        printOption.setPageStart(print.pageStart());
        printOption.setCopies(print.copies());
        printOption.setDraft(print.draft());
        printOption.setTopToBottom(print.topToBottom());

        int horizontalResolution = print.horizontalResolution();
        if (horizontalResolution != -1) {
            printOption.setHorizontalResolution(horizontalResolution);
        }
        int verticalResolution = print.verticalResolution();
        if (verticalResolution != -1) {
            printOption.setVerticalResolution(verticalResolution);
        }
        int width = print.width();
        if (width != -1) {
            printOption.setWidth(width);
        }
        int height = print.height();
        if (height != -1) {
            printOption.setHeight(height);
        }
        double leftMargin = print.leftMargin();
        if (leftMargin != -1) {
            printOption.setLeftMargin(leftMargin);
        }
        double rightMargin = print.rightMargin();
        if (rightMargin != -1) {
            printOption.setRightMargin(rightMargin);
        }
        double topMargin = print.topMargin();
        if (topMargin != -1) {
            printOption.setTopMargin(topMargin);
        }
        double bottomMargin = print.bottomMargin();
        if (bottomMargin != -1) {
            printOption.setBottomMargin(bottomMargin);
        }
        double headerMargin = print.headerMargin();
        if (headerMargin != -1) {
            printOption.setHeaderMargin(headerMargin);
        }
        double footerMargin = print.footerMargin();
        if (footerMargin != -1) {
            printOption.setFooterMargin(footerMargin);
        }
        int limit = print.limit();
        if (limit != -1) {
            printOption.setAutoLimit(true);
            printOption.setLimit(limit);
        }
        int[] repeat = print.repeat();
        if (repeat.length != 0) {
            printOption.setRepeat(repeat);
        }
        sheetConfig.sheetOption.setPrintOption(printOption);
    }

}
