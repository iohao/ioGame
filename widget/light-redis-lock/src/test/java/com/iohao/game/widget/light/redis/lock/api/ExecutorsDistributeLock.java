//package com.iohao.game.widget.light.redis.lock.api;
//
//
//import com.iohao.game.widget.light.redis.lock.entity.UserWallet;
//import com.iohao.game.widget.light.redis.lock.DefaultRedissonDistributedLock;
//import com.iohao.game.widget.light.redis.lock.DistributedLock;
//import com.iohao.game.widget.light.redis.lock.RedissonConfig;
//
//import java.math.BigDecimal;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * 使用自建线程池模拟多线程环境竞争锁
// *
// * @author shen
// * @date 2022-03-31
// * @Slogan 慢慢变好，是给自己最好的礼物
// */
//public class ExecutorsDistributeLock {
//
//    public final DistributedLock distributedLock = new DefaultRedissonDistributedLock(RedissonConfig.me());
//
//    public static ExecutorService executors = new ThreadPoolExecutor(
//            8,
//            64,
//            0L, TimeUnit.MILLISECONDS,
//            new LinkedBlockingQueue<>(16),
//            new ThreadPoolExecutor.DiscardPolicy()
//    );
//
//    public void testRlock() {
//        UserWallet wallet = new UserWallet();
//        wallet.setUserId("10086");
//        wallet.setName("中国移动");
//        wallet.setBalance(BigDecimal.valueOf(100000L));
//        for (int i = 0; i < 32; i++) {
//            executors.submit(() -> consume(wallet));
//        }
//    }
//
//    public void testlockWithTime() {
//        UserWallet wallet = new UserWallet();
//        wallet.setUserId("10086");
//        wallet.setName("中国移动");
//        wallet.setBalance(BigDecimal.valueOf(100000L));
//
//        final long leaseTime = 2L;
//        final long watiTime = 1L;
//        final long sleepTime = 5000L;
//        for (int i = 0; i < 100; i++) {
//            executors.submit(() -> consumeTryLock(wallet, watiTime, leaseTime, sleepTime));
//        }
//        System.err.println("全部任务加入完毕");
//    }
//
//    public void consume(UserWallet wallet) {
//        try {
//            distributedLock.tryLockAndExecute(wallet.getUserId(), 10, 1, TimeUnit.SECONDS, () -> {
//                System.out.println("线程：" + Thread.currentThread().getName() + "拿到锁了");
//                BigDecimal sub = new BigDecimal("1");
//                wallet.setBalance(wallet.getBalance().subtract(sub));
//                System.out.println("【" + wallet.getName() + "】当前余额【" + wallet.getBalance() + "】");
//            });
//        } catch (InterruptedException e) {
//            System.out.println("获取锁等待失败");
//        }
//    }
//
//    /**
//     * tryLock方式模拟分布式锁
//     *
//     * @param wallet    用户数据
//     * @param waitTime  等待时间
//     * @param leaseTime 释放时间
//     * @param sleepTime 模拟多线程竞争睡眠时间
//     */
//    public void consumeTryLock(UserWallet wallet, long waitTime, long leaseTime, long sleepTime) {
//        try {
//            distributedLock.tryLockAndExecute(wallet.getUserId(), waitTime, leaseTime, TimeUnit.SECONDS, () -> {
//                System.out.println("线程：" + Thread.currentThread().getName() + "拿到锁了");
//                try {
//                    Thread.sleep(sleepTime);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                BigDecimal sub = new BigDecimal("1");
//                wallet.setBalance(wallet.getBalance().subtract(sub));
//                System.out.println("【" + wallet.getName() + "】当前余额【" + wallet.getBalance() + "】");
//            });
//        } catch (InterruptedException e) {
//            System.out.println("获取锁等待失败");
//        }
//    }
//
//    public void consumeLock(UserWallet wallet, long leaseTime) {
//        distributedLock.lockAndExecute(wallet.getUserId(), leaseTime, TimeUnit.SECONDS, () -> {
//            System.out.println("线程：" + Thread.currentThread().getName() + "拿到锁了");
//            try {
//                Thread.sleep(20000L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            BigDecimal sub = new BigDecimal("1");
//            wallet.setBalance(wallet.getBalance().subtract(sub));
//            System.out.println("【" + wallet.getName() + "】当前余额【" + wallet.getBalance() + "】");
//        });
//    }
//
//}
