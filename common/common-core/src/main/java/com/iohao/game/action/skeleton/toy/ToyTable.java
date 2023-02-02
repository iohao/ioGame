/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.toy;

import org.fusesource.jansi.Ansi;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2023-01-30
 */
final class ToyTable {
    Ansi.Color color = BannerColorStrategy.anyColor();
    Map<String, ToyTableRegion> regionMap = new LinkedHashMap<>();
    ToyTableRender tableRender;
    int bodyMaxNum;

    void render() {

        this.bodyMaxNum = this.countRegionMaxLine();
        this.tableRender = new ToyTableRender(bodyMaxNum);

        int size = this.regionMap.size();
        int eleNum = 0;

        for (ToyTableRegion region : regionMap.values()) {
            region.render();
            eleNum++;

            boolean lastEle = size == eleNum;
            if (!lastEle) {
                tableRender.next();
            }
        }

        this.tableRender.render();

    }

    int countRegionMaxLine() {
        int lineMax = 0;

        for (ToyTableRegion region : this.regionMap.values()) {

            int size = region.lineMap.size();
            if (size > lineMax) {
                lineMax = size;
            }
        }

        return lineMax;
    }

    ToyTableRegion getRegion(String regionName) {

        ToyTableRegion region = this.regionMap.get(regionName);

        if (Objects.isNull(region)) {

            region = new ToyTableRegion();
            region.head = regionName;
            region.table = this;

            this.regionMap.putIfAbsent(regionName, region);
            region = this.regionMap.get(regionName);
        }

        return region;
    }


}
