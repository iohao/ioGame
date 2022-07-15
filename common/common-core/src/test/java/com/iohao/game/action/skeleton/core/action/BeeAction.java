/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.action.skeleton.core.action;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.core.action.pojo.BeeApple;
import com.iohao.game.action.skeleton.core.action.pojo.DogValid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActionController(ExampleActionCont.BeeActionCont.cmd)
public class BeeAction {

    /**
     * <pre>
     *     打招呼
     *     实现注解 ActionMethod 告知框架这是一个对外开放的action (即一个方法就是一个对外的处理)
     * </pre>
     *
     * @param beeApple a
     * @return 返回具体信息
     */
    @ActionMethod(ExampleActionCont.BeeActionCont.hello)
    public BeeApple hello(BeeApple beeApple) {
        // 响应给客户端的数据 string 类型. 框架可根据返回参数类型将返回结果装到响应体中
        BeeApple that = new BeeApple();
        that.setContent(beeApple.content + "，I'm hello");
        return that;
    }

    @ActionMethod(ExampleActionCont.BeeActionCont.name)
    public BeeApple name(BeeApple beeApple) {
        log.debug("beeApple: {}", beeApple);
        // 响应给客户端的数据 string 类型. 框架可根据返回参数类型将返回结果装到响应体中
        BeeApple that = new BeeApple();
        that.setContent(beeApple.content + "，I'm name");
        return that;
    }

    @ActionMethod(ExampleActionCont.BeeActionCont.test_void)
    public void thatVoid(BeeApple beeApple) {
        // 响应给客户端的数据 string 类型. 框架可根据返回参数类型将返回结果装到响应体中
        BeeApple that = new BeeApple();
        that.setContent(beeApple.content + "，I'm thatVoid");
    }

    @ActionMethod(ExampleActionCont.BeeActionCont.jsr380)
    public void jsr380(DogValid dogValid) {
        log.info("dogValid : {}", dogValid);
    }
}
