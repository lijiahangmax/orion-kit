package com.orion.remote.connection.ssh;

import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.orion.utils.Exceptions;

import java.io.IOException;

/**
 * 抽象命令执行器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/15 22:26
 */
public class ShellExecutor extends BaseRemoteExecutor {

    /**
     * shell解析器类型
     */
    private String shellType;

    public ShellExecutor(Session session) {
        this(session, "xterm");
    }

    public ShellExecutor(Session session, String shellType) {
        super(session);
        this.shellType = shellType;
    }

    /**
     * 启动并且读取输出
     */
    @Override
    public void exec() {
        super.exec();
        try {
            session.requestPTY(this.shellType);
            session.startShell();
        } catch (IOException e) {
            done = true;
            throw Exceptions.connection(e);
        }
        outputStream = session.getStdin();
        inputStream = new StreamGobbler(session.getStdout());
        super.listenerInput();
    }

    public String getShellType() {
        return shellType;
    }

}
