package com.orion.test;

import com.orion.utils.QRCodes;
import org.junit.Test;

import java.io.File;

/**
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/14 13:04
 */
public class QRTests {

    @Test
    public void encode() throws Exception {
        QRCodes.encode("test1", "C:\\Users\\ljh15\\Desktop\\tmp", "axc1");
        QRCodes.encode("test2", "C:\\Users\\ljh15\\Desktop\\tmp", "axc2", "C:\\Users\\ljh15\\Desktop\\tmp\\1.jpg", false);
        QRCodes.encode("test3", "C:\\Users\\ljh15\\Desktop\\tmp", "axc3", "C:\\Users\\ljh15\\Desktop\\tmp\\1.jpg", true);
        System.out.println(QRCodes.encode("test4", "C:\\Users\\ljh15\\Desktop\\tmp"));
    }

    @Test
    public void decode() throws Exception {
        System.out.println(QRCodes.decode(new File("C:\\Users\\ljh15\\Desktop\\tmp\\axc1.jpg")));
        System.out.println(QRCodes.decode(new File("C:\\Users\\ljh15\\Desktop\\tmp\\axc2.jpg")));
        System.out.println(QRCodes.decode(new File("C:\\Users\\ljh15\\Desktop\\tmp\\axc3.jpg")));
        System.out.println(QRCodes.decode(new File("C:\\Users\\ljh15\\Desktop\\tmp\\axc4.jpg")));
        System.out.println(QRCodes.decode(new File("C:\\Users\\ljh15\\Desktop\\tmp\\1.jpg")));
    }

}
