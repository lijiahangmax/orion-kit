package com.orion.utils.io;

import com.orion.lang.iterator.LineIterator;
import com.orion.utils.Exceptions;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.orion.utils.io.Files1.openInputStream;
import static com.orion.utils.io.Streams.close;

/**
 * 文件读取工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/27 14:39
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileReaders {

    private FileReaders() {
    }

    /**
     * 读取文件
     *
     * @param file  文件
     * @param bytes 读取的数组
     * @return 读取的长度
     */
    public static int read(String file, byte[] bytes) {
        return read(new File(file), bytes);
    }

    /**
     * 读取文件
     *
     * @param file  文件
     * @param bytes 读取的数组
     * @return 读取的长度
     */
    public static int read(File file, byte[] bytes) {
        try (FileInputStream in = openInputStream(file)) {
            return in.read(bytes);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 读取文件
     *
     * @param file     文件
     * @param bytes    读取的数组
     * @param skipByte 文件起始偏移量
     * @return 读取的长度
     */
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

    /**
     * 读取一行
     *
     * @param file     文件
     * @param skipByte 偏移量
     * @return 行
     */
    public static String readLine(String file, long skipByte) {
        return readLine(new File(file), skipByte, null);
    }

    /**
     * 读取一行
     *
     * @param file     文件
     * @param skipByte 偏移量
     * @return 行
     */
    public static String readLine(File file, long skipByte) {
        return readLine(file, skipByte, null);
    }

    /**
     * 读取一行
     *
     * @param file     文件
     * @param skipByte 偏移量
     * @param charset  编码格式
     * @return 行
     */
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

    /**
     * 读取文件
     *
     * @param file     文件
     * @param skipByte 文件偏移量
     * @return 行
     */
    public static List<String> readLines(File file, long skipByte) {
        return readLines(file, skipByte, -1, null);
    }

    /**
     * 读取文件
     *
     * @param file     文件
     * @param skipByte 文件偏移量
     * @return 行
     */
    public static List<String> readLines(String file, long skipByte) {
        return readLines(new File(file), skipByte, -1, null);
    }

    /**
     * 读取文件
     *
     * @param file      文件
     * @param skipByte  文件偏移量
     * @param readLines 读取多少行 <= 0 所有行
     * @return 行
     */
    public static List<String> readLines(File file, long skipByte, int readLines) {
        return readLines(file, skipByte, readLines, null);
    }

    /**
     * 读取文件
     *
     * @param file      文件
     * @param skipByte  文件偏移量
     * @param readLines 读取多少行 <= 0 所有行
     * @return 行
     */
    public static List<String> readLines(String file, long skipByte, int readLines) {
        return readLines(new File(file), skipByte, readLines, null);
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

    /**
     * 读取文件
     *
     * @param file      文件
     * @param skipLine  文件偏移行
     * @param readLines 读取多少行  <=0 所有行
     * @return 行
     */
    public static List<String> readLines(File file, int skipLine, int readLines) {
        return readLines(file, skipLine, readLines, null);
    }

    /**
     * 读取文件
     *
     * @param file      文件
     * @param skipLine  文件偏移行
     * @param readLines 读取多少行  <=0 所有行
     * @return 行
     */
    public static List<String> readLines(String file, int skipLine, int readLines) {
        return readLines(new File(file), skipLine, readLines, null);
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

    /**
     * 行消费者
     *
     * @param file file
     * @param c    consumer
     */
    public static void lineConsumer(String file, Consumer<String> c) {
        lineConsumer(new File(file), null, c);
    }

    /**
     * 行消费者
     *
     * @param file file
     * @param c    consumer
     */
    public static void lineConsumer(File file, Consumer<String> c) {
        lineConsumer(file, null, c);
    }

    /**
     * 行消费者
     *
     * @param file    file
     * @param charset charset
     * @param c       consumer
     */
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
        try (InputStream in = Files1.openInputStreamSafe(file)) {
            if (charset == null) {
                Streams.lineConsumer(in, c);
            } else {
                Streams.lineConsumer(in, charset, c);
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 行迭代器
     *
     * @param file file
     * @return LineIterator
     */
    public static LineIterator lineIterator(String file) {
        return lineIterator(new File(file), null);
    }

    /**
     * 行迭代器
     *
     * @param file file
     * @return LineIterator
     */
    public static LineIterator lineIterator(File file) {
        return lineIterator(file, null);
    }

    /**
     * 行迭代器
     *
     * @param file    file
     * @param charset charset
     * @return LineIterator
     */
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
                return new LineIterator(new InputStreamReader(Files1.openInputStream(file)));
            } else {
                return new LineIterator(new InputStreamReader(Files1.openInputStream(file), charset));
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- fast --------------------

    /**
     * 读取文件所有字节
     *
     * @param file file
     * @return bytes
     */
    public static byte[] readFast(String file) {
        return readFast(Paths.get(file));
    }

    /**
     * 读取文件所有字节
     *
     * @param file file
     * @return bytes
     */
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

    /**
     * 读取文件所有行
     *
     * @param file 文件
     * @return 行
     */
    public static List<String> readLinesFast(String file) {
        return readLinesFast(Paths.get(file), null);
    }

    /**
     * 读取文件所有行
     *
     * @param file 文件
     * @return 行
     */
    public static List<String> readLinesFast(File file) {
        return readLinesFast(Paths.get(file.getAbsolutePath()), null);
    }

    /**
     * 读取文件所有行
     *
     * @param file 文件
     * @return 行
     */
    public static List<String> readLinesFast(Path file) {
        return readLinesFast(file, null);
    }

    /**
     * 读取文件所有行
     *
     * @param file    文件
     * @param charset 编码
     * @return 行
     */
    public static List<String> readLinesFast(String file, String charset) {
        return readLinesFast(Paths.get(file), Charset.forName(charset));
    }

    /**
     * 读取文件所有行
     *
     * @param file    文件
     * @param charset 编码
     * @return 行
     */
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

    /**
     * 行消费者
     *
     * @param file file
     * @param c    consumer
     */
    public static void lineConsumerFast(String file, Consumer<String> c) {
        lineConsumerFast(Paths.get(file), null, c);
    }

    /**
     * 行消费者
     *
     * @param file file
     * @param c    consumer
     */
    public static void lineConsumerFast(File file, Consumer<String> c) {
        lineConsumerFast(Paths.get(file.getAbsolutePath()), null, c);
    }

    /**
     * 行消费者
     *
     * @param file file
     * @param c    consumer
     */
    public static void lineConsumerFast(Path file, Consumer<String> c) {
        lineConsumerFast(file, null, c);
    }

    /**
     * 行消费者
     *
     * @param file    file
     * @param charset charset
     * @param c       consumer
     */
    public static void lineConsumerFast(String file, String charset, Consumer<String> c) {
        lineConsumerFast(Paths.get(file), Charset.forName(charset), c);
    }

    /**
     * 行消费者
     *
     * @param file    file
     * @param charset charset
     * @param c       consumer
     */
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

    /**
     * 行迭代器
     *
     * @param file file
     * @return LineIterator
     */
    public static LineIterator lineIteratorFast(String file) {
        return lineIteratorFast(Paths.get(file), null);
    }

    /**
     * 行迭代器
     *
     * @param file file
     * @return LineIterator
     */
    public static LineIterator lineIteratorFast(File file) {
        return lineIteratorFast(Paths.get(file.getAbsolutePath()), null);
    }

    /**
     * 行迭代器
     *
     * @param file file
     * @return LineIterator
     */
    public static LineIterator lineIteratorFast(Path file) {
        return lineIteratorFast(file, null);
    }

    /**
     * 行迭代器
     *
     * @param file    file
     * @param charset charset
     * @return LineIterator
     */
    public static LineIterator lineIteratorFast(String file, String charset) {
        return lineIteratorFast(Paths.get(file), Charset.forName(charset));
    }

    /**
     * 行迭代器
     *
     * @param file    file
     * @param charset charset
     * @return LineIterator
     */
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

}
