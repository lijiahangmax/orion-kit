package com.orion.utils.io;

import com.orion.lang.wrapper.Args;
import com.orion.utils.Streams;
import com.orion.utils.Strings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * 文件合并器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/2/14 16:11
 */
public class FileMerge implements Callable<String> {

    /**
     * 默认缓冲区大小 2m
     */
    private static int BUFFER_SIZE = 1024 * 2;

    /**
     * 文件夹
     */
    private File file;

    /**
     * 文件块名称
     */
    private String[] blockFile;

    /**
     * 缓冲区大小
     */
    private int bufferSize;

    public FileMerge(String file) {
        this(new File(file), BUFFER_SIZE);
    }

    public FileMerge(String file, int bufferSize) {
        this(new File(file), bufferSize);
    }

    public FileMerge(File file) {
        this(file, BUFFER_SIZE);
    }

    public FileMerge(File file, int bufferSize) {
        if (file == null || !file.isDirectory()) {
            throw new RuntimeException("The folder path is incorrect");
        }
        this.file = file;
        String[] cf = file.list();
        if (cf == null || cf.length == 0) {
            throw new RuntimeException("File block does not exist");
        }
        this.blockFile = cf;
        if (bufferSize < BUFFER_SIZE) {
            bufferSize = BUFFER_SIZE;
        }
        this.bufferSize = bufferSize;
    }

    @Override
    public String call() {
        Args.Two<String, List<String>> fl = shuffleFile(blockFile);
        try {
            String mergeFile = mergeFile(file.getAbsolutePath(), fl.getArg1(), fl.getArg2());
            System.out.println("MD5 sign: " + Files1.md5(mergeFile));
            return mergeFile;
        } catch (Exception e) {
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    /**
     * 整理块文件
     *
     * @param files 块文件
     * @return 合并文件路径, 块文件路径
     */
    private Args.Two<String, List<String>> shuffleFile(String[] files) {
        Map<Integer, String> fm = new HashMap<>();
        for (String file : files) {
            int l = file.lastIndexOf(".");
            String blockIndex = file.substring(l + 1, file.length());
            if (Strings.isNumber(blockIndex)) {
                fm.put(new Integer(blockIndex), file);
            }
        }
        int last = 0, i = 0;
        String filePath = "";
        List<String> fileList = new ArrayList<>();
        TreeSet<Map.Entry<Integer, String>> entries = new TreeSet<>(Comparator.comparingInt(Map.Entry::getKey));
        entries.addAll(fm.entrySet());
        for (Map.Entry<Integer, String> is : entries) {
            if (i++ == 0) {
                last = is.getKey();
                String ff = is.getValue();
                filePath = ff.substring(0, ff.lastIndexOf("."));
            }
            if (last++ != is.getKey()) {
                throw new RuntimeException("缺少 " + (last - 1) + " 文件");
            }
            fileList.add(is.getValue());
        }
        return Args.of(filePath, fileList);
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
        FileOutputStream fo = null;
        try {
            File wf = new File(dir + "\\" + fileName);
            if (!wf.exists()) {
                wf.createNewFile();
            }
            fo = new FileOutputStream(wf);
            for (String block : blocks) {
                FileInputStream in = new FileInputStream(new File(dir + "\\" + block));
                int read = 0;
                int c = 0;
                byte[] buffer = new byte[bufferSize];
                while ((read = in.read(buffer)) != -1) {
                    fo.write(buffer, 0, read);
                }
                Streams.closeQuietly(in);
            }
            fo.flush();
            return wf.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("merge error: " + e.getMessage());
        } finally {
            Streams.closeQuietly(fo);
        }
    }

}
