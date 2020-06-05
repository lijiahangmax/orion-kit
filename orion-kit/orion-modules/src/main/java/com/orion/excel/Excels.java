package com.orion.excel;

import com.orion.excel.copying.CopySheet;
import com.orion.utils.Dates;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Excel 工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/6 15:50
 */
public class Excels {

    private static final DecimalFormat DF = new DecimalFormat("#");

    /**
     * 获取String的值
     *
     * @param cell cell
     * @return value
     */
    public static String getValue(Cell cell) {
        String value = "";
        if (null == cell) {
            return value;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = Dates.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                } else {
                    // 纯数字
                    BigDecimal big = new BigDecimal(cell.getNumericCellValue());
                    value = big.toString();
                    if (null != value && !"".equals(value.trim())) {
                        String[] item = value.split("[.]");
                        if (1 < item.length && Integer.parseInt(item[1]) == 0) {
                            value = item[0];
                        }
                    }
                }
                break;
            case STRING:
                value = cell.getStringCellValue();
                break;
            case FORMULA:
                value = String.valueOf(cell.getNumericCellValue());
                if ("NaN".equals(value)) {
                    value = cell.getStringCellValue();
                }
                break;
            case BOOLEAN:
                value = "" + cell.getBooleanCellValue();
                break;
            case BLANK:
                value = "";
                break;
            case ERROR:
                value = "";
                break;
            default:
                value = cell.getStringCellValue();
        }
        return value;
    }

    /**
     * 获取时间
     *
     * @param cell cell
     * @return 时间
     */
    public static Date getCellDate(Cell cell) {
        if (cell != null) {
            CellType cellType = cell.getCellType();
            switch (cellType) {
                case NUMERIC:
                case FORMULA:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue();
                    }
                default:
                    return Dates.date(cell.toString());
            }
        } else {
            return null;
        }
    }

    /**
     * 获取手机号
     *
     * @param cell cell
     * @return 手机号
     */
    public static String getCellPhone(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                return DF.format(cell.getNumericCellValue());
            case STRING:
                return DF.format(Double.parseDouble(cell.toString()));
            default:
                return cell.toString();
        }
    }

    /**
     * 合并单元格
     *
     * @param row       合并行
     * @param firstCell 合并开始单元格
     * @param lastCell  合并结束单元格
     * @return merge
     */
    public static CellRangeAddress mergeCell(int row, int firstCell, int lastCell) {
        return new CellRangeAddress(row, row, firstCell, lastCell);
    }

    /**
     * 合并单元格
     *
     * @param firstRow  合并开始行
     * @param lastRow   合并结束行
     * @param firstCell 合并开始单元格
     * @param lastCell  合并结束单元格
     * @return merge
     */
    public static CellRangeAddress mergeCell(int firstRow, int lastRow, int firstCell, int lastCell) {
        return new CellRangeAddress(firstRow, lastRow, firstCell, lastCell);
    }

    /**
     * 合并单元格
     *
     * @param sheet     sheet
     * @param firstRow  合并开始行
     * @param lastRow   合并结束行
     * @param firstCell 合并开始单元格
     * @param lastCell  合并结束单元格
     */
    public static void mergeCell(Sheet sheet, int firstRow, int lastRow, int firstCell, int lastCell) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCell, lastCell));
    }

    /**
     * 合并单元格
     *
     * @param sheet     sheet
     * @param row       合并行
     * @param firstCell 合并开始单元格
     * @param lastCell  合并结束单元格
     */
    public static void mergeCell(Sheet sheet, int row, int firstCell, int lastCell) {
        sheet.addMergedRegion(new CellRangeAddress(row, row, firstCell, lastCell));
    }

    /**
     * 复制sheet
     *
     * @param resourceWorkbook 源表
     * @param targetWorkbook   目标表
     * @param resourceIndex    源sheet索引
     * @return CopySheet
     */
    public static CopySheet copySheet(Workbook resourceWorkbook, Workbook targetWorkbook, int resourceIndex) {
        Sheet resourceSheet = resourceWorkbook.getSheetAt(resourceIndex);
        Sheet targetSheet = targetWorkbook.createSheet(resourceSheet.getSheetName());
        return new CopySheet(resourceWorkbook, targetWorkbook, resourceSheet, targetSheet);
    }

}
