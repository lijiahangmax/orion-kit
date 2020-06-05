package com.orion.utils.file;

import com.orion.id.UUIds;
import com.orion.utils.Systems;
import com.orion.utils.*;
import com.orion.utils.collect.Lists;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.orion.utils.Streams.closeQuietly;

/**
 * 文件工具类
 *
 * @author:Li
 * @time: 2019/10/9 12:01
 * @version: 1.0.0
 */
@SuppressWarnings("ALL")
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

    private static final String[] DATA_UNIT = {"K", "KB", "M", "MB", "MBPS", "G", "GB", "T", "TB", "B"};

    private static final long[] DATA_UNIT_EFFECT = {1000, 1024, 1000 * 1000, 1024 * 1024, 1024 * 128, 1000 * 1000 * 1000, 1024 * 1024 * 1024, 1000 * 1000 * 1000 * 1000, 1024 * 1024 * 1024 * 1024, 1};

    // -------------------- file --------------------

    /**
     * 通过文件头推算文件类型 不准确
     *
     * @param file 文件
     * @return 类型
     */
    public static String getFileType(File file) {
        String type = null;
        byte[] b = new byte[20];
        FileInputStream in = null;
        try {
            in = openInputStream(file);
            if (in.read(b) != -1) {
                type = Streams.getFileType(b);
            }
        } catch (IOException e) {
            // ignore
        } finally {
            closeQuietly(in);
        }
        return type;
    }

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
        if (size / (DATA_UNIT_EFFECT[1] * DATA_UNIT_EFFECT[1] * DATA_UNIT_EFFECT[1]) > 0) {
            s = String.valueOf(size / (DATA_UNIT_EFFECT[1] * DATA_UNIT_EFFECT[1] * DATA_UNIT_EFFECT[1])) + " GB";
        } else if (size / (DATA_UNIT_EFFECT[1] * DATA_UNIT_EFFECT[1]) > 0) {
            s = String.valueOf(size / (DATA_UNIT_EFFECT[1] * DATA_UNIT_EFFECT[1])) + " MB";
        } else if (size / DATA_UNIT_EFFECT[1] > 0) {
            s = String.valueOf(size / DATA_UNIT_EFFECT[1]) + " KB";
        } else {
            s = String.valueOf(size) + " bytes";
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
        for (int i = 0; i < DATA_UNIT.length; i++) {
            if (size.endsWith(DATA_UNIT[i])) {
                effectIndex = i;
                unitLen = DATA_UNIT[i].length();
                break;
            }
        }
        if (effectIndex == -1) {
            return 0L;
        }
        Double d = Double.valueOf(size.substring(0, size.length() - unitLen).trim());
        return (long) (d * DATA_UNIT_EFFECT[effectIndex]);
    }

    /**
     * 将resource文件保存到本地
     *
     * @param source   文件
     * @param childDir 子文件夹
     * @param charset  编码格式
     * @param force    如果存在是否覆盖
     * @return 文件路径
     */
    public static String resourceToFile(String source, String childDir, String charset, boolean force) {
        InputStream in = null;
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
            in = Files1.class.getClassLoader().getResourceAsStream(source);
            OutputStream out = null;
            try {
                out = openOutputStream(file);
                byte[] bs = new byte[BUFFER_SIZE];
                int read;
                while (-1 != (read = in.read(bs))) {
                    out.write(bs, 0, read);
                }
                out.flush();
            } catch (Exception e) {
                throw Exceptions.ioRuntime(e);
            } finally {
                closeQuietly(in);
                closeQuietly(out);
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
            closeQuietly(writer);
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
        OutputStream out = null;
        try {
            out = openOutputStream(file);
            byte[] bs = new byte[BUFFER_SIZE];
            int read;
            while (-1 != (read = in.read(bs))) {
                out.write(bs, 0, read);
            }
            out.flush();
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            closeQuietly(out);
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
                closeQuietly(in);
                in = null;
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            closeQuietly(in);
            closeQuietly(out);
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
        InputStream in = null;
        OutputStream out = null;
        try {
            in = openInputStream(source);
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
            closeQuietly(in);
            closeQuietly(out);
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
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = openInputStream(source);
            out = openOutputStream(target);
            FileChannel inc = in.getChannel();
            FileChannel outc = out.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (inc.read(buffer) != -1) {
                buffer.flip();
                outc.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            closeQuietly(in);
            closeQuietly(out);
        }
    }

    /**
     * 创建文件
     *
     * @param file 文件
     */
    public static void touch(String file) {
        touch(new File(file), null);
    }

    /**
     * 创建文件
     *
     * @param file 文件
     */
    public static void touch(File file) {
        touch(file, null);
    }

    /**
     * 创建文件
     *
     * @param file    文件
     * @param charset 编码格式
     */
    public static void touch(String file, String charset) {
        touch(new File(file), charset);
    }

    /**
     * 创建文件
     *
     * @param file    文件
     * @param charset 编码格式
     */
    public static void touch(File file, String charset) {
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
                        closeQuietly(osw);
                    }
                }
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            }
        }
    }

    /**
     * 获取一个IO临时文件
     *
     * @return io临时文件路径
     */
    public static String getIOTempFilePath() {
        return Systems.HOME_DIR + IO_TEMP_DIR + UUIds.random32() + ".temp";
    }

    /**
     * 创建一个IO临时文件
     *
     * @return io临时文件路径
     */
    public static File touchIOTempFile() {
        String path = getIOTempFilePath();
        touch(path);
        return new File(path);
    }

    /**
     * 创建一个IO临时文件
     *
     * @param exitDelete 是否退出删除
     * @return io临时文件路径
     */
    public static File touchIOTempFile(boolean exitDelete) {
        String path = getIOTempFilePath();
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
        List<File> files = listFilesAndDirs(file);
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteDir(f);
                } else {
                    deleteFile(f);
                }
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
        File f = new File(file);
        cleanFile(f);
        f.delete();
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
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("");
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 清空文件
     *
     * @param file 文件
     */
    public static void cleanFile(File file) {
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
            Streams.closeQuietly(r);
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
        } finally {
            Streams.closeQuietly(r);
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
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(file, "r");
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
        } finally {
            Streams.closeQuietly(r);
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
            closeQuietly(rf);
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

    public static RandomAccessFile openRandomAccess(String file, String mode) throws IOException {
        return openRandomAccess(new File(file), mode);
    }

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

    // -------------------- charset --------------------

    /**
     * 通过文件头获取编码格式
     *
     * @param file 文件
     * @return GBK UTF-16LE UTF-16BE UTF-8 默认GBK
     */
    public static String getCharset(String file) {
        return getCharset(new File(file));
    }

    /**
     * 通过文件头获取编码格式
     *
     * @param file 文件
     * @return GBK UTF-16LE UTF-16BE UTF-8 默认GBK
     */
    public static String getCharset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        BufferedInputStream bis = null;
        try {
            boolean checked = false;
            bis = new BufferedInputStream(openInputStream(file));
            bis.mark(0);
            // bis.mark(100);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                bis.close();
                return charset;
            } else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0) {
                        break;
                    }
                    if (0x80 <= read && read <= 0xBF) {
                        break;
                    }
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (!(0x80 <= read && read <= 0xBF)) {
                            break;
                        }
                    } else if (0xE0 <= read) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            closeQuietly(bis);
        }
        return charset;
    }

    /**
     * 转换文件编码格式
     *
     * @param file        文件
     * @param fromCharset 原编码
     * @param toCharset   新编码
     */
    public static void convertCharset(String file, String fromCharset, String toCharset) {
        convertCharset(new File(file), fromCharset, toCharset);
    }

    /**
     * 转换文件编码格式
     *
     * @param file        文件
     * @param fromCharset 原编码
     * @param toCharset   新编码
     */
    public static void convertCharset(File file, String fromCharset, String toCharset) {
        // read
        InputStreamReader reader = null;
        String s = "";
        try {
            reader = new InputStreamReader(openInputStream(file), fromCharset);
            char[] cs = new char[10];
            char[] t = new char[10];
            int index = 0;
            int read;
            while (-1 != (read = reader.read(t))) {
                cs = Arrays1.arraycopy(t, 0, cs, index, read);
                index += read;
            }
            s = new String(cs, 0, index);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            closeQuietly(reader);
        }

        // write
        OutputStreamWriter write = null;
        try {
            write = new OutputStreamWriter(openOutputStream(file), toCharset);
            write.write(s);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            closeQuietly(write);
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
     * @return
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
                continue;
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

    // -------------------- list --------------------

    /**
     * 获取路径下的全部文件夹 递归
     *
     * @param path 需要处理的文件夹
     * @return 文件夹
     */
    public static List<File> listDirs(File path) {
        return listDirs(path, true);
    }

    /**
     * 获取路径下的全部文件夹 递归
     *
     * @param path 需要处理的文件夹
     * @return 文件夹
     */
    public static List<File> listDirs(String path) {
        return listDirs(new File(path), true);
    }

    /**
     * 获取路径下的全部文件夹
     *
     * @param path  文件夹
     * @param child 是否递归
     * @return 文件夹
     */
    public static List<File> listDirs(String path, boolean child) {
        return listDirs(new File(path), child);
    }

    /**
     * 获取路径下的全部文件夹
     *
     * @param path  文件夹
     * @param child 是否递归
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
        return listFiles(path, true, false);
    }

    /**
     * 获取路径下的全部文件 递归
     *
     * @param path 需要处理的文件
     * @return 文件
     */
    public static List<File> listFiles(String path) {
        return listFiles(new File(path), true, false);
    }

    /**
     * 获取路径下的全部文件
     *
     * @param path  文件夹
     * @param child 是否递归
     * @return 文件
     */
    public static List<File> listFiles(String path, boolean child) {
        return listFiles(new File(path), child, false);
    }

    /**
     * 获取路径下的全部文件
     *
     * @param path  文件夹
     * @param child 是否递归
     * @return 文件
     */
    public static List<File> listFiles(File path, boolean child) {
        return listFiles(path, child, false);
    }

    /**
     * 获取路径下的全部文件包括文件夹 递归
     *
     * @param path 文件夹
     * @return 文件和文件夹
     */
    public static List<File> listFilesAndDirs(File path) {
        return listFiles(path, true, true);
    }

    /**
     * 获取路径下的全部文件包括文件夹 递归
     *
     * @param path 文件夹
     * @return 文件和文件夹
     */
    public static List<File> listFilesAndDirs(String path) {
        return listFiles(new File(path), true, true);
    }

    /**
     * 获取路径下的全部文件包括文件夹 递归
     *
     * @param path  文件夹
     * @param child 是否递归
     * @return 文件和文件夹
     */
    public static List<File> listFilesAndDirs(String path, boolean child) {
        return listFiles(new File(path), child, true);
    }

    /**
     * 获取路径下的全部文件包括文件夹
     *
     * @param path  文件夹
     * @param child 是否递归
     * @return 文件和文件夹
     */
    public static List<File> listFilesAndDirs(File path, boolean child) {
        return listFiles(path, child, true);
    }

    /**
     * 获取路径下的全部文件包括文件夹
     *
     * @param path  文件夹
     * @param child 是否递归
     * @param dir   是否添加文件夹
     * @return 文件
     */
    private static List<File> listFiles(File path, boolean child, boolean dir) {
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
    private static List<File> listFilesSuffix(File dirPath, String suffix) {
        return listFilesSearch(dirPath, suffix, null, null, 1, true, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param suffix  后缀
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesSuffix(File dirPath, String suffix, boolean child) {
        return listFilesSearch(dirPath, suffix, null, null, 1, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param suffix  后缀
     * @return 文件
     */
    private static List<File> listFilesAndDirSuffix(File dirPath, String suffix) {
        return listFilesSearch(dirPath, suffix, null, null, 1, true, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param suffix  后缀
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesAndDirSuffix(File dirPath, String suffix, boolean child) {
        return listFilesSearch(dirPath, suffix, null, null, 1, child, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param suffix  后缀
     * @return 文件
     */
    private static List<File> listFilesSuffix(String dirPath, String suffix) {
        return listFilesSearch(new File(dirPath), suffix, null, null, 1, true, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param suffix  后缀
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesSuffix(String dirPath, String suffix, boolean child) {
        return listFilesSearch(new File(dirPath), suffix, null, null, 1, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param suffix  后缀
     * @return 文件
     */
    private static List<File> listFilesAndDirSuffix(String dirPath, String suffix) {
        return listFilesSearch(new File(dirPath), suffix, null, null, 1, true, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param suffix  后缀
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesAndDirSuffix(String dirPath, String suffix, boolean child) {
        return listFilesSearch(new File(dirPath), suffix, null, null, 1, child, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @return 文件
     */
    private static List<File> listFilesMatch(File dirPath, String name) {
        return listFilesSearch(dirPath, name, null, null, 2, true, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesMatch(File dirPath, String name, boolean child) {
        return listFilesSearch(dirPath, name, null, null, 2, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @return 文件
     */
    private static List<File> listFilesAndDirMatch(File dirPath, String name) {
        return listFilesSearch(dirPath, name, null, null, 2, true, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesAndDirMatch(File dirPath, String name, boolean child) {
        return listFilesSearch(dirPath, name, null, null, 2, child, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @return 文件
     */
    private static List<File> listFilesMatch(String dirPath, String name) {
        return listFilesSearch(new File(dirPath), name, null, null, 2, true, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesMatch(String dirPath, String name, boolean child) {
        return listFilesSearch(new File(dirPath), name, null, null, 2, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @return 文件
     */
    private static List<File> listFilesAndDirMatch(String dirPath, String name) {
        return listFilesSearch(new File(dirPath), name, null, null, 2, true, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesAndDirMatch(String dirPath, String name, boolean child) {
        return listFilesSearch(new File(dirPath), name, null, null, 2, child, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @return 文件
     */
    private static List<File> listFilesPattern(File dirPath, Pattern pattern) {
        return listFilesSearch(dirPath, null, pattern, null, 3, true, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesPattern(File dirPath, Pattern pattern, boolean child) {
        return listFilesSearch(dirPath, null, pattern, null, 3, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @return 文件
     */
    private static List<File> listFilesAndDirPattern(File dirPath, Pattern pattern) {
        return listFilesSearch(dirPath, null, pattern, null, 3, true, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesAndDirPattern(File dirPath, Pattern pattern, boolean child) {
        return listFilesSearch(dirPath, null, pattern, null, 3, child, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @return 文件
     */
    private static List<File> listFilesPattern(String dirPath, Pattern pattern) {
        return listFilesSearch(new File(dirPath), null, pattern, null, 3, true, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesPattern(String dirPath, Pattern pattern, boolean child) {
        return listFilesSearch(new File(dirPath), null, pattern, null, 3, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @return 文件
     */
    private static List<File> listFilesAndDirPattern(String dirPath, Pattern pattern) {
        return listFilesSearch(new File(dirPath), null, pattern, null, 3, true, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesAndDirPattern(String dirPath, Pattern pattern, boolean child) {
        return listFilesSearch(new File(dirPath), null, pattern, null, 3, child, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @return 文件
     */
    private static List<File> listFilesFilter(File dirPath, FilenameFilter filter) {
        return listFilesSearch(dirPath, null, null, filter, 4, true, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesFilter(File dirPath, FilenameFilter filter, boolean child) {
        return listFilesSearch(dirPath, null, null, filter, 4, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @return 文件
     */
    private static List<File> listFilesAndDirFilter(File dirPath, FilenameFilter filter) {
        return listFilesSearch(dirPath, null, null, filter, 4, true, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesAndDirFilter(File dirPath, FilenameFilter filter, boolean child) {
        return listFilesSearch(dirPath, null, null, filter, 4, child, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @return 文件
     */
    private static List<File> listFilesFilter(String dirPath, FilenameFilter filter) {
        return listFilesSearch(new File(dirPath), null, null, filter, 4, true, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesFilter(String dirPath, FilenameFilter filter, boolean child) {
        return listFilesSearch(new File(dirPath), null, null, filter, 4, child, false);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @return 文件
     */
    private static List<File> listFilesAndDirFilter(String dirPath, FilenameFilter filter) {
        return listFilesSearch(new File(dirPath), null, null, filter, 4, true, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @param child   是否递归
     * @return 文件
     */
    private static List<File> listFilesAndDirFilter(String dirPath, FilenameFilter filter, boolean child) {
        return listFilesSearch(new File(dirPath), null, null, filter, 4, child, true);
    }

    /**
     * 搜索文件
     *
     * @param dirPath 文件夹
     * @param search  搜索
     * @param pattern 正则
     * @param filter  过滤器
     * @param type    类型 1后缀 2匹配 3正则 4过滤器
     * @param child   是否递归
     * @param dir     是否添加文件夹
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

    // -------------------- write --------------------

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
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(file, "rw");
            r.seek(r.length());
            r.write(bs, off, len);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            Streams.closeQuietly(r);
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
            throw Exceptions.unEnding(e);
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
            throw Exceptions.unEnding(e);
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
                File endFile = touchIOTempFile(true);
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
            closeQuietly(in);
            closeQuietly(r);
        }
    }

    /**
     * 拼接一行
     *
     * @param file 文件
     * @param str  string
     */
    public static void appendLine(File file, String str) {
        appendLines(file, Lists.of(str), null);
    }

    /**
     * 拼接一行
     *
     * @param file 文件
     * @param str  string
     */
    public static void appendLine(String file, String str) {
        appendLines(new File(file), Lists.of(str), null);
    }

    /**
     * 拼接一行
     *
     * @param file    文件
     * @param str     string
     * @param charset 编码格式
     */
    public static void appendLine(File file, String str, String charset) {
        appendLines(file, Lists.of(str), charset);
    }

    /**
     * 拼接一行
     *
     * @param file    文件
     * @param str     string
     * @param charset 编码格式
     */
    public static void appendLine(String file, String str, String charset) {
        appendLines(new File(file), Lists.of(str), charset);
    }

    /**
     * 拼接多行
     *
     * @param file 文件
     * @param list strings
     */
    public static void appendLines(File file, List<String> list) {
        appendLines(file, list, null);
    }

    /**
     * 拼接多行
     *
     * @param file 文件
     * @param list strings
     */
    public static void appendLines(String file, List<String> list) {
        appendLines(new File(file), list, null);
    }

    /**
     * 拼接多行
     *
     * @param file    文件
     * @param list    strings
     * @param charset 编码格式
     */
    public static void appendLines(String file, List<String> list, String charset) {
        writeLines(new File(file), list, charset, true);
    }

    /**
     * 拼接多行
     *
     * @param file    文件
     * @param list    strings
     * @param charset 编码格式
     */
    public static void appendLines(File file, List<String> list, String charset) {
        writeLines(file, list, charset, true);
    }

    /**
     * 拼接行到偏移处 不拼接 \n
     *
     * @param file   文件
     * @param offset 拼接偏移量
     * @param line   行
     */
    public static void appendLines(String file, long offset, String line) {
        appendLines(new File(file), offset, Lists.of(line), null);
    }

    /**
     * 拼接行到偏移处 不拼接 \n
     *
     * @param file   文件
     * @param offset 拼接偏移量
     * @param line   行
     */
    public static void appendLines(File file, long offset, String line) {
        appendLines(file, offset, Lists.of(line), null);
    }

    /**
     * 拼接行到偏移处 不拼接 \n
     *
     * @param file    文件
     * @param offset  拼接偏移量
     * @param line    行
     * @param charset 编码格式
     */
    public static void appendLines(String file, long offset, String line, String charset) {
        appendLines(new File(file), offset, Lists.of(line), charset);
    }

    /**
     * 拼接行到偏移处 不拼接 \n
     *
     * @param file    文件
     * @param offset  拼接偏移量
     * @param line    行
     * @param charset 编码格式
     */
    public static void appendLines(File file, long offset, String line, String charset) {
        appendLines(file, offset, Lists.of(line), charset);
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
                } else {
                    endFile = touchIOTempFile(true);
                    writeToFile(file, offset, endFile);
                    r.seek(offset);
                    useBuffer = false;
                }
            }
            for (int i = 0, size = lines.size(); i < size; i++) {
                String line = lines.get(i);
                if (i != 0) {
                    line = "\n" + line;
                }
                if (charset == null) {
                    r.write(line.getBytes());
                } else {
                    r.write(line.getBytes(charset));
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
            closeQuietly(in);
            closeQuietly(r);
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
        FileOutputStream out = null;
        try {
            out = openOutputStream(file);
            out.write(bs, off, len);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            closeQuietly(out);
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
            throw Exceptions.unEnding(e);
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
            throw Exceptions.unEnding(e);
        }
    }

    /**
     * 写入一行
     *
     * @param file 文件
     * @param str  string
     */
    public static void writeLine(File file, String str) {
        writeLines(file, Lists.of(str), null);
    }

    /**
     * 写入一行
     *
     * @param file 文件
     * @param str  string
     */
    public static void writeLine(String file, String str) {
        writeLines(new File(file), Lists.of(str), null);
    }

    /**
     * 写入一行
     *
     * @param file    文件
     * @param str     string
     * @param charset 编码格式
     */
    public static void writeLine(File file, String str, String charset) {
        writeLines(file, Lists.of(str), charset);
    }

    /**
     * 写入一行
     *
     * @param file    文件
     * @param str     string
     * @param charset 编码格式
     */
    public static void writeLine(String file, String str, String charset) {
        writeLines(new File(file), Lists.of(str), charset);
    }

    /**
     * 写入多行
     *
     * @param file 文件
     * @param list strings
     */
    public static void writeLines(File file, List<String> list) {
        writeLines(file, list, null);
    }

    /**
     * 写入多行
     *
     * @param file    文件
     * @param list    strings
     * @param charset 编码格式
     */
    public static void writeLines(File file, List<String> list, String charset) {
        writeLines(file, list, charset, false);
    }

    /**
     * 写入多行
     *
     * @param file    文件
     * @param list    strings
     * @param charset 编码格式
     */
    public static void writeLines(String file, List<String> list, String charset) {
        writeLines(new File(file), list, charset, false);
    }

    /**
     * 写入多行
     *
     * @param file    文件
     * @param list    strings
     * @param charset 编码格式
     * @param append  true 拼接
     */
    private static void writeLines(File file, List<String> list, String charset, boolean append) {
        String lineSeparator = getFileEndLineSeparator(file);
        boolean before = false;
        if (lineSeparator == null) {
            lineSeparator = "\n";
            if (append) {
                before = true;
            }
        }
        RandomAccessFile rw = null;
        try {
            rw = new RandomAccessFile(file, "rw");
            if (append) {
                rw.seek(rw.length());
            }
            if (before) {
                rw.write(lineSeparator.getBytes());
            }
            if (charset == null) {
                for (String str : list) {
                    str += lineSeparator;
                    rw.write(str.getBytes());
                }
            } else {
                for (String str : list) {
                    str += lineSeparator;
                    rw.write(str.getBytes(charset));
                }
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            closeQuietly(rw);
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
            FileInputStream in = null;
            try {
                in = openInputStream(file);
                byte[] skips = new byte[skipBytes];
                int read = in.read(skips);
                skips = Arrays1.arraycopy(bs, off, skips, read, len);
                write(file, skips, 0, len + read);
            } catch (Exception e) {
                throw Exceptions.ioRuntime(e);
            } finally {
                closeQuietly(in);
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
    public static void write(String file, int rangeStart, int rangeEnd, byte[] bytes) {
        write(new File(file), rangeStart, rangeEnd, bytes, 0, bytes.length);
    }

    /**
     * 写入到文件
     *
     * @param file       文件
     * @param rangeStart 写入指定区域开始
     * @param rangeEnd   写入指定区域结束
     * @param bytes      数据
     */
    public static void write(File file, int rangeStart, int rangeEnd, byte[] bytes) {
        write(file, rangeStart, rangeEnd, bytes, 0, bytes.length);
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
    public static void write(String file, int rangeStart, int rangeEnd, byte[] bytes, int off, int len) {
        write(new File(file), rangeStart, rangeEnd, bytes, off, len);
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
    public static void write(File file, int rangeStart, int rangeEnd, byte[] bytes, int off, int len) {
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
        try {
            if (startBuffer && endBuffer) {
                byte[] s = Streams.read(new RandomAccessFile(file, "r"), 0, rangeStart);
                byte[] e = Streams.read(new RandomAccessFile(file, "r"), rangeEnd, fileLen);
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
                    byte[] s = Streams.read(new RandomAccessFile(file, "r"), 0, rangeStart);
                    byte[] n = new byte[s.length + len];
                    int i = 0;
                    File endFile = touchIOTempFile(true);
                    writeToFile(file, Long.valueOf(rangeEnd), endFile);
                    System.arraycopy(s, 0, n, 0, i += s.length);
                    System.arraycopy(bytes, off, n, i, len);
                    write(file, n);
                    mergeFile(endFile, file);
                } else if (endBuffer) {
                    byte[] e = Streams.read(new RandomAccessFile(file, "r"), rangeEnd, fileLen);
                    byte[] n = new byte[e.length + len];
                    System.arraycopy(bytes, off, n, 0, len);
                    System.arraycopy(e, 0, n, len, e.length);
                    RandomAccessFile r = new RandomAccessFile(file, "rw");
                    r.seek(rangeStart);
                    r.write(n);
                    closeQuietly(r);
                } else {
                    File endFile = touchIOTempFile(true);
                    writeToFile(file, Long.valueOf(rangeEnd), endFile);
                    RandomAccessFile r = new RandomAccessFile(file, "rw");
                    r.seek(rangeStart);
                    r.write(bytes, off, len);
                    int read;
                    byte[] bs = new byte[BUFFER_SIZE];
                    FileInputStream in = openInputStream(endFile);
                    while (-1 != (read = in.read(bs))) {
                        r.write(bs, 0, read);
                    }
                    closeQuietly(r);
                    closeQuietly(in);
                }
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 写入文件到临时文件
     *
     * @param file 文件
     * @param off  文件偏移量
     * @param temp 临时文件
     */
    public static void writeToFile(File file, long off, File temp) {
        writeToFile(file, off, ((int) (file.length() - off)), temp);
    }

    /**
     * 写入文件到临时文件
     *
     * @param file 文件
     * @param len  写入长度
     * @param temp 临时文件
     */
    public static void writeToFile(File file, int len, File temp) {
        writeToFile(file, 0, len, temp);
    }

    /**
     * 写入文件到临时文件
     *
     * @param file 文件
     * @param off  文件偏移量
     * @param len  写入长度
     * @param temp 临时文件
     */
    public static void writeToFile(File file, long off, int len, File temp) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            if (off <= 0 && len >= file.length()) {
                copy(file, temp);
                return;
            }
            in = openInputStream(file);
            if (in.skip(off) < off) {
                // all skip
                return;
            }
            out = openOutputStream(temp);
            int read;
            byte[] buf = new byte[BUFFER_SIZE];
            while (-1 != (read = in.read(buf))) {
                out.write(buf, 0, read);
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            closeQuietly(in);
            closeQuietly(out);
        }
    }

    // -------------------- read --------------------

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
        try {
            return openInputStream(file).read(bytes);
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
        FileInputStream in = null;
        try {
            in = openInputStream(file);
            if (skipByte > 0) {
                in.skip(skipByte);
            }
            return in.read(bytes);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            closeQuietly(in);
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
            closeQuietly(reader);
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
            closeQuietly(reader);
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
            closeQuietly(reader);
        }
    }

    /**
     * 读取文件的所有行
     *
     * @param file 文件
     * @return 行
     */
    public static List<String> readLines(String file) {
        return readLines(new File(file), 0, null);
    }

    /**
     * 读取文件的所有行
     *
     * @param file 文件
     * @return 行
     */
    public static List<String> readLines(File file) {
        return readLines(file, 0, null);
    }

    /**
     * 读取文件的所有行
     *
     * @param file    文件
     * @param charset 编码格式
     * @return 行
     */
    public static List<String> readLines(String file, String charset) {
        return readLines(new File(file), 0, charset);
    }

    /**
     * 读取文件的所有行
     *
     * @param file    文件
     * @param charset 编码格式
     * @return 行
     */
    public static List<String> readLines(File file, String charset) {
        return readLines(file, 0, charset);
    }

    /**
     * 读取文件到指定行
     *
     * @param file    文件
     * @param endLine 行
     * @return 行
     */
    public static List<String> readLines(String file, int endLine) {
        return readLines(new File(file), endLine, null);
    }

    /**
     * 读取文件到指定行
     *
     * @param file    文件
     * @param endLine 行
     * @return 行
     */
    public static List<String> readLines(File file, int endLine) {
        return readLines(file, endLine, null);
    }

    /**
     * 读取文件到指定行
     *
     * @param file    文件
     * @param endLine 行
     * @param charset 编码格式
     * @return 行
     */
    public static List<String> readLines(String file, int endLine, String charset) {
        return readLines(new File(file), endLine, charset);
    }

    /**
     * 读取文件到指定行
     *
     * @param file    文件
     * @param endLine 行
     * @param charset 编码格式
     * @return 行
     */
    public static List<String> readLines(File file, int endLine, String charset) {
        BufferedReader reader = null;
        try {
            if (charset == null) {
                reader = new BufferedReader(new InputStreamReader(openInputStream(file)));
            } else {
                reader = new BufferedReader(new InputStreamReader(openInputStream(file), charset));
            }
            List<String> list = new ArrayList<>();
            String line;
            if (endLine <= 0) {
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
            } else {
                int i = 0;
                while ((line = reader.readLine()) != null && ++i <= endLine) {
                    list.add(line);
                }
            }
            return list;
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            closeQuietly(reader);
        }
    }

    // -------------------- sign --------------------

    /**
     * 文件MD5签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String md5(File file) {
        return sign(file, "MD5");
    }

    /**
     * 文件MD5签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String md5(String file) {
        return sign(new File(file), "MD5");
    }

    /**
     * 文件SHA1签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String sha1(File file) {
        return sign(file, "SHA-1");
    }

    /**
     * 文件SHA1签名
     *
     * @param file 文件
     * @return 签名
     */
    public static String sha1(String file) {
        return sign(new File(file), "SHA-1");
    }

    /**
     * 散列签名
     *
     * @param file 文件
     * @param type 加密类型
     * @return 签名
     */
    public static String sign(String file, String type) {
        return sign(new File(file), type);
    }

    /**
     * 散列签名
     *
     * @param file 文件
     * @param type 加密类型 MD5 SHA-1 SHA-256 SHA-384 SHA-512
     * @return 签名
     */
    public static String sign(File file, String type) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        try {
            return Streams.sign(openInputStream(file), type);
        } catch (Exception e) {
            return null;
        }
    }

}
