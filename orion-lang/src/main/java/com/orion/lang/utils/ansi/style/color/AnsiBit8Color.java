package com.orion.lang.utils.ansi.style.color;

import static com.orion.lang.utils.ansi.AnsiConst.*;

/**
 * ANSI bit8 颜色
 * <p>
 * 前景: 38:5:code
 * 背景: 48:5:code
 * <p>
 * 0 - 7:     标准色彩
 * 8 - 15:    高亮色彩
 * 16 - 231:  6 × 6 × 6 RGB (216 种颜色): 16 + 36 × r + 6 × g + b (0 ≤ r, g, b ≤ 5)
 * 232 - 255: 从黑到白 24 级灰度
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 16:40
 */
public class AnsiBit8Color extends AnsiBitColor {

    public AnsiBit8Color(String code) {
        super(code);
    }

    /**
     * 8bit 前景色
     *
     * @param color color
     * @return color
     */
    public static AnsiColor foreground(int color) {
        return new AnsiBit8Color(color(COLOR_FG, COLOR_BIT8, color));
    }

    /**
     * 8bit 背景色
     *
     * @param color color
     * @return color
     */
    public static AnsiColor background(int color) {
        return new AnsiBit8Color(color(COLOR_BG, COLOR_BIT8, color));
    }

}
