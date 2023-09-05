/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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

import com.iohao.game.action.skeleton.core.*;
import com.iohao.game.common.kit.StrKit;
import com.iohao.game.common.kit.io.FileKit;
import lombok.Setter;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

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

    @Setter
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

    public void buildDoc() {

        if (!generateDoc) {
            return;
        }

        // 路径为当前项目
        if (Objects.isNull(this.docPath)) {
            this.docPath = System.getProperty("user.dir") + File.separator + docFileName;
        }

        this.buildDoc(this.docPath);
    }

    public void buildDoc(String docPath) {

        Objects.requireNonNull(docPath);

        if (FileKit.isDirectory(docPath)) {
            throw new RuntimeException("file is Directory ");
        }

        cmdDataClassRegionDevInfo();

        ActionSendDocsRegion actionSendDocsRegion = this.createActionSendDocsRegion();

        List<String> docContentList = new ArrayList<>(128);

        // 加上游戏文档格式说明
        this.gameDocURLDescription(docContentList);

        // 生成文档 - action
        ActionDocs.stream().forEach(actionDoc -> {
            DocInfo docInfo = new DocInfo();
            docInfo.actionSendDocsRegion = actionSendDocsRegion;

            actionDoc.stream()
                    .map(ActionCommandDoc::getActionCommand)
                    .filter(Objects::nonNull)
                    .forEach(subBehavior -> {
                        docInfo.setHead(subBehavior);
                        docInfo.add(subBehavior);
                    });

            String render = docInfo.render();
            docContentList.add(render);

        });

        // 生成文档 - 广播（推送）文档
        extractedActionSend(actionSendDocsRegion, docContentList);

        // 生成文档 - 错误码文档
        extractedErrorCode(docContentList);

        String docText = String.join("", docContentList);
        FileKit.writeUtf8String(docText, docPath);
    }

    public List<BarSkeleton> listBarSkeleton() {
        return new ArrayList<>(this.skeletonList);
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

            String template = "路由: {cmd} - {subCmd}  --- 广播推送: {dataClass}";

            if (StrKit.isNotEmpty(actionSendDoc.getDescription())) {
                template = "路由: {cmd} - {subCmd}  --- 广播推送: {dataClass} ({description})";
            }

            var stringObjectMap = new HashMap<>();
            stringObjectMap.put("cmd", actionSendDoc.getCmd());
            stringObjectMap.put("subCmd", actionSendDoc.getSubCmd());
            stringObjectMap.put("dataClass", actionSendDoc.getDataClass().getName());
            stringObjectMap.put("description", actionSendDoc.getDescription());


            String format = StrKit.format(template, stringObjectMap);

            docContentList.add(format);
            docContentList.add(separator);
        }
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

    private void cmdDataClassRegionDevInfo() {
        // 这个方法主要是保存一下 cmd 路由对应的响应数据类型信息
        skeletonList.stream()
                // 得到业务框架的 ActionCommandRegions
                .map(BarSkeleton::getActionCommandRegions)
                // 将 map.values 合并成一个 list，即将 ActionCommandRegions 中的 regionMap 的 value 转为 stream
                .flatMap((Function<ActionCommandRegions, Stream<ActionCommandRegion>>) actionCommandRegions -> actionCommandRegions.getRegionMap().values().stream())
                // 将 map.values 合并成一个 list，即将 ActionCommandRegion 中的 subActionCommandMap 的 value 转为 stream
                .flatMap((Function<ActionCommandRegion, Stream<ActionCommand>>) actionCommandRegion -> actionCommandRegion.values().stream())
                .forEach(actionCommand -> {
                    // 路由
                    CmdInfo cmdInfo = actionCommand.getCmdInfo();
                    // action 的返回值
                    ActionCommand.ActionMethodReturnInfo actionMethodReturnInfo = actionCommand.getActionMethodReturnInfo();
                    Class<?> dataClass = actionMethodReturnInfo.getActualTypeArgumentClazz();
                    // cmd 路由对应的响应数据类型信息
                    DevConfig.put(cmdInfo.getCmdMerge(), dataClass);
                });
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
