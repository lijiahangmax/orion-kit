package com.orion.ftp.clint;

import com.orion.ftp.client.FtpInstance;
import com.orion.ftp.client.config.FtpConfig;
import com.orion.ftp.client.config.FtpsConfig;
import com.orion.ftp.client.pool.FtpClientFactory;
import com.orion.ftp.client.pool.FtpClientPool;
import com.orion.utils.Threads;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/13 1:43
 */
public class FtpPoolTests {

    private FtpClientPool pool;

    @Before
    public void init() {
        FtpConfig config = new FtpsConfig("127.0.0.1").auth("user", "123");
        FtpClientFactory f = new FtpClientFactory(config);
        pool = new FtpClientPool(f, 3);
    }

    @Test
    public void get1() {
        pool.emptyCreateTemp(true);
        System.out.println(pool.getClient());
        System.out.println(pool.getClient());
        System.out.println(pool.getClient());
        System.out.println(pool.getClient());
    }

    @Test
    public void get2() {
        System.out.println(pool.getClient());
        System.out.println(pool.getClient());
        System.out.println(pool.getClient());
        System.out.println(pool.getClient());
    }

    @Test
    public void get3() {
        FtpInstance i;
        i = pool.getInstance();
        System.out.println(i.client());
        i.close();
        i = pool.getInstance();
        System.out.println(i.client());
        i.close();
        i = pool.getInstance();
        System.out.println(i.client());
        i.close();
        Threads.sleep(1000);

        i = pool.getInstance();
        System.out.println(i.client());
        i.close();
        i = pool.getInstance();
        System.out.println(i.client());
        i.close();
        i = pool.getInstance();
        System.out.println(i.client());
        i.close();
        i = pool.getInstance();
        System.out.println(i.client());
    }

    @Test
    public void get4() {
        FtpInstance i;
        i = pool.getInstance();
        System.out.println(i.client());
        i.close();
        i = pool.getInstance();
        System.out.println(i.client());
        i.close();
        i = pool.getInstance();
        System.out.println(i.client());
        i.close();
        Threads.sleep(1000);

        i = pool.getInstance();
        System.out.println(i.client());
        i = pool.getInstance();
        System.out.println(i.client());
        i = pool.getInstance();
        System.out.println(i.client());
        i = pool.getInstance();
        System.out.println(i.client());
    }

    @After
    public void destroy() throws InterruptedException {
        pool.close();
    }

}
