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
import java.util.stream.Collectors;

/**
 * Generate C# code, such as broadcast, error code, action
 *
 * @author 渔民小镇
 * @date 2024-11-15
 * @since 21.20
 */
@Slf4j
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class CsharpDocumentGenerate extends AbstractDocumentGenerate {
    /** c# namespace that generate files */
    String namespace = "IoGame.Gen";
    @Setter(AccessLevel.PRIVATE)
    CsharpAnalyseImport analyseImport;

    public CsharpDocumentGenerate() {
        this.typeMappingDocument = new CSharpTypeMappingDocument(this);
    }

    @Override
    public void generate(IoGameDocument ioGameDocument) {
        Objects.requireNonNull(this.path);
        InternalProtoClassKit.analyseProtoClass(ioGameDocument);
        Map<Class<?>, ProtoFileMergeClass> protoClassMap = InternalProtoClassKit.protoClassMap;
        this.analyseImport = new CsharpAnalyseImport(protoClassMap.values());

        this.generateAction(ioGameDocument);
        this.generateBroadcast(ioGameDocument);
        this.generateErrorCode(ioGameDocument);

        log.info("CSharpDocumentGenerate success: {}", this.path);
    }

    @Override
    protected void generateErrorCode(IoGameDocument ioGameDocument) {
        Template template = ofTemplate("game_code.txt");
        template.binding("namespace", this.namespace);

        new GameCodeGenerate()
                .setIoGameDocument(ioGameDocument)
                .setInternalErrorCode(this.internalErrorCode)
                .setTemplate(template)
                .setFilePath(this.path)
                .setFileSuffix(".cs")
                .generate();
    }

    @Override
    protected void generateAction(IoGameDocument ioGameDocument) {
        List<ActionDocument> actionDocumentList = DocumentAnalyseKit.analyseActionDocument(ioGameDocument, typeMappingDocument);

        actionDocumentList.forEach(actionDocument -> {
            Template template = ofTemplate("action.txt");
            // using、namespace
//            template.binding("using", String.join("\n", this.actionImportList));
            template.binding("namespace", this.namespace);

            new ActionGenerate()
                    .setActionDocument(actionDocument)
                    .setTemplate(template)
                    .setFilePath(this.path)
                    .setFileSuffix(".cs")
                    .setTemplateCreator(this::ofTemplate)
                    .generate();
        });
    }

    @Override
    protected void generateBroadcast(IoGameDocument ioGameDocument) {
        Template template = ofTemplate(DocumentGenerateKit.broadcastActionTemplatePath);
//        template.binding("using", String.join("\n", this.broadcastImportList));
        template.binding("namespace", this.namespace);

        new BroadcastGenerate()
                .setIoGameDocument(ioGameDocument)
                .setTypeMappingDocument(typeMappingDocument)
                .setTemplateCreator(this::ofTemplate)
                .setTemplate(template)
                .setFilePath(this.path)
                .setFileSuffix(".cs")
                .generate();
    }

    private Template ofTemplate(String fileName) {
        return DocumentGenerateKit.getTemplate("csharp/" + fileName);
    }

    private static class CSharpTypeMappingDocument implements TypeMappingDocument {
        @Getter
        final Map<Class<?>, TypeMappingRecord> map = new HashMap<>();
        final CsharpDocumentGenerate documentGenerate;

        public CSharpTypeMappingDocument(CsharpDocumentGenerate documentGenerate) {
            this.documentGenerate = documentGenerate;
            extractedInitTypeMapping();
        }

        private void extractedInitTypeMapping() {
            // about int
            var intTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("int").setListParamTypeName("List<int>")
                    .setOfMethodTypeName("Int").setOfMethodListTypeName("IntList")
                    .setResultMethodTypeName("GetInt()").setResultMethodListTypeName("ListInt()");

            this.mapping(intTypeMapping, intClassList);

            // about long
            var longTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("long").setListParamTypeName("List<long>")
                    .setOfMethodTypeName("Long").setOfMethodListTypeName("LongList")
                    .setResultMethodTypeName("GetLong()").setResultMethodListTypeName("ListLong()");

            this.mapping(longTypeMapping, longClassList);

            // about boolean
            var boolTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("bool").setListParamTypeName("List<bool>")
                    .setOfMethodTypeName("Bool").setOfMethodListTypeName("BoolList")
                    .setResultMethodTypeName("GetBool()").setResultMethodListTypeName("ListBool()");

            this.mapping(boolTypeMapping, boolClassList);

            // about String
            var stringTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("string").setListParamTypeName("List<string>")
                    .setOfMethodTypeName("String").setOfMethodListTypeName("StringList")
                    .setResultMethodTypeName("GetString()").setResultMethodListTypeName("ListString()");

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
            if (Objects.nonNull(protoMessage)) {
                paramTypeName = protoMessage.getFullParamTypeName();
            } else {
                paramTypeName = protoTypeClazz.getSimpleName();
            }

            var record = new TypeMappingRecord().setInternalType(false)
                    .setParamTypeName(paramTypeName).setListParamTypeName("List<%s>".formatted(paramTypeName))
                    .setOfMethodTypeName("").setOfMethodListTypeName("ValueList")
                    .setResultMethodTypeName("GetValue<%s>()".formatted(paramTypeName)).setResultMethodListTypeName("ListValue<%s>()".formatted(paramTypeName));

            map.put(protoTypeClazz, record);

            return record;
        }
    }

    private static class CsharpAnalyseImport {
        final Map<Class<?>, CsProtoMessage> map = new HashMap<>();

        CsharpAnalyseImport(Collection<ProtoFileMergeClass> messageList) {
            messageList.forEach(protoFileMergeClass -> {
                String filePackage = Arrays.stream(protoFileMergeClass.filePackage().split("\\."))
                        .map(StrKit::firstCharToUpperCase)
                        .collect(Collectors.joining("."));

                var message = new CsProtoMessage(filePackage, protoFileMergeClass.dataClass());
                map.put(message.dataClass, message);
            });
        }

        CsProtoMessage getProtoMessage(Class<?> dataClass) {
            return map.get(dataClass);
        }
    }

    private record CsProtoMessage(String filePackage, Class<?> dataClass) {
        String getFullParamTypeName() {
            return filePackage + "." + dataClass.getSimpleName();
        }
    }
}