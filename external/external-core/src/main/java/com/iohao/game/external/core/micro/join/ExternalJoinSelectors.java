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
package com.iohao.game.external.core.micro.join;

import com.iohao.game.common.kit.exception.ThrowKit;
import com.iohao.game.external.core.config.ExternalJoinEnum;
import lombok.experimental.UtilityClass;

import java.util.EnumMap;

/**
 * 连接方式 : ExternalJoinSelector
 * <pre>
 *     工厂方法
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-05-29
 */
@UtilityClass
public final class ExternalJoinSelectors {
    final EnumMap<ExternalJoinEnum, ExternalJoinSelector> map = new EnumMap<>(ExternalJoinEnum.class);

    public void putIfAbsent(ExternalJoinSelector joinSelector) {
        putIfAbsent(joinSelector.getExternalJoinEnum(), joinSelector);
    }

    public void putIfAbsent(ExternalJoinEnum joinEnum, ExternalJoinSelector joinSelector) {
        map.putIfAbsent(joinEnum, joinSelector);
    }

    /**
     * 根据连接方式得到对应的 ExternalJoinSelector
     *
     * @param joinEnum 连接方式
     * @return ExternalJoinSelector
     */
    public ExternalJoinSelector getExternalJoinSelector(ExternalJoinEnum joinEnum) {
        if (!map.containsKey(joinEnum)) {
            ThrowKit.ofRuntimeException(joinEnum + " 还没有对应的连接实现类");
        }

        return map.get(joinEnum);
    }
}
