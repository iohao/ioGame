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
package com.iohao.game.action.skeleton.core.doc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.ActionCommandRegions;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.common.kit.StrKit;
import lombok.Setter;

import java.util.*;
import java.util.function.Consumer;

/**
 * 游戏文档生成
 *
 * @author 渔民小镇
 * @date 2022-01-23
 */
public class BarSkeletonDoc {

    final List<BarSkeleton> skeletonList = new LinkedList<>();
    @Setter
    String docFileName = "doc_game.txt";

    public void addSkeleton(BarSkeleton barSkeleton) {
        skeletonList.add(barSkeleton);
    }

    public void buildDoc() {
        // 路径为当前项目
        String docPath = SystemUtil.getUserInfo().getCurrentDir() + docFileName;
        this.buildDoc(docPath);
    }

    private ActionSendDocsRegion createActionSendDocsRegion() {
        ActionSendDocsRegion actionSendDocsRegion = new ActionSendDocsRegion();

        skeletonList.stream()
                .map(BarSkeleton::getActionSendDocs)
                .forEach(actionSendDocsRegion::addActionSendDocs);

        return actionSendDocsRegion;

    }

    private ErrorCodeDocsRegion createErrorCodeDocsRegion() {
        ErrorCodeDocsRegion region = new ErrorCodeDocsRegion();
        skeletonList.stream()
                .map(BarSkeleton::getErrorCodeDocs)
                .forEach(region::addErrorCodeDocs);

        return region;
    }


    public void buildDoc(String docPath) {

        Objects.requireNonNull(docPath);

        if (FileUtil.isDirectory(docPath)) {
            throw new RuntimeException("file is Directory ");

        }

        ActionSendDocsRegion actionSendDocsRegion = this.createActionSendDocsRegion();

        List<String> docContentList = new ArrayList<>(128);

        // 加上游戏文档格式说明
        this.gameDocURLDescription(docContentList);

        Consumer<ActionCommand[][]> consumer = behaviors -> {

            for (ActionCommand[] subBehaviors : behaviors) {
                if (Objects.isNull(subBehaviors)) {
                    continue;
                }

                DocInfo docInfo = new DocInfo();
                docInfo.actionSendDocsRegion = actionSendDocsRegion;

                for (ActionCommand subBehavior : subBehaviors) {
                    if (Objects.isNull(subBehavior)) {
                        continue;
                    }

                    docInfo.setHead(subBehavior);
                    docInfo.add(subBehavior);
                }

                String render = docInfo.render();
                docContentList.add(render);
            }
        };

        // 生成文档 - action
        skeletonList.parallelStream()
                .map(BarSkeleton::getActionCommandRegions)
                .map(ActionCommandRegions::getActionCommands)
                .forEach(consumer);

        // 生成文档 - 广播（推送）文档
        extractedActionSend(actionSendDocsRegion, docContentList);

        // 生成文档 - 错误码文档
        extractedErrorCode(docContentList);

        String docText = String.join("", docContentList);
        FileUtil.writeUtf8String(docText, docPath);
    }

    private void gameDocURLDescription(List<String> docContentList) {
        // 加上游戏文档格式说明
        String gameDocInfo = """
                ==================== 游戏文档格式说明 ====================
                https://www.yuque.com/iohao/game/irth38#cJLdC
                
                """;

        docContentList.add(gameDocInfo);
    }

    private void extractedErrorCode(List<String> docContentList) {
        ErrorCodeDocsRegion errorCodeDocsRegion = this.createErrorCodeDocsRegion();


        String separator = System.getProperty("line.separator");

        docContentList.add("==================== 错误码 ====================");
        docContentList.add(separator);

        for (ErrorCodeDoc errorCodeDoc : errorCodeDocsRegion.listErrorCodeDoc()) {
            String template = " {} : {} ";

            String format = StrKit.format(template, errorCodeDoc.getCode(), errorCodeDoc.getMsg());
            docContentList.add(format);
            docContentList.add(separator);
        }
    }

    private void extractedActionSend(ActionSendDocsRegion actionSendDocsRegion, List<String> docContentList) {
        // 生成剩余的推送文档
        List<ActionSendDoc> actionSendDocList = actionSendDocsRegion.listActionSendDoc();

        if (actionSendDocList.isEmpty()) {
            return;
        }

        String separator = System.getProperty("line.separator");

        docContentList.add("==================== 其它广播推送 ====================");
        docContentList.add(separator);

        for (ActionSendDoc actionSendDoc : actionSendDocList) {

            Map<String, Object> stringObjectMap = BeanUtil.beanToMap(actionSendDoc);
            stringObjectMap.put("dataClass", actionSendDoc.getDataClass().getName());

            String template = "路由: {cmd} - {subCmd}  --- 广播推送: {dataClass}";

            if (StrKit.isNotEmpty(actionSendDoc.getDescription())) {
                template = "路由: {cmd} - {subCmd}  --- 广播推送: {dataClass} ({description})";
            }

            String format = StrKit.format(template, stringObjectMap);

            docContentList.add(format);
            docContentList.add(separator);
        }
    }


    private BarSkeletonDoc() {

    }

    public static BarSkeletonDoc me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final BarSkeletonDoc ME = new BarSkeletonDoc();
    }
}
