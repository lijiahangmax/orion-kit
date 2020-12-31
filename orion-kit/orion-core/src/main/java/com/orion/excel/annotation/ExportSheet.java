package com.orion.excel.annotation;

import com.orion.utils.Strings;

import java.lang.annotation.*;

/**
 * Excel 导出表格注解
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/27 12:11
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportSheet {

    /**
     * sheet 名称
     */
    String name() default Strings.EMPTY;

    /**
     * 默认行宽
     */
    int columnWidth() default -1;

    /**
     * 行高
     *
     * @see #titleHeight()
     * @see #headerHeight()
     * @see #rowHeight()
     */
    int height() default -1;

    /**
     * 标题行高
     */
    int titleHeight() default -1;

    /**
     * 头部行高
     */
    int headerHeight() default -1;

    /**
     * 数据行高
     */
    int rowHeight() default -1;

    /**
     * 缩放
     */
    int zoom() default -1;

    /**
     * 表头是否使用数据列的样式
     */
    boolean headerUseColumnStyle() default true;

    /**
     * 是否跳过默认表头
     *
     * @see ExportField#header()
     */
    boolean skipFieldHeader() default false;

    /**
     * 是否跳过表头批注
     *
     * @see ExportComment
     */
    boolean skipComment() default false;

    /**
     * 是否跳过超链接
     *
     * @see ExportLink
     */
    boolean skipLink() default false;

    /**
     * 是否跳过图片
     *
     * @see ExportPicture
     */
    boolean skipPicture() default false;

    /**
     * 是否跳过图片异常
     *
     * @see ExportPicture
     */
    boolean skipPictureException() default true;

    /**
     * 是否跳过下拉选择框
     */
    boolean skipSelectOption() default false;

    /**
     * 是否固定表头
     */
    boolean freezeHeader() default false;

    /**
     * 是否可以筛选
     */
    boolean filterHeader() default false;

    /**
     * 选中
     */
    boolean selected() default false;

    /**
     * 隐藏
     */
    boolean hidden() default false;

}
