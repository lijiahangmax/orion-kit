package com.orion.excel;

import com.orion.utils.Dates;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * excel工具类
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
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = Dates.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                } else {
                    // 纯数字
                    BigDecimal big = new BigDecimal(cell.getNumericCellValue());
                    value = big.toString();
                    if (null != value && !"".equals(value.trim())) {
                        String[] item = value.split("[.]");
                        if (1 < item.length && "0".equals(item[1])) {
                            value = item[0];
                        }
                    }
                }
                break;
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_FORMULA:
                value = String.valueOf(cell.getNumericCellValue());
                if ("NaN".equals(value)) {
                    value = cell.getStringCellValue();
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = "" + cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            case Cell.CELL_TYPE_ERROR:
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
    private static Date getCellDate(Cell cell) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                case Cell.CELL_TYPE_FORMULA:
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
    private static String getCellPhone(Cell cell) {
        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_NUMERIC:
                return DF.format(cell.getNumericCellValue());
            case XSSFCell.CELL_TYPE_STRING:
                return DF.format(Double.parseDouble(cell.toString()));
            default:
                return cell.toString();
        }
    }

}
