package com.orion.office.excel.annotation;

import com.orion.lang.utils.Strings;

import java.lang.annotation.*;

/**
 * excel 导出表格注解
 *
 * @author Jiahang Li
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
     * 列是否使用默认样式 全局
     * skip() 会使用默认样式
     */
    boolean columnUseDefaultStyle() default false;

    /**
     * 是否将index作为排序字段
     */
    boolean indexToSort() default false;

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
     * 是否选中
     */
    boolean selected() default false;

    /**
     * 是否隐藏
     */
    boolean hidden() default false;

    /**
     * 是否隐藏网格线
     */
    boolean displayGridLines() default false;

    /**
     * 是否隐藏列数和行数
     */
    boolean displayRowColHeadings() default false;

    /**
     * 是否不执行公式 会修改列宽单位
     */
    boolean displayFormulas() default false;

}
