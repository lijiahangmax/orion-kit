package com.orion.id;

import com.orion.utils.Systems;
import com.orion.utils.random.Randoms;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MongoDB ID生成策略实现(线程安全)
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/15 18:09
 */
public class ObjectIds {

    private ObjectIds() {
    }

    private static final AtomicInteger NEXT_SEQ_INCR = new AtomicInteger(Randoms.randomInt());

    private static final int MACHINE = Systems.getMachineCode();
    // private static final int MACHINE = Systems.getMachineCode() | Systems.getProcessCode();

    /**
     * 判断objectId是否有效
     *
     * @param s 字符串
     * @return ignore
     */
    public static boolean isValid(String s) {
        if (s == null) {
            return false;
        }
        s = s.replaceAll("-", "");
        int len = s.length();
        if (len != 24) {
            return false;
        }

        char c;
        for (int i = 0; i < len; i++) {
            c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                continue;
            }
            if (c >= 'a' && c <= 'f') {
                continue;
            }
            if (c >= 'A' && c <= 'F') {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * 获取一个objectId
     *
     * @return objectId
     */
    public static byte[] nextBytes() {
        ByteBuffer bb = ByteBuffer.wrap(new byte[12]);
        bb.putInt((int) System.currentTimeMillis() / 1000);
        bb.putInt(MACHINE);
        bb.putInt(NEXT_SEQ_INCR.getAndIncrement());
        return bb.array();
    }

    /**
     * 获取一个objectId用下划线分割
     *
     * @return objectId
     */
    public static String next() {
        return next(false);
    }

    /**
     * 获取一个objectId
     *
     * @param symbol 是否包含分隔符
     * @return objectId
     */
    public static String next(boolean symbol) {
        byte[] array = nextBytes();
        StringBuilder buf = new StringBuilder(symbol ? 26 : 24);
        int t;
        for (int i = 0; i < array.length; i++) {
            if (symbol && i % 4 == 0 && i != 0) {
                buf.append("-");
            }
            t = array[i] & 0xff;
            if (t < 16) {
                buf.append('0');
            }
            buf.append(Integer.toHexString(t));

        }
        return buf.toString();
    }

    /**
     * 获取机器唯一信息
     *
     * @return 机器码
     */
    public static int getMachine() {
        return MACHINE;
    }

}
