/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.iohao.game.action.skeleton.toy;

import com.iohao.game.action.skeleton.IoGameVersion;
import com.iohao.game.common.kit.ExecutorKit;
import com.iohao.game.common.kit.RandomKit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ioGame Banner ， 不提供关闭 Banner 的方法，让开发者含泪看完 Banner
 *
 * @author 渔民小镇
 * @date 2023-01-30
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class IoGameBanner {
    /** 特殊字段，开发者不要使用 */
    public static String flag57;
    final AtomicBoolean trigger = new AtomicBoolean(false);
    Date startTime = new Date();

    CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void render() {

        // 只触发一次
        if (!me().trigger.compareAndSet(false, true)) {
            return;
        }

        me().renderBanner();
    }

    public void initCountDownLatch(int num) {
        this.countDownLatch = new CountDownLatch(num);
    }

    public void countDown() {
        if (Objects.nonNull(this.countDownLatch)) {
            this.countDownLatch.countDown();
        }
    }

    private void renderBanner() {

        Runnable runnable = () -> {

            try {
                if (Objects.nonNull(IoGameBanner.me().countDownLatch)) {
                    IoGameBanner.me().countDownLatch.await(5, TimeUnit.SECONDS);
                }
            } catch (InterruptedException e) {
            }

            var table = new ToyTable();

            // app
            var ioGameRegion = table.getRegion("ioGame");
            ioGameRegion.putLine("pid", getPid());
            ioGameRegion.putLine("version", IoGameVersion.VERSION);
            ioGameRegion.putLine("document", "http://game.iohao.com");

            // 内存相关
            var internalMemory = new InternalMemory();
            var memory = table.getRegion("Memory");
            memory.putAll(internalMemory.getMemoryMap());

            // 启动时间
            extractedTime(table);

            extractedPrint(table);

            extractedAdv();
            // breaking news
            extractedBreakingNews();
//            extractedMiss();

            clean();

            System.out.println();
        };

        var executorService = ExecutorKit.newSingleThreadExecutor("IoGameBanner");
        executorService.execute(runnable);
        executorService.shutdown();
    }

    private void clean() {
        this.startTime = null;
        this.countDownLatch = null;
    }

    private void extractedTime(ToyTable table) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date endTime = new Date();
        var consumeTime = (endTime.getTime() - this.startTime.getTime()) / 1000f;
        var consume = String.format("%.2f s", consumeTime);

        // 时间相关
        ToyTableRegion other = table.getRegion("Time");
        other.putLine("start", simpleDateFormat.format(this.startTime));
        other.putLine("end", simpleDateFormat.format(endTime));
        other.putLine("consume", consume);
    }

    private void extractedBreakingNews() {
        String next = BreakingNews.randomNews();
        String builder = "| News     | %s%n";
        System.out.printf(builder, next);
        System.out.println("+----------+--------------------------------------------------------------------------------------");
    }

    private void extractedAdv() {
        String s = BreakingNews.randomAdvText();
        String builder = "| adv      | %s - %s%n";
        System.out.printf(builder, "启动项广告位招租", s);
        System.out.println("+----------+--------------------------------------------------------------------------------------");
    }

    private void extractedMiss() {
        String desc = "大家可以来登记，有需要展示或记念的宠物、动漫人物等各种事物";

        String builder = "| miss     | %s%n";
        System.out.printf(builder, "女娲:中国神话中的造物主，据说创造了人类和世界。", desc);
        builder = "|          | (%s)%n";
        System.out.printf(builder, desc);

        System.out.println("+----------+--------------------------------------------------------------------------------------");
    }

    private void extractedPrint(ToyTable table) {
        // 为了在控制台上显示得不那么的枯燥，这里使用随机的 banner 和随机上色策略
        List<String> bannerList = new BannerData().listData();
        String banner = RandomKit.randomEle(bannerList);

        // 上色策略
        var anyFunction = new BannerColorStrategy().anyColorFun();
        String anyBanner = anyFunction.apply(banner);

        System.out.println();
        System.out.println(anyBanner);
        table.render();
    }

    private static String getPid() {
        //获取进程的PID
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();

        try {
            return name.substring(0, name.indexOf('@'));
        } catch (Exception e) {
            return "-1";
        }
    }


    private IoGameBanner() {
    }

    public static IoGameBanner me() {
        flag57 = "1";
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final IoGameBanner ME = new IoGameBanner();
    }
}
