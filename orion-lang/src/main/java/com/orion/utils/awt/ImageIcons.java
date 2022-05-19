package com.orion.utils.awt;

import com.orion.able.Processable;
import com.orion.utils.Chars;
import com.orion.utils.Colors;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * 图标生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/7/23 16:03
 */
public class ImageIcons implements Processable<Character, BufferedImage> {

    /**
     * 图标大小
     */
    private int size;

    /**
     * 背景颜色
     */
    private Color color;

    /**
     * 字体颜色
     */
    private Color fontColor;

    /**
     * 字体名称
     */
    private Font font;

    /**
     * 图标是否为正方形
     */
    private boolean square;

    public ImageIcons() {
        this.size = 60;
        this.color = Colors.toColor("#3A76BE");
        this.fontColor = Color.WHITE;
        this.font = new Font("宋体", Font.PLAIN | Font.BOLD, 45);
    }

    public static ImageIcons create() {
        return new ImageIcons();
    }

    /**
     * base64编码图标
     *
     * @param s 字符
     * @return base64
     */
    public String executeBase64(Character s) {
        return Images.base64Encode(this.execute(s));
    }

    /**
     * base64编码图标
     *
     * @param s      字符
     * @param format 格式
     * @return base64
     */
    public String executeBase64(Character s, String format) {
        return Images.base64Encode(this.execute(s), format);
    }

    @Override
    public BufferedImage execute(Character c) {
        BufferedImage img = Images.getTransparentImage(size, size);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(color);
        if (square) {
            // 正方形
            g2d.fill(new RoundRectangle2D.Float(0, 0, size, size, 0, 0));
        } else {
            // 圆形
            g2d.fill(new RoundRectangle2D.Float(0, 0, size, size, size, size));
        }
        g2d.setColor(fontColor);
        g2d.setFont(font);
        int[] wh = Fonts.getWidthHeightPixel(g2d, font, c.toString());
        int width = wh[0], height = wh[1];
        int x, y;
        x = (size - width) / 2;
        if (Chars.isAscii(c)) {
            y = (size - height) / 2 + height;
        } else {
            y = (((size - height) / 2) + height) - (font.getSize() - height);
        }
        g2d.drawString(c.toString(), x - 2F, y - 2);
        g2d.dispose();
        return img;
    }

    public ImageIcons size(int size) {
        this.size = size;
        return this;
    }

    public ImageIcons color(Color color) {
        this.color = color;
        return this;
    }

    public ImageIcons font(String fontName, int fontSize) {
        this.font = new Font(fontName, Font.PLAIN, fontSize);
        return this;
    }

    public ImageIcons font(Font font) {
        this.font = font;
        return this;
    }

    public ImageIcons fontColor(Color fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    /**
     * 设置为正方形
     *
     * @return this
     */
    public ImageIcons square() {
        this.square = true;
        return this;
    }

    /**
     * 设置为圆形
     *
     * @return this
     */
    public ImageIcons circular() {
        this.square = false;
        return this;
    }

    public int getSize() {
        return size;
    }

    public Color getColor() {
        return color;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public Font getFont() {
        return font;
    }

    public boolean isSquare() {
        return square;
    }

}
