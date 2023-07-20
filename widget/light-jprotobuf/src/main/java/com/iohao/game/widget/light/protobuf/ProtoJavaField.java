/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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

        String templateFiled = getTemplateFiled(this.protoJavaParent.getClazz().isEnum());

        return StrKit.format(templateFiled, messageMap);
    }

    /**
     * 生成proto文本模板
     *
     * @param fieldIsInEnum 该bool含义表示当前Field所在的类文件是否是枚举类型，取得是protoJavaParent的isEnum
     * @return 如果是枚举类文件中，属性前面不用加{fieldProtoType}，如果枚举类型是在类文件中则加上{fieldProtoType}
     */
    private String getTemplateFiled(boolean fieldIsInEnum) {
        StringBuilder templateFiled = new StringBuilder();

        if (!Objects.isNull(this.comment)) {
            templateFiled.append("""
                      // {comment}
                    """);
        }

        if (fieldIsInEnum) {
            templateFiled.append("  {repeated}{fieldName} = {order};");
        } else {
            templateFiled.append("  {repeated}{fieldProtoType} {fieldName} = {order};");
        }

        return templateFiled.toString();
    }

}
