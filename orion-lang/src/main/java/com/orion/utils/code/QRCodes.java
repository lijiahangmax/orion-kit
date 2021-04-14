package com.orion.utils.code;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.orion.constant.Const;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.codec.Base64s;
import com.orion.utils.image.Images;
import com.orion.utils.io.Streams;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具类
 * <p>
 * 如果logo图大于二维码的大小那么logo会覆盖二维码, 这时必须压缩logo
 * 绘制logo的话 纠错等级需要提高 否则无法解析
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/7/8 21:19
 */
public class QRCodes {

    /**
     * 格式
     */
    private BarcodeFormat format;

    /**
     * 后缀
     */
    private String suffix;

    /**
     * 编码
     */
    private String charset;

    /**
     * 解析x坐标
     */
    private int parserX;

    /**
     * 解析y坐标
     */
    private int parserY;

    /**
     * 二维码尺寸
     */
    private int size;

    /**
     * 纠错等级
     * 1 ~7%
     * 2 ~15%
     * 3 ~25%
     * 4 ~30%
     */
    private int errorCorrectionLevel;

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

    /**
     * 二维码文字宽
     */
    private int wordsWidth;

    /**
     * 二维码文字高
     */
    private int wordsHeight;

    /**
     * 字体名称
     */
    private String wordsFontName;

    /**
     * 字体大小
     */
    private int wordsFontSize;

    /**
     * 上边距
     */
    private int wordsTopMargin;

    /**
     * 下边距
     */
    private int wordsButtonMargin;

    public QRCodes() {
        this.format = BarcodeFormat.QR_CODE;
        this.suffix = Const.SUFFIX_PNG;
        this.charset = Const.UTF_8;
        this.size = 300;
        this.errorCorrectionLevel = 1;
        this.logoWidth = 60;
        this.logoHeight = 60;
        this.wordsWidth = size;
        this.wordsHeight = 10;
        this.wordsFontName = Const.FONT_MICROSOFT_ELEGANT_BLACK;
        this.wordsFontSize = 18;
        this.wordsTopMargin = 10;
        this.wordsButtonMargin = 20;
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

    /**
     * 创建 BufferedImage
     *
     * @param content 二维码内容
     * @return BufferedImage
     */
    public BufferedImage getBufferedImage(String content) {
        try {
            Map<EncodeHintType, Object> hint = new HashMap<>(8);
            hint.put(EncodeHintType.ERROR_CORRECTION, this.getErrorCorrectionLevel());
            hint.put(EncodeHintType.CHARACTER_SET, charset);
            hint.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, format, size, size, hint);
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            return image;
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    /**
     * 插入logo
     *
     * @param source
     */
    private void insertLogo(BufferedImage source) {
        int width = logo.getWidth(null);
        int height = logo.getHeight(null);
        // 插入logo
        Graphics2D graph = source.createGraphics();
        int x = (size - width) / 2;
        int y = (size - height) / 2;
        graph.drawImage(logo, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
        source.flush();
    }

    /**
     * 插入文字
     *
     * @param image image
     * @param words 文字
     * @return BufferedImage
     */
    private BufferedImage insertWords(BufferedImage image, String words) {
        int newHeight = size + wordsHeight + wordsTopMargin + wordsButtonMargin;
        BufferedImage outImage = new BufferedImage(size, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outImage.createGraphics();
        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        g2d.setStroke(s);
        // 设置背景颜色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, size, newHeight);
        // 画二维码
        g2d.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font(wordsFontName, Font.PLAIN, wordsFontSize));
        int strWidth = g2d.getFontMetrics().stringWidth(words);
        int wordStartX = (wordsWidth - strWidth) / 2;
        int wordStartY = size + wordsHeight + wordsTopMargin;
        // 画文字
        g2d.drawString(words, wordStartX, wordStartY);
        g2d.dispose();
        outImage.flush();
        return outImage;
    }

    /**
     * 获取纠错等级
     *
     * @return ErrorCorrectionLevel
     */
    private ErrorCorrectionLevel getErrorCorrectionLevel() {
        if (this.errorCorrectionLevel <= 1) {
            return ErrorCorrectionLevel.L;
        } else if (this.errorCorrectionLevel == 2) {
            return ErrorCorrectionLevel.M;
        } else if (this.errorCorrectionLevel == 3) {
            return ErrorCorrectionLevel.Q;
        }
        return ErrorCorrectionLevel.H;
    }

    // -------------------- encord --------------------

    public void encode(String content, OutputStream out) {
        this.encode(content, null, out);
    }

    /**
     * 生成二维码到流
     *
     * @param content 内容
     * @param words   文字
     * @param out     outputStream
     */
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

    public byte[] encode(String content) {
        return this.encode(content, (String) null);
    }

    /**
     * 获取二维码byte[]
     *
     * @param content 内容
     * @param words   文字
     * @return byte[]
     */
    public byte[] encode(String content, String words) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.encode(content, words, out);
        return out.toByteArray();
    }

    public String encodeBase64(String content) {
        return Base64s.img64Encode(this.encode(content, (String) null), suffix);
    }

    /**
     * 获取二维码base64
     *
     * @param content 内容
     * @param words   文字
     * @return base64
     */
    public String encodeBase64(String content, String words) {
        return Base64s.img64Encode(this.encode(content, words), suffix);
    }

    // -------------------- decord --------------------

    public String decode(String file) {
        try {
            return decode(ImageIO.read(new File(file)));
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    public String decode(File file) {
        try {
            return decode(ImageIO.read(file));
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    public String decode(byte[] bs) {
        try {
            return decode(ImageIO.read(Streams.toInputStream(bs)));
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    public String decode(InputStream in) {
        try {
            return decode(ImageIO.read(in));
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    public String decodeBase64(String base64) {
        return decode(Images.base64Decode(base64));
    }

    /**
     * 解析二维码数据
     *
     * @param image 二维码
     */
    public String decode(BufferedImage image) {
        try {
            // 设置二维码的大小 如果不设置可能会导致解析失败
            image = image.getSubimage(parserX, parserY, size, size);
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Map<DecodeHintType, Object> hint = new HashMap<>(4);
            hint.put(DecodeHintType.CHARACTER_SET, charset);
            hint.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            hint.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
            return new MultiFormatReader().decode(bitmap, hint).getText();
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    // -------------------- setter --------------------

    public QRCodes format(BarcodeFormat format) {
        this.format = format;
        return this;
    }

    public QRCodes suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public QRCodes charset(String charset) {
        this.charset = charset;
        return this;
    }

    public QRCodes parserX(int x) {
        this.parserX = x;
        return this;
    }

    public QRCodes parserY(int y) {
        this.parserY = y;
        return this;
    }

    public QRCodes parser(int x, int y) {
        this.parserX = x;
        this.parserY = y;
        return this;
    }

    public QRCodes size(int size) {
        this.size = size;
        return this;
    }

    public QRCodes errorCorrectionLevel(int errorCorrectionLevel) {
        this.errorCorrectionLevel = errorCorrectionLevel;
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
        return this;
    }

    public QRCodes wordsWidth(int wordsWidth) {
        this.wordsWidth = wordsWidth;
        return this;
    }

    public QRCodes wordsHeight(int wordsHeight) {
        this.wordsHeight = wordsHeight;
        return this;
    }

    public QRCodes wordsFontName(String wordsFontName) {
        this.wordsFontName = wordsFontName;
        return this;
    }

    public QRCodes wordsFontSize(int wordsFontSize) {
        this.wordsFontSize = wordsFontSize;
        return this;
    }

    public QRCodes wordsTopMargin(int wordsTopMargin) {
        this.wordsTopMargin = wordsTopMargin;
        return this;
    }

    public QRCodes wordsButtonMargin(int wordsButtonMargin) {
        this.wordsButtonMargin = wordsButtonMargin;
        return this;
    }

    // -------------------- getter --------------------

    public BarcodeFormat getFormat() {
        return format;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getCharset() {
        return charset;
    }

    public int getSize() {
        return size;
    }

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

    public int getWordsWidth() {
        return wordsWidth;
    }

    public int getWordsHeight() {
        return wordsHeight;
    }

    public String getWordsFontName() {
        return wordsFontName;
    }

    public int getWordsFontSize() {
        return wordsFontSize;
    }

    public int getWordsTopMargin() {
        return wordsTopMargin;
    }

    public int getWordsButtonMargin() {
        return wordsButtonMargin;
    }

}
