package com.orion.test.vcs;

import com.orion.vcs.git.Gits;
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
        Gits.clone("https://gitee.com/lijiahangHome/tmp1.git", new File("C:\\Users\\ljh15\\Desktop\\1"));
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
