package com.orion.ext.watch.file;

import com.orion.able.Stoppable;
import com.orion.able.Watchable;

/**
 * 文件监听器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/28 16:40
 */
public abstract class FileWatcher implements Watchable, Runnable, Stoppable {

    @Override
    public void run() {
        watch();
    }

    /**
     * 是否为运行状态
     *
     * @return ignore
     */
    public abstract boolean isRun();

}
