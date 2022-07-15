//package com.iohao.game.widget.light.redis.lock.api;
//
//import com.iohao.game.widget.light.redis.lock.entity.Consumer;
//import com.iohao.game.widget.light.redis.lock.entity.UserWallet;
//
//import java.math.BigDecimal;
//
///**
// * 使用自建线程池模拟多线程环境竞争锁
// *
// * @author shen
// * @date 2022-03-31
// * 慢慢变好，是给自己最好的礼物
// */
////@Component
//public class SpringMultiThreadDistributeLock {
//
//    private Consumer consumer;
//
//    /**
//     * 模拟正常的，能够执行完所有任务的lock
//     */
//    public void testRlock() {
//        UserWallet wallet = new UserWallet();
//        wallet.setUserId("10086");
//        wallet.setName("中国移动");
//        wallet.setBalance(BigDecimal.valueOf(100000L));
//        for (int i = 0; i < 100; i++) {
//            consumer.consume(wallet);
//        }
//    }
//
//    /**
//     * 模拟等到超时的lock
//     * 以下时间默认单位：秒
//     */
//    public void testTryLock() {
//        UserWallet wallet = new UserWallet();
//        wallet.setUserId("10086");
//        wallet.setName("中国移动");
//        wallet.setBalance(BigDecimal.valueOf(100000L));
//
//        //等到锁时间
//        long waitTime = 1000L;
//
//        //执行时间
//        long leaseTime = -1L;
//
//        for (int i = 0; i < 8; i++) {
//            consumer.consumeTryLock(wallet, waitTime, leaseTime);
//        }
//    }
//
//    /**
//     * 模拟等到超时的lock
//     * 以下时间默认单位：秒
//     */
//    public void testLock() {
//        UserWallet wallet = new UserWallet();
//        wallet.setUserId("10086");
//        wallet.setName("中国移动");
//        wallet.setBalance(BigDecimal.valueOf(100000L));
//
//        //执行时间
//        long leaseTime = -1L;
//
//        for (int i = 0; i < 8; i++) {
//            consumer.consumeLock(wallet, leaseTime);
//        }
//    }
//}
