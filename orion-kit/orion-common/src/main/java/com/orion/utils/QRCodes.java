package com.orion.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.orion.id.UUIds;
import com.orion.utils.io.Files1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 二维码生成
 * 如果logo图大于二维码的大小那么logo会覆盖二维码, 这时必须压缩logo(needCompress: true)
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/10/10 16:34
 */
public class QRCodes {

    private QRCodes() {
    }

    /**
     * 编码
     */
    private static final String CHARSET = "UTF-8";

    /**
     * 格式
     */
    private static final String FORMAT_NAME = "jpg";

    /**
     * 二维码尺寸
     */
    private static final int QR_CODE_SIZE = 300;

    /**
     * LOGO宽度
     */
    private static final int WIDTH = 60;

    /**
     * LOGO高度
     */
    private static final int HEIGHT = 60;

    /**
     * 生成二维码
     *
     * @param content      源内容
     * @param logoInput    二维码logo流
     * @param needCompress 是否要压缩logo
     * @return 返回二维码图片
     */
    private static BufferedImage createImage(String content, InputStream logoInput, boolean needCompress) throws Exception {
        ConcurrentHashMap<EncodeHintType, Object> map = new ConcurrentHashMap<>(3);
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        map.put(EncodeHintType.CHARACTER_SET, CHARSET);
        map.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, map);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (logoInput == null) {
            return image;
        }
        // 插入图片
        insertImage(image, logoInput, needCompress);
        return image;
    }

    /**
     * 在生成的二维码中插入图片
     *
     * @param source       二维码源
     * @param logoInput    二维码logo流
     * @param needCompress 是否压缩
     */
    private static void insertImage(BufferedImage source, InputStream logoInput, boolean needCompress) throws Exception {
        Image src = ImageIO.read(logoInput);
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        // 压缩LOGO
        if (needCompress) {
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            // 绘制缩小后的图
            g.drawImage(image, 0, 0, null);
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QR_CODE_SIZE - width) / 2;
        int y = (QR_CODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成二维码
     *
     * @param content      二维码内容
     * @param destPath     存储的地址
     * @param fileName     文件名称 不需要后缀
     * @param logoPath     logo图片 可以为null
     * @param needCompress 是否压缩
     */
    public static void encode(String content, String destPath, String fileName, String logoPath, boolean needCompress) throws Exception {
        BufferedImage image = createImage(content, logoPath == null ? null : Files1.openInputStream(logoPath), needCompress);
        Files1.mkdirs(destPath);
        fileName = fileName + "." + FORMAT_NAME;
        ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + fileName));
    }

    /**
     * 生成二维码 不压缩, 不带logo
     *
     * @param content  二维码内容
     * @param destPath 存储的地址
     * @param fileName 文件名称 不需要后缀
     * @param logoPath logo图片
     */
    public static void encode(String content, String destPath, String fileName, String logoPath) throws Exception {
        encode(content, destPath, fileName, logoPath, false);
    }

    /**
     * 生成二维码 没有logo, 不压缩
     *
     * @param content  二维码内容
     * @param destPath 存储的地址
     * @param fileName 文件名称 不需要后缀
     */
    public static void encode(String content, String destPath, String fileName) throws Exception {
        encode(content, destPath, fileName, null, false);
    }

    /**
     * 生成二维码 没有logo, 不压缩
     *
     * @param content  二维码内容
     * @param destPath 存储的地址
     * @return 二维码路径
     */
    public static String encode(String content, String destPath) throws Exception {
        String name = UUIds.random32();
        encode(content, destPath, name, null, false);
        return Files1.getPath(destPath + "/" + name);
    }

    /**
     * 图片流输出二维码 没logo, 不压缩
     *
     * @param content 存储的内容
     * @param output  图片输出流
     */
    public static void encode(String content, OutputStream output) throws Exception {
        encode(content, output, null, false);
    }

    /**
     * 图片流输出二维码
     *
     * @param content   存储的内容
     * @param output    图片输出流
     * @param logoInput logo流
     */
    public static void encode(String content, OutputStream output, InputStream logoInput) throws Exception {
        encode(content, output, logoInput, false);
    }

    /**
     * 图片流输出二维码
     *
     * @param content      存储的内容
     * @param output       图片输出流
     * @param logoInput    logo流
     * @param needCompress 是否压缩
     */
    public static void encode(String content, OutputStream output, InputStream logoInput, boolean needCompress) throws Exception {
        BufferedImage image = createImage(content, logoInput, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 解析二维码数据
     *
     * @param file 二维码文件路径
     */
    public static String decode(String file) throws Exception {
        return decode(ImageIO.read(QRCodes.class.getClassLoader().getResourceAsStream(file)));
    }

    /**
     * 解析二维码数据
     *
     * @param file 二维码文件路径
     */
    public static String decode(File file) throws Exception {
        return decode(ImageIO.read(file));
    }

    /**
     * 解析二维码数据
     *
     * @param in 二维码流
     */
    public static String decode(InputStream in) throws Exception {
        return decode(ImageIO.read(in));
    }

    /**
     * 解析二维码数据
     *
     * @param image 二维码
     */
    private static String decode(BufferedImage image) throws Exception {
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Map<DecodeHintType, Object> map = new HashMap<>(1);
        map.put(DecodeHintType.CHARACTER_SET, CHARSET);
        return new MultiFormatReader().decode(bitmap, map).getText();
    }

}
