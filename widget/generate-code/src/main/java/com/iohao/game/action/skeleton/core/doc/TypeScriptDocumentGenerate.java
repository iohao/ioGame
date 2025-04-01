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
package com.iohao.game.action.skeleton.core.doc;

import com.iohao.game.action.skeleton.protocol.wrapper.BoolValue;
import com.iohao.game.action.skeleton.protocol.wrapper.IntValue;
import com.iohao.game.action.skeleton.protocol.wrapper.LongValue;
import com.iohao.game.action.skeleton.protocol.wrapper.StringValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Template;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Generate TypeScript code, such as broadcast, error code, action
 *
 * @author 渔民小镇
 * @date 2024-12-01
 * @since 21.21
 */
@Slf4j
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class TypeScriptDocumentGenerate extends AbstractDocumentGenerate {
    //    String protoPrefix = "ioGame.";
    String protoPrefix = "";
    @Setter(AccessLevel.PRIVATE)
    TypeScriptAnalyseImport analyseImport;

    public TypeScriptDocumentGenerate() {
        this.typeMappingDocument = new TypeScriptMappingDocument(this);
    }

    @Override
    public void generate(IoGameDocument ioGameDocument) {
        Objects.requireNonNull(this.path);

        InternalProtoClassKit.analyseProtoClass(ioGameDocument);
        Map<Class<?>, ProtoFileMergeClass> protoClassMap = InternalProtoClassKit.protoClassMap;
        this.analyseImport = new TypeScriptAnalyseImport(protoClassMap.values());

        this.generateAction(ioGameDocument);
        this.generateBroadcast(ioGameDocument);
        this.generateErrorCode(ioGameDocument);

        log.info("TypeScriptDocumentGenerate success: {}", this.path);
    }

    @Override
    protected void generateErrorCode(IoGameDocument ioGameDocument) {
        Template template = ofTemplate(DocumentGenerateKit.gameCodeTemplatePath);

        new GameCodeGenerate()
                .setIoGameDocument(ioGameDocument)
                .setInternalErrorCode(this.internalErrorCode)
                .setTemplate(template)
                .setFilePath(this.path)
                .setFileSuffix(".ts")
                .generate();
    }

    @Override
    protected void generateBroadcast(IoGameDocument ioGameDocument) {

        List<Class<?>> protoMessageClassList = new ArrayList<>();
        ioGameDocument.getBroadcastDocumentList().forEach(broadcastDocument -> {
            Class<?> dataClass = broadcastDocument.getDataClass();
            TsProtoMessage bizDataProtoMessage = this.analyseImport.getProtoMessage(dataClass);
            if (Objects.nonNull(bizDataProtoMessage)) {
                protoMessageClassList.add(dataClass);
            }
        });

        String imports = this.analyseImport.getImportPath(protoMessageClassList);

        Template template = ofTemplate(DocumentGenerateKit.broadcastActionTemplatePath);
        template.binding("imports", imports);

        new BroadcastGenerate()
                .setIoGameDocument(ioGameDocument)
                .setTypeMappingDocument(typeMappingDocument)
                .setTemplateCreator(this::ofTemplate)
                .setTemplate(template)
                .setFilePath(this.path)
                .setFileSuffix(".ts")
                .generate();
    }

    @Override
    protected void generateAction(IoGameDocument ioGameDocument) {
        List<ActionDocument> actionDocumentList = DocumentAnalyseKit.analyseActionDocument(ioGameDocument, typeMappingDocument);

        List<Class<?>> protoMessageClassList = new ArrayList<>();
        actionDocumentList.forEach(actionDocument -> {

            // collect the action inputParam and outputParam
            List<ActionMethodDocument> actionMethodDocumentList = actionDocument.getActionMethodDocumentList();
            actionMethodDocumentList.forEach(actionMethodDocument -> {
                Class<?> bizDataTypeClazz = actionMethodDocument.bizDataTypeClazz;
                Class<?> returnTypeClazz = actionMethodDocument.returnTypeClazz;

                TsProtoMessage bizDataProtoMessage = this.analyseImport.getProtoMessage(bizDataTypeClazz);
                TsProtoMessage returnProtoMessage = this.analyseImport.getProtoMessage(returnTypeClazz);

                if (Objects.nonNull(bizDataProtoMessage)) {
                    protoMessageClassList.add(bizDataProtoMessage.dataClass);
                }

                if (Objects.nonNull(returnProtoMessage)) {
                    protoMessageClassList.add(returnProtoMessage.dataClass);
                }
            });

            String imports = this.analyseImport.getImportPath(protoMessageClassList);

            Template template = ofTemplate(DocumentGenerateKit.actionTemplatePath);
            // imports
//            template.binding("imports", String.join("\n", this.actionImportList));
            template.binding("imports", imports);

            new ActionGenerate()
                    .setActionDocument(actionDocument)
                    .setTemplate(template)
                    .setFilePath(this.path)
                    .setFileSuffix(".ts")
                    .setTemplateCreator(this::ofTemplate)
                    .generate();
        });
    }

    private Template ofTemplate(String fileName) {
        var template = DocumentGenerateKit.getTemplate("ts/" + fileName);
        template.binding("protoPrefix", Objects.isNull(this.protoPrefix) ? "" : this.protoPrefix);
        return template;
    }

    private static class TypeScriptMappingDocument implements TypeMappingDocument {
        @Getter
        final Map<Class<?>, TypeMappingRecord> map = new HashMap<>();
        final TypeScriptDocumentGenerate documentGenerate;

        public TypeScriptMappingDocument(TypeScriptDocumentGenerate documentGenerate) {
            this.documentGenerate = documentGenerate;
            extractedInitTypeMapping();
        }

        private void extractedInitTypeMapping() {
            // about int
            var intTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("number").setListParamTypeName("number[]")
                    .setOfMethodTypeName("Int").setOfMethodListTypeName("IntList")
                    .setResultMethodTypeName("getInt").setResultMethodListTypeName("listInt");

            this.mapping(intTypeMapping, List.of(int.class, Integer.class, IntValue.class));

            // about long
            var longTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("bigint").setListParamTypeName("bigint[]")
                    .setOfMethodTypeName("Long").setOfMethodListTypeName("LongList")
                    .setResultMethodTypeName("getLong").setResultMethodListTypeName("listLong");

            this.mapping(longTypeMapping, List.of(long.class, Long.class, LongValue.class));

            // about boolean
            var boolTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("boolean").setListParamTypeName("boolean[]")
                    .setOfMethodTypeName("Bool").setOfMethodListTypeName("BoolList")
                    .setResultMethodTypeName("getBool").setResultMethodListTypeName("listBool");

            this.mapping(boolTypeMapping, List.of(boolean.class, Boolean.class, BoolValue.class));

            // about String
            var stringTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("string").setListParamTypeName("string[]")
                    .setOfMethodTypeName("String").setOfMethodListTypeName("StringList")
                    .setResultMethodTypeName("getString").setResultMethodListTypeName("listString");

            this.mapping(stringTypeMapping, List.of(String.class, StringValue.class));
        }

        @Override
        public TypeMappingRecord getTypeMappingRecord(Class<?> protoTypeClazz) {
            var map = getMap();
            if (map.containsKey(protoTypeClazz)) {
                return map.get(protoTypeClazz);
            }

            var analyseImport = documentGenerate.analyseImport;
            var protoMessage = analyseImport.getProtoMessage(protoTypeClazz);

            String paramTypeName;
            if (Objects.nonNull(protoMessage)) {
                paramTypeName = protoMessage.getFullParamTypeName();
            } else {
                paramTypeName = protoTypeClazz.getSimpleName();
            }

            return new TypeMappingRecord()
                    .setInternalType(false)
                    .setParamTypeName(paramTypeName).setListParamTypeName("%s[]".formatted(paramTypeName))
                    .setOfMethodTypeName("").setOfMethodListTypeName("ValueList")
                    .setResultMethodTypeName("getValue").setResultMethodListTypeName("listValue");
        }
    }

    private static class TypeScriptAnalyseImport {
        final Map<Class<?>, TsProtoMessage> map = new HashMap<>();

        TypeScriptAnalyseImport(Collection<ProtoFileMergeClass> messageList) {
            messageList.forEach(protoFileMergeClass -> {
                String fileName = protoFileMergeClass.fileName();
                String simpleFileName = fileName.substring(0, fileName.lastIndexOf(".proto"));
                String importFileName = "%s_pb".formatted(simpleFileName);

                var message = new TsProtoMessage(importFileName, protoFileMergeClass.dataClass());
                map.put(message.dataClass, message);
            });
        }

        TsProtoMessage getProtoMessage(Class<?> dataClass) {
            return map.get(dataClass);
        }

        String getImportPath(List<? extends Class<?>> dataClassList) {
            Set<String> importFileNameSet = new HashSet<>();
            for (Class<?> dataClass : dataClassList) {
                TsProtoMessage protoMessage = getProtoMessage(dataClass);
                String importFileName = protoMessage.importFileName;
                importFileNameSet.add(importFileName);
            }

            // ts 只需要导入文件
            return importFileNameSet.stream().map(importFileName -> {
                String tpl = "import * as %s from \"../%s\";";
                return tpl.formatted(importFileName, importFileName);
            }).collect(Collectors.joining("\n"));
        }
    }

    private record TsProtoMessage(String importFileName, Class<?> dataClass) {
        String getFullParamTypeName() {
            return importFileName + "." + dataClass.getSimpleName();
        }
    }
}
