/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 命令域 管理器
 * <pre>
 *     管理命令域
 *
 *     路由与子路由的关系维护
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@FieldDefaults(level = AccessLevel.PACKAGE)
public class ActionCommandRegions {
    private static final ActionCommand[][] EMPTY = new ActionCommand[0][0];

    /**
     * action map
     * <pre>
     *     key : cmd
     *     value : subCmd region
     * </pre>
     */
    final Map<Integer, ActionCommandRegion> regionMap = new NonBlockingHashMap<>();

    /**
     * action 数组. 下标对应 cmd
     * <pre>
     *     第一维: cmd
     *     第二维: cmd 下面的子 subCmd
     *
     *     这里使用数组，没有使用 map 嵌入 map 的方式
     *     因为 map 嵌 map 的方式获取一个 action 需要多次 hash 和 equals 才能找到
     *     而通过数组则可以快速的找到对应的 action
     * </pre>
     */
    @Getter
    ActionCommand[][] actionCommands = EMPTY;

    /**
     * 获取命令处理器
     *
     * @param cmd    路由
     * @param subCmd 子路由
     * @return 命令处理器
     */
    public ActionCommand getActionCommand(int cmd, int subCmd) {

        if (cmd >= actionCommands.length) {
            return null;
        }

        var subActionCommands = actionCommands[cmd];

        if (subCmd >= subActionCommands.length) {
            return null;
        }

        return actionCommands[cmd][subCmd];
    }

    /**
     * 命令列表
     *
     * @return cmdMerge
     */
    public List<Integer> listCmdMerge() {
        return regionMap.values()
                // 并发流
                .parallelStream()
                // 将 map.values 合并成一个 list
                .flatMap((Function<ActionCommandRegion, Stream<ActionCommand>>) actionCommandRegion -> actionCommandRegion.values().parallelStream())
                // 转为命令路由信息
                .map(ActionCommand::getCmdInfo)
                // 转为 合并的路由
                .map(CmdInfo::getCmdMerge)
                .collect(Collectors.toList())
                ;
    }

    ActionCommandRegion getActionCommandRegion(int cmd) {

        var actionCommandRegion = this.regionMap.get(cmd);

        // 无锁化
        if (Objects.isNull(actionCommandRegion)) {
            actionCommandRegion = new ActionCommandRegion(cmd);
            actionCommandRegion = this.regionMap.putIfAbsent(cmd, actionCommandRegion);
            if (Objects.isNull(actionCommandRegion)) {
                actionCommandRegion = this.regionMap.get(cmd);
            }
        }

        return actionCommandRegion;
    }

    void initActionCommandArray(BarSkeletonSetting barSkeletonSetting) {
        this.actionCommands = this.convertArray(barSkeletonSetting);
    }

    /**
     * 将 map 转换成二维数组
     * <pre>
     *     第一维: cmd
     *     第二维: cmd 下面的子 subCmd
     * </pre>
     *
     * @param barSkeletonSetting config
     * @return 二维数组
     */
    private ActionCommand[][] convertArray(BarSkeletonSetting barSkeletonSetting) {

        if (this.regionMap.isEmpty()) {
            return EMPTY;
        }

        // 获取主路由最大值
        int max = getMaxCmd(barSkeletonSetting);

        var behaviors = new ActionCommand[max][1];

        this.regionMap.keySet().forEach(cmd -> {
            var actionCommandRegion = this.regionMap.get(cmd);

            behaviors[cmd] = actionCommandRegion.arrayActionCommand();
        });

        return behaviors;
    }

    private int getMaxCmd(BarSkeletonSetting barSkeletonSetting) {
        // 获取最大的路由数字 并且+1
        int max = this.regionMap
                .keySet()
                .stream()
                .max(Integer::compareTo)
                .orElse(0) + 1;

        if (max > barSkeletonSetting.getCmdMaxLen()) {

            String info = String.format("cmd 超过最大默认值! 如果有需要, 请手动设置容量!  默认最大容量 %s. 当前容量 %s"
                    , barSkeletonSetting.getCmdMaxLen(), max
            );

            throw new RuntimeException(info);
        }

        // subCmd
        for (ActionCommandRegion actionCommandRegion : this.regionMap.values()) {
            int subCmdMax = actionCommandRegion.getMaxSubCmd() + 1;

            if (subCmdMax > barSkeletonSetting.getSubCmdMaxLen()) {

                String info = String.format("subCmd 超过最大默认值! 如果有需要, 请手动设置容量!  默认最大容量 %s. 当前容量 %s"
                        , barSkeletonSetting.getSubCmdMaxLen(), subCmdMax
                );

                throw new RuntimeException(info);
            }
        }

        return max;
    }
}
