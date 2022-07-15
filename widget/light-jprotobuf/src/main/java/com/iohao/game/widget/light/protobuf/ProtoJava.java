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
import com.thoughtworks.qdox.model.JavaClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 渔民小镇
 * @date 2022-01-24
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class ProtoJava {
    Class<?> clazz;
    String className;

    String comment;

    String fileName;
    String filePackage;

    JavaClass javaClass;

    List<ProtoJavaField> protoJavaFieldList = new ArrayList<>();

    public void addProtoJavaFiled(ProtoJavaField protoJavaField) {
        this.protoJavaFieldList.add(protoJavaField);
    }

    public boolean inThisFile(ProtoJava protoJava) {
        return Objects.equals(this.fileName, protoJava.fileName) && Objects.equals(this.filePackage, protoJava.filePackage);
    }

    public ProtoJavaRegionKey getProtoJavaRegionKey() {
        return new ProtoJavaRegionKey(this.fileName, this.filePackage);

    }

    public String toProtoMessage() {

        String fieldsString = protoJavaFieldList
                .stream()
                .map(ProtoJavaField::toProtoFieldLine)
                .collect(Collectors.joining());

        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("className", this.className);
        messageMap.put("fieldsString", fieldsString);
        messageMap.put("classComment", this.comment);

        String template = """
                // {classComment}
                message {className} {
                {fieldsString}
                }
                                
                """;

        return StrKit.format(template, messageMap);
    }

}
