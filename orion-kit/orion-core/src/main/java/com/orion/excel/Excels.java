package com.orion.excel;

import com.monitorjbl.xlsx.StreamingReader;
import com.monitorjbl.xlsx.impl.StreamingSheet;
import com.monitorjbl.xlsx.impl.StreamingWorkbook;
import com.orion.excel.builder.ExcelFieldType;
import com.orion.excel.copy.CopySheet;
import com.orion.utils.Exceptions;
import com.orion.utils.Objects1;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.convert.Converts;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import com.orion.utils.time.Dates;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    private static final int BUFFER_ROW = 100;

    /**
     * 流式读取缓冲区大小
     */
    private static final int BUFFER_SIZE = 1024 * 4;

    private Excels() {
    }

    /**
     * 获取String的值
     *
     * @param cell cell
     * @return value
     */
    public static String getValue(Cell cell) {
        String value = Strings.EMPTY;
        if (null == cell) {
            return value;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = Dates.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                } else {
                    // 纯数字
                    BigDecimal big = BigDecimal.valueOf(cell.getNumericCellValue());
                    value = big.toString();
                    if (!Strings.isBlank(value)) {
                        String[] item = value.split("[.]");
                        if (1 < item.length && Integer.parseInt(item[1]) == 0) {
                            value = item[0];
                        }
                    }
                }
                break;
            case FORMULA:
                value = String.valueOf(cell.getNumericCellValue());
                if ("NaN".equals(value)) {
                    value = cell.getStringCellValue();
                }
                break;
            case BOOLEAN:
                value = Strings.EMPTY + cell.getBooleanCellValue();
                break;
            case BLANK:
            case ERROR:
                break;
            case STRING:
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
     * 设置cell的值
     *
     * @param cell  cell
     * @param value value
     */
    public static void setCellValue(Cell cell, Object value) {
        setCellValue(cell, value, null, null);
    }

    /**
     * 设置cell的值
     *
     * @param cell  cell
     * @param value value
     * @param type  type
     */
    public static void setCellValue(Cell cell, Object value, ExcelFieldType type) {
        setCellValue(cell, value, type, null);
    }

    /**
     * 设置cell的值
     *
     * @param cell        cell
     * @param value       value
     * @param type        type
     * @param datePattern 日期格式
     */
    public static void setCellValue(Cell cell, Object value, ExcelFieldType type, String datePattern) {
        if (type == null) {
            cell.setCellValue(Objects1.toString(value));
        } else {
            switch (type) {
                case NUMBER:
                    if (value == null) {
                        cell.setCellValue(0D);
                    } else {
                        cell.setCellValue(Converts.toDouble(value));
                    }
                    break;
                case DATE:
                    if (datePattern != null) {
                        cell.setCellValue(Dates.format(Converts.toDate(value), datePattern));
                    } else {
                        cell.setCellValue(Converts.toDate(value));
                    }
                    break;
                case FORMULA:
                    cell.setCellFormula(Objects1.toString(value));
                    break;
                case BOOLEAN:
                    cell.setCellValue(Converts.toBoolean(value));
                    break;
                case TEXT:
                default:
                    cell.setCellValue(Objects1.toString(value));
                    break;
            }
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

    // --------------- open ---------------

    public static Workbook openWorkbook(InputStream in) {
        return openWorkbook(in, null, null);
    }

    public static Workbook openWorkbook(InputStream in, String fileName) {
        return openWorkbook(in, fileName, null);
    }

    public static Workbook openWorkbook(String file) {
        return openWorkbook(Files1.openInputStreamSafe(file), Files1.getFileName(file), null);
    }

    public static Workbook openWorkbook(File file) {
        return openWorkbook(Files1.openInputStreamSafe(file), Files1.getFileName(file), null);
    }

    public static Workbook openWorkbook(String file, String password) {
        return openWorkbook(Files1.openInputStreamSafe(file), Files1.getFileName(file), password);
    }

    public static Workbook openWorkbook(File file, String password) {
        return openWorkbook(Files1.openInputStreamSafe(file), Files1.getFileName(file), password);
    }

    /**
     * 获取流式workbook
     *
     * @param in 文件流
     * @return StreamingWorkbook
     */
    public static Workbook openWorkbook(InputStream in, String fileName, String password) {
        try {
            if (password == null) {
                if (fileName != null && fileName.toLowerCase().endsWith("xls")) {
                    return new HSSFWorkbook(in);
                } else {
                    return new XSSFWorkbook(OPCPackage.open(in));
                }
            } else {
                if (fileName != null && fileName.toLowerCase().endsWith("xls")) {
                    return WorkbookFactory.create(in, password);
                } else {
                    POIFSFileSystem pfs = new POIFSFileSystem(in);
                    Decryptor decryptor = Decryptor.getInstance(new EncryptionInfo(pfs));
                    decryptor.verifyPassword(password);
                    return new XSSFWorkbook(decryptor.getDataStream(pfs));
                }
            }
        } catch (Exception e) {
            throw Exceptions.parse("cannot open excel file", e);
        }
    }

    // --------------- open streaming ---------------

    public static Workbook openStreamingWorkbook(String file) {
        return openStreamingWorkbook(new File(file), null, BUFFER_ROW, BUFFER_SIZE);
    }

    public static Workbook openStreamingWorkbook(File file) {
        return openStreamingWorkbook(file, null, BUFFER_ROW, BUFFER_SIZE);
    }

    public static Workbook openStreamingWorkbook(InputStream in) {
        return openStreamingWorkbook(in, null, BUFFER_ROW, BUFFER_SIZE);
    }

    public static Workbook openStreamingWorkbook(String file, String password) {
        return openStreamingWorkbook(new File(file), password, BUFFER_ROW, BUFFER_SIZE);
    }

    public static Workbook openStreamingWorkbook(File file, String password) {
        return openStreamingWorkbook(file, password, BUFFER_ROW, BUFFER_SIZE);
    }

    public static Workbook openStreamingWorkbook(InputStream in, String password) {
        return openStreamingWorkbook(in, password, BUFFER_ROW, BUFFER_SIZE);
    }

    public static Workbook openStreamingWorkbook(String file, int rowCache) {
        return openStreamingWorkbook(new File(file), null, rowCache, BUFFER_SIZE);
    }

    public static Workbook openStreamingWorkbook(File file, int rowCache) {
        return openStreamingWorkbook(file, null, rowCache, BUFFER_SIZE);
    }

    public static Workbook openStreamingWorkbook(InputStream in, int rowCache) {
        return openStreamingWorkbook(in, null, rowCache, BUFFER_SIZE);
    }

    public static Workbook openStreamingWorkbook(String file, String password, int rowCache) {
        return openStreamingWorkbook(new File(file), password, rowCache, BUFFER_SIZE);
    }

    public static Workbook openStreamingWorkbook(File file, String password, int rowCache) {
        return openStreamingWorkbook(file, password, rowCache, BUFFER_SIZE);
    }

    public static Workbook openStreamingWorkbook(InputStream in, String password, int rowCache) {
        return openStreamingWorkbook(in, password, rowCache, BUFFER_SIZE);
    }

    public static Workbook openStreamingWorkbook(String file, int rowCache, int bufferSize) {
        return openStreamingWorkbook(new File(file), null, rowCache, bufferSize);
    }

    public static Workbook openStreamingWorkbook(File file, int rowCache, int bufferSize) {
        return openStreamingWorkbook(file, null, rowCache, bufferSize);
    }

    public static Workbook openStreamingWorkbook(InputStream in, int rowCache, int bufferSize) {
        return openStreamingWorkbook(in, null, rowCache, bufferSize);
    }

    public static Workbook openStreamingWorkbook(String file, String password, int rowCache, int bufferSize) {
        return openStreamingWorkbook(new File(file), password, rowCache, bufferSize);
    }

    public static Workbook openStreamingWorkbook(File file, String password, int rowCache, int bufferSize) {
        if (file.getName().toLowerCase().endsWith("xls")) {
            throw Exceptions.parse("Cannot using streaming open 2003 workbook");
        }
        return openStreamingWorkbook(Files1.openInputStreamSafe(file), password, rowCache, bufferSize);
    }

    /**
     * 获取流式workbook
     *
     * @param in         文件流
     * @param password   密码
     * @param rowCache   row缓存
     * @param bufferSize 缓冲区大小
     * @return StreamingWorkbook
     */
    public static Workbook openStreamingWorkbook(InputStream in, String password, int rowCache, int bufferSize) {
        try {
            return StreamingReader.builder()
                    .password(password)
                    .rowCacheSize(rowCache)
                    .bufferSize(bufferSize)
                    .open(in);
        } catch (Exception e) {
            throw Exceptions.parse("cannot open streaming excel file", e);
        }
    }

    // --------------- check ---------------

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
    public static boolean isStreamingWorkbook(Class<?> clazz) {
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
    public static boolean isStreamingSheet(Class<?> clazz) {
        return StreamingSheet.class.equals(clazz);
    }

}
