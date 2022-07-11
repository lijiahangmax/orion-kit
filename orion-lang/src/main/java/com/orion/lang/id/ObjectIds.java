package com.orion.lang.id;

import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Systems;

/**
 * MongoDB id 生成工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/15 18:09
 */
public class ObjectIds {

    private static final int MACHINE_CODE;

    private static final ObjectIdWorker ID_WORKER;

    private ObjectIds() {
    }

    static {
        MACHINE_CODE = Systems.getMachineCode();
        ID_WORKER = new ObjectIdWorker(MACHINE_CODE);
    }

    /**
     * 判断 objectId 是否有效
     *
     * @param s 字符串
     * @return ignore
     */
    public static boolean isValid(String s) {
        if (s == null) {
            return false;
        }
        s = s.replaceAll("-", Strings.EMPTY);
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
     * 获取一个 objectId
     *
     * @return objectId
     */
    public static byte[] nextBytes() {
        return ID_WORKER.nextBytes();
    }

    /**
     * 获取一个 objectId (无下划线)
     *
     * @return objectId
     */
    public static String nextId() {
        return ID_WORKER.nextId();
    }

    /**
     * 获取一个 objectId
     *
     * @param symbol 是否包含分隔符
     * @return objectId
     */
    public static String nextId(boolean symbol) {
        return ID_WORKER.nextId(symbol);
    }

    /**
     * 获取机器唯一信息
     *
     * @return 机器码
     */
    public static int getMachineCode() {
        return MACHINE_CODE;
    }

}
