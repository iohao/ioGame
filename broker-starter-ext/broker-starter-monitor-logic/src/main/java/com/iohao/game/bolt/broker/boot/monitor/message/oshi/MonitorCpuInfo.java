package com.iohao.game.bolt.broker.boot.monitor.message.oshi;

import cn.hutool.core.util.NumberUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import oshi.hardware.CentralProcessor;

import java.io.Serial;
import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * copy from hutool
 * <p>
 * CPU相关信息
 *
 * @author 渔民小镇
 * @date 2022-06-04
 */
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonitorCpuInfo implements Serializable {

    private static final DecimalFormat LOAD_FORMAT = new DecimalFormat("#.00");
    @Serial
    private static final long serialVersionUID = -4190965324132570713L;

    /**
     * CPU核心数
     */
    Integer cpuNum;

    /**
     * CPU总的使用率
     */
    double toTal;

    /**
     * CPU系统使用率
     */
    double sys;

    /**
     * CPU用户使用率
     */
    double user;

    /**
     * CPU当前等待率
     */
    double wait;

    /**
     * CPU当前空闲率
     */
    double free;

    /**
     * CPU型号信息
     */
    String cpuModel;

    /**
     * CPU型号信息
     */
    MonitorCpuTicks ticks;

    /**
     * 空构造
     */
    public MonitorCpuInfo() {
    }

    /**
     * 构造，等待时间为用于计算在一定时长内的CPU负载情况，如传入1000表示最近1秒的负载情况
     *
     * @param processor   {@link CentralProcessor}
     * @param waitingTime 设置等待时间，单位毫秒
     */
    public MonitorCpuInfo(CentralProcessor processor, long waitingTime) {
        init(processor, waitingTime);
    }

    /**
     * 构造
     *
     * @param cpuNum   CPU核心数
     * @param toTal    CPU总的使用率
     * @param sys      CPU系统使用率
     * @param user     CPU用户使用率
     * @param wait     CPU当前等待率
     * @param free     CPU当前空闲率
     * @param cpuModel CPU型号信息
     */
    public MonitorCpuInfo(Integer cpuNum, double toTal, double sys, double user, double wait, double free, String cpuModel) {
        this.cpuNum = cpuNum;
        this.toTal = toTal;
        this.sys = sys;
        this.user = user;
        this.wait = wait;
        this.free = free;
        this.cpuModel = cpuModel;
    }

    /**
     * 获取用户+系统的总的CPU使用率
     *
     * @return 总CPU使用率
     */
    public double getUsed() {
        return NumberUtil.sub(100, this.free);
    }

    @Override
    public String toString() {
        return "CpuInfo{" +
                "CPU核心数=" + cpuNum +
                ", CPU总的使用率=" + toTal +
                ", CPU系统使用率=" + sys +
                ", CPU用户使用率=" + user +
                ", CPU当前等待率=" + wait +
                ", CPU当前空闲率=" + free +
                ", CPU利用率=" + getUsed() +
                ", CPU型号信息='" + cpuModel + '\'' +
                '}';
    }

    /**
     * 获取指定等待时间内系统CPU 系统使用率、用户使用率、利用率等等 相关信息
     *
     * @param processor   {@link CentralProcessor}
     * @param waitingTime 设置等待时间，单位毫秒
     * @since 5.7.12
     */
    private void init(CentralProcessor processor, long waitingTime) {
        final MonitorCpuTicks ticks = new MonitorCpuTicks(processor, waitingTime);
        this.ticks = ticks;

        this.cpuNum = processor.getLogicalProcessorCount();
        this.cpuModel = processor.toString();

        final long totalCpu = ticks.totalCpu();
        this.toTal = totalCpu;
        this.sys = formatDouble(ticks.cSys, totalCpu);
        this.user = formatDouble(ticks.user, totalCpu);
        this.wait = formatDouble(ticks.ioWait, totalCpu);
        this.free = formatDouble(ticks.idle, totalCpu);
    }

    /**
     * 获取每个CPU核心的tick，计算方式为 100 * tick / totalCpu
     *
     * @param tick     tick
     * @param totalCpu CPU总数
     * @return 平均每个CPU核心的tick
     * @since 5.7.12
     */
    private static double formatDouble(long tick, long totalCpu) {
        if (0 == totalCpu) {
            return 0D;
        }
        return Double.parseDouble(LOAD_FORMAT.format(tick <= 0 ? 0 : (100d * tick / totalCpu)));
    }
}
