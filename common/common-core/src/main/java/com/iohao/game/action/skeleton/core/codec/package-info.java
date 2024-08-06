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
 * 业务框架 - 业务数据的编解码器，<a href="https://www.yuque.com/iohao/game/uq2zrltrc7to27bt">扩展协议</a>。
 * <br/>
 * 简介
 * <pre>
 *     ioGame 支持同样的一套业务代码，且无需做变更任何业务代码就能支持序列化库的切换，如 protobuf、json，或其他的扩展序列化库。
 *     在 ioGame 中切换协议是简单的，只需要一行代码。这种扩展方式基本做到了：零迁移成本、零维护成本、零学习成本。
 * </pre>
 * <p>
 * 下面，我们通过一个示例来简单的说明。
 * <p>
 * 使用 java 类来定义一个业务数据协议
 * <pre>{@code
 * @ProtobufClass
 * @FieldDefaults(level = AccessLevel.PUBLIC)
 * public class Student {
 *     String name;
 * }
 * }</pre>
 * <p>
 * ioGame 中的业务数据协议是通过 java 类来定义的，这么做的优点有
 * <pre>
 *     1. 即使是 java 新手也能看得明白，而通过 DSL 生成的 java 代码，是不可能这么清晰的。
 *     2. 同时，java 类定义的协议既能支持 protobuf ，又能支持 json ，且开发者无需做任何变更，这点是 DSL 做不到的。
 *     3. 还能支持 <a href="https://www.yuque.com/iohao/game/ghng6g">JSR380 验证</a>，在属性上添加 JSR380 相关注解即可，这点是 DSL 做不到的。
 *     4. 此外，还能在协议类中添加一些自定义方法，这点是 DSL 做不到的。
 *     5. 减少学习成本，不需要学习各种 DSL 相关库的语法。
 *
 *     结论，支持 java 代码定义协议的序列化库，会优先做支持。比如，将来支持了 Fury，那么开发者无需变更现有的业务代码，就能直接使用。从而为开发者减少迁移成本、维护成本、学习成本。
 *
 *     相关讨论 <a href="https://github.com/iohao/ioGame/issues/317">issue-317</a>
 * </pre>
 * <p>
 * 协议切换示例
 * <pre>{@code
 * public void chooseCodec() {
 *     // 使用 JSON 编解码
 *     IoGameGlobalSetting.setDataCodec(new JsonDataCodec());
 *     // 使用 Protobuf 编解码
 *     IoGameGlobalSetting.setDataCodec(new ProtoDataCodec());
 *     // 使用 Fury 编解码
 *     IoGameGlobalSetting.setDataCodec(new FuryDataCodec());
 * }
 * }</pre>
 * 不对现有的业务代码做变更，只需设置当前所使用的编解码器就能切换序列化库。
 * 之后，如果支持了 Fury，我们只需要扩展一个 FuryDataCodec 编解码器即可。
 * 这种扩展方式基本做到了：零迁移成本、零维护成本、零学习成本。
 *
 * @author 渔民小镇
 * @date 2024-08-05
 */
package com.iohao.game.action.skeleton.core.codec;