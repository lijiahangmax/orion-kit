/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.id;

import cn.orionsec.kit.lang.able.IdGenerator;
import cn.orionsec.kit.lang.define.SystemClock;
import cn.orionsec.kit.lang.utils.Exceptions;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 分布式有序 id 生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/7/11 17:17
 */
public class SequenceIdWorker implements IdGenerator<Long> {

    /**
     * 起始时间戳
     */
    private final static long START_TIME = 7L;

    /**
     * dataCenterId占用的位数: 2
     */
    private final static long DATA_CENTER_ID_BITS = 2L;

    /**
     * workerId占用的位数: 8
     **/
    private final static long WORKER_ID_BITS = 8L;

    /**
     * 序列号占用的位数: 12 (表示只允许workId的范围为: 0 - 4095)
     */
    private final static long SEQUENCE_BITS = 12L;

    /**
     * workerId可以使用范围: 0 - 255
     **/
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /**
     * dataCenterId可以使用范围: 0 - 3
     */
    private final static long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);

    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;

    private final static long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    /**
     * 用 mask 防止溢出: 位与运算保证计算的结果范围始终是 0 - 4095
     **/
    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private final long workerId;

    private final long dataCenterId;

    private long sequence;

    private long lastTimestamp = -1L;

    private final boolean clock;

    private final long timeOffset;

    private final boolean randomSequence;

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public SequenceIdWorker(long dataCenterId, long workerId) {
        this(dataCenterId, workerId, false, 5L, false);
    }

    public SequenceIdWorker(long dataCenterId, long workerId, boolean clock, boolean randomSequence) {
        this(dataCenterId, workerId, clock, 5L, randomSequence);
    }

    /**
     * 基于Snowflake创建分布式ID生成器
     *
     * @param dataCenterId   数据中心ID,数据范围为0~255
     * @param workerId       工作机器ID,数据范围为0~3
     * @param clock          true表示解决高并发下获取时间戳的性能问题
     * @param timeOffset     允许时间回拨的毫秒量,建议5ms
     * @param randomSequence true表示使用毫秒内的随机序列(超过范围则取余)
     */
    public SequenceIdWorker(long dataCenterId, long workerId, boolean clock, long timeOffset, boolean randomSequence) {
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw Exceptions.argument("data center id can't be greater than " + MAX_DATA_CENTER_ID + " or less than 0");
        }
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw Exceptions.argument("worker id can't be greater than " + MAX_WORKER_ID + " or less than 0");
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        this.clock = clock;
        this.timeOffset = timeOffset;
        this.randomSequence = randomSequence;
    }

    @Override
    public synchronized Long nextId() {
        long currentTimestamp = this.timeGen();
        // 闰秒: 如果当前时间小于上一次ID生成的时间戳, 说明系统时钟回退过, 这个时候应当抛出异常
        if (currentTimestamp < lastTimestamp) {
            // 校验时间偏移回拨量
            long offset = lastTimestamp - currentTimestamp;
            if (offset > timeOffset) {
                throw Exceptions.runtime("clock moved backwards, refusing to generate id for [" + offset + "ms]");
            }
            try {
                // 时间回退timeOffset毫秒内, 则允许等待2倍的偏移量后重新获取, 解决小范围的时间回拨问题
                this.wait(offset << 1);
            } catch (Exception e) {
                throw Exceptions.runtime(e);
            }
            // 再次获取
            currentTimestamp = this.timeGen();
            // 再次校验
            if (currentTimestamp < lastTimestamp) {
                throw Exceptions.runtime("clock moved backwards, refusing to generate id for [" + offset + "ms]");
            }
        }

        // 同一毫秒内序列直接自增
        if (lastTimestamp == currentTimestamp) {
            // randomSequence为true表示随机生成允许范围内的序列起始值并取余数,否则毫秒内起始值为0L开始自增
            long tempSequence = sequence + 1;
            if (randomSequence && tempSequence > SEQUENCE_MASK) {
                tempSequence = tempSequence % SEQUENCE_MASK;
            }
            // 通过位与运算保证计算的结果范围始终是 0-4095
            this.sequence = tempSequence & SEQUENCE_MASK;
            if (sequence == 0) {
                currentTimestamp = this.tilNextMillis(lastTimestamp);
            }
        } else {
            // randomSequence为true表示随机生成允许范围内的序列起始值,否则毫秒内起始值为0L开始自增
            this.sequence = randomSequence ? random.nextLong(SEQUENCE_MASK + 1) : 0L;
        }
        this.lastTimestamp = currentTimestamp;
        long currentOffsetTime = currentTimestamp - START_TIME;
        /*
         * 1.左移运算是为了将数值移动到对应的段(41、5、5, 12那段因为本来就在最右, 因此不用左移)
         * 2.然后对每个左移后的值(la、lb、lc、sequence)做位或运算, 是为了把各个短的数据合并起来, 合并成一个二进制数
         * 3.最后转换成10进制, 就是最终生成的id
         */
        return (currentOffsetTime << TIMESTAMP_LEFT_SHIFT) |
                // 数据中心位
                (dataCenterId << DATA_CENTER_ID_SHIFT) |
                // 工作ID位
                (workerId << WORKER_ID_SHIFT) |
                // 毫秒序列化位
                sequence;
    }

    /**
     * 保证返回的毫秒数在参数之后(阻塞到下一个毫秒, 直到获得新的时间戳) CAS
     *
     * @param lastTimestamp last timestamp
     * @return next millis
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            // 如果发现时间回拨, 则自动重新获取 (可能会处于无限循环中)
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    /**
     * 获得系统当前毫秒时间戳
     *
     * @return timestamp 毫秒时间戳
     */
    private long timeGen() {
        return clock ? SystemClock.now() : System.currentTimeMillis();
    }

}
