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
    public void testCheckout() throws Exception {
        gits.checkout("master").pull().close();
    }

    @Test
    public void reset() throws Exception {
        gits.checkout("develop").reset("482a5dbcec5607abd5f8cd2ab1adac323616f2bd").close();
    }

    @Test
    public void log() throws Exception {
        gits.logList("develop").forEach(System.out::println);
    }

    @Test
    public void branchList() throws Exception {
        gits.branchList().forEach(System.out::println);
    }

    @Test
    public void clean() throws Exception {
        gits.clean().close();
    }

}
