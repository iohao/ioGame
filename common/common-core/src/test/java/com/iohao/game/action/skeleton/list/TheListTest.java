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
package com.iohao.game.action.skeleton.list;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.iohao.game.action.skeleton.core.action.pojo.Snake;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author 渔民小镇
 * @date 2023-02-08
 */
@Slf4j
public class TheListTest {


    @Test
    public void test() throws Exception {
        List<Snake> list = getSnakes();


        RequestMessage requestMessage= new RequestMessage();

        requestMessage.setData(list);

        log.info("list : {}", list);

        Codec<Snake> snakeCodec = ProtobufProxy.create(Snake.class);
        snakeCodec.encode(null);

        


    }

    private List<Snake> getSnakes() {
        // cn name
        Faker faker = new Faker(Locale.CHINA);
        Name name = faker.name();

        int loop = 3;
        List<Snake> list = new ArrayList<>(loop);

        for (int i = 0; i < loop; i++) {
            Snake snake = new Snake();
            snake.id = i + 1;
            snake.name = name.lastName() + name.firstName();
            list.add(snake);
        }

        return list;
    }


}
