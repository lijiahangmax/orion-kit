package com.orion.utils.io;

import com.orion.utils.Exceptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

}
