package com.orion.utils.io;

import com.orion.id.UUIds;
import com.orion.utils.Exceptions;
import com.orion.utils.Matches;
import com.orion.utils.Strings;
import com.orion.utils.Systems;
import com.orion.utils.crypto.enums.HashMessageDigest;

import java.io.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 文件工具类
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/10/9 12:01
 */
public class Files1 {

    private Files1() {
    }

    /**
     * 缓冲区默认大小
     */
    private static final int BUFFER_SIZE = 1024 * 8;

    /**
     * IO 临时文件夹
     */
    private static final String IO_TEMP_DIR = File.separator + ".io.temp" + File.separator;

    private static final String[] SIZE_UNIT = {"K", "KB", "M", "MB", "MBPS", "G", "GB", "T", "TB", "B"};

    private static final long[] SIZE_UNIT_EFFECT = {1000, 1024, 1000 * 1000, 1024 * 1024, 1024 * 128, 1000 * 1000 * 1000, 1024 * 1024 * 1024, 1000 * 1000 * 1000 * 1000L, 1024 * 1024 * 1024 * 1024L, 1};

    // -------------------- attr --------------------

    /**
     * 获取文件属性
     *
     * @param file 文件
     * @return FileAttribute
     */
    public static FileAttribute getAttribute(String file) {
        return getAttribute(Paths.get(file));
    }

    /**
     * 获取文件属性
     *
     * @param file 文件
     * @return FileAttribute
     */
    public static FileAttribute getAttribute(File file) {
        return getAttribute(Paths.get(file.getAbsolutePath()));
    }

    /**
     * 获取文件属性
     *
     * @param file 文件
     * @return FileAttribute
     */
    public static FileAttribute getAttribute(URI file) {
        return getAttribute(Paths.get(file));
    }

    /**
     * 获取文件属性
     *
     * @param file 文件
     * @return FileAttribute 文件不存在或报错返回null
     */
    public static FileAttribute getAttribute(Path file) {
        if (!Files.exists(file)) {
            return null;
        }
        try {
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            return new FileAttribute(file, attr);
        } catch (IOException e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    // -------------------- file --------------------

    /**
     * 获取文件或文件夹大小
     *
     * @param file 文件
     * @return ignore
     */
    public static long getSize(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                return file.length();
            } else {
                long size = 0;
                List<File> files = listFiles(file, true);
                for (File f : files) {
                    size += f.length();
                }
                return size;
            }
        }
        return 0L;
    }

    /**
     * 获取文件或文件夹大小
     *
     * @param file 文件
     * @return ignore
     */
    public static long getSize(String file) {
        return getSize(new File(file));
    }

    /**
     * 获取大小
     *
     * @param size 字节
     * @return ignore
     */
    public static String getSize(long size) {
        String s;
        if (size / (SIZE_UNIT_EFFECT[1] * SIZE_UNIT_EFFECT[1] * SIZE_UNIT_EFFECT[1]) > 0) {
            s = size / (SIZE_UNIT_EFFECT[1] * SIZE_UNIT_EFFECT[1] * SIZE_UNIT_EFFECT[1]) + " GB";
        } else if (size / (SIZE_UNIT_EFFECT[1] * SIZE_UNIT_EFFECT[1]) > 0) {
            s = size / (SIZE_UNIT_EFFECT[1] * SIZE_UNIT_EFFECT[1]) + " MB";
        } else if (size / SIZE_UNIT_EFFECT[1] > 0) {
            s = size / SIZE_UNIT_EFFECT[1] + " KB";
        } else {
            s = size + " bytes";
        }
        return s;
    }

    /**
     * 获取size
     *
     * @param size 1K = 1000b  1KB = 1024b  1MBPS = 1024*128
     * @return bytes
     */
    public static long getByteSize(String size) {
        if (Strings.isBlank(size)) {
            return 0L;
        }
        int effectIndex = -1;
        int unitLen = 0;
        size = size.toUpperCase();
        for (int i = 0; i < SIZE_UNIT.length; i++) {
            if (size.endsWith(SIZE_UNIT[i])) {
                effectIndex = i;
                unitLen = SIZE_UNIT[i].length();
                break;
            }
        }
        if (effectIndex == -1) {
            return 0L;
        }
        double d = Double.parseDouble(size.substring(0, size.length() - unitLen).trim());
        return (long) (d * SIZE_UNIT_EFFECT[effectIndex]);
    }

    /**
     * 将resource文件保存到本地
     *
     * @param source   文件
     * @param childDir 子文件夹
     * @param charset  编码格式
     * @param force    如果存在是否覆盖
     * @return 文件路径 null未找到资源
     */
    public static String resourceToFile(String source, String childDir, String charset, boolean force) {
        File file = new File(getRootPathFile(childDir, source));
        boolean write = false;
        if (file.exists() && file.isFile()) {
            if (force) {
                write = true;
            }
        } else {
            touch(file, charset);
            write = true;
        }
        if (write) {
            InputStream in = Files1.class.getClassLoader().getResourceAsStream(source);
            if (in == null) {
                return null;
            }
            try (OutputStream out = openOutputStream(file)) {
                byte[] bs = new byte[BUFFER_SIZE];
                int read;
                while (-1 != (read = in.read(bs))) {
                    out.write(bs, 0, read);
                }
                out.flush();
            } catch (Exception e) {
                throw Exceptions.ioRuntime(e);
            } finally {
                Streams.close(in);
            }
        }
        return file.getAbsolutePath();
    }

    /**
     * 将流保存为文件
     *
     * @param reader 流
     * @param file   文件
     * @return 文件路径
     */
    public static String streamToFile(InputStreamReader reader, File file) {
        return streamToFile(reader, file, null);
    }

    /**
     * 将流保存为文件
     *
     * @param reader  流
     * @param file    文件
     * @param charset 编码格式
     * @return 文件路径
     */
    public static String streamToFile(InputStreamReader reader, File file, String charset) {
        touch(file);
        OutputStreamWriter writer = null;
        try {
            if (charset == null) {
                writer = new OutputStreamWriter(openOutputStream(file));
            } else {
                writer = new OutputStreamWriter(openOutputStream(file), charset);
            }
            char[] cs = new char[BUFFER_SIZE];
            int read;
            while (-1 != (read = reader.read(cs))) {
                writer.write(cs, 0, read);
            }
            writer.flush();
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            Streams.close(writer);
        }
        return file.getAbsolutePath();
    }

    /**
     * 将流保存为文件
     *
     * @param in   流
     * @param file 文件
     * @return 文件路径
     */
    public static String streamToFile(InputStream in, File file) {
        touch(file);
        try (OutputStream out = openOutputStream(file)) {
            byte[] bs = new byte[BUFFER_SIZE];
            int read;
            while (-1 != (read = in.read(bs))) {
                out.write(bs, 0, read);
            }
            out.flush();
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
        return file.getAbsolutePath();
    }

    /**
     * 合并文件内容到target
     *
     * @param sources 文件
     * @param target  合并的文件
     */
    public static void mergeFile(List<String> sources, String target) {
        mergeFile(toFiles(sources), new File(target));
    }

    /**
     * 合并文件内容到target
     *
     * @param sources 文件
     * @param target  合并的文件
     */
    public static void mergeFile(List<File> sources, File target) {
        if (!(target.exists() && target.isFile())) {
            touch(target);
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            for (File source : sources) {
                in = openInputStream(source);
                out = new FileOutputStream(target, true);
                byte[] bytes = new byte[BUFFER_SIZE];
                int read;
                while (-1 != (read = in.read(bytes))) {
                    out.write(bytes, 0, read);
                }
                Streams.close(in);
                in = null;
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            Streams.close(in);
            Streams.close(out);
        }

    }

    /**
     * 合并文件内容到target
     *
     * @param source 文件
     * @param target 合并的文件
     */
    public static void mergeFile(String source, String target) {
        mergeFile(new File(source), new File(target));
    }

    /**
     * 合并文件内容到target
     *
     * @param source 文件
     * @param target 合并的文件
     */
    public static void mergeFile(File source, File target) {
        OutputStream out = null;
        try (InputStream in = openInputStream(source)) {
            out = new FileOutputStream(target, true);
            byte[] bytes = new byte[BUFFER_SIZE];
            int read;
            while (-1 != (read = in.read(bytes))) {
                out.write(bytes, 0, read);
            }
            out.flush();
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            Streams.close(out);
        }
    }

    /**
     * 复制目录
     *
     * @param source 源目录
     * @param target 目标目录
     */
    public static void copyDir(String source, String target) {
        copyDir(new File(source), new File(target));
    }

    /**
     * 复制目录
     *
     * @param source 源目录
     * @param target 目标目录
     */
    public static void copyDir(File source, File target) {
        if (!target.exists() || !target.isDirectory()) {
            target.mkdirs();
        }
        File[] files = source.listFiles();
        if (files != null) {
            for (File file : files) {
                String path = file.getName();
                if (file.isDirectory()) {
                    copyDir(file, new File(target.getAbsolutePath() + "/" + path));
                } else {
                    copy(file, new File(target.getAbsolutePath() + "/" + path));
                }
            }
        }
    }

    /**
     * 复制文件
     *
     * @param source 源文件
     * @param target 目标文件
     */
    public static void copy(String source, String target) {
        copy(new File(source), new File(target));
    }

    /**
     * 复制文件
     *
     * @param source 源文件
     * @param target 目标文件
     */
    public static void copy(File source, File target) {
        try (FileChannel inc = openInputStream(source).getChannel();
             FileChannel outc = openOutputStream(target).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (inc.read(buffer) != -1) {
                buffer.flip();
                outc.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 创建文件
     *
     * @param file 文件
     */
    public static void touch(String file) {
        touch(new File(file), null, false);
    }

    /**
     * 创建文件
     *
     * @param file 文件
     */
    public static void touch(File file) {
        touch(file, null, false);
    }

    /**
     * 创建文件
     *
     * @param file    文件
     * @param charset 编码格式
     */
    public static void touch(String file, String charset) {
        touch(new File(file), charset, false);
    }

    /**
     * 创建文件
     *
     * @param file    文件
     * @param charset 编码格式
     */
    public static void touch(File file, String charset) {
        touch(file, charset, false);
    }

    /**
     * 创建文件
     *
     * @param file  文件
     * @param clean 如果文件存在是否清除文件内容
     */
    public static void touch(String file, boolean clean) {
        touch(new File(file), null, clean);
    }

    /**
     * 创建文件
     *
     * @param file  文件
     * @param clean 如果文件存在是否清除文件内容
     */
    public static void touch(File file, boolean clean) {
        touch(file, null, clean);
    }

    /**
     * 创建文件
     *
     * @param file    文件
     * @param charset 编码格式
     * @param clean   如果文件存在是否清除文件内容
     */
    public static void touch(String file, String charset, boolean clean) {
        touch(new File(file), charset, clean);
    }

    /**
     * 创建文件
     *
     * @param file    文件
     * @param charset 编码格式
     * @param clean   如果文件存在是否清除文件内容
     */
    public static void touch(File file, String charset, boolean clean) {
        File dir = file.getParentFile();
        boolean create = false;
        if (!dir.exists()) {
            dir.mkdirs();
            create = true;
        } else {
            if (!file.exists() || !file.isFile()) {
                create = true;
            }
        }
        if (create) {
            try {
                file.createNewFile();
                if (charset != null) {
                    OutputStreamWriter osw = null;
                    try {
                        file.createNewFile();
                        osw = new OutputStreamWriter(openOutputStream(file), charset);
                        osw.write("");
                    } catch (IOException e) {
                        throw Exceptions.ioRuntime(e);
                    } finally {
                        Streams.close(osw);
                    }
                }
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            }
        } else if (clean) {
            cleanFile(file);
        }
    }

    /**
     * 获取一个IO临时文件
     *
     * @return io临时文件路径
     */
    public static String getTempFilePath() {
        return Systems.HOME_DIR + IO_TEMP_DIR + UUIds.random32() + ".temp";
    }

    /**
     * 创建一个IO临时文件
     *
     * @return io临时文件路径
     */
    public static File touchTempFile() {
        String path = getTempFilePath();
        touch(path);
        return new File(path);
    }

    /**
     * 创建一个IO临时文件
     *
     * @param exitDelete 是否退出删除
     * @return io临时文件路径
     */
    public static File touchTempFile(boolean exitDelete) {
        String path = getTempFilePath();
        touch(path);
        File file = new File(path);
        if (exitDelete) {
            file.deleteOnExit();
        }
        return file;
    }

    /**
     * 文件剪切
     *
     * @param file   文件
     * @param targer 目标位置
     */
    public static void mv(File file, File targer) {
        file.renameTo(targer);
    }

    /**
     * 文件重命名
     *
     * @param file 文件
     * @param name 名称
     */
    public static void mv(File file, String name) {
        file.renameTo(new File(file.getParentFile() + "/" + name));
    }

    /**
     * 文件剪切
     *
     * @param file   文件
     * @param targer 目标位置
     */
    public static void mv(String file, File targer) {
        new File(file).renameTo(targer);
    }

    /**
     * 文件重命名
     *
     * @param file 文件
     * @param name 名称
     */
    public static void mv(String file, String name) {
        File f = new File(file);
        f.renameTo(new File(f.getParentFile() + "/" + name));
    }

    /**
     * 创建文件夹
     *
     * @param file 文件夹
     */
    public static void mkdirs(String file) {
        new File(file).mkdirs();
    }

    /**
     * 创建文件夹
     *
     * @param file 文件夹
     */
    public static void mkdirs(File file) {
        file.mkdirs();
    }

    /**
     * 删除一个文件夹
     *
     * @param file 文件夹
     */
    public static void deleteDir(String file) {
        deleteDir(new File(file));
    }

    /**
     * 删除一个文件夹
     *
     * @param file 文件夹
     */
    public static void deleteDir(File file) {
        List<File> files = listFiles(file, true, true);
        for (File f : files) {
            if (f.isDirectory()) {
                deleteDir(f);
            } else {
                deleteFile(f);
            }
        }
        file.delete();
    }

    /**
     * 删除文件
     *
     * @param file 文件
     */
    public static void deleteFile(String file) {
        File f = new File(file);
        if (f.isFile()) {
            f.delete();
        }
    }

    /**
     * 删除文件
     *
     * @param file 文件
     */
    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
        }
    }

    /**
     * 删除大文件
     *
     * @param file 文件
     */
    public static void deleteBigFile(String file) {
        deleteBigFile(new File(file));
    }

    /**
     * 删除大文件
     *
     * @param file 文件
     */
    public static void deleteBigFile(File file) {
        cleanFile(file);
        file.delete();
    }

    /**
     * 清空文件
     *
     * @param file 文件
     */
    public static void cleanFile(String file) {
        cleanFile(new File(file));
    }

    /**
     * 清空文件
     *
     * @param file 文件
     */
    public static void cleanFile(File file) {
        if (!file.exists()) {
            return;
        }
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("");
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 获取文件最后的修改时间
     *
     * @param file 文件
     * @return 最后修改时间
     */
    public static Date getModifyTime(File file) {
        return new Date(file.lastModified());
    }

    /**
     * 获取文件最后的修改时间
     *
     * @param file 文件
     * @return 最后修改时间
     */
    public static Date getModifyTime(String file) {
        return new Date(new File(file).lastModified());
    }

    /**
     * 判断文件是否为空
     *
     * @param file 文件
     * @return true 空
     */
    public static boolean isEmpty(File file) {
        return file.length() == 0;
    }

    /**
     * 判断文件是否为空
     *
     * @param file 文件
     * @return true 空
     */
    public static boolean isEmpty(String file) {
        return new File(file).length() == 0;
    }

    /**
     * 获取文件行分隔符
     *
     * @param file 文件
     * @return \n \r \r\n
     */
    public static String getFileLineSeparator(String file) {
        return getFileLineSeparator(new File(file));
    }

    /**
     * 获取文件行分隔符
     *
     * @param file 文件
     * @return \n \r \r\n
     */
    public static String getFileLineSeparator(File file) {
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(file, "r");
            long length = r.length();
            if (length == 0) {
                return "\n";
            } else if (length >= 2) {
                r.seek(length - 2);
                byte[] bs = new byte[2];
                r.read(bs);
                if (bs[0] == 13 && bs[1] == 10) {
                    return "\r\n";
                } else if (bs[1] == 13) {
                    return "\r";
                }
            } else if (length == 1) {
                r.seek(length - 1);
                if (r.read() == 13) {
                    return "\r";
                }
            }
        } catch (IOException e) {
            // ignore
        } finally {
            Streams.close(r);
        }
        return "\n";
    }

    /**
     * 获取文件行分隔符
     *
     * @param file 文件
     * @return \n \r \r\n null
     */
    public static String getFileEndLineSeparator(String file) {
        return getFileEndLineSeparator(new File(file));
    }

    /**
     * 获取文件行分隔符
     *
     * @param file 文件
     * @return \n \r \r\n null
     */
    public static String getFileEndLineSeparator(File file) {
        try (RandomAccessFile r = new RandomAccessFile(file, "r")) {
            long length = r.length();
            if (length == 0) {
                return "\n";
            } else if (length >= 2) {
                r.seek(length - 2);
                byte[] bs = new byte[2];
                r.read(bs);
                if (bs[0] == 13 && bs[1] == 10) {
                    return "\r\n";
                } else if (bs[1] == 13) {
                    return "\r";
                } else if (bs[1] == 10) {
                    return "\n";
                }
            } else if (length == 1) {
                r.seek(length - 1);
                int read = r.read();
                if (read == 13) {
                    return "\r";
                } else if (read == 10) {
                    return "\n";
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return null;
    }

    /**
     * 文件是否以行分隔符结尾
     *
     * @param file 文件
     * @return true 是 \n \r \r\n结尾
     */
    public static boolean hasEndLineSeparator(String file) {
        return hasEndLineSeparator(new File(file));
    }

    /**
     * 文件是否以行分隔符结尾
     *
     * @param file 文件
     * @return true 是 \n \r \r\n结尾
     */
    public static boolean hasEndLineSeparator(File file) {
        try (RandomAccessFile r = new RandomAccessFile(file, "r")) {
            long length = r.length();
            if (length == 0) {
                return true;
            } else if (length >= 1) {
                r.seek(length - 1);
                int s = r.read();
                if (s == 13 || s == 10) {
                    return true;
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return false;
    }

    /**
     * 获取文件的行数
     *
     * @param file 统计的文件
     * @return 文件行数
     */
    public static int countLines(String file) {
        return countLines(new File(file));
    }

    /**
     * 获取文件的行数
     *
     * @param file 统计的文件
     * @return 文件行数
     */
    public static int countLines(File file) {
        LineNumberReader rf = null;
        try {
            rf = new LineNumberReader(new InputStreamReader(openInputStream(file)));
            rf.skip(file.length());
            return rf.getLineNumber();
        } catch (IOException e) {
            // ignore
        } finally {
            Streams.close(rf);
        }
        return 0;
    }

    // -------------------- stream --------------------

    /**
     * 打开文件输入流
     *
     * @param file 文件
     * @return 输入流
     * @throws IOException IO
     */
    public static FileInputStream openInputStream(String file) throws IOException {
        return openInputStream(new File(file));
    }

    /**
     * 打开文件输入流
     *
     * @param file 文件
     * @return 输入流
     * @throws IOException IO
     */
    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    /**
     * 打开文件输入流
     *
     * @param file 文件
     * @return 输入流
     */
    public static FileInputStream openInputStreamSafe(String file) {
        return openInputStreamSafe(new File(file));
    }

    /**
     * 打开文件输入流
     *
     * @param file 文件
     * @return 输入流
     */
    public static FileInputStream openInputStreamSafe(File file) {
        try {
            return openInputStream(file);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 打开文件输出流
     *
     * @param file 文件
     * @return 输出流
     * @throws IOException IO
     */
    public static FileOutputStream openOutputStream(String file) throws IOException {
        return openOutputStream(new File(file), false);
    }

    /**
     * 打开文件输出流
     *
     * @param file   文件
     * @param append 是否拼接
     * @return 输出流
     * @throws IOException IO
     */
    public static FileOutputStream openOutputStream(String file, boolean append) throws IOException {
        return openOutputStream(new File(file), append);
    }

    /**
     * 打开文件输出流
     *
     * @param file 文件
     * @return 输出流
     * @throws IOException IO
     */
    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }

    /**
     * 打开文件输出流
     *
     * @param file 文件
     * @return 输出流
     * @throws IOException IO
     */
    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    throw new IOException("File '" + file + "' could not be created");
                }
            }
        }
        if (append) {
            return new FileOutputStream(file, true);
        } else {
            return new FileOutputStream(file);
        }
    }

    /**
     * 打开文件输出流
     *
     * @param file 文件
     * @return 输出流
     */
    public static FileOutputStream openOutputStreamSafe(String file) {
        return openOutputStreamSafe(new File(file), false);
    }

    /**
     * 打开文件输出流
     *
     * @param file   文件
     * @param append 是否拼接
     * @return 输出流
     */
    public static FileOutputStream openOutputStreamSafe(String file, boolean append) {
        return openOutputStreamSafe(new File(file), append);
    }

    /**
     * 打开文件输出流
     *
     * @param file 文件
     * @return 输出流
     */
    public static FileOutputStream openOutputStreamSafe(File file) {
        return openOutputStreamSafe(file, false);
    }

    /**
     * 打开文件输出流
     *
     * @param file 文件
     * @return 输出流
     */
    public static FileOutputStream openOutputStreamSafe(File file, boolean append) {
        try {
            return openOutputStream(file, append);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 打开文件随机读取
     *
     * @param file file
     * @param mode r rw rws rwd
     * @return RandomAccessFile
     * @throws IOException IOException
     */
    public static RandomAccessFile openRandomAccess(String file, String mode) throws IOException {
        return openRandomAccess(new File(file), mode);
    }

    /**
     * 打开文件随机读取
     *
     * @param file file
     * @param mode r rw rws rwd
     * @return RandomAccessFile
     * @throws IOException IOException
     */
    public static RandomAccessFile openRandomAccess(File file, String mode) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    throw new IOException("File '" + file + "' could not be created");
                }
            }
        }
        return new RandomAccessFile(file, mode);
    }

    /**
     * 打开文件随机读取
     *
     * @param file file
     * @param mode r rw rws rwd
     * @return RandomAccessFile
     */
    public static RandomAccessFile openRandomAccessSafe(String file, String mode) {
        return openRandomAccessSafe(new File(file), mode);
    }

    /**
     * 打开文件随机读取
     *
     * @param file file
     * @param mode r rw rws rwd
     * @return RandomAccessFile
     */
    public static RandomAccessFile openRandomAccessSafe(File file, String mode) {
        try {
            return openRandomAccess(file, mode);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }


    // -------------------- path --------------------

    /**
     * 文件转路径
     *
     * @param files 文件
     * @return 路径
     */
    public static List<String> toPaths(List<File> files) {
        List<String> list = new ArrayList<>();
        for (File file : files) {
            list.add(file.getAbsolutePath());
        }
        return list;
    }

    /**
     * 文件转路径
     *
     * @param files 文件
     * @return 路径
     */
    public static String[] toPaths(File[] files) {
        int len = files.length;
        String[] ss = new String[len];
        for (int i = 0; i < len; i++) {
            ss[i] = files[i].getAbsolutePath();
        }
        return ss;
    }

    /**
     * 路径转文件
     *
     * @param files 路径
     * @return 文件
     */
    public static List<File> toFiles(List<String> files) {
        List<File> list = new ArrayList<>();
        for (String file : files) {
            list.add(new File(file));
        }
        return list;
    }

    /**
     * 路径转文件
     *
     * @param files 路径
     * @return 文件
     */
    public static File[] toFiles(String[] files) {
        int len = files.length;
        File[] ss = new File[len];
        for (int i = 0; i < len; i++) {
            ss[i] = new File(files[i]);
        }
        return ss;
    }

    /**
     * 获得用户根目录+输入路径
     *
     * @param path 路径
     * @return 路径
     */
    public static String getRootPath(String path) {
        if (Strings.isBlank(path)) {
            return System.getProperty(Systems.HOME_DIR);
        }
        path = path.trim();
        if (path.startsWith("\\") || path.startsWith("/")) {
            return replacePath(Systems.HOME_DIR + path);
        } else {
            return replacePath(Systems.HOME_DIR + File.separator + path);
        }
    }

    /**
     * 获得用户根目录+输入路径
     *
     * @param path 路径
     * @return 路径
     */
    public static String getRootPathFile(String path, String file) {
        path = getRootPath(path);
        file = file.trim();
        if (!(file.startsWith("\\") || file.startsWith("/")) && !(path.endsWith("\\") || path.endsWith("/"))) {
            path += "/" + file;
        }
        return replacePath(path);
    }

    /**
     * 将路径 替换为系统路径
     *
     * @param path 路径
     * @return 系统路径
     */
    public static String replacePath(String path) {
        // windows下
        if ("\\".equals(File.separator)) {
            path = path.replaceAll("/+", "\\\\");
            if ("\\".equals(path.substring(0, 1))) {
                path = path.substring(1);
            }
        }
        // linux下
        if ("/".equals(File.separator)) {
            path = path.replaceAll("\\\\+", "/");
        }
        return path;
    }

    /**
     * 判断是否符是合法的文件路径
     *
     * @param path 需要处理的文件路径
     */
    public static boolean isPath(String path) {
        return Matches.isWindowsPath(path) || Matches.isLinuxPath(path);
    }

    /**
     * 获取文件后缀名
     *
     * @param file 文件
     * @return 后缀
     */
    public static String getSuffix(File file) {
        return getSuffix(file.getName());
    }

    /**
     * 获取文件后缀名
     *
     * @param file 文件
     * @return 后缀
     */
    public static String getSuffix(String file) {
        return file.substring(file.lastIndexOf(".") + 1);
    }

    /**
     * 获取文件名称
     *
     * @param file 文件
     * @return 文件名称
     */
    public static String getFileName(String file) {
        String[] paths = file.replaceAll("\\\\+", "/").replaceAll("//+", "/").split("/");
        return paths[paths.length - 1];
    }

    /**
     * 获取文件名称
     *
     * @param file 文件
     * @return 文件名称
     */
    public static String getFileName(File file) {
        return file.getName();
    }

    /**
     * 获取文件上级路径
     *
     * @param file 文件
     * @return 上级路径
     */
    public static String getParentPath(String file) {
        String[] paths = file.replaceAll("\\\\+", "/").replaceAll("//+", "/").split("/");
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = paths.length - 1; i < len; i++) {
            sb.append(paths[i]).append("/");
        }
        return sb.toString();
    }

    /**
     * 获取文件上级路径
     *
     * @param file 文件
     * @return 上级路径
     */
    public static String getParentPath(File file) {
        return file.getParent();
    }

    /**
     * 获取所有文件上级路径
     *
     * @param file 文件
     * @return 上级路径
     */
    public static List<String> getParentPaths(String file) {
        file = file.replaceAll("\\\\+", "/").replaceAll("//+", "/");
        String[] paths = file.split("/");
        List<String> list = new ArrayList<>();
        StringBuilder sb;
        if (paths[0].contains(":")) {
            sb = new StringBuilder(paths[0] + "/");
            paths[0] = null;
        } else {
            sb = new StringBuilder("/");
        }
        for (int i = 0, len = paths.length - 1; i < len; i++) {
            String path = paths[i];
            if (Strings.isBlank(path)) {
                continue;
            }
            list.add(sb.append(path).toString());
            sb.append("/");
        }
        return list;
    }

    /**
     * 获取所有文件上级路径
     *
     * @param file 文件
     * @return 上级路径
     */
    public static List<String> getParentPaths(File file) {
        return getParentPaths(file.getAbsolutePath());
    }

    /**
     * 获取一个通用路径
     *
     * @param path path
     * @return path
     */
    public static String getPath(String path) {
        return path.replaceAll("\\\\+", "/").replaceAll("/+", "/");
    }

    /**
     * 统一化路径
     *
     * @param path 路径 /home/.././etc --> /etc
     * @return 统一化路径
     */
    public static String normalize(String path) {
        String[] paths = path.split(":");
        String separator, pre, p;
        if (paths.length == 1) {
            // linux
            separator = "/";
            pre = "/";
            p = paths[0];
        } else {
            // windows
            separator = "\\";
            pre = paths[0] + ":\\";
            p = paths[1];
        }
        StringBuilder sb = new StringBuilder(pre);
        String[] ps = getPath(p).split("/");
        for (int i = 0; i < ps.length; i++) {
            String s = ps[i];
            if (Strings.isBlank(s) || ".".equals(s)) {
                // ignore
            } else if ("..".equals(s)) {
                sb.delete(getParentPath(sb.toString()).length(), sb.length());
            } else {
                if (i == ps.length - 1) {
                    sb.append(s);
                } else {
                    sb.append(s).append(separator);
                }
            }
        }
        int len = sb.length();
        if (len > 1 && sb.charAt(len - 1) == '/') {
            return sb.deleteCharAt(len - 1).toString();
        } else {
            return sb.toString();
        }
    }

    // -------------------- sign --------------------

    /**
     * 文件 MD5 签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String md5(File file) {
        return sign(file, HashMessageDigest.MD5);
    }

    /**
     * 文件 MD5 签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String md5(String file) {
        return sign(new File(file), HashMessageDigest.MD5);
    }

    /**
     * 文件 SHA1 签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String sha1(File file) {
        return sign(file, HashMessageDigest.SHA1);
    }

    /**
     * 文件 SHA1 签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String sha1(String file) {
        return sign(new File(file), HashMessageDigest.SHA1);
    }

    /**
     * 文件 SHA224 签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String sha224(File file) {
        return sign(file, HashMessageDigest.SHA224);
    }

    /**
     * 文件 SHA224 签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String sha224(String file) {
        return sign(new File(file), HashMessageDigest.SHA224);
    }

    /**
     * 文件 SHA256 签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String sha256(File file) {
        return sign(file, HashMessageDigest.SHA256);
    }

    /**
     * 文件 SHA256 签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String sha256(String file) {
        return sign(new File(file), HashMessageDigest.SHA256);
    }

    /**
     * 文件 SHA384 签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String sha384(File file) {
        return sign(file, HashMessageDigest.SHA384);
    }

    /**
     * 文件 SHA384 签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String sha384(String file) {
        return sign(new File(file), HashMessageDigest.SHA384);
    }

    /**
     * 文件 SHA512 签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String sha512(File file) {
        return sign(file, HashMessageDigest.SHA512);
    }

    /**
     * 文件 SHA512 签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String sha512(String file) {
        return sign(new File(file), HashMessageDigest.SHA512);
    }

    /**
     * 文件散列签名
     *
     * @param file 文件
     * @param type 加密类型 MD5 SHA-1 SHA-224 SHA-256 SHA-384 SHA-512
     * @return 签名
     */
    public static String sign(String file, HashMessageDigest type) {
        return sign(new File(file), type);
    }

    /**
     * 文件散列签名
     *
     * @param file 文件
     * @param type 加密类型 MD5 SHA-1 SHA-224 SHA-256 SHA-384 SHA-512
     * @return 签名
     */
    public static String sign(File file, HashMessageDigest type) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        try (FileInputStream in = openInputStream(file)) {
            return Streams.sign(in, type);
        } catch (Exception e) {
            return null;
        }
    }

    // -------------------- list --------------------

    /**
     * 获取路径下的全部文件夹 递归
     *
     * @param path 需要处理的文件夹
     * @return 文件夹
     */
    public static List<File> listDirs(File path) {
        return listDirs(path, false);
    }

    /**
     * 获取路径下的全部文件夹 递归
     *
     * @param path 需要处理的文件夹
     * @return 文件夹
     */
    public static List<File> listDirs(String path) {
        return listDirs(new File(path), false);
    }

    /**
     * 获取路径下的全部文件夹
     *
     * @param path  文件夹
     * @param child 是否递归子文件夹
     * @return 文件夹
     */
    public static List<File> listDirs(String path, boolean child) {
        return listDirs(new File(path), child);
    }

    /**
     * 获取路径下的全部文件夹
     *
     * @param path  文件夹
     * @param child 是否递归子文件夹
     * @return 文件夹
     */
    public static List<File> listDirs(File path, boolean child) {
        List<File> list = new ArrayList<>();
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.add(file);
                    if (child) {
                        list.addAll(listDirs(file, true));
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取路径下的全部文件 递归
     *
     * @param path 需要处理的文件
     * @return 文件
     */
    public static List<File> listFiles(File path) {
        return listFiles(path, false, false);
    }

    /**
     * 获取路径下的全部文件 递归
     *
     * @param path 需要处理的文件
     * @return 文件
     */
    public static List<File> listFiles(String path) {
        return listFiles(new File(path), false, false);
    }

    /**
     * 获取路径下的全部文件
     *
     * @param path  文件夹
     * @param child 是否递归子文件夹
     * @return 文件
     */
    public static List<File> listFiles(String path, boolean child) {
        return listFiles(new File(path), child, false);
    }

    /**
     * 获取路径下的全部文件
     *
     * @param path  文件夹
     * @param child 是否递归子文件夹
     * @return 文件
     */
    public static List<File> listFiles(File path, boolean child) {
        return listFiles(path, child, false);
    }

    /**
     * 获取路径下的全部文件包括文件夹
     *
     * @param path  文件夹
     * @param child 是否递归子文件夹
     * @param dir   是否添加文件夹
     * @return 文件
     */
    public static List<File> listFiles(String path, boolean child, boolean dir) {
        return listFiles(new File(path), child, dir);
    }

    /**
     * 获取路径下的全部文件包括文件夹
     *
     * @param path  文件夹
     * @param child 是否递归子文件夹
     * @param dir   是否添加文件夹
     * @return 文件
     */
    public static List<File> listFiles(File path, boolean child, boolean dir) {
        List<File> list = new ArrayList<>();
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    list.add(file);
                } else if (file.isDirectory()) {
                    if (dir) {
                        list.add(file);
                    }
                    if (child) {
                        list.addAll(listFiles(file, true, dir));
                    }
                }
            }
        }
        return list;
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param suffix  后缀
     * @return 文件
     */
    public static List<File> listFilesSuffix(File dirPath, String suffix) {
        return listFilesSearch(dirPath, suffix, null, null, 1, false, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param suffix  后缀
     * @param child   是否递归子文件夹
     * @return 文件
     */
    public static List<File> listFilesSuffix(File dirPath, String suffix, boolean child) {
        return listFilesSearch(dirPath, suffix, null, null, 1, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param suffix  后缀
     * @param child   是否递归子文件夹
     * @param dir     是否包含文件夹
     * @return 文件
     */
    public static List<File> listFilesSuffix(File dirPath, String suffix, boolean child, boolean dir) {
        return listFilesSearch(dirPath, suffix, null, null, 1, child, dir);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param suffix  后缀
     * @return 文件
     */
    public static List<File> listFilesSuffix(String dirPath, String suffix) {
        return listFilesSearch(new File(dirPath), suffix, null, null, 1, false, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param suffix  后缀
     * @param child   是否递归子文件夹
     * @return 文件
     */
    public static List<File> listFilesSuffix(String dirPath, String suffix, boolean child) {
        return listFilesSearch(new File(dirPath), suffix, null, null, 1, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param suffix  后缀
     * @param child   是否递归子文件夹
     * @param dir     是否包含文件夹
     * @return 文件
     */
    public static List<File> listFilesSuffix(String dirPath, String suffix, boolean child, boolean dir) {
        return listFilesSearch(new File(dirPath), suffix, null, null, 1, child, dir);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @return 文件
     */
    public static List<File> listFilesMatch(File dirPath, String name) {
        return listFilesSearch(dirPath, name, null, null, 2, false, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @param child   是否递归子文件夹
     * @return 文件
     */
    public static List<File> listFilesMatch(File dirPath, String name, boolean child) {
        return listFilesSearch(dirPath, name, null, null, 2, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @param child   是否递归子文件夹
     * @param dir     是否包含文件夹
     * @return 文件
     */
    public static List<File> listFilesMatch(File dirPath, String name, boolean child, boolean dir) {
        return listFilesSearch(dirPath, name, null, null, 2, child, dir);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @return 文件
     */
    public static List<File> listFilesMatch(String dirPath, String name) {
        return listFilesSearch(new File(dirPath), name, null, null, 2, false, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @param child   是否递归子文件夹
     * @return 文件
     */
    public static List<File> listFilesMatch(String dirPath, String name, boolean child) {
        return listFilesSearch(new File(dirPath), name, null, null, 2, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @param child   是否递归子文件夹
     * @param dir     是否包含文件夹
     * @return 文件
     */
    public static List<File> listFilesMatch(String dirPath, String name, boolean child, boolean dir) {
        return listFilesSearch(new File(dirPath), name, null, null, 2, child, dir);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @return 文件
     */
    public static List<File> listFilesPattern(File dirPath, Pattern pattern) {
        return listFilesSearch(dirPath, null, pattern, null, 3, false, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @param child   是否递归子文件夹
     * @return 文件
     */
    public static List<File> listFilesPattern(File dirPath, Pattern pattern, boolean child) {
        return listFilesSearch(dirPath, null, pattern, null, 3, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @param child   是否递归子文件夹
     * @param dir     是否包含文件夹
     * @return 文件
     */
    public static List<File> listFilesPattern(File dirPath, Pattern pattern, boolean child, boolean dir) {
        return listFilesSearch(dirPath, null, pattern, null, 3, child, dir);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @return 文件
     */
    public static List<File> listFilesPattern(String dirPath, Pattern pattern) {
        return listFilesSearch(new File(dirPath), null, pattern, null, 3, false, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @param child   是否递归子文件夹
     * @return 文件
     */
    public static List<File> listFilesPattern(String dirPath, Pattern pattern, boolean child) {
        return listFilesSearch(new File(dirPath), null, pattern, null, 3, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @param child   是否递归子文件夹
     * @param dir     是否包含文件夹
     * @return 文件
     */
    public static List<File> listFilesPattern(String dirPath, Pattern pattern, boolean child, boolean dir) {
        return listFilesSearch(new File(dirPath), null, pattern, null, 3, child, dir);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @return 文件
     */
    public static List<File> listFilesFilter(File dirPath, FilenameFilter filter) {
        return listFilesSearch(dirPath, null, null, filter, 4, false, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @param child   是否递归子文件夹
     * @return 文件
     */
    public static List<File> listFilesFilter(File dirPath, FilenameFilter filter, boolean child) {
        return listFilesSearch(dirPath, null, null, filter, 4, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @param child   是否递归子文件夹
     * @param dir     是否包含文件夹
     * @return 文件
     */
    public static List<File> listFilesFilter(File dirPath, FilenameFilter filter, boolean child, boolean dir) {
        return listFilesSearch(dirPath, null, null, filter, 4, child, dir);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @return 文件
     */
    public static List<File> listFilesFilter(String dirPath, FilenameFilter filter) {
        return listFilesSearch(new File(dirPath), null, null, filter, 4, false, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @param child   是否递归子文件夹
     * @return 文件
     */
    public static List<File> listFilesFilter(String dirPath, FilenameFilter filter, boolean child) {
        return listFilesSearch(new File(dirPath), null, null, filter, 4, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @param child   是否递归子文件夹
     * @param dir     是否包含文件夹
     * @return 文件
     */
    public static List<File> listFilesFilter(String dirPath, FilenameFilter filter, boolean child, boolean dir) {
        return listFilesSearch(new File(dirPath), null, null, filter, 4, child, dir);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param search  搜索
     * @param pattern 正则
     * @param filter  过滤器
     * @param type    类型 1后缀 2匹配 3正则 4过滤器
     * @param child   是否递归子文件夹
     * @param dir     是否包含文件夹
     * @return 文件
     */
    private static List<File> listFilesSearch(File dirPath, String search, Pattern pattern, FilenameFilter filter, int type, boolean child, boolean dir) {
        List<File> list = new ArrayList<>();
        File[] files = dirPath.listFiles();
        if (files != null) {
            for (File file : files) {
                boolean isDir = file.isDirectory();
                if (!isDir || dir) {
                    String fn = file.getName();
                    if (type == 1 && fn.toLowerCase().endsWith(search.toLowerCase())) {
                        list.add(file);
                    } else if (type == 2 && fn.toLowerCase().contains(search.toLowerCase())) {
                        list.add(file);
                    } else if (type == 3 && Matches.test(fn, pattern)) {
                        list.add(file);
                    } else if (type == 4 && filter.accept(file, fn)) {
                        list.add(file);
                    }
                }
                if (isDir && child) {
                    list.addAll(listFilesSearch(file, search, pattern, filter, type, true, dir));
                }
            }
        }
        return list;
    }

}
