/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
