/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.net.ftp.clint;

import cn.orionsec.kit.lang.utils.Threads;
import cn.orionsec.kit.net.ftp.client.config.FtpConfig;
import cn.orionsec.kit.net.ftp.client.instance.IFtpInstance;
import cn.orionsec.kit.net.ftp.client.pool.FtpClientFactory;
import cn.orionsec.kit.net.ftp.client.pool.FtpClientPool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/13 1:43
 */
public class FtpPoolTests {

    private FtpClientPool pool;

    @Before
    public void init() {
        FtpConfig config = new FtpConfig("127.0.0.1").auth("user", "123");
        FtpClientFactory f = new FtpClientFactory(config);
        this.pool = new FtpClientPool(f, 3);
    }

    @Test
    public void get1() {
        pool.noAvailableThenCreate(true);
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
        IFtpInstance i;
        i = pool.getInstance();
        System.out.println(i.getClient());
        i.close();
        i = pool.getInstance();
        System.out.println(i.getClient());
        i.close();
        i = pool.getInstance();
        System.out.println(i.getClient());
        i.close();
        Threads.sleep(1000);

        i = pool.getInstance();
        System.out.println(i.getClient());
        i.close();
        i = pool.getInstance();
        System.out.println(i.getClient());
        i.close();
        i = pool.getInstance();
        System.out.println(i.getClient());
        i.close();
        i = pool.getInstance();
        System.out.println(i.getClient());
    }

    @Test
    public void get4() {
        IFtpInstance i;
        i = pool.getInstance();
        System.out.println(i.getClient());
        i.close();
        i = pool.getInstance();
        System.out.println(i.getClient());
        i.close();
        i = pool.getInstance();
        System.out.println(i.getClient());
        i.close();
        Threads.sleep(1000);

        i = pool.getInstance();
        System.out.println(i.getClient());
        i = pool.getInstance();
        System.out.println(i.getClient());
        i = pool.getInstance();
        System.out.println(i.getClient());
        i = pool.getInstance();
        System.out.println(i.getClient());
    }

    @After
    public void destroy() throws InterruptedException {
        pool.close();
    }

}
