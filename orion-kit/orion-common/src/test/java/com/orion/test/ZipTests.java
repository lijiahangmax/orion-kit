package com.orion.test;

import com.orion.utils.Zips;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/14 13:39
 */
public class ZipTests {

    @Test
    public void zip() {
        System.out.println(Zips.doCompress("C:\\Users\\ljh15\\Desktop\\tmp", "zip"));
        System.out.println(Zips.doCompress("C:\\Users\\ljh15\\Desktop\\tmp", "gz"));
    }

    @Test
    public void unzip() {
        System.out.println(Zips.unCompress("C:\\Users\\ljh15\\Desktop\\tmp.zip"));
    }

}
