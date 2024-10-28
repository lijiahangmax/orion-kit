/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.office.excel.writer.exporting;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.codec.Base64s;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.lang.utils.reflect.Methods;
import cn.orionsec.kit.office.excel.Excels;
import cn.orionsec.kit.office.excel.option.ExportFieldOption;
import cn.orionsec.kit.office.excel.option.LinkOption;
import cn.orionsec.kit.office.excel.option.PictureOption;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * export 单元格处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/31 11:21
 */
public class ExportProcessor<T> {

    private final Workbook workbook;

    private final Sheet sheet;

    private final SheetConfig<T> sheetConfig;

    protected ExportProcessor(Workbook workbook, Sheet sheet, SheetConfig<T> sheetConfig) {
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
        // 设置样式
        CellStyle style = Optional.ofNullable(sheetConfig.columnStyleSelector.get(columnIndex))
                .map(s -> s.apply(row))
                .orElse(sheetConfig.columnStyles.get(columnIndex));
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
                    String type = Base64s.getMimeTypeLast((String) pictureValue);
                    byte[] bytes = Base64s.mimeTypeDecode((String) pictureValue);
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
