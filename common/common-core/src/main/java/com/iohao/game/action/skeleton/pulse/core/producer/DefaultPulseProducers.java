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
package com.iohao.game.action.skeleton.pulse.core.producer;

import com.iohao.game.action.skeleton.pulse.core.PulseTransmit;
import com.iohao.game.action.skeleton.pulse.message.PulseSignalRequest;
import com.iohao.game.action.skeleton.pulse.message.PulseSignalResponse;
import com.iohao.game.common.kit.ExecutorKit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author 渔民小镇
 * @date 2023-04-20
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class DefaultPulseProducers implements PulseProducers {
    final ScheduledExecutorService executor = ExecutorKit.newSingleScheduled(DefaultPulseProducers.class.getSimpleName());
    final Map<String, PulseProducer<?>> map = new NonBlockingHashMap<>();
    final List<PulseTask> taskList = new CopyOnWriteArrayList<>();
    final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    final Map<String, PulseSignalResponseAccept<?>> responseAcceptMap = new NonBlockingHashMap<>();

    PulseTransmit pulseTransmit;

    @Override
    public void addResponseAccept(PulseSignalResponseAccept<?> responseAccept) {
        String channel = responseAccept.channel();
        this.responseAcceptMap.put(channel, responseAccept);
    }

    @Override
    public void addPulseProducer(PulseProducer<?> pulseProducer) {
        String channel = pulseProducer.channel();

        PulseProducer<?> pulse = this.map.putIfAbsent(channel, pulseProducer);
        if (Objects.nonNull(pulse)) {
            return;
        }

        // 添加到任务中
        this.taskList.add(new PulseTask(pulseProducer));
    }

    @Override
    public void startup() {
        if (atomicBoolean.compareAndSet(false, true)) {
            final Consumer<PulseSignalRequest> consumer = request -> this.pulseTransmit.transmit(request);

            final Runnable runnable = () -> taskList.forEach(task -> task.ifPresent(consumer));

            this.executor.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.SECONDS);
        }
    }

    @Override
    public void acceptPulseSign(PulseSignalResponse response) {
        String channel = response.getChannel();

        Optional.ofNullable(this.map.get(channel))
                .ifPresent(pulseProducer -> pulseProducer.accept(response.getData(), response));

        Optional.ofNullable(this.responseAcceptMap.get(channel))
                .ifPresent(pulseProducer -> pulseProducer.accept(response.getData(), response));
    }

    @Override
    public void setPulseTransmit(PulseTransmit pulseTransmit) {
        this.pulseTransmit = pulseTransmit;
    }

    @Override
    public PulseTransmit getPulseTransmit() {
        return this.pulseTransmit;
    }

    private static class PulseTask {
        final PulseProducer<?> pulseProducer;
        int count;

        PulseTask(PulseProducer<?> pulseProducer) {
            this.pulseProducer = pulseProducer;
            // 当为负数时也表示不想执行
            this.count = pulseProducer.period();
        }

        void ifPresent(Consumer<PulseSignalRequest> consumer) {
            this.count--;

            if (this.count != 0) {
                return;
            }

            try {
                PulseSignalRequest request = this.pulseProducer.createMessage();
                request.setChannel(this.pulseProducer.channel());

                if (request.getSignalType() == 0) {
                    String text = """
                            请添加脉冲信号类型 request.addSignalType(SignalType)
                            class : {}
                            request: {}
                            """;

                    log.error(text, this.pulseProducer.getClass().getSimpleName(), request);
                    // 重置次数
                    this.count = this.pulseProducer.period();
                    return;
                }

                consumer.accept(request);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }

            // 重置次数
            this.count = this.pulseProducer.period();
        }
    }
}
