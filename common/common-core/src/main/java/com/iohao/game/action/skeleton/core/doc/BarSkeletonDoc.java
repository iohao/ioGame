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

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import lombok.Setter;
import org.jctools.maps.NonBlockingHashSet;

import java.util.*;

/**
 * 游戏文档生成
 *
 * @author 渔民小镇
 * @date 2022-01-23
 */
public final class BarSkeletonDoc {

    final List<BarSkeleton> skeletonList = new LinkedList<>();
    final Map<String, DocumentGenerate> docGenerateMap = new HashMap<>();
    final Set<Class<? extends MsgExceptionInfo>> errorCodeClassSet = new NonBlockingHashSet<>();

    @Setter
    @Deprecated
    String docFileName = "doc_game.txt";

    @Setter
    @Deprecated
    String docPath;

    /** true 生成文档 */
    boolean generateDoc = true;

    /**
     * 只有当 this.generateDoc 为 true 时，才会执行 set 操作
     *
     * @param generateDoc generateDoc
     */
    public void setGenerateDoc(boolean generateDoc) {
        // 只有当 this.generateDoc 为 true 时，才会执行 set 操作
        if (this.generateDoc) {
            this.generateDoc = generateDoc;
        }
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
        errorCodeClassSet.add(clazz);
    }

    public void addSkeleton(BarSkeleton barSkeleton) {
        skeletonList.add(barSkeleton);
    }

    public void addDocGenerate(DocumentGenerate documentGenerate) {
        String key = documentGenerate.getClass().getName();
        docGenerateMap.put(key, documentGenerate);
    }

    public void buildDoc() {

        if (!generateDoc) {
            return;
        }

        IoGameDocument ioGameDocument = ofIoGameDoc();

        docGenerateMap.values().forEach(documentGenerate -> documentGenerate.generate(ioGameDocument));

        docGenerateMap.values().clear();
    }

    @Deprecated
    public void buildDoc(String docPath) {
        if (!generateDoc) {
            return;
        }

        // 兼容
        if (docGenerateMap.get(TextDocumentGenerate.class.getName()) instanceof TextDocumentGenerate textDocGenerate) {
            textDocGenerate.setPath(docPath);
        }

        buildDoc();
    }

    private IoGameDocument ofIoGameDoc() {

        IoGameDocument ioGameDocument = new IoGameDocument();
        ioGameDocument.actionDocMap = ActionDocs.actionDocMap;
        ioGameDocument.broadcastDocumentRegion = this.createBroadcastDocRegion();
        ioGameDocument.errorCodeDocsRegion = this.createErrorCodeDocsRegion();
        ioGameDocument.errorCodeDocumentList = this.errorCodeClassSet.stream().flatMap(clazz -> {
            // to stream
            return DocumentAnalyseKit.analyseErrorCodeDocument(clazz).stream();
        }).sorted(Comparator.comparingInt(ErrorCodeDocument::getValue)).toList();

        return ioGameDocument;
    }

    public List<BarSkeleton> listBarSkeleton() {
        return new ArrayList<>(this.skeletonList);
    }

    private BroadcastDocumentRegion createBroadcastDocRegion() {
        BroadcastDocumentRegion broadcastDocumentRegion = new BroadcastDocumentRegion();

        skeletonList.stream()
                .map(BarSkeleton::getActionSendDocs)
                .flatMap(actionSendDocs -> actionSendDocs.getActionSendDocMap().values().stream())
                .map(actionSendDoc -> {
                    // 转换为 BroadcastDocRecord
                    BroadcastDocument broadcastDocument = new BroadcastDocument();
                    broadcastDocument.setCmd(actionSendDoc.getCmd())
                            .setSubCmd(actionSendDoc.getSubCmd())
                            .setDataClass(actionSendDoc.getDataClass())
                            .setMethodDescription(actionSendDoc.getDescription())
                            .setDataClassName(actionSendDoc.getDataClassName())
                            .setDataDescription(actionSendDoc.getDataDescription())
                            .setDataIsList(actionSendDoc.isList())
                            .setMethodName(actionSendDoc.getMethodName())
                    ;
                    return broadcastDocument;
                }).forEach(broadcastDocumentRegion::add);

        return broadcastDocumentRegion;
    }

    private ErrorCodeDocsRegion createErrorCodeDocsRegion() {
        ErrorCodeDocsRegion region = new ErrorCodeDocsRegion();
        skeletonList.stream()
                .map(BarSkeleton::getErrorCodeDocs)
                .forEach(region::addErrorCodeDocs);

        return region;
    }

    private BarSkeletonDoc() {
        addDocGenerate(new TextDocumentGenerate());

        // 框架默认提供的错误码
        addErrorCodeClass(ActionErrorEnum.class);
    }

    public static BarSkeletonDoc me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final BarSkeletonDoc ME = new BarSkeletonDoc();
    }
}