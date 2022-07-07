package com.orion.lang.utils.io;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Strings;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;

/**
 * 输出流工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 14:03
 */
public class StreamWriters {

    private StreamWriters() {
    }

    /**
     * 写入数组
     *
     * @param out out
     * @param bs  bytes
     * @param off offset
     * @param len length
     */
    public static void write(OutputStream out, byte[] bs, int off, int len) throws IOException {
        out.write(bs, off, len);
    }

    /**
     * 写入字符
     *
     * @param out out
     * @param str str
     */
    public static void write(OutputStream out, String str) throws IOException {
        out.write(Strings.bytes(str));
    }

    /**
     * 写入字符
     *
     * @param out out
     * @param str str
     */
    public static void write(OutputStream out, String str, String charset) throws IOException {
        if (charset == null) {
            out.write(Strings.bytes(str));
        } else {
            out.write(Strings.bytes(str, charset));
        }
    }

    public static void write(Writer writer, byte[] bs) throws IOException {
        writer.write(new String(bs));
    }

    public static void write(Writer writer, byte[] bs, int off, int len) throws IOException {
        writer.write(new String(bs, off, len));
    }

    public static void write(Writer writer, char[] cs) throws IOException {
        writer.write(cs);
    }

    /**
     * 写入字符
     *
     * @param writer writer
     * @param str    str
     */
    public static void write(Writer writer, String str) throws IOException {
        writer.write(str);
    }

    public static void writeLine(Writer writer, String line) throws IOException {
        writeLine(writer, line, Const.LF);
    }

    /**
     * 写入一行
     *
     * @param writer        writer
     * @param line          line
     * @param lineSeparator 行分隔符
     */
    public static void writeLine(Writer writer, String line, String lineSeparator) throws IOException {
        writer.write(line + lineSeparator);
    }

    public static void writeLines(Writer writer, List<String> lines) throws IOException {
        writeLines(writer, lines, Const.LF);
    }

    /**
     * 写入多行
     *
     * @param writer        writer
     * @param lines         lines
     * @param lineSeparator 行分隔符
     */
    public static void writeLines(Writer writer, List<String> lines, String lineSeparator) throws IOException {
        for (String line : lines) {
            writer.write(line + lineSeparator);
        }
    }

}
