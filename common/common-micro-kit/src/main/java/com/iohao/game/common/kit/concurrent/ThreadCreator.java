/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.common.kit.concurrent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * copy from springframework
 *
 * @author 渔民小镇
 * @date 2023-04-02
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThreadCreator {

    String threadNamePrefix;

    int threadPriority = Thread.NORM_PRIORITY;

    boolean daemon = false;

    ThreadGroup threadGroup;

    final AtomicInteger threadCount = new AtomicInteger();

    public ThreadCreator(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    public void setThreadGroupName(String name) {
        this.threadGroup = new ThreadGroup(name);
    }

    public Thread createThread(Runnable runnable) {
        Thread thread = new Thread(getThreadGroup(), runnable, nextThreadName());
        thread.setPriority(threadPriority);
        thread.setDaemon(daemon);
        return thread;
    }

    protected String nextThreadName() {
        String format = "%s-%d";
        return String.format(format, this.threadNamePrefix, this.threadCount.incrementAndGet());
    }
}
