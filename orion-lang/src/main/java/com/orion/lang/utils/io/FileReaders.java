package com.orion.lang.utils.io;

import com.orion.lang.constant.Const;
import com.orion.lang.constant.Letters;
import com.orion.lang.define.iterator.ByteArrayIterator;
import com.orion.lang.define.iterator.LineIterator;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Objects1;
import com.orion.lang.utils.Strings;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.Stream;

import static com.orion.lang.utils.io.Files1.openInputStream;
import static com.orion.lang.utils.io.Files1.openInputStreamFast;

/**
 * 文件读取工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/27 14:39
 */
public class FileReaders {

    private static final String UTF_8 = Const.UTF_8;

    private static final Charset C_UTF_8 = StandardCharsets.UTF_8;

    private FileReaders() {
    }

    // -------------------- random read --------------------

    public static byte[] read(RandomAccessFile reader, long offset) throws IOException {
        return read(reader, offset, reader.length());
    }

    /**
     * 从偏移量开始读取,读取到指定位置
     *
     * @param reader 流
     * @param offset 偏移量
     * @param end    结束位置
     * @return bytes
     * @throws IOException IO
     */
    public static byte[] read(RandomAccessFile reader, long offset, long end) throws IOException {
        reader.seek(offset);
        long e = end, len = reader.length();
        if (end > len) {
            e = len;
        }
        byte[] bs = new byte[((int) (end - offset))];
        reader.read(bs, 0, ((int) (e - offset)));
        return bs;
    }

    public static String readLine(RandomAccessFile reader) throws IOException {
        return readLine(reader, UTF_8);
    }

    /**
     * 从当前偏移量读取一行
     *
     * @param reader  reader
     * @param charset 编码
     * @return 行
     * @throws IOException IO
     */
    public static String readLine(RandomAccessFile reader, String charset) throws IOException {
        long pos = reader.getFilePointer();
        byte[] bytes = new byte[Const.BUFFER_KB_8];
        byte[] line = new byte[Const.BUFFER_KB_8];
        int linePos = 0;
        int seek = 0;
        int read;
        while ((read = reader.read(bytes)) != -1) {
            seek += read;
            int bi = -1;
            for (int i = 0; i < read; i++) {
                byte b = bytes[i];
                if (b == Letters.CR) {
                    // \r
                    if (i + 1 < read) {
                        if (bytes[i + 1] == Letters.LF) {
                            seek++;
                        }
                        bi = i;
                        break;
                    } else {
                        byte[] bs1 = new byte[1];
                        int tmpRead = reader.read(bs1);
                        if (tmpRead != -1 && bs1[0] == Letters.LF) {
                            seek++;
                        }
                        bi = i;
                        break;
                    }
                } else if (b == Letters.LF) {
                    // \n
                    bi = i;
                    break;
                }
            }
            if (bi != -1) {
                line = Arrays1.arraycopy(bytes, 0, line, linePos, bi);
                linePos += bi;
                seek -= read - bi - 1;
                break;
            } else {
                line = Arrays1.arraycopy(bytes, 0, line, linePos, read);
                linePos += read;
            }
        }
        reader.seek(pos + seek);
        if (seek == 0 || linePos == 0) {
            return Strings.EMPTY;
        }
        return new String(line, 0, linePos, charset);
    }

    public static String readAllLines(RandomAccessFile reader) throws IOException {
        return readAllLines(reader, UTF_8);
    }

    /**
     * 从当前偏移量读取到最后一行
     *
     * @param reader  输入流
     * @param charset charset
     * @return lines
     * @throws IOException I/O异常
     */
    public static String readAllLines(RandomAccessFile reader, String charset) throws IOException {
        long pos = reader.getFilePointer();
        long length = reader.length();
        int more = (int) (length - pos);
        if (more <= 0) {
            return Strings.EMPTY;
        }
        byte[] buffer = new byte[more];
        int read = reader.read(buffer);
        return new String(buffer, 0, read, charset);
    }

    /**
     * 读取文件尾部行的seek
     *
     * @param reader reader
     * @param line   最后几行
     * @return seek
     */
    public static long readTailLinesSeek(RandomAccessFile reader, int line) throws IOException {
        long beforePos = reader.getFilePointer();
        long len = reader.length();
        if (len == 0L || line == 0) {
            return 0L;
        }
        boolean lastLf = false;
        long pos = len;
        while (pos > 0) {
            pos--;
            reader.seek(pos);
            int read = reader.read();
            boolean isLf = read == Letters.LF;
            if (isLf || (!lastLf && read == Letters.CR)) {
                if (isLf) {
                    lastLf = true;
                }
                if (pos != len - 1) {
                    line--;
                }
                if (line <= 0) {
                    pos++;
                    break;
                }
            }
        }
        reader.seek(beforePos);
        return pos;
    }

    public static String readTailLines(RandomAccessFile reader, int line) throws IOException {
        return readTailLines(reader, UTF_8, line);
    }

    /**
     * 读取文件最后几行
     *
     * @param reader  输入流
     * @param charset charset
     * @param line    line
     * @return lines
     * @throws IOException I/O异常
     */
    public static String readTailLines(RandomAccessFile reader, String charset, int line) throws IOException {
        long seek = readTailLinesSeek(reader, line);
        byte[] read = read(reader, seek);
        return new String(read, charset);
    }

    // -------------------- read --------------------

    public static int read(String file, byte[] bytes) {
        return read(new File(file), bytes, 0);
    }

    public static int read(File file, byte[] bytes) {
        return read(file, bytes, 0);
    }

    public static int read(String file, byte[] bytes, long skip) {
        return read(new File(file), bytes, skip);
    }

    /**
     * 读取文件
     *
     * @param file  文件
     * @param bytes 读取的数组
     * @param skip  跳过的长度
     * @return 读取的长度
     */
    public static int read(File file, byte[] bytes, long skip) {
        try (FileInputStream in = openInputStream(file)) {
            return StreamReaders.read(in, bytes, skip);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- read all --------------------

    public static byte[] readAllBytes(String file) {
        return readAllBytes(new File(file));
    }

    public static byte[] readAllBytes(File file) {
        try (FileInputStream in = openInputStream(file)) {
            return StreamReaders.readAllBytes(in);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- read line --------------------

    public static String readLine(String file) {
        return readLine(new File(file), 0L, UTF_8);
    }

    public static String readLine(File file) {
        return readLine(file, 0L, UTF_8);
    }

    public static String readLine(String file, String charset) {
        return readLine(new File(file), 0L, charset);
    }

    public static String readLine(File file, String charset) {
        return readLine(file, 0L, charset);
    }

    public static String readLine(String file, long skip) {
        return readLine(new File(file), skip, UTF_8);
    }

    public static String readLine(File file, long skip) {
        return readLine(file, skip, UTF_8);
    }

    public static String readLine(String file, long skip, String charset) {
        return readLine(new File(file), skip, charset);
    }

    /**
     * 读取一行
     *
     * @param file    文件
     * @param skip    偏移量
     * @param charset 编码格式
     * @return 行
     */
    public static String readLine(File file, long skip, String charset) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(openInputStream(file), Strings.def(charset, UTF_8)))) {
            return StreamReaders.readLine(reader, skip);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static String readLine(String file, int skipLine) {
        return readLine(new File(file), skipLine, UTF_8);
    }

    public static String readLine(File file, int skipLine) {
        return readLine(file, skipLine, UTF_8);
    }

    /**
     * 读取一行
     *
     * @param file     文件
     * @param skipLine 偏移行
     * @param charset  编码格式
     * @return 行
     */
    public static String readLine(String file, int skipLine, String charset) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(openInputStream(file), Strings.def(charset, UTF_8)))) {
            return StreamReaders.readLine(reader, skipLine);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- read lines --------------------

    public static List<String> readLines(File file) {
        return readLines(file, 0L, -1, UTF_8);
    }

    public static List<String> readLines(String file) {
        return readLines(new File(file), 0L, -1, UTF_8);
    }

    public static List<String> readLines(File file, int lines) {
        return readLines(file, 0L, lines, UTF_8);
    }

    public static List<String> readLines(String file, int lines) {
        return readLines(new File(file), 0L, lines, UTF_8);
    }

    public static List<String> readLines(String file, int lines, String charset) {
        return readLines(new File(file), 0L, lines, charset);
    }

    public static List<String> readLines(File file, int lines, String charset) {
        return readLines(file, 0L, lines, charset);
    }

    public static List<String> readLines(File file, long skip) {
        return readLines(file, skip, -1, UTF_8);
    }

    public static List<String> readLines(String file, long skip) {
        return readLines(new File(file), skip, -1, UTF_8);
    }

    public static List<String> readLines(File file, long skip, int lines) {
        return readLines(file, skip, lines, UTF_8);
    }

    public static List<String> readLines(String file, long skip, int lines) {
        return readLines(new File(file), skip, lines, UTF_8);
    }

    public static List<String> readLines(String file, long skip, int lines, String charset) {
        return readLines(new File(file), skip, lines, charset);
    }

    /**
     * 读取多行
     *
     * @param file    文件
     * @param skip    文件偏移量
     * @param lines   读取多少行  <= 0 所有行
     * @param charset 编码格式
     * @return 行
     */
    public static List<String> readLines(File file, long skip, int lines, String charset) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(openInputStream(file), Strings.def(charset, UTF_8)))) {
            return StreamReaders.readLines(reader, skip, lines);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static List<String> readLines(File file, int skipLine, int lines) {
        return readLines(file, skipLine, lines, UTF_8);
    }

    public static List<String> readLines(String file, int skipLine, int lines) {
        return readLines(new File(file), skipLine, lines, UTF_8);
    }

    public static List<String> readLines(String file, int skipLine, int lines, String charset) {
        return readLines(new File(file), skipLine, lines, charset);
    }

    /**
     * 读取多行
     *
     * @param file     文件
     * @param skipLine 文件偏移行
     * @param lines    读取多少行  <=0 所有行
     * @param charset  编码格式
     * @return 行
     */
    public static List<String> readLines(File file, int skipLine, int lines, String charset) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(openInputStream(file), Strings.def(charset, UTF_8)))) {
            return StreamReaders.readLines(reader, skipLine, lines);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- line consumer --------------------

    public static void lineConsumer(String file, Consumer<String> c) {
        lineConsumer(new File(file), UTF_8, c);
    }

    public static void lineConsumer(File file, Consumer<String> c) {
        lineConsumer(file, UTF_8, c);
    }

    public static void lineConsumer(String file, String charset, Consumer<String> c) {
        lineConsumer(new File(file), charset, c);
    }

    /**
     * 行消费者
     *
     * @param file    file
     * @param charset charset
     * @param c       consumer
     */
    public static void lineConsumer(File file, String charset, Consumer<String> c) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(openInputStream(file), Strings.def(charset, UTF_8)))) {
            Streams.lineConsumer(reader, c);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- line iterator --------------------

    public static LineIterator lineIterator(String file) {
        return lineIterator(new File(file), UTF_8);
    }

    public static LineIterator lineIterator(File file) {
        return lineIterator(file, UTF_8);
    }

    public static LineIterator lineIterator(String file, String charset) {
        return lineIterator(new File(file), charset);
    }

    /**
     * 行迭代器
     *
     * @param file    file
     * @param charset charset
     * @return LineIterator
     */
    public static LineIterator lineIterator(File file, String charset) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(openInputStream(file), Strings.def(charset, UTF_8)))) {
            return Streams.lineIterator(reader).autoClose(true);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- byte array consumer --------------------

    public static void byteArrayConsumer(String file, byte[] bytes, IntConsumer c) {
        byteArrayConsumer(new File(file), bytes, c);
    }

    /**
     * byte[]消费者
     *
     * @param file  file
     * @param bytes bytes
     * @param c     consumer
     */
    public static void byteArrayConsumer(File file, byte[] bytes, IntConsumer c) {
        try (InputStream in = Files1.openInputStream(file)) {
            Streams.byteArrayConsumer(in, bytes, c);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- byte array iterator --------------------

    public static ByteArrayIterator byteArrayIterator(String file, byte[] buffer) {
        return byteArrayIterator(new File(file), buffer);
    }

    /**
     * byte[]迭代器
     *
     * @param file   file
     * @param buffer buffer
     * @return ByteArrayIterator
     */
    public static ByteArrayIterator byteArrayIterator(File file, byte[] buffer) {
        try {
            return Streams.byteArrayIterator(Files1.openInputStream(file), buffer).autoClose(true);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- fast read all --------------------

    public static byte[] readAllBytesFast(String file) {
        return readAllBytesFast(Paths.get(file));
    }

    public static byte[] readAllBytesFast(File file) {
        return readAllBytesFast(Paths.get(file.getAbsolutePath()));
    }

    /**
     * 读取文件所有字节
     *
     * @param path path
     * @return bytes
     */
    public static byte[] readAllBytesFast(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- fast read lines --------------------

    public static List<String> readLinesFast(String file) {
        return readLinesFast(Paths.get(file), C_UTF_8);
    }

    public static List<String> readLinesFast(File file) {
        return readLinesFast(Paths.get(file.getAbsolutePath()), C_UTF_8);
    }

    public static List<String> readLinesFast(Path file) {
        return readLinesFast(file, C_UTF_8);
    }

    public static List<String> readLinesFast(String file, String charset) {
        return readLinesFast(Paths.get(file), Charset.forName(charset));
    }

    public static List<String> readLinesFast(File file, String charset) {
        return readLinesFast(Paths.get(file.getAbsolutePath()), Charset.forName(charset));
    }

    /**
     * 读取文件所有行
     *
     * @param file    文件
     * @param charset 编码
     * @return 行
     */
    public static List<String> readLinesFast(Path file, Charset charset) {
        try {
            return Files.readAllLines(file, Objects1.def(charset, C_UTF_8));
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- fast line consumer --------------------

    public static void lineConsumerFast(String file, Consumer<String> c) {
        lineConsumerFast(Paths.get(file), C_UTF_8, c);
    }

    public static void lineConsumerFast(File file, Consumer<String> c) {
        lineConsumerFast(Paths.get(file.getAbsolutePath()), C_UTF_8, c);
    }

    public static void lineConsumerFast(Path file, Consumer<String> c) {
        lineConsumerFast(file, C_UTF_8, c);
    }

    public static void lineConsumerFast(String file, String charset, Consumer<String> c) {
        lineConsumerFast(Paths.get(file), Charset.forName(charset), c);
    }

    public static void lineConsumerFast(File file, String charset, Consumer<String> c) {
        lineConsumerFast(Paths.get(file.getAbsolutePath()), Charset.forName(charset), c);
    }

    /**
     * 行消费者
     *
     * @param file    file
     * @param charset charset
     * @param c       consumer
     */
    public static void lineConsumerFast(Path file, Charset charset, Consumer<String> c) {
        try (Stream<String> stream = Files.lines(file, Objects1.def(charset, C_UTF_8))) {
            stream.forEach(c);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- fast line iterator --------------------

    public static LineIterator lineIteratorFast(String file) {
        return lineIteratorFast(Paths.get(file), C_UTF_8);
    }

    public static LineIterator lineIteratorFast(File file) {
        return lineIteratorFast(Paths.get(file.getAbsolutePath()), C_UTF_8);
    }

    public static LineIterator lineIteratorFast(Path file) {
        return lineIteratorFast(file, C_UTF_8);
    }

    public static LineIterator lineIteratorFast(String file, String charset) {
        return lineIteratorFast(Paths.get(file), Charset.forName(charset));
    }

    public static LineIterator lineIteratorFast(File file, String charset) {
        return lineIteratorFast(Paths.get(file.getAbsolutePath()), Charset.forName(charset));
    }

    /**
     * 行迭代器
     *
     * @param file    file
     * @param charset charset
     * @return LineIterator
     */
    public static LineIterator lineIteratorFast(Path file, Charset charset) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(openInputStreamFast(file), Objects1.def(charset, C_UTF_8)))) {
            return Streams.lineIterator(reader).autoClose(true);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- fast byte array consumer --------------------

    public static void byteArrayConsumerFast(String file, byte[] bytes, IntConsumer c) {
        byteArrayConsumerFast(Paths.get(file), bytes, c);
    }

    public static void byteArrayConsumerFast(File file, byte[] bytes, IntConsumer c) {
        byteArrayConsumerFast(Paths.get(file.getAbsolutePath()), bytes, c);
    }

    /**
     * byte[]消费者
     *
     * @param file  file
     * @param bytes bytes
     * @param c     consumer
     */
    public static void byteArrayConsumerFast(Path file, byte[] bytes, IntConsumer c) {
        try (InputStream in = Files1.openInputStreamFast(file)) {
            Streams.byteArrayConsumer(in, bytes, c);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- fast byte array iterator --------------------

    public static ByteArrayIterator byteArrayIteratorFast(String file, byte[] buffer) {
        return byteArrayIteratorFast(Paths.get(file), buffer);
    }

    public static ByteArrayIterator byteArrayIteratorFast(File file, byte[] buffer) {
        return byteArrayIteratorFast(Paths.get(file.getAbsolutePath()), buffer);
    }

    /**
     * byte[]迭代器
     *
     * @param file   file
     * @param buffer buffer
     * @return ByteArrayIterator
     */
    public static ByteArrayIterator byteArrayIteratorFast(Path file, byte[] buffer) {
        try {
            return Streams.byteArrayIterator(openInputStreamFast(file), buffer).autoClose(true);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

}
