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
package com.iohao.game.widget.light.protobuf;

import lombok.AccessLevel;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2022-01-24
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class ProtoFieldTypeHolder {

    final Map<Class<?>, String> filedTypeMap = new HashMap<>();

    public String getProtoType(Class<?> filedTypeClass) {
        return filedTypeMap.get(filedTypeClass);
    }

    private void init() {
        filedTypeMap.put(Double.class, "double");
        filedTypeMap.put(double.class, "double");

        filedTypeMap.put(Float.class, "float");
        filedTypeMap.put(float.class, "float");

        filedTypeMap.put(Long.class, "sint64");
        filedTypeMap.put(long.class, "sint64");

        filedTypeMap.put(Integer.class, "sint32");
        filedTypeMap.put(int.class, "sint32");

        filedTypeMap.put(Boolean.class, "bool");
        filedTypeMap.put(boolean.class, "bool");

        filedTypeMap.put(String.class, "string");


        filedTypeMap.put(byte[].class, "bytes");

    }

    public ProtoFieldTypeHolder() {
        init();
    }

    public static ProtoFieldTypeHolder me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final ProtoFieldTypeHolder ME = new ProtoFieldTypeHolder();
    }

}
