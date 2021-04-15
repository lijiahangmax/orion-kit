package com.orion.utils.code;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * 条形码工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/7/8 17:15
 */
public class BarCodes extends CodeGenerator {

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
            Exceptions.printStacks(e);
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
            Exceptions.printStacks(e);
        }
    }

}
