package com.csy.summary.daily.distributedlock;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @author csy
 */
public interface DistributedLocker {
    /**
     * 上锁，返回锁对象
     *
     * @param lockKey 锁key
     * @return RLock
     */
    RLock lock(String lockKey);

    /**
     * 上锁，指定超时时间，返回锁对象
     *
     * @param lockKey 锁key
     * @param timeout 超时时间
     * @return RLock
     */
    RLock lock(String lockKey, int timeout);

    /**
     * 上锁，指定超时时间及时间单位，返回锁对象
     *
     * @param lockKey 锁key
     * @param unit    时间单位
     * @param timeout 超时时间
     * @return RLock
     */
    RLock lock(String lockKey, TimeUnit unit, int timeout);

    /**
     * 尝试上锁，指定超时时间及时间单位，返回锁对象
     *
     * @param lockKey   锁key
     * @param unit      时间单位
     * @param waitTime  锁等待时间
     * @param leaseTime 加锁后自动释放时间
     * @return boolean
     */
    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    /**
     * 指定key解锁
     *
     * @param lockKey 锁key
     */
    void unlock(String lockKey);

    /**
     * 指定锁对象解锁
     *
     * @param lock 锁对象
     */
    void unlock(RLock lock);

}