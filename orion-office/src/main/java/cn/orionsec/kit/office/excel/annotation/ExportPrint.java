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
package cn.orionsec.kit.office.excel.annotation;

import cn.orionsec.kit.office.excel.type.ExcelPaperType;

import java.lang.annotation.*;

/**
 * excel 导出打印配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/31 10:35
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportPrint {

    /**
     * 是否打印网格线
     */
    boolean printGridLines() default true;

    /**
     * 是否打印行标题和列标题
     */
    boolean printHeading() default false;

    /**
     * 打印自适应
     */
    boolean fit() default true;

    /**
     * 每页行数
     */
    int limit() default -1;

    /**
     * 重复打印的行和列
     * 0 rowStartIndex
     * 1 rowEndIndex
     * 2 columnStartIndex
     * 3 columnEndIndex
     * [1, 3] = [0, 1, 0, 3]
     */
    int[] repeat() default {};

    /**
     * 纸张大小
     */
    ExcelPaperType paper() default ExcelPaperType.DEFAULT;

    /**
     * 是否是彩色
     */
    boolean color() default false;

    /**
     * 是否横向打印
     */
    boolean landScapePrint() default false;

    /**
     * 是否设置打印方向
     */
    boolean setPrintOrientation() default false;

    /**
     * 缩放比例  10 - 400
     */
    int scale() default 100;

    /**
     * 是否打印批注
     */
    boolean notes() default false;

    /**
     * 水平分辨率
     */
    int horizontalResolution() default -1;

    /**
     * 垂直分辨率
     */
    int verticalResolution() default -1;

    /**
     * 宽
     */
    int width() default -1;

    /**
     * 高
     */
    int height() default -1;

    /**
     * 左边距 英寸
     */
    double leftMargin() default -1;

    /**
     * 右边距 英寸
     */
    double rightMargin() default -1;

    /**
     * 上边距 英寸
     */
    double topMargin() default -1;

    /**
     * 下边距 英寸
     */
    double bottomMargin() default -1;

    /**
     * 页眉边距 英寸
     */
    double headerMargin() default -1;

    /**
     * 页脚边距 英寸
     */
    double footerMargin() default -1;

    /**
     * 是否使用起始页
     */
    boolean usePage() default true;

    /**
     * 起始页码
     */
    int pageStart() default 1;

    /**
     * 打印份数
     */
    int copies() default 1;

    /**
     * 是否为草稿模式
     */
    boolean draft() default false;

    /**
     * 是否自上而下
     */
    boolean topToBottom() default true;

    /**
     * 页面是否水平居中
     */
    boolean horizontallyCenter() default false;

    /**
     * 页面是否垂直居中
     */
    boolean verticallyCenter() default false;

}
