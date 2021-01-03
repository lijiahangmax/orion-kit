package com.orion.excel;

import com.monitorjbl.xlsx.StreamingReader;
import com.monitorjbl.xlsx.impl.StreamingSheet;
import com.monitorjbl.xlsx.impl.StreamingWorkbook;
import com.orion.excel.copy.CopySheet;
import com.orion.excel.option.*;
import com.orion.excel.picture.PictureParser;
import com.orion.excel.style.FontStream;
import com.orion.excel.style.PrintStream;
import com.orion.excel.style.StyleStream;
import com.orion.excel.type.ExcelFieldType;
import com.orion.excel.type.ExcelLinkType;
import com.orion.excel.type.ExcelPictureType;
import com.orion.excel.type.ExcelReadType;
import com.orion.utils.*;
import com.orion.utils.convert.Converts;
import com.orion.utils.io.FileReaders;
import com.orion.utils.io.FileTypes;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import com.orion.utils.math.BigDecimals;
import com.orion.utils.time.Dates;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hpsf.Thumbnail;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTProperties;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Optional;

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
     * 通过类型获取值
     *
     * @param cell cell
     * @param type type 不包含picture
     * @param <T>  T
     * @return value
     */
    public static <T> T getCellValue(Cell cell, ExcelReadType type) {
        return getCellValue(cell, type, null);
    }

    /**
     * 通过类型获取值
     *
     * @param cell   cell
     * @param type   type 不包含picture
     * @param option option
     * @param <T>    T
     * @return value
     */
    @SuppressWarnings("unchecked")
    public static <T> T getCellValue(Cell cell, ExcelReadType type, CellOption option) {
        Object value;
        switch (type) {
            case DECIMAL:
                value = Excels.getCellDecimal(cell, null, option);
                break;
            case DATE:
                value = Excels.getCellDate(cell, null, option);
                break;
            case PHONE:
                value = Excels.getCellPhone(cell);
                break;
            case LINK_ADDRESS:
                value = Excels.getCellHyperUrl(cell);
                break;
            case COMMENT:
                value = Excels.getCellComment(cell);
                break;
            case PICTURE:
                value = null;
                break;
            case TEXT:
            default:
                value = Excels.getCellValue(cell);
                break;
        }
        return (T) value;
    }

    /**
     * 获取String的值
     *
     * @param cell cell
     * @return value
     */
    public static String getCellValue(Cell cell) {
        return getCellValue(cell, (CellType) null);
    }

    /**
     * 获取String的值
     *
     * @param cell cell
     * @param type type
     * @return value
     */
    private static String getCellValue(Cell cell, CellType type) {
        String value = Strings.EMPTY;
        if (cell == null) {
            return value;
        }
        if (type == null) {
            type = cell.getCellType();
        }
        switch (type) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 日期
                    value = Dates.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                } else {
                    // 纯数字
                    value = BigDecimal.valueOf(cell.getNumericCellValue()).toString();
                    String[] item = value.split("\\.");
                    if (item.length > 1 && Integer.parseInt(item[1]) == 0) {
                        // 整数
                        value = item[0];
                    }
                }
                break;
            case FORMULA:
                value = getCellValue(cell, cell.getCachedFormulaResultType());
                if ("NaN".equals(value)) {
                    value = cell.getStringCellValue();
                }
                break;
            case BOOLEAN:
                value = Strings.EMPTY + cell.getBooleanCellValue();
                break;
            case BLANK:
                break;
            case ERROR:
                FormulaError error = FormulaError.forInt(cell.getErrorCellValue());
                value = error == null ? Strings.EMPTY : error.getString();
                break;
            case STRING:
            default:
                value = cell.getStringCellValue();
        }
        return Objects1.def(value, Strings.EMPTY);
    }

    /**
     * 获取数字
     *
     * @param cell cell
     * @return 数字
     */
    public static BigDecimal getCellDecimal(Cell cell) {
        return getCellDecimal(cell, null, null);
    }

    /**
     * 获取数字
     *
     * @param cell   cell
     * @param option option
     * @return BigDecimal
     */
    public static BigDecimal getCellDecimal(Cell cell, CellOption option) {
        return getCellDecimal(cell, null, option);
    }

    /**
     * 获取数字
     *
     * @param cell   cell
     * @param type   type
     * @param option option
     * @return BigDecimal
     */
    private static BigDecimal getCellDecimal(Cell cell, CellType type, CellOption option) {
        if (cell == null) {
            return null;
        }
        if (type == null) {
            type = cell.getCellType();
        }
        switch (type) {
            case FORMULA:
                return getCellDecimal(cell, cell.getCachedFormulaResultType(), option);
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING:
                if (option != null && !Strings.isEmpty(option.getFormat())) {
                    return BigDecimals.parse(cell.getStringCellValue(), option.getFormat());
                } else {
                    return BigDecimals.toBigDecimal(cell.getStringCellValue());
                }
            default:
                return null;
        }
    }

    /**
     * 获取时间
     *
     * @param cell cell
     * @return 时间
     */
    public static Date getCellDate(Cell cell) {
        return getCellDate(cell, null, null);
    }

    /**
     * 获取时间
     *
     * @param cell   cell
     * @param option option
     * @return 时间
     */
    public static Date getCellDate(Cell cell, CellOption option) {
        return getCellDate(cell, null, option);
    }

    /**
     * 获取时间
     *
     * @param cell   cell
     * @param type   type
     * @param option option
     * @return 时间
     */
    private static Date getCellDate(Cell cell, CellType type, CellOption option) {
        if (cell == null) {
            return null;
        }
        if (type == null) {
            type = cell.getCellType();
        }
        switch (type) {
            case FORMULA:
                if (!DateUtil.isCellDateFormatted(cell)) {
                    // 非日期公式
                    return getCellDate(cell, cell.getCachedFormulaResultType(), option);
                }
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                double value = cell.getNumericCellValue();
                if (DateUtil.isValidExcelDate(value)) {
                    // 非日期公式
                    return DateUtil.getJavaDate(cell.getNumericCellValue());
                }
            default:
                String o = cell.getStringCellValue();
                if (option != null && !Strings.isEmpty(option.getFormat())) {
                    return Dates.parse(o, option.getFormat());
                } else {
                    return Dates.date(o);
                }
        }
    }

    /**
     * 获取手机号
     *
     * @param cell cell
     * @return 手机号
     */
    public static String getCellPhone(Cell cell) {
        return getCellPhone(cell, null);
    }

    /**
     * 获取手机号
     *
     * @param cell cell
     * @return 手机号
     */
    private static String getCellPhone(Cell cell, CellType type) {
        if (cell == null) {
            return Strings.EMPTY;
        }
        if (type == null) {
            type = cell.getCellType();
        }
        switch (type) {
            case FORMULA:
                return getCellPhone(cell, cell.getCachedFormulaResultType());
            case NUMERIC:
                return DF.format(cell.getNumericCellValue());
            case STRING:
                return DF.format(Double.parseDouble(cell.toString()));
            default:
                return cell.toString();
        }
    }

    /**
     * 获取批注
     *
     * @param cell cell
     * @return 批注
     */
    public static String getCellComment(Cell cell) {
        if (cell == null) {
            return Strings.EMPTY;
        }
        return Optional.ofNullable(cell.getCellComment())
                .map(Comment::getString)
                .map(RichTextString::getString)
                .orElse(Strings.EMPTY);
    }

    /**
     * 获取超链接
     *
     * @param cell cell
     * @return 超链接url
     */
    public static String getCellHyperUrl(Cell cell) {
        if (cell == null) {
            return Strings.EMPTY;
        }
        return Optional.ofNullable(cell.getHyperlink())
                .map(Hyperlink::getAddress)
                .orElse(Strings.EMPTY);
    }

    /**
     * 获取行 (可能为null)
     *
     * @param sheet sheet
     * @param index index
     * @return row
     */
    public static Row getRow(Sheet sheet, int index) {
        return sheet.getRow(index);
    }

    /**
     * 获取行样式 (可能为null)
     *
     * @param sheet sheet
     * @param index index
     * @return row
     */
    public static CellStyle getRowStyle(Sheet sheet, int index) {
        return Optional.ofNullable(sheet.getRow(index)).map(Row::getRowStyle).get();
    }

    /**
     * 获取单元格  (可能为null)
     *
     * @param sheet  sheet
     * @param row    rowIndex
     * @param column columnIndex
     * @return cell
     */
    public static Cell getCell(Sheet sheet, int row, int column) {
        return SheetUtil.getCell(sheet, row, column);
    }

    /**
     * 获取单元格样式 (可能为null)
     *
     * @param sheet  sheet
     * @param row    rowIndex
     * @param column columnIndex
     * @return cell
     */
    public static CellStyle getCellStyle(Sheet sheet, int row, int column) {
        return Optional.ofNullable(SheetUtil.getCell(sheet, row, column)).map(Cell::getCellStyle).get();
    }

    /**
     * 获取单元格 如果是合并则获取合并后的
     *
     * @param sheet  sheet
     * @param row    rowIndex
     * @param column columnIndex
     * @return cell
     */
    public static Cell getCellMerge(Sheet sheet, int row, int column) {
        return SheetUtil.getCellWithMerges(sheet, row, column);
    }

    /**
     * 获取字段类型
     *
     * @param o object
     * @see ExcelFieldType
     */
    public static ExcelFieldType getFieldType(Object o) {
        if (o == null) {
            return ExcelFieldType.TEXT;
        }
        return ExcelFieldType.of(o.getClass());
    }

    /**
     * 获取字段类型
     *
     * @param clazz clazz
     * @see ExcelFieldType
     */
    public static ExcelFieldType getFieldType(Class<?> clazz) {
        return ExcelFieldType.of(clazz);
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
     * @param cell   cell
     * @param value  value
     * @param type   type
     * @param option 参数
     */
    public static void setCellValue(Cell cell, Object value, ExcelFieldType type, CellOption option) {
        if (value == null) {
            return;
        }
        if (type == null) {
            cell.setCellValue(Objects1.toString(value));
        } else if (type.equals(ExcelFieldType.AUTO)) {
            setCellValue(cell, value, ExcelFieldType.of(value.getClass()), option);
        } else {
            switch (type) {
                case NUMBER:
                    cell.setCellValue(Converts.toDouble(value));
                    break;
                case DATE:
                    cell.setCellValue(Converts.toDate(value));
                    break;
                case DATE_FORMAT:
                    if (option != null && !Strings.isEmpty(option.getFormat())) {
                        cell.setCellValue(Dates.format(Converts.toDate(value), option.getFormat()));
                    } else {
                        cell.setCellValue(Dates.format(Converts.toDate(value)));
                    }
                    break;
                case DECIMAL_FORMAT:
                    if (option != null && !Strings.isEmpty(option.getFormat())) {
                        cell.setCellValue(BigDecimals.format(value, option.getFormat()));
                    } else {
                        cell.setCellValue(Objects1.toString(value));
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
     * 获取最后一行的位置 (索引+1 但有可能行为空)
     *
     * @param sheet sheet
     * @return 总行数
     */
    public static int getRowCount(Sheet sheet) {
        return sheet.getLastRowNum() + 1;
    }

    /**
     * 获取实际总行数 (不全)
     *
     * @param sheet sheet
     * @return 总行数
     */
    public static int getPhysicalRowCount(Sheet sheet) {
        return sheet.getPhysicalNumberOfRows() + 1;
    }

    /**
     * 获取列数
     *
     * @param sheet sheet
     * @return 列数
     */
    public static int getColumnCount(Sheet sheet) {
        return getColumnCount(sheet, 0);
    }

    /**
     * 获取列数
     *
     * @param sheet  sheet
     * @param rowNum 行索引
     * @return 列数
     */
    public static int getColumnCount(Sheet sheet, int rowNum) {
        Row row = sheet.getRow(rowNum);
        if (null != row) {
            return row.getLastCellNum();
        }
        return 0;
    }

    /**
     * 调色板 主要用与 HSSFColor
     *
     * @param index index
     * @param rgb   color
     * @return colorIndex
     */
    public static short paletteColor(Workbook workbook, short index, String rgb) {
        return paletteColor(workbook, index, Colors.toRgb(rgb));
    }

    /**
     * 调色板 主要用与 HSSFColor
     *
     * @param index index
     * @param rgb   color
     * @return colorIndex
     */
    public static short paletteColor(Workbook workbook, short index, byte[] rgb) {
        if (workbook instanceof HSSFWorkbook) {
            HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
            if (rgb != null) {
                HSSFColor color = palette.findColor(rgb[0], rgb[1], rgb[2]);
                if (color == null) {
                    palette.setColorAtIndex(index, rgb[0], rgb[1], rgb[2]);
                    return index;
                } else {
                    return color.getIndex();
                }
            }
            return 0;
        } else {
            return new XSSFColor(rgb, null).getIndex();
        }
    }

    // --------------- option ---------------

    /**
     * 创建图片解析器
     *
     * @param sheet sheet
     * @return PictureParser
     */
    public static PictureParser createPictureParser(Sheet sheet) {
        return new PictureParser(sheet.getWorkbook(), sheet);
    }

    /**
     * 创建图片解析器
     *
     * @param workbook workbook
     * @param index    sheetIndex
     * @return PictureParser
     */
    public static PictureParser createPictureParser(Workbook workbook, int index) {
        return new PictureParser(workbook, workbook.getSheetAt(index));
    }

    /**
     * 创建图片解析器
     *
     * @param workbook workbook
     * @param sheet    sheet
     * @return PictureParser
     */
    public static PictureParser createPictureParser(Workbook workbook, Sheet sheet) {
        return new PictureParser(workbook, sheet);
    }

    /**
     * 获取缩略图
     *
     * @param workbook workbook
     * @return 缩略图
     */
    public static byte[] getThumbnail(Workbook workbook) {
        if (workbook instanceof HSSFWorkbook) {
            return Optional.ofNullable(((HSSFWorkbook) workbook).getSummaryInformation())
                    .map(SummaryInformation::getThumbnailThumbnail)
                    .map(Thumbnail::getThumbnail)
                    .orElse(null);
        } else if (workbook instanceof XSSFWorkbook) {
            try {
                InputStream in = ((XSSFWorkbook) workbook).getProperties().getThumbnailImage();
                if (in != null) {
                    return Streams.toByteArray(in);
                }
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            }
        } else if (workbook instanceof SXSSFWorkbook) {
            return getThumbnail(((SXSSFWorkbook) workbook).getXSSFWorkbook());
        }
        return null;
    }

    /**
     * 设置缩略图
     *
     * @param workbook  workbook
     * @param thumbnail 缩略图
     */
    public static void setThumbnail(Workbook workbook, File thumbnail) {
        try {
            setThumbnail(workbook, Streams.toByteArray(Files1.openInputStream(thumbnail)), thumbnail.getName());
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 设置缩略图
     *
     * @param workbook  workbook
     * @param thumbnail 缩略图
     */
    public static void setThumbnail(Workbook workbook, String thumbnail) {
        try {
            setThumbnail(workbook, Streams.toByteArray(Files1.openInputStream(thumbnail)), Files1.getFileName(thumbnail));
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 设置缩略图
     *
     * @param workbook  workbook
     * @param thumbnail 缩略图
     */
    public static void setThumbnail(Workbook workbook, InputStream thumbnail) {
        try {
            setThumbnail(workbook, Streams.toByteArray(thumbnail), null);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 设置缩略图
     *
     * @param workbook  workbook
     * @param thumbnail 缩略图
     */
    public static void setThumbnail(Workbook workbook, byte[] thumbnail) {
        setThumbnail(workbook, thumbnail, null);
    }

    /**
     * 设置缩略图
     *
     * @param workbook  workbook
     * @param thumbnail 缩略图
     * @param fileName  文件名
     */
    public static void setThumbnail(Workbook workbook, InputStream thumbnail, String fileName) {
        try {
            setThumbnail(workbook, Streams.toByteArray(thumbnail), fileName);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 设置缩略图
     *
     * @param workbook  workbook
     * @param thumbnail 缩略图
     * @param fileName  文件名
     * @see org.apache.poi.hpsf.Property#write(OutputStream, int) 519
     * @deprecated XLS 设置写入会报错
     */
    public static void setThumbnail(Workbook workbook, byte[] thumbnail, String fileName) {
        if (thumbnail == null) {
            return;
        }
        if (workbook instanceof HSSFWorkbook) {
            HSSFWorkbook wb = (HSSFWorkbook) workbook;
            if (wb.getSummaryInformation() == null) {
                wb.createInformationProperties();
            }
            wb.getSummaryInformation().setThumbnail(thumbnail);
        } else if (workbook instanceof XSSFWorkbook) {
            XSSFWorkbook wb = (XSSFWorkbook) workbook;
            try {
                if (fileName == null) {
                    String fileType = FileTypes.getFileType(thumbnail);
                    if ("jpg".equals(fileType)) {
                        fileName = "tmp.jpg";
                    } else {
                        fileName = "tmp.png";
                    }
                }
                wb.getProperties().setThumbnail(fileName, Streams.toInputStream(thumbnail));
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            }
        } else if (workbook instanceof SXSSFWorkbook) {
            setThumbnail(((SXSSFWorkbook) workbook).getXSSFWorkbook(), thumbnail, fileName);
        }
    }

    /**
     * 获取属性
     *
     * @param workbook workbook
     * @return 属性
     */
    public static PropertiesOption getProperties(Workbook workbook) {
        PropertiesOption option = new PropertiesOption();
        if (workbook instanceof HSSFWorkbook) {
            SummaryInformation i = ((HSSFWorkbook) workbook).getSummaryInformation();
            DocumentSummaryInformation di = ((HSSFWorkbook) workbook).getDocumentSummaryInformation();
            if (i == null) {
                return option;
            }
            option.setAuthor(i.getAuthor());
            option.setTitle(i.getTitle());
            option.setSubject(i.getSubject());
            option.setKeywords(i.getKeywords());
            option.setDescription(i.getComments());
            option.setCategory(di.getCategory());
            option.setModifiedUser(i.getLastAuthor());
            option.setContentType(di.getContentType());
            option.setContentStatus(di.getContentStatus());
            option.setCreated(i.getCreateDateTime());
            option.setManager(di.getManager());
            option.setCompany(di.getCompany());
            option.setApplication(i.getApplicationName());
            if (i.getEditTime() != 0) {
                option.setModified(new Date(i.getEditTime()));
            }
        } else if (workbook instanceof XSSFWorkbook) {
            POIXMLProperties.CoreProperties p = ((XSSFWorkbook) workbook).getProperties().getCoreProperties();
            CTProperties cp = ((XSSFWorkbook) workbook).getProperties().getExtendedProperties().getUnderlyingProperties();
            option.setAuthor(p.getCreator());
            option.setTitle(p.getTitle());
            option.setSubject(p.getSubject());
            option.setKeywords(p.getKeywords());
            option.setDescription(p.getDescription());
            option.setRevision(p.getRevision());
            option.setCategory(p.getCategory());
            option.setModifiedUser(p.getLastModifiedByUser());
            option.setContentType(p.getContentType());
            option.setContentStatus(p.getContentStatus());
            option.setIdentifier(p.getIdentifier());
            option.setCreated(option.getCreated());
            option.setModified(option.getModified());
            option.setManager(cp.getManager());
            option.setCompany(cp.getCompany());
            option.setApplication(cp.getApplication());
        } else if (workbook instanceof SXSSFWorkbook) {
            return getProperties(((SXSSFWorkbook) workbook).getXSSFWorkbook());
        }
        return option;
    }

    /**
     * 设置属性
     *
     * @param workbook workbook
     * @param option   属性
     */
    public static void setProperties(Workbook workbook, PropertiesOption option) {
        if (option == null) {
            return;
        }
        if (workbook instanceof HSSFWorkbook) {
            // 非中文可能不可设置
            if (((HSSFWorkbook) workbook).getSummaryInformation() == null) {
                ((HSSFWorkbook) workbook).createInformationProperties();
            }
            SummaryInformation i = ((HSSFWorkbook) workbook).getSummaryInformation();
            DocumentSummaryInformation di = ((HSSFWorkbook) workbook).getDocumentSummaryInformation();
            Optional.ofNullable(option.getAuthor()).ifPresent(i::setAuthor);
            Optional.ofNullable(option.getTitle()).ifPresent(i::setTitle);
            Optional.ofNullable(option.getSubject()).ifPresent(i::setSubject);
            Optional.ofNullable(option.getKeywords()).ifPresent(i::setKeywords);
            // revision
            Optional.ofNullable(option.getDescription()).ifPresent(i::setComments);
            Optional.ofNullable(option.getCategory()).ifPresent(di::setCategory);
            Optional.ofNullable(option.getModifiedUser()).ifPresent(i::setLastAuthor);
            Optional.ofNullable(option.getContentType()).ifPresent(di::setContentType);
            Optional.ofNullable(option.getContentStatus()).ifPresent(di::setContentStatus);
            // identifier
            Optional.ofNullable(option.getCreated()).ifPresent(i::setCreateDateTime);
            Optional.ofNullable(option.getModified()).ifPresent(m -> i.setEditTime(m.getTime()));
            Optional.ofNullable(option.getManager()).ifPresent(di::setManager);
            Optional.ofNullable(option.getCompany()).ifPresent(di::setCompany);
            Optional.ofNullable(option.getApplication()).ifPresent(i::setApplicationName);
        } else if (workbook instanceof XSSFWorkbook) {
            POIXMLProperties.CoreProperties p = ((XSSFWorkbook) workbook).getProperties().getCoreProperties();
            CTProperties cp = ((XSSFWorkbook) workbook).getProperties().getExtendedProperties().getUnderlyingProperties();
            p.setCreator(option.getAuthor());
            p.setTitle(option.getTitle());
            p.setSubjectProperty(option.getSubject());
            p.setKeywords(option.getKeywords());
            p.setRevision(option.getRevision());
            p.setDescription(option.getDescription());
            p.setCategory(option.getCategory());
            p.setLastModifiedByUser(option.getModifiedUser());
            p.setContentType(option.getContentType());
            p.setContentStatus(option.getContentStatus());
            p.setIdentifier(option.getIdentifier());
            p.setCreated(Optional.ofNullable(option.getCreated()));
            p.setModified(Optional.ofNullable(option.getModified()));
            cp.setManager(option.getManager());
            cp.setCompany(option.getCompany());
            cp.setApplication(option.getApplication());
        } else if (workbook instanceof SXSSFWorkbook) {
            setProperties(((SXSSFWorkbook) workbook).getXSSFWorkbook(), option);
        }
    }

    /**
     * 获取页眉
     *
     * @param sheet sheet
     * @return 页眉
     */
    public static HeaderOption getHeader(Sheet sheet) {
        Header header = sheet.getHeader();
        HeaderOption option = new HeaderOption();
        option.setLeft(header.getLeft());
        option.setCenter(header.getCenter());
        option.setRight(header.getRight());
        return option;
    }

    /**
     * 设置页眉
     *
     * @param sheet  sheet
     * @param option 页眉
     */
    public static void setHeader(Sheet sheet, HeaderOption option) {
        Header header = sheet.getHeader();
        header.setLeft(option.getLeft());
        header.setCenter(option.getCenter());
        header.setRight(option.getRight());
    }

    /**
     * 获取页脚
     *
     * @param sheet sheet
     * @return 页脚
     */
    public static FooterOption getFooter(Sheet sheet) {
        Footer footer = sheet.getFooter();
        FooterOption option = new FooterOption();
        option.setLeft(footer.getLeft());
        option.setCenter(footer.getCenter());
        option.setRight(footer.getRight());
        return option;
    }

    /**
     * 获取页脚
     *
     * @param sheet  sheet
     * @param option 页脚
     */
    public static void setFooter(Sheet sheet, FooterOption option) {
        Footer footer = sheet.getFooter();
        footer.setLeft(option.getLeft());
        footer.setCenter(option.getCenter());
        footer.setRight(option.getRight());
    }

    /**
     * 创建批注
     *
     * @param sheet   sheet
     * @param column  列索引
     * @param row     行索引
     * @param comment 批注
     * @return 批注
     */
    public static Comment createComment(Sheet sheet, int column, int row, String comment) {
        return createComment(sheet, column, row, new CommentOption(comment));
    }

    /**
     * 创建批注
     *
     * @param sheet  sheet
     * @param column 列索引
     * @param row    行索引
     * @param option 批注
     * @return 批注
     */
    public static Comment createComment(Sheet sheet, int column, int row, CommentOption option) {
        if (option == null) {
            return null;
        }
        Drawing<?> d = sheet.createDrawingPatriarch();
        ClientAnchor anchor = d.createAnchor(0, 0, 0, 0, column, row, 0, 0);
        Comment comment = d.createCellComment(anchor);
        Optional.ofNullable(option.getAuthor()).ifPresent(comment::setAuthor);
        comment.setVisible(option.isVisible());
        if (comment instanceof HSSFComment) {
            comment.setString(new HSSFRichTextString(option.getComment()));
        } else if (comment instanceof XSSFComment) {
            comment.setString(new XSSFRichTextString(option.getComment()));
        }
        return comment;
    }

    /**
     * 解析样式
     *
     * @param workbook workbook
     * @param option   option
     */
    public static CellStyle parseStyle(Workbook workbook, ExportFieldOption option) {
        return StyleStream.parseStyle(workbook, option);
    }

    /**
     * 解析列样式
     *
     * @param workbook workbook
     * @param option   option
     */
    public static CellStyle parseColumnStyle(Workbook workbook, ExportFieldOption option) {
        return StyleStream.parseColumnStyle(workbook, option);
    }

    /**
     * 解析标题样式
     *
     * @param workbook workbook
     * @param option   option
     */
    public static CellStyle parseTitleStyle(Workbook workbook, TitleOption option) {
        return StyleStream.parseTitleStyle(workbook, option);
    }

    /**
     * 解析字体
     *
     * @param workbook workbook
     * @param option   option
     * @return font
     */
    public static Font parseFont(Workbook workbook, FontOption option) {
        return FontStream.parseFont(workbook, option);
    }

    /**
     * 解析打印
     *
     * @param sheet  sheet
     * @param option option
     * @return PrintSetup
     */
    public static PrintSetup parsePrint(Sheet sheet, PrintOption option) {
        return PrintStream.parsePrint(sheet, option);
    }

    /**
     * 设置超链接
     *
     * @param workbook workbook
     * @param cell     cell
     * @param linkType 链接类型
     * @param address  链接地址
     * @param textType 文本类型
     * @param text     文本
     */
    public static void setLink(Workbook workbook, Cell cell, ExcelLinkType linkType, String address, ExcelFieldType textType, Object text) {
        setLink(workbook, cell, linkType, address, textType, null, text);
    }

    /**
     * 设置超链接
     *
     * @param workbook workbook
     * @param cell     cell
     * @param linkType 链接类型
     * @param address  链接地址
     * @param textType 文本类型
     * @param text     文本
     */
    public static void setLink(Workbook workbook, Cell cell, ExcelLinkType linkType, String address, ExcelFieldType textType, CellOption option, Object text) {
        if (address == null) {
            return;
        }
        Hyperlink link;
        switch (linkType) {
            case LINK_URL:
                link = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
                break;
            case LINK_DOC:
                link = workbook.getCreationHelper().createHyperlink(HyperlinkType.DOCUMENT);
                break;
            case LINK_EMAIL:
                link = workbook.getCreationHelper().createHyperlink(HyperlinkType.EMAIL);
                break;
            case LINK_FILE:
                link = workbook.getCreationHelper().createHyperlink(HyperlinkType.FILE);
                break;
            default:
                link = null;
        }
        setCellValue(cell, text, textType, option);
        if (link == null) {
            return;
        }
        if (linkType.equals(ExcelLinkType.LINK_FILE) && !address.startsWith("file:///")) {
            // 文件
            address = "file:///" + Files1.getPath(address);
        } else if (linkType.equals(ExcelLinkType.LINK_EMAIL) && !address.startsWith("mailto:")) {
            // 邮件
            address = "mailto:" + address;
        }
        link.setAddress(Strings.def(address));
        cell.setHyperlink(link);
    }

    /**
     * 添加图片
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param image       图片
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @return Picture
     */
    public static Picture setPicture(Workbook workbook, Sheet sheet, File image, int rowIndex, int columnIndex) {
        return setPicture(workbook, sheet, FileReaders.readFast(image), rowIndex, columnIndex, null, null);
    }

    /**
     * 添加图片
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param image       图片
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @param fileName    文件名
     * @return Picture
     */
    public static Picture setPicture(Workbook workbook, Sheet sheet, File image, int rowIndex, int columnIndex, String fileName) {
        return setPicture(workbook, sheet, FileReaders.readFast(image), rowIndex, columnIndex, fileName, null);
    }

    /**
     * 添加图片
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param image       图片
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @param type        文件类型
     * @return Picture
     */
    public static Picture setPicture(Workbook workbook, Sheet sheet, File image, int rowIndex, int columnIndex, ExcelPictureType type) {
        return setPicture(workbook, sheet, FileReaders.readFast(image), rowIndex, columnIndex, null, type);
    }

    /**
     * 添加图片
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param image       图片
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @return Picture
     */
    public static Picture setPicture(Workbook workbook, Sheet sheet, String image, int rowIndex, int columnIndex) {
        return setPicture(workbook, sheet, FileReaders.readFast(image), rowIndex, columnIndex, null, null);
    }

    /**
     * 添加图片
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param image       图片
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @param fileName    文件名
     * @return Picture
     */
    public static Picture setPicture(Workbook workbook, Sheet sheet, String image, int rowIndex, int columnIndex, String fileName) {
        return setPicture(workbook, sheet, FileReaders.readFast(image), rowIndex, columnIndex, fileName, null);
    }

    /**
     * 添加图片
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param image       图片
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @param type        文件类型
     * @return Picture
     */
    public static Picture setPicture(Workbook workbook, Sheet sheet, String image, int rowIndex, int columnIndex, ExcelPictureType type) {
        return setPicture(workbook, sheet, FileReaders.readFast(image), rowIndex, columnIndex, null, type);
    }

    /**
     * 添加图片
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param image       图片
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @return Picture
     */
    public static Picture setPicture(Workbook workbook, Sheet sheet, InputStream image, int rowIndex, int columnIndex) {
        try {
            return setPicture(workbook, sheet, Streams.toByteArray(image), rowIndex, columnIndex, null, null);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 添加图片
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param image       图片
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @param fileName    文件名
     * @return Picture
     */
    public static Picture setPicture(Workbook workbook, Sheet sheet, InputStream image, int rowIndex, int columnIndex, String fileName) {
        try {
            return setPicture(workbook, sheet, Streams.toByteArray(image), rowIndex, columnIndex, fileName, null);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 添加图片
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param image       图片
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @param type        文件类型
     * @return Picture
     */
    public static Picture setPicture(Workbook workbook, Sheet sheet, InputStream image, int rowIndex, int columnIndex, ExcelPictureType type) {
        try {
            return setPicture(workbook, sheet, Streams.toByteArray(image), rowIndex, columnIndex, null, type);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 添加图片
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param image       图片
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @return Picture
     */
    public static Picture setPicture(Workbook workbook, Sheet sheet, byte[] image, int rowIndex, int columnIndex) {
        return setPicture(workbook, sheet, image, rowIndex, columnIndex, null, null);
    }

    /**
     * 添加图片
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param image       图片
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @param fileName    文件名
     * @return Picture
     */
    public static Picture setPicture(Workbook workbook, Sheet sheet, byte[] image, int rowIndex, int columnIndex, String fileName) {
        return setPicture(workbook, sheet, image, rowIndex, columnIndex, fileName, null);
    }

    /**
     * 添加图片
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param image       图片
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @param type        文件类型
     * @return Picture
     */
    public static Picture setPicture(Workbook workbook, Sheet sheet, byte[] image, int rowIndex, int columnIndex, ExcelPictureType type) {
        return setPicture(workbook, sheet, image, rowIndex, columnIndex, null, type);
    }

    /**
     * 添加图片
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param image       图片
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @param fileName    文件名
     * @param type        文件类型
     * @return Picture
     */
    public static Picture setPicture(Workbook workbook, Sheet sheet, byte[] image, int rowIndex, int columnIndex, String fileName, ExcelPictureType type) {
        if (type == null || type.equals(ExcelPictureType.AUTO)) {
            if (Strings.isEmpty(fileName)) {
                type = ExcelPictureType.PNG;
            } else {
                type = ExcelPictureType.of(Files1.getFileName(fileName));
            }
        }
        int pictureIndex;
        if (workbook instanceof HSSFWorkbook) {
            int type1 = type.getType1();
            if (type1 == -1) {
                type1 = ExcelPictureType.PNG.getType1();
            }
            pictureIndex = workbook.addPicture(image, type1);
        } else {
            pictureIndex = workbook.addPicture(image, type.getType2());
        }
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor;
        if (workbook instanceof HSSFWorkbook) {
            anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) columnIndex, rowIndex, (short) (columnIndex + 1), rowIndex + 1);
        } else {
            anchor = new XSSFClientAnchor(0, 0, 0, 0, columnIndex, rowIndex, columnIndex + 1, rowIndex + 1);
        }
        return drawing.createPicture(anchor, pictureIndex);
    }

    // --------------- row cell ---------------

    /**
     * 冻结首行
     *
     * @param sheet sheet
     */
    public static void freezeFirstRow(Sheet sheet) {
        sheet.createFreezePane(0, 1);
    }

    /**
     * 冻结行
     *
     * @param sheet   sheet
     * @param lastRow 结束行索引 不包含
     */
    public static void freezeRow(Sheet sheet, int lastRow) {
        sheet.createFreezePane(0, lastRow);
    }

    /**
     * 筛选首行
     *
     * @param sheet sheet
     */
    public static void filterFirstRow(Sheet sheet) {
        Row row = sheet.getRow(0);
        sheet.setAutoFilter(new CellRangeAddress(1, 2, 0, row.getLastCellNum() - 1));
    }

    /**
     * 筛选首行
     *
     * @param sheet      sheet
     * @param lastColumn 结束列索引
     */
    public static void filterFirstRow(Sheet sheet, int lastColumn) {
        sheet.setAutoFilter(new CellRangeAddress(0, 1, 0, lastColumn));
    }

    /**
     * 筛选首行
     *
     * @param sheet       sheet
     * @param firstColumn 开始列索引
     * @param lastColumn  结束列索引
     */
    public static void filterFirstRow(Sheet sheet, int firstColumn, int lastColumn) {
        sheet.setAutoFilter(new CellRangeAddress(0, 1, firstColumn, lastColumn));
    }

    /**
     * 筛选行
     *
     * @param sheet    sheet
     * @param rowIndex 行索引
     */
    public static void filterRow(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(0);
        sheet.setAutoFilter(new CellRangeAddress(rowIndex, rowIndex + 1, 0, row.getLastCellNum() - 1));
    }

    /**
     * 筛选行
     *
     * @param sheet      sheet
     * @param rowIndex   行索引
     * @param lastColumn 结束列索引
     */
    public static void filterRow(Sheet sheet, int rowIndex, int lastColumn) {
        sheet.setAutoFilter(new CellRangeAddress(rowIndex, rowIndex + 1, 0, lastColumn));
    }

    /**
     * 筛选行
     *
     * @param sheet       sheet
     * @param rowIndex    行索引
     * @param firstColumn 开始列索引
     * @param lastColumn  结束列索引
     */
    public static void filterRow(Sheet sheet, int rowIndex, int firstColumn, int lastColumn) {
        sheet.setAutoFilter(new CellRangeAddress(rowIndex, rowIndex + 1, firstColumn, lastColumn));
    }

    /**
     * 合并单元格
     *
     * @param row         合并行
     * @param firstColumn 合并开始单元格
     * @param lastColumn  合并结束单元格
     * @return merge
     */
    public static CellRangeAddress mergeCellRange(int row, int firstColumn, int lastColumn) {
        return new CellRangeAddress(row, row, firstColumn, lastColumn);
    }

    /**
     * 合并单元格
     *
     * @param firstRow    合并开始行
     * @param lastRow     合并结束行
     * @param firstColumn 合并开始单元格
     * @param lastColumn  合并结束单元格
     * @return merge
     */
    public static CellRangeAddress mergeCellRange(int firstRow, int lastRow, int firstColumn, int lastColumn) {
        return new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn);
    }

    /**
     * 合并单元格
     *
     * @param sheet       sheet
     * @param row         合并行
     * @param firstColumn 合并开始单元格
     * @param lastColumn  合并结束单元格
     */
    public static void mergeCell(Sheet sheet, int row, int firstColumn, int lastColumn) {
        mergeCell(sheet, new CellRangeAddress(row, row, firstColumn, lastColumn));
    }

    /**
     * 合并单元格
     *
     * @param sheet       sheet
     * @param firstRow    合并开始行
     * @param lastRow     合并结束行
     * @param firstColumn 合并开始单元格
     * @param lastColumn  合并结束单元格
     */
    public static void mergeCell(Sheet sheet, int firstRow, int lastRow, int firstColumn, int lastColumn) {
        mergeCell(sheet, new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn));
    }

    /**
     * 合并单元格
     *
     * @param sheet  sheet
     * @param region region
     */
    public static void mergeCell(Sheet sheet, CellRangeAddress region) {
        sheet.addMergedRegion(region);
    }

    /**
     * 合并单元格边框
     *
     * @param sheet       sheet
     * @param borderCode  边框
     * @param colorIndex  颜色
     * @param row         合并行
     * @param firstColumn 合并开始单元格
     * @param lastColumn  合并结束单元格
     */
    public static void mergeCellBorder(Sheet sheet, int borderCode, int colorIndex, int row, int firstColumn, int lastColumn) {
        mergeCellBorder(sheet, borderCode, colorIndex, new CellRangeAddress(row, row, firstColumn, lastColumn));
    }

    /**
     * 合并单元格边框
     *
     * @param sheet       sheet
     * @param borderCode  边框
     * @param firstRow    合并开始行
     * @param lastRow     合并结束行
     * @param firstColumn 合并开始单元格
     * @param lastColumn  合并结束单元格
     */
    public static void mergeCellBorder(Sheet sheet, int borderCode, int colorIndex, int firstRow, int lastRow, int firstColumn, int lastColumn) {
        mergeCellBorder(sheet, borderCode, colorIndex, new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn));
    }

    /**
     * 合并单元格边框
     *
     * @param sheet      sheet
     * @param borderCode 边框
     * @param region     region
     */
    public static void mergeCellBorder(Sheet sheet, int borderCode, int colorIndex, CellRangeAddress region) {
        RegionUtil.setBorderTop(BorderStyle.valueOf((short) borderCode), region, sheet);
        RegionUtil.setBorderRight(BorderStyle.valueOf((short) borderCode), region, sheet);
        RegionUtil.setBorderBottom(BorderStyle.valueOf((short) borderCode), region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.valueOf((short) borderCode), region, sheet);
        RegionUtil.setTopBorderColor(colorIndex, region, sheet);
        RegionUtil.setLeftBorderColor(colorIndex, region, sheet);
        RegionUtil.setBottomBorderColor(colorIndex, region, sheet);
        RegionUtil.setRightBorderColor(colorIndex, region, sheet);
    }

    /**
     * 添加下拉框
     *
     * @param sheet   sheet
     * @param column  开始列
     * @param options 选项
     */
    public static void addSelectOptions(Sheet sheet, int column, String[] options) {
        addSelectOptions(sheet, 0, Short.MAX_VALUE * 2 + 1, column, column, options);
    }

    /**
     * 添加下拉框
     *
     * @param sheet    sheet
     * @param startRow 开始行
     * @param column   开始列
     * @param options  选项
     */
    public static void addSelectOptions(Sheet sheet, int startRow, int column, String[] options) {
        addSelectOptions(sheet, startRow, Short.MAX_VALUE * 2 + 1, column, column, options);
    }

    /**
     * 添加下拉框
     *
     * @param sheet    sheet
     * @param startRow 开始行
     * @param endRow   结束行
     * @param column   结束列
     * @param options  选项
     */
    public static void addSelectOptions(Sheet sheet, int startRow, int endRow, int column, String[] options) {
        addSelectOptions(sheet, startRow, endRow, column, column, options);
    }

    /**
     * 添加下拉框
     *
     * @param sheet       sheet
     * @param startRow    开始行
     * @param endRow      结束行
     * @param startColumn 开始列
     * @param endColumn   结束列
     * @param options     选项
     */
    public static void addSelectOptions(Sheet sheet, int startRow, int endRow, int startColumn, int endColumn, String[] options) {
        CellRangeAddressList range = new CellRangeAddressList(startRow, endRow, startColumn, endColumn);
        DataValidationConstraint c = sheet.getDataValidationHelper().createExplicitListConstraint(options);
        DataValidation v = sheet.getDataValidationHelper().createValidation(c, range);
        sheet.addValidationData(v);
    }

    // --------------- write ---------------

    public static void write(Workbook workbook, String file) {
        write(workbook, Files1.openOutputStreamSafe(file), true);
    }

    public static void write(Workbook workbook, File file) {
        write(workbook, Files1.openOutputStreamSafe(file), true);
    }

    public static void write(Workbook workbook, OutputStream out) {
        write(workbook, out, false);
    }

    /**
     * 写入workbook到流
     *
     * @param workbook workbook
     * @param out      流
     * @param close    是否关闭流
     */
    private static void write(Workbook workbook, OutputStream out, boolean close) {
        Valid.notNull(workbook, "workbook is null");
        Valid.notNull(out, "outputStream is null");
        try {
            workbook.write(out);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            if (close) {
                Streams.close(out);
            }
        }
    }

    public static void write(Workbook workbook, String file, String password) {
        write(workbook, Files1.openOutputStreamSafe(new File(file)), password, true);
    }

    public static void write(Workbook workbook, File file, String password) {
        write(workbook, Files1.openOutputStreamSafe(file), password, true);
    }

    public static void write(Workbook workbook, OutputStream out, String password) {
        write(workbook, out, password, false);
    }

    /**
     * 写入workbook到流
     *
     * @param workbook workbook
     * @param out      流
     * @param password password
     * @param close    是否关闭流
     */
    private static void write(Workbook workbook, OutputStream out, String password, boolean close) {
        Valid.notNull(workbook, "workbook is null");
        Valid.notNull(out, "outputStream is null");
        try (POIFSFileSystem fs = new POIFSFileSystem()) {
            EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
            Encryptor enc = info.getEncryptor();
            ByteArrayOutputStream tmpOut = new ByteArrayOutputStream();
            workbook.write(tmpOut);
            ByteArrayInputStream tmpIn = new ByteArrayInputStream(tmpOut.toByteArray());
            enc.confirmPassword(password);
            try (OPCPackage opc = OPCPackage.open(tmpIn);
                 OutputStream os = enc.getDataStream(fs)) {
                opc.save(os);
            }
            fs.writeFilesystem(out);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            if (close) {
                Streams.close(out);
            }
        }
    }

    /**
     * 关闭workbook
     *
     * @param workbook workbook
     */
    public static void close(Workbook workbook) {
        try {
            Streams.close(workbook);
        } catch (Exception e) {
            // skip streaming workbook
        }
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
        return openWorkbook(in, null);
    }

    public static Workbook openWorkbook(String file) {
        return openWorkbook(Files1.openInputStreamSafe(file), null);
    }

    public static Workbook openWorkbook(File file) {
        return openWorkbook(Files1.openInputStreamSafe(file), null);
    }

    public static Workbook openWorkbook(String file, String password) {
        return openWorkbook(Files1.openInputStreamSafe(file), password);
    }

    public static Workbook openWorkbook(File file, String password) {
        return openWorkbook(Files1.openInputStreamSafe(file), password);
    }

    /**
     * 获取workbook
     *
     * @param in 文件流
     * @return StreamingWorkbook
     */
    public static Workbook openWorkbook(InputStream in, String password) {
        try {
            return WorkbookFactory.create(in, password);
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
