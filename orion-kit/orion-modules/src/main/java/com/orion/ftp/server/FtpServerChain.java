package com.orion.ftp.server;

import com.orion.utils.Exceptions;
import com.orion.utils.collect.Lists;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.util.ArrayList;
import java.util.List;

/**
 * Ftp ServerChain
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/8/14 10:08
 */
public class FtpServerChain {

    /**
     * 服务器工厂
     */
    private FtpServerFactory serverFactory;

    /**
     * 监听器工厂
     */
    private ListenerFactory factory;

    /**
     * 用户管理器
     */
    private UserManager userManager;

    /**
     * ftpServer 实例
     */
    private FtpServer ftpServer;

    /**
     * 监听主机
     */
    private String hostName;

    /**
     * 监听端口
     */
    private int port;

    /**
     * 监听最大空闲时间
     */
    private int idleTimeout;

    /**
     * 用户
     */
    private List<User> initUsers = new ArrayList<>();

    public FtpServerChain() {
    }

    public FtpServerChain(int port) {
        this.port = port;
    }

    public static FtpServerChain chain() {
        return new FtpServerChain();
    }

    public static FtpServerChain chain(int port) {
        return new FtpServerChain(port);
    }

    // ------------------------ listener ------------------------

    /**
     * 设置监听主机
     *
     * @param hostName hostName
     * @return this
     */
    public FtpServerChain host(String hostName) {
        this.hostName = hostName;
        return this;
    }

    /**
     * 设置监听端口
     *
     * @param port port
     * @return this
     */
    public FtpServerChain port(int port) {
        this.port = port;
        return this;
    }

    /**
     * 设置监听空闲超时时间
     *
     * @param timeout timeout
     * @return this
     */
    public FtpServerChain idleTimeout(int timeout) {
        this.idleTimeout = timeout;
        return this;
    }

    // ------------------------ user ------------------------

    /**
     * 添加用户
     *
     * @param user user
     * @return this
     */
    public FtpServerChain addUser(FtpUser user) {
        User u = this.convertUser(user);
        if (serverFactory == null) {
            initUsers.add(u);
            return this;
        }
        try {
            userManager.save(u);
            return this;
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 添加用户
     *
     * @param username 用户名
     * @param password 密码
     * @param homePath 根目录
     * @return this
     */
    public FtpServerChain addUser(String username, String password, String homePath) {
        User u = this.convertUser(new FtpUser(username, password, homePath));
        if (serverFactory == null) {
            initUsers.add(u);
            return this;
        }
        try {
            userManager.save(u);
            return this;
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 添加用户
     *
     * @param users users
     * @return this
     */
    public FtpServerChain addUsers(List<FtpUser> users) {
        for (FtpUser user : users) {
            User u = this.convertUser(user);
            if (serverFactory == null) {
                initUsers.add(u);
                continue;
            }
            try {
                userManager.save(u);
            } catch (Exception e) {
                throw Exceptions.ftp(e);
            }
        }
        return this;
    }

    /**
     * FtpUser -> User
     *
     * @param ftpUser ftpUser
     * @return user
     */
    private User convertUser(FtpUser ftpUser) {
        BaseUser user = new BaseUser();
        user.setName(ftpUser.getUsername());
        user.setPassword(ftpUser.getPassword());
        user.setHomeDirectory(ftpUser.getHomePath());
        user.setMaxIdleTime(ftpUser.getMaxIdleTime());
        List<Authority> authorities = new ArrayList<>();
        if (ftpUser.isWritePermission()) {
            authorities.add(new WritePermission());
        }
        if (ftpUser.getMaxUploadRate() != 0 || ftpUser.getMaxDownloadRate() != 0) {
            authorities.add(new TransferRatePermission(ftpUser.getMaxDownloadRate(), ftpUser.getMaxUploadRate()));
        }
        user.setAuthorities(authorities);
        return user;
    }

    /**
     * 删除用户
     *
     * @param user user
     * @return this
     */
    public FtpServerChain deleteUser(String user) {
        try {
            userManager.delete(user);
            return this;
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 删除用户
     *
     * @param users users
     * @return this
     */
    public FtpServerChain deleteUsers(List<String> users) {
        for (String user : users) {
            try {
                userManager.delete(user);
            } catch (Exception e) {
                throw Exceptions.ftp(e);
            }
        }
        return this;
    }

    /**
     * 判断用户是否存在
     *
     * @param user 用户名称
     * @return true存在
     */
    public boolean exists(String user) {
        try {
            return userManager.doesExist(user);
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 获取所有的用户名
     *
     * @return 用户名
     */
    public List<String> getUserNames() {
        try {
            String[] userNames = userManager.getAllUserNames();
            return Lists.of(userNames);
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    // ------------------------ server ------------------------

    /**
     * 监听
     *
     * @return this
     */
    public FtpServerChain listener() {
        serverFactory = new FtpServerFactory();
        factory = new ListenerFactory();
        if (hostName != null) {
            factory.setServerAddress(hostName);
        }
        if (port != 0) {
            factory.setPort(port);
        }
        if (idleTimeout != 0) {
            factory.setIdleTimeout(idleTimeout);
        }

        Listener listener = factory.createListener();
        serverFactory.addListener("default", listener);
        userManager = serverFactory.getUserManager();
        for (User initUser : initUsers) {
            try {
                userManager.save(initUser);
            } catch (Exception e) {
                throw Exceptions.ftp(e);
            }
        }
        ftpServer = serverFactory.createServer();
        return this;
    }

    /**
     * 启动
     *
     * @return this
     */
    public FtpServerChain start() {
        try {
            ftpServer.start();
            return this;
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 停止
     *
     * @return this
     */
    public FtpServerChain stop() {
        ftpServer.stop();
        return this;
    }

    /**
     * 暂停
     *
     * @return this
     */
    public FtpServerChain suspend() {
        ftpServer.suspend();
        return this;
    }

    /**
     * 暂停恢复
     *
     * @return this
     */
    public FtpServerChain resume() {
        ftpServer.resume();
        return this;
    }

    /**
     * 是否暂停
     *
     * @return true停止
     */
    public boolean isStopped() {
        return ftpServer.isStopped();
    }

    /**
     * 是否暂停
     *
     * @return true暂停
     */
    public boolean isSuspended() {
        return ftpServer.isSuspended();
    }

    public FtpServerFactory getServerFactory() {
        return serverFactory;
    }

    public ListenerFactory getFactory() {
        return factory;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public FtpServer getFtpServer() {
        return ftpServer;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

}
