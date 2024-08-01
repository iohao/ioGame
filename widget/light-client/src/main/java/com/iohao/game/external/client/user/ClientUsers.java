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
package com.iohao.game.external.client.user;

import com.iohao.game.common.kit.concurrent.TaskKit;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 压力测试时的一个辅助类
 *
 * @author 渔民小镇
 * @date 2023-07-16
 */
@Slf4j
@UtilityClass
public class ClientUsers {
    final BlockingQueue<Runnable> runnableQueue = new LinkedBlockingQueue<>();
    final AtomicBoolean loginSuccess = new AtomicBoolean();
    final List<ClientUser> clientUsers = new CopyOnWriteArrayList<>();

    static {
        TaskKit.execute(() -> {

            try {
                // 小等一会
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }

            while (true) {
                // 等所有玩家登录完成
                boolean loginSuccess = ClientUsers.isLoginSuccess();
                if (loginSuccess) {
                    break;
                }

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }

            extractedExecute();
        });
    }

    boolean executeStart;

    private void extractedExecute() {
        executeStart = true;

        TaskKit.execute(() -> {
            if (clientUsers.size() > 1) {
                int sleep = 5;
                log.info("[{}]个玩家全部登录完成，[{}]秒后开始执行任务[{}]", clientUsers.size(), sleep, runnableQueue.size());

                try {
                    TimeUnit.SECONDS.sleep(sleep);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }

            while (executeStart) {
                try {
                    Runnable take = runnableQueue.take();
                    TaskKit.execute(take);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    public void addClientUser(ClientUser clientUser) {
        clientUsers.add(clientUser);
    }

    boolean isLoginSuccess() {

        if (loginSuccess.get()) {
            return true;
        }

        int size = 0;
        for (ClientUser clientUser : clientUsers) {
            if (clientUser.getUserId() != 0) {
                size++;
            }
        }

        loginSuccess.set(clientUsers.size() == size);
        return loginSuccess.get();
    }

    /**
     * 将任务添加到队列中，当玩家全部登录完成后会执行任务
     *
     * @param command 任务
     */
    public void execute(Runnable command) {
        runnableQueue.add(command);
    }
}
