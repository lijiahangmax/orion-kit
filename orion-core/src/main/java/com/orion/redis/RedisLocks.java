package com.orion.redis;

import com.orion.utils.Strings;
import redis.clients.jedis.Jedis;

/**
 * redisLock
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/8 15:03
 */
public class RedisLocks {

    private static Jedis redisTemplate;

    private RedisLocks() {
    }

    /**
     * 默认锁过期时间
     */
    private static final long EXPIRED = 1000 * 5;

    public static void setClient(Jedis redisTemplate) {
        RedisLocks.redisTemplate = redisTemplate;
    }

    /**
     * 尝试获取分布式锁
     * setnx如果key存在就会做任何操作 不存在就会set
     *
     * @param lock 锁名称
     * @return 锁的值 0没抢到锁
     */
    public static long tryLock(String lock) {
        return tryLock(lock, EXPIRED);
    }

    /**
     * 尝试获取分布式锁
     *
     * @param lock    锁名称
     * @param expired 锁过期时间
     * @return 锁的值 0没抢到锁
     */
    public static long tryLock(String lock, long expired) {
        try {
            long lockValue = System.currentTimeMillis() + expired;
            Long r = redisTemplate.setnx(lock, String.valueOf(lockValue));
            if (r == 1) {
                return lockValue;
            } else {
                long oldLockValue = Long.parseLong(redisTemplate.get(lock));
                if (oldLockValue < System.currentTimeMillis()) {
                    String getOldLockValue = redisTemplate.getSet(lock, String.valueOf(lockValue));
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
    public static void unLock(String lockName, Long lockValue) {
        try {
            // 先获取锁
            String currentLockValue = redisTemplate.get(lockName);
            if (!Strings.isBlank(currentLockValue) && lockValue.equals(Long.valueOf(currentLockValue))) {
                // 删除key
                redisTemplate.del(lockName);
            }
        } catch (Exception e) {
            // ignore
        }
    }

}
