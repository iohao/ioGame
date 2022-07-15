package com.iohao.game.bolt.broker.boot.monitor.message.oshi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import oshi.hardware.CentralProcessor;
import oshi.util.Util;

import java.io.Serial;
import java.io.Serializable;

/**
 * copy from hutool
 * <p>
 * CPU负载时间信息
 *
 * @author 渔民小镇
 * @date 2022-06-04
 */
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PACKAGE)
public class MonitorCpuTicks implements Serializable {

    @Serial
    private static final long serialVersionUID = -5674890669004076268L;

    long idle;
    long nice;
    long irq;
    long softIrq;
    long steal;
    long cSys;
    long user;
    long ioWait;

    public MonitorCpuTicks() {
    }

    /**
     * 构造，等待时间为用于计算在一定时长内的CPU负载情况，如传入1000表示最近1秒的负载情况
     *
     * @param processor   {@link CentralProcessor}
     * @param waitingTime 设置等待时间，单位毫秒
     */
    public MonitorCpuTicks(CentralProcessor processor, long waitingTime) {
        // CPU信息
        final long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 这里必须要设置延迟
        Util.sleep(waitingTime);
        final long[] ticks = processor.getSystemCpuLoadTicks();

        this.idle = tick(prevTicks, ticks, CentralProcessor.TickType.IDLE);
        this.nice = tick(prevTicks, ticks, CentralProcessor.TickType.NICE);
        this.irq = tick(prevTicks, ticks, CentralProcessor.TickType.IRQ);
        this.softIrq = tick(prevTicks, ticks, CentralProcessor.TickType.SOFTIRQ);
        this.steal = tick(prevTicks, ticks, CentralProcessor.TickType.STEAL);
        this.cSys = tick(prevTicks, ticks, CentralProcessor.TickType.SYSTEM);
        this.user = tick(prevTicks, ticks, CentralProcessor.TickType.USER);
        this.ioWait = tick(prevTicks, ticks, CentralProcessor.TickType.IOWAIT);
    }


    /**
     * 获取CPU总的使用率
     *
     * @return CPU总使用率
     */
    public long totalCpu() {
        return Math.max(user + nice + cSys + idle + ioWait + irq + softIrq + steal, 0);
    }

    @Override
    public String toString() {
        return "CpuTicks{" +
                "idle=" + idle +
                ", nice=" + nice +
                ", irq=" + irq +
                ", softIrq=" + softIrq +
                ", steal=" + steal +
                ", cSys=" + cSys +
                ", user=" + user +
                ", ioWait=" + ioWait +
                '}';
    }

    /**
     * 获取一段时间内的CPU负载标记差
     *
     * @param prevTicks 开始的ticks
     * @param ticks     结束的ticks
     * @param tickType  tick类型
     * @return 标记差
     * @since 5.7.12
     */
    private static long tick(long[] prevTicks, long[] ticks, CentralProcessor.TickType tickType) {
        return ticks[tickType.getIndex()] - prevTicks[tickType.getIndex()];
    }
}
