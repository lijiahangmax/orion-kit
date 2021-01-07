package com.orion.remote.channel;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * 控制台 Shell
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/22 11:11
 */
public class ConsoleShell {

    /**
     * 主机
     */
    private String host;

    /**
     * 端口
     */
    private int port = 22;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

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
        session.connect(2000);
        this.channelShell = (ChannelShell) session.openChannel("shell");
        channelShell.setInputStream(System.in);
        channelShell.setOutputStream(System.out);
        channelShell.connect(2000);
    }

    /**
     * 关闭连接
     */
    public void disconnect() {
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
