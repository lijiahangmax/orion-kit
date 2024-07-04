package com.orion.lang.utils.ansi;

import com.orion.lang.utils.ansi.style.AnsiStyle;

/**
 * ANSI 拼接器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 11:20
 */
public class AnsiAppender {

    private StringBuilder builder;

    public AnsiAppender() {
        this.builder = new StringBuilder();
    }

    public static AnsiAppender create() {
        return new AnsiAppender();
    }

    /**
     * 拼接
     *
     * @param text text
     * @return this
     */
    public AnsiAppender append(String text) {
        builder.append(text);
        return this;
    }

    /**
     * 拼接
     *
     * @param style style
     * @param text  text
     * @return this
     */
    public AnsiAppender append(AnsiStyle style, String text) {
        builder.append(style.toString())
                .append(text)
                .append(AnsiConst.CSI_RESET);
        return this;
    }

    /**
     * 拼接
     *
     * @param element element
     * @return this
     */
    public AnsiAppender append(AnsiElement element) {
        builder.append(element.toString());
        return this;
    }

    /**
     * 拼接
     *
     * @param elements elements
     * @return this
     */
    public AnsiAppender append(Object... elements) {
        for (Object element : elements) {
            builder.append(element.toString());
        }
        return this;
    }

    /**
     * 拼接 reset
     *
     * @return this
     */
    public AnsiAppender reset() {
        builder.append(AnsiConst.CSI_RESET);
        return this;
    }

    /**
     * 新行
     *
     * @return this
     */
    public AnsiAppender newLine() {
        builder.append(AnsiCtrl.CR)
                .append(AnsiCtrl.LF);
        return this;
    }

    /**
     * 重置
     */
    public void clear() {
        this.builder = new StringBuilder();
    }

    /**
     * 获取 value
     *
     * @return value
     */
    public String get() {
        return builder.toString();
    }

    /**
     * 获取后重置
     *
     * @return value
     */
    public String getAndClear() {
        String r = this.get();
        this.clear();
        return r;
    }

    @Override
    public String toString() {
        return this.get();
    }

}
