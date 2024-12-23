/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.lang.utils;

import java.awt.*;

/**
 * 颜色工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/29 21:38
 */
public class Colors {

    private Colors() {
    }

    /**
     * Color > HEX
     *
     * @param color color
     * @return HEX
     */
    public static String toHex(Color color) {
        String r, g, b;
        StringBuilder su = new StringBuilder();
        r = Integer.toHexString(color.getRed());
        g = Integer.toHexString(color.getGreen());
        b = Integer.toHexString(color.getBlue());
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        r = r.toUpperCase();
        g = g.toUpperCase();
        b = b.toUpperCase();
        su.append("#");
        su.append(r);
        su.append(g);
        su.append(b);
        return su.toString();
    }

    /**
     * HEX > RGB
     *
     * @param hex Hex
     * @return RGB
     */
    public static int[] toRgbColor(String hex) {
        if (hex == null) {
            return null;
        }
        try {
            int length = hex.length();
            if (length == 4) {
                char c1 = hex.charAt(1);
                char c2 = hex.charAt(2);
                char c3 = hex.charAt(3);
                hex = "#" + c1 + c1 + c2 + c2 + c3 + c3;
            }
            String str1 = hex.substring(1, 3);
            String str2 = hex.substring(3, 5);
            String str3 = hex.substring(5, 7);
            int red = Integer.parseInt(str1, 16);
            int green = Integer.parseInt(str2, 16);
            int blue = Integer.parseInt(str3, 16);
            return new int[]{red, green, blue};
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * HEX > RGB
     *
     * @param hex Hex
     * @return RGB
     */
    public static byte[] toRgb(String hex) {
        int[] color = toRgbColor(hex);
        if (color == null) {
            return null;
        }
        return new byte[]{(byte) color[0], (byte) color[1], (byte) color[2]};
    }

    /**
     * HEX > Color
     *
     * @param hex HEX
     * @return Color
     */
    public static Color toColor(String hex) {
        int[] rgb = toRgbColor(hex);
        if (rgb == null) {
            return null;
        }
        return toColor(rgb);
    }

    /**
     * RGB > Color
     *
     * @param bs RGB
     * @return Color
     */
    public static Color toColor(byte[] bs) {
        return toColor(bs[0], bs[1], bs[2]);
    }

    /**
     * RGB > Color
     *
     * @param bs RGB
     * @return Color
     */
    public static Color toColor(int[] bs) {
        return toColor(bs[0], bs[1], bs[2]);
    }

    /**
     * RGB > Color
     *
     * @param r R
     * @param g G
     * @param b B
     * @return Color
     */
    public static Color toColor(int r, int g, int b) {
        return new Color(r, g, b);
    }

    /**
     * 是否为深色
     * 亮度值 < 128 ? 深色 : 128
     *
     * @param hex hex
     * @return 是否为深色
     */
    public static boolean isDarkColor(String hex) {
        int[] rgb = toRgbColor(hex);
        return isDarkColor(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * 是否为深色
     * 亮度值 < 128 ? 深色 : 128
     *
     * @param r r
     * @param g g
     * @param b b
     * @return 是否为深色
     */
    public static boolean isDarkColor(int r, int g, int b) {
        return (r * 299 + g * 587 + b * 114) / 1000 < 128;
    }

    /**
     * 调整颜色
     *
     * @param hex   hex
     * @param range 正数越浅 负数越深
     * @return hex
     */
    public static String adjustColor(String hex, int range) {
        int[] rgb = toRgbColor(hex);
        StringBuilder newColor = new StringBuilder("#");
        for (int c : rgb) {
            c += range;
            if (c < 0) {
                c = 0;
            } else if (c > 255) {
                c = 255;
            }
            newColor.append(Strings.leftPad(Integer.toString(c, 16), 2, "0"));
        }
        return newColor.toString();
    }

}
