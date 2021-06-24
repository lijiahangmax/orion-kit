package com.orion.utils.io;

import com.orion.constant.Const;
import com.orion.constant.Letters;
import com.orion.function.FileFilter;
import com.orion.id.UUIds;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Systems;
import com.orion.utils.crypto.enums.HashMessageDigest;
import com.orion.utils.regexp.Matches;

import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
@SuppressWarnings("ALL")
public class Files1 {

    private Files1() {
    }

    /**
     * IO 临时文件夹
     */
    public static final String IO_TEMP_DIR = File.separator + Const.ORION_DISPLAY + File.separator + ".temp" + File.separator;

    private static final String[] SIZE_UNIT = {"K", "KB", "M", "MB", "MBPS", "G", "GB", "T", "TB", "B"};

    private static final long[] SIZE_UNIT_EFFECT = {1000, 1024, 1000 * 1000, 1024 * 1024, 1024 * 128, 1000 * 1000 * 1000, 1024 * 1024 * 1024, 1000 * 1000 * 1000 * 1000L, 1024 * 1024 * 1024 * 1024L, 1};

    // -------------------- attr --------------------

    public static FileAttribute getAttribute(String file) {
        return getAttribute(Paths.get(file));
    }

    public static FileAttribute getAttribute(File file) {
        return getAttribute(Paths.get(file.getAbsolutePath()));
    }

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
            return null;
        }
    }

    public static boolean isExecutable(String file) {
        return new File(file).canExecute();
    }

    public static boolean isExecutable(File file) {
        return file.canExecute();
    }

    /**
     * 文件是否可执行
     *
     * @param file 文件
     * @return 可执行true
     */
    public static boolean isExecutable(Path file) {
        return Files.isExecutable(file);
    }

    public static boolean setExecutable(String file, boolean exec) {
        return new File(file).setExecutable(exec);
    }

    public static boolean setExecutable(File file, boolean exec) {
        return file.setExecutable(exec);
    }

    /**
     * 设置文件是否可执行
     *
     * @param file 文件
     * @param exec 设置文件是否可执行
     * @return 是否执行成功
     */
    public static boolean setExecutable(Path file, boolean exec) {
        return file.toFile().setExecutable(exec);
    }

    public static boolean isReadable(String file) {
        return new File(file).canRead();
    }

    public static boolean isReadable(File file) {
        return file.canRead();
    }

    /**
     * 文件是否可读
     *
     * @param file 文件
     * @return 可读true
     */
    public static boolean isReadable(Path file) {
        return Files.isReadable(file);
    }

    public static boolean setReadable(String file, boolean readable) {
        return new File(file).setReadable(readable);
    }

    public static boolean setReadable(File file, boolean readable) {
        return file.setReadable(readable);
    }

    /**
     * 设置文件是否可读
     *
     * @param file 文件
     * @param exec 设置文件是否可读
     * @return 是否执行成功
     */
    public static boolean setReadable(Path file, boolean readable) {
        return file.toFile().setReadable(readable);
    }

    public static boolean isWritable(String file) {
        return new File(file).canWrite();
    }

    public static boolean isWritable(File file) {
        return file.canWrite();
    }

    /**
     * 文件是否可写
     *
     * @param file 文件
     * @return 可写true
     */
    public static boolean isWritable(Path file) {
        return Files.isWritable(file);
    }

    public static boolean setWritable(String file, boolean writable) {
        return new File(file).setWritable(writable);
    }

    public static boolean setWritable(File file, boolean writable) {
        return file.setWritable(writable);
    }

    /**
     * 设置文件是否可写
     *
     * @param file 文件
     * @param exec 设置文件是否可写
     * @return 是否执行成功
     */
    public static boolean setWritable(Path file, boolean writable) {
        return file.toFile().setWritable(writable);
    }

    public static boolean isHidden(String file) {
        return new File(file).isHidden();
    }

    public static boolean isHidden(File file) {
        return file.isHidden();
    }

    /**
     * 文件是否隐藏
     *
     * @param file 文件
     * @return 隐藏true
     */
    public static boolean isHidden(Path file) {
        try {
            return Files.isHidden(file);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static boolean isFile(String file) {
        return isFile(new File(file));
    }

    public static boolean isFile(File file) {
        return file.exists() && file.isFile();
    }

    /**
     * 是否为普通文件
     *
     * @param file 文件
     * @return 普通文件true
     */
    public static boolean isFile(Path file) {
        return Files.isRegularFile(file);
    }

    public static boolean isDirectory(String file) {
        return isDirectory(new File(file));
    }

    public static boolean isDirectory(File file) {
        return file.exists() && file.isDirectory();
    }

    /**
     * 是否为文件夹
     *
     * @param file 文件
     * @return 文件夹true
     */
    public static boolean isDirectory(Path file) {
        return Files.isDirectory(file);
    }

    public static boolean isSymbolicLink(String file) {
        return Files.isSymbolicLink(Paths.get(file));
    }

    public static boolean isSymbolicLink(File file) {
        return Files.isSymbolicLink(Paths.get(file.getAbsolutePath()));
    }

    /**
     * 是否为连接文件
     *
     * @param file 文件
     * @return 连接文件true
     */
    public static boolean isSymbolicLink(Path file) {
        return Files.isSymbolicLink(file);
    }

    public static Path getSymbolicLink(String file) {
        try {
            return Files.readSymbolicLink(Paths.get(file));
        } catch (IOException e) {
            return null;
        }
    }

    public static File getSymbolicLink(File file) {
        try {
            return Files.readSymbolicLink(Paths.get(file.getAbsolutePath())).toFile();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取连接源文件
     *
     * @param file 连接文件
     * @return 源文件
     */
    public static Path getSymbolicLink(Path file) {
        try {
            return Files.readSymbolicLink(file);
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean createLink(String source, String link) {
        return createLink(Paths.get(source), Paths.get(link));
    }

    public static boolean createLink(File source, File link) {
        return createLink(Paths.get(source.getAbsolutePath()), Paths.get(link.getAbsolutePath()));
    }

    /**
     * 创建连接文件
     *
     * @param source 源文件
     * @param link   连接文件
     * @return 是否成功
     */
    public static boolean createLink(Path source, Path link) {
        try {
            Files.createDirectories(link.getParent());
            Files.createLink(link, source);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean createSymbolLink(String source, String link) {
        return createSymbolLink(Paths.get(source), Paths.get(link));
    }

    public static boolean createSymbolLink(File source, File link) {
        return createSymbolLink(Paths.get(source.getAbsolutePath()), Paths.get(link.getAbsolutePath()));
    }

    /**
     * 创建连接符号文件
     *
     * @param source 源文件
     * @param link   连接文件
     * @return 是否成功
     */
    public static boolean createSymbolLink(Path source, Path link) {
        try {
            Files.createDirectories(link.getParent());
            Files.createSymbolicLink(link, source);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static String getOwner(String file) {
        return getOwner(Paths.get(file));
    }

    public static String getOwner(File file) {
        return getOwner(Paths.get(file.getAbsolutePath()));
    }

    /**
     * 获取文件所有人
     *
     * @param file 文件
     * @return 文件所有人
     */
    public static String getOwner(Path file) {
        try {
            return Files.getOwner(file).getName();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    // -------------------- file --------------------

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
            s = size + " B";
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

    public static boolean resourceToFile(String source, String file) throws IOException {
        return resourceToFile(source, file, null);
    }

    /**
     * 将资源文件保存到本地
     *
     * @param source  资源
     * @param path    path
     * @param charset 编码
     * @return 是否保存成功
     * @throws IOException exception
     */
    public static boolean resourceToFile(String source, String path, String charset) throws IOException {
        File file = new File(path);
        InputStream in = Files1.class.getClassLoader().getResourceAsStream(source);
        boolean needInit = false;
        if (!file.exists() || !file.isFile()) {
            needInit = true;
        }
        if (!needInit) {
            try {
                if (in != null && file.length() != in.available()) {
                    needInit = true;
                }
            } catch (Exception e) {
                // ignore
            }
        }
        if (needInit && in == null) {
            return false;
        }
        OutputStreamWriter writer = null;
        FileOutputStream out = null;
        try {
            if (!Files1.touch(file, charset)) {
                return false;
            }
            // 清空重新写入数据
            out = new FileOutputStream(file, false);
            Streams.transfer(in, out);
            out.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            Streams.close(in);
            Streams.close(writer);
            Streams.close(out);
        }
        return true;
    }

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
            char[] cs = new char[Const.BUFFER_KB_8];
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
            byte[] bs = new byte[Const.BUFFER_KB_8];
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
                byte[] bytes = new byte[Const.BUFFER_KB_8];
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
            byte[] bytes = new byte[Const.BUFFER_KB_8];
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
            ByteBuffer buffer = ByteBuffer.allocate(Const.BUFFER_KB_8);
            while (inc.read(buffer) != -1) {
                buffer.flip();
                outc.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static boolean touch(String file) {
        return touch(new File(file), null, false);
    }

    public static boolean touch(File file) {
        return touch(file, null, false);
    }

    /**
     * 创建文件
     *
     * @param file 文件
     * @return 是否处理成功
     */
    public static boolean touch(Path file) {
        return touch(file, null, false);
    }

    public static boolean touch(String file, String charset) {
        return touch(new File(file), charset, false);
    }

    public static boolean touch(File file, String charset) {
        return touch(file, charset, false);
    }

    /**
     * 创建文件
     *
     * @param file    文件
     * @param charset 编码格式
     * @return 是否处理成功
     */
    public static boolean touch(Path file, String charset) {
        return touch(file, charset, false);
    }

    public static boolean touch(String file, boolean clean) {
        return touch(new File(file), null, clean);
    }

    public static boolean touch(File file, boolean clean) {
        return touch(file, null, clean);
    }

    /**
     * 创建文件
     *
     * @param file  文件
     * @param clean 如果文件存在是否清除文件内容
     * @return 是否处理成功
     */
    public static boolean touch(Path file, boolean clean) {
        return touch(file, null, clean);
    }

    public static boolean touch(String file, String charset, boolean clean) {
        return touch(new File(file), charset, clean);
    }

    public static boolean touch(File file, String charset, boolean clean) {
        File dir = file.getParentFile();
        boolean create = false;
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return false;
            }
            create = true;
        } else {
            if (!file.exists() || !file.isFile()) {
                create = true;
            }
        }
        if (create) {
            try {
                if (charset != null) {
                    try (OutputStreamWriter w = new OutputStreamWriter(openOutputStream(file), charset)) {
                        w.write(Strings.SPACE);
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return file.createNewFile();
                }
            } catch (IOException e) {
                return false;
            }
        } else if (clean) {
            return cleanFile(file);
        }
        return true;
    }

    /**
     * 创建文件
     *
     * @param file    文件
     * @param charset 编码格式
     * @param clean   如果文件存在是否清除文件内容
     * @return 是否处理成功
     */
    public static boolean touch(Path file, String charset, boolean clean) {
        Path dir = file.getParent();
        boolean create = false;
        if (!Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
                create = true;
            } catch (IOException e) {
                return false;
            }
        } else {
            if (!Files.isRegularFile(file)) {
                create = true;
            }
        }
        if (create) {
            try {
                if (charset != null) {
                    try (OutputStreamWriter w = new OutputStreamWriter(openOutputStreamFast(file), charset)) {
                        w.write(Strings.EMPTY);
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    Files.createFile(file);
                    return true;
                }
            } catch (IOException e) {
                return false;
            }
        } else if (clean) {
            return cleanFile(file);
        }
        return true;
    }

    /**
     * 获取一个IO临时文件
     *
     * @return io临时文件路径
     */
    public static String getTempFilePath() {
        return Systems.HOME_DIR + IO_TEMP_DIR + UUIds.random32() + ".temp";
    }

    public static File touchTempFile() {
        File file = new File(getTempFilePath());
        touch(file);
        return file;
    }

    /**
     * 创建一个IO临时文件
     *
     * @param exitDelete 是否退出删除
     * @return io临时文件路径
     */
    public static File touchTempFile(boolean exitDelete) {
        File file = new File(getTempFilePath());
        touch(file);
        if (exitDelete) {
            file.deleteOnExit();
        }
        return file;
    }

    public static File touchOnDelete(String path) {
        File file = new File(path);
        touchOnDelete(file);
        return file;
    }

    /**
     * 创建一个在退出时删除的文件
     *
     * @param file file
     */
    public static void touchOnDelete(File file) {
        touch(file);
        file.deleteOnExit();
    }

    public static boolean mv(String file, File target) {
        return new File(file).renameTo(target);
    }

    public static boolean mv(File file, File target) {
        return file.renameTo(target);
    }

    /**
     * 文件移动
     *
     * @param file   文件
     * @param target 目标位置
     * @return 是否处理成功
     */
    public static boolean mv(Path file, Path target) {
        try {
            Files.move(file, target);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean mv(File file, String name) {
        return file.renameTo(new File(file.getParentFile() + "/" + name));
    }

    public static boolean mv(String file, String name) {
        File f = new File(file);
        return f.renameTo(new File(f.getParentFile() + "/" + name));
    }

    /**
     * 文件重命名
     *
     * @param file 文件
     * @param name 名称
     * @return 是否处理成功
     */
    public static boolean mv(Path file, String name) {
        try {
            Files.move(file, file.getParent().resolve(name));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean mkdirs(String path) {
        return new File(path).mkdirs();
    }

    public static boolean mkdirs(File path) {
        return path.mkdirs();
    }

    /**
     * 创建文件夹
     *
     * @param path 文件夹
     * @return 是否处理成功
     */
    public static boolean mkdirs(Path path) {
        try {
            Files.createDirectories(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean delete(String file) {
        return delete(new File(file));
    }

    public static boolean delete(File file) {
        if (file.isFile()) {
            return deleteFile(file);
        } else {
            return deleteDir(file);
        }
    }

    /**
     * 删除文件或文件夹
     *
     * @param file 文件
     * @return 是否处理成功
     */
    public static boolean delete(Path file) {
        if (Files.isDirectory(file)) {
            return deleteDir(file);
        } else {
            return deleteFile(file);
        }
    }

    public static boolean deleteDir(String path) {
        return deleteDir(new File(path));
    }

    public static boolean deleteDir(File path) {
        List<File> files = listFiles(path, true, true);
        for (File f : files) {
            if (f.isDirectory()) {
                deleteDir(f);
            } else {
                deleteFile(f);
            }
        }
        return path.delete();
    }

    /**
     * 删除一个文件夹
     *
     * @param path 文件夹
     * @return 是否处理成功
     */
    public static boolean deleteDir(Path path) {
        try {
            Files.list(path).forEach(f -> {
                if (Files.isDirectory(f)) {
                    deleteDir(f);
                } else {
                    try {
                        Files.delete(f);
                    } catch (IOException e) {
                        // ignore
                    }
                }
            });
            Files.delete(path);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static boolean deleteFile(String file) {
        File f = new File(file);
        if (f.isFile()) {
            return f.delete();
        }
        return false;
    }

    public static boolean deleteFile(File file) {
        if (file.isFile()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param file 文件
     * @return 是否处理成功
     */
    public static boolean deleteFile(Path file) {
        try {
            Files.delete(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean deleteBigFile(String file) {
        return deleteBigFile(new File(file));
    }

    /**
     * 删除大文件
     *
     * @param file 文件
     * @return 是否处理成功
     */
    public static boolean deleteBigFile(File file) {
        cleanFile(file);
        return file.delete();
    }

    public static boolean cleanFile(String file) {
        return cleanFile(new File(file));
    }

    public static boolean cleanFile(File file) {
        if (!file.exists()) {
            return true;
        }
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(Strings.EMPTY);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 清空文件
     *
     * @param file 文件
     * @return 是否处理成功
     */
    public static boolean cleanFile(Path file) {
        if (!Files.exists(file)) {
            return true;
        }
        try {
            Files.write(file, new byte[0]);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

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
            r = new RandomAccessFile(file, Const.ACCESS_R);
            long length = r.length();
            if (length == 0) {
                return Const.LF;
            } else if (length >= 2) {
                r.seek(length - 2);
                byte[] bs = new byte[2];
                r.read(bs);
                if (bs[0] == Letters.CR && bs[1] == Letters.LF) {
                    return Const.CR_LF;
                } else if (bs[1] == Letters.CR) {
                    return Const.CR;
                }
            } else if (length == 1) {
                r.seek(length - 1);
                if (r.read() == Letters.CR) {
                    return Const.CR;
                }
            }
        } catch (IOException e) {
            // ignore
        } finally {
            Streams.close(r);
        }
        return Const.LF;
    }

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
        try (RandomAccessFile r = new RandomAccessFile(file, Const.ACCESS_R)) {
            long length = r.length();
            if (length == 0) {
                return Const.LF;
            } else if (length >= 2) {
                r.seek(length - 2);
                byte[] bs = new byte[2];
                r.read(bs);
                if (bs[0] == Letters.CR && bs[1] == Letters.LF) {
                    return Const.CR_LF;
                } else if (bs[1] == Letters.CR) {
                    return Const.CR;
                } else if (bs[1] == Letters.LF) {
                    return Const.LF;
                }
            } else if (length == 1) {
                r.seek(length - 1);
                int read = r.read();
                if (read == Letters.CR) {
                    return Const.CR;
                } else if (read == Letters.LF) {
                    return Const.LF;
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return null;
    }

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
        try (RandomAccessFile r = new RandomAccessFile(file, Const.ACCESS_R)) {
            long length = r.length();
            if (length == 0) {
                return true;
            } else if (length >= 1) {
                r.seek(length - 1);
                int s = r.read();
                if (s == Letters.CR || s == Letters.LF) {
                    return true;
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return false;
    }

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
        try (LineNumberReader rf = new LineNumberReader(new InputStreamReader(openInputStream(file)))) {
            rf.skip(file.length());
            return rf.getLineNumber();
        } catch (IOException e) {
            // ignore
        }
        return 0;
    }

    // -------------------- stream --------------------

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
                throw Exceptions.io("file '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw Exceptions.io("file '" + file + "' cannot be read");
            }
        } else {
            throw Exceptions.io("file '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    public static FileOutputStream openOutputStreamSafe(String file) {
        return openOutputStreamSafe(new File(file), false);
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

    public static FileOutputStream openOutputStreamSafe(String file, boolean append) {
        return openOutputStreamSafe(new File(file), append);
    }

    /**
     * 打开文件输出流
     *
     * @param file   文件
     * @param append append
     * @return 输出流
     */
    public static FileOutputStream openOutputStreamSafe(File file, boolean append) {
        try {
            return openOutputStream(file, append);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static FileOutputStream openOutputStream(String file) throws IOException {
        return openOutputStream(new File(file), false);
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

    public static FileOutputStream openOutputStream(String file, boolean append) throws IOException {
        return openOutputStream(new File(file), append);
    }

    /**
     * 打开文件输出流
     *
     * @param file   文件
     * @param append append
     * @return 输出流
     * @throws IOException IO
     */
    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw Exceptions.io("file '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw Exceptions.io("file '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    throw Exceptions.io("file '" + file + "' could not be created");
                }
            }
        }
        if (append) {
            return new FileOutputStream(file, true);
        } else {
            return new FileOutputStream(file);
        }
    }

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
                throw Exceptions.io("file '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw Exceptions.io("file '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    throw Exceptions.io("file '" + file + "' could not be created");
                }
            }
        }
        return new RandomAccessFile(file, mode);
    }

    // -------------------- path fast --------------------

    public static InputStream openInputStreamFastSafe(String file) {
        try {
            return openInputStreamFast(Paths.get(file));
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static InputStream openInputStreamFastSafe(File file) {
        try {
            return openInputStreamFast(Paths.get(file.getAbsolutePath()));
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 打开文件输入流
     *
     * @param file 文件
     * @return 输入流
     */
    public static InputStream openInputStreamFastSafe(Path file) {
        try {
            return openInputStreamFast(file);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static InputStream openInputStreamFast(String file) throws IOException {
        return openInputStreamFast(Paths.get(file));
    }

    public static InputStream openInputStreamFast(File file) throws IOException {
        return openInputStreamFast(Paths.get(file.getAbsolutePath()));
    }

    /**
     * 打开文件输入流
     *
     * @param file 文件
     * @return 输入流
     * @throws IOException IO
     */
    public static InputStream openInputStreamFast(Path file) throws IOException {
        if (Files.exists(file)) {
            if (Files.isDirectory(file)) {
                throw Exceptions.io("file '" + file + "' exists but is a directory");
            }
            if (!Files.isReadable(file)) {
                throw Exceptions.io("file '" + file + "' cannot be read");
            }
        } else {
            throw Exceptions.io("file '" + file + "' does not exist");
        }
        return Files.newInputStream(file);
    }

    public static OutputStream openOutputStreamFastSafe(String file) {
        try {
            return openOutputStreamFast(Paths.get(file), false);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static OutputStream openOutputStreamFastSafe(File file) {
        try {
            return openOutputStreamFast(Paths.get(file.getAbsolutePath()), false);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 打开文件输出流
     *
     * @param file 文件
     * @return 输出流
     */
    public static OutputStream openOutputStreamFastSafe(Path file) {
        try {
            return openOutputStreamFast(file, false);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static OutputStream openOutputStreamFastSafe(String file, boolean append) {
        try {
            return openOutputStreamFast(Paths.get(file), append);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static OutputStream openOutputStreamFastSafe(File file, boolean append) {
        try {
            return openOutputStreamFast(Paths.get(file.getAbsolutePath()), append);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 打开文件输出流
     *
     * @param file   文件
     * @param append append
     * @return 输出流
     */
    public static OutputStream openOutputStreamFastSafe(Path file, boolean append) {
        try {
            return openOutputStreamFast(file, append);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public static OutputStream openOutputStreamFast(String file) throws IOException {
        return openOutputStreamFast(Paths.get(file), false);
    }

    public static OutputStream openOutputStreamFast(File file) throws IOException {
        return openOutputStreamFast(Paths.get(file.getAbsolutePath()), false);
    }

    /**
     * 打开文件输出流
     *
     * @param file 文件
     * @return 输出流
     * @throws IOException IO
     */
    public static OutputStream openOutputStreamFast(Path file) throws IOException {
        return openOutputStreamFast(file, false);
    }

    public static OutputStream openOutputStreamFast(String file, boolean append) throws IOException {
        return openOutputStreamFast(Paths.get(file), append);
    }

    public static OutputStream openOutputStreamFast(File file, boolean append) throws IOException {
        return openOutputStreamFast(Paths.get(file.getAbsolutePath()), append);
    }

    /**
     * 打开文件输出流
     *
     * @param file   文件
     * @param append append
     * @return 输出流
     * @throws IOException IO
     */
    public static OutputStream openOutputStreamFast(Path file, boolean append) throws IOException {
        if (Files.exists(file)) {
            if (Files.isDirectory(file)) {
                throw Exceptions.io("file '" + file + "' exists but is a directory");
            }
            if (!Files.isWritable(file)) {
                throw Exceptions.io("file '" + file + "' cannot be written to");
            }
        } else {
            Path parent = file.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
        }
        if (append) {
            return Files.newOutputStream(file, StandardOpenOption.APPEND);
        } else {
            return Files.newOutputStream(file);
        }
    }

    // -------------------- path --------------------

    public static URL toUrl(Path path) {
        return toUrl(path.toFile());
    }

    public static URL toUrl(String file) {
        return toUrl(new File(file));
    }

    /**
     * File -> URL
     *
     * @param file file
     * @return URL
     */
    public static URL toUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (Exception e) {
            throw Exceptions.runtime("file to url error", e);
        }
    }

    public static URI toUri(Path path) {
        return toUri(path.toFile());
    }

    public static URI toUri(String file) {
        return toUri(new File(file));
    }

    /**
     * File -> URI
     *
     * @param file file
     * @return URI
     */
    public static URI toUri(File file) {
        return file.toURI();
    }

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
     * File -> URL
     *
     * @param files files
     * @return URL
     */
    public static List<URL> toUrls(List<File> files) {
        List<URL> urls = new ArrayList<>();
        for (File file : files) {
            urls.add(toUrl(file));
        }
        return urls;
    }

    /**
     * File -> URI
     *
     * @param files files
     * @return URI
     */
    public static URL[] toUrls(File[] files) {
        int len = files.length;
        URL[] urls = new URL[len];
        for (int i = 0; i < len; i++) {
            urls[i] = toUrl(files[i]);
        }
        return urls;
    }

    /**
     * File -> URI
     *
     * @param files files
     * @return URI
     */
    public static List<URI> toUris(List<File> files) {
        List<URI> uris = new ArrayList<>();
        for (File file : files) {
            uris.add(toUri(file));
        }
        return uris;
    }

    /**
     * File -> URI
     *
     * @param files files
     * @return URI
     */
    public static URI[] toUris(File[] files) {
        int len = files.length;
        URI[] uris = new URI[len];
        for (int i = 0; i < len; i++) {
            uris[i] = toUri(files[i]);
        }
        return uris;
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
        int i = file.lastIndexOf(".");
        if (i == -1) {
            return Strings.EMPTY;
        }
        return file.substring(i + 1);
    }

    /**
     * 获取文件名称
     *
     * @param file 文件
     * @return 文件名称
     */
    public static String getFileName(String file) {
        file = file.replaceAll("\\\\+", "/").replaceAll("//+", "/");
        if (file.equals("/")) {
            return "/";
        }
        String[] paths = file.split("/");
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

    /**
     * 路径是否统一化
     * <p>
     * /home/.././etc --> false
     * /home/./etc --> false
     * /home/ --> true
     * /home --> true
     *
     * @param path 路径
     * @return 是否统一化
     */
    public static boolean isNormalize(String path) {
        String[] ps = getPath(path).split("/");
        for (int i = 0; i < ps.length; i++) {
            String s = ps[i];
            if (".".equals(s)) {
                return false;
            } else if ("..".equals(s)) {
                return false;
            } else {
                continue;
            }
        }
        return true;
    }

    /**
     * 10进制权限转8进制权限
     * <p>
     * 777 -> 511
     *
     * @param permission 10进制
     * @return 8进制
     */
    public static int permission10to8(int permission) {
        return Integer.parseInt(new BigInteger(permission + Strings.EMPTY, 8).toString(10));
    }

    /**
     * 8进制权限转10进制权限
     * <p>
     * 511 -> 777
     *
     * @param permission 8进制
     * @return 10进制
     */
    public static int permission8to10(int permission) {
        return Integer.parseInt(new BigInteger(permission + Strings.EMPTY, 10).toString(8));
    }

    public static void main(String[] args) {
        System.out.println(permission10to8(777));
        System.out.println(permission8to10(511));
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

    public static List<File> listDirs(String path) {
        return listDirs(new File(path), false);
    }

    public static List<File> listDirs(File path) {
        return listDirs(path, false);
    }

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

    public static List<File> listFiles(File path) {
        return listFiles(path, false, false);
    }

    public static List<File> listFiles(String path) {
        return listFiles(new File(path), false, false);
    }

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

    public static List<File> listFilesSuffix(String dirPath, String suffix) {
        return listFilesSuffix(new File(dirPath), suffix, false, false);
    }

    public static List<File> listFilesSuffix(String dirPath, String suffix, boolean child) {
        return listFilesSuffix(new File(dirPath), suffix, child, false);
    }

    public static List<File> listFilesSuffix(String dirPath, String suffix, boolean child, boolean dir) {
        return listFilesSuffix(new File(dirPath), suffix, child, dir);
    }

    public static List<File> listFilesSuffix(File dirPath, String suffix) {
        return listFilesSuffix(dirPath, suffix, false, false);
    }

    public static List<File> listFilesSuffix(File dirPath, String suffix, boolean child) {
        return listFilesSuffix(dirPath, suffix, child, false);
    }

    /**
     * 搜索文件 后缀
     *
     * @param dirPath 文件夹
     * @param suffix  后缀
     * @param child   是否递归子文件夹
     * @param dir     是否包含文件夹
     * @return 文件
     */
    public static List<File> listFilesSuffix(File dirPath, String suffix, boolean child, boolean dir) {
        return listFilesSearch(dirPath, FileFilter.suffix(suffix), child, dir);
    }

    public static List<File> listFilesMatch(String dirPath, String name) {
        return listFilesMatch(new File(dirPath), name, false, false);
    }

    public static List<File> listFilesMatch(String dirPath, String name, boolean child) {
        return listFilesMatch(new File(dirPath), name, child, false);
    }

    public static List<File> listFilesMatch(String dirPath, String name, boolean child, boolean dir) {
        return listFilesMatch(new File(dirPath), name, child, dir);
    }

    public static List<File> listFilesMatch(File dirPath, String name) {
        return listFilesMatch(dirPath, name, false, false);
    }

    public static List<File> listFilesMatch(File dirPath, String name, boolean child) {
        return listFilesMatch(dirPath, name, child, false);
    }

    /**
     * 搜索文件 文件名
     *
     * @param dirPath 文件夹
     * @param name    搜索
     * @param child   是否递归子文件夹
     * @param dir     是否包含文件夹
     * @return 文件
     */
    public static List<File> listFilesMatch(File dirPath, String name, boolean child, boolean dir) {
        return listFilesSearch(dirPath, FileFilter.match(name), child, dir);
    }

    public static List<File> listFilesPattern(String dirPath, Pattern pattern) {
        return listFilesPattern(new File(dirPath), pattern, false, false);
    }

    public static List<File> listFilesPattern(String dirPath, Pattern pattern, boolean child) {
        return listFilesPattern(new File(dirPath), pattern, child, false);
    }

    public static List<File> listFilesPattern(String dirPath, Pattern pattern, boolean child, boolean dir) {
        return listFilesPattern(new File(dirPath), pattern, child, dir);
    }

    public static List<File> listFilesPattern(File dirPath, Pattern pattern) {
        return listFilesPattern(dirPath, pattern, false, false);
    }

    public static List<File> listFilesPattern(File dirPath, Pattern pattern, boolean child) {
        return listFilesPattern(dirPath, pattern, child, false);
    }

    /**
     * 搜索文件 正则
     *
     * @param dirPath 文件夹
     * @param pattern 正则
     * @param child   是否递归子文件夹
     * @param dir     是否包含文件夹
     * @return 文件
     */
    public static List<File> listFilesPattern(File dirPath, Pattern pattern, boolean child, boolean dir) {
        return listFilesSearch(dirPath, FileFilter.pattern(pattern), child, dir);
    }

    public static List<File> listFilesFilter(String dirPath, FileFilter filter) {
        return listFilesFilter(new File(dirPath), filter, false, false);
    }

    public static List<File> listFilesFilter(String dirPath, FileFilter filter, boolean child) {
        return listFilesFilter(new File(dirPath), filter, child, false);
    }

    public static List<File> listFilesFilter(String dirPath, FileFilter filter, boolean child, boolean dir) {
        return listFilesFilter(new File(dirPath), filter, child, dir);
    }

    public static List<File> listFilesFilter(File dirPath, FileFilter filter) {
        return listFilesFilter(dirPath, filter, false, false);
    }

    public static List<File> listFilesFilter(File dirPath, FileFilter filter, boolean child) {
        return listFilesFilter(dirPath, filter, child, false);
    }

    /**
     * 搜索文件 过滤器
     *
     * @param dirPath 文件夹
     * @param filter  过滤器
     * @param child   是否递归子文件夹
     * @param dir     是否包含文件夹
     * @return 文件
     */
    public static List<File> listFilesFilter(File dirPath, FileFilter filter, boolean child, boolean dir) {
        return listFilesSearch(dirPath, filter, child, dir);
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
    private static List<File> listFilesSearch(File dirPath, FileFilter filter, boolean child, boolean dir) {
        List<File> list = new ArrayList<>();
        File[] files = dirPath.listFiles();
        if (files != null) {
            for (File file : files) {
                boolean isDir = file.isDirectory();
                if (!isDir || dir) {
                    if (filter.accept(file, file.getName())) {
                        list.add(file);
                    }
                }
                if (isDir && child) {
                    list.addAll(listFilesSearch(file, filter, true, dir));
                }
            }
        }
        return list;
    }

}
