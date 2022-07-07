package com.orion.net.remote.channel;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.orion.lang.able.SafeCloseable;
import com.orion.lang.constant.Const;
import com.orion.net.remote.TerminalType;

/**
 * 控制台 Shell
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/22 11:11
 */
public class ConsoleShell implements SafeCloseable {

    /**
     * 主机
     */
    private final String host;

    /**
     * 端口
     */
    private final int port;

    /**
     * 用户名
     */
    private final String username;

    /**
     * 密码
     */
    private final String password;

    /**
     * session
     */
    private Session session;

    /**
     * channel
     */
    private ChannelShell channelShell;

    public ConsoleShell(String host, String username, String password) {
        this.host = host;
        this.port = 22;
        this.username = username;
        this.password = password;
    }

    public ConsoleShell(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * 打开shell
     */
    public void openShell() throws Exception {
        JSch c = new JSch();
        this.session = c.getSession(username, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(Const.MS_S_3);
        this.channelShell = (ChannelShell) session.openChannel("shell");
        channelShell.setPtyType(TerminalType.XTERM.getType());
        channelShell.setInputStream(System.in);
        channelShell.setOutputStream(System.out);
        channelShell.connect();
    }

    @Override
    public void close() {
        session.disconnect();
        channelShell.disconnect();
    }

    public Session getSession() {
        return session;
    }

    public ChannelShell getChannelShell() {
        return channelShell;
    }

}
