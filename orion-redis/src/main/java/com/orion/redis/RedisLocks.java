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
package com.orion.redis;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Strings;
import redis.clients.jedis.Jedis;

/**
 * redisLock
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/8 15:03
 */
public class RedisLocks {

    private final Jedis jedis;

    /**
     * 默认锁过期时间
     */
    private static final long EXPIRED = Const.MS_S_5;

    public RedisLocks(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 尝试获取分布式锁
     * setnx如果key存在就会做任何操作 不存在就会set
     *
     * @param lock 锁名称
     * @return 锁的值 0没抢到锁
     */
    public long tryLock(String lock) {
        return this.tryLock(lock, EXPIRED);
    }

    /**
     * 尝试获取分布式锁
     *
     * @param lock    锁名称
     * @param expired 锁过期时间
     * @return 锁的值 0没抢到锁
     */
    public long tryLock(String lock, long expired) {
        try {
            long lockValue = System.currentTimeMillis() + expired;
            Long r = jedis.setnx(lock, String.valueOf(lockValue));
            if (r == 1) {
                return lockValue;
            } else {
                long oldLockValue = Long.parseLong(jedis.get(lock));
                if (oldLockValue < System.currentTimeMillis()) {
                    String getOldLockValue = jedis.getSet(lock, String.valueOf(lockValue));
                    if (!Strings.isBlank(getOldLockValue) && Long.valueOf(getOldLockValue).equals(oldLockValue)) {
                        return lockValue;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 释放分布式锁
     * 锁超时时不能直接del 如果在del之前有其他线程获得了锁, 那么可能造成锁的释放
     *
     * @param lockName  锁名称
     * @param lockValue 锁的值
     */
    public void unLock(String lockName, Long lockValue) {
        try {
            // 先获取锁
            String currentLockValue = jedis.get(lockName);
            if (!Strings.isBlank(currentLockValue) && lockValue.equals(Long.valueOf(currentLockValue))) {
                // 删除key
                jedis.del(lockName);
            }
        } catch (Exception e) {
            // ignore
        }
    }

}
