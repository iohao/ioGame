/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
import com.iohao.game.common.kit.RandomKit;
import com.iohao.game.common.kit.concurrent.TaskKit;
import com.iohao.game.common.kit.exception.ThrowKit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.out;

/**
 * ioGame Banner ， 不提供关闭 Banner 的方法，让开发者含泪看完 Banner
 *
 * @author 渔民小镇
 * @date 2023-01-30
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class IoGameBanner {
    /** 特殊字段，开发者不要使用 */
    public static String flag21;
    public static boolean troublemaker;
    public static int troubleCounter;

    final AtomicBoolean trigger = new AtomicBoolean(false);
    /** 特殊字段，开发者不要使用 */
    AtomicInteger errorCount = new AtomicInteger(0);
    Date startTime = new Date();

    CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void render() {

        if (me().trigger.get()) {
            return;
        }

        // 只触发一次
        if (!me().trigger.compareAndSet(false, true)) {
            return;
        }

        me().renderBanner1();
    }

    public void initCountDownLatch(int num) {
        this.countDownLatch = new CountDownLatch(num);
    }

    public void countDown() {
        if (Objects.nonNull(this.countDownLatch)) {
            this.countDownLatch.countDown();
        }
    }

    private void incErrorCount() {
        if (Objects.nonNull(errorCount)) {
            errorCount.getAndIncrement();
        }
    }

    private final AtomicBoolean print = new AtomicBoolean(false);

    public void ofRuntimeException(String message) {
        if (!print.get()) {
            return;
        }

        incErrorCount();
        ThrowKit.ofRuntimeException(message);
        render();
    }

    public static void printLine() {
        out.println();
    }

    public static void printMessage(String message) {
        out.print(message);
    }

    public static void printMessage(Object message) {
        out.print(message);
    }

    public static void println1(Object message) {
        out.println(message);
    }

    public static void printlnMsg(String message) {
        out.println(message);
    }

    public static void printlnMsg2(String message) {
        printlnMsg(message);
    }

    private void renderBanner1() {
        print.set(true);

        Runnable runnable = () -> {

            try {
                if (Objects.nonNull(IoGameBanner.me().countDownLatch)) {
                    boolean r = IoGameBanner.me().countDownLatch.await(5, TimeUnit.SECONDS);
                    if (!r) {
                        IoGameBanner.printlnMsg("countDownLatch await is false");
                    }
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
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

            extractedIoGameJavadocApi();

            extractedAdv();
            // breaking news
            extractedBreakingNews();

            extractedErrorCount();

            clean();

            IoGameBanner.printLine();
        };

        TaskKit.execute(runnable);
    }

    private void clean() {
        this.startTime = null;
        this.countDownLatch = null;
        flag21 = "ioGame21 ";
        errorCount = null;
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
        // 每次展示 N 条小报
        var newsList = BreakingNewsKit.randomNewsList();
        for (News news : newsList) {
            System.out.printf("| News     | %s%n", news);
        }

        IoGameBanner.printlnMsg("+----------+--------------------------------------------------------------------------------------");
    }

    private void extractedAdv() {
        String s = BreakingNewsKit.randomAdv().toString();
        String builder = "| adv      | %s%n";
        System.out.printf(builder, s);
        IoGameBanner.printlnMsg("+----------+--------------------------------------------------------------------------------------");
    }

    private void extractedIoGameJavadocApi() {
        String s = BreakingNewsKit.randomMainNews().toString();
        String builder = "|          | %s%n";
        System.out.printf(builder, s);
        IoGameBanner.printlnMsg("+----------+--------------------------------------------------------------------------------------");
    }

    private void extractedErrorCount() {
        if (Objects.isNull(errorCount) || errorCount.get() == 0) {
            return;
        }

        String builder = "| Error    | error count : %s%n";
        System.out.printf(builder, errorCount.get());
        IoGameBanner.printlnMsg("+----------+--------------------------------------------------------------------------------------");
    }

    private void extractedPrint(ToyTable table) {
        // 为了在控制台上显示得不那么的枯燥，这里使用随机的 banner 和随机上色策略
        List<String> bannerList = new BannerData().listData();
        String banner = RandomKit.randomEle(bannerList);

        // 上色策略
        var anyFunction = new BannerColorStrategy().anyColorFun();
        String anyBanner = anyFunction.apply(banner);

        IoGameBanner.printLine();
        IoGameBanner.printlnMsg(anyBanner);
        table.render();
    }

    private static String getPid() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();

        try {
            return name.substring(0, name.indexOf('@'));
        } catch (Exception e) {
            return "-1";
        }
    }

    private IoGameBanner() {
        flag21 = "ioGame..21..";
    }

    public static IoGameBanner me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final IoGameBanner ME = new IoGameBanner();
    }

    public void init() {

    }
}
