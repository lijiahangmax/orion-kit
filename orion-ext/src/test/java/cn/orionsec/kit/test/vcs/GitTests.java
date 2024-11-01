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
package cn.orionsec.kit.test.vcs;

import cn.orionsec.kit.ext.vcs.git.Gits;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/7/2 15:44
 */
public class GitTests {

    private Gits gits;

    @Before
    public void before() throws Exception {
        gits = Gits.of(new File("A:\\tmp\\git\\tmp"));
        System.out.println(gits.getBranch());
        System.out.println(gits.getRemoteUrl());
        System.out.println(gits.getDirectory());
    }

    @Test
    public void cloneTest() {
        Gits.clone("https://gitee.com/lijiahangmax/tmp1.git", new File("C:\\Users\\ljh15\\Desktop\\1"));
    }

    @Test
    public void testCheckout() {
        gits.checkout("master").pull().close();
    }

    @Test
    public void reset() {
        gits.checkout("develop").reset("482a5dbcec5607abd5f8cd2ab1adac323616f2bd").close();
    }

    @Test
    public void log() {
        gits.logList("dev1").forEach(System.out::println);
    }

    @Test
    public void branchList() {
        gits.fetch().branchList().forEach(System.out::println);
    }

    @Test
    public void clean() {
        gits.clean().close();
    }

}
