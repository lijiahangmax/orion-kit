package com.orion.lang.utils.ansi.style.color;

import com.orion.lang.constant.Const;

/**
 * ANSI 前景色
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/20 10:51
 */
public enum AnsiForeground implements AnsiColor {

    /**
     * 黑色
     */
    BLACK(30),

    /**
     * 红色
     */
    RED(31),

    /**
     * 绿色
     */
    GREEN(32),

    /**
     * 黄色
     */
    YELLOW(33),

    /**
     * 蓝色
     */
    BLUE(34),

    /**
     * 紫色
     */
    PURPLE(35),

    /**
     * 青色
     */
    CYAN(36),

    /**
     * 白色
     */
    WHITE(37),

    /**
     * 默认
     */
    DEFAULT(39),


    // -------------------- 亮色 --------------------

    /**
     * 亮黑色 (灰)
     */
    BRIGHT_BLACK(90),

    /**
     * 亮红色
     */
    BRIGHT_RED(91),

    /**
     * 亮绿色
     */
    BRIGHT_GREEN(92),

    /**
     * 亮黄色
     */
    BRIGHT_YELLOW(93),

    /**
     * 亮蓝色
     */
    BRIGHT_BLUE(94),

    /**
     * 亮紫色
     */
    BRIGHT_PURPLE(95),

    /**
     * 亮青色
     */
    BRIGHT_CYAN(96),

    /**
     * 亮白色
     */
    BRIGHT_WHITE(97),

    ;

    /**
     * 颜色码
     */
    public final int code;

    AnsiForeground(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code + Const.EMPTY;
    }

}
