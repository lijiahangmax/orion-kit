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
package com.orion.office.excel.type;

import com.orion.lang.utils.Strings;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * excel 图片类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/27 22:50
 */
public enum ExcelPictureType {

    /**
     * 自动
     */
    AUTO(0, 0, Strings.EMPTY),

    /**
     * EMF Windows
     */
    EMF(Workbook.PICTURE_TYPE_EMF, XSSFWorkbook.PICTURE_TYPE_EMF, "emf"),

    /**
     * WMF Windows
     */
    WMF(Workbook.PICTURE_TYPE_WMF, XSSFWorkbook.PICTURE_TYPE_WMF, "wmf"),

    /**
     * PICT Mac
     */
    PICT(Workbook.PICTURE_TYPE_PICT, XSSFWorkbook.PICTURE_TYPE_PICT, "pict"),

    /**
     * JPG
     */
    JPG(Workbook.PICTURE_TYPE_JPEG, XSSFWorkbook.PICTURE_TYPE_JPEG, "jpg"),

    /**
     * JPG
     */
    JPEG(Workbook.PICTURE_TYPE_JPEG, XSSFWorkbook.PICTURE_TYPE_JPEG, "jpeg"),

    /**
     * PNG
     */
    PNG(Workbook.PICTURE_TYPE_PNG, XSSFWorkbook.PICTURE_TYPE_PNG, "png"),

    /**
     * BIT_MAT
     */
    BIT_MAT(Workbook.PICTURE_TYPE_DIB, XSSFWorkbook.PICTURE_TYPE_DIB, "bid"),

    /**
     * GIF
     */
    GIF(-1, XSSFWorkbook.PICTURE_TYPE_GIF, "gif"),

    /**
     * TIFF
     */
    TIFF(-1, XSSFWorkbook.PICTURE_TYPE_TIFF, "tiff"),

    /**
     * EPS
     */
    EPS(-1, XSSFWorkbook.PICTURE_TYPE_EPS, "eps"),

    /**
     * BMP Windows
     */
    BMP(-1, XSSFWorkbook.PICTURE_TYPE_BMP, "bmp"),

    /**
     * WPG
     */
    WPG(-1, XSSFWorkbook.PICTURE_TYPE_WPG, "wpg");

    private final int type1;

    private final int type2;

    private final String suffix;

    ExcelPictureType(int type1, int type2, String suffix) {
        this.type1 = type1;
        this.type2 = type2;
        this.suffix = suffix;
    }

    public int getType1() {
        return type1;
    }

    public int getType2() {
        return type2;
    }

    public String getSuffix() {
        return suffix;
    }

    /**
     * 获取后缀类型
     *
     * @param suffix suffix
     * @return ExcelPictureType
     */
    public static ExcelPictureType of(String suffix) {
        if (Strings.isEmpty(suffix)) {
            return PNG;
        }
        for (ExcelPictureType value : ExcelPictureType.values()) {
            if (value.getSuffix().equals(suffix)) {
                return value;
            }
        }
        return PNG;
    }

}
