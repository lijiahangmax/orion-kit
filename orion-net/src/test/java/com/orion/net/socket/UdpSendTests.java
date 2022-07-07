package com.orion.net.socket;

import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Threads;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/10 11:16
 */
public class UdpSendTests {

    public static void main(String[] args) throws Exception {
        UdpSend send = new UdpSend("127.0.0.1", 7791);
        while (true) {
            String s = Strings.randomChars(5);
            send.send(s.getBytes());
            send.sendLf();
            System.out.println("send: " + s);
            Threads.sleep(1500);
        }
    }

}
