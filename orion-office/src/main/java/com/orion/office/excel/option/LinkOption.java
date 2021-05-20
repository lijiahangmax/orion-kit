package com.orion.office.excel.option;

import com.orion.office.excel.type.ExcelFieldType;
import com.orion.office.excel.type.ExcelLinkType;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Excel 超链接配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/25 10:09
 */
public class LinkOption implements Serializable {

    public static final String NORMAL_PREFIX = "!";

    public static final String FIELD_PREFIX = "$";

    public static final String ORIGIN = "@";

    /**
     * 超链接类型
     */
    private ExcelLinkType type;

    /**
     * 超链地址公式
     * <p>
     * 固定地址: !xxx
     * 字段: $xxx
     * 原文本: @
     */
    private String address;

    /**
     * 超链文本公式
     * <p>
     * 固定文本: !xxx
     * 字段: $xxx
     * 原文本: @
     */
    private String text;

    /**
     * 超链接地址
     */
    private String linkValue;

    /**
     * 超链接文本
     */
    private Object textValue;

    /**
     * 超链接文本类型
     */
    private ExcelFieldType textType;

    /**
     * 超链接地址 getter
     */
    private Method linkGetterMethod;

    /**
     * 超链接文本 getter
     */
    private Method textGetterMethod;

    /**
     * 原始对象地址
     */
    private boolean originLink;

    /**
     * 原始对象文本
     */
    private boolean originText;

    /**
     * 文本单元格参数
     */
    private CellOption cellOption;

    public ExcelLinkType getType() {
        return type;
    }

    public void setType(ExcelLinkType type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLinkValue() {
        return linkValue;
    }

    public void setLinkValue(String linkValue) {
        this.linkValue = linkValue;
    }

    public Object getTextValue() {
        return textValue;
    }

    public void setTextValue(Object textValue) {
        this.textValue = textValue;
    }

    public ExcelFieldType getTextType() {
        return textType;
    }

    public void setTextType(ExcelFieldType textType) {
        this.textType = textType;
    }

    public Method getLinkGetterMethod() {
        return linkGetterMethod;
    }

    public void setLinkGetterMethod(Method linkGetterMethod) {
        this.linkGetterMethod = linkGetterMethod;
    }

    public Method getTextGetterMethod() {
        return textGetterMethod;
    }

    public void setTextGetterMethod(Method textGetterMethod) {
        this.textGetterMethod = textGetterMethod;
    }

    public boolean isOriginLink() {
        return originLink;
    }

    public void setOriginLink(boolean originLink) {
        this.originLink = originLink;
    }

    public boolean isOriginText() {
        return originText;
    }

    public void setOriginText(boolean originText) {
        this.originText = originText;
    }

    public CellOption getCellOption() {
        return cellOption;
    }

    public void setCellOption(CellOption cellOption) {
        this.cellOption = cellOption;
    }

}
