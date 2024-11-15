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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public CsharpDocumentGenerate() {
        this.typeMappingDocument = new CSharpTypeMappingDocument(this);
        protoImportPath = "using Pb.Common;";
    }

    @Override
    public void generate(IoGameDocument ioGameDocument) {
        Objects.requireNonNull(this.path);

        defaultValue();

        this.generateAction(ioGameDocument);
        this.generateBroadcast(ioGameDocument);
        this.generateErrorCode(ioGameDocument);

        log.info("CSharpDocumentGenerate success: {}", this.path);
    }

    private void defaultValue() {
        this.actionImportList.add(protoImportPath);
        this.broadcastImportList.add(protoImportPath);
    }

    @Override
    protected void generateErrorCode(IoGameDocument ioGameDocument) {
        Template template = ofTemplate("game_code.txt");
        template.binding("using", String.join("\n", this.errorCodeImportList));
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
            template.binding("using", String.join("\n", this.actionImportList));
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
        template.binding("using", String.join("\n", this.broadcastImportList));
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

            this.mapping(intTypeMapping, List.of(int.class, Integer.class, IntValue.class));

            // about long
            var longTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("long").setListParamTypeName("List<long>")
                    .setOfMethodTypeName("Long").setOfMethodListTypeName("LongList")
                    .setResultMethodTypeName("GetLong()").setResultMethodListTypeName("ListLong()");

            this.mapping(longTypeMapping, List.of(long.class, Long.class, LongValue.class));

            // about boolean
            var boolTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("bool").setListParamTypeName("List<bool>")
                    .setOfMethodTypeName("Bool").setOfMethodListTypeName("BoolList")
                    .setResultMethodTypeName("GetBool()").setResultMethodListTypeName("ListBool()");

            this.mapping(boolTypeMapping, List.of(boolean.class, Boolean.class, BoolValue.class));

            // about String
            var stringTypeMapping = new TypeMappingRecord()
                    .setParamTypeName("string").setListParamTypeName("List<string>")
                    .setOfMethodTypeName("String").setOfMethodListTypeName("StringList")
                    .setResultMethodTypeName("GetString()").setResultMethodListTypeName("ListString()");

            this.mapping(stringTypeMapping, List.of(String.class, StringValue.class));
        }

        @Override
        public TypeMappingRecord getTypeMappingRecord(Class<?> protoTypeClazz) {
            var map = getMap();
            if (map.containsKey(protoTypeClazz)) {
                return map.get(protoTypeClazz);
            }

            String simpleName = protoTypeClazz.getSimpleName();

            var record = new TypeMappingRecord().setInternalType(false)
                    .setParamTypeName(simpleName).setListParamTypeName("List<%s>".formatted(simpleName))
                    .setOfMethodTypeName("").setOfMethodListTypeName("ValueList")
                    .setResultMethodTypeName("GetValue<%s>()".formatted(simpleName)).setResultMethodListTypeName("ListValue<%s>()".formatted(simpleName));

            map.put(protoTypeClazz, record);

            return record;
        }
    }
}