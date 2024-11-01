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
package cn.orionsec.kit.lang.utils.io;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Exceptions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 文件编码工具类
 *
 * @author Jiahang Li
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
        String charset = Const.GBK;
        byte[] first3Bytes = new byte[3];
        try (BufferedInputStream bis = new BufferedInputStream(Files1.openInputStream(file))) {
            boolean checked = false;
            bis.mark(0);
            // bis.mark(100);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                bis.close();
                return charset;
            } else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = Const.UTF_16LE;
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = Const.UTF_16BE;
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
                charset = Const.UTF_8;
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
                                charset = Const.UTF_8;
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
        try (InputStreamReader reader = new InputStreamReader(Files1.openInputStream(file), fromCharset);
             OutputStreamWriter writer = new OutputStreamWriter(Files1.openOutputStream(file), toCharset)) {
            Streams.transfer(reader, writer);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

}
