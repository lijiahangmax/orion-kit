package com.orion.utils.code;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.orion.id.Sequences;
import com.orion.utils.Strings;
import com.orion.utils.Systems;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具类
 * 如果logo图大于二维码的大小那么logo会覆盖二维码, 这时必须压缩logo
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/7/8 21:19
 */
public class QRCodes {

    public QRCodes() {
    }

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
     * 编码
     */
    private String charset = "UTF-8";

    /**
     * 二维码尺寸
     */
    private int size = 300;

    /**
     * 纠错等级
     * 1 ~7%
     * 2 ~15%
     * 3 ~25%
     * 4 ~30%
     */
    private int errorCorrectionLevel = 1;

    /**
     * LOGO宽度
     */
    private int logoWidth = 60;

    /**
     * LOGO高度
     */
    private int logoHeight = 60;

    /**
     * logo
     */
    private Image logo;

    /**
     * 是否压缩LOGO
     */
    private boolean logoCompress;

    /**
     * 二维码文字宽
     */
    private int wordWidth = size;

    /**
     * 二维码文字高
     */
    private int wordHeight = 10;

    /**
     * 字体名称
     */
    private String wordsFontName = "微软雅黑";

    /**
     * 字体大小
     */
    private int wordsFontSize = 18;

    /**
     * 上边距
     */
    private int wordsTopMargin = 10;

    /**
     * 下边距
     */
    private int wordsButtonMargin = 20;

    /**
     * 创建 BufferedImage
     *
     * @param content 二维码内容
     * @return BufferedImage
     */
    public BufferedImage getBufferedImage(String content) {
        try {
            HashMap<EncodeHintType, Object> hint = new HashMap<>(8);
            hint.put(EncodeHintType.ERROR_CORRECTION, this.getErrorCorrectionLevel());
            hint.put(EncodeHintType.CHARACTER_SET, charset);
            hint.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hint);
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
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
     * @param source
     */
    private void insertLogo(BufferedImage source) {
        int width = logo.getWidth(null);
        int height = logo.getHeight(null);
        // 插入LOGO
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
        int newHeight = size + wordHeight + wordsTopMargin + wordsButtonMargin;
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
        int wordStartX = (wordWidth - strWidth) / 2;
        int wordStartY = size + wordHeight + wordsTopMargin;
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

    /**
     * 生成二维码
     *
     * @param content 内容
     * @return 二维码文件
     */
    public File encodeToFile(String content) {
        return encodeAndWordsToFile(content, null, Sequences.createId() + "");
    }

    /**
     * 生成二维码
     *
     * @param content  内容
     * @param fileName 文件名
     * @return 二维码文件
     */
    public File encodeToFile(String content, String fileName) {
        return encodeAndWordsToFile(content, null, fileName);
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     * @param words   文字
     * @return 二维码文件
     */
    public File encodeAndWordsToFile(String content, String words) {
        return encodeAndWordsToFile(content, words, Sequences.createId() + "");
    }

    /**
     * 生成二维码
     *
     * @param content  内容
     * @param words    文字
     * @param fileName 文件名
     * @return 二维码文件
     */
    public File encodeAndWordsToFile(String content, String words, String fileName) {
        OutputStream out = null;
        File file = new File(Files1.getPath(dir + "/" + fileName + "." + suffix));
        try {
            Files1.touch(file);
            out = Files1.openOutputStream(file);
            BufferedImage image = getBufferedImage(content);
            if (this.logo != null) {
                insertLogo(image);
            }
            if (!Strings.isBlank(words)) {
                image = insertWords(image, words);
            }
            ImageIO.write(image, suffix, out);
            return file;
        } catch (Exception e) {
            return null;
        } finally {
            Streams.close(out);
        }
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     * @param out     outputStream
     */
    public QRCodes encodeToStream(String content, OutputStream out) {
        return encodeAndWordsToStream(content, null, out);
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     * @param words   文字
     * @param out     outputStream
     */
    public QRCodes encodeAndWordsToStream(String content, String words, OutputStream out) {
        try {
            BufferedImage image = getBufferedImage(content);
            if (this.logo != null) {
                insertLogo(image);
            }
            if (!Strings.isBlank(words)) {
                image = insertWords(image, words);
            }
            ImageIO.write(image, suffix, out);
        } catch (Exception e) {
            // ignore
        }
        return this;
    }

    /**
     * 解析二维码数据
     *
     * @param file 二维码文件路径
     */
    public static String decode(String file) {
        try {
            return decode(ImageIO.read(new File(file)));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析二维码数据
     *
     * @param file 二维码文件路径
     */
    public static String decode(File file) {
        try {
            return decode(ImageIO.read(file));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析二维码数据
     *
     * @param in 二维码流
     */
    public static String decode(InputStream in) {
        try {
            return decode(ImageIO.read(in));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析二维码数据
     *
     * @param image 二维码
     */
    private static String decode(BufferedImage image) {
        try {
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Map<DecodeHintType, Object> hint = new HashMap<>(4);
            hint.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            return new MultiFormatReader().decode(bitmap, hint).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public QRCodes setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public QRCodes setDir(String dir) {
        this.dir = dir;
        return this;
    }

    public QRCodes setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public QRCodes setSize(int size) {
        this.size = size;
        return this;
    }

    public QRCodes setErrorCorrectionLevel(int errorCorrectionLevel) {
        this.errorCorrectionLevel = errorCorrectionLevel;
        return this;
    }

    public QRCodes setLogoWidth(int logoWidth) {
        this.logoWidth = logoWidth;
        return this;
    }

    public QRCodes setLogoHeight(int logoHeight) {
        this.logoHeight = logoHeight;
        return this;
    }

    public QRCodes setLogo(String file) {
        try {
            return this.setLogo(ImageIO.read(new File(file)));
        } catch (IOException e) {
            return this;
        }
    }

    public QRCodes setLogo(File file) {
        try {
            return this.setLogo(ImageIO.read(file));
        } catch (IOException e) {
            return this;
        }
    }

    public QRCodes setLogo(InputStream in) {
        try {
            return this.setLogo(ImageIO.read(in));
        } catch (IOException e) {
            return this;
        }
    }

    public QRCodes setLogo(byte[] bs) {
        try {
            return this.setLogo(ImageIO.read(Streams.toInputStream(bs)));
        } catch (IOException e) {
            return this;
        }
    }

    public QRCodes setLogo(Image logo) {
        this.logo = logo;
        if (logoCompress) {
            this.logoCompress();
        }
        return this;
    }

    public QRCodes setWordWidth(int wordWidth) {
        this.wordWidth = wordWidth;
        return this;
    }

    public QRCodes setWordHeight(int wordHeight) {
        this.wordHeight = wordHeight;
        return this;
    }

    public QRCodes setWordsFontName(String wordsFontName) {
        this.wordsFontName = wordsFontName;
        return this;
    }

    public QRCodes setWordsFontSize(int wordsFontSize) {
        this.wordsFontSize = wordsFontSize;
        return this;
    }

    public QRCodes setWordsTopMargin(int wordsTopMargin) {
        this.wordsTopMargin = wordsTopMargin;
        return this;
    }

    public QRCodes setWordsButtonMargin(int wordsButtonMargin) {
        this.wordsButtonMargin = wordsButtonMargin;
        return this;
    }

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

    public QRCodes logoSize(int size) {
        this.logoWidth = size;
        this.logoHeight = size;
        return this;
    }

}
