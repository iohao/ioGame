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
package com.iohao.game.common.kit;

import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Trace 相关工具
 *
 * @author 渔民小镇
 * @date 2023-12-20
 */
@UtilityClass
public class TraceKit {
    final Map<String, TraceIdSupplier> traceIdSupplierMap = new NonBlockingHashMap<>();
    @Setter
    TraceIdSupplier traceIdSupplier = new SnowTraceIdSupplier(0, 0);
    public String traceName = "ioGameTraceId";

    public void putTraceIdSupplier(String name, TraceIdSupplier traceIdSupplier) {
        traceIdSupplierMap.putIfAbsent(name, traceIdSupplier);
    }

    public String newTraceId(String name) {
        return traceIdSupplierMap.getOrDefault(name, traceIdSupplier).get();
    }

    public String newTraceId() {
        return traceIdSupplier.get();
    }

    @FunctionalInterface
    public interface TraceIdSupplier extends Supplier<String> {

    }

    private class TraceIdSupplierMD5 implements TraceIdSupplier {

        @Override
        public String get() {
            String id = UUID.randomUUID().toString();
            return md5Encrypt(id);
        }

        public String md5Encrypt(String input) {
            try {
                // 创建MessageDigest对象，指定MD5算法
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 将输入的字符串转换为字节数组
                byte[] bytes = input.getBytes();
                // 对字节数组进行MD5加密
                md.update(bytes);
                // 获取加密后的字节数组
                byte[] digest = md.digest();
                // 将字节数组转换为16进制字符串
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) {
                    // 将每个字节转换为两位16进制数，不足两位的补0
                    sb.append(String.format("%02x", b));
                }

                // 返回加密后的字符串
                return sb.toString();
            } catch (NoSuchAlgorithmException e) {
                // 如果没有找到MD5算法，抛出异常
                return null;
            }
        }
    }

    private class SnowTraceIdSupplier implements TraceIdSupplier {

        final static long START_TIMESTAMP = 1480166465631L;

        final static long SEQUENCE_BIT = 12;
        final static long MACHINE_BIT = 5;
        final static long DATACENTER_BIT = 5;

        final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
        final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
        final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);

        final static long MACHINE_LEFT = SEQUENCE_BIT;
        final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
        final static long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

        /** 数据中心 */
        final long dataCenterId;
        /** 机器标识 */
        final long machineId;
        /** 序列号 */
        long sequence = 0L;
        /** 上一次时间戳 */
        long lastTimestamp = -1L;

        public SnowTraceIdSupplier(long dataCenterId, long machineId) {
            if (dataCenterId > MAX_DATACENTER_NUM || dataCenterId < 0) {
                throw new IllegalArgumentException("Datacenter ID can't be greater than MAX_DATACENTER_NUM or less than 0");
            }
            if (machineId > MAX_MACHINE_NUM || machineId < 0) {
                throw new IllegalArgumentException("Machine ID can't be greater than MAX_MACHINE_NUM or less than 0");
            }
            this.dataCenterId = dataCenterId;
            this.machineId = machineId;
        }

        /**
         * 产生下一个ID
         *
         * @return 下一个ID
         */
        @Override
        public String get() {
            return Long.toString(nextId());
        }

        synchronized long nextId() {
            long currTimestamp = getTimestamp();
            if (currTimestamp < lastTimestamp) {
                throw new RuntimeException("Clock moved backwards. Refusing to generate id");
            }

            // 如果是同一时间生成的，则进行毫秒内序列
            if (currTimestamp == lastTimestamp) {
                sequence = (sequence + 1) & MAX_SEQUENCE;
                // 序列号已经达到最大值，下一个毫秒
                if (sequence == 0L) {
                    currTimestamp = getNextTimestamp();
                }
            } else {
                // 时间戳改变，毫秒内序列重置
                sequence = 0L;
            }

            // 上次生成ID的时间截
            lastTimestamp = currTimestamp;

            // 移位并通过或运算拼到一起组成64位的ID
            // 时间戳部分
            return (currTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT
                    // 数据中心部分
                    | dataCenterId << DATACENTER_LEFT
                    // 机器标识部分
                    | machineId << MACHINE_LEFT
                    // 序列号部分
                    | sequence;
        }

        /**
         * 获取下一个毫秒数
         *
         * @return 下一个毫秒数
         */
        long getNextTimestamp() {
            long timestamp = getTimestamp();
            while (timestamp <= lastTimestamp) {
                timestamp = getTimestamp();
            }
            return timestamp;
        }

        /**
         * 获取当前的时间戳
         *
         * @return 当前的时间戳
         */
        long getTimestamp() {
            return System.currentTimeMillis();
        }
    }
}
