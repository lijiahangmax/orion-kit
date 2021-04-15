package com.orion.utils.code;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.orion.constant.Const;
import com.orion.utils.Colors;
import com.orion.utils.Exceptions;
import com.orion.utils.codec.Base64s;
import com.orion.utils.image.Images;
import com.orion.utils.io.Streams;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 条码生成器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/14 11:39
 */
public abstract class CodeGenerator {

    protected static final MultiFormatWriter WRITER = new MultiFormatWriter();

    /**
     * 格式
     */
    protected BarcodeFormat format;

    /**
     * 格式
     */
    protected String suffix;

    /**
     * 编码
     */
    protected String charset;

    /**
     * 条形码宽
     */
    protected int width;

    /**
     * 条形码高
     */
    protected int height;

    /**
     * 纠错等级
     * 1 ~7%
     * 2 ~15%
     * 3 ~25%
     * 4 ~30%
     */
    protected int errorCorrectionLevel;

    /**
     * 字体名称
     */
    protected Font font;

    /**
     * 字体颜色
     */
    protected Color fontColor;

    /**
     * 图片上边距
     */
    protected int imgTopMargin;

    /**
     * 图片下边距
     */
    protected int imgButtonMargin;

    /**
     * 文字上边距
     */
    protected int wordsTopMargin;

    /**
     * 文字下边距
     */
    protected int wordsButtonMargin;

    /**
     * 编码配置
     */
    protected Map<EncodeHintType, Object> encodeHint;

    /**
     * 解码配置
     */
    protected Map<DecodeHintType, Object> decodeHint;

    protected CodeGenerator() {
        this.suffix = Const.SUFFIX_PNG;
        this.charset = Const.UTF_8;
        this.errorCorrectionLevel = 1;
        this.font = new Font(Const.FONT_MICROSOFT_ELEGANT_BLACK, Font.PLAIN, 18);
        this.fontColor = Color.BLACK;
        this.wordsButtonMargin = 10;
        this.encodeHint = new HashMap<>(8);
        this.decodeHint = new HashMap<>(8);
        encodeHint.put(EncodeHintType.ERROR_CORRECTION, this.toErrorCorrectionLevel());
        encodeHint.put(EncodeHintType.CHARACTER_SET, charset);
        encodeHint.put(EncodeHintType.MARGIN, 1);
        decodeHint.put(DecodeHintType.CHARACTER_SET, charset);
    }

    /**
     * 获取纠错等级
     *
     * @return ErrorCorrectionLevel
     */
    protected ErrorCorrectionLevel toErrorCorrectionLevel() {
        if (this.errorCorrectionLevel <= 1) {
            return ErrorCorrectionLevel.L;
        } else if (this.errorCorrectionLevel == 2) {
            return ErrorCorrectionLevel.M;
        } else if (this.errorCorrectionLevel == 3) {
            return ErrorCorrectionLevel.Q;
        }
        return ErrorCorrectionLevel.H;
    }

    /**
     * 获取 BufferedImage
     *
     * @param content 条形码内容
     * @return BufferedImage
     */
    protected abstract BufferedImage getBufferedImage(String content);

    /**
     * 插入文字
     *
     * @param image image
     * @param words 文字
     * @return BufferedImage
     */
    protected BufferedImage insertWords(BufferedImage image, String words) {
        int fontSize = font.getSize();
        int newHeight = imgTopMargin + height + imgButtonMargin +
                wordsTopMargin + fontSize + wordsButtonMargin;
        BufferedImage outImage = new BufferedImage(width, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outImage.createGraphics();
        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        g2d.setStroke(s);
        // 设置背景颜色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, newHeight);
        // 画条形码
        g2d.drawImage(image, 0, imgTopMargin, image.getWidth(), image.getHeight(), null);
        g2d.setColor(fontColor);
        g2d.setFont(font);
        int strWidth = g2d.getFontMetrics().stringWidth(words);
        int wordStartX = (width - strWidth) / 2;
        int wordStartY = imgTopMargin + height + imgButtonMargin + wordsTopMargin + fontSize;
        // 画文字
        g2d.drawString(words, wordStartX, wordStartY);
        g2d.dispose();
        outImage.flush();
        return outImage;
    }

    // -------------------- hint --------------------

    public CodeGenerator encodeHint(Map<EncodeHintType, Object> encodeHint) {
        this.encodeHint = encodeHint;
        return this;
    }

    public CodeGenerator decodeHint(Map<DecodeHintType, Object> decodeHint) {
        this.decodeHint = decodeHint;
        return this;
    }

    public CodeGenerator encodeHint(EncodeHintType type, Object hint) {
        encodeHint.put(type, hint);
        return this;
    }

    public CodeGenerator decodeHint(DecodeHintType type, Object hint) {
        decodeHint.put(type, hint);
        return this;
    }

    // -------------------- encode --------------------

    public void encode(String content, OutputStream out) {
        this.encode(content, null, out);
    }

    public byte[] encode(String content) {
        return this.encode(content, (String) null);
    }

    public byte[] encode(String content, String words) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.encode(content, words, out);
        return out.toByteArray();
    }

    public String encodeBase64(String content) {
        return Base64s.img64Encode(this.encode(content, (String) null), suffix);
    }

    public String encodeBase64(String content, String words) {
        return Base64s.img64Encode(this.encode(content, words), suffix);
    }

    /**
     * 生成条码到流
     *
     * @param content 内容
     * @param words   文字
     * @param out     outputStream
     */
    public abstract void encode(String content, String words, OutputStream out);

    // -------------------- decode --------------------

    public String decode(String file) {
        try {
            return this.decode(ImageIO.read(new File(file)));
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    public String decode(File file) {
        try {
            return this.decode(ImageIO.read(file));
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    public String decode(byte[] bs) {
        try {
            return this.decode(ImageIO.read(Streams.toInputStream(bs)));
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    public String decode(InputStream in) {
        try {
            return this.decode(ImageIO.read(in));
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    public String decodeBase64(String base64) {
        return decode(Images.base64Decode(base64));
    }

    /**
     * 解析条码数据
     *
     * @param image img
     * @return data
     */
    public String decode(BufferedImage image) {
        try {
            // 设置实际大小 如果不设置可能会导致解析失败
            image = image.getSubimage(0, imgTopMargin, width, height);
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            return new MultiFormatReader().decode(bitmap, this.getDecodeHint()).getText();
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    // -------------------- setter --------------------

    public CodeGenerator format(BarcodeFormat format) {
        this.format = format;
        return this;
    }

    public CodeGenerator suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public CodeGenerator charset(String charset) {
        this.charset = charset;
        return this;
    }

    public CodeGenerator errorCorrectionLevel(int errorCorrectionLevel) {
        this.errorCorrectionLevel = errorCorrectionLevel;
        encodeHint.put(EncodeHintType.ERROR_CORRECTION, this.toErrorCorrectionLevel());
        return this;
    }

    public CodeGenerator width(int width) {
        this.width = width;
        return this;
    }

    public CodeGenerator height(int height) {
        this.height = height;
        return this;
    }

    public CodeGenerator size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public CodeGenerator wordsTopMargin(int wordsTopMargin) {
        this.wordsTopMargin = wordsTopMargin;
        return this;
    }

    public CodeGenerator wordsButtonMargin(int wordsButtonMargin) {
        this.wordsButtonMargin = wordsButtonMargin;
        return this;
    }

    public CodeGenerator wordsMargin(int wordsTopMargin, int wordsButtonMargin) {
        this.wordsTopMargin = wordsTopMargin;
        this.wordsButtonMargin = wordsButtonMargin;
        return this;
    }

    public CodeGenerator imgTopMargin(int imgTopMargin) {
        this.imgTopMargin = imgTopMargin;
        return this;
    }

    public CodeGenerator imgButtonMargin(int imgButtonMargin) {
        this.imgButtonMargin = imgButtonMargin;
        return this;
    }

    public CodeGenerator imgMargin(int imgTopMargin, int imgButtonMargin) {
        this.imgTopMargin = imgTopMargin;
        this.imgButtonMargin = imgButtonMargin;
        return this;
    }

    public CodeGenerator font(Font font) {
        this.font = font;
        return this;
    }

    public CodeGenerator font(String name) {
        this.font = new Font(name, Font.PLAIN, 18);
        return this;
    }

    public CodeGenerator font(int size) {
        this.font = new Font(Const.FONT_MICROSOFT_ELEGANT_BLACK, Font.PLAIN, size);
        return this;
    }

    public CodeGenerator font(String name, int size) {
        this.font = new Font(name, Font.PLAIN, size);
        return this;
    }

    public CodeGenerator fontColor(Color fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    public CodeGenerator fontColor(String hexColor) {
        this.fontColor = Colors.toColor(hexColor);
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getErrorCorrectionLevel() {
        return errorCorrectionLevel;
    }

    public Font getFont() {
        return font;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public int getImgTopMargin() {
        return imgTopMargin;
    }

    public int getImgButtonMargin() {
        return imgButtonMargin;
    }

    public int getWordsTopMargin() {
        return wordsTopMargin;
    }

    public int getWordsButtonMargin() {
        return wordsButtonMargin;
    }

    public Map<EncodeHintType, Object> getEncodeHint() {
        return encodeHint;
    }

    public Map<DecodeHintType, Object> getDecodeHint() {
        return decodeHint;
    }

}
