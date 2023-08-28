package com.orion.lang.utils.ansi.style.color;

import com.orion.lang.constant.Const;

/**
 * ANSI 背景色
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/20 10:51
 */
public enum AnsiBackground implements AnsiColor {

    /**
     * 黑色
     */
    BLACK(40),

    /**
     * 红色
     */
    RED(41),

    /**
     * 绿色
     */
    GREEN(42),

    /**
     * 黄色
     */
    YELLOW(43),

    /**
     * 蓝色
     */
    BLUE(44),

    /**
     * 紫色
     */
    PURPLE(45),

    /**
     * 青色
     */
    CYAN(46),

    /**
     * 白色
     */
    WHITE(47),

    /**
     * 默认
     */
    DEFAULT(49),

    // -------------------- 亮色 --------------------

    /**
     * 亮黑色 (灰)
     */
    GLOSS_BLACK(100),

    /**
     * 亮红色
     */
    GLOSS_RED(101),

    /**
     * 亮绿色
     */
    GLOSS_GREEN(102),

    /**
     * 亮黄色
     */
    GLOSS_YELLOW(103),

    /**
     * 亮蓝色
     */
    GLOSS_BLUE(104),

    /**
     * 亮紫色
     */
    GLOSS_PURPLE(105),

    /**
     * 亮青色
     */
    GLOSS_CYAN(106),

    /**
     * 亮白色
     */
    GLOSS_WHITE(107),

    ;

    /**
     * 颜色码
     */
    public final int code;

    AnsiBackground(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code + Const.EMPTY;
    }

}
