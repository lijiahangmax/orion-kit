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
package cn.orionsec.kit.lang.utils.code;

import cn.orionsec.kit.lang.utils.Strings;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * 条形码工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/7/8 17:15
 */
public class BarCodes extends CodeGenerator {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BarCodes.class);

    public BarCodes() {
        this.format = BarcodeFormat.CODE_128;
        this.width = 200;
        this.height = 30;
        this.imgTopMargin = 10;
        this.imgButtonMargin = 10;
        this.wordsTopMargin = -5;
    }

    @Override
    protected BufferedImage getBufferedImage(String content) {
        try {
            BitMatrix bitMatrix = WRITER.encode(content, format, width, height, this.getEncodeHint());
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            return null;
        }
    }

    /**
     * 设置边距
     *
     * @param image image
     * @return BufferedImage
     */
    protected BufferedImage setMargin(BufferedImage image) {
        int newHeight = imgTopMargin + height + imgButtonMargin;
        BufferedImage outImage = new BufferedImage(width, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outImage.createGraphics();
        // 设置颜色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, newHeight);
        g2d.setColor(Color.BLACK);
        // 画条形码
        g2d.drawImage(image, 0, imgTopMargin, image.getWidth(), image.getHeight(), null);
        g2d.dispose();
        outImage.flush();
        return outImage;
    }

    @Override
    public void encode(String content, String words, OutputStream out) {
        try {
            BufferedImage image = getBufferedImage(content);
            if (!Strings.isBlank(words)) {
                image = this.insertWords(image, words);
            } else if (wordsTopMargin != 0 || wordsButtonMargin != 0) {
                image = this.setMargin(image);
            }
            ImageIO.write(image, suffix, out);
        } catch (Exception e) {
            LOGGER.error("BarCodes.encode error", e);
        }
    }

}
