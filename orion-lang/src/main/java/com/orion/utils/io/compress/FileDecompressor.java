package com.orion.utils.io.compress;

import java.io.File;

/**
 * 文件解压器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 15:57
 */
public interface FileDecompressor {

    /**
     * 设置解压文件
     *
     * @param decompressFile decompressFile
     */
    void setDecompressFile(String decompressFile);

    /**
     * 设置解压文件
     *
     * @param decompressFile decompressFile
     */
    void setDecompressFile(File decompressFile);

    /**
     * 设置解压路径
     *
     * @param decompressPath 解压路径
     */
    void setDecompressTargetPath(String decompressPath);

    /**
     * 设置解压路径
     *
     * @param decompressPath 解压路径
     */
    void setDecompressTargetPath(File decompressPath);

    /**
     * 设置后缀
     *
     * @param suffix 后缀
     */
    void setSuffix(String suffix);

    /**
     * 解压
     *
     * @throws Exception decompressException
     */
    void decompress() throws Exception;

    /**
     * 获取解压文件
     *
     * @return 解压文件
     */
    File getDecompressFile();

    /**
     * 获取解压路径
     *
     * @return 解压路径
     */
    File getDecompressTargetPath();

    /**
     * 获取后缀
     *
     * @return 后缀
     */
    String getSuffix();

}
