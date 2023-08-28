package com.orion.lang.utils;

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

}
