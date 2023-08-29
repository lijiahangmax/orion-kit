package com.orion.lang.utils.ansi.cursor;

import com.orion.lang.utils.ansi.AnsiElement;

import static com.orion.lang.utils.ansi.AnsiConst.CSI_PREFIX;

/**
 * ANSI 光标样式
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/29 14:13
 */
public enum AnsiCursorStyle implements AnsiElement {

    /**
     * 块
     */
    STEADY_BLOCK(1),

    /**
     * 闪烁块
     */
    BLINK_BLOCK(2),

    /**
     * 下滑线
     */
    STEADY_UNDERLINE(3),

    /**
     * 闪烁下滑线
     */
    BLINK_UNDERLINE(4),

    /**
     * 数显
     */
    STEADY_BAR(5),

    /**
     * 闪烁竖线
     */
    BLINK_BAR(6),

    ;

    private final int code;

    AnsiCursorStyle(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return CSI_PREFIX + code + " q";
    }

}
