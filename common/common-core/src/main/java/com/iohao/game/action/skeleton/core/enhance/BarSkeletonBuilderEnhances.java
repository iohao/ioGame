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
package com.iohao.game.action.skeleton.core.enhance;

import com.iohao.game.action.skeleton.core.BarSkeletonBuilder;
import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashSet;

import java.util.ServiceLoader;
import java.util.Set;

/**
 * @author 渔民小镇
 * @date 2023-06-16
 */
@UtilityClass
public class BarSkeletonBuilderEnhances {

    final Set<BarSkeletonBuilderEnhance> enhanceSet = new NonBlockingHashSet<>();

    static {
        ServiceLoader.load(BarSkeletonBuilderEnhance.class).forEach(BarSkeletonBuilderEnhances::add);
    }

    void add(BarSkeletonBuilderEnhance enhance) {
        enhanceSet.add(enhance);
    }

    public void enhance(BarSkeletonBuilder builder) {
        for (BarSkeletonBuilderEnhance enhance : enhanceSet) {
            enhance.enhance(builder);
        }
    }
}
