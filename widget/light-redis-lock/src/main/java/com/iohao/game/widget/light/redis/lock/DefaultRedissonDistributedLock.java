package com.iohao.game.widget.light.redis.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;


/**
 * 简单的基于Redisson分布式锁接口实现类
 * <pre>
 *     使用Redisson实现
 * </pre>
 *
 * @author shen
 * @date 2022-03-28
 * @Slogan 慢慢变好，是给自己最好的礼物
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultRedissonDistributedLock implements DistributedLock {

    private final RedissonClient redissonClient;

    @Override
    public <T> T tryLockAndExecute(String key, long waitTime, long leaseTime, TimeUnit unit, ReturnHandle<T> action) throws InterruptedException {
        RLock lock = redissonClient.getLock(key);
        try {
            boolean tryLock = lock.tryLock(waitTime, leaseTime, unit);
            if (tryLock) {
                log.debug("{} 获取锁成功", key);
                return action.execute();
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return null;
    }

    @Override
    public void tryLockAndExecute(String key, long waitTime, long leaseTime, TimeUnit unit, VoidHandle action) throws InterruptedException {
        RLock lock = redissonClient.getLock(key);
        try {
            boolean tryLock = lock.tryLock(waitTime, leaseTime, unit);
            if (tryLock) {
                log.info("{} 获取锁成功", key);
                action.execute();
            } else {
                log.info("{} 获取锁超时", key);
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public <T> T lockAndExecute(String key, long leaseTime, TimeUnit unit, ReturnHandle<T> action) {
        RLock lock = redissonClient.getLock(key);
        try {
            lock.lock(leaseTime, unit);
            log.info("{} 获取锁成功", key);
            return action.execute();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public void lockAndExecute(String key, long leaseTime, TimeUnit unit, VoidHandle action) {
        RLock lock = redissonClient.getLock(key);
        try {
            lock.lock(leaseTime, unit);
            log.info("{} 获取锁成功", key);
            action.execute();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public boolean unlock(String key) {
        RLock lock = redissonClient.getLock(key);
        lock.unlock();
        return true;
    }
}
