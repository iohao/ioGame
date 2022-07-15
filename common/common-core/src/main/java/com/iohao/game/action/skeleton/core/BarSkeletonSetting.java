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
package com.iohao.game.action.skeleton.core;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务框架 Setting
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@Getter
@Setter
public class BarSkeletonSetting {
    /**
     * <pre>
     *     true: action 对象是 single.
     *     false: 每次都创建新的 action 对象.
     * </pre>
     */
    boolean createSingleActionCommandController = true;

    /** action 的默认长度 (一级 cmd; 主路由) */
    int cmdMaxLen = 127;
    /** 子 action 的默认长度 (二级 subCmd; 子路由) */
    int subCmdMaxLen = 127;

    /** true action 日志打印 */
    boolean printAction = true;
    /** false action 日志打印短名称(类、参数名、返回值) */
    boolean printActionShort = true;
    /** true inout 日志打印 */
    boolean printInout = true;
    /** true handler 日志打印 */
    boolean printHandler = true;

    /** inOut 的 in 。 true 开启 */
    boolean openIn = true;
    /** inOut 的 out 。 true 开启 */
    boolean openOut = true;
    /** 解析类型 */
    ParseType parseType = ParseType.PB;

    /**
     * true : 业务参数开启 JSR380 验证规范
     *
     * <pre>
     *     关于启 JSR380 验证规范可以参考这里：
     *     https://www.yuque.com/iohao/game/ghng6g
     * </pre>
     * <p>
     * 需要在你的项目 maven 中引入相关 pom
     * 具体参考 https://www.yuque.com/iohao/game/ghng6g#qRSEu
     * <pre>
     *         &lt;!-- hibernate validator -->
     *         &lt;dependency>
     *             &lt;groupId>org.hibernate.validator&lt;/groupId>
     *             &lt;artifactId>hibernate-validator&lt;/artifactId>
     *             &lt;version>7.0.4.Final&lt;/version>
     *         &lt;/dependency>
     *
     *         &lt;!-- EL实现。在Java SE环境中，您必须将实现作为依赖项添加到POM文件中-->
     *         &lt;dependency>
     *             &lt;groupId>org.glassfish&lt;/groupId>
     *             &lt;artifactId>jakarta.el&lt;/artifactId>
     *             &lt;version>4.0.2&lt;/version>
     *         &lt;/dependency>
     *
     *         &lt;!-- 验证器Maven依赖项 -->
     *         &lt;dependency>
     *             &lt;groupId>jakarta.validation&lt;/groupId>
     *             &lt;artifactId>jakarta.validation-api&lt;/artifactId>
     *             &lt;version>3.0.2&lt;/version>
     *         &lt;/dependency>
     * </pre>
     */
    boolean validator = false;
}
