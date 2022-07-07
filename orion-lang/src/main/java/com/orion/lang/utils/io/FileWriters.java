package com.orion.lang.utils.io;

import com.orion.lang.constant.Const;
import com.orion.lang.constant.Letters;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Objects1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.orion.lang.utils.io.Files1.*;
import static com.orion.lang.utils.io.Streams.close;

/**
 * 文件写入工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/27 14:39
 */
public class FileWriters {

    private static final String UTF_8 = Const.UTF_8;

    private static final Charset C_UTF_8 = StandardCharsets.UTF_8;

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
        try (OutputStream out = openOutputStream(file, true)) {
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
                File endFile = touchTempFile(true);
                writeToFile(file, offset, endFile);
                r.seek(offset);
                r.write(bytes, off, len);
                in = openInputStream(endFile);
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

    public static void appendLine(File file, String line) {
        appendLines(file, Lists.singleton(line), UTF_8);
    }

    public static void appendLine(String file, String line) {
        appendLines(new File(file), Lists.singleton(line), UTF_8);
    }

    public static void appendLine(File file, String line, String charset) {
        appendLines(file, Lists.singleton(line), charset);
    }

    /**
     * 拼接一行 尾行拼接\n
     *
     * @param file    文件
     * @param line    string
     * @param charset 编码格式
     */
    public static void appendLine(String file, String line, String charset) {
        appendLines(new File(file), Lists.singleton(line), charset);
    }

    public static void appendLines(File file, List<String> list) {
        appendLines(file, list, UTF_8);
    }

    public static void appendLines(String file, List<String> list) {
        appendLines(new File(file), list, UTF_8);
    }

    public static void appendLines(String file, List<String> list, String charset) {
        writeLines(new File(file), list, charset, true);
    }

    /**
     * 拼接多行 可能在开头拼接\n 尾行拼接\n
     *
     * @param file    文件
     * @param lines   lines
     * @param charset 编码格式
     */
    public static void appendLines(File file, List<String> lines, String charset) {
        writeLines(file, lines, charset, true);
    }

    public static void appendLine(String file, long offset, String line) {
        appendLines(new File(file), offset, Lists.singleton(line), UTF_8);
    }

    public static void appendLine(File file, long offset, String line) {
        appendLines(file, offset, Lists.singleton(line), UTF_8);
    }

    public static void appendLine(String file, long offset, String line, String charset) {
        appendLines(new File(file), offset, Lists.singleton(line), charset);
    }

    /**
     * 拼接行到偏移处  尾行拼接\n
     *
     * @param file    文件
     * @param offset  拼接偏移量
     * @param line    行
     * @param charset 编码格式
     */
    public static void appendLine(File file, long offset, String line, String charset) {
        appendLines(file, offset, Lists.singleton(line), charset);
    }

    public static void appendLines(File file, long offset, List<String> lines) {
        appendLines(file, offset, lines, UTF_8);
    }

    public static void appendLines(String file, long offset, List<String> lines) {
        appendLines(new File(file), offset, lines, UTF_8);
    }

    public static void appendLines(String file, long offset, List<String> lines, String charset) {
        appendLines(new File(file), offset, lines, charset);
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
        boolean append = false;
        if (offset >= fileLen) {
            append = true;
        }
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
                    endFile = touchTempFile(true);
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
                    in = openInputStream(endFile);
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
        try (FileOutputStream out = openOutputStream(file)) {
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
            try (FileOutputStream out = openOutputStream(file)) {
                out.write(bs, off, len);
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            }
        }
    }

    public static void writeLine(File file, String line) {
        writeLines(file, Lists.singleton(line), UTF_8, false);
    }

    public static void writeLine(String file, String line) {
        writeLines(new File(file), Lists.singleton(line), UTF_8, false);
    }

    public static void writeLine(File file, String line, String charset) {
        writeLines(file, Lists.singleton(line), charset, false);
    }

    /**
     * 写入一行 尾行拼接\n
     *
     * @param file    文件
     * @param line    string
     * @param charset 编码格式
     */
    public static void writeLine(String file, String line, String charset) {
        writeLines(new File(file), Lists.singleton(line), charset, false);
    }

    public static void writeLines(String file, List<String> lines) {
        writeLines(new File(file), lines, UTF_8, false);
    }

    public static void writeLines(File file, List<String> lines) {
        writeLines(file, lines, UTF_8, false);
    }

    public static void writeLines(File file, List<String> lines, String charset) {
        writeLines(file, lines, charset, false);
    }

    /**
     * 写入多行 尾行拼接\n
     *
     * @param file    文件
     * @param lines   lines
     * @param charset 编码格式
     */
    public static void writeLines(String file, List<String> lines, String charset) {
        writeLines(new File(file), lines, charset, false);
    }

    /**
     * 写入多行 尾行拼接\n
     *
     * @param file    文件
     * @param lines   lines
     * @param charset 编码格式
     * @param append  true 拼接
     */
    private static void writeLines(File file, List<String> lines, String charset, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openOutputStream(file, append), Objects1.def(charset, UTF_8)), Const.BUFFER_KB_8)) {
            StreamWriters.writeLines(writer, lines);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
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
                    File endFile = touchTempFile(true);
                    writeToFile(file, Long.valueOf(rangeEnd), endFile);
                    System.arraycopy(s, 0, n, 0, i += s.length);
                    System.arraycopy(bytes, off, n, i, len);
                    write(file, n);
                    mergeFile(endFile, file);
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
                    File endFile = touchTempFile(true);
                    writeToFile(file, Long.valueOf(rangeEnd), endFile);
                    RandomAccessFile rw = new RandomAccessFile(file, Const.ACCESS_RW);
                    close.add(rw);
                    rw.seek(rangeStart);
                    rw.write(bytes, off, len);
                    int read;
                    byte[] bs = new byte[Const.BUFFER_KB_8];
                    FileInputStream in = openInputStream(endFile);
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
        try (OutputStream out = openOutputStreamFast(file, append)) {
            StreamWriters.write(out, bs, off, len);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static void writeLineFast(String file, String line) {
        writeLinesFast(Paths.get(file), Lists.singleton(line), C_UTF_8, false);
    }

    public static void writeLineFast(File file, String line) {
        writeLinesFast(Paths.get(file.getAbsolutePath()), Lists.singleton(line), C_UTF_8, false);
    }

    public static void writeLineFast(Path file, String line) {
        writeLinesFast(file, Lists.singleton(line), C_UTF_8, false);
    }

    public static void appendLineFast(String file, String line) {
        writeLinesFast(Paths.get(file), Lists.singleton(line), C_UTF_8, true);
    }

    public static void appendLineFast(File file, String line) {
        writeLinesFast(Paths.get(file.getAbsolutePath()), Lists.singleton(line), C_UTF_8, true);
    }

    public static void appendLineFast(Path file, String line) {
        writeLinesFast(file, Lists.singleton(line), C_UTF_8, true);
    }

    public static void writeLineFast(String file, String line, String charset) {
        writeLinesFast(Paths.get(file), Lists.singleton(line), Charset.forName(charset), false);
    }

    public static void writeLineFast(File file, String line, String charset) {
        writeLinesFast(Paths.get(file.getAbsolutePath()), Lists.singleton(line), Charset.forName(charset), false);
    }

    public static void writeLineFast(Path file, String line, Charset charset) {
        writeLinesFast(file, Lists.singleton(line), charset, false);
    }

    public static void appendLineFast(String file, String line, String charset) {
        writeLinesFast(Paths.get(file), Lists.singleton(line), Charset.forName(charset), true);
    }

    public static void appendLineFast(File file, String line, String charset) {
        writeLinesFast(Paths.get(file.getAbsolutePath()), Lists.singleton(line), Charset.forName(charset), true);
    }

    /**
     * 拼接 尾拼接\n
     *
     * @param file    file
     * @param line    line
     * @param charset charset
     */
    public static void appendLineFast(Path file, String line, Charset charset) {
        writeLinesFast(file, Lists.singleton(line), charset, true);
    }

    public static void writeLinesFast(String file, List<String> lines) {
        writeLinesFast(Paths.get(file), lines, C_UTF_8, false);
    }

    public static void writeLinesFast(File file, List<String> lines) {
        writeLinesFast(Paths.get(file.getAbsolutePath()), lines, C_UTF_8, false);
    }

    public static void writeLinesFast(Path file, List<String> lines) {
        writeLinesFast(file, lines, C_UTF_8, false);
    }

    public static void appendLinesFast(String file, List<String> lines) {
        writeLinesFast(Paths.get(file), lines, C_UTF_8, true);
    }

    public static void appendLinesFast(File file, List<String> lines) {
        writeLinesFast(Paths.get(file.getAbsolutePath()), lines, C_UTF_8, true);
    }

    public static void appendLinesFast(Path file, List<String> lines) {
        writeLinesFast(file, lines, C_UTF_8, true);
    }

    public static void writeLinesFast(String file, List<String> lines, String charset) {
        writeLinesFast(Paths.get(file), lines, Charset.forName(charset), false);
    }

    public static void writeLinesFast(File file, List<String> lines, String charset) {
        writeLinesFast(Paths.get(file.getAbsolutePath()), lines, Charset.forName(charset), false);
    }

    public static void writeLinesFast(Path file, List<String> lines, Charset charset) {
        writeLinesFast(file, lines, charset, false);
    }

    public static void appendLinesFast(String file, List<String> lines, String charset) {
        writeLinesFast(Paths.get(file), lines, Charset.forName(charset), true);
    }

    public static void appendLinesFast(File file, List<String> lines, String charset) {
        writeLinesFast(Paths.get(file.getAbsolutePath()), lines, Charset.forName(charset), true);
    }

    /**
     * 拼接 尾行拼接\n
     *
     * @param file    file
     * @param lines   lines
     * @param charset charset
     */
    public static void appendLinesFast(Path file, List<String> lines, Charset charset) {
        writeLinesFast(file, lines, charset, true);
    }

    /**
     * 写入/拼接 尾行拼接\n
     *
     * @param file    file
     * @param lines   lines
     * @param charset charset
     * @param append  append
     */
    private static void writeLinesFast(Path file, List<String> lines, Charset charset, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openOutputStreamFast(file, append), Objects1.def(charset, C_UTF_8)), Const.BUFFER_KB_8)) {
            StreamWriters.writeLines(writer, lines);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

}
