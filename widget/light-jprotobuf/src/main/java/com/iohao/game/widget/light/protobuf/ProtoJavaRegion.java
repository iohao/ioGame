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

import java.util.*;

/**
 * @author 渔民小镇
 * @date 2022-01-25
 */
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class ProtoJavaRegion {
    String fileName;
    String filePackage;

    final Map<Class<?>, ProtoJava> protoJavaMap = new HashMap<>();
    final List<ProtoJava> protoJavaList = new ArrayList<>();
    final ProtoJavaRegionHead regionHead = new ProtoJavaRegionHead();

    public void addProtoJava(ProtoJava protoJava) {
        this.protoJavaList.add(protoJava);
        this.protoJavaMap.put(protoJava.getClazz(), protoJava);
    }

    public void addOtherProtoFile(ProtoJava protoJava) {
        String fileName = protoJava.getFileName();
        this.regionHead.fileNameSet.add(fileName);
    }

    static class ProtoJavaRegionHead {
        Set<String> fileNameSet = new HashSet<>();
        String filePackage;

        private String toProtoHead() {

            String templateFileName = """
                    import "{}";
                    """;

            StringBuilder fileNameBuilder = new StringBuilder();

            for (String filePackage : fileNameSet) {
                String filePackageString = StrKit.format(templateFileName, filePackage);
                fileNameBuilder.append(filePackageString);
            }


            String templateHead = """
                    syntax = "proto3";
                    package {};
                    {}
                    """;

            return StrKit.format(templateHead, this.filePackage, fileNameBuilder.toString());
        }
    }

    public String toProtoFile() {
        this.regionHead.filePackage = this.filePackage;
        String protoHead = this.regionHead.toProtoHead();

        StringBuilder builder = new StringBuilder();
        builder.append(protoHead);

        this.protoJavaList.stream()
                // 排序规则
                .sorted(Comparator.comparing(ProtoJava::getClassName))
                .map(ProtoJava::toProtoMessage)
                .forEach(builder::append);

        return builder.toString();
    }
}
