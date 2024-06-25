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
package com.iohao.game.action.skeleton.core.doc;

import com.iohao.game.action.skeleton.core.CmdKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Collection;
import java.util.Map;

/**
 * 广播相关文档
 *
 * @author 渔民小镇
 * @date 2024-06-25
 */
@Getter
@Setter(AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BroadcastDocRecordRegion {
    Map<Integer, BroadcastDocRecord> map = new NonBlockingHashMap<>();

    public void add(BroadcastDocRecord broadcastDocRecord) {
        int cmd = broadcastDocRecord.getCmd();
        int subCmd = broadcastDocRecord.getSubCmd();
        int merge = CmdKit.merge(cmd, subCmd);
        map.put(merge, broadcastDocRecord);
    }

    public Collection<BroadcastDocRecord> values() {
        return map.values();
    }
}
