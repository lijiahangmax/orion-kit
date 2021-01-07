package com.orion.utils;

import java.awt.*;

/**
 * 颜色工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/29 21:38
 */
public class Colors {

    private Colors() {
    }

    /**
     * Color > #
     *
     * @param color Color
     * @return #
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
     * # > RGB
     *
     * @param c Hex
     * @return RGB
     */
    public static byte[] toRgb(String c) {
        if (c == null) {
            return null;
        }
        try {
            int length = c.length();
            if (length == 4) {
                char c1 = c.charAt(1);
                char c2 = c.charAt(2);
                char c3 = c.charAt(3);
                c = "#" + c1 + c1 + c2 + c2 + c3 + c3;
            }
            String str1 = c.substring(1, 3);
            String str2 = c.substring(3, 5);
            String str3 = c.substring(5, 7);
            int red = Integer.parseInt(str1, 16);
            int green = Integer.parseInt(str2, 16);
            int blue = Integer.parseInt(str3, 16);
            return new byte[]{(byte) red, (byte) green, (byte) blue};
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    /**
     * # > RGB
     *
     * @param color Color
     * @return RGB
     */
    public static byte[] toRgb(Color color) {
        return new byte[]{(byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue()};
    }

    /**
     * # > Color
     *
     * @param c #
     * @return Color
     */
    public static Color toColor(String c) {
        if (c == null) {
            return null;
        }
        try {
            int length = c.length();
            if (length == 4) {
                char c1 = c.charAt(1);
                char c2 = c.charAt(2);
                char c3 = c.charAt(3);
                c = "#" + c1 + c1 + c2 + c2 + c3 + c3;
            }
            String str1 = c.substring(1, 3);
            String str2 = c.substring(3, 5);
            String str3 = c.substring(5, 7);
            int red = Integer.parseInt(str1, 16);
            int green = Integer.parseInt(str2, 16);
            int blue = Integer.parseInt(str3, 16);
            return new Color(red, green, blue);
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
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
        return toColor((byte) bs[0], (byte) bs[1], (byte) bs[2]);
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
        return toColor((byte) r, (byte) g, (byte) b);
    }

    /**
     * RGB > Color
     *
     * @param r R
     * @param g G
     * @param b B
     * @return Color
     */
    public static Color toColor(byte r, byte g, byte b) {
        return new Color(r, g, b);
    }

}
