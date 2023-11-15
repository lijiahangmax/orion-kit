package com.orion.net.base.sftp.transfer;

import com.orion.lang.able.SafeCloseable;
import com.orion.lang.support.progress.ByteTransferRateProgress;

/**
 * 文件传输器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/19 9:56
 */
public interface IFileTransfer extends Runnable, SafeCloseable {

    /**
     * 终止传输
     */
    void abort();

    /**
     * 获取传输进度条
     *
     * @return 传输进度条
     */
    ByteTransferRateProgress getProgress();

}
