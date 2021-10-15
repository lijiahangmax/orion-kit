package com.orion.utils.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 输入流工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 9:56
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class StreamReaders {

    private StreamReaders() {
    }

    public static int read(InputStream in, byte[] bytes) throws IOException {
        return read(in, bytes, 0);
    }

    /**
     * 读取流
     *
     * @param in    in
     * @param bytes 读取的数组
     * @param skip  跳过的长度
     * @return 读取的长度
     */
    public static int read(InputStream in, byte[] bytes, long skip) throws IOException {
        if (skip > 0) {
            in.skip(skip);
        }
        return in.read(bytes);
    }

    /**
     * 读取所有字节
     *
     * @param in in
     * @return byte[]
     */
    public static byte[] readAllBytes(InputStream in) throws IOException {
        return Streams.toByteArray(in);
    }

    public static String readLine(Reader reader) throws IOException {
        return readLine(reader, 0);
    }

    /**
     * 读取一行
     *
     * @param reader reader
     * @param skip   偏移量
     * @return 行
     */
    public static String readLine(Reader reader, long skip) throws IOException {
        BufferedReader bufferedReader = Streams.toBufferedReader(reader);
        if (skip > 0) {
            reader.skip(skip);
        }
        return bufferedReader.readLine();
    }

    /**
     * 读取一行
     *
     * @param reader   reader
     * @param skipLine 偏移行
     * @return 行
     */
    public static String readLine(Reader reader, int skipLine) throws IOException {
        BufferedReader bufferedReader = Streams.toBufferedReader(reader);
        if (skipLine > 0) {
            for (int i = 0; i < skipLine; i++) {
                if (bufferedReader.readLine() == null) {
                    return null;
                }
            }
        }
        return bufferedReader.readLine();
    }

    public static List<String> readLines(InputStream input) throws IOException {
        return readLines(new InputStreamReader(input), 0L, 0);
    }

    public static List<String> readLines(InputStream input, String charset) throws IOException {
        if (charset == null) {
            return readLines(new InputStreamReader(input), 0L, 0);
        } else {
            return readLines(new InputStreamReader(input, charset), 0L, 0);
        }
    }

    public static List<String> readLines(InputStream input, int lines) throws IOException {
        return readLines(new InputStreamReader(input), 0L, lines);
    }

    public static List<String> readLines(InputStream input, String charset, int lines) throws IOException {
        if (charset == null) {
            return readLines(new InputStreamReader(input), 0L, lines);
        } else {
            return readLines(new InputStreamReader(input, charset), 0L, lines);
        }
    }

    public static List<String> readLines(InputStream input, long skip, int lines) throws IOException {
        return readLines(new InputStreamReader(input), skip, lines);
    }

    public static List<String> readLines(InputStream input, int skipLine, int lines) throws IOException {
        return readLines(new InputStreamReader(input), skipLine, lines);
    }

    /**
     * 读取多行
     *
     * @param input   input
     * @param charset charset
     * @param skip    偏移量
     * @param lines   读取多少行  <= 0 所有行
     * @return 行
     */
    public static List<String> readLines(InputStream input, String charset, long skip, int lines) throws IOException {
        if (charset == null) {
            return readLines(new InputStreamReader(input), skip, lines);
        } else {
            return readLines(new InputStreamReader(input, charset), skip, lines);
        }
    }

    /**
     * 读取多行
     *
     * @param input    input
     * @param charset  charset
     * @param skipLine 偏移行
     * @param lines    读取多少行  <=0 所有行
     * @return 行
     */
    public static List<String> readLines(InputStream input, String charset, int skipLine, int lines) throws IOException {
        if (charset == null) {
            return readLines(new InputStreamReader(input), skipLine, lines);
        } else {
            return readLines(new InputStreamReader(input, charset), skipLine, lines);
        }
    }

    public static List<String> readLines(Reader reader) throws IOException {
        return readLines(reader, 0L, 0);
    }

    public static List<String> readLines(Reader reader, int lines) throws IOException {
        return readLines(reader, 0L, lines);
    }

    /**
     * 读取多行
     *
     * @param reader reader
     * @param skip   偏移量
     * @param lines  读取多少行  <= 0 所有行
     * @return 行
     */
    public static List<String> readLines(Reader reader, long skip, int lines) throws IOException {
        return readLines(reader, skip, 0, lines);
    }

    /**
     * 读取多行
     *
     * @param reader   reader
     * @param skipLine 偏移行
     * @param lines    读取多少行  <=0 所有行
     * @return 行
     */
    public static List<String> readLines(Reader reader, int skipLine, int lines) throws IOException {
        return readLines(reader, 0L, skipLine, lines);
    }

    private static List<String> readLines(Reader reader, long skip, int skipLine, int lines) throws IOException {
        BufferedReader bufferedReader = Streams.toBufferedReader(reader);
        List<String> list = new ArrayList<>();
        if (skip > 0) {
            reader.skip(skip);
        }
        if (skipLine > 0) {
            for (int i = 0; i < skipLine; i++) {
                if (bufferedReader.readLine() == null) {
                    return list;
                }
            }
        }
        if (lines <= 0) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
        } else {
            String line;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null && ++i <= lines) {
                list.add(line);
            }
        }
        return list;
    }

}
