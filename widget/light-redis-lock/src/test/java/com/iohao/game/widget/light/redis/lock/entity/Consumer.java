//package com.iohao.game.widget.light.redis.lock.entity;
//
//import com.iohao.game.widget.light.redis.lock.DistributedLock;
//
//import java.math.BigDecimal;
//import java.util.concurrent.TimeUnit;
//
////@Component
//public class Consumer {
//
////    @Resource
//    private DistributedLock distributedLock;
//
////    @Async
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
////    @Async
//    public void consumeTryLock(UserWallet wallet, long waitTime, long leaseTime) {
//        try {
//            distributedLock.tryLockAndExecute(wallet.getUserId(), waitTime, leaseTime, TimeUnit.SECONDS, () -> {
//                System.out.println("线程：" + Thread.currentThread().getName() + "拿到锁了");
//                try {
//                    Thread.sleep(20000L);
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
////    @Async
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
//}
