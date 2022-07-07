package com.orion.office.excel.annotation;

import com.orion.lang.utils.Strings;
import com.orion.office.excel.option.PictureOption;
import com.orion.office.excel.type.ExcelPictureType;

import java.lang.annotation.*;

/**
 * excel 导出图片
 * 可以和超链接一起使用
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see ExportLink
 * @since 2020/12/27 22:49
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportPicture {

    /**
     * 图片类型
     */
    ExcelPictureType type() default ExcelPictureType.AUTO;

    /**
     * 数据是否为base64
     */
    boolean base64() default false;

    /**
     * 如果是流是否自动关闭
     *
     * @see java.io.InputStream
     */
    boolean autoClose() default false;

    /**
     * X 缩放
     */
    double scaleX() default 1;

    /**
     * Y 缩放
     */
    double scaleY() default 1;

    /**
     * 图片公式
     * <p>
     * 默认: @
     * 字段: $xxx
     */
    String image() default PictureOption.ORIGIN;

    /**
     * 单元格内容公式
     * <p>
     * 不设置内容: ""
     * 原文本: @
     * 固定文本: !xxx
     * 字段: $xxx
     */
    String text() default Strings.EMPTY;

}
