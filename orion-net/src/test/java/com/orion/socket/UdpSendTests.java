package com.orion.socket;

import com.orion.utils.Strings;
import com.orion.utils.Threads;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/10 11:16
 */
public class UdpSendTests {

    public static void main(String[] args) throws Exception {
        UdpSend send = new UdpSend("127.0.0.1", 7791);
        while (true) {
            String s = Strings.randomChars(5);
            send.send(s.getBytes());
            send.sendLF();
            System.out.println("send: " + s);
            Threads.sleep(1500);
        }
    }

}
