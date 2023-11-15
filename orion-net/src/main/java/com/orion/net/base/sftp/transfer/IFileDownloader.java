package com.orion.net.base.sftp.transfer;

import java.io.IOException;

/**
 * 文件下载器 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/4/11 18:09
 */
public interface IFileDownloader extends IFileTransfer {

    /**
     * 强制覆盖下载 (不检测文件锁定/不检测文件大小/不走断点续传)
     *
     * @param forceOverride 是否覆盖
     */
    void forceOverride(boolean forceOverride);

    /**
     * 文件大小相同则覆盖下载
     *
     * @param fileSizeEqualOverride fileSizeEqualOverride
     */
    void fileSizeEqualOverride(boolean fileSizeEqualOverride);

    /**
     * 获取远程文件大小
     *
     * @return fileSize 文件不存在则返回 -1
     * @throws IOException IOException
     */
    long getRemoteFileLength() throws IOException;

    /**
     * 检查远程文件存在并且大小相同
     *
     * @return 是否相等
     * @throws IOException IOException
     */
    boolean checkRemoteFilePresentSizeEqual() throws IOException;

}
