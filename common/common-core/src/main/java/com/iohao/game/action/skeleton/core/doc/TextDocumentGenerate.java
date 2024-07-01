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
import com.iohao.game.common.kit.io.FileKit;
import lombok.Setter;
import org.jctools.maps.NonBlockingHashMap;

import java.io.File;
import java.util.*;

/**
 * @author 渔民小镇
 * @date 2024-06-25
 */
@Setter
public final class TextDocumentGenerate implements DocumentGenerate {
    /** 文档生成后所存放的目录 */
    String path = System.getProperty("user.dir") + File.separator + "doc_game.txt";

    @Override
    public void generate(IoGameDocument ioGameDocument) {
        BroadcastDocumentRegion broadcastDocumentRegion = ioGameDocument.getBroadcastDocumentRegion();

        List<String> docContentList = new ArrayList<>(128);

        // 加上游戏文档格式说明
        this.gameDocURLDescription(docContentList);

        Map<Integer, BroadcastDocument> broadcastDocumentMap = new NonBlockingHashMap<>();
        broadcastDocumentMap.putAll(broadcastDocumentRegion.getMap());

        // 生成文档 - action
        ioGameDocument.streamActionDoc().forEach(actionDoc -> {
            DocInfo docInfo = new DocInfo();
            docInfo.broadcastDocumentMap = broadcastDocumentMap;

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
        extractedBroadcastDoc(broadcastDocumentMap, docContentList);

        // 生成文档 - 错误码文档
        ErrorCodeDocsRegion errorCodeDocsRegion = ioGameDocument.getErrorCodeDocsRegion();
        extractedErrorCode(errorCodeDocsRegion, docContentList);

        // 写文件
        String docText = String.join("", docContentList);
        FileKit.writeUtf8String(docText, path);
    }

    private void gameDocURLDescription(List<String> docContentList) {
        // 加上游戏文档格式说明
        String gameDocInfo = """
                ==================== 游戏文档格式说明 ====================
                https://www.yuque.com/iohao/game/irth38#cJLdC
                                
                """;

        docContentList.add(gameDocInfo);
    }

    private void extractedBroadcastDoc(Map<Integer, BroadcastDocument> broadcastDocumentMap, List<String> docContentList) {

        var broadcastDocumentList = broadcastDocumentMap.values();
        if (broadcastDocumentList.isEmpty()) {
            return;
        }

        String separator = System.lineSeparator();

        docContentList.add("==================== 其它广播推送 ====================");
        docContentList.add(separator);

        for (BroadcastDocument broadcastDocument : broadcastDocumentList) {

            String template = "路由: {cmd} - {subCmd}  --- 广播推送: {dataClass} {dataDescription}";

            if (StrKit.isNotEmpty(broadcastDocument.getMethodDescription())) {
                template = "路由: {cmd} - {subCmd}  --- 广播推送: {dataClass} {dataDescription}，({description})";
            }

            var stringObjectMap = new HashMap<>();
            stringObjectMap.put("cmd", broadcastDocument.getCmd());
            stringObjectMap.put("subCmd", broadcastDocument.getSubCmd());
            stringObjectMap.put("dataClass", broadcastDocument.getDataClassName());
            stringObjectMap.put("description", broadcastDocument.getMethodDescription());
            stringObjectMap.put("dataDescription", broadcastDocument.getDataDescription());

            String format = StrKit.format(template, stringObjectMap);

            docContentList.add(format);
            docContentList.add(separator);
        }
    }

    private void extractedErrorCode(ErrorCodeDocsRegion errorCodeDocsRegion, List<String> docContentList) {

        String separator = System.lineSeparator();

        docContentList.add("==================== 错误码 ====================");
        docContentList.add(separator);

        for (ErrorCodeDoc errorCodeDoc : errorCodeDocsRegion.listErrorCodeDoc()) {
            String template = " {} : {} ";

            String format = StrKit.format(template, errorCodeDoc.getCode(), errorCodeDoc.getMsg());
            docContentList.add(format);
            docContentList.add(separator);
        }
    }
}
