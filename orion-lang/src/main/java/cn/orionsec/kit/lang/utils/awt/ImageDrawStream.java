/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.lang.utils.awt;

import cn.orionsec.kit.lang.able.Executable;
import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Colors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

/**
 * 图片绘制流
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/7/15 11:11
 */
public class ImageDrawStream implements Executable {

    private final Graphics2D g2d;

    public ImageDrawStream(BufferedImage image) {
        this(image.createGraphics());
    }

    public ImageDrawStream(Graphics2D g2d) {
        this.g2d = g2d;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    public static ImageDrawStream of(BufferedImage image) {
        return new ImageDrawStream(image);
    }

    public static ImageDrawStream of(Graphics2D g2d) {
        return new ImageDrawStream(g2d);
    }

    // -------------------- 绘制图片 --------------------

    public ImageDrawStream insertImageBase64(String base64) {
        return insertImage(Images.getImageBase64(base64), 0, 0);
    }

    public ImageDrawStream insertImageBase64(String base64, int x, int y) {
        return insertImage(Images.getImageBase64(base64), x, y);
    }

    public ImageDrawStream insertImage(InputStream in) {
        return insertImage(Images.getImage(in), 0, 0);
    }

    public ImageDrawStream insertImage(InputStream in, int x, int y) {
        return insertImage(Images.getImage(in), x, y);
    }

    public ImageDrawStream insertImage(File file) {
        return insertImage(Images.getImage(file), 0, 0);
    }

    public ImageDrawStream insertImage(File file, int x, int y) {
        return insertImage(Images.getImage(file), x, y);
    }

    public ImageDrawStream insertImage(byte[] img) {
        return insertImage(Images.getImage(img), 0, 0);
    }

    public ImageDrawStream insertImage(byte[] img, int x, int y) {
        return insertImage(Images.getImage(img), x, y);
    }

    public ImageDrawStream insertImage(BufferedImage img) {
        return insertImage(img, 0, 0);
    }

    /**
     * 插入图片
     *
     * @param img 图片
     * @param x   x坐标
     * @param y   y坐标
     * @return this
     */
    public ImageDrawStream insertImage(BufferedImage img, int x, int y) {
        g2d.drawImage(img, x, y, img.getWidth(), img.getHeight(), null);
        return this;
    }

    /**
     * 绘制文字
     *
     * @param s 文字
     * @param x x坐标
     * @param y y坐标
     * @return this
     */
    public ImageDrawStream insertString(String s, int x, int y) {
        g2d.drawString(s, x, y);
        return this;
    }

    /**
     * 绘制线
     *
     * @param p1x 点1 x坐标
     * @param p1y 点1 y坐标
     * @param p2x 点2 x坐标
     * @param p2y 点2 y坐标
     * @return this
     */
    public ImageDrawStream insertLine(int p1x, int p1y, int p2x, int p2y) {
        g2d.drawLine(p1x, p1y, p2x, p2y);
        return this;
    }

    // -------------------- 颜色和字体 --------------------

    /**
     * 设置颜色 RGB
     *
     * @param r R
     * @param g G
     * @param b B
     * @return this
     */
    public ImageDrawStream color(int r, int g, int b) {
        g2d.setColor(Colors.toColor(r, g, b));
        return this;
    }

    /**
     * 设置颜色 HEX
     *
     * @param hex HEX
     * @return this
     */
    public ImageDrawStream color(String hex) {
        g2d.setColor(Colors.toColor(hex));
        return this;
    }

    /**
     * 设置颜色
     *
     * @param color 颜色
     * @return this
     */
    public ImageDrawStream color(Color color) {
        g2d.setColor(color);
        return this;
    }

    /**
     * 设置字体
     *
     * @param font font
     * @return this
     */
    public ImageDrawStream font(Font font) {
        g2d.setFont(font);
        return this;
    }

    /**
     * 设置字体
     *
     * @param name 字体名称
     * @param size 字体大小
     * @return this
     */
    public ImageDrawStream font(String name, int size) {
        g2d.setFont(new Font(name, Font.PLAIN, size));
        return this;
    }

    /**
     * 设置字体
     *
     * @param name 字体名称
     * @return this
     */
    public ImageDrawStream font(String name) {
        g2d.setFont(new Font(name, Font.PLAIN, 18));
        return this;
    }

    /**
     * 设置字体
     *
     * @param size 字体大小
     * @return this
     */
    public ImageDrawStream font(int size) {
        g2d.setFont(new Font(Const.FONT_MICROSOFT_ELEGANT_BLACK, Font.PLAIN, size));
        return this;
    }

    @Override
    public void exec() {
        g2d.dispose();
    }

    public Graphics2D getGraphics() {
        return g2d;
    }

}
