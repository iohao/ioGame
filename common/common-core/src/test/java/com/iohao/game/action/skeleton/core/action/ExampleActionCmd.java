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
package com.iohao.game.action.skeleton.core.action;


public interface ExampleActionCmd {
    /**
     * bee 模块功能
     */
    interface BeeActionCmd {
        /**
         * bee 模块 - 主 cmd
         */
        int cmd = 10;

        int hello = 0;
        int name = 1;
        int test_void = 3;
        int jsr380 = 4;
        int validated_group_update = 5;
        int validated_group_create = 6;
        int hello_dog = 7;
    }


    interface WrapperIntActionCmd {
        /**
         * bee 模块 - 主 cmd
         */
        int cmd = 11;

        int intValue2Void = 0;
        int intValue2Int = 1;
        int intValue2IntValue = 2;
        int intValue2IntList = 3;
        int intListVoid = 12;


        int int2Void = 4;
        int int2Int = 5;
        int int2IntValue = 6;
        int int2IntList = 7;

        int integer2Void = 8;
        int integer2Integer = 9;
        int integer2IntValue = 10;
        int integer2IntegerList = 11;

    }

    interface WrapperLongActionCmd {
        /**
         * bee 模块 - 主 cmd
         */
        int cmd = 12;

        int longValue2Void = 0;
        int longValue2Long = 1;
        int longValue2LongValue = 2;
        int longValue2LongList = 3;

        int long2Void = 4;
        int long2Long = 5;
        int long2LongValue = 6;
        int long2LongList = 7;

        int longer2Void = 8;
        int longer2Long = 9;
        int longer2LongValue = 10;
        int longer2LongList = 11;
    }

    interface SimpleWrapperActionActionCmd {
        int cmd = 13;
        int testInt = 0;
    }
}
