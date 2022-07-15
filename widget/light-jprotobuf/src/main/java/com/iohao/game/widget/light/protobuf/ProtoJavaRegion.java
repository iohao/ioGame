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

        for (ProtoJava protoJava : protoJavaList) {
            String protoMessage = protoJava.toProtoMessage();
            builder.append(protoMessage);
        }

        return builder.toString();
    }
}
