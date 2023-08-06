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
package com.iohao.game.action.skeleton.core.action;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.annotation.ValidatedGroup;
import com.iohao.game.action.skeleton.core.action.group.Create;
import com.iohao.game.action.skeleton.core.action.group.Update;
import com.iohao.game.action.skeleton.core.action.pojo.BeeApple;
import com.iohao.game.action.skeleton.core.action.pojo.BirdValid;
import com.iohao.game.action.skeleton.core.action.pojo.DogValid;
import com.iohao.game.common.consts.IoGameLogName;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = IoGameLogName.CommonStdout)
@ActionController(ExampleActionCmd.BeeActionCmd.cmd)
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
    @ActionMethod(ExampleActionCmd.BeeActionCmd.hello)
    public BeeApple hello(BeeApple beeApple) {
        BeeApple that = new BeeApple();
        that.setContent(beeApple.content + "，I'm hello");
        return that;
    }

    @ActionMethod(ExampleActionCmd.BeeActionCmd.name)
    public BeeApple name(BeeApple beeApple) {
        log.debug("beeApple: {}", beeApple);
        BeeApple that = new BeeApple();
        that.setContent(beeApple.content + "，I'm name");
        return that;
    }

    @ActionMethod(ExampleActionCmd.BeeActionCmd.test_void)
    public void thatVoid(BeeApple beeApple) {
        BeeApple that = new BeeApple();
        that.setContent(beeApple.content + "，I'm thatVoid");
    }

    @ActionMethod(ExampleActionCmd.BeeActionCmd.jsr380)
    public void jsr380(DogValid dogValid) {
        log.info("dogValid : {}", dogValid);
    }

    @ActionMethod(ExampleActionCmd.BeeActionCmd.validated_group_update)
    public void validateUpdate(@ValidatedGroup(value = Update.class) BirdValid birdValid) {
        log.info("dogValid : {}", birdValid);
    }

    @ActionMethod(ExampleActionCmd.BeeActionCmd.validated_group_create)
    public void validateCreate(@ValidatedGroup(value = Create.class) BirdValid birdValid) {
        log.info("dogValid : {}", birdValid);
    }
}
