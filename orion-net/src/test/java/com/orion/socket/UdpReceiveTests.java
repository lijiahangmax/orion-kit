package com.orion.socket;

import java.io.IOException;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/10 11:10
 */
public class UdpReceiveTests {

    public static void main(String[] args) throws IOException {
        UdpReceive receive = new UdpReceive(7791);
        byte[] bytes = new byte[1024];
        while (true) {
            int len = receive.receive(bytes);
            System.out.print(new String(bytes, 0, len));
        }
    }

}
