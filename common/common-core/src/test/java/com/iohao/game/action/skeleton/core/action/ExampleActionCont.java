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
package com.iohao.game.action.skeleton.core.action;


public interface ExampleActionCont {
    /**
     * bee 模块功能
     */
    interface BeeActionCont {
        /**
         * bee 模块 - 主 cmd
         */
        int cmd = 10;

        int hello = 0;
        int name = 1;
        int test_void = 3;
        int jsr380 = 4;
    }

    interface WrapperIntActionCont {
        /**
         * bee 模块 - 主 cmd
         */
        int cmd = 11;

        int intPb2Void = 0;
        int intPb2Int = 1;
        int intPb2IntPb = 2;
        int intPb2IntList = 3;
        int intListVoid = 12;


        int int2Void = 4;
        int int2Int = 5;
        int int2IntPb = 6;
        int int2IntList = 7;

        int integer2Void = 8;
        int integer2Integer = 9;
        int integer2IntPb = 10;
        int integer2IntegerList = 11;

    }

    interface WrapperLongActionCont {
        /**
         * bee 模块 - 主 cmd
         */
        int cmd = 12;

        int longPb2Void = 0;
        int longPb2Long = 1;
        int longPb2LongPb = 2;
        int longPb2LongList = 3;

        int long2Void = 4;
        int long2Long = 5;
        int long2LongPb = 6;
        int long2LongList = 7;

        int longer2Void = 8;
        int longer2Long = 9;
        int longer2LongPb = 10;
        int longer2LongList = 11;
    }
}
