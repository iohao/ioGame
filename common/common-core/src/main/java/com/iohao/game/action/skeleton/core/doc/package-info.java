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
 * 业务框架 - <a href="https://www.yuque.com/iohao/game/irth38">代码生成</a>，你只需要编写一次 java 代码，就能为 Unity、Godot、CocosCreator、Laya、Vue 等前端项目生成交互接口。
 * <pre>{@code
 * public static void main(String[] args) {
 *     // 添加枚举错误码 class，用于生成错误码相关信息
 *     IoGameDocumentHelper.addErrorCodeClass(GameCode.class);
 *
 *     // 添加代码生成器，生成 C# 联调代码。（21.20 支持）
 *     // IoGameDocumentHelper.addDocumentGenerate(new CsharpDocumentGenerate());
 *     // 添加代码生成器，生成 Ts 联调代码。（21.21 支持）
 *     // IoGameDocumentHelper.addDocumentGenerate(new TypeScriptDocumentGenerate());
 *     // 生成文档
 *     IoGameDocumentHelper.generateDocument();
 * }
 * }</pre>
 * 介绍
 * <pre>
 *     ioGame 是非常注重开发体验的，代码注释即文档、方法即交互接口的原则。
 *
 *     ioGame 具备一次编写到处对接的能力，从而做到了你们团队提升巨大的生产力可能性。
 *     一次编写指的是编写一次 java 业务代码，而到处对接则是指为不同的前端项目生成与服务器交互的代码。
 *
 *     ioGame 能为各种前端项目生成 action、广播、错误码 相关接口代码。
 *     这将意味着，你只需要编写一次业务代码，就可以同时与这些游戏引擎或现代化的前端框架交互。
 * </pre>
 * 代码生成的几个优势
 * <pre>
 *     1. 帮助客户端开发者减少巨大的工作量，不需要编写大量的模板代码。
 *     2. 语义明确，清晰。生成的交互代码即能明确所需要的参数类型，又能明确服务器是否会有返回值。这些会在生成接口时就提前明确好。
 *     3. 由于我们可以做到明确交互接口，进而可以明确参数类型。这使得接口方法参数类型安全、明确，从而有效避免安全隐患，从而减少联调时的低级错误。
 *     4. 减少服务器与客户端双方对接时的沟通成本，代码即文档。生成的联调代码中有文档与使用示例，方法上的示例会教你如何使用，即使是新手也能做到零学习成本。
 *     5. 帮助客户端开发者屏蔽与服务器交互部分，将更多的精力放在真正的业务上。
 *     6. 为双方联调减少心智负担。联调代码使用简单，与本地方法调用一般丝滑。
 *     7. 抛弃传统面向协议对接的方式，转而使用面向接口方法的对接方式。
 *     8. 当我们的 java 代码编写完成后，我们的文档及交互接口可做到同步更新，不需要额外花时间去维护对接文档及其内容。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-08-05
 */
package com.iohao.game.action.skeleton.core.doc;