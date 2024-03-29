package com.orion.lang.utils.io.split;

import com.orion.lang.constant.Const;
import com.orion.lang.define.wrapper.Pair;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;

/**
 * 文件合并器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/2/14 16:11
 */
public class FileMerge implements Callable<String> {

    /**
     * 文件夹
     */
    private final File file;

    /**
     * 文件块名称
     */
    private final String[] blockFile;

    /**
     * 缓冲区大小
     */
    private final int bufferSize;

    public FileMerge(String file) {
        this(new File(file), Const.BUFFER_KB_8);
    }

    public FileMerge(String file, int bufferSize) {
        this(new File(file), bufferSize);
    }

    public FileMerge(File file) {
        this(file, Const.BUFFER_KB_8);
    }

    public FileMerge(File file, int bufferSize) {
        if (file == null || !file.isDirectory()) {
            throw Exceptions.runtime("the folder path is incorrect");
        }
        this.file = file;
        String[] cf = file.list();
        if (cf == null || cf.length == 0) {
            throw Exceptions.runtime("file block does not exist");
        }
        this.blockFile = cf;
        if (bufferSize < Const.BUFFER_KB_8) {
            bufferSize = Const.BUFFER_KB_8;
        }
        this.bufferSize = bufferSize;
    }

    @Override
    public String call() {
        Pair<String, List<String>> fl = this.shuffleFile(blockFile);
        try {
            return this.mergeFile(file.getAbsolutePath(), fl.getKey(), fl.getValue());
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * 整理块文件
     *
     * @param files 块文件
     * @return 合并文件路径, 块文件路径
     */
    private Pair<String, List<String>> shuffleFile(String[] files) {
        Map<Integer, String> fm = new TreeMap<>();
        for (String file : files) {
            int l = file.lastIndexOf(".");
            String blockIndex = file.substring(l + 1);
            if (Strings.isNumber(blockIndex)) {
                fm.put(Integer.valueOf(blockIndex), file);
            }
        }
        int last = 0, i = 0;
        String filePath = Strings.EMPTY;
        List<String> fileList = new ArrayList<>();
        for (Map.Entry<Integer, String> is : fm.entrySet()) {
            if (i++ == 0) {
                last = is.getKey();
                String ff = is.getValue();
                filePath = ff.substring(0, ff.lastIndexOf("."));
            }
            if (last++ != is.getKey()) {
                throw Exceptions.runtime("not found index: " + (last - 1) + " block file");
            }
            fileList.add(is.getValue());
        }
        return Pair.of(filePath, fileList);
    }

    /**
     * 合并文件
     *
     * @param dir      文件夹目录
     * @param fileName 合并文件名
     * @param blocks   文件块路径
     * @return 合并问价路径
     */
    private String mergeFile(String dir, String fileName, List<String> blocks) {
        String path = Files1.getPath(dir, fileName);
        try (OutputStream dist = Files1.openOutputStreamFast(path)) {
            for (String block : blocks) {
                FileInputStream in = Files1.openInputStream(Files1.getPath(dir, block));
                byte[] buffer = new byte[bufferSize];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    dist.write(buffer, 0, read);
                }
                Streams.close(in);
            }
            dist.flush();
            return path;
        } catch (Exception e) {
            throw Exceptions.runtime("merge error: " + e.getMessage());
        }
    }

}
