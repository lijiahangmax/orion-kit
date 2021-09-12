package com.orion.office.excel.annotation;

import com.orion.office.excel.option.LinkOption;
import com.orion.office.excel.type.ExcelLinkType;

import java.lang.annotation.*;

/**
 * excel 导出字段超链接注解
 * 可以和图片一起使用
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see ExportPicture
 * @since 2020/5/27 12:11
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportLink {

    /**
     * 超链接类型
     */
    ExcelLinkType type() default ExcelLinkType.LINK_URL;

    /**
     * 超链地址公式
     * <p>
     * 固定地址: !xxx
     * 字段: $xxx
     * 原文本: @
     */
    String address() default LinkOption.ORIGIN;

    /**
     * 超链文本公式
     * <p>
     * 固定文本: !xxx
     * 字段: $xxx
     * 原文本: @
     */
    String text() default LinkOption.ORIGIN;

}
