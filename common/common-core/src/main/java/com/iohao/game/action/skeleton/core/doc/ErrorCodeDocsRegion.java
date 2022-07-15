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

import java.util.*;

/**
 * 错误码域
 *
 * @author 渔民小镇
 * @date 2022-02-03
 */
public class ErrorCodeDocsRegion {

    Set<ErrorCodeDoc> errorCodeDocSet = new HashSet<>();

    public void addErrorCodeDocs(ErrorCodeDocs errorCodeDocs) {
        List<ErrorCodeDoc> errorCodeDocList = errorCodeDocs.getErrorCodeDocList();
        errorCodeDocSet.addAll(errorCodeDocList);
    }

    public List<ErrorCodeDoc> listErrorCodeDoc() {

        List<ErrorCodeDoc> list = new ArrayList<>(this.errorCodeDocSet);

        // 排序
        list.sort((o1, o2) -> o1.code - o2.getCode());

        return list;
    }
}
