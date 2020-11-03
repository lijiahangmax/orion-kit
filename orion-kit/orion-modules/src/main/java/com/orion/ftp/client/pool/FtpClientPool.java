package com.orion.ftp.client.pool;

import com.orion.utils.Exceptions;
import org.apache.commons.net.ftp.FTPClient;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * FTP连接池
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/18 11:19
 */
public class FtpClientPool {

    /**
     * 入栈超时秒
     */
    private static final int DEFAULT_TIMEOUT = 5;

    /**
     * 默认池内连接数量
     */
    private static final int DEFAULT_POOL_SIZE = 12;

    /**
     * 队列
     */
    private BlockingQueue<FTPClient> pool;

    /**
     * 连接工厂
     */
    private FtpClientFactory factory;

    /**
     * 长连接心跳监听
     */
    private FtpClientKeepAlive keepAlive;

    public FtpClientPool(FtpClientFactory factory) {
        this(factory, DEFAULT_POOL_SIZE);
    }

    public FtpClientPool(FtpClientFactory factory, int size) {
        this.factory = factory;
        this.pool = new ArrayBlockingQueue<>(size);
        init(size);
    }

    /**
     * 初始化
     *
     * @param maxPoolSize 池内数量
     */
    private void init(int maxPoolSize) {
        try {
            for (int i = 0; i < maxPoolSize; i++) {
                addClient();
            }
        } catch (Exception e) {
            Exceptions.printStacks(e);
        }

    }

    /**
     * 监听keepAlive的连接是否超时
     *
     * @return this
     */
    public FtpClientPool keepAliveListener() {
        // keepAlive 监听
        keepAlive = new FtpClientKeepAlive(this);
        keepAlive.listener(factory.getConfig().getConnTimeout() / 2);
        return this;
    }

    /**
     * 获取一个连接
     *
     * @return 连接
     */
    public synchronized FTPClient getClient() {
        try {
            FTPClient client = pool.take();
            if (!factory.isActive(client)) {
                invalidateClient(client);
                client = factory.getClient();
                addClient();
            }
            return client;
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 归还一个对象, 如果10秒内无法归还, 则销毁改对象
     *
     * @param client 客户端
     */
    public synchronized void returnClient(FTPClient client) {
        try {
            if (client != null) {
                if (!pool.offer(client, DEFAULT_TIMEOUT, TimeUnit.SECONDS)) {
                    factory.destroy(client);
                }
            }
        } catch (Exception e) {
            Exceptions.printStacks(e);
        }
    }

    /**
     * 新建一个连接
     */
    synchronized void addClient() {
        try {
            pool.offer(factory.getClient(), DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            Exceptions.printStacks(e);
        }
    }

    /**
     * 使客户端无效
     *
     * @param client 客户端
     */
    synchronized void invalidateClient(FTPClient client) {
        pool.remove(client);
        factory.destroy(client);
    }

    /**
     * 关闭连接池
     *
     * @throws InterruptedException e
     */
    public void close() throws InterruptedException {
        while (pool.iterator().hasNext()) {
            FTPClient client = pool.take();
            factory.destroy(client);
        }
        keepAlive.stop();
    }

    /**
     * 获取连接池
     *
     * @return 连接池
     */
    BlockingQueue<FTPClient> getPool() {
        return pool;
    }

    /**
     * 获取客户端工厂
     *
     * @return 客户端工厂
     */
    public FtpClientFactory getFactory() {
        return factory;
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
