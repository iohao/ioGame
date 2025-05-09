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

import com.iohao.game.common.kit.StrKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Template;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * @author 渔民小镇
 * @date 2025-05-09
 * @since 21.27
 */
@Slf4j
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class GDScriptDocumentGenerate extends AbstractDocumentGenerate {

    @Setter(AccessLevel.PRIVATE)
    GDScriptAnalyseImport analyseImport;

    public GDScriptDocumentGenerate() {
        this.typeMappingDocument = new GDScriptTypeMappingDocument(this);
    }

    @Override
    public void generate(IoGameDocument ioGameDocument) {
        InternalProtoClassKit.analyseProtoClass(ioGameDocument);
        Map<Class<?>, ProtoFileMergeClass> protoClassMap = InternalProtoClassKit.protoClassMap;
        this.analyseImport = new GDScriptAnalyseImport(protoClassMap.values());

        this.generateAction(ioGameDocument);
        this.generateBroadcast(ioGameDocument);
        this.generateErrorCode(ioGameDocument);

        log.info("GDScriptDocumentGenerate success: {}", this.path);
    }


    @Override
    protected void generateAction(IoGameDocument ioGameDocument) {
        List<ActionDocument> actionDocumentList = DocumentAnalyseKit.analyseActionDocument(ioGameDocument, typeMappingDocument);
        extractedSnakeName(actionDocumentList);
        List<Class<?>> protoMessageClassList = new ArrayList<>();

        actionDocumentList.forEach(actionDocument -> {

            // collect the action inputParam and outputParam
            List<ActionMethodDocument> actionMethodDocumentList = actionDocument.getActionMethodDocumentList();
            actionMethodDocumentList.forEach(actionMethodDocument -> {
                Class<?> bizDataTypeClazz = actionMethodDocument.bizDataTypeClazz;
                Class<?> returnTypeClazz = actionMethodDocument.returnTypeClazz;

                GDScriptDocumentGenerate.GDScriptProtoMessage bizDataProtoMessage = this.analyseImport.getProtoMessage(bizDataTypeClazz);
                GDScriptDocumentGenerate.GDScriptProtoMessage returnProtoMessage = this.analyseImport.getProtoMessage(returnTypeClazz);

                if (Objects.nonNull(bizDataProtoMessage)) {
                    protoMessageClassList.add(bizDataProtoMessage.dataClass);
                }

                if (Objects.nonNull(returnProtoMessage)) {
                    protoMessageClassList.add(returnProtoMessage.dataClass);
                }
            });

            String imports = this.analyseImport.getImportPath(protoMessageClassList);

            Template template = ofTemplate("action.txt");
            template.binding("imports", imports);

            new ActionGenerate()
                    .setActionDocument(actionDocument)
                    .setTemplate(template)
                    .setFilePath(this.path)
                    .setFileSuffix(".gd")
                    .setTemplateCreator(this::ofTemplate)
                    .generate();
        });
    }

    private void extractedSnakeName(List<ActionDocument> actionDocumentList) {
        for (ActionDocument actionDocument : actionDocumentList) {
            for (ActionMemberCmdDocument memberCmdDocument : actionDocument.getActionMemberCmdDocumentList()) {
                memberCmdDocument.memberName = toSnakeName(memberCmdDocument.memberName);
            }

            for (ActionMethodDocument methodDocument : actionDocument.getActionMethodDocumentList()) {
                methodDocument.actionMethodName = toSnakeName(methodDocument.actionMethodName);
                methodDocument.bizDataName = toSnakeName(methodDocument.bizDataName);
                methodDocument.memberCmdName = toSnakeName(methodDocument.memberCmdName);
            }
        }
    }

    private String toSnakeName(String s) {
        return DocumentGenerateKit.toSnakeName(s);
    }

    private Template ofTemplate(String fileName) {
        var template = DocumentGenerateKit.getTemplate("gdscript/" + fileName);
        template.binding("protoPrefix", "IoGame");
        return template;
    }

    @Override
    protected void generateBroadcast(IoGameDocument ioGameDocument) {

        Consumer<BroadcastDocument> broadcastRenderBeforeConsumer = broadcastDocument -> {
            broadcastDocument.cmdMethodName = toSnakeName(broadcastDocument.cmdMethodName);
            broadcastDocument.methodName = toSnakeName(broadcastDocument.methodName);
        };

        // collect protoMessage
        List<Class<?>> protoMessageClassList = new ArrayList<>();
        ioGameDocument.getBroadcastDocumentList().forEach(broadcastDocument -> {
            Class<?> dataClass = broadcastDocument.getDataClass();
            GDScriptDocumentGenerate.GDScriptProtoMessage bizDataProtoMessage = this.analyseImport.getProtoMessage(dataClass);
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
                .setFileSuffix(".gd")
                .setBroadcastRenderBeforeConsumer(broadcastRenderBeforeConsumer)
                .generate();
    }

    @Override
    protected void generateErrorCode(IoGameDocument ioGameDocument) {
        Template template = ofTemplate("game_code.txt");

        new GameCodeGenerate()
                .setIoGameDocument(ioGameDocument)
                .setInternalErrorCode(this.internalErrorCode)
                .setTemplate(template)
                .setFilePath(this.path)
                .setFileSuffix(".gd")
                .generate();
    }

    private static class GDScriptTypeMappingDocument implements TypeMappingDocument {
        @Getter
        final Map<Class<?>, TypeMappingRecord> map = new HashMap<>();
        final GDScriptDocumentGenerate documentGenerate;

        public GDScriptTypeMappingDocument(GDScriptDocumentGenerate documentGenerate) {
            this.documentGenerate = documentGenerate;
            this.extractedInitTypeMapping();
        }

        private void extractedInitTypeMapping() {
            // about int
            var intTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("int").setListParamTypeName("Array[int]")
                    .setOfMethodTypeName("int").setOfMethodListTypeName("int_list")
                    .setResultMethodTypeName("get_int()").setResultMethodListTypeName("list_int()");

            this.mapping(intTypeMapping, intClassList);

            // about long
            var longTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("int").setListParamTypeName("Array[int]")
                    .setOfMethodTypeName("long").setOfMethodListTypeName("long_list")
                    .setResultMethodTypeName("get_long()").setResultMethodListTypeName("list_long()");

            this.mapping(longTypeMapping, longClassList);

            // about boolean
            var boolTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("bool").setListParamTypeName("Array[bool]")
                    .setOfMethodTypeName("bool").setOfMethodListTypeName("bool_list")
                    .setResultMethodTypeName("get_bool()").setResultMethodListTypeName("list_bool()");

            this.mapping(boolTypeMapping, boolClassList);

            // about String
            var stringTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("String").setListParamTypeName("Array[String]")
                    .setOfMethodTypeName("string").setOfMethodListTypeName("string_list")
                    .setResultMethodTypeName("get_string()").setResultMethodListTypeName("list_string()");

            this.mapping(stringTypeMapping, stringClassList);
        }

        @Override
        public TypeMappingRecord getTypeMappingRecord(Class<?> protoTypeClazz) {
            var map = getMap();
            if (map.containsKey(protoTypeClazz)) {
                return map.get(protoTypeClazz);
            }

            var analyseImport = this.documentGenerate.analyseImport;
            var protoMessage = analyseImport.getProtoMessage(protoTypeClazz);

            String paramTypeName;
            if (Objects.isNull(protoMessage)) {
                paramTypeName = protoTypeClazz.getSimpleName();
            } else {
                paramTypeName = protoMessage.getFullParamTypeName();
            }

            var record = new TypeMappingRecord().setInternalType(false)
                    .setParamTypeName(paramTypeName).setListParamTypeName("Array[%s]".formatted(paramTypeName))
                    .setOfMethodTypeName("").setOfMethodListTypeName("value_list")
                    .setResultMethodTypeName("get_value(%s)".formatted(paramTypeName)).setResultMethodListTypeName("list_value(%s)".formatted(paramTypeName));

            map.put(protoTypeClazz, record);

            return record;
        }
    }

    private static class GDScriptAnalyseImport {
        final Map<Class<?>, GDScriptProtoMessage> map = new HashMap<>();

        GDScriptAnalyseImport(Collection<ProtoFileMergeClass> messageList) {
            messageList.forEach(protoFileMergeClass -> {
                String fileName = protoFileMergeClass.fileName();
                String importFileName = fileName.substring(0, fileName.length() - ".proto".length());

                var message = new GDScriptProtoMessage(importFileName, protoFileMergeClass.dataClass());
                map.put(message.dataClass, message);
            });
        }

        GDScriptProtoMessage getProtoMessage(Class<?> dataClass) {
            return map.get(dataClass);
        }

        String getImportPath(List<? extends Class<?>> dataClassList) {
            Set<String> importFileNameSet = new HashSet<>();
            for (Class<?> dataClass : dataClassList) {
                GDScriptDocumentGenerate.GDScriptProtoMessage protoMessage = getProtoMessage(dataClass);
                String importFileName = protoMessage.importFileName;
                importFileNameSet.add(importFileName);
            }

            // preload .proto (proto.gd)
            return importFileNameSet.stream().map(importFileName -> {
                String tpl = "const %s = preload(\"../%s.gd\")";
                String variateName = StrKit.firstCharToUpperCase(importFileName);
                return tpl.formatted(variateName, importFileName);
            }).collect(Collectors.joining("\n"));
        }
    }

    private record GDScriptProtoMessage(String importFileName, Class<?> dataClass) {
        String getFullParamTypeName() {
            return StrKit.firstCharToUpperCase(importFileName) + "." + dataClass.getSimpleName();
        }
    }
}
