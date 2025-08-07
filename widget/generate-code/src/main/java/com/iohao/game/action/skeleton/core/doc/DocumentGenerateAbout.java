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

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.protocol.wrapper.BoolValue;
import com.iohao.game.action.skeleton.protocol.wrapper.IntValue;
import com.iohao.game.action.skeleton.protocol.wrapper.LongValue;
import com.iohao.game.action.skeleton.protocol.wrapper.StringValue;
import com.iohao.game.common.kit.ArrayKit;
import com.iohao.game.common.kit.StrKit;
import com.iohao.game.common.kit.io.FileKit;
import com.iohao.game.common.kit.time.CacheTimeKit;
import com.iohao.game.common.kit.time.FormatTimeKit;
import com.iohao.game.widget.light.protobuf.ProtoFileMerge;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.beetl.core.Configuration;
import org.beetl.core.Context;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import org.jctools.maps.NonBlockingHashMap;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@UtilityClass
class DocumentGenerateKit {
    private GroupTemplate gt;
    String broadcastActionTemplatePath = "broadcast_action.txt";
    String broadcastExampleTemplatePath = "broadcast_action_example.txt";
    String broadcastExampleActionTemplatePath = "broadcast_action_example_action.txt";
    String actionMethodResultExampleTemplatePath = "action_method_result_example.txt";
    String gameCodeTemplatePath = "game_code.txt";
    String actionTemplatePath = "action.txt";

    static {
        init();
    }

    @SneakyThrows
    private void init() {
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("generate/");
        Configuration cfg = Configuration.defaultConfiguration();
        gt = new GroupTemplate(resourceLoader, cfg);
        gt.registerFunction("codeEscape", new ExampleCodeEscape());
        gt.registerFunction("originalCode", new ExampleOriginalCode());
        gt.registerFunction("snakeName", new SnakeName());
    }

    Template getTemplate(String path) {
        return gt.getTemplate(path);
    }

    String toSnakeName(String name) {
        return Optional.ofNullable(name)
                .filter(StrKit::isNotEmpty)
                .map(input -> {
                    StringBuilder result = new StringBuilder();
                    result.append(Character.toLowerCase(input.charAt(0)));

                    for (int i = 1; i < input.length(); i++) {
                        char currentChar = input.charAt(i);
                        if (Character.isUpperCase(currentChar)) {
                            result.append('_');
                            result.append(Character.toLowerCase(currentChar));
                        } else {
                            result.append(currentChar);
                        }
                    }

                    return result.toString();
                }).orElse("");
    }

    class ExampleCodeEscape implements org.beetl.core.Function {
        @Override
        public Object call(Object[] paras, Context ctx) {
            return Optional.ofNullable(paras[0])
                    .map(Object::toString)
                    .map(str -> {
                        // Escape
                        return str.replace("<", "&lt;").replace(">", "&gt;");
                    }).orElse("");
        }
    }

    class ExampleOriginalCode implements org.beetl.core.Function {
        @Override
        public Object call(Object[] paras, Context ctx) {
            return Optional.ofNullable(paras[0])
                    .map(Object::toString)
                    .map(str -> {
                        // Escape
                        return str.replace("&lt;", "<").replace("&gt;", ">");
                    }).orElse("");
        }
    }

    class SnakeName implements org.beetl.core.Function {
        @Override
        public Object call(Object[] paras, Context ctx) {
            var value = paras[0];
            if (Objects.isNull(value)) {
                return "";
            }

            return toSnakeName(value.toString());
        }
    }
}


@Accessors(chain = true)
@Setter(AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE)
final class GameCodeGenerate {
    IoGameDocument ioGameDocument;
    /** true : 生成框架内置的错误码，see {@link ActionErrorEnum} */
    boolean internalErrorCode;

    Template template;
    String filePath;
    String fileSuffix;

    void generate() {
        Objects.requireNonNull(ioGameDocument);
        Objects.requireNonNull(template);
        Objects.requireNonNull(filePath);
        Objects.requireNonNull(fileSuffix);

        List<ErrorCodeDocument> errorCodeDocumentList = ioGameDocument
                .getErrorCodeDocumentList()
                .stream()
                .filter(errorCodeDocument -> internalErrorCode || errorCodeDocument.getValue() >= 0)
                .peek(errorCodeDocument -> {
                    String name = StrKit.firstCharToUpperCase(errorCodeDocument.getName());
                    errorCodeDocument.setName(name);
                }).toList();

        template.binding("errorCodeDocumentList", errorCodeDocumentList);
        GenerateInternalKit.binding(template);

        String fileText = template.render();
        String path = "%s%sGameCode%s".formatted(this.filePath, File.separator, this.fileSuffix);
        FileKit.writeUtf8String(fileText, path);
    }
}

@Accessors(chain = true)
@Setter(AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE)
final class BroadcastGenerate {
    IoGameDocument ioGameDocument;
    TypeMappingDocument typeMappingDocument;

    Template template;
    String filePath;
    String fileSuffix;
    Function<String, Template> templateCreator;
    Consumer<BroadcastDocument> broadcastRenderBeforeConsumer;

    void generate() {
        Objects.requireNonNull(ioGameDocument);
        Objects.requireNonNull(typeMappingDocument);
        Objects.requireNonNull(template);
        Objects.requireNonNull(filePath);
        Objects.requireNonNull(fileSuffix);
        Objects.requireNonNull(templateCreator);

        Collection<BroadcastDocument> broadcastDocumentList = this.listBroadcastDocument();
        template.binding("broadcastDocumentList", broadcastDocumentList);
        GenerateInternalKit.binding(template);

        String fileText = template.render();
        String path = "%s%sListener%s".formatted(this.filePath, File.separator, this.fileSuffix);
        FileKit.writeUtf8String(fileText, path);
    }

    Collection<BroadcastDocument> listBroadcastDocument() {
        return ioGameDocument.getBroadcastDocumentList().stream()
                .peek(broadcastDocument -> {
                    // 如果没有指定方法名，则方法名使用下述规则
                    String methodName = StrKit.firstCharToUpperCase(broadcastDocument.getMethodName());
                    broadcastDocument.setMethodName(methodName);

                    // 生成广播使用代码示例
                    extractedBroadcastExampleCode(broadcastDocument);
                }).toList();
    }

    private void extractedBroadcastExampleCode(BroadcastDocument broadcastDocument) {
        Class<?> dataClass = broadcastDocument.getDataClass();
        if (Objects.isNull(dataClass)) {
            if (Objects.nonNull(broadcastRenderBeforeConsumer)) {
                broadcastRenderBeforeConsumer.accept(broadcastDocument);
            }
            return;
        }

        TypeMappingRecord typeMappingRecord = typeMappingDocument.getTypeMappingRecord(dataClass);
        broadcastDocument.setBizDataType(typeMappingRecord.getParamTypeName());
        broadcastDocument.setDataTypeIsInternal(typeMappingRecord.isInternalType());
        broadcastDocument.setResultMethodTypeName(typeMappingRecord.getResultMethodTypeName());
        broadcastDocument.setResultMethodListTypeName(typeMappingRecord.getResultMethodListTypeName());
        broadcastDocument.setDataActualTypeName(typeMappingRecord.getParamTypeName());

        if (Objects.nonNull(broadcastRenderBeforeConsumer)) {
            broadcastRenderBeforeConsumer.accept(broadcastDocument);
        }

        String exampleCode = render(broadcastDocument, typeMappingRecord, DocumentGenerateKit.broadcastExampleTemplatePath);
        broadcastDocument.setExampleCode(exampleCode);

        // code action
        String examplePrefixCode = render(broadcastDocument, typeMappingRecord, DocumentGenerateKit.broadcastExampleActionTemplatePath);
        broadcastDocument.setExampleCodeAction(examplePrefixCode);
    }

    private String render(BroadcastDocument broadcastDocument, TypeMappingRecord typeMappingRecord, String examplePath) {
        Template exampleTemplate = templateCreator.apply(examplePath);

        if (Objects.isNull(exampleTemplate)) {
            return "";
        }

        exampleTemplate.binding("_root", broadcastDocument);
        exampleTemplate.binding("typeMappingRecord", typeMappingRecord);

        return exampleTemplate.render().trim();
    }
}

@Accessors(chain = true)
@Setter(AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE)
final class ActionGenerate {
    ActionDocument actionDocument;
    /** action template */
    Template template;
    String filePath;
    String fileSuffix;

    Function<String, Template> templateCreator;

    void generate() {
        ActionDoc actionDoc = actionDocument.getActionDoc();
        String classComment = actionDoc.getJavaClassDocInfo().getComment();
        template.binding("classComment", classComment);
        GenerateInternalKit.binding(template);

        // 路由成员变量
        List<ActionMemberCmdDocument> actionMemberCmdDocumentList = actionDocument.getActionMemberCmdDocumentList();
        template.binding("actionMemberCmdDocumentList", actionMemberCmdDocumentList);

        // action method
        List<String> renderMethodList = actionDocument
                .getActionMethodDocumentList()
                .stream()
                .map(actionMethodDocument -> {
                    // example template code
                    var exampleTemplate = this.templateCreator.apply(DocumentGenerateKit.actionMethodResultExampleTemplatePath);
                    exampleTemplate.binding("_root", actionMethodDocument);
                    String exampleCode = exampleTemplate.render().trim();

                    // generate method
                    String templateFileName = getActionMethodDocumentTemplateFileName(actionMethodDocument);
                    Template methodTemplate = this.templateCreator.apply(templateFileName);
                    methodTemplate.binding("_root", actionMethodDocument);
                    methodTemplate.binding("exampleCode", exampleCode);

                    if (actionMethodDocument.internalBizDataType) {
                        methodTemplate.binding("protoPrefix", "");
                    }

                    return methodTemplate.render();
                })
                .toList();

        template.binding("methodCodeList", renderMethodList);

        Class<?> controllerClazz = actionDoc.getControllerClazz();
        String simpleName = controllerClazz.getSimpleName();
        template.binding("ActionName", simpleName);

        String actionComment = actionDoc.getJavaClassDocInfo().getComment();
        template.binding("ActionComment", actionComment);

        String render = template.render();
        String actionFilePath = "%s%s%s%s".formatted(this.filePath, File.separator, simpleName, this.fileSuffix);
        FileKit.writeUtf8String(render, actionFilePath);
    }

    private String getActionMethodDocumentTemplateFileName(ActionMethodDocument actionMethodDocument) {
        if (actionMethodDocument.isVoid()) {
            return actionMethodDocument.isHasBizData()
                    ? "action_method_void.txt"
                    : "action_method_void_no_param.txt";

        } else {
            return actionMethodDocument.isHasBizData()
                    ? "action_method.txt"
                    : "action_method_no_param.txt";
        }
    }
}

@UtilityClass
class GenerateInternalKit {
    void binding(Template template) {
        var generateTime = FormatTimeKit.ofPattern("yyyy-MM-dd").format(CacheTimeKit.nowLocalDate());
        String gtKey = new String(new byte[]{103, 101, 110, 101, 114, 97, 116, 101, 84, 105, 109, 101}, StandardCharsets.UTF_8);
        template.binding(gtKey, "// %s %s".formatted(gtKey, generateTime));

        String iohao = new String(new byte[]{105, 111, 71, 97, 109, 101, 72, 111, 109, 101}, StandardCharsets.UTF_8);
        String u = new String(new byte[]{104, 116, 116, 112, 115, 58, 47, 47, 103, 105, 116, 104, 117, 98, 46, 99, 111, 109, 47, 105, 111, 104, 97, 111, 47, 105, 111, 71, 97, 109, 101}, StandardCharsets.UTF_8);
        template.binding(iohao, "// %s".formatted(u));
    }
}

@UtilityClass
class InternalProtoClassKit {
    final Map<Class<?>, ProtoFileMergeClass> protoClassMap = new NonBlockingHashMap<>();

    void analyseProtoClass(IoGameDocument ioGameDocument) {
        if (!protoClassMap.isEmpty()) {
            return;
        }

        // --------- collect proto class ---------
        final Set<Class<?>> protoClassSet = new HashSet<>();
        ioGameDocument.actionDocList.stream()
                .flatMap(actionDoc -> actionDoc.getActionCommandDocMap().values().stream())
                .forEach(actionCommandDoc -> {
                    ActionCommand actionCommand = actionCommandDoc.getActionCommand();
                    // --------- action return class ---------
                    ActionCommand.ActionMethodReturnInfo returnInfo = actionCommand.getActionMethodReturnInfo();
                    if (!returnInfo.isVoid()) {
                        Class<?> returnTypeClazz = returnInfo.getActualTypeArgumentClazz();
                        protoClassSet.add(returnTypeClazz);
                    }

                    // --------- action param class ---------
                    ActionCommand.ParamInfo bizParam = DocumentAnalyseKit.getBizParam(actionCommand);
                    if (Objects.nonNull(bizParam)) {
                        Class<?> actualTypeArgumentClazz = bizParam.getActualTypeArgumentClazz();
                        protoClassSet.add(actualTypeArgumentClazz);
                    }
                });

        ioGameDocument.broadcastDocumentList.forEach(broadcastDocument -> {
            Class<?> dataClass = broadcastDocument.getDataClass();
            if (Objects.nonNull(dataClass)) {
                protoClassSet.add(dataClass);
            }
        });

        var excludeTypeList = List.of(
                int.class, Integer.class, IntValue.class,
                long.class, Long.class, LongValue.class,
                boolean.class, Boolean.class, BoolValue.class,
                String.class, StringValue.class
        );

        protoClassSet.stream().filter(protoClass -> {
            for (Class<?> aClass : excludeTypeList) {
                if (aClass == protoClass) {
                    return false;
                }
            }

            return true;
        }).forEach(protoClass -> {
            ProtoFileMerge annotation = protoClass.getAnnotation(ProtoFileMerge.class);
            if (Objects.isNull(annotation)) {
                return;
            }

            String fileName = annotation.fileName();
            String filePackage = annotation.filePackage();

            var message = new ProtoFileMergeClass(fileName, filePackage, protoClass);
            protoClassMap.put(protoClass, message);
        });
    }
}

record ProtoFileMergeClass(String fileName, String filePackage, Class<?> dataClass) {
}

@Setter
@FieldDefaults(level = AccessLevel.PACKAGE)
abstract class AbstractDocumentGenerate implements DocumentGenerate {
    @Getter
    @Deprecated
    final Set<String> actionImportList = new LinkedHashSet<>();
    @Getter
    @Deprecated
    final Set<String> broadcastImportList = new LinkedHashSet<>();
    @Getter
    @Deprecated
    final Set<String> errorCodeImportList = new LinkedHashSet<>();
    /** true : generate ActionErrorEnum */
    boolean internalErrorCode = true;
    /** your .proto path */
    @Deprecated
    String protoImportPath;
    boolean publicActionCmdName;
    /**
     * The storage path of the generated files.
     * By default, it will be generated in the ./target/action directory
     */
    String path = ArrayKit.join(new String[]{System.getProperty("user.dir"), "target", "code"}, File.separator);
    TypeMappingDocument typeMappingDocument;

    protected abstract void generateAction(IoGameDocument ioGameDocument);

    protected abstract void generateBroadcast(IoGameDocument ioGameDocument);

    protected abstract void generateErrorCode(IoGameDocument ioGameDocument);
}