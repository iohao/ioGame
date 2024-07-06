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
                .collect(Collectors.joining("\n"));

        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("className", this.className);
        messageMap.put("fieldsString", fieldsString);
        messageMap.put("classComment", this.comment);
        messageMap.put("classOrEnum", clazz.isEnum() ? "enum" : "message");

        String template = """
                // {classComment}
                {classOrEnum} {className} {
                {fieldsString}
                }
                                
                """;

        return StrKit.format(template, messageMap);
    }
}
