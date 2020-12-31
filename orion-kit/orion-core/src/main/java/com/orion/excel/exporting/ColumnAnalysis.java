package com.orion.excel.exporting;

import com.orion.excel.annotation.*;
import com.orion.excel.option.*;
import com.orion.excel.type.ExcelFieldType;
import com.orion.lang.wrapper.Tuple;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.reflect.Annotations;
import com.orion.utils.reflect.Classes;
import com.orion.utils.reflect.Fields;
import com.orion.utils.reflect.Methods;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 列 解析器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/12/29 18:05
 */
public class ColumnAnalysis implements Analysable {

    private Class<?> targetClass;

    private int columnSize;

    private SheetOption sheetOption;

    private Map<Integer, FieldOption> fieldOptions;

    public ColumnAnalysis(Class<?> targetClass, SheetOption sheetOption, Map<Integer, FieldOption> fieldOptions) {
        this.targetClass = targetClass;
        this.sheetOption = sheetOption;
        this.fieldOptions = fieldOptions;
    }

    @Override
    public void analysis() {
        // 注解field
        List<Field> fieldList = Fields.getFieldByCache(targetClass);
        // 注解method
        List<Method> methodList = Methods.getAllGetterMethodByCache(targetClass);
        for (Field field : fieldList) {
            this.analysisColumn(Annotations.getAnnotation(field, ExportField.class),
                    Annotations.getAnnotation(field, ExportFont.class),
                    Annotations.getAnnotation(field, ExportComment.class),
                    Annotations.getAnnotation(field, ExportLink.class),
                    Annotations.getAnnotation(field, ExportPicture.class),
                    Annotations.getAnnotation(field, ExportIgnore.class),
                    Methods.getGetterMethodByField(targetClass, field));
        }
        for (Method method : methodList) {
            this.analysisColumn(Annotations.getAnnotation(method, ExportField.class),
                    Annotations.getAnnotation(method, ExportFont.class),
                    Annotations.getAnnotation(method, ExportComment.class),
                    Annotations.getAnnotation(method, ExportLink.class),
                    Annotations.getAnnotation(method, ExportPicture.class),
                    Annotations.getAnnotation(method, ExportIgnore.class),
                    method);
        }
    }

    /**
     * 解析 field font comment link picture
     *
     * @param field   field
     * @param font    font
     * @param comment comment
     * @param link    link
     * @param picture picture
     * @param ignore  ignore
     * @param method  getter
     */
    private void analysisColumn(ExportField field, ExportFont font,
                                ExportComment comment, ExportLink link,
                                ExportPicture picture, ExportIgnore ignore,
                                Method method) {
        if (field == null || ignore != null) {
            return;
        }
        int index = field.index();
        sheetOption.setColumnSize(columnSize = Math.max(columnSize, index));
        // 解析 field
        FieldOption fieldOption = this.analysisField(field, method);
        if (font != null) {
            // 解析 font
            FontOption fontOption = this.analysisFont(font);
            fieldOption.setFontOption(fontOption);
        }
        // 解析 comment
        if (comment != null && !sheetOption.isSkipComment()) {
            fieldOption.setCommentOption(this.analysisComment(comment));
        }
        // 解析 link
        if (link != null && !sheetOption.isSkipLink()) {
            fieldOption.setLinkOption(this.analysisLink(link, fieldOption));
        }
        // 解析 picture
        if (picture != null && !sheetOption.isSkipPicture()) {
            fieldOption.setPictureOption(this.analysisPicture(picture, fieldOption, method));
        }
    }

    /**
     * 解析样式
     *
     * @param field annotation
     * @return FieldOption
     */
    private FieldOption analysisField(ExportField field, Method method) {
        FieldOption fieldOption = new FieldOption();
        fieldOption.setGetterMethod(method);
        // 对齐
        fieldOption.setAlign(field.align());
        fieldOption.setVerticalAlign(field.verticalAlign());
        // 宽
        int width = field.width();
        if (width != -1) {
            fieldOption.setWidth(width);
        }
        // 背景色
        String backgroundColor = field.backgroundColor();
        if (!Strings.isEmpty(backgroundColor)) {
            fieldOption.setBackgroundColor(backgroundColor);
        }
        if (field.wrapText()) {
            fieldOption.setWrapText(true);
        }
        // 边框
        fieldOption.setBorder(field.border());
        String borderColor = field.borderColor();
        if (!Strings.isEmpty(borderColor)) {
            fieldOption.setBorderColor(borderColor);
        }
        // 缩进
        if (field.indent() != -1) {
            fieldOption.setIndent((short) field.indent());
        }
        // 类型
        ExcelFieldType type = field.type();
        if (type.equals(ExcelFieldType.AUTO)) {
            fieldOption.setType(ExcelFieldType.of(method.getReturnType()));
        } else {
            fieldOption.setType(type);
        }
        fieldOption.setFormat(field.format());
        fieldOption.setHeader(field.header());
        fieldOption.setSkipHeaderStyle(field.skipHeaderStyle());
        fieldOption.setSelectOptions(field.selectOptions());
        // 隐藏
        fieldOption.setHidden(field.hidden());
        fieldOption.setLock(field.lock());
        fieldOption.setAutoResize(field.autoResize());
        fieldOption.setQuotePrefixed(field.quotePrefixed());
        // cell
        CellOption cellOption = new CellOption();
        cellOption.setFormat(field.format());
        fieldOption.setCellOption(cellOption);
        fieldOptions.put(field.index(), fieldOption);
        return fieldOption;
    }

    /**
     * 解析字体
     *
     * @param font annotation
     * @return FontOption
     */
    private FontOption analysisFont(ExportFont font) {
        return parseFont(font);
    }

    /**
     * 解析批注
     *
     * @param comment annotation
     * @return CommentOption
     */
    private CommentOption analysisComment(ExportComment comment) {
        CommentOption commentOption = new CommentOption();
        commentOption.setAuthor(comment.author());
        commentOption.setComment(comment.comment());
        commentOption.setVisible(comment.visible());
        return commentOption;
    }

    /**
     * 解析超链接
     *
     * @param link        link
     * @param fieldOption fieldOption
     * @return LinkOption
     */
    private LinkOption analysisLink(ExportLink link, FieldOption fieldOption) {
        LinkOption linkOption = new LinkOption();
        linkOption.setType(link.type());
        linkOption.setAddress(link.address());
        linkOption.setText(link.text());
        // 解析地址
        if (LinkOption.ORIGIN.equals(linkOption.getAddress())) {
            // 原数据
            linkOption.setOriginLink(true);
        } else if (linkOption.getAddress().startsWith(LinkOption.NORMAL_PREFIX)) {
            // 普通文本
            linkOption.setLinkValue(linkOption.getAddress().substring(1));
        } else if (linkOption.getAddress().startsWith(LinkOption.FIELD_PREFIX)) {
            // 字段
            String fieldName = linkOption.getAddress().substring(1);
            this.setLinkOptionType(linkOption, true, fieldName);
        } else {
            throw Exceptions.parse("Failed to parse the hyperlink address formula, reason: unknown formula " + linkOption.getAddress());
        }
        // 解析文本
        if (LinkOption.ORIGIN.equals(linkOption.getText())) {
            // 原数据
            linkOption.setOriginText(true);
            linkOption.setTextType(fieldOption.getType());
            linkOption.setCellOption(fieldOption.getCellOption());
        } else if (linkOption.getText().startsWith(LinkOption.NORMAL_PREFIX)) {
            // 普通文本
            linkOption.setTextType(ExcelFieldType.TEXT);
            linkOption.setTextValue(linkOption.getText().substring(1));
        } else if (linkOption.getText().startsWith(LinkOption.FIELD_PREFIX)) {
            // 字段
            String fieldName = linkOption.getText().substring(1);
            this.setLinkOptionType(linkOption, false, fieldName);
        } else {
            throw Exceptions.parse("Failed to parse the hyperlink text formula, reason: unknown formula " + linkOption.getTextGetterMethod());
        }
        return linkOption;
    }

    /**
     * 解析图片
     *
     * @param picture     图片
     * @param fieldOption fieldOption
     * @param method      getter
     * @return PictureOption
     */
    private PictureOption analysisPicture(ExportPicture picture, FieldOption fieldOption, Method method) {
        PictureOption option = new PictureOption();
        option.setScaleX(picture.scaleX());
        option.setScaleY(picture.scaleY());
        option.setType(picture.type());
        option.setAutoClose(picture.autoClose());
        option.setBase64(picture.base64());
        option.setText(picture.text());
        option.setImage(picture.image());

        Method pictureGetterMethod;
        // 解析图片
        if (PictureOption.ORIGIN.equals(option.getImage())) {
            // 原数据
            option.setOriginImage(true);
            pictureGetterMethod = method;
        } else if (option.getImage().startsWith(PictureOption.FIELD_PREFIX)) {
            // 字段
            pictureGetterMethod = method;
            option.setImageGetter(pictureGetterMethod);
        } else {
            throw Exceptions.parse("Failed to parse the hyperlink address formula, reason: unknown formula " + option.getImage());
        }
        Class<?> pictureReturnType = pictureGetterMethod.getReturnType();
        if (!Classes.isImplClass(InputStream.class, pictureReturnType) &&
                !pictureReturnType.equals(byte[].class) &&
                !(option.isBase64() && pictureReturnType.equals(String.class))) {
            throw Exceptions.parse("The image field type must not be InputStream, byte[], or Base64 String");
        }

        // 解析文本
        if (Strings.isEmpty(option.getText())) {
            // 无数据
            option.setNoneText(true);
        } else if (PictureOption.ORIGIN.equals(option.getText())) {
            // 原数据
            option.setOriginText(true);
            option.setTextType(fieldOption.getType());
            option.setCellOption(fieldOption.getCellOption());
        } else if (option.getText().startsWith(PictureOption.NORMAL_PREFIX)) {
            // 普通文本
            option.setTextType(ExcelFieldType.TEXT);
            option.setTextValue(option.getText().substring(1));
        } else if (option.getText().startsWith(PictureOption.FIELD_PREFIX)) {
            // 字段
            String fieldName = option.getText().substring(1);
            this.setPictureTextOption(option, fieldName);
        } else {
            throw Exceptions.parse("Failed to parse the picture text formula, reason: unknown formula " + option.getText());
        }
        return option;
    }

    /**
     * 设置图片文本 method
     *
     * @param option    option
     * @param fieldName 字段名称
     */
    private void setPictureTextOption(PictureOption option, String fieldName) {
        Tuple tuple = this.getFieldOptionByFieldName(fieldName);
        option.setTextGetter(tuple.get(0));
        option.setTextType(tuple.get(1));
        option.setCellOption(tuple.get(2));
    }

    /**
     * 设置超链接 method
     *
     * @param option    option
     * @param isLink    isLink || isValue
     * @param fieldName 字段名称
     */
    private void setLinkOptionType(LinkOption option, boolean isLink, String fieldName) {
        Tuple tuple = this.getFieldOptionByFieldName(fieldName);
        Method getterMethod = tuple.get(0);
        if (isLink) {
            option.setLinkGetterMethod(getterMethod);
            return;
        } else {
            option.setTextGetterMethod(getterMethod);
        }
        option.setTextType(tuple.get(1));
        option.setCellOption(tuple.get(2));
    }

    /**
     * 通过字段名称获取 getter ExcelFieldType CellOption
     *
     * @param fieldName fieldName
     * @return field getter ExcelFieldType CellOption
     */
    private Tuple getFieldOptionByFieldName(String fieldName) {
        // 解析method
        Field field = Fields.getFieldByCache(targetClass, fieldName);
        Method getterMethod;
        if (field == null) {
            getterMethod = Methods.getGetterMethodByCache(targetClass, fieldName);
        } else {
            getterMethod = Methods.getGetterMethodByField(targetClass, field);
        }
        if (getterMethod == null) {
            throw Exceptions.parse("Did not find " + fieldName + " getter method at " + targetClass);
        }
        ExportField exportField = null;
        if (field != null) {
            exportField = Annotations.getAnnotation(field, ExportField.class);
        }
        if (exportField == null) {
            exportField = Annotations.getAnnotation(getterMethod, ExportField.class);
        }
        ExcelFieldType type;
        CellOption cellOption = null;
        if (exportField == null) {
            type = ExcelFieldType.of(getterMethod.getReturnType());
        } else if (exportField.type().equals(ExcelFieldType.AUTO)) {
            type = ExcelFieldType.of(getterMethod.getReturnType());
            cellOption = new CellOption(exportField.format());
        } else {
            type = exportField.type();
            cellOption = new CellOption(exportField.format());
        }
        return Tuple.of(getterMethod, type, cellOption);
    }

    // -------------------- static --------------------

    /**
     * 解析字体
     *
     * @param font annotation
     * @return FontOption
     */
    protected static FontOption parseFont(ExportFont font) {
        FontOption fontOption = new FontOption();
        String name = font.fontName();
        if (!Strings.isEmpty(name)) {
            fontOption.setFontName(name);
        }
        int s = font.fontSize();
        if (s != -1) {
            fontOption.setFontSize(s);
        }
        String c = font.color();
        if (!c.isEmpty()) {
            fontOption.setColor(c);
        }
        if (font.bold()) {
            fontOption.setBold(true);
        }
        if (font.italic()) {
            fontOption.setItalic(true);
        }
        fontOption.setUnder(font.under());
        if (font.delete()) {
            fontOption.setDelete(true);
        }
        return fontOption;
    }

}
