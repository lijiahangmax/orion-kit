package com.orion.utils.io;

import com.orion.utils.Exceptions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static com.orion.utils.io.Files1.openInputStream;
import static com.orion.utils.io.Files1.openOutputStream;

/**
 * 文件编码工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/27 15:06
 */
public class FileEncodes {

    private FileEncodes() {
    }

    /**
     * 通过文件头获取编码格式
     *
     * @param file 文件
     * @return GBK UTF-16LE UTF-16BE UTF-8 默认GBK
     */
    public static String getEncoding(String file) {
        return getEncoding(new File(file));
    }

    /**
     * 通过文件头获取编码格式
     *
     * @param file 文件
     * @return GBK UTF-16LE UTF-16BE UTF-8 默认GBK
     */
    public static String getEncoding(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try (BufferedInputStream bis = new BufferedInputStream(openInputStream(file))) {
            boolean checked = false;
            bis.mark(0);
            // bis.mark(100);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                bis.close();
                return charset;
            } else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0) {
                        break;
                    }
                    if (0x80 <= read && read <= 0xBF) {
                        break;
                    }
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (!(0x80 <= read && read <= 0xBF)) {
                            break;
                        }
                    } else if (0xE0 <= read) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
        return charset;
    }

    /**
     * 转换文件编码格式
     *
     * @param file        文件
     * @param fromCharset 原编码
     * @param toCharset   新编码
     */
    public static void convertEncoding(String file, String fromCharset, String toCharset) {
        convertEncoding(new File(file), fromCharset, toCharset);
    }

    /**
     * 转换文件编码格式
     *
     * @param file        文件
     * @param fromCharset 原编码
     * @param toCharset   新编码
     */
    public static void convertEncoding(File file, String fromCharset, String toCharset) {
        try (InputStreamReader reader = new InputStreamReader(openInputStream(file), fromCharset);
             OutputStreamWriter writer = new OutputStreamWriter(openOutputStream(file), toCharset)) {
            Streams.transfer(reader, writer);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

}
