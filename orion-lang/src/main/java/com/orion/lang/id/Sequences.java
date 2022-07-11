package com.orion.lang.id;

import com.orion.lang.utils.Exceptions;

import java.net.InetAddress;

/**
 * 分布式高效有序ID生产黑科技 (sequence)
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/07/23 19:15
 */
public class Sequences {

    private static byte LAST_IP;

    private static final SequenceIdWorker ID_WORKER;

    static {
        ID_WORKER = new SequenceIdWorker(getLastAddress(), 0x000000FF & getLastAddress());
    }

    /**
     * 生成id
     */
    public static Long nextId() {
        return ID_WORKER.nextId();
    }

    /**
     * 用 IP 地址最后几个字节标识
     * <p>
     * e.g: 192.168.1.30 -> 30
     *
     * @return last IP
     */
    private static byte getLastAddress() {
        if (LAST_IP != 0) {
            return LAST_IP;
        }
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            byte[] addressByte = inetAddress.getAddress();
            LAST_IP = addressByte[addressByte.length - 1];
        } catch (Exception e) {
            throw Exceptions.runtime("unknown host exception", e);
        }
        return (byte) Math.abs(LAST_IP % 3);
    }

}
