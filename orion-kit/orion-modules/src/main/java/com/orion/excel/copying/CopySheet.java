package com.orion.excel.copying;

import com.orion.utils.Valid;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

/**
 * @author ljh15
 * @version 1.0.0
 * @date 2020/6/4 12:03
 */
public class CopySheet {

    /**
     * 源workbook
     */
    private Workbook resourceWorkbook;

    /**
     * 目标workbook
     */
    private Workbook targetWorkbook;

    /**
     * 源sheet
     */
    private Sheet resourceSheet;

    /**
     * 目标sheet
     */
    private Sheet targetSheet;

    /**
     * 列数
     */
    private int column = 128;

    public CopySheet(Workbook resourceWorkbook, Workbook targetWorkbook, Sheet resourceSheet, Sheet targetSheet) {
        Valid.notNull(resourceWorkbook, "ResourceWorkbook is null");
        Valid.notNull(targetWorkbook, "TargetWorkbook is null");
        Valid.notNull(resourceSheet, "ResourceSheet is null");
        Valid.notNull(targetSheet, "TargetSheet is null");
        Valid.isTrue((resourceSheet.getClass() == targetSheet.getClass()), "ResourceSheet class not equal targetSheet class");
        this.resourceWorkbook = resourceWorkbook;
        this.targetWorkbook = targetWorkbook;
        this.resourceSheet = resourceSheet;
        this.targetSheet = targetSheet;
    }

    /**
     * copy
     *
     * @return this
     */
    public CopySheet copy() {
        this.copyWorkbookColor();
        this.copySheet();
        for (Row resource : resourceSheet) {
            Row target = targetSheet.createRow(resource.getRowNum());
            copyRow(resource, target);
        }
        this.copyRegion(resourceSheet, targetSheet);
        return this;
    }

    /**
     * 设置列数 用于设置列宽
     *
     * @param column 列数
     * @return this
     */
    public CopySheet setColumn(int column) {
        this.column = column;
        return this;
    }

    /**
     * 复制sheet属性
     */
    private void copySheet() {
        targetSheet.setDefaultColumnWidth(resourceSheet.getDefaultColumnWidth());
        targetSheet.setDefaultRowHeight(resourceSheet.getDefaultRowHeight());
        targetSheet.setFitToPage(resourceSheet.getFitToPage());
        targetSheet.setAutobreaks(resourceSheet.getAutobreaks());
        for (int i = 0; i < column; i++) {
            targetSheet.setColumnWidth(i, resourceSheet.getColumnWidth(i));
        }
    }

    /**
     * copy color
     */
    private void copyWorkbookColor() {
        if (resourceWorkbook instanceof HSSFWorkbook && targetWorkbook instanceof HSSFWorkbook) {
            HSSFPalette resourcePalette = ((HSSFWorkbook) resourceWorkbook).getCustomPalette();
            HSSFPalette targetPalette = ((HSSFWorkbook) targetWorkbook).getCustomPalette();
            for (int i = 8; i < 64; i++) {
                short[] cs = resourcePalette.getColor(i).getTriplet();
                targetPalette.setColorAtIndex((short) i, (byte) cs[0], (byte) cs[1], (byte) cs[2]);
            }
        }
    }

    /**
     * copy row
     *
     * @param resource resource row
     * @param target   target row
     */
    private void copyRow(Row resource, Row target) {
        target.setHeight(resource.getHeight());
        CellStyle resourceStyle = resource.getRowStyle();
        if (resourceStyle != null) {
            CellStyle targetStyle = targetWorkbook.createCellStyle();
            this.copyCellStyle(resourceStyle, targetStyle);
            target.setRowStyle(targetStyle);
        }
        target.setZeroHeight(resource.getZeroHeight());
        for (Cell cell : resource) {
            this.copyCell(cell, target.createCell(cell.getColumnIndex()));
        }
    }

    /**
     * copy region
     *
     * @param resource resource region
     * @param target   target region
     */
    private void copyRegion(Sheet resource, Sheet target) {
        int sheetMergerCount = resource.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            CellRangeAddress address = resource.getMergedRegion(i);
            if (address != null) {
                target.addMergedRegion(address);
            }
        }
    }

    /**
     * copy cell
     *
     * @param resource resource cell
     * @param target   target cell
     */
    private void copyCell(Cell resource, Cell target) {
        CellStyle resStyle = resource.getCellStyle();
        if (resStyle != null) {
            CellStyle style = targetWorkbook.createCellStyle();
            this.copyCellStyle(resStyle, style);
            target.setCellStyle(style);
        }
        Comment comment = resource.getCellComment();
        if (comment != null) {
            target.setCellComment(comment);
        }
        CellType type = resource.getCellType();
        target.setCellType(type);
        switch (type) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(resource)) {
                    target.setCellValue(resource.getDateCellValue());
                } else {
                    target.setCellValue(resource.getNumericCellValue());
                }
                break;
            case STRING:
                target.setCellValue(resource.getRichStringCellValue());
                break;
            case FORMULA:
                target.setCellFormula(resource.getCellFormula());
                break;
            case ERROR:
                target.setCellErrorValue(resource.getErrorCellValue());
                break;
            case BOOLEAN:
                target.setCellValue(resource.getBooleanCellValue());
                break;
            default:
                target.setCellValue("");
        }
    }

    /**
     * copy style
     *
     * @param resource resource style
     * @param target   target style
     */
    private void copyCellStyle(CellStyle resource, CellStyle target) {
        target.setBorderBottom(resource.getBorderBottom());
        target.setBorderLeft(resource.getBorderLeft());
        target.setBorderRight(resource.getBorderRight());
        target.setBorderTop(resource.getBorderTop());
        target.setTopBorderColor(resource.getTopBorderColor());
        target.setBottomBorderColor(resource.getBottomBorderColor());
        target.setRightBorderColor(resource.getRightBorderColor());
        target.setLeftBorderColor(resource.getLeftBorderColor());
        target.setFillPattern(resource.getFillPattern());
        if (resource instanceof XSSFCellStyle && target instanceof XSSFCellStyle) {
            ((XSSFCellStyle) target).setFillBackgroundColor(((XSSFCellStyle) resource).getFillBackgroundColorColor());
            ((XSSFCellStyle) target).setFillForegroundColor(((XSSFCellStyle) resource).getFillForegroundColorColor());
        } else {
            target.setFillBackgroundColor(resource.getFillBackgroundColor());
            target.setFillForegroundColor(resource.getFillForegroundColor());
        }
        target.setDataFormat(resource.getDataFormat());
        target.setFillPattern(resource.getFillPattern());
        target.setHidden(resource.getHidden());
        target.setIndention(resource.getIndention());
        target.setLocked(resource.getLocked());
        target.setRotation(resource.getRotation());
        target.setVerticalAlignment(resource.getVerticalAlignment());
        target.setWrapText(resource.getWrapText());
        target.setAlignment(resource.getAlignment());
        target.setShrinkToFit(resource.getShrinkToFit());
        target.setQuotePrefixed(resource.getQuotePrefixed());

        Font resourceFont = resourceWorkbook.getFontAt(resource.getFontIndexAsInt());
        if (resourceFont != null) {
            Font targetFont = targetWorkbook.createFont();
            this.copyFont(resourceFont, targetFont);
            target.setFont(targetFont);
        }
    }

    /**
     * copy cont
     *
     * @param resource resource cont
     * @param target   target cont
     */
    private void copyFont(Font resource, Font target) {
        if (resource instanceof XSSFFont && target instanceof XSSFFont) {
            ((XSSFFont) target).setColor(((XSSFFont) resource).getXSSFColor());
        } else {
            target.setColor(resource.getColor());
        }
        target.setUnderline(resource.getUnderline());
        target.setItalic(resource.getItalic());
        target.setBold(resource.getBold());
        target.setFontName(resource.getFontName());
        target.setFontHeight(resource.getFontHeight());
        target.setCharSet(resource.getCharSet());
        target.setTypeOffset(resource.getTypeOffset());
        target.setStrikeout(resource.getStrikeout());
    }

    public Workbook getResourceWorkbook() {
        return resourceWorkbook;
    }

    public Workbook getTargetWorkbook() {
        return targetWorkbook;
    }

    public Sheet getResourceSheet() {
        return resourceSheet;
    }

    public Sheet getTargetSheet() {
        return targetSheet;
    }

}
