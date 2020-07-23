package com.orion.utils.code;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.orion.id.Sequences;
import com.orion.utils.Strings;
import com.orion.utils.Systems;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 条形码工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/7/8 17:15
 */
@SuppressWarnings("ALL")
public class BarCodes {

    public BarCodes() {
    }

    private static final MultiFormatWriter WRITER = new MultiFormatWriter();

    private static final String DEFAULT_DIR = Systems.HOME_DIR;

    /**
     * 格式
     */
    private String suffix = "png";

    /**
     * 存放文件夹
     */
    private String dir = DEFAULT_DIR;

    /**
     * 条形码宽
     */
    private int width = 200;

    /**
     * 条形码高
     */
    private int height = 30;

    /**
     * 上边距
     */
    private int topMargin = 10;

    /**
     * 下边距
     */
    private int bottomMargin = 10;

    /**
     * 条形码文字宽
     */
    private int wordsWidth = width;

    /**
     * 条形码文字高
     */
    private int wordsHeight = 25;

    /**
     * 字体名称
     */
    private String wordsFontName = "微软雅黑";

    /**
     * 字体大小
     */
    private int wordsFontSize = 18;

    /**
     * 获取 BufferedImage
     *
     * @param content 条形码内容
     * @return BufferedImage
     */
    public BufferedImage getBufferedImage(String content) {
        try {
            BitMatrix bitMatrix = WRITER.encode(content, BarcodeFormat.CODE_128, width, height, null);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            return null;
        }
    }

    /**
     * 插入文字
     *
     * @param image image
     * @param words 文字
     * @return BufferedImage
     */
    private BufferedImage insertWords(BufferedImage image, String words) {
        int newHeight = height + wordsHeight + topMargin + bottomMargin;
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
        g2d.drawImage(image, 0, topMargin, image.getWidth(), image.getHeight(), null);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font(wordsFontName, Font.PLAIN, wordsFontSize));
        int strWidth = g2d.getFontMetrics().stringWidth(words);
        int wordStartX = (wordsWidth - strWidth) / 2;
        int wordStartY = height + topMargin + wordsHeight;
        // 画文字
        g2d.drawString(words, wordStartX, wordStartY);
        g2d.dispose();
        outImage.flush();
        return outImage;
    }

    /**
     * 设置边距
     *
     * @param image image
     * @return BufferedImage
     */
    private BufferedImage setMargin(BufferedImage image) {
        int newHeight = height + topMargin + bottomMargin;
        BufferedImage outImage = new BufferedImage(width, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outImage.createGraphics();
        // 设置颜色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, newHeight);
        g2d.setColor(Color.BLACK);
        // 画条形码
        g2d.drawImage(image, 0, topMargin, image.getWidth(), image.getHeight(), null);
        g2d.dispose();
        outImage.flush();
        return outImage;
    }

    /**
     * 生成条形码
     *
     * @param content 内容
     * @return 条形码文件
     */
    public File encodeToFile(String content) {
        return encodeAndWordsToFile(content, null, Sequences.createId() + "");
    }

    /**
     * 生成条形码
     *
     * @param content  内容
     * @param fileName 文件名
     * @return 条形码文件
     */
    public File encodeToFile(String content, String fileName) {
        return encodeAndWordsToFile(content, null, fileName);
    }

    /**
     * 生成条形码
     *
     * @param content 内容
     * @param words   文字
     * @return 条形码文件
     */
    public File encodeAndWordsToFile(String content, String words) {
        return encodeAndWordsToFile(content, words, Sequences.createId() + "");
    }

    /**
     * 生成条形码
     *
     * @param content  内容
     * @param words    文字
     * @param fileName 文件名
     * @return 条形码文件
     */
    public File encodeAndWordsToFile(String content, String words, String fileName) {
        OutputStream out = null;
        File file = new File(Files1.getPath(dir + "/" + fileName + "." + suffix));
        try {
            Files1.touch(file);
            out = Files1.openOutputStream(file);
            BufferedImage image = getBufferedImage(content);
            if (!Strings.isBlank(words)) {
                image = insertWords(image, words);
            } else if (topMargin != 0 || bottomMargin != 0) {
                image = setMargin(image);
            }
            ImageIO.write(image, suffix, out);
            return file;
        } catch (Exception e) {
            return null;
        } finally {
            Streams.closeQuietly(out);
        }
    }

    /**
     * 生成条形码
     *
     * @param content 内容
     * @param out     outputStream
     */
    public void encodeToStream(String content, OutputStream out) {
        encodeAndWordsToStream(content, null, out);
    }

    /**
     * 生成条形码
     *
     * @param content 内容
     * @param words   文字
     * @param out     outputStream
     */
    public void encodeAndWordsToStream(String content, String words, OutputStream out) {
        try {
            BufferedImage image = getBufferedImage(content);
            if (!Strings.isBlank(words)) {
                image = insertWords(image, words);
            } else if (topMargin != 0 || bottomMargin != 0) {
                image = setMargin(image);
            }
            ImageIO.write(image, suffix, out);
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * 条形码解析
     *
     * @param imagePath image
     * @return 条形码内容
     */
    public static String decode(String imagePath) {
        try {
            return decode(ImageIO.read(new File(imagePath)));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 条形码解析
     *
     * @param imageFile image
     * @return 条形码内容
     */
    public static String decode(File imageFile) {
        try {
            return decode(ImageIO.read(imageFile));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 条形码解析
     *
     * @param in image
     * @return 条形码内容
     */
    public static String decode(InputStream in) {
        try {
            return decode(ImageIO.read(in));
        } catch (Exception e) {
            return null;
        } finally {
            Streams.closeQuietly(in);
        }
    }

    /**
     * 条形码解析
     *
     * @param image image
     * @return 条形码内容
     */
    public static String decode(BufferedImage image) {
        Result result;
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            result = new MultiFormatReader().decode(bitmap, null);
            return result.getText();
        } catch (Exception e) {
            return null;
        }
    }

    public BarCodes setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public BarCodes setDir(String dir) {
        this.dir = dir;
        return this;
    }

    public BarCodes setWidth(int width) {
        this.width = width;
        return this;
    }

    public BarCodes setHeight(int height) {
        this.height = height;
        return this;
    }

    public BarCodes setWordsWidth(int wordsWidth) {
        this.wordsWidth = wordsWidth;
        return this;
    }

    public BarCodes setWordsHeight(int wordsHeight) {
        this.wordsHeight = wordsHeight;
        return this;
    }

    public BarCodes setWordsFontName(String wordsFontName) {
        this.wordsFontName = wordsFontName;
        return this;
    }

    public BarCodes setWordsFontSize(int wordsFontSize) {
        this.wordsFontSize = wordsFontSize;
        return this;
    }

    public BarCodes setTopMargin(int topMargin) {
        this.topMargin = topMargin;
        return this;
    }

    public BarCodes setBottomMargin(int bottomMargin) {
        this.bottomMargin = bottomMargin;
        return this;
    }

    public BarCodes setMargin(int margin) {
        this.topMargin = margin;
        this.bottomMargin = margin;
        return this;
    }

}
