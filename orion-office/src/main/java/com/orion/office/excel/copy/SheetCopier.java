package com.orion.office.excel.copy;

import com.orion.lang.utils.Valid;
import com.orion.office.excel.Excels;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

/**
 * 拷贝Sheet
 * 只能拷贝简单的对象 拷贝的列宽有些许差异
 * Streaming不适用
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/4 12:03
 */
public class SheetCopier {

    /**
     * 源workbook
     */
    private final Workbook sourceWorkbook;

    /**
     * 目标workbook
     */
    private final Workbook targetWorkbook;

    /**
     * 源sheet
     */
    private final Sheet sourceSheet;

    /**
     * 目标sheet
     */
    private final Sheet targetSheet;

    /**
     * 列数
     */
    private int column;

    public SheetCopier(Workbook sourceWorkbook, Workbook targetWorkbook, Sheet sourceSheet, Sheet targetSheet) {
        Valid.notNull(sourceWorkbook, "sourceWorkbook is null");
        Valid.notNull(targetWorkbook, "targetWorkbook is null");
        Valid.notNull(sourceSheet, "sourceSheet is null");
        Valid.notNull(targetSheet, "targetSheet is null");
        Valid.isTrue((sourceSheet.getClass() == targetSheet.getClass()), "sourceSheet class not equal targetSheet class");
        this.sourceWorkbook = sourceWorkbook;
        this.targetWorkbook = targetWorkbook;
        this.sourceSheet = sourceSheet;
        this.targetSheet = targetSheet;
        this.column = 32;
    }

    /**
     * copy
     */
    public void copy() {
        this.copyWorkbookColor();
        this.copySheetStyle();
        this.copyMargin();
        this.copyPrint();
        for (Row source : sourceSheet) {
            Row target = targetSheet.createRow(source.getRowNum());
            this.copyRow(source, target);
        }
        this.copyRegion(sourceSheet, targetSheet);
    }

    /**
     * 设置列数 用于设置列宽
     *
     * @param column 列数
     * @return this
     */
    public SheetCopier column(int column) {
        this.column = column;
        return this;
    }

    /**
     * 复制sheet属性
     */
    private void copySheetStyle() {
        targetSheet.setDefaultColumnWidth(sourceSheet.getDefaultColumnWidth());
        targetSheet.setDefaultRowHeight(sourceSheet.getDefaultRowHeight());
        targetSheet.setFitToPage(sourceSheet.getFitToPage());
        targetSheet.setAutobreaks(sourceSheet.getAutobreaks());
        for (int i = 0; i < column; i++) {
            targetSheet.setColumnWidth(i, sourceSheet.getColumnWidth(i));
        }
    }

    /**
     * copy color
     */
    private void copyWorkbookColor() {
        if (sourceWorkbook instanceof HSSFWorkbook && targetWorkbook instanceof HSSFWorkbook) {
            HSSFPalette sourcePalette = ((HSSFWorkbook) sourceWorkbook).getCustomPalette();
            HSSFPalette targetPalette = ((HSSFWorkbook) targetWorkbook).getCustomPalette();
            for (int i = 8; i < 64; i++) {
                short[] cs = sourcePalette.getColor(i).getTriplet();
                targetPalette.setColorAtIndex((short) i, (byte) cs[0], (byte) cs[1], (byte) cs[2]);
            }
        }
    }

    /**
     * copy row
     *
     * @param source source row
     * @param target target row
     */
    private void copyRow(Row source, Row target) {
        target.setHeight(source.getHeight());
        CellStyle sourceStyle = source.getRowStyle();
        if (sourceStyle != null) {
            CellStyle targetStyle = targetWorkbook.createCellStyle();
            targetStyle.cloneStyleFrom(sourceStyle);
            // this.copyCellStyle(sourceStyle, targetStyle);
            target.setRowStyle(targetStyle);
        }
        target.setZeroHeight(source.getZeroHeight());
        for (Cell cell : source) {
            this.copyCell(cell, target.createCell(cell.getColumnIndex()));
        }
    }

    /**
     * copy region
     *
     * @param source source region
     * @param target target region
     */
    private void copyRegion(Sheet source, Sheet target) {
        int sheetMergerCount = source.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            CellRangeAddress address = source.getMergedRegion(i);
            if (address != null) {
                target.addMergedRegion(address);
            }
        }
    }

    /**
     * copy cell
     *
     * @param source source cell
     * @param target target cell
     */
    private void copyCell(Cell source, Cell target) {
        CellStyle resStyle = source.getCellStyle();
        if (resStyle != null) {
            CellStyle style = targetWorkbook.createCellStyle();
            style.cloneStyleFrom(resStyle);
            // this.copyCellStyle(resStyle, style);
            target.setCellStyle(style);
        }
        Comment comment = source.getCellComment();
        if (comment != null) {
            target.setCellComment(comment);
        }
        Excels.copyCellValue(source, target);
    }

    /**
     * copy style
     *
     * @param source source style
     * @param target target style
     */
    private void copyCellStyle(CellStyle source, CellStyle target) {
        target.setBorderBottom(source.getBorderBottom());
        target.setBorderLeft(source.getBorderLeft());
        target.setBorderRight(source.getBorderRight());
        target.setBorderTop(source.getBorderTop());
        target.setTopBorderColor(source.getTopBorderColor());
        target.setBottomBorderColor(source.getBottomBorderColor());
        target.setRightBorderColor(source.getRightBorderColor());
        target.setLeftBorderColor(source.getLeftBorderColor());
        target.setFillPattern(source.getFillPattern());
        if (source instanceof XSSFCellStyle && target instanceof XSSFCellStyle) {
            ((XSSFCellStyle) target).setFillBackgroundColor(((XSSFCellStyle) source).getFillBackgroundColorColor());
            ((XSSFCellStyle) target).setFillForegroundColor(((XSSFCellStyle) source).getFillForegroundColorColor());
        } else {
            target.setFillBackgroundColor(source.getFillBackgroundColor());
            target.setFillForegroundColor(source.getFillForegroundColor());
        }
        target.setDataFormat(source.getDataFormat());
        target.setFillPattern(source.getFillPattern());
        target.setHidden(source.getHidden());
        target.setIndention(source.getIndention());
        target.setLocked(source.getLocked());
        target.setRotation(source.getRotation());
        target.setVerticalAlignment(source.getVerticalAlignment());
        target.setWrapText(source.getWrapText());
        target.setAlignment(source.getAlignment());
        target.setShrinkToFit(source.getShrinkToFit());
        target.setQuotePrefixed(source.getQuotePrefixed());

        Font sourceFont = sourceWorkbook.getFontAt(source.getFontIndexAsInt());
        if (sourceFont != null) {
            Font targetFont = targetWorkbook.createFont();
            this.copyFont(sourceFont, targetFont);
            target.setFont(targetFont);
        }
    }

    /**
     * copy cont
     *
     * @param source source cont
     * @param target target cont
     */
    private void copyFont(Font source, Font target) {
        if (source instanceof XSSFFont && target instanceof XSSFFont) {
            ((XSSFFont) target).setColor(((XSSFFont) source).getXSSFColor());
            ((XSSFFont) target).setFamily(((XSSFFont) source).getFamily());
            ((XSSFFont) target).setScheme(((XSSFFont) source).getScheme());
        } else {
            target.setColor(source.getColor());
        }
        target.setUnderline(source.getUnderline());
        target.setItalic(source.getItalic());
        target.setBold(source.getBold());
        target.setFontName(source.getFontName());
        target.setFontHeight(source.getFontHeight());
        target.setCharSet(source.getCharSet());
        target.setTypeOffset(source.getTypeOffset());
        target.setStrikeout(source.getStrikeout());
    }

    /**
     * copy sheet print setting
     */
    private void copyPrint() {
        PrintSetup source = sourceSheet.getPrintSetup();
        PrintSetup target = targetSheet.getPrintSetup();
        target.setUsePage(source.getUsePage());
        target.setScale(source.getScale());
        target.setPaperSize(source.getPaperSize());
        target.setPageStart(source.getPageStart());
        target.setNotes(source.getNotes());
        if (source.getNoOrientation()) {
            target.setNoOrientation(true);
        } else {
            target.setNoOrientation(false);
            target.setLandscape(source.getLandscape());
        }
        target.setNoColor(source.getNoColor());
        target.setLeftToRight(source.getLeftToRight());
        target.setHResolution(source.getHResolution());
        target.setVResolution(source.getVResolution());
        target.setHeaderMargin(source.getHeaderMargin());
        target.setFooterMargin(source.getFooterMargin());
        target.setCopies(source.getCopies());
        target.setDraft(source.getDraft());
        target.setFitWidth(source.getFitWidth());
        target.setFitHeight(source.getFitHeight());
        target.setValidSettings(source.getValidSettings());
    }

    /**
     * copy margin
     */
    private void copyMargin() {
        for (short i = 0; i < 5; i++) {
            targetSheet.setMargin(i, sourceSheet.getMargin(i));
        }
    }

    public Workbook getSourceWorkbook() {
        return sourceWorkbook;
    }

    public Workbook getTargetWorkbook() {
        return targetWorkbook;
    }

    public Sheet getSourceSheet() {
        return sourceSheet;
    }

    public Sheet getTargetSheet() {
        return targetSheet;
    }

}
