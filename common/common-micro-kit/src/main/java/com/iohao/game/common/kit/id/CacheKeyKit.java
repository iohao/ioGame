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
package com.iohao.game.common.kit.id;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 渔民小镇
 * @date 2021-12-25
 */
@Slf4j
@UtilityClass
class CacheKeyKit {

    private final String ASSIGNED_SEQUENCES = "com.iohao.assignedSequences";

    private final AtomicInteger count = new AtomicInteger(0);

    private final long TYPE1 = 0x1000L;

    private final byte VARIANT = (byte) 0x80;

    private final int SEQUENCE_MASK = 0x3FFF;

    private final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;

    private final long least;

    private final long LOW_MASK = 0xffffffffL;
    private final long MID_MASK = 0xffff00000000L;
    private final long HIGH_MASK = 0xfff000000000000L;
    private final int NODE_SIZE = 8;
    private final int SHIFT_2 = 16;
    private final int SHIFT_4 = 32;
    private final int SHIFT_6 = 48;
    private final int HUNDRED_NANOS_PER_MILLI = 10000;

    static {
        byte[] mac = null;
        try {
            final InetAddress address = InetAddress.getLocalHost();
            try {
                NetworkInterface ni = NetworkInterface.getByInetAddress(address);
                if (ni != null && !ni.isLoopback() && ni.isUp()) {
                    final Method method = ni.getClass().getMethod("getHardwareAddress");
                    mac = (byte[]) method.invoke(ni);
                }

                if (mac == null) {
                    final Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
                    while (enumeration.hasMoreElements() && mac == null) {
                        ni = enumeration.nextElement();
                        if (ni != null && ni.isUp() && !ni.isLoopback()) {
                            final Method method = ni.getClass().getMethod("getHardwareAddress");
                            mac = (byte[]) method.invoke(ni);
                        }
                    }
                }
            } catch (final Exception ex) {
                log.error(ex.getMessage(), ex);
            }

            if (mac == null || mac.length == 0) {
                mac = address.getAddress();
            }
        } catch (final UnknownHostException e) {
            // Ignore exception
        }

        final Random randomGenerator = new SecureRandom();
        if (mac == null || mac.length == 0) {
            mac = new byte[6];
            randomGenerator.nextBytes(mac);
        }

        final int length = Math.min(mac.length, 6);
        final int index = mac.length >= 6 ? mac.length - 6 : 0;
        final byte[] node = new byte[NODE_SIZE];
        node[0] = VARIANT;
        node[1] = 0;
        for (int i = 2; i < NODE_SIZE; ++i) {
            node[i] = 0;
        }

        System.arraycopy(mac, index, node, index + 2, length);
        final ByteBuffer buf = ByteBuffer.wrap(node);
        String assigned = ASSIGNED_SEQUENCES;
        long[] sequences;

        final String[] array = new String[]{"1", "9", "9", "0", "0", "4", "0", "5"};
        sequences = new long[array.length];
        int i = 0;
        for (final String value : array) {
            sequences[i] = Long.parseLong(value);
            ++i;
        }

        long rand = randomGenerator.nextLong();
        rand &= SEQUENCE_MASK;
        boolean duplicate;

        do {
            duplicate = false;
            for (final long sequence : sequences) {
                if (sequence == rand) {
                    duplicate = true;
                    break;
                }
            }
            if (duplicate) {
                rand = (rand + 1) & SEQUENCE_MASK;
            }
        } while (duplicate);

        assigned = assigned + ',' + rand;
        System.setProperty(ASSIGNED_SEQUENCES, assigned);

        least = buf.getLong() | rand << SHIFT_6;
    }

    /**
     * 时间生成UUID
     *
     * @return UUID
     */
    private UUID getTimeBasedUuid() {

        final long time = ((System.currentTimeMillis() * HUNDRED_NANOS_PER_MILLI) +
                NUM_100NS_INTERVALS_SINCE_UUID_EPOCH) + (count.incrementAndGet() % HUNDRED_NANOS_PER_MILLI);
        final long timeLow = (time & LOW_MASK) << SHIFT_4;
        final long timeMid = (time & MID_MASK) >> SHIFT_2;
        final long timeHi = (time & HIGH_MASK) >> SHIFT_6;
        final long most = timeLow | timeMid | TYPE1 | timeHi;
        return new UUID(most, least);
    }

    /**
     * 时间生成UUID
     *
     * @return UUID str
     */
    public String uuid() {
        return getTimeBasedUuid().toString();
    }

    public static void main(String[] args) {
        System.out.println(uuid());
    }
}
