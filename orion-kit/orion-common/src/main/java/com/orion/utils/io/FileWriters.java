package com.orion.utils.io;

import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.collect.Lists;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.orion.utils.io.Files1.*;
import static com.orion.utils.io.Streams.close;

/**
 * 文件写入工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/27 14:39
 */
public class FileWriters {

    private FileWriters() {
    }

    /**
     * 缓冲区默认大小
     */
    private static final int BUFFER_SIZE = 1024 * 8;

    /**
     * 拼接到文件最后一行
     *
     * @param file 文件
     * @param bs   bytes
     */
    public static void append(File file, byte[] bs) {
        append(file, bs, 0, bs.length);
    }

    /**
     * 拼接到文件最后一行
     *
     * @param file 文件
     * @param bs   bytes
     */
    public static void append(String file, byte[] bs) {
        append(new File(file), bs, 0, bs.length);
    }

    /**
     * 拼接到文件最后一行
     *
     * @param file 文件
     * @param bs   bytes
     * @param off  offset
     * @param len  length
     */
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
        try (OutputStream out = openOutputStream(file, true)) {
            out.write(bs, off, len);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 拼接到文件最后一行
     *
     * @param file 文件
     * @param s    string
     */
    public static void append(File file, String s) {
        append(file, s.getBytes());
    }

    /**
     * 拼接到文件最后一行
     *
     * @param file 文件
     * @param s    string
     */
    public static void append(String file, String s) {
        append(new File(file), s.getBytes());
    }

    /**
     * 拼接到文件最后一行
     *
     * @param file    文件
     * @param s       string
     * @param charset 编码格式
     */
    public static void append(File file, String s, String charset) {
        try {
            append(file, s.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unCoding(e);
        }
    }

    /**
     * 拼接到文件最后一行
     *
     * @param file    文件
     * @param s       string
     * @param charset 编码格式
     */
    public static void append(String file, String s, String charset) {
        try {
            append(new File(file), s.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unCoding(e);
        }
    }

    /**
     * 拼接到偏移处
     *
     * @param file   文件
     * @param offset 拼接偏移量
     * @param bytes  bytes
     */
    public static void append(String file, long offset, byte[] bytes) {
        append(new File(file), offset, bytes, 0, bytes.length);
    }

    /**
     * 拼接到偏移处
     *
     * @param file   文件
     * @param offset 拼接偏移量
     * @param bytes  bytes
     */
    public static void append(File file, long offset, byte[] bytes) {
        append(file, offset, bytes, 0, bytes.length);
    }

    /**
     * 拼接到偏移处
     *
     * @param file   文件
     * @param offset 拼接偏移量
     * @param bytes  bytes
     * @param off    偏移量
     * @param len    长度
     */
    public static void append(String file, long offset, byte[] bytes, int off, int len) {
        append(new File(file), offset, bytes, off, len);
    }

    /**
     * 拼接到偏移处
     *
     * @param file   文件
     * @param offset 拼接偏移量
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
            if (fileLen - offset <= BUFFER_SIZE) {
                byte[] bs = new byte[((int) (fileLen - offset + len))];
                System.arraycopy(bytes, 0, bs, 0, len);
                r = new RandomAccessFile(file, "rw");
                r.seek(offset);
                r.read(bs, len, bs.length - len);
                r.seek(offset);
                r.write(bs);
            } else {
                r = new RandomAccessFile(file, "rw");
                File endFile = touchTempFile(true);
                writeToFile(file, offset, endFile);
                r.seek(offset);
                r.write(bytes, off, len);
                in = openInputStream(endFile);
                byte[] buf = new byte[BUFFER_SIZE];
                int read;
                while (-1 != (read = in.read(buf))) {
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
     * 拼接一行 可能在开头拼接 \n
     *
     * @param file 文件
     * @param line string
     */
    public static void appendLine(File file, String line) {
        appendLines(file, Lists.singleton(line), null);
    }

    /**
     * 拼接一行 可能在开头拼接 \n
     *
     * @param file 文件
     * @param line string
     */
    public static void appendLine(String file, String line) {
        appendLines(new File(file), Lists.singleton(line), null);
    }

    /**
     * 拼接一行 可能在开头拼接 \n
     *
     * @param file    文件
     * @param line    string
     * @param charset 编码格式
     */
    public static void appendLine(File file, String line, String charset) {
        appendLines(file, Lists.singleton(line), charset);
    }

    /**
     * 拼接一行 可能在开头拼接 \n
     *
     * @param file    文件
     * @param line    string
     * @param charset 编码格式
     */
    public static void appendLine(String file, String line, String charset) {
        appendLines(new File(file), Lists.singleton(line), charset);
    }

    /**
     * 拼接多行 可能在首行开头拼接 \n
     *
     * @param file 文件
     * @param list strings
     */
    public static void appendLines(File file, List<String> list) {
        appendLines(file, list, null);
    }

    /**
     * 拼接多行 可能在首行开头拼接 \n
     *
     * @param file 文件
     * @param list strings
     */
    public static void appendLines(String file, List<String> list) {
        appendLines(new File(file), list, null);
    }

    /**
     * 拼接多行 可能在首行开头拼接 \n
     *
     * @param file    文件
     * @param list    strings
     * @param charset 编码格式
     */
    public static void appendLines(String file, List<String> list, String charset) {
        writeLines(new File(file), list, charset, true);
    }

    /**
     * 拼接多行 可能在首行开头拼接 \n
     *
     * @param file    文件
     * @param list    strings
     * @param charset 编码格式
     */
    public static void appendLines(File file, List<String> list, String charset) {
        writeLines(file, list, charset, true);
    }

    /**
     * 拼接行到偏移处 首尾不拼接 \n
     *
     * @param file   文件
     * @param offset 拼接偏移量
     * @param line   行
     */
    public static void appendLine(String file, long offset, String line) {
        appendLines(new File(file), offset, Lists.singleton(line), null);
    }

    /**
     * 拼接行到偏移处 首尾不拼接 \n
     *
     * @param file   文件
     * @param offset 拼接偏移量
     * @param line   行
     */
    public static void appendLine(File file, long offset, String line) {
        appendLines(file, offset, Lists.singleton(line), null);
    }

    /**
     * 拼接行到偏移处 首尾不拼接 \n
     *
     * @param file    文件
     * @param offset  拼接偏移量
     * @param line    行
     * @param charset 编码格式
     */
    public static void appendLine(String file, long offset, String line, String charset) {
        appendLines(new File(file), offset, Lists.singleton(line), charset);
    }

    /**
     * 拼接行到偏移处 首尾不拼接 \n
     *
     * @param file    文件
     * @param offset  拼接偏移量
     * @param line    行
     * @param charset 编码格式
     */
    public static void appendLine(File file, long offset, String line, String charset) {
        appendLines(file, offset, Lists.singleton(line), charset);
    }

    /**
     * 拼接行到偏移处 首尾不拼接 \n
     *
     * @param file   文件
     * @param offset 拼接偏移量
     * @param lines  行
     */
    public static void appendLines(File file, long offset, List<String> lines) {
        appendLines(file, offset, lines, null);
    }

    /**
     * 拼接行到偏移处 首尾不拼接 \n
     *
     * @param file   文件
     * @param offset 拼接偏移量
     * @param lines  行
     */
    public static void appendLines(String file, long offset, List<String> lines) {
        appendLines(new File(file), offset, lines, null);
    }

    /**
     * 拼接行到偏移处 首尾不拼接 \n
     *
     * @param file    文件
     * @param offset  拼接偏移量
     * @param lines   行
     * @param charset 编码格式
     */
    public static void appendLines(String file, long offset, List<String> lines, String charset) {
        appendLines(new File(file), offset, lines, charset);
    }

    /**
     * 拼接行到偏移处 首尾不拼接 \n
     *
     * @param file    文件
     * @param offset  拼接偏移量
     * @param lines   行
     * @param charset 编码格式
     */
    public static void appendLines(File file, long offset, List<String> lines, String charset) {
        long fileLen = file.length();
        boolean append = false;
        if (offset >= fileLen) {
            append = true;
        }
        FileInputStream in = null;
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(file, "rw");
            boolean useBuffer = true;
            byte[] bs = null;
            int read = 0;
            File endFile = null;
            if (!append) {
                if (fileLen - offset <= BUFFER_SIZE) {
                    bs = new byte[((int) (fileLen - offset))];
                    r.seek(offset);
                    read = r.read(bs);
                    r.seek(offset);
                } else {
                    endFile = touchTempFile(true);
                    writeToFile(file, offset, endFile);
                    r.seek(offset);
                    useBuffer = false;
                }
            }
            for (int i = 0, size = lines.size(); i < size; i++) {
                String line = lines.get(i);
                if (charset == null) {
                    r.write(line.getBytes());
                } else {
                    r.write(line.getBytes(charset));
                }
                if (i != size - 1) {
                    r.write('\n');
                }
            }
            if (!append) {
                if (useBuffer) {
                    r.write(bs, 0, read);
                } else {
                    in = openInputStream(endFile);
                    bs = new byte[BUFFER_SIZE];
                    while (-1 != (read = in.read(bs))) {
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

    /**
     * 写入到文件
     *
     * @param file 文件
     * @param bs   bytes
     */
    public static void write(File file, byte[] bs) {
        write(file, bs, 0, bs.length);
    }

    /**
     * 写入到文件
     *
     * @param file 文件
     * @param bs   bytes
     */
    public static void write(String file, byte[] bs) {
        write(new File(file), bs, 0, bs.length);
    }

    /**
     * 写入到文件
     *
     * @param file 文件
     * @param bs   bytes
     * @param off  offset
     * @param len  length
     */
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
        try (FileOutputStream out = openOutputStream(file)) {
            out.write(bs, off, len);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 写入到文件
     *
     * @param file 文件
     * @param s    string
     */
    public static void write(File file, String s) {
        write(file, s.getBytes());
    }

    /**
     * 写入到文件
     *
     * @param file 文件
     * @param s    string
     */
    public static void write(String file, String s) {
        write(new File(file), s.getBytes());
    }

    /**
     * 写入到文件
     *
     * @param file    文件
     * @param s       string
     * @param charset 编码格式
     */
    public static void write(File file, String s, String charset) {
        try {
            write(file, s.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unCoding(e);
        }
    }

    /**
     * 写入到文件
     *
     * @param file    文件
     * @param s       string
     * @param charset 编码格式
     */
    public static void write(String file, String s, String charset) {
        try {
            write(new File(file), s.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unCoding(e);
        }
    }

    /**
     * 写入一行 尾行拼接\n
     *
     * @param file 文件
     * @param str  string
     */
    public static void writeLine(File file, String str) {
        writeLines(file, Lists.singleton(str), null, false);
    }

    /**
     * 写入一行 尾行拼接\n
     *
     * @param file 文件
     * @param str  string
     */
    public static void writeLine(String file, String str) {
        writeLines(new File(file), Lists.singleton(str), null, false);
    }

    /**
     * 写入一行 尾行拼接\n
     *
     * @param file    文件
     * @param str     string
     * @param charset 编码格式
     */
    public static void writeLine(File file, String str, String charset) {
        writeLines(file, Lists.singleton(str), charset, false);
    }

    /**
     * 写入一行 尾行拼接\n
     *
     * @param file    文件
     * @param str     string
     * @param charset 编码格式
     */
    public static void writeLine(String file, String str, String charset) {
        writeLines(new File(file), Lists.singleton(str), charset, false);
    }

    /**
     * 写入多行 尾行拼接\n
     *
     * @param file 文件
     * @param list strings
     */
    public static void writeLines(String file, List<String> list) {
        writeLines(new File(file), list, null, false);
    }

    /**
     * 写入多行 尾行拼接\n
     *
     * @param file 文件
     * @param list strings
     */
    public static void writeLines(File file, List<String> list) {
        writeLines(file, list, null, false);
    }

    /**
     * 写入多行 尾行拼接\n
     *
     * @param file    文件
     * @param list    strings
     * @param charset 编码格式
     */
    public static void writeLines(File file, List<String> list, String charset) {
        writeLines(file, list, charset, false);
    }

    /**
     * 写入多行 尾行拼接\n
     *
     * @param file    文件
     * @param list    strings
     * @param charset 编码格式
     */
    public static void writeLines(String file, List<String> list, String charset) {
        writeLines(new File(file), list, charset, false);
    }

    /**
     * 写入多行 尾行拼接\n
     *
     * @param file    文件
     * @param list    strings
     * @param charset 编码格式
     * @param append  true 拼接
     */
    private static void writeLines(File file, List<String> list, String charset, boolean append) {
        String lineSeparator = "\n";
        boolean beforeAppend = false;
        if (append) {
            lineSeparator = getFileEndLineSeparator(file);
            if (lineSeparator == null) {
                lineSeparator = "\n";
                beforeAppend = true;
            }
        }
        try (OutputStream out = openOutputStream(file, append)) {
            if (beforeAppend) {
                out.write(lineSeparator.getBytes());
            }
            if (charset == null) {
                for (String str : list) {
                    str += lineSeparator;
                    out.write(str.getBytes());
                }
            } else {
                for (String str : list) {
                    str += lineSeparator;
                    out.write(str.getBytes(charset));
                }
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 写入到文件
     *
     * @param file      文件
     * @param skipBytes 跳过前几位
     * @param bs        写入数组
     */
    public static void write(String file, int skipBytes, byte[] bs) {
        write(new File(file), skipBytes, bs, 0, bs.length);
    }

    /**
     * 写入到文件
     *
     * @param file      文件
     * @param skipBytes 跳过前几位
     * @param bs        写入数组
     */
    public static void write(File file, int skipBytes, byte[] bs) {
        write(file, skipBytes, bs, 0, bs.length);
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
            try (FileInputStream in = openInputStream(file)) {
                byte[] skips = new byte[skipBytes];
                int read = in.read(skips);
                try (FileOutputStream out = openOutputStream(file)) {
                    out.write(skips, 0, read);
                    out.write(bs, off, len);
                } catch (IOException e) {
                    throw Exceptions.ioRuntime(e);
                }
            } catch (Exception e) {
                throw Exceptions.ioRuntime(e);
            }
        } else {
            write(file, bs, off, len);
        }
    }

    /**
     * 写入到文件
     *
     * @param file       文件
     * @param rangeStart 写入指定区域开始
     * @param rangeEnd   写入指定区域结束
     * @param bytes      数据
     */
    public static void replace(String file, int rangeStart, int rangeEnd, byte[] bytes) {
        replace(new File(file), rangeStart, rangeEnd, bytes, 0, bytes.length);
    }

    /**
     * 写入到文件
     *
     * @param file       文件
     * @param rangeStart 写入指定区域开始
     * @param rangeEnd   写入指定区域结束
     * @param bytes      数据
     */
    public static void replace(File file, int rangeStart, int rangeEnd, byte[] bytes) {
        replace(file, rangeStart, rangeEnd, bytes, 0, bytes.length);
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
            throw Exceptions.argument("rangeStart and rangeEnd Not less than 0");
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
        if (rangeStart <= BUFFER_SIZE) {
            startBuffer = true;
        }
        if (fileLen - rangeEnd <= BUFFER_SIZE) {
            endBuffer = true;
        }
        List<Closeable> close = new ArrayList<>();
        try {
            if (startBuffer && endBuffer) {
                RandomAccessFile r = new RandomAccessFile(file, "r");
                close.add(r);
                byte[] s = Streams.read(r, 0, rangeStart);
                byte[] e = Streams.read(r, rangeEnd, fileLen);
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
                    RandomAccessFile r = new RandomAccessFile(file, "r");
                    close.add(r);
                    byte[] s = Streams.read(r, 0, rangeStart);
                    byte[] n = new byte[s.length + len];
                    int i = 0;
                    File endFile = touchTempFile(true);
                    writeToFile(file, Long.valueOf(rangeEnd), endFile);
                    System.arraycopy(s, 0, n, 0, i += s.length);
                    System.arraycopy(bytes, off, n, i, len);
                    write(file, n);
                    mergeFile(endFile, file);
                } else if (endBuffer) {
                    RandomAccessFile r = new RandomAccessFile(file, "r");
                    close.add(r);
                    byte[] e = Streams.read(r, rangeEnd, fileLen);
                    byte[] n = new byte[e.length + len];
                    System.arraycopy(bytes, off, n, 0, len);
                    System.arraycopy(e, 0, n, len, e.length);
                    RandomAccessFile rw = new RandomAccessFile(file, "rw");
                    close.add(rw);
                    rw.seek(rangeStart);
                    rw.write(n);
                } else {
                    File endFile = touchTempFile(true);
                    writeToFile(file, Long.valueOf(rangeEnd), endFile);
                    RandomAccessFile rw = new RandomAccessFile(file, "rw");
                    close.add(rw);
                    rw.seek(rangeStart);
                    rw.write(bytes, off, len);
                    int read;
                    byte[] bs = new byte[BUFFER_SIZE];
                    FileInputStream in = openInputStream(endFile);
                    close.add(in);
                    while (-1 != (read = in.read(bs))) {
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

    /**
     * 写入文件到文件
     *
     * @param file       文件
     * @param off        文件偏移量
     * @param targetFile 目标文件
     */
    public static void writeToFile(File file, long off, File targetFile) {
        writeToFile(file, off, ((int) (file.length() - off)), targetFile);
    }

    /**
     * 写入文件到文件
     *
     * @param file       文件
     * @param len        写入长度
     * @param targetFile 目标文件
     */
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
                copy(file, targetFile);
                return;
            }
            in = openInputStream(file);
            if (in.skip(off) < off) {
                // all skip
                return;
            }
            out = openOutputStream(targetFile);
            int read;
            byte[] buf = new byte[BUFFER_SIZE];
            while (-1 != (read = in.read(buf))) {
                out.write(buf, 0, read);
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            Streams.close(in);
            Streams.close(out);
        }
    }

}
