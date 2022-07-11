package com.orion.lang.id;

import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Systems;
import com.orion.lang.utils.random.Randoms;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * 雪花算法 id 生成工具
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/5/29 14:07
 */
public class SnowFlakes {

    private static final SnowFlakeIdWorker ID_WORKER;

    static {
        ID_WORKER = new SnowFlakeIdWorker(getWorkId(), getDataCenterId());
    }

    /**
     * 获取id
     *
     * @return id
     */
    public static Long nextId() {
        return ID_WORKER.nextId();
    }

    /**
     * 获取工作机器id
     *
     * @return 工作机器id
     */
    private static Long getWorkId() {
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] code = Strings.getCodePoints(hostAddress);
            int sums = 0;
            for (int b : code) {
                sums += b;
            }
            return (long) (sums % 32);
        } catch (UnknownHostException e) {
            // 如果获取失败, 则使用随机数备用
            return (long) Randoms.randomInt(0, 31);
        }
    }

    /**
     * 通过 hostName 获取机器id
     *
     * @return 机器id
     */
    private static Long getDataCenterId() {
        int[] cps = Strings.getCodePoints(Systems.HOST_NAME);
        int sums = 0;
        for (int i : cps) {
            sums += i;
        }
        return (long) (sums % 32);
    }

}

