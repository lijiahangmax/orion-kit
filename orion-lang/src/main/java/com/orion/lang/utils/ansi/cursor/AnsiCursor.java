package com.orion.lang.utils.ansi.cursor;

import com.orion.lang.utils.ansi.AnsiElement;

import static com.orion.lang.utils.ansi.AnsiConst.*;

/**
 * ANSI 光标
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 18:18
 */
public class AnsiCursor implements AnsiElement {

    private final String code;

    public AnsiCursor(String code) {
        this.code = code;
    }

    public static AnsiCursor up() {
        return new AnsiCursor(CURSOR_UP);
    }

    /**
     * 向上移动 N 行
     *
     * @param line line
     * @return cursor
     */
    public static AnsiCursor up(int line) {
        return new AnsiCursor(line + CURSOR_UP);
    }

    public static AnsiCursor down() {
        return new AnsiCursor(CURSOR_DOWN);
    }

    /**
     * 向下移动 N 行
     *
     * @param line line
     * @return cursor
     */
    public static AnsiCursor down(int line) {
        return new AnsiCursor(line + CURSOR_DOWN);
    }

    public static AnsiCursor right() {
        return new AnsiCursor(CURSOR_RIGHT);
    }

    /**
     * 向右移动 N 列
     *
     * @param line line
     * @return cursor
     */
    public static AnsiCursor right(int line) {
        return new AnsiCursor(line + CURSOR_RIGHT);
    }

    public static AnsiCursor left() {
        return new AnsiCursor(CURSOR_LEFT);
    }

    /**
     * 向左移动 N 列
     *
     * @param line line
     * @return cursor
     */
    public static AnsiCursor left(int line) {
        return new AnsiCursor(line + CURSOR_LEFT);
    }

    public static AnsiCursor next() {
        return new AnsiCursor(CURSOR_NEXT_LINE);
    }

    /**
     * 将光标移动到下 N 行的开头
     *
     * @param line line
     * @return cursor
     */
    public static AnsiCursor next(int line) {
        return new AnsiCursor(line + CURSOR_NEXT_LINE);
    }

    public static AnsiCursor prev() {
        return new AnsiCursor(CURSOR_PREV_LINE);
    }

    /**
     * 将光标移动到上 N 行的开头
     *
     * @param line line
     * @return cursor
     */
    public static AnsiCursor prev(int line) {
        return new AnsiCursor(line + CURSOR_PREV_LINE);
    }

    public static AnsiCursor column() {
        return new AnsiCursor(CURSOR_COLUMN);
    }

    /**
     * 将光标移动到第 N 列
     *
     * @param column column
     * @return cursor
     */
    public static AnsiCursor column(int column) {
        return new AnsiCursor(column + CURSOR_COLUMN);
    }

    public static AnsiCursor forwardTab() {
        return new AnsiCursor(CURSOR_FORWARD_TAB);
    }

    /**
     * 将光标后移 N TAB
     *
     * @param tab tab
     * @return cursor
     */
    public static AnsiCursor forwardTab(int tab) {
        return new AnsiCursor(tab + CURSOR_FORWARD_TAB);
    }

    public static AnsiCursor backwardTab() {
        return new AnsiCursor(CURSOR_BACKWARD_TAB);
    }

    /**
     * 将光标前移 N TAB
     *
     * @param tab tab
     * @return cursor
     */
    public static AnsiCursor backwardTab(int tab) {
        return new AnsiCursor(tab + CURSOR_BACKWARD_TAB);
    }

    /**
     * 将光标移动到第 N 行
     *
     * @param line line
     * @return cursor
     */
    public static AnsiCursor line(int line) {
        return new AnsiCursor(line + JOIN + CURSOR_MOVE);
    }

    /**
     * 将光标移动到第 首行首列
     *
     * @return cursor
     */
    public static AnsiCursor reset() {
        return new AnsiCursor(JOIN + CURSOR_MOVE);
    }

    /**
     * 将光标移动到第 N 行 N 列
     * <p>
     * [{line};{column}H 同 [{line};{column}f
     *
     * @param line   line
     * @param column column
     * @return cursor
     */
    public static AnsiCursor move(int line, int column) {
        return new AnsiCursor(line + JOIN + column + CURSOR_MOVE);
    }

    /**
     * 标记光标位置
     *
     * @return cursor
     */
    public static AnsiCursor mark() {
        return new AnsiCursor(CURSOR_MARK);
    }

    /**
     * 恢复光标位置
     *
     * @return cursor
     */
    public static AnsiCursor resume() {
        return new AnsiCursor(CURSOR_RESUME);
    }

    /**
     * 请求光标位置
     *
     * @return cursor
     */
    public static AnsiCursor report() {
        return new AnsiCursor(CURSOR_REPORT);
    }

    @Override
    public String toString() {
        return CSI_PREFIX + code;
    }

}
