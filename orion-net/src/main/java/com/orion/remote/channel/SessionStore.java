package com.orion.remote.channel;

import com.jcraft.jsch.*;
import com.orion.remote.channel.sftp.SftpExecutor;
import com.orion.remote.channel.sftp.bigfile.SftpDownload;
import com.orion.remote.channel.sftp.bigfile.SftpUpload;
import com.orion.remote.channel.ssh.CommandExecutor;
import com.orion.remote.channel.ssh.ShellExecutor;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;

import java.io.File;

/**
 * Session Store
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/5 23:08
 */
public class SessionStore {

    private Session session;

    public SessionStore(String username, String host) {
        this(username, host, 22);
    }

    public SessionStore(String username, String host, int port) {
        try {
            this.session = SessionHolder.CH.getSession(username, host, port);
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

    /**
     * 设置属性
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public SessionStore setConfig(String key, String value) {
        session.setConfig(key, value);
        return this;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     * @return this
     */
    public SessionStore setPassword(String password) {
        session.setPassword(password);
        return this;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     * @return this
     */
    public SessionStore setPassword(byte[] password) {
        session.setPassword(password);
        return this;
    }

    /**
     * 设置超时时间
     *
     * @param timeout 超时时间
     * @return this
     */
    public SessionStore setTimeout(int timeout) {
        Valid.gte(timeout, 0, "the time must greater than or equal 0");
        try {
            session.setServerAliveInterval(timeout);
            session.setServerAliveCountMax(2);
            session.setTimeout(timeout);
        } catch (Exception e) {
            // impossible
            throw Exceptions.runtime(e);
        }
        return this;
    }

    /**
     * 设置http代理
     *
     * @param host 主机
     * @param port 端口
     * @return this
     */
    public SessionStore setHttpProxy(String host, int port) {
        session.setProxy(new ProxyHTTP(host, port));
        return this;
    }

    /**
     * 设置http代理
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return this
     */
    public SessionStore setHttpProxy(String host, int port, String username, String password) {
        ProxyHTTP proxy = new ProxyHTTP(host, port);
        proxy.setUserPasswd(username, password);
        session.setProxy(proxy);
        return this;
    }

    /**
     * 设置socket4代理
     *
     * @param host 主机
     * @param port 端口
     * @return this
     */
    public SessionStore setSocket4Proxy(String host, int port) {
        session.setProxy(new ProxySOCKS4(host, port));
        return this;
    }

    /**
     * 设置socket4代理
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return this
     */
    public SessionStore setSocket4Proxy(String host, int port, String username, String password) {
        ProxySOCKS4 proxy = new ProxySOCKS4(host, port);
        proxy.setUserPasswd(username, password);
        session.setProxy(proxy);
        return this;
    }

    /**
     * 设置socket5代理
     *
     * @param host 主机
     * @param port 端口
     * @return this
     */
    public SessionStore setSocket5Proxy(String host, int port) {
        session.setProxy(new ProxySOCKS5(host, port));
        return this;
    }

    /**
     * 设置socket5代理
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return this
     */
    public SessionStore setSocket5Proxy(String host, int port, String username, String password) {
        ProxySOCKS5 proxy = new ProxySOCKS5(host, port);
        proxy.setUserPasswd(username, password);
        session.setProxy(proxy);
        return this;
    }

    /**
     * 设置客户端版本
     *
     * @param version 版本
     * @return this
     */
    public SessionStore setClientVersion(String version) {
        session.setClientVersion(version);
        return this;
    }

    /**
     * 设置是否守护进程
     *
     * @param daemon true守护进程
     * @return this
     */
    public SessionStore setDaemonThread(boolean daemon) {
        session.setDaemonThread(daemon);
        return this;
    }

    /**
     * 建立连接
     *
     * @return this
     */
    public SessionStore connect() {
        try {
            session.connect();
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
        return this;
    }

    /**
     * 建立连接
     *
     * @param timeout 超时时间
     * @return this
     */
    public SessionStore connect(int timeout) {
        try {
            session.connect(timeout);
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
        return this;
    }

    /**
     * 关闭连接
     */
    public void disconnect() {
        session.disconnect();
    }

    /**
     * @return 是否已建立连接
     */
    public boolean isConnected() {
        return session.isConnected();
    }

    /**
     * 获取 CommandExecutor
     *
     * @param command 命令
     * @return CommandExecutor
     */
    public CommandExecutor getCommandExecutor(String command) {
        try {
            return new CommandExecutor((ChannelExec) session.openChannel("exec"), command);
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    /**
     * 获取 CommandExecutor
     *
     * @param command 命令
     * @param charset 命令编码格式
     * @return CommandExecutor
     */
    public CommandExecutor getCommandExecutor(String command, String charset) {
        try {
            return new CommandExecutor((ChannelExec) session.openChannel("exec"), command, charset);
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    /**
     * 获取 CommandExecutor
     *
     * @param command 命令
     * @return CommandExecutor
     */
    public CommandExecutor getCommandExecutor(byte[] command) {
        try {
            return new CommandExecutor((ChannelExec) session.openChannel("exec"), command);
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    /**
     * 获取 ShellExecutor
     *
     * @return ShellExecutor
     */
    public ShellExecutor getShellExecutor() {
        try {
            return new ShellExecutor((ChannelShell) session.openChannel("shell"));
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    /**
     * 获取 SftpExecutor
     *
     * @return SftpExecutor
     */
    public SftpExecutor getSftpExecutor() {
        try {
            return new SftpExecutor((ChannelSftp) session.openChannel("sftp"));
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    /**
     * 获取 SftpExecutor
     *
     * @param fileNameEncoding 文件名称编码
     * @return SftpExecutor
     */
    public SftpExecutor getSftpExecutor(String fileNameEncoding) {
        try {
            return new SftpExecutor((ChannelSftp) session.openChannel("sftp"), fileNameEncoding);
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    /**
     * 获取文件上传器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    public SftpUpload getSftpUpload(String remote, File local) {
        try {
            return new SftpUpload((ChannelSftp) session.openChannel("sftp"), remote, local);
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    /**
     * 获取文件上传器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    public SftpUpload getSftpUpload(String remote, String local) {
        try {
            return new SftpUpload((ChannelSftp) session.openChannel("sftp"), remote, local);
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    /**
     * 获取文件下载器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    public SftpDownload getSftpDownload(String remote, File local) {
        try {
            return new SftpDownload((ChannelSftp) session.openChannel("sftp"), remote, local);
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    /**
     * 获取文件下载器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件下载器
     */
    public SftpDownload getSftpDownload(String remote, String local) {
        try {
            return new SftpDownload((ChannelSftp) session.openChannel("sftp"), remote, local);
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    /**
     * 获取session
     *
     * @return Session
     */
    public Session getSession() {
        return session;
    }

}
