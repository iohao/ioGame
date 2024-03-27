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
package com.iohao.game.action.skeleton.protocol.collect;

import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 模块之间的访问，访问【同类型】的多个逻辑服
 * <pre>
 *     如： 模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据
 *     是把多个相同逻辑服结果聚合在一起
 *
 *     具体的意思可以参考文档中的说明：
 *     https://www.yuque.com/iohao/game/nelwuz#gSdya
 *     https://www.yuque.com/iohao/game/rf9rb9
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseCollectMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -2510491415150451094L;

    List<ResponseCollectItemMessage> messageList;
    /**
     * 响应码: 0:成功, 其他表示有错误
     * <pre>
     *     通常情况下，如果这里有错误码，基本是没有请求对应的逻辑服
     * </pre>
     */
    int statusCode;
    /** 错误信息 */
    String statusMes;

    public ResponseCollectMessage setError(MsgExceptionInfo msgExceptionInfo) {
        this.statusCode = msgExceptionInfo.getCode();
        this.statusMes = msgExceptionInfo.getMsg();
        return this;
    }

    public boolean success() {
        return statusCode == 0;
    }
}
