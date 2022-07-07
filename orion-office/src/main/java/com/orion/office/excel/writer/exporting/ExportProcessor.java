package com.orion.office.excel.writer.exporting;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Objects1;
import com.orion.lang.utils.codec.Base64s;
import com.orion.lang.utils.io.Streams;
import com.orion.lang.utils.reflect.Methods;
import com.orion.office.excel.Excels;
import com.orion.office.excel.option.ExportFieldOption;
import com.orion.office.excel.option.LinkOption;
import com.orion.office.excel.option.PictureOption;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * export 单元格处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/31 11:21
 */
public class ExportProcessor<T> {

    private Workbook workbook;

    private Sheet sheet;

    private SheetConfig sheetConfig;

    protected ExportProcessor(Workbook workbook, Sheet sheet, SheetConfig sheetConfig) {
        this.workbook = workbook;
        this.sheetConfig = sheetConfig;
        this.sheet = sheet;
    }

    /**
     * 设置cell的值
     *
     * @param cell        cell
     * @param rowIndex    rowIndex
     * @param columnIndex columnIndex
     * @param row         row
     */
    protected void setCellValue(Cell cell, int rowIndex, int columnIndex, T row, ExportFieldOption option) {
        CellStyle style = sheetConfig.columnStyles.get(columnIndex);
        if (style != null) {
            cell.setCellStyle(style);
        }
        if (row == null) {
            return;
        }
        boolean setValue = true;
        // 图片
        if (option.getPictureOption() != null) {
            this.setPicture(cell, row, rowIndex, columnIndex, option);
            setValue = false;
        }
        // 链接
        if (option.getLinkOption() != null) {
            this.setLink(cell, row, option);
            setValue = false;
        }
        if (setValue) {
            // 普通
            Object o = Methods.invokeMethod(row, option.getGetterMethod());
            if (o instanceof String && option.isTrim()) {
                o = ((String) o).trim();
            }
            Excels.setCellValue(cell, o, option.getType(), option.getCellOption());
        }
    }

    /**
     * 设置图片
     *
     * @param cell   cell
     * @param row    row
     * @param option option
     */
    private void setPicture(Cell cell, T row, int rowIndex, int columnIndex, ExportFieldOption option) {
        Method getterMethod = option.getGetterMethod();
        PictureOption pictureOption = option.getPictureOption();
        // 图片
        Method pictureGetterMethod = pictureOption.isOriginImage() ? getterMethod : pictureOption.getImageGetter();
        Object pictureValue = Methods.invokeMethod(row, pictureGetterMethod);
        if (pictureValue != null) {
            try {
                Picture picture = null;
                if (pictureValue instanceof InputStream) {
                    picture = Excels.setPicture(workbook, sheet, (InputStream) pictureValue, rowIndex, columnIndex, pictureOption.getType());
                } else if (pictureValue instanceof byte[]) {
                    picture = Excels.setPicture(workbook, sheet, (byte[]) pictureValue, rowIndex, columnIndex, pictureOption.getType());
                } else if (pictureValue instanceof String && pictureOption.isBase64()) {
                    String type = Base64s.img64Type((String) pictureValue);
                    byte[] bytes = Base64s.img64Decode((String) pictureValue);
                    picture = Excels.setPicture(workbook, sheet, bytes, rowIndex, columnIndex, type, pictureOption.getType());
                }
                if (picture != null) {
                    picture.resize(pictureOption.getScaleX(), pictureOption.getScaleY());
                }
            } catch (Exception e) {
                if (!sheetConfig.sheetOption.isSkipPictureException()) {
                    throw Exceptions.unchecked(e);
                }
            } finally {
                if (pictureOption.isAutoClose() && pictureValue instanceof InputStream) {
                    Streams.close((InputStream) pictureValue);
                }
            }
        }
        // 文本
        Object textValue = null;
        if (pictureOption.isNoneText()) {
            return;
        } else if (pictureOption.isOriginText()) {
            textValue = Methods.invokeMethod(row, getterMethod);
        } else if (pictureOption.getTextValue() != null) {
            textValue = pictureOption.getTextValue();
        } else if (pictureOption.getTextGetter() != null) {
            textValue = Methods.invokeMethod(row, pictureOption.getTextGetter());
        }
        Excels.setCellValue(cell, textValue, pictureOption.getTextType(), pictureOption.getCellOption());
    }

    /**
     * 设置超链接
     *
     * @param cell   cell
     * @param row    row
     * @param option option
     */
    private void setLink(Cell cell, T row, ExportFieldOption option) {
        Method getterMethod = option.getGetterMethod();
        LinkOption linkOption = option.getLinkOption();
        String address = null;
        Object textValue = null;
        Object methodValue = null;
        // 链接
        if (linkOption.isOriginLink()) {
            methodValue = Methods.invokeMethod(row, getterMethod);
            address = Objects1.toString(methodValue);
        } else if (linkOption.getLinkValue() != null) {
            address = linkOption.getLinkValue();
        } else if (linkOption.getLinkGetterMethod() != null) {
            address = Objects1.toString(Methods.invokeMethod(row, linkOption.getLinkGetterMethod()));
        }
        // 文本
        if (linkOption.isOriginText()) {
            if (methodValue == null) {
                methodValue = Methods.invokeMethod(row, getterMethod);
            }
            textValue = methodValue;
        } else if (linkOption.getTextValue() != null) {
            textValue = linkOption.getTextValue();
        } else if (linkOption.getTextGetterMethod() != null) {
            textValue = Objects1.toString(Methods.invokeMethod(row, linkOption.getTextGetterMethod()));
        }
        // 设置链接
        Excels.setLink(workbook, cell, linkOption.getType(), address, linkOption.getTextType(), linkOption.getCellOption(), textValue);
    }

}
