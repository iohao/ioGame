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

import com.iohao.game.common.kit.StrKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2022-01-24
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class ProtoJavaField {
    boolean repeated;
    String fieldName;
    String comment;
    int order;
    Class<?> fieldTypeClass;
    String fieldProtoType;
    Field field;

    ProtoJava protoJavaParent;

    boolean isMap() {
        return Map.class.equals(fieldTypeClass);
    }

    boolean isList() {
        return List.class.equals(fieldTypeClass);
    }

    private Map<String, String> createParam() {
        Map<String, String> messageMap = new HashMap<>();

        messageMap.put("comment", this.comment);
        messageMap.put("repeated", "");
        messageMap.put("fieldProtoType", this.fieldProtoType);
        messageMap.put("fieldName", this.fieldName);
        messageMap.put("order", String.valueOf(this.order));

        if (this.repeated) {
            messageMap.put("repeated", "repeated ");
        }

        return messageMap;
    }

    public String toProtoFieldLine() {
        Map<String, String> messageMap = this.createParam();

        String templateFiled = getTemplateFiled();

        return StrKit.format(templateFiled, messageMap);
    }

    private String getTemplateFiled() {
        String templateFiled = """
                  // {comment}
                  {repeated}{fieldProtoType} {fieldName} = {order};
                """;

        if (Objects.isNull(this.comment)) {
            templateFiled = """
                      {repeated}{fieldProtoType} {fieldName} = {order};
                    """;
        }

        return templateFiled;
    }
}
