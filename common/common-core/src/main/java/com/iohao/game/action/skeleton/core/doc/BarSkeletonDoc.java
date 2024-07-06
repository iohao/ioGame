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
import lombok.Setter;

import java.util.*;

/**
 * 游戏文档生成
 *
 * @author 渔民小镇
 * @date 2022-01-23
 * @deprecated 请使用 {@link IoGameDocumentHelper}
 */
@Deprecated
public final class BarSkeletonDoc {

    final List<BarSkeleton> skeletonList = new LinkedList<>();

    @Setter
    String docFileName = "doc_game.txt";

    @Setter
    String docPath;

    /**
     * 只有当 this.generateDoc 为 true 时，才会执行 set 操作
     *
     * @param generateDoc generateDoc
     */
    public void setGenerateDoc(boolean generateDoc) {
        IoGameDocumentHelper.setGenerateDoc(generateDoc);
    }

    public void addSkeleton(BarSkeleton barSkeleton) {
        skeletonList.add(barSkeleton);
    }

    public void buildDoc() {
        IoGameDocumentHelper.generateDocument();
    }

    @Deprecated
    public void buildDoc(String docPath) {
        buildDoc();
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