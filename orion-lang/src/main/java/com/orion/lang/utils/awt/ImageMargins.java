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
package com.orion.lang.utils.awt;

import com.orion.lang.able.Awaitable;
import com.orion.lang.utils.Colors;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图片边距设置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/7/23 14:22
 */
public class ImageMargins implements Awaitable<BufferedImage> {

    /**
     * 图片
     */
    private final BufferedImage image;

    /**
     * 边距色
     */
    private Color color;

    /**
     * 是否为透明背景
     */
    private boolean transparent;

    /**
     * 边距
     */
    private int topMargin, rightMargin, bottomMargin, leftMargin;

    public ImageMargins(BufferedImage image) {
        this.image = image;
    }

    @Override
    public BufferedImage await() {
        int width = image.getWidth();
        int height = image.getHeight();
        int newWidth = width + rightMargin + leftMargin;
        int newHeight = height + topMargin + bottomMargin;
        BufferedImage s;
        if (transparent) {
            s = Images.getTransparentImage(newWidth, newHeight);
        } else {
            s = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        }
        Graphics2D g2d = s.createGraphics();
        if (!transparent) {
            if (color == null) {
                g2d.setColor(Color.WHITE);
            } else {
                g2d.setColor(color);
            }
            g2d.fillRect(0, 0, newWidth, newHeight);
        }
        g2d.drawImage(image, leftMargin, topMargin, width, height, null);
        g2d.dispose();
        return s;
    }

    /**
     * 设置透明背景
     *
     * @return this
     */
    public ImageMargins transparent() {
        this.transparent = true;
        return this;
    }

    /**
     * 设置颜色 RGB
     *
     * @param r R
     * @param g G
     * @param b B
     * @return this
     */
    public ImageMargins color(int r, int g, int b) {
        this.color = Colors.toColor(r, g, b);
        this.transparent = false;
        return this;
    }

    /**
     * 设置颜色 HEX
     *
     * @param hex HEX
     * @return this
     */
    public ImageMargins color(String hex) {
        this.color = Colors.toColor(hex);
        this.transparent = false;
        return this;
    }

    /**
     * 设置颜色
     *
     * @param color 颜色
     * @return this
     */
    public ImageMargins color(Color color) {
        this.color = color;
        this.transparent = false;
        return this;
    }

    /**
     * 设置边距
     *
     * @param margin 边距
     * @return this
     */
    public ImageMargins margin(int margin) {
        this.topMargin = margin;
        this.rightMargin = margin;
        this.bottomMargin = margin;
        this.leftMargin = margin;
        return this;
    }

    /**
     * 设置边距
     *
     * @param topBottom 上下边距
     * @param rightLeft 右左边距
     * @return this
     */
    public ImageMargins margin(int topBottom, int rightLeft) {
        this.topMargin = topBottom;
        this.rightMargin = rightLeft;
        this.bottomMargin = topBottom;
        this.leftMargin = rightLeft;
        return this;
    }

    /**
     * 设置边距
     *
     * @param topMargin    上边距
     * @param rightMargin  右边距
     * @param bottomMargin 下边距
     * @param leftMargin   左边距
     * @return this
     */
    public ImageMargins margin(int topMargin, int rightMargin, int bottomMargin, int leftMargin) {
        this.topMargin = topMargin;
        this.rightMargin = rightMargin;
        this.bottomMargin = bottomMargin;
        this.leftMargin = leftMargin;
        return this;
    }

    /**
     * 设置上边距
     *
     * @param topMargin 边距
     * @return this
     */
    public ImageMargins topMargin(int topMargin) {
        this.topMargin = topMargin;
        return this;
    }

    /**
     * 设置右边距
     *
     * @param rightMargin 边距
     * @return this
     */
    public ImageMargins rightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
        return this;
    }

    /**
     * 设置下边距
     *
     * @param bottomMargin 边距
     * @return this
     */
    public ImageMargins bottomMargin(int bottomMargin) {
        this.bottomMargin = bottomMargin;
        return this;
    }

    /**
     * 设置左边距
     *
     * @param leftMargin 边距
     * @return this
     */
    public ImageMargins leftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
        return this;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Color getColor() {
        return color;
    }

    public int getTopMargin() {
        return topMargin;
    }

    public int getRightMargin() {
        return rightMargin;
    }

    public int getBottomMargin() {
        return bottomMargin;
    }

    public int getLeftMargin() {
        return leftMargin;
    }

}
