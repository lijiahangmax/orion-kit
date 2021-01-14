package com.orion.excel.writer.exporting;

import com.orion.able.Analysable;
import com.orion.excel.annotation.*;
import com.orion.excel.option.*;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.reflect.Annotations;

/**
 * Export sheet 解析器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/12/29 17:49
 */
public class ExportSheetAnalysis implements Analysable {

    private Class<?> targetClass;

    private ExportSheetOption sheetOption;

    public ExportSheetAnalysis(Class<?> targetClass, ExportSheetOption sheetOption) {
        this.targetClass = targetClass;
        this.sheetOption = sheetOption;
    }

    @Override
    public void analysis() {
        ExportSheet sheet = Annotations.getAnnotation(targetClass, ExportSheet.class);
        if (sheet == null) {
            throw Exceptions.parse("The exported class cannot be parsed because an @ExportSheet was not found");
        }

        String sheetName = sheet.name();
        if (!Strings.isEmpty(sheetName)) {
            sheetOption.setName(sheetName);
        }
        // 复用style
        if (sheet.headerUseColumnStyle()) {
            sheetOption.setHeaderUseColumnStyle(true);
        }
        // 默认行宽
        int width = sheet.columnWidth();
        if (width != -1) {
            sheetOption.setColumnWidth(width);
        }
        // 默认行高
        int defaultHeight = sheet.height();
        if (defaultHeight != -1) {
            sheetOption.setTitleHeight(defaultHeight);
            sheetOption.setHeaderHeight(defaultHeight);
            sheetOption.setRowHeight(defaultHeight);
        }
        // 标题行高
        int titleHeight = sheet.titleHeight();
        if (titleHeight != -1) {
            sheetOption.setTitleHeight(titleHeight);
        }
        // 头部行高
        int headerHeight = sheet.headerHeight();
        if (headerHeight != -1) {
            sheetOption.setHeaderHeight(headerHeight);
        }
        // 数据行高
        int rowHeight = sheet.rowHeight();
        if (rowHeight != -1) {
            sheetOption.setRowHeight(rowHeight);
        }
        // 缩放
        if (sheet.zoom() != -1) {
            sheetOption.setZoom(sheet.zoom());
        }
        sheetOption.setSkipFieldHeader(sheet.skipFieldHeader());
        sheetOption.setSkipComment(sheet.skipComment());
        sheetOption.setSkipLink(sheet.skipLink());
        sheetOption.setSkipPicture(sheet.skipPicture());
        sheetOption.setSkipPictureException(sheet.skipPictureException());
        sheetOption.setSkipSelectOption(sheet.skipSelectOption());
        sheetOption.setFreezeHeader(sheet.freezeHeader());
        sheetOption.setFilterHeader(sheet.filterHeader());
        sheetOption.setSelected(sheet.selected());
        sheetOption.setHidden(sheet.hidden());
        sheetOption.setDisplayGridLines(sheet.displayGridLines());
        sheetOption.setDisplayRowColHeadings(sheet.displayRowColHeadings());
        sheetOption.setDisplayFormulas(sheet.displayFormulas());
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
        FontOption fontOption = ExportColumnAnalysis.parseFont(title.font());
        titleOption.setFont(fontOption);
        sheetOption.setTitleOption(titleOption);
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
        sheetOption.setHeaderOption(headerOption);
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
        sheetOption.setFooterOption(footerOption);
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
        sheetOption.setPrintOption(printOption);
    }

}
