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
package com.iohao.game.widget.light.protobuf.pojo;

import com.baidu.bjf.remoting.protobuf.annotation.EnableZigZap;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.widget.light.protobuf.ProtoFileMerge;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * teacher
 *
 * @author 渔民小镇
 * @date 2022-01-07
 */
@ProtobufClass
@EnableZigZap
@FieldDefaults(level = AccessLevel.PUBLIC)
@ToString
@ProtoFileMerge(fileName = TempProtoFile.commonFileName, filePackage = TempProtoFile.commonFilePackage)
public class ProtoTeacher {
    /** 姓名 */
    String name;
    int id;
    long age;
    /** 邮箱 */
    String email;

    Double doubleF;
    Float floatF;
    byte[] bytesF;

    Boolean boolF;
}
