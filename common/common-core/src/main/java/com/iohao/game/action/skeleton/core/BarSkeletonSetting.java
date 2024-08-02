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
public final class BarSkeletonSetting {
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

    /** false 关闭打印 */
    boolean print = true;
    /** true action 日志打印 */
    boolean printAction = true;
    /** false action 日志打印短名称(类、参数名、返回值) */
    boolean printActionShort = true;
    /** true inout 日志打印 */
    boolean printInout = true;
    /** true handler 日志打印 */
    boolean printHandler = true;
    /** true 编解码器日志打印 */
    boolean printDataCodec = true;

    /** true runners 日志打印 */
    boolean printRunners = true;

    /** inOut 的 in 。 true 开启 */
    @Deprecated
    boolean openIn = true;
    /** inOut 的 out 。 true 开启 */
    @Deprecated
    boolean openOut = true;

    /**
     * true : 业务参数开启 JSR380 验证规范
     *
     * <pre>
     *     关于启 JSR380 验证规范可以参考这里：
     *     <a href="https://www.yuque.com/iohao/game/ghng6g">文档 - JSR380</a>
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
    /** true 开启文档解析 */
    boolean parseDoc = true;
    /** true 生成文档 */
    boolean generateDoc = true;

    BarSkeletonSetting() {
    }
}
