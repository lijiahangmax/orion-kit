package com.orion.utils.io;

import com.orion.utils.Exceptions;
import com.orion.utils.Strings;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具类 支持文件夹 zip jar
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/10/9 11:59
 */
public class FileCompress {

    private FileCompress() {
    }

    private static final String ZIP_FORMAT = "zip";

    private static final String JAR_FORMAT = "jar";

    public static String compress(String path) {
        return compress(new File(path), null, ZIP_FORMAT);
    }

    public static String compress(File file) {
        return compress(file, null, ZIP_FORMAT);
    }

    public static String compress(String path, String format) {
        return compress(new File(path), null, format);
    }

    public static String compress(File file, String format) {
        return compress(file, null, format);
    }

    public static String compress(String file, String dest, String format) {
        return compress(new File(file), dest, format);
    }

    /**
     * 压缩文件
     *
     * @param file   要压缩的文件
     * @param dest   压缩后的文件路径 不需要加文件名
     * @param format 生成的格式
     * @return 压缩文件目录 null压缩失败
     */
    public static String compress(File file, String dest, String format) {
        format = Strings.def(format, ZIP_FORMAT);
        if (!file.exists()) {
            throw Exceptions.runtime("File not found " + file);
        }
        if (Strings.isBlank(dest)) {
            dest = file.getAbsolutePath() + "." + format;
        } else {
            dest += file.getName() + "." + format;
        }

        ZipOutputStream zos = null;
        try {
            if (format.endsWith(JAR_FORMAT)) {
                zos = new JarOutputStream(new BufferedOutputStream(Files1.openOutputStreamFast(dest)));
            } else {
                zos = new ZipOutputStream(new BufferedOutputStream(Files1.openOutputStreamFast(dest)));
            }
            generateFile(zos, file, "");
            return dest;
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        } finally {
            Streams.close(zos);
        }
    }

    /**
     * 压缩
     *
     * @param out  输出流
     * @param file 压缩文件
     * @param dir  压缩文件内的文件夹
     */
    private static void generateFile(ZipOutputStream out, File file, String dir) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            out.putNextEntry(new ZipEntry(dir + "/"));
            dir = dir.length() == 0 ? "" : dir + "/";
            if (files != null && files.length != 0) {
                for (File file1 : files) {
                    generateFile(out, file1, dir + file1.getName());
                }
            }
        } else {
            try (InputStream in = Files1.openInputStreamFast(file)) {
                out.putNextEntry(new ZipEntry(dir));
                Streams.transfer(in, out);
            }
        }
    }

    public static String unCompress(String src) {
        return unCompress(new File(src), null);
    }

    public static String unCompress(File src) {
        return unCompress(src, null);
    }

    public static String unCompress(String file, String destPath) {
        return unCompress(new File(file), destPath);
    }

    /**
     * 解压文件
     *
     * @param file     压缩文件路径
     * @param destPath 解压路径
     * @return 解压后的路径
     */
    public static String unCompress(File file, String destPath) {
        if (!file.exists()) {
            throw Exceptions.runtime("File not found " + file);
        }
        if (Strings.isBlank(destPath)) {
            String name = file.getName();
            destPath = file.getParent() + File.separator + (name.substring(0, name.lastIndexOf(".")));
        }
        ZipFile zipFile = null;
        try {
            if (file.getAbsolutePath().endsWith(JAR_FORMAT)) {
                zipFile = new JarFile(file);
            } else {
                zipFile = new ZipFile(file);
            }
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    String dirPath = destPath + File.separator + entry.getName();
                    File dir = new File(dirPath);
                    Files1.mkdirs(dir);
                } else {
                    File targetFile = new File(destPath + File.separator + entry.getName());
                    Files1.touch(targetFile);
                    try (InputStream in = zipFile.getInputStream(entry);
                         FileOutputStream out = new FileOutputStream(targetFile)) {
                        Streams.transfer(in, out);
                    }
                }
            }
            return destPath;
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        } finally {
            Streams.close(zipFile);
        }
    }

}
