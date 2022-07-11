package com.orion.lang.id;

import com.orion.lang.able.IdGenerator;
import com.orion.lang.utils.random.Randoms;
import com.orion.lang.utils.time.Dates;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MongoDB 线程安全 id 生成机
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/7/11 16:14
 */
public class ObjectIdWorker implements IdGenerator<String> {

    /**
     * 自增序列
     */
    private final AtomicInteger seq;

    /**
     * 机器码
     */
    private final int machineCode;

    public ObjectIdWorker(int machineCode) {
        this.machineCode = machineCode;
        this.seq = new AtomicInteger(Randoms.randomInt());
    }

    /**
     * 获取一个 objectId
     *
     * @return objectId
     */
    public byte[] nextBytes() {
        ByteBuffer bb = ByteBuffer.wrap(new byte[12]);
        bb.putInt(((int) (System.currentTimeMillis() / Dates.SECOND_STAMP)));
        bb.putInt(machineCode);
        bb.putInt(seq.getAndIncrement());
        return bb.array();
    }

    /**
     * 获取一个 objectId (无下划线)
     *
     * @return objectId
     */
    @Override
    public String nextId() {
        return this.nextId(false);
    }

    /**
     * 获取一个 objectId
     *
     * @param symbol 是否包含分隔符
     * @return objectId
     */
    public String nextId(boolean symbol) {
        byte[] array = nextBytes();
        StringBuilder buf = new StringBuilder(symbol ? 26 : 24);
        int t;
        for (int i = 0; i < array.length; i++) {
            if (symbol && i % 4 == 0 && i != 0) {
                buf.append("-");
            }
            t = array[i] & 0xFF;
            if (t < 16) {
                buf.append('0');
            }
            buf.append(Integer.toHexString(t));

        }
        return buf.toString();
    }

    public int getMachineCode() {
        return machineCode;
    }

}
