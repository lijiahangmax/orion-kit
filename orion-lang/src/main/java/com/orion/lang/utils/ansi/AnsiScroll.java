package com.orion.lang.utils.ansi;

import static com.orion.lang.utils.ansi.AnsiConst.*;

/**
 * ANSI 滚动
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/29 11:01
 */
public class AnsiScroll implements AnsiElement {

    private final String code;

    public AnsiScroll(String code) {
        this.code = code;
    }

    public static AnsiScroll up() {
        return new AnsiScroll(SCROLL_UP);
    }

    /**
     * 向上滚动 N 行
     *
     * @param line line
     * @return scroll
     */
    public static AnsiScroll up(int line) {
        return new AnsiScroll(line + SCROLL_UP);
    }

    public static AnsiScroll down() {
        return new AnsiScroll(SCROLL_DOWN);
    }

    /**
     * 向下滚动 N 行
     *
     * @param line line
     * @return scroll
     */
    public static AnsiScroll down(int line) {
        return new AnsiScroll(line + SCROLL_DOWN);
    }

    public static AnsiScroll right() {
        return new AnsiScroll(SCROLL_RIGHT);
    }

    /**
     * 向右滚动 N 行
     *
     * @param line line
     * @return scroll
     */
    public static AnsiScroll right(int line) {
        return new AnsiScroll(line + SCROLL_RIGHT);
    }

    public static AnsiScroll left() {
        return new AnsiScroll(SCROLL_LEFT);
    }

    /**
     * 向左滚动 N 行
     *
     * @param line line
     * @return scroll
     */
    public static AnsiScroll left(int line) {
        return new AnsiScroll(line + SCROLL_LEFT);
    }

    @Override
    public String toString() {
        return CSI_PREFIX + code;
    }

}
