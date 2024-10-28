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

import cn.orionsec.kit.lang.utils.io.Streams;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.resizers.configurations.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 图片处理流
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/7/14 17:20
 */
public class ImageExecutorStream {

    /**
     * 文件格式 如果有的话
     */
    private String format;

    private final Thumbnails.Builder<?> builder;

    public ImageExecutorStream(String name) {
        builder = Thumbnails.of(new File(name));
    }

    public ImageExecutorStream(File file) {
        builder = Thumbnails.of(file);
    }

    public ImageExecutorStream(InputStream in) {
        builder = Thumbnails.of(in);
    }

    public ImageExecutorStream(byte[] bs) {
        builder = Thumbnails.of(Streams.toInputStream(bs));
    }

    public static ImageExecutorStream of(String name) {
        return new ImageExecutorStream(name);
    }

    public static ImageExecutorStream of(File file) {
        return new ImageExecutorStream(file);
    }

    public static ImageExecutorStream of(InputStream in) {
        return new ImageExecutorStream(in);
    }

    public static ImageExecutorStream of(byte[] bs) {
        return new ImageExecutorStream(bs);
    }

    /**
     * 设置大小
     *
     * @param width  宽
     * @param height 高
     * @return this
     */
    public ImageExecutorStream size(int width, int height) {
        builder.size(width, height);
        return this;
    }

    /**
     * 设置宽
     *
     * @param width 宽
     * @return this
     */
    public ImageExecutorStream width(int width) {
        builder.width(width);
        return this;
    }

    /**
     * 设置高
     *
     * @param height 高
     * @return this
     */
    public ImageExecutorStream height(int height) {
        builder.height(height);
        return this;
    }

    /**
     * 设置旋转角度
     *
     * @param angle 旋转角度 顺时针正数 逆时针负数
     * @return this
     */
    public ImageExecutorStream rotate(double angle) {
        builder.rotate(angle);
        return this;
    }

    /**
     * 设置逆时针旋转90度
     *
     * @return this
     */
    public ImageExecutorStream rotateLeft() {
        builder.rotate(-90);
        return this;
    }

    /**
     * 设置顺时针旋转90度
     *
     * @return this
     */
    public ImageExecutorStream rotateRight() {
        builder.rotate(90);
        return this;
    }

    /**
     * 设置 缩放比例 0 - 1
     *
     * @param scale 缩放比例
     * @return this
     */
    public ImageExecutorStream scale(double scale) {
        builder.scale(scale);
        return this;
    }

    /**
     * 设置是否保留缩放比例
     * 如果手动设置宽高 则需要设置为false
     *
     * @param keep true保留
     * @return this
     */
    public ImageExecutorStream keepAspectRatio(boolean keep) {
        builder.keepAspectRatio(keep);
        return this;
    }

    /**
     * 设置不保留缩放比例 当手动设置宽高时调用
     *
     * @return this
     */
    public ImageExecutorStream skipAspectRatio() {
        builder.keepAspectRatio(false);
        return this;
    }

    /**
     * 设置输出文件格式化后缀 当输入文件与输出文件格式不一样时需调用
     *
     * @param format 后缀
     * @return this
     */
    public ImageExecutorStream format(String format) {
        this.format = format;
        builder.outputFormat(format);
        return this;
    }

    /**
     * 设置图片输出质量
     *
     * @param quality 质量 0 - 1
     * @return this
     */
    public ImageExecutorStream quality(double quality) {
        builder.outputQuality(quality);
        return this;
    }

    /**
     * 设置是否覆盖输出文件
     *
     * @param overWrite true覆盖
     * @return this
     */
    public ImageExecutorStream overWrite(boolean overWrite) {
        builder.allowOverwrite(overWrite);
        return this;
    }

    /**
     * 设置不覆盖输出文件
     *
     * @return this
     */
    public ImageExecutorStream unOverWrite() {
        builder.allowOverwrite(false);
        return this;
    }

    /**
     * 设置缩放时的模式
     *
     * @param scalingMode 缩放模式
     * @return this
     */
    public ImageExecutorStream scalingMode(ScalingMode scalingMode) {
        builder.scalingMode(scalingMode);
        return this;
    }

    /**
     * 设置调整大小时的渲染模式
     *
     * @param rendering 渲染模式
     * @return this
     */
    public ImageExecutorStream rendering(Rendering rendering) {
        builder.rendering(rendering);
        return this;
    }

    /**
     * 设置插值模式
     *
     * @param alphaInterpolation alphaInterpolation
     * @return this
     */
    public ImageExecutorStream alphaInterpolation(AlphaInterpolation alphaInterpolation) {
        builder.alphaInterpolation(alphaInterpolation);
        return this;
    }

    /**
     * 设置抗锯齿
     *
     * @param antialias 抗锯齿
     * @return this
     */
    public ImageExecutorStream antialias(Antialiasing antialias) {
        builder.antialiasing(antialias);
        return this;
    }

    /**
     * 设置抖动模式
     *
     * @param dithering dithering
     * @return this
     */
    public ImageExecutorStream dithering(Dithering dithering) {
        builder.dithering(dithering);
        return this;
    }

    // -------------------- 水印 --------------------

    public ImageExecutorStream watermark(byte[] bs) throws IOException {
        builder.watermark(ImageIO.read(Streams.toInputStream(bs)));
        return this;
    }

    public ImageExecutorStream watermark(File file) throws IOException {
        builder.watermark(ImageIO.read(file));
        return this;
    }

    public ImageExecutorStream watermark(InputStream in) throws IOException {
        builder.watermark(ImageIO.read(in));
        return this;
    }

    public ImageExecutorStream watermark(BufferedImage image) {
        builder.watermark(image);
        return this;
    }

    public ImageExecutorStream watermark(byte[] bs, float opacity) throws IOException {
        builder.watermark(ImageIO.read(Streams.toInputStream(bs)), opacity);
        return this;
    }

    public ImageExecutorStream watermark(File file, float opacity) throws IOException {
        builder.watermark(ImageIO.read(file), opacity);
        return this;
    }

    public ImageExecutorStream watermark(InputStream in, float opacity) throws IOException {
        builder.watermark(ImageIO.read(in), opacity);
        return this;
    }

    public ImageExecutorStream watermark(BufferedImage image, float opacity) {
        builder.watermark(image, opacity);
        return this;
    }

    public ImageExecutorStream watermark(int x, int y, byte[] bs) throws IOException {
        builder.watermark((enclosingWidth, enclosingHeight, width, height, insetLeft, insetRight, insetTop, insetBottom) -> new Point(x, y), ImageIO.read(Streams.toInputStream(bs)), 0.5F);
        return this;
    }

    public ImageExecutorStream watermark(int x, int y, File file) throws IOException {
        builder.watermark((enclosingWidth, enclosingHeight, width, height, insetLeft, insetRight, insetTop, insetBottom) -> new Point(x, y), ImageIO.read(file), 0.5F);
        return this;
    }

    public ImageExecutorStream watermark(int x, int y, InputStream in) throws IOException {
        builder.watermark((enclosingWidth, enclosingHeight, width, height, insetLeft, insetRight, insetTop, insetBottom) -> new Point(x, y), ImageIO.read(in), 0.5F);
        return this;
    }

    public ImageExecutorStream watermark(int x, int y, BufferedImage image) {
        builder.watermark((enclosingWidth, enclosingHeight, width, height, insetLeft, insetRight, insetTop, insetBottom) -> new Point(x, y), image, 0.5F);
        return this;
    }

    public ImageExecutorStream watermark(int x, int y, byte[] bs, float opacity) throws IOException {
        builder.watermark((enclosingWidth, enclosingHeight, width, height, insetLeft, insetRight, insetTop, insetBottom) -> new Point(x, y), ImageIO.read(Streams.toInputStream(bs)), opacity);
        return this;
    }

    public ImageExecutorStream watermark(int x, int y, File file, float opacity) throws IOException {
        builder.watermark((enclosingWidth, enclosingHeight, width, height, insetLeft, insetRight, insetTop, insetBottom) -> new Point(x, y), ImageIO.read(file), opacity);
        return this;
    }

    public ImageExecutorStream watermark(int x, int y, InputStream in, float opacity) throws IOException {
        builder.watermark((enclosingWidth, enclosingHeight, width, height, insetLeft, insetRight, insetTop, insetBottom) -> new Point(x, y), ImageIO.read(in), opacity);
        return this;
    }

    public ImageExecutorStream watermark(int x, int y, BufferedImage image, float opacity) {
        builder.watermark((enclosingWidth, enclosingHeight, width, height, insetLeft, insetRight, insetTop, insetBottom) -> new Point(x, y), image, opacity);
        return this;
    }

    public ImageExecutorStream watermark(Position p, byte[] bs) throws IOException {
        builder.watermark(p, ImageIO.read(Streams.toInputStream(bs)), 0.5F);
        return this;
    }

    public ImageExecutorStream watermark(Position p, File file) throws IOException {
        builder.watermark(p, ImageIO.read(file), 0.5F);
        return this;
    }

    public ImageExecutorStream watermark(Position p, InputStream in) throws IOException {
        builder.watermark(p, ImageIO.read(in), 0.5F);
        return this;
    }

    public ImageExecutorStream watermark(Position p, BufferedImage image) {
        builder.watermark(p, image, 0.5F);
        return this;
    }

    public ImageExecutorStream watermark(Position p, byte[] bs, float opacity) throws IOException {
        builder.watermark(p, ImageIO.read(Streams.toInputStream(bs)), opacity);
        return this;
    }

    public ImageExecutorStream watermark(Position p, File file, float opacity) throws IOException {
        builder.watermark(p, ImageIO.read(file), opacity);
        return this;
    }

    public ImageExecutorStream watermark(Position p, InputStream in, float opacity) throws IOException {
        builder.watermark(p, ImageIO.read(in), opacity);
        return this;
    }

    /**
     * 添加水印
     *
     * @param p       位置信息
     * @param image   水印文件
     * @param opacity 不透明度
     * @return this
     */
    public ImageExecutorStream watermark(Position p, BufferedImage image, float opacity) {
        builder.watermark(p, image, opacity);
        return this;
    }

    // -------------------- 裁剪 --------------------

    /**
     * 剪裁图片
     * 需要先调用 {{@link #size(int, int)}} 方法
     *
     * @return this
     */
    public ImageExecutorStream crop() {
        builder.crop((enclosingWidth, enclosingHeight, width, height, insetLeft, insetRight, insetTop, insetBottom) -> new Point());
        return this;
    }

    public ImageExecutorStream sourceRegion(int width, int height) {
        builder.sourceRegion(0, 0, width, height);
        return this;
    }

    /**
     * 设置缩放源图片大小
     * 需要先调用 {{@link #scale(double)} 方法
     *
     * @param x      横坐标
     * @param y      纵坐标
     * @param width  剪裁宽
     * @param height 剪裁高
     * @return this
     */
    public ImageExecutorStream sourceRegion(int x, int y, int width, int height) {
        builder.sourceRegion(x, y, width, height);
        return this;
    }

    /**
     * 处理 输出到文件
     *
     * @param file 文件
     * @throws IOException IOException
     */
    public void execute(File file) throws IOException {
        builder.toFile(file);
    }

    /**
     * 处理 输出到文件
     *
     * @param file 文件路径
     * @throws IOException IOException
     */
    public void execute(String file) throws IOException {
        builder.toFile(file);
    }

    /**
     * 处理 输出到流
     *
     * @param out 输出流
     * @throws IOException IOException
     */
    public void execute(OutputStream out) throws IOException {
        builder.toOutputStream(out);
    }

    /**
     * 转化为 BufferedImage
     *
     * @return BufferedImage
     * @throws IOException IOException
     */
    public BufferedImage getImage() throws IOException {
        return builder.asBufferedImage();
    }

    /**
     * 转化为 base64
     *
     * @return base64
     * @throws IOException IOException
     */
    public String getBase64() throws IOException {
        if (this.format == null) {
            return Images.base64Encode(builder.asBufferedImage());
        } else {
            return Images.base64Encode(builder.asBufferedImage(), this.format);
        }
    }

    public Thumbnails.Builder<?> getBuilder() {
        return builder;
    }

}
