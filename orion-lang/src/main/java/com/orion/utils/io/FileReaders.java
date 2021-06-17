package com.orion.utils.io;

import com.orion.constant.Const;
import com.orion.constant.Letters;
import com.orion.lang.iterator.ByteArrayIterator;
import com.orion.lang.iterator.LineIterator;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.Stream;

import static com.orion.utils.io.Files1.openInputStream;
import static com.orion.utils.io.Streams.close;

/**
 * 文件读取工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/27 14:39
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileReaders {

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
        return readLine(reader, Const.UTF_8);
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
        while (-1 != (read = reader.read(bytes))) {
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

    public static String readLines(RandomAccessFile reader) throws IOException {
        return readLines(reader, Const.UTF_8);
    }

    /**
     * 从当前偏移量读取到最后一行
     *
     * @param reader  输入流
     * @param charset charset
     * @return lines
     * @throws IOException I/O异常
     */
    public static String readLines(RandomAccessFile reader, String charset) throws IOException {
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
        if (len == 0L) {
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
                    break;
                }
            }
        }
        reader.seek(beforePos);
        if (pos == 0) {
            return 0;
        }
        return pos + 1;
    }

    public static String readTailLines(RandomAccessFile reader, int line) throws IOException {
        return readTailLines(reader, Const.UTF_8, line);
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
        return read(new File(file), bytes);
    }

    public static int read(File file, byte[] bytes) {
        try (FileInputStream in = openInputStream(file)) {
            return in.read(bytes);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static int read(String file, byte[] bytes, long skipByte) {
        return read(new File(file), bytes, skipByte);
    }

    /**
     * 读取文件
     *
     * @param file     文件
     * @param bytes    读取的数组
     * @param skipByte 文件起始偏移量
     * @return 读取的长度
     */
    public static int read(File file, byte[] bytes, long skipByte) {
        try (FileInputStream in = openInputStream(file)) {
            if (skipByte > 0) {
                in.skip(skipByte);
            }
            return in.read(bytes);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- read line --------------------

    public static String readLine(String file, long skipByte) {
        return readLine(new File(file), skipByte, null);
    }

    public static String readLine(File file, long skipByte) {
        return readLine(file, skipByte, null);
    }

    public static String readLine(String file, long skipByte, String charset) {
        return readLine(new File(file), skipByte, charset);
    }

    /**
     * 读取一行
     *
     * @param file     文件
     * @param skipByte 偏移量
     * @param charset  编码格式
     * @return 行
     */
    public static String readLine(File file, long skipByte, String charset) {
        BufferedReader reader = null;
        try {
            if (charset == null) {
                reader = new BufferedReader(new InputStreamReader(openInputStream(file)));
            } else {
                reader = new BufferedReader(new InputStreamReader(openInputStream(file), charset));
            }
            if (skipByte > 0) {
                reader.skip(skipByte);
            }
            return reader.readLine();
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            close(reader);
        }
    }

    // -------------------- read lines --------------------

    public static List<String> readLines(File file, long skipByte) {
        return readLines(file, skipByte, -1, null);
    }

    public static List<String> readLines(String file, long skipByte) {
        return readLines(new File(file), skipByte, -1, null);
    }

    public static List<String> readLines(File file, long skipByte, int readLines) {
        return readLines(file, skipByte, readLines, null);
    }

    public static List<String> readLines(String file, long skipByte, int readLines) {
        return readLines(new File(file), skipByte, readLines, null);
    }

    public static List<String> readLines(String file, long skipByte, int readLines, String charset) {
        return readLines(new File(file), skipByte, readLines, charset);
    }

    /**
     * 读取文件
     *
     * @param file      文件
     * @param skipByte  文件偏移量
     * @param readLines 读取多少行  <= 0 所有行
     * @param charset   编码格式
     * @return 行
     */
    public static List<String> readLines(File file, long skipByte, int readLines, String charset) {
        BufferedReader reader = null;
        try {
            if (charset == null) {
                reader = new BufferedReader(new InputStreamReader(openInputStream(file)));
            } else {
                reader = new BufferedReader(new InputStreamReader(openInputStream(file), charset));
            }
            if (skipByte > 0) {
                reader.skip(skipByte);
            }
            List<String> list = new ArrayList<>();
            if (readLines <= 0) {
                String line;
                while (null != (line = reader.readLine())) {
                    list.add(line);
                }
            } else {
                String line;
                int i = 0;
                while (null != (line = reader.readLine()) && ++i <= readLines) {
                    list.add(line);
                }
            }
            return list;
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            close(reader);
        }
    }

    public static List<String> readLines(File file, int skipLine, int readLines) {
        return readLines(file, skipLine, readLines, null);
    }

    public static List<String> readLines(String file, int skipLine, int readLines) {
        return readLines(new File(file), skipLine, readLines, null);
    }

    public static List<String> readLines(String file, int skipLine, int readLines, String charset) {
        return readLines(new File(file), skipLine, readLines, charset);
    }

    /**
     * 读取文件
     *
     * @param file      文件
     * @param skipLine  文件偏移行
     * @param readLines 读取多少行  <=0 所有行
     * @param charset   编码格式
     * @return 行
     */
    public static List<String> readLines(File file, int skipLine, int readLines, String charset) {
        BufferedReader reader = null;
        try {
            if (charset == null) {
                reader = new BufferedReader(new InputStreamReader(openInputStream(file)));
            } else {
                reader = new BufferedReader(new InputStreamReader(openInputStream(file), charset));
            }
            List<String> list = new ArrayList<>();
            if (skipLine > 0) {
                for (int i = 0; i < skipLine; i++) {
                    if (reader.readLine() == null) {
                        return list;
                    }
                }
            }
            if (readLines <= 0) {
                String line;
                while (null != (line = reader.readLine())) {
                    list.add(line);
                }
            } else {
                String line;
                int i = 0;
                while (null != (line = reader.readLine()) && ++i <= readLines) {
                    list.add(line);
                }
            }
            return list;
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            close(reader);
        }
    }

    // -------------------- line consumer --------------------

    public static void lineConsumer(String file, Consumer<String> c) {
        lineConsumer(new File(file), null, c);
    }

    public static void lineConsumer(File file, Consumer<String> c) {
        lineConsumer(file, null, c);
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
        try (InputStream in = Files1.openInputStream(file)) {
            Streams.lineConsumer(in, charset, c);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- line iterator --------------------

    public static LineIterator lineIterator(String file) {
        return lineIterator(new File(file), null);
    }

    public static LineIterator lineIterator(File file) {
        return lineIterator(file, null);
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
        try {
            if (charset == null) {
                return new LineIterator(new InputStreamReader(Files1.openInputStream(file))).autoClose(true);
            } else {
                return new LineIterator(new InputStreamReader(Files1.openInputStream(file), charset)).autoClose(true);
            }
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
            return new ByteArrayIterator(Files1.openInputStream(file), buffer).autoClose(true);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- fast read --------------------

    public static byte[] readFast(String file) {
        return readFast(Paths.get(file));
    }

    public static byte[] readFast(File file) {
        return readFast(Paths.get(file.getAbsolutePath()));
    }

    /**
     * 读取文件所有字节
     *
     * @param path path
     * @return bytes
     */
    public static byte[] readFast(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- fast read lines --------------------

    public static List<String> readLinesFast(String file) {
        return readLinesFast(Paths.get(file), null);
    }

    public static List<String> readLinesFast(File file) {
        return readLinesFast(Paths.get(file.getAbsolutePath()), null);
    }

    public static List<String> readLinesFast(Path file) {
        return readLinesFast(file, null);
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
            if (charset == null) {
                return Files.readAllLines(file);
            } else {
                return Files.readAllLines(file, charset);
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- fast line consumer --------------------

    public static void lineConsumerFast(String file, Consumer<String> c) {
        lineConsumerFast(Paths.get(file), null, c);
    }

    public static void lineConsumerFast(File file, Consumer<String> c) {
        lineConsumerFast(Paths.get(file.getAbsolutePath()), null, c);
    }

    public static void lineConsumerFast(Path file, Consumer<String> c) {
        lineConsumerFast(file, null, c);
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
        Stream<String> stream = null;
        try {
            if (charset == null) {
                stream = Files.lines(file);
            } else {
                stream = Files.lines(file, charset);
            }
            stream.forEach(c);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            close(stream);
        }
    }

    // -------------------- fast line iterator --------------------

    public static LineIterator lineIteratorFast(String file) {
        return lineIteratorFast(Paths.get(file), null);
    }

    public static LineIterator lineIteratorFast(File file) {
        return lineIteratorFast(Paths.get(file.getAbsolutePath()), null);
    }

    public static LineIterator lineIteratorFast(Path file) {
        return lineIteratorFast(file, null);
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
        try {
            if (charset == null) {
                return new LineIterator(Files.newBufferedReader(file));
            } else {
                return new LineIterator(Files.newBufferedReader(file, charset));
            }
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
            return new ByteArrayIterator(Files1.openInputStreamFast(file), buffer).autoClose(true);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

}
