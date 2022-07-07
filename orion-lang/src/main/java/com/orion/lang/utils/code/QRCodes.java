package com.orion.lang.utils.code;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.io.Streams;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 二维码工具类
 * <p>
 * 如果logo图大于二维码的大小那么logo会覆盖二维码, 这时必须压缩logo
 * 绘制logo的话 纠错等级需要提高 否则无法解析
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/7/8 21:19
 */
public class QRCodes extends CodeGenerator {

    /**
     * logo宽度
     */
    private int logoWidth;

    /**
     * logo高度
     */
    private int logoHeight;

    /**
     * logo
     */
    private Image logo;

    /**
     * 是否压缩logo
     */
    private boolean logoCompress;

    public QRCodes() {
        this.format = BarcodeFormat.QR_CODE;
        this.width = 300;
        this.height = 300;
        this.logoWidth = 60;
        this.logoHeight = 60;
        this.wordsTopMargin = -5;
        this.wordsButtonMargin = 10;
    }

    /**
     * 压缩logo
     *
     * @return this
     */
    public QRCodes logoCompress() {
        this.logoCompress = true;
        if (logo != null) {
            int width = logo.getWidth(null);
            int height = logo.getHeight(null);
            if (width > this.logoWidth) {
                width = this.logoWidth;
            }
            if (height > this.logoHeight) {
                height = this.logoHeight;
            }
            Image image = logo.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            // 绘制缩小后的图
            g.drawImage(image, 0, 0, null);
            g.dispose();
            logo = image;
        }
        return this;
    }

    @Override
    protected BufferedImage getBufferedImage(String content) {
        try {
            BitMatrix bitMatrix = WRITER.encode(content, format, width, height, this.getEncodeHint());
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            return image;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 插入logo
     *
     * @param source source
     */
    private void insertLogo(BufferedImage source) {
        int w = logo.getWidth(null);
        int h = logo.getHeight(null);
        // 插入logo
        Graphics2D graph = source.createGraphics();
        int x = (width - w) / 2;
        int y = (height - h) / 2;
        graph.drawImage(logo, x, y, w, h, null);
        Shape shape = new RoundRectangle2D.Float(x, y, w, w, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
        source.flush();
    }

    @Override
    public void encode(String content, String words, OutputStream out) {
        try {
            BufferedImage image = this.getBufferedImage(content);
            if (this.logo != null) {
                insertLogo(image);
            }
            if (!Strings.isBlank(words)) {
                image = this.insertWords(image, words);
            }
            ImageIO.write(image, suffix, out);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- setter --------------------

    @Override
    public QRCodes width(int width) {
        return this.size(width);
    }

    @Override
    public QRCodes height(int height) {
        return this.size(height);
    }

    @Override
    public QRCodes size(int width, int height) {
        return this.size(width);
    }

    public QRCodes size(int size) {
        this.width = size;
        this.height = size;
        return this;
    }

    public QRCodes logoWidth(int logoWidth) {
        this.logoWidth = logoWidth;
        return this;
    }

    public QRCodes logoHeight(int logoHeight) {
        this.logoHeight = logoHeight;
        return this;
    }

    public QRCodes logoSize(int size) {
        this.logoWidth = size;
        this.logoHeight = size;
        return this;
    }

    public QRCodes logo(String file) {
        try {
            return this.logo(ImageIO.read(new File(file)));
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public QRCodes logo(File file) {
        try {
            return this.logo(ImageIO.read(file));
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public QRCodes logo(InputStream in) {
        try {
            return this.logo(ImageIO.read(in));
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public QRCodes logo(byte[] bs) {
        try {
            return this.logo(ImageIO.read(Streams.toInputStream(bs)));
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public QRCodes logo(Image logo) {
        this.logo = logo;
        if (logoCompress) {
            this.logoCompress();
        }
        this.errorCorrectionLevel = 4;
        encodeHint.put(EncodeHintType.ERROR_CORRECTION, this.toErrorCorrectionLevel());
        return this;
    }

    // -------------------- getter --------------------

    public int getLogoWidth() {
        return logoWidth;
    }

    public int getLogoHeight() {
        return logoHeight;
    }

    public Image getLogo() {
        return logo;
    }

    public boolean isLogoCompress() {
        return logoCompress;
    }

}
