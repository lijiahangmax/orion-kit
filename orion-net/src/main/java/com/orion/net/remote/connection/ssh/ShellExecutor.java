package com.orion.net.remote.connection.ssh;

import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.orion.lang.utils.Exceptions;
import com.orion.net.base.ssh.BaseShellExecutor;

import java.io.IOException;

/**
 * 抽象命令执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/15 22:26
 */
public class ShellExecutor extends BaseShellExecutor {

    /**
     * 会话
     */
    protected Session session;

    public ShellExecutor(Session session) {
        this.session = session;
    }

    @Override
    public void x11Forward(boolean enable) {
        throw Exceptions.unsupported("unsupported x11 forwarding");
    }

    @Override
    public void resize() {
        try {
            session.requestWindowChange(cols, rows, width, height);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public void exec() {
        if (streamHandler == null) {
            throw Exceptions.runtime("shell std output stream handler is null");
        }
        if (run) {
            throw Exceptions.runtime("shell executor can only be executed once");
        }
        this.run = true;
        try {
            session.requestPTY(terminalType, cols, rows, width, height, null);
            session.startShell();
        } catch (IOException e) {
            throw Exceptions.connection(e);
        }
        this.outputStream = session.getStdin();
        this.inputStream = new StreamGobbler(session.getStdout());
        // read standard output
        super.listenerStdout();
    }

    @Override
    public void close() {
        super.close();
        session.close();
    }

    public Session getSession() {
        return session;
    }

}
