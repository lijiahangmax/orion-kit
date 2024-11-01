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
package cn.orionsec.kit.office.excel.option;

import cn.orionsec.kit.office.excel.type.ExcelFieldType;
import cn.orionsec.kit.office.excel.type.ExcelPictureType;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * excel 图片类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/27 23:13
 */
public class PictureOption implements Serializable {

    public static final String NORMAL_PREFIX = "!";

    public static final String FIELD_PREFIX = "$";

    public static final String ORIGIN = "@";

    /**
     * 类型
     */
    private ExcelPictureType type;

    /**
     * 数据是否为base64
     */
    private boolean base64;

    /**
     * 如果是流是否自动关闭
     */
    private boolean autoClose;

    /**
     * X 缩放
     */
    private double scaleX;

    /**
     * Y 缩放
     */
    private double scaleY;

    /**
     * 图片公式
     * <p>
     * 默认: @
     * 字段: $xxx
     */
    private String image;

    /**
     * 单元格内容公式
     * <p>
     * 不设置内容: ""
     * 原文本: @
     * 固定文本: !xxx
     * 字段: $xxx
     */
    private String text;

    /**
     * 是否不设置文本
     */
    private boolean noneText;

    /**
     * 固定文本内容
     */
    private String textValue;

    /**
     * 是否使用原文本
     */
    private boolean originText;

    /**
     * 是否用原始图片字段
     */
    private boolean originImage;

    /**
     * 图片字段getter
     */
    private Method imageGetter;

    /**
     * 文本字段getter
     */
    private Method textGetter;

    /**
     * 文本类型
     */
    private ExcelFieldType textType;

    /**
     * 文本单元格参数
     */
    private CellOption cellOption;

    public ExcelPictureType getType() {
        return type;
    }

    public void setType(ExcelPictureType type) {
        this.type = type;
    }

    public boolean isBase64() {
        return base64;
    }

    public void setBase64(boolean base64) {
        this.base64 = base64;
    }

    public boolean isAutoClose() {
        return autoClose;
    }

    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isNoneText() {
        return noneText;
    }

    public void setNoneText(boolean noneText) {
        this.noneText = noneText;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public boolean isOriginText() {
        return originText;
    }

    public void setOriginText(boolean originText) {
        this.originText = originText;
    }

    public boolean isOriginImage() {
        return originImage;
    }

    public void setOriginImage(boolean originImage) {
        this.originImage = originImage;
    }

    public Method getImageGetter() {
        return imageGetter;
    }

    public void setImageGetter(Method imageGetter) {
        this.imageGetter = imageGetter;
    }

    public Method getTextGetter() {
        return textGetter;
    }

    public void setTextGetter(Method textGetter) {
        this.textGetter = textGetter;
    }

    public ExcelFieldType getTextType() {
        return textType;
    }

    public void setTextType(ExcelFieldType textType) {
        this.textType = textType;
    }

    public CellOption getCellOption() {
        return cellOption;
    }

    public void setCellOption(CellOption cellOption) {
        this.cellOption = cellOption;
    }

}
