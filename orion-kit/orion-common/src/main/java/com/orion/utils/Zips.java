package com.orion.utils;

import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具类
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/10/9 11:59
 */
public class Zips {

    private Zips() {
    }

    private static final String DEFAULT_FORMAT = "zip";

    /**
     * 压缩文件
     *
     * @param path 要压缩的文件路径
     * @return 压缩文件目录 null压缩失败
     */
    public static String compress(String path) {
        return compress(path, null, DEFAULT_FORMAT);
    }

    /**
     * 压缩文件
     *
     * @param path   要压缩的文件路径
     * @param format 生成的格式
     * @return 压缩文件目录 null压缩失败
     */
    public static String compress(String path, String format) {
        return compress(path, null, format);
    }

    /**
     * 压缩文件
     *
     * @param path   要压缩的文件路径
     * @param dest   压缩后的文件路径 不需要加文件名
     * @param format 生成的格式
     * @return 压缩文件目录 null压缩失败
     */
    public static String compress(String path, String dest, String format) {
        Valid.notNull(path, "path is null");
        format = Strings.def(format, DEFAULT_FORMAT);
        File file = new File(path);
        if (!file.exists()) {
            throw Exceptions.runtime("File not found " + path);
        }
        if (Strings.isBlank(dest)) {
            dest = file.getAbsolutePath() + "." + format;
        } else {
            dest += file.getName() + "." + format;
        }
        try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(dest)))) {
            generateFile(zos, file, "");
            return dest;
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
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
            try (FileInputStream in = new FileInputStream(file)) {
                out.putNextEntry(new ZipEntry(dir));
                Streams.transfer(in, out);
            }
        }
    }

    /**
     * 解压文件
     *
     * @param src 压缩文件路径
     * @return 解压后的路径
     */
    public static String unCompress(String src) {
        return unCompress(src, null);
    }

    /**
     * 解压文件
     *
     * @param src      压缩文件路径
     * @param destPath 解压路径
     * @return 解压后的路径
     */
    public static String unCompress(String src, String destPath) {
        Valid.notNull(src, "path is null");
        File file = new File(src);
        if (!file.exists()) {
            throw Exceptions.runtime("File not found " + src);
        }
        if (Strings.isBlank(destPath)) {
            String name = file.getName();
            destPath = file.getParent() + File.separator + (name.substring(0, name.lastIndexOf(".")));
        }
        try (ZipFile zipFile = new ZipFile(file)) {
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
        }
    }

}
