package com.orion.net.ftp.server;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;
import org.apache.ftpserver.ConnectionConfig;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.impl.DefaultConnectionConfig;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfiguration;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Ftp Server 实例
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/8/14 10:08
 */
public class FtpServerInstance {

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
     * 连接配置
     */
    private FtpServerConfig serverConfig;

    /**
     * ssl配置
     */
    private FtpServerSslConfig sslConfig;

    /**
     * 用户
     */
    private List<User> initUsers;

    /**
     * 监听主机
     */
    private String host;

    /**
     * 监听端口
     */
    private int port;

    /**
     * 监听最大空闲时间
     */
    private int idleTimeout;

    public FtpServerInstance() {
        this(Const.LOCALHOST_IP_V4, 21);
    }

    public FtpServerInstance(String host) {
        this(host, 21);
    }

    public FtpServerInstance(int port) {
        this(Const.LOCALHOST_IP_V4, port);
    }

    public FtpServerInstance(String host, int port) {
        this.host = host;
        this.port = port;
        this.idleTimeout = 300;
        this.serverConfig = new FtpServerConfig();
        this.initUsers = new ArrayList<>();
    }

    public static FtpServerInstance newInstance() {
        return new FtpServerInstance();
    }

    public static FtpServerInstance newInstance(int port) {
        return new FtpServerInstance(port);
    }

    // -------------------- listener --------------------

    /**
     * 设置监听主机
     *
     * @param hostName hostName
     * @return this
     */
    public FtpServerInstance host(String hostName) {
        this.host = hostName;
        return this;
    }

    /**
     * 设置监听端口
     *
     * @param port port
     * @return this
     */
    public FtpServerInstance port(int port) {
        this.port = port;
        return this;
    }

    /**
     * 设置监听空闲超时时间
     *
     * @param timeout timeout
     * @return this
     */
    public FtpServerInstance idleTimeout(int timeout) {
        this.idleTimeout = timeout;
        return this;
    }

    /**
     * 设置连接配置信息
     *
     * @param serverConfig serverConfig
     * @return this
     */
    public FtpServerInstance serverConfig(FtpServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        return this;
    }

    /**
     * 设置ssl配置信息
     *
     * @param sslConfig sslConfig
     * @return this
     */
    public FtpServerInstance sslConfig(FtpServerSslConfig sslConfig) {
        this.sslConfig = sslConfig;
        return this;
    }

    /**
     * FtpServerConfig ->  ConnectionConfig
     *
     * @return ConnectionConfig
     */
    private ConnectionConfig convertConnectionConfig() {
        return new DefaultConnectionConfig(serverConfig.isAnonymousLogin(), serverConfig.getLoginFailureDelay(), serverConfig.getMaxLogin(),
                serverConfig.getMaxAnonymousLogin(), serverConfig.getMaxLoginFailures(), serverConfig.getMaxThreads());
    }

    /**
     * FtpServerSslConfig  -> SslConfiguration
     *
     * @return SslConfiguration
     */
    private SslConfiguration convertSslConfig() {
        SslConfigurationFactory ssl = new SslConfigurationFactory();
        File keyStoreFile = Valid.notNull(sslConfig.getKeyStoreFile(), "key store file is null");
        String keyStorePassword = Valid.notNull(sslConfig.getKeyStorePassword(), "key store password is null");
        ssl.setKeystoreFile(keyStoreFile);
        ssl.setKeystorePassword(keyStorePassword);
        String sslProtocol = sslConfig.getSslProtocol();
        if (sslProtocol != null) {
            ssl.setSslProtocol(sslProtocol);
        }
        String keyStoreAlgorithm = sslConfig.getKeyStoreAlgorithm();
        if (keyStoreAlgorithm != null) {
            ssl.setKeystoreAlgorithm(keyStoreAlgorithm);
        }
        String keyAlias = sslConfig.getKeyAlias();
        if (keyAlias != null) {
            ssl.setKeyAlias(keyAlias);
        }
        return ssl.createSslConfiguration();
    }

    // -------------------- user --------------------

    /**
     * 添加用户
     *
     * @param user user
     * @return this
     */
    public FtpServerInstance addUser(FtpUser user) {
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
     * @return this
     */
    public FtpServerInstance addUser(String username, String password) {
        return addUser(username, password, "/home/" + username);
    }

    /**
     * 添加用户
     *
     * @param username 用户名
     * @param password 密码
     * @param homePath 根目录
     * @return this
     */
    public FtpServerInstance addUser(String username, String password, String homePath) {
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
    public FtpServerInstance addUsers(Collection<FtpUser> users) {
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
     * 删除用户
     *
     * @param user user
     * @return this
     */
    public FtpServerInstance deleteUser(String user) {
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
    public FtpServerInstance deleteUsers(Collection<String> users) {
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
    public boolean userExists(String user) {
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

    // -------------------- server --------------------

    /**
     * 监听
     *
     * @return this
     */
    public FtpServerInstance listener() {
        serverFactory = new FtpServerFactory();
        serverFactory.setConnectionConfig(this.convertConnectionConfig());
        factory = new ListenerFactory();
        if (sslConfig != null) {
            factory.setSslConfiguration(this.convertSslConfig());
        }
        factory.setPort(port);
        if (host != null) {
            factory.setServerAddress(host);
        }
        factory.setIdleTimeout(idleTimeout);
        Listener listener = factory.createListener();
        serverFactory.addListener(Const.DEFAULT, listener);
        userManager = serverFactory.getUserManager();
        for (User initUser : initUsers) {
            try {
                userManager.save(initUser);
            } catch (Exception e) {
                throw Exceptions.ftp(e);
            }
        }
        this.ftpServer = serverFactory.createServer();
        return this;
    }

    /**
     * 启动
     *
     * @return this
     */
    public FtpServerInstance start() {
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
    public FtpServerInstance stop() {
        ftpServer.stop();
        return this;
    }

    /**
     * 暂停
     *
     * @return this
     */
    public FtpServerInstance suspend() {
        ftpServer.suspend();
        return this;
    }

    /**
     * 暂停恢复
     *
     * @return this
     */
    public FtpServerInstance resume() {
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

    // -------------------- getter --------------------

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

    public FtpServerConfig getServerConfig() {
        return serverConfig;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

}
