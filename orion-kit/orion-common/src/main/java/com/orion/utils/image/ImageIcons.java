package com.orion.utils.image;

import com.orion.able.Processable;
import com.orion.utils.Colors;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * 图标生成器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/7/23 16:03
 */
public class ImageIcons implements Processable<Character, BufferedImage> {

    /**
     * 图标大小
     */
    private int size = 60;

    /**
     * 背景颜色
     */
    private Color color = Colors.toColor("#3A76BE");

    /**
     * 字体颜色
     */
    private Color fontColor = Color.WHITE;

    /**
     * 字体名称
     */
    private String fontName = "宋体";

    /**
     * 字体大小
     */
    private int fontSize;

    /**
     * 字体绘制的X坐标
     */
    private float fontPointX;

    /**
     * 字体绘制的Y坐标
     */
    private float fontPointY;

    /**
     * base64编码图标
     *
     * @param c 字符
     * @return base64
     */
    public String executeBase64(Character c) {
        return Images.base64Encode(this.execute(c));
    }

    /**
     * base64编码图标
     *
     * @param c      字符
     * @param format 格式
     * @return base64
     */
    public String executeBase64(Character c, String format) {
        return Images.base64Encode(this.execute(c), format);
    }

    @Override
    public BufferedImage execute(Character c) {
        BufferedImage img = Images.getTransparentImage(size, size);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.fill(new RoundRectangle2D.Float(0, 0, size, size, size, size));
        g2d.setColor(fontColor);
        if (fontSize != 0) {
            int i = (size - fontSize) / 2;
            g2d.setFont(new Font(fontName, Font.PLAIN, fontSize));
            if (fontPointX != 0 && fontPointY != 0) {
                g2d.drawString(c.toString(), fontPointX, fontPointY);
            } else {
                g2d.drawString(c.toString(), i, (float) (size - i * 1.5));
            }
        } else {
            int i = size / 6;
            g2d.setFont(new Font(fontName, Font.PLAIN, i * 4));
            if (fontPointX != 0 && fontPointY != 0) {
                g2d.drawString(c.toString(), fontPointX, fontPointY);
            } else {
                g2d.drawString(c.toString(), i, (float) (size - i * 1.5));
            }
        }
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

    public ImageIcons font(String fontName, int fontSize, Color fontColor) {
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        return this;
    }

    public ImageIcons fontColor(Color fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    public ImageIcons fontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    public ImageIcons fontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public ImageIcons fontPoint(int x, int y) {
        this.fontPointX = x;
        this.fontPointY = y;
        return this;
    }

    public ImageIcons fontPoint(float x, float y) {
        this.fontPointX = x;
        this.fontPointY = y;
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

    public String getFontName() {
        return fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public float getFontPointX() {
        return fontPointX;
    }

    public float getFontPointY() {
        return fontPointY;
    }

}
