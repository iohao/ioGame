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
/**
 * 生态融合（集成扩展）- 在生态融合方面，ioGame 可以很方便的与 <a href="https://www.yuque.com/iohao/game/evkgnz">spring 集成（5 行代码）</a>，从而能方便的使用其相关生态。
 * <pre>{@code
 * @SpringBootApplication
 * public class DemoSpringApplication {
 *     @Bean
 *     public ActionFactoryBeanForSpring actionFactoryBean() {
 *         // 将业务框架交给 spring 管理
 *         return ActionFactoryBeanForSpring.me();
 *     }
 * }
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2024-08-07
 */
package com.iohao.game.action.skeleton.ext.spring;