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
import com.iohao.game.action.skeleton.core.doc.generate.DocGenerate;
import com.iohao.game.action.skeleton.core.doc.generate.IoGameDoc;
import com.iohao.game.action.skeleton.core.doc.generate.TextDocGenerate;
import lombok.Setter;

import java.util.*;

/**
 * 游戏文档生成
 *
 * @author 渔民小镇
 * @date 2022-01-23
 */
public class BarSkeletonDoc {

    final List<BarSkeleton> skeletonList = new LinkedList<>();
    final Map<String, DocGenerate> docGenerateMap = new HashMap<>();

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

    public void addSkeleton(BarSkeleton barSkeleton) {
        skeletonList.add(barSkeleton);
    }

    public void addDocGenerate(DocGenerate docGenerate) {
        String key = docGenerate.getClass().getName();
        docGenerateMap.put(key, docGenerate);
    }

    public void buildDoc() {

        if (!generateDoc) {
            return;
        }

        IoGameDoc ioGameDoc = ofIoGameDoc();
        docGenerateMap.values().forEach(docGenerate -> docGenerate.generate(ioGameDoc));

        docGenerateMap.values().clear();
    }

    @Deprecated
    public void buildDoc(String docPath) {
        if (!generateDoc) {
            return;
        }

        // 兼容
        if (docGenerateMap.get(TextDocGenerate.class.getName()) instanceof TextDocGenerate textDocGenerate) {
            textDocGenerate.setPath(docPath);
        }

        buildDoc();
    }

    private IoGameDoc ofIoGameDoc() {

        IoGameDoc ioGameDoc = new IoGameDoc();
        ioGameDoc.setActionDocMap(ActionDocs.actionDocMap);

        BroadcastDocRecordRegion broadcastDocRecordRegion = this.createBroadcastDocRegion();
        ioGameDoc.setBroadcastDocRecordRegion(broadcastDocRecordRegion);

        ErrorCodeDocsRegion errorCodeDocsRegion = this.createErrorCodeDocsRegion();
        ioGameDoc.setErrorCodeDocsRegion(errorCodeDocsRegion);

        return ioGameDoc;
    }

    public List<BarSkeleton> listBarSkeleton() {
        return new ArrayList<>(this.skeletonList);
    }

    private BroadcastDocRecordRegion createBroadcastDocRegion() {
        BroadcastDocRecordRegion broadcastDocRecordRegion = new BroadcastDocRecordRegion();

        skeletonList.stream()
                .map(BarSkeleton::getActionSendDocs)
                .flatMap(actionSendDocs -> actionSendDocs.getActionSendDocMap().values().stream())
                .map(actionSendDoc -> {
                    // 转换为 BroadcastDocRecord
                    BroadcastDocRecord record = new BroadcastDocRecord();
                    record.setCmd(actionSendDoc.getCmd())
                            .setSubCmd(actionSendDoc.getSubCmd())
                            .setDataClass(actionSendDoc.getDataClass())
                            .setDescription(actionSendDoc.getDescription())
                            .setDataClassName(actionSendDoc.getDataClassName())
                            .setDataDescription(actionSendDoc.getDataDescription())
                            .setList(actionSendDoc.isList())
                            .setMethodName(actionSendDoc.getMethodName())
                    ;
                    return record;
                }).forEach(broadcastDocRecordRegion::add);

        return broadcastDocRecordRegion;
    }

    private ErrorCodeDocsRegion createErrorCodeDocsRegion() {
        ErrorCodeDocsRegion region = new ErrorCodeDocsRegion();
        skeletonList.stream()
                .map(BarSkeleton::getErrorCodeDocs)
                .forEach(region::addErrorCodeDocs);

        return region;
    }

    private BarSkeletonDoc() {
        addDocGenerate(new TextDocGenerate());
    }

    public static BarSkeletonDoc me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final BarSkeletonDoc ME = new BarSkeletonDoc();
    }
}