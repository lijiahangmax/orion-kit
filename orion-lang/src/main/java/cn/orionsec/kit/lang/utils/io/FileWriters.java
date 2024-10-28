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
import cn.orionsec.kit.lang.constant.Letters;
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static cn.orionsec.kit.lang.utils.io.Streams.close;

/**
 * 文件写入工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/27 14:39
 */
public class FileWriters {

    private FileWriters() {
    }

    public static void append(File file, byte[] bs) {
        append(file, bs, 0, bs.length);
    }

    public static void append(String file, byte[] bs) {
        append(new File(file), bs, 0, bs.length);
    }

    public static void append(String file, byte[] bs, int off, int len) {
        append(new File(file), bs, off, len);
    }

    /**
     * 拼接到文件最后一行
     *
     * @param file 文件
     * @param bs   bytes
     * @param off  offset
     * @param len  length
     */
    public static void append(File file, byte[] bs, int off, int len) {
        try (OutputStream out = Files1.openOutputStream(file, true)) {
            StreamWriters.write(out, bs, off, len);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static void append(File file, String s) {
        append(file, Strings.bytes(s));
    }

    public static void append(String file, String s) {
        append(new File(file), Strings.bytes(s));
    }

    public static void append(File file, String s, String charset) {
        append(file, Strings.bytes(s, charset));
    }

    /**
     * 拼接到文件最后一行
     *
     * @param file    文件
     * @param s       string
     * @param charset 编码格式
     */
    public static void append(String file, String s, String charset) {
        append(new File(file), Strings.bytes(s, charset));
    }

    public static void append(String file, long offset, byte[] bytes) {
        append(new File(file), offset, bytes, 0, bytes.length);
    }

    public static void append(File file, long offset, byte[] bytes) {
        append(file, offset, bytes, 0, bytes.length);
    }

    public static void append(String file, long offset, byte[] bytes, int off, int len) {
        append(new File(file), offset, bytes, off, len);
    }

    /**
     * 拼接到偏移处
     *
     * @param file   文件
     * @param offset 起始偏移量
     * @param bytes  bytes
     * @param off    偏移量
     * @param len    长度
     */
    public static void append(File file, long offset, byte[] bytes, int off, int len) {
        long fileLen = file.length();
        if (offset >= fileLen) {
            append(file, bytes, off, len);
            return;
        }
        FileInputStream in = null;
        RandomAccessFile r = null;
        try {
            if (fileLen - offset <= Const.BUFFER_KB_8) {
                byte[] bs = new byte[((int) (fileLen - offset + len))];
                System.arraycopy(bytes, 0, bs, 0, len);
                r = new RandomAccessFile(file, Const.ACCESS_RW);
                r.seek(offset);
                r.read(bs, len, bs.length - len);
                r.seek(offset);
                r.write(bs);
            } else {
                r = new RandomAccessFile(file, Const.ACCESS_RW);
                File endFile = Files1.touchTempFile(true);
                writeToFile(file, offset, endFile);
                r.seek(offset);
                r.write(bytes, off, len);
                in = Files1.openInputStream(endFile);
                byte[] buf = new byte[Const.BUFFER_KB_8];
                int read;
                while ((read = in.read(buf)) != -1) {
                    r.write(buf, 0, read);
                }
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            close(in);
            close(r);
        }
    }


    /**
     * 拼接行到偏移处 尾行拼接\n
     *
     * @param file    文件
     * @param offset  拼接偏移量
     * @param lines   行
     * @param charset 编码格式
     */
    public static void appendLines(File file, long offset, List<String> lines, String charset) {
        long fileLen = file.length();
        boolean append = offset >= fileLen;
        FileInputStream in = null;
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(file, Const.ACCESS_RW);
            boolean useBuffer = true;
            byte[] bs = null;
            int read = 0;
            File endFile = null;
            if (!append) {
                if (fileLen - offset <= Const.BUFFER_KB_8) {
                    bs = new byte[((int) (fileLen - offset))];
                    r.seek(offset);
                    read = r.read(bs);
                    r.seek(offset);
                } else {
                    endFile = Files1.touchTempFile(true);
                    writeToFile(file, offset, endFile);
                    r.seek(offset);
                    useBuffer = false;
                }
            }
            for (String line : lines) {
                if (charset == null) {
                    r.write(Strings.bytes(line));
                } else {
                    r.write(Strings.bytes(line, charset));
                }
                r.write(Letters.LF);
            }
            if (!append) {
                if (useBuffer) {
                    r.write(bs, 0, read);
                } else {
                    in = Files1.openInputStream(endFile);
                    bs = new byte[Const.BUFFER_KB_8];
                    while ((read = in.read(bs)) != -1) {
                        r.write(bs, 0, read);
                    }
                }
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            close(in);
            close(r);
        }
    }

    public static void write(File file, byte[] bs) {
        write(file, bs, 0, bs.length);
    }

    public static void write(String file, byte[] bs) {
        write(new File(file), bs, 0, bs.length);
    }

    public static void write(String file, byte[] bs, int off, int len) {
        write(new File(file), bs, off, len);
    }

    /**
     * 写入到文件
     *
     * @param file 文件
     * @param bs   bytes
     * @param off  offset
     * @param len  length
     */
    public static void write(File file, byte[] bs, int off, int len) {
        try (FileOutputStream out = Files1.openOutputStream(file)) {
            StreamWriters.write(out, bs, off, len);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static void write(File file, String s) {
        write(file, Strings.bytes(s));
    }

    public static void write(String file, String s) {
        write(new File(file), Strings.bytes(s));
    }

    public static void write(File file, String s, String charset) {
        write(file, Strings.bytes(s, charset));
    }

    /**
     * 写入到文件
     *
     * @param file    文件
     * @param s       string
     * @param charset 编码格式
     */
    public static void write(String file, String s, String charset) {
        write(new File(file), Strings.bytes(s, charset));
    }

    public static void write(String file, int skipBytes, byte[] bs) {
        write(new File(file), skipBytes, bs, 0, bs.length);
    }

    public static void write(File file, int skipBytes, byte[] bs) {
        write(file, skipBytes, bs, 0, bs.length);
    }

    public static void write(String file, int skipBytes, byte[] bs, int off, int len) {
        write(new File(file), skipBytes, bs, off, len);
    }

    /**
     * 写入到文件
     *
     * @param file      文件
     * @param skipBytes 跳过前几位
     * @param bs        写入数组
     * @param off       偏移量
     * @param len       写入长度
     */
    public static void write(File file, int skipBytes, byte[] bs, int off, int len) {
        if (skipBytes > 0) {
            try (FileInputStream in = Files1.openInputStream(file)) {
                byte[] skips = new byte[skipBytes];
                int read = in.read(skips);
                try (FileOutputStream out = Files1.openOutputStream(file)) {
                    out.write(skips, 0, read);
                    out.write(bs, off, len);
                } catch (IOException e) {
                    throw Exceptions.ioRuntime(e);
                }
            } catch (Exception e) {
                throw Exceptions.ioRuntime(e);
            }
        } else {
            try (FileOutputStream out = Files1.openOutputStream(file)) {
                out.write(bs, off, len);
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            }
        }
    }

    // -------------------- replace --------------------

    public static void replace(String file, int rangeStart, int rangeEnd, byte[] bytes) {
        replace(new File(file), rangeStart, rangeEnd, bytes, 0, bytes.length);
    }

    public static void replace(File file, int rangeStart, int rangeEnd, byte[] bytes) {
        replace(file, rangeStart, rangeEnd, bytes, 0, bytes.length);
    }

    public static void replace(String file, int rangeStart, int rangeEnd, byte[] bytes, int off, int len) {
        replace(new File(file), rangeStart, rangeEnd, bytes, off, len);
    }

    /**
     * 写入到文件
     *
     * @param file       文件
     * @param rangeStart 写入指定区域开始
     * @param rangeEnd   写入指定区域结束
     * @param bytes      数据
     * @param off        偏移量
     * @param len        长度
     */
    public static void replace(File file, int rangeStart, int rangeEnd, byte[] bytes, int off, int len) {
        long fileLen = file.length();
        boolean append = false;
        if (rangeStart < 0 || rangeEnd < 0) {
            throw Exceptions.argument("range start and range end not less than 0");
        }
        if (rangeStart > rangeEnd) {
            int tmpStart = rangeStart;
            rangeStart = rangeEnd;
            rangeEnd = tmpStart;
        }
        if (rangeStart >= fileLen) {
            append = true;
        } else if (rangeEnd > fileLen) {
            rangeEnd = (int) fileLen;
        }
        if (append) {
            append(file, bytes, off, len);
            return;
        }
        boolean startBuffer = false;
        boolean endBuffer = false;
        if (rangeStart <= Const.BUFFER_KB_8) {
            startBuffer = true;
        }
        if (fileLen - rangeEnd <= Const.BUFFER_KB_8) {
            endBuffer = true;
        }
        List<Closeable> close = new ArrayList<>();
        try {
            if (startBuffer && endBuffer) {
                RandomAccessFile r = new RandomAccessFile(file, Const.ACCESS_R);
                close.add(r);
                byte[] s = FileReaders.read(r, 0, rangeStart);
                byte[] e = FileReaders.read(r, rangeEnd, fileLen);
                byte[] re = Arrays1.newBytes(s.length + e.length + len);
                int i = 0;
                System.arraycopy(s, 0, re, 0, s.length);
                i += s.length;
                System.arraycopy(bytes, off, re, i, len);
                i += len;
                System.arraycopy(e, 0, re, i, e.length);
                write(file, re);
            } else {
                if (startBuffer) {
                    RandomAccessFile r = new RandomAccessFile(file, Const.ACCESS_R);
                    close.add(r);
                    byte[] s = FileReaders.read(r, 0, rangeStart);
                    byte[] n = new byte[s.length + len];
                    int i = 0;
                    File endFile = Files1.touchTempFile(true);
                    writeToFile(file, Long.valueOf(rangeEnd), endFile);
                    System.arraycopy(s, 0, n, 0, i += s.length);
                    System.arraycopy(bytes, off, n, i, len);
                    write(file, n);
                    Files1.mergeFile(endFile, file);
                } else if (endBuffer) {
                    RandomAccessFile r = new RandomAccessFile(file, Const.ACCESS_R);
                    close.add(r);
                    byte[] e = FileReaders.read(r, rangeEnd, fileLen);
                    byte[] n = new byte[e.length + len];
                    System.arraycopy(bytes, off, n, 0, len);
                    System.arraycopy(e, 0, n, len, e.length);
                    RandomAccessFile rw = new RandomAccessFile(file, Const.ACCESS_RW);
                    close.add(rw);
                    rw.seek(rangeStart);
                    rw.write(n);
                } else {
                    File endFile = Files1.touchTempFile(true);
                    writeToFile(file, Long.valueOf(rangeEnd), endFile);
                    RandomAccessFile rw = new RandomAccessFile(file, Const.ACCESS_RW);
                    close.add(rw);
                    rw.seek(rangeStart);
                    rw.write(bytes, off, len);
                    int read;
                    byte[] bs = new byte[Const.BUFFER_KB_8];
                    FileInputStream in = Files1.openInputStream(endFile);
                    close.add(in);
                    while ((read = in.read(bs)) != -1) {
                        rw.write(bs, 0, read);
                    }
                }
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            close.forEach(Streams::close);
        }
    }

    public static void writeToFile(File file, long off, File targetFile) {
        writeToFile(file, off, ((int) (file.length() - off)), targetFile);
    }

    public static void writeToFile(File file, int len, File targetFile) {
        writeToFile(file, 0, len, targetFile);
    }

    /**
     * 写入文件到文件
     *
     * @param file       文件
     * @param off        文件偏移量
     * @param len        写入长度
     * @param targetFile 目标文件
     */
    public static void writeToFile(File file, long off, int len, File targetFile) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            if (off <= 0 && len >= file.length()) {
                Files1.copy(file, targetFile);
                return;
            }
            in = Files1.openInputStream(file);
            if (in.skip(off) < off) {
                // all skip
                return;
            }
            out = Files1.openOutputStream(targetFile);
            int read;
            byte[] buf = new byte[Const.BUFFER_KB_8];
            while ((read = in.read(buf)) != -1) {
                out.write(buf, 0, read);
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            Streams.close(in);
            Streams.close(out);
        }
    }

    // -------------------- fast --------------------

    public static void writeFast(String file, byte[] bs) {
        writeFast(Paths.get(file), bs, 0, bs.length, false);
    }

    public static void writeFast(File file, byte[] bs) {
        writeFast(Paths.get(file.getAbsolutePath()), bs, 0, bs.length, false);
    }

    public static void writeFast(Path file, byte[] bs) {
        writeFast(file, bs, 0, bs.length, false);
    }

    public static void writeFast(String file, byte[] bs, int off, int len) {
        writeFast(Paths.get(file), bs, off, len, false);
    }

    public static void writeFast(File file, byte[] bs, int off, int len) {
        writeFast(Paths.get(file.getAbsolutePath()), bs, off, len, false);
    }

    /**
     * 写入
     *
     * @param file file
     * @param off  offset
     * @param len  len
     * @param bs   bs
     */
    public static void writeFast(Path file, byte[] bs, int off, int len) {
        writeFast(file, bs, off, len, false);
    }

    public static void writeFast(String file, InputStream in) {
        writeFast(Paths.get(file), in, false, false);
    }

    public static void writeFast(File file, InputStream in) {
        writeFast(Paths.get(file.getAbsolutePath()), in, false, false);
    }

    public static void writeFast(Path file, InputStream in) {
        writeFast(file, in, false, false);
    }

    public static void writeFast(String file, InputStream in, boolean autoClose) {
        writeFast(Paths.get(file), in, autoClose, false);
    }

    public static void writeFast(File file, InputStream in, boolean autoClose) {
        writeFast(Paths.get(file.getAbsolutePath()), in, autoClose, false);
    }

    public static void writeFast(Path file, InputStream in, boolean autoClose) {
        writeFast(file, in, autoClose, false);
    }

    public static void appendFast(String file, byte[] bs) {
        writeFast(Paths.get(file), bs, 0, bs.length, true);
    }

    public static void appendFast(File file, byte[] bs) {
        writeFast(Paths.get(file.getAbsolutePath()), bs, 0, bs.length, true);
    }

    public static void appendFast(Path file, byte[] bs) {
        writeFast(file, bs, 0, bs.length, true);
    }

    public static void appendFast(String file, byte[] bs, int off, int len) {
        writeFast(Paths.get(file), bs, off, len, true);
    }

    public static void appendFast(File file, byte[] bs, int off, int len) {
        writeFast(Paths.get(file.getAbsolutePath()), bs, off, len, true);
    }

    /**
     * 拼接
     *
     * @param file file
     * @param off  offset
     * @param len  len
     * @param bs   bs
     */
    public static void appendFast(Path file, byte[] bs, int off, int len) {
        writeFast(file, bs, off, len, true);
    }

    public static void appendFast(String file, InputStream in) {
        writeFast(Paths.get(file), in, false, true);
    }

    public static void appendFast(File file, InputStream in) {
        writeFast(Paths.get(file.getAbsolutePath()), in, false, true);
    }

    public static void appendFast(Path file, InputStream in) {
        writeFast(file, in, false, true);
    }

    public static void appendFast(String file, InputStream in, boolean autoClose) {
        writeFast(Paths.get(file), in, autoClose, true);
    }

    public static void appendFast(File file, InputStream in, boolean autoClose) {
        writeFast(Paths.get(file.getAbsolutePath()), in, autoClose, true);
    }

    public static void appendFast(Path file, InputStream in, boolean autoClose) {
        writeFast(file, in, autoClose, true);
    }

    /**
     * 写入/拼接
     *
     * @param file   file
     * @param bs     bs
     * @param off    offset
     * @param len    len
     * @param append append
     */
    private static void writeFast(Path file, byte[] bs, int off, int len, boolean append) {
        try (OutputStream out = Files1.openOutputStreamFast(file, append)) {
            StreamWriters.write(out, bs, off, len);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 写入/拼接
     *
     * @param file      file
     * @param in        in
     * @param autoClose autoClose input
     * @param append    append
     */
    private static void writeFast(Path file, InputStream in, boolean autoClose, boolean append) {
        try (OutputStream out = Files1.openOutputStreamFast(file, append)) {
            Streams.transfer(in, out);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            close(in, autoClose);
        }
    }

}
