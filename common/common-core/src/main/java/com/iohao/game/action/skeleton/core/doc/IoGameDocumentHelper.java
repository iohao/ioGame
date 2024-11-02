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

import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import com.iohao.game.common.kit.MoreKit;
import com.iohao.game.common.kit.RuntimeKit;
import com.iohao.game.common.kit.StrKit;
import com.iohao.game.common.kit.exception.ThrowKit;
import com.thoughtworks.qdox.model.JavaClass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashSet;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 对接文档生成器辅助，<a href="https://www.yuque.com/iohao/game/irth38">游戏对接文档生成</a>
 * <p>
 * for example
 * <pre>{@code
 *     // 添加自定义的文档生成器
 *     IoGameDocumentHelper.addDocumentGenerate(new YourDocumentGenerate());
 *     // 添加枚举错误码 class，用于生成错误码相关信息
 *     IoGameDocumentHelper.addErrorCodeClass(YourGameCode.class);
 *     // 生成文档
 *     IoGameDocumentHelper.generateDocument();
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2024-07-05
 * @see DocumentGenerate
 */
@UtilityClass
public class IoGameDocumentHelper {
    /**
     * action 文档相关信息
     * <pre>
     *      key : action controller
     *      value : action 文档
     *  </pre>
     */
    private Map<Class<?>, ActionDoc> actionDocMap = new NonBlockingHashMap<>();
    /** 错误码枚举类信息，用于生成错误码相关信息 */
    private Set<Class<? extends MsgExceptionInfo>> errorCodeClassSet = new NonBlockingHashSet<>();
    private Map<Class<? extends DocumentGenerate>, DocumentGenerate> documentGenerateMap = new HashMap<>();
    private List<BroadcastDocument> broadcastDocumentList = new CopyOnWriteArrayList<>();

    /** true 生成文档 */
    private boolean generateDoc = true;
    private boolean once = true;
    /** 文档路由访问权限控制 */
    @Getter
    @Setter
    private DocumentAccessAuthentication documentAccessAuthentication = cmdMerge -> false;

    /**
     * 只有当 generateDoc 为 true 时，才会执行 set 操作
     *
     * @param generateDoc generateDoc，当为 false 时，将不会生成文档
     */
    public void setGenerateDoc(boolean generateDoc) {
        // 只有当 this.generateDoc 为 true 时，才会执行 set 操作
        if (IoGameDocumentHelper.generateDoc) {
            IoGameDocumentHelper.generateDoc = generateDoc;
        }
    }

    /**
     * 对接文档生成
     */
    public void generateDocument() {
        if (!IoGameDocumentHelper.generateDoc || !once) {
            return;
        }

        // 只生成一次
        once = false;

        // 文档解析
        IoGameDocument ioGameDocument = analyse();

        // 文档生成
        IoGameDocumentHelper.documentGenerateMap
                .values()
                .forEach(documentGenerate -> documentGenerate.generate(ioGameDocument));

        clear();
    }

    private void clear() {
        // 生成文档后，移除静态数据
        actionDocMap = null;
        errorCodeClassSet = null;
        documentGenerateMap = null;
        broadcastDocumentList = null;
    }

    private IoGameDocument analyse() {
        // 添加文本文档解析器
        IoGameDocumentHelper.addDocumentGenerate(new TextDocumentGenerate());

        IoGameDocument ioGameDocument = new IoGameDocument();

        // ------- 错误码解析 -------
        // 框架默认提供的错误码
        IoGameDocumentHelper.addErrorCodeClass(ActionErrorEnum.class);

        ioGameDocument.errorCodeDocumentList = IoGameDocumentHelper.errorCodeClassSet
                .stream()
                .flatMap(clazz -> DocumentAnalyseKit.analyseErrorCodeDocument(clazz).stream())
                .sorted(Comparator.comparingInt(ErrorCodeDocument::getValue))
                .toList();

        // ------- 广播解析 -------
        ioGameDocument.broadcastDocumentList = IoGameDocumentHelper.broadcastDocumentList;
        ioGameDocument.broadcastDocumentList.sort(Comparator.comparingInt(BroadcastDocument::getCmdMerge));
        ioGameDocument.broadcastDocumentList
                .stream()
                .filter(broadcastDocument -> Objects.nonNull(broadcastDocument.getDataClass()))
                .filter(broadcastDocument -> StrKit.isEmpty(broadcastDocument.getDataDescription()))
                .forEach(broadcastDocument -> {
                    // 广播业务数据解析，使用类信息的文档注释
                    Class<?> dataClass = broadcastDocument.getDataClass();
                    JavaClass javaClass = DocumentAnalyseKit.analyseJavaClass(dataClass).javaClass();
                    String classComment = javaClass.getComment();

                    broadcastDocument.setDataDescription(classComment);
                });

        // ------- action 解析 -------
        ioGameDocument.actionDocList = IoGameDocumentHelper.actionDocMap
                .values()
                .stream()
                .sorted(Comparator
                        // 先按 cmd 排序
                        .comparingInt(ActionDoc::getCmd)
                        // 若 cmd 相同，则按 className 排序
                        .thenComparing(o -> o.getControllerClazz().getName())
                ).toList();

        return ioGameDocument;
    }

    /**
     * 添加文档生成器，相同类型只能添加一个
     *
     * @param documentGenerate 文档生成接口
     */
    public void addDocumentGenerate(DocumentGenerate documentGenerate) {
        var key = documentGenerate.getClass();

        if (documentGenerateMap.containsKey(key)) {
            ThrowKit.ofRuntimeException("%s exist".formatted(key));
        }

        IoGameDocumentHelper.documentGenerateMap.putIfAbsent(key, documentGenerate);
    }

    /**
     * 添加枚举错误码 class
     * <pre>
     *     参考 {@link com.iohao.game.action.skeleton.core.exception.ActionErrorEnum} 的实现
     * </pre>
     *
     * @param clazz 枚举错误码 class
     */
    public void addErrorCodeClass(Class<? extends MsgExceptionInfo> clazz) {
        IoGameDocumentHelper.errorCodeClassSet.add(clazz);
    }

    /**
     * 添加广播文档
     *
     * @param broadcastDocument broadcastDocument
     */
    public void addBroadcastDocument(BroadcastDocument broadcastDocument) {
        IoGameDocumentHelper.broadcastDocumentList.add(broadcastDocument);
    }

    /**
     * 获取 ActionDoc，如果 ActionDoc 不存在则创建
     *
     * @param cmd             主路由
     * @param controllerClazz action class
     * @return 一定不为 null
     */
    public ActionDoc ofActionDoc(int cmd, Class<?> controllerClazz) {
        ActionDoc actionDocRegion = IoGameDocumentHelper.actionDocMap.get(controllerClazz);

        if (Objects.isNull(actionDocRegion)) {
            return MoreKit.putIfAbsent(actionDocMap, controllerClazz, new ActionDoc(cmd, controllerClazz));
        }

        return actionDocRegion;
    }
}
