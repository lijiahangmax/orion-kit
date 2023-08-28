package com.orion.lang.utils.ansi.style.color;

import com.orion.lang.utils.Colors;

import static com.orion.lang.utils.ansi.AnsiConst.*;

/**
 * ANSI bit24 颜色
 * <p>
 * 前景: 38:2:r;g;b
 * 背景: 48:2:r;g;b
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 16:40
 */
public class AnsiBit24Color extends AnsiBitColor {

    public AnsiBit24Color(String code) {
        super(code);
    }

    /**
     * 24bit 前景色
     *
     * @param r r
     * @param g g
     * @param b b
     * @return color
     */
    public static AnsiColor foreground(int r, int g, int b) {
        return new AnsiBit24Color(color(EXTENDED_FG, EXTENDED_BIT24, r, g, b));
    }

    public static AnsiColor foreground(int[] rgb) {
        return new AnsiBit24Color(color(EXTENDED_FG, EXTENDED_BIT24, rgb));
    }

    public static AnsiColor foreground(String hex) {
        return new AnsiBit24Color(color(EXTENDED_FG, EXTENDED_BIT24, Colors.toRgbColor(hex)));
    }

    /**
     * 24bit 背景色
     *
     * @param r r
     * @param g g
     * @param b b
     * @return color
     */
    public static AnsiColor background(int r, int g, int b) {
        return new AnsiBit24Color(color(EXTENDED_BG, EXTENDED_BIT24, r, g, b));
    }

    public static AnsiColor background(int[] rgb) {
        return new AnsiBit24Color(color(EXTENDED_BG, EXTENDED_BIT24, rgb));
    }

    public static AnsiColor background(String hex) {
        return new AnsiBit24Color(color(EXTENDED_BG, EXTENDED_BIT24, Colors.toRgbColor(hex)));
    }

}
