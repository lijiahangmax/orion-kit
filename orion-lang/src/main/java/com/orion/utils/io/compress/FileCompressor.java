package com.orion.utils.io.compress;

import java.io.File;
import java.io.InputStream;

/**
 * 文件压缩器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 15:57
 */
public interface FileCompressor {

    /**
     * 添加文件
     *
     * @param file file
     */
    void addFile(String file);

    /**
     * 添加文件
     *
     * @param file file
     */
    void addFile(File file);

    /**
     * 添加文件
     *
     * @param name name
     * @param bs   bs
     */
    void addFile(String name, byte[] bs);

    /**
     * 添加文件
     *
     * @param name name
     * @param in   in
     */
    void addFile(String name, InputStream in);

    /**
     * 进行压缩
     *
     * @throws Exception compressException
     */
    void compress() throws Exception;

    /**
     * 设置后缀
     *
     * @param suffix 后缀
     */
    void setSuffix(String suffix);

    /**
     * 设置压缩产物文件目录
     *
     * @param compressPath 压缩文件目录
     */
    void setCompressPath(String compressPath);

    /**
     * 设置压缩产物文件名
     */
    void setFileName(String fileName);

    /**
     * 设置压缩产物文件绝对路径
     *
     * @param path 压缩产物文件绝对路径
     */
    void setAbsoluteCompressPath(String path);

    /**
     * 获取压缩产物文件绝对路径
     *
     * @return 压缩产物文件绝对路径
     */
    String getAbsoluteCompressPath();

    /**
     * 获取后缀
     *
     * @return 后缀
     */
    String getSuffix();

}
