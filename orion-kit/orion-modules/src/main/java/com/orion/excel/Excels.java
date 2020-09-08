package com.orion.excel;

import com.monitorjbl.xlsx.StreamingReader;
import com.monitorjbl.xlsx.impl.StreamingSheet;
import com.monitorjbl.xlsx.impl.StreamingWorkbook;
import com.orion.excel.copy.CopySheet;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import com.orion.utils.time.Dates;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Excel 工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/6 15:50
 */
public class Excels {

    private static final DecimalFormat DF = new DecimalFormat("#");

    /**
     * 流式读取缓存行数
     */
    private static final int STREAMING_ROW = 100;

    /**
     * 流式读取缓冲区大小
     */
    private static final int STREAMING_BUFFER = 4096;

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
     * 写入workbook到文件
     *
     * @param workbook workbook
     * @param file     文件
     */
    public static void write(Workbook workbook, String file) {
        write(workbook, new File(file));
    }

    /**
     * 写入workbook到文件
     *
     * @param workbook workbook
     * @param file     文件
     */
    public static void write(Workbook workbook, File file) {
        Valid.notNull(workbook, "workbook is null");
        Valid.notNull(file, "file is null");
        Files1.touch(file);
        FileOutputStream out = null;
        try {
            out = Files1.openOutputStream(file);
            workbook.write(out);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            Streams.close(out);
        }
    }

    /**
     * 写入workbook到流
     *
     * @param workbook workbook
     * @param out      流
     */
    public static void write(Workbook workbook, OutputStream out) {
        Valid.notNull(workbook, "workbook is null");
        Valid.notNull(out, "outputStream is null");
        try {
            workbook.write(out);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 关闭workbook
     *
     * @param workbook workbook
     */
    public static void close(Workbook workbook) {
        Streams.close(workbook);
    }

    /**
     * 复制sheet 新建sheet
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

    /**
     * 复制sheet 如果未找到目标sheet 则新建sheet
     *
     * @param resourceWorkbook 源表
     * @param targetWorkbook   目标表
     * @param resourceIndex    源sheet索引
     * @param targetIndex      目标sheet索引
     * @return CopySheet
     */
    public static CopySheet copySheet(Workbook resourceWorkbook, Workbook targetWorkbook, int resourceIndex, int targetIndex) {
        Sheet resourceSheet = resourceWorkbook.getSheetAt(resourceIndex);
        Sheet targetSheet;
        try {
            targetSheet = targetWorkbook.getSheetAt(targetIndex);
        } catch (Exception e) {
            targetSheet = targetWorkbook.createSheet(resourceSheet.getSheetName());
        }
        return new CopySheet(resourceWorkbook, targetWorkbook, resourceSheet, targetSheet);
    }

    /**
     * 获取流式workbook
     *
     * @param in 文件流
     * @return StreamingWorkbook
     */
    public static Workbook getStreamingWorkbook(InputStream in) {
        return StreamingReader.builder()
                .rowCacheSize(STREAMING_ROW)
                .bufferSize(STREAMING_BUFFER)
                .open(in);
    }

    /**
     * 获取流式workbook
     *
     * @param file 文件
     * @return StreamingWorkbook
     */
    public static Workbook getStreamingWorkbook(File file) {
        return StreamingReader.builder()
                .rowCacheSize(STREAMING_ROW)
                .bufferSize(STREAMING_BUFFER)
                .open(file);
    }

    /**
     * 获取流式workbook
     *
     * @param file 文件
     * @return StreamingWorkbook
     */
    public static Workbook getStreamingWorkbook(String file) {
        return StreamingReader.builder()
                .rowCacheSize(STREAMING_ROW)
                .bufferSize(STREAMING_BUFFER)
                .open(new File(file));
    }

    /**
     * 获取流式workbook
     *
     * @param in       文件流
     * @param rowCache row缓存
     * @return StreamingWorkbook
     */
    public static Workbook getStreamingWorkbook(InputStream in, int rowCache) {
        return StreamingReader.builder()
                .rowCacheSize(rowCache)
                .bufferSize(STREAMING_BUFFER)
                .open(in);
    }

    /**
     * 获取流式workbook
     *
     * @param file     文件
     * @param rowCache row缓存
     * @return StreamingWorkbook
     */
    public static Workbook getStreamingWorkbook(File file, int rowCache) {
        return StreamingReader.builder()
                .rowCacheSize(rowCache)
                .bufferSize(STREAMING_BUFFER)
                .open(file);
    }

    /**
     * 获取流式workbook
     *
     * @param file     文件
     * @param rowCache row缓存
     * @return StreamingWorkbook
     */
    public static Workbook getStreamingWorkbook(String file, int rowCache) {
        return StreamingReader.builder()
                .rowCacheSize(rowCache)
                .bufferSize(STREAMING_BUFFER)
                .open(new File(file));
    }

    /**
     * 获取流式workbook
     *
     * @param in         文件流
     * @param rowCache   row缓存
     * @param bufferSize 缓冲区大小
     * @return StreamingWorkbook
     */
    public static Workbook getStreamingWorkbook(InputStream in, int rowCache, int bufferSize) {
        return StreamingReader.builder()
                .rowCacheSize(rowCache)
                .bufferSize(bufferSize)
                .open(in);
    }

    /**
     * 获取流式workbook
     *
     * @param file       文件
     * @param rowCache   row缓存
     * @param bufferSize 缓冲区大小
     * @return StreamingWorkbook
     */
    public static Workbook getStreamingWorkbook(File file, int rowCache, int bufferSize) {
        return StreamingReader.builder()
                .rowCacheSize(rowCache)
                .bufferSize(bufferSize)
                .open(file);
    }

    /**
     * 获取流式workbook
     *
     * @param file       文件
     * @param rowCache   row缓存
     * @param bufferSize 缓冲区大小
     * @return StreamingWorkbook
     */
    public static Workbook getStreamingWorkbook(String file, int rowCache, int bufferSize) {
        return StreamingReader.builder()
                .rowCacheSize(rowCache)
                .bufferSize(bufferSize)
                .open(new File(file));
    }

    /**
     * 是否为流式读取的workbook
     *
     * @param workbook workbook
     * @return true StreamingWorkbook
     */
    public static boolean isStreamingWorkbook(Workbook workbook) {
        return workbook instanceof StreamingWorkbook;
    }

    /**
     * 是否为流式读取的workbook
     *
     * @param clazz workbook class
     * @return true StreamingWorkbook
     */
    public static boolean isStreamingWorkbook(Class clazz) {
        return StreamingWorkbook.class.equals(clazz);
    }

    /**
     * 是否为流式读取的sheet
     *
     * @param sheet sheet
     * @return true StreamingSheet
     */
    public static boolean isStreamingSheet(Sheet sheet) {
        return sheet instanceof StreamingSheet;
    }

    /**
     * 是否为流式读取的sheet
     *
     * @param clazz sheet class
     * @return true StreamingSheet
     */
    public static boolean isStreamingSheet(Class clazz) {
        return StreamingSheet.class.equals(clazz);
    }

}
