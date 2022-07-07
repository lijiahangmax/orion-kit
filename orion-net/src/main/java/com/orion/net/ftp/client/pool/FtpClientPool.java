package com.orion.net.ftp.client.pool;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Streams;
import com.orion.net.ftp.client.Ftps;
import com.orion.net.ftp.client.config.FtpConfig;
import com.orion.net.ftp.client.instance.FtpInstance;
import com.orion.net.ftp.client.instance.IFtpInstance;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * FTP连接池
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/18 11:19
 */
public class FtpClientPool implements AutoCloseable {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpClientPool.class);

    /**
     * 如果池中连接不足是否创建而不是阻塞后报错
     */
    private boolean noAvailableThenCreate;

    /**
     * 入栈超时时间 ms
     */
    private int timeout;

    /**
     * 队列
     */
    private final BlockingQueue<FTPClient> pool;

    /**
     * 连接工厂
     */
    private final FtpClientFactory factory;

    /**
     * 长连接心跳监听
     */
    private FtpClientKeepAlive keepAlive;

    public FtpClientPool(FtpClientFactory factory) {
        this(factory, Const.N_10);
    }

    public FtpClientPool(FtpClientFactory factory, int size) {
        Valid.notNull(factory, "ftp client factory is null");
        this.timeout = Const.MS_S_5;
        this.noAvailableThenCreate = false;
        this.factory = factory;
        this.pool = new ArrayBlockingQueue<>(size);
        this.init(size);
    }

    /**
     * 初始化
     *
     * @param maxPoolSize 池内数量
     */
    private void init(int maxPoolSize) {
        try {
            for (int i = 0; i < maxPoolSize; i++) {
                this.addClient();
            }
        } catch (Exception e) {
            throw Exceptions.ftp("init ftp client to pool error", e);
        }
    }

    /**
     * 入栈超时 ms
     *
     * @param timeout timeout ms
     * @return this
     */
    public FtpClientPool timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 如果池中连接不足是否创建而不是阻塞后报错
     *
     * @param noAvailableThenCreate 是否创建
     * @return this
     */
    public FtpClientPool noAvailableThenCreate(boolean noAvailableThenCreate) {
        this.noAvailableThenCreate = noAvailableThenCreate;
        return this;
    }

    /**
     * 监听 keepAlive 的连接是否超时
     *
     * @return this
     */
    public FtpClientPool keepAliveListener() {
        // keepAlive 监听
        this.keepAlive = new FtpClientKeepAlive(this);
        keepAlive.listener(factory.getConfig().getConnTimeout() / 2);
        return this;
    }

    /**
     * 获取一个连接
     *
     * @return 连接
     */
    public synchronized FTPClient getClient() {
        LOGGER.debug("get ftp client with pool");
        try {
            if (pool.size() == 0 && noAvailableThenCreate) {
                LOGGER.debug("there are no free ftp connections in the pool, used create temp client");
                return factory.createClient();
            }
            FTPClient client = pool.poll(timeout, TimeUnit.MILLISECONDS);
            if (client == null) {
                LOGGER.error("cannot get ftp client with pool, not have more free ftp connection");
                throw Exceptions.ftp("cannot get ftp client with pool, not have more free ftp connection");
            }
            // 不存活
            if (!Ftps.isActive(client)) {
                this.invalidClient(client);
                this.addClient();
                return this.getClient();
            }
            return client;
        } catch (InterruptedException e) {
            LOGGER.error("get ftp client with pool error", e);
            throw Exceptions.ftp("get ftp client with pool error", e);
        }
    }

    /**
     * 获取一个实例
     *
     * @return this
     */
    public synchronized IFtpInstance getInstance() {
        return new FtpInstance(this);
    }

    /**
     * 归还一个连接, 如果归还超时则销毁改对象
     *
     * @param client 客户端
     */
    public synchronized void returnClient(FTPClient client) {
        try {
            Valid.notNull(client, "return client is null");
            LOGGER.debug("return ftp client with pool");
            if (!pool.offer(client, timeout, TimeUnit.MILLISECONDS)) {
                Ftps.destroy(client);
            }
        } catch (InterruptedException e) {
            LOGGER.error("return ftp client with to pool error", e);
        }
    }

    /**
     * 新建一个连接到池中
     */
    protected synchronized void addClient() {
        try {
            pool.offer(factory.createClient(), timeout, TimeUnit.MILLISECONDS);
            LOGGER.debug("add ftp client with pool");
        } catch (InterruptedException e) {
            LOGGER.error("cannot add a new connection to the pool", e);
        }
    }

    /**
     * 使客户端无效
     *
     * @param client 客户端
     */
    protected synchronized void invalidClient(FTPClient client) {
        LOGGER.debug("invalid ftp client with pool");
        pool.remove(client);
        Ftps.destroy(client);
    }

    /**
     * 关闭连接池 并且释放连接
     *
     * @throws InterruptedException 释放连接超时
     */
    @Override
    public void close() throws InterruptedException {
        LOGGER.debug("ftp client pool closing...");
        Streams.close(keepAlive);
        while (pool.iterator().hasNext()) {
            FTPClient client = pool.take();
            Ftps.destroy(client);
        }
    }

    /**
     * 获取连接池
     *
     * @return 连接池
     */
    protected BlockingQueue<FTPClient> getPool() {
        return pool;
    }

    public boolean isNoAvailableThenCreate() {
        return noAvailableThenCreate;
    }

    public FtpClientFactory getFactory() {
        return factory;
    }

    /**
     * 获取 FTP 配置文件
     *
     * @return FtpConfig
     */
    public FtpConfig getConfig() {
        return factory.getConfig();
    }

    /**
     * 获取空闲的连接数量
     *
     * @return 数量
     */
    public int getFreeSize() {
        return pool.size();
    }

}
