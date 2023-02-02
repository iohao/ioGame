/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.common.log.space;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 渔民小镇
 * @date 2023-01-31
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpaceId {
    static Map<String, SpaceId> GLOBAL_SPACE_ID_CACHE = new ConcurrentHashMap<>();

    final Map<String, String> tags = new HashMap<>();

    final String spaceName;

    public SpaceId(String spaceName) {
        Objects.requireNonNull(spaceName);
        this.spaceName = spaceName;
    }

    public static SpaceId withSpaceName(String spaceName) {
        return GLOBAL_SPACE_ID_CACHE.computeIfAbsent(spaceName, SpaceId::new);
    }

    public SpaceId withTag(String key, String value) {
        tags.put(key, value);
        return this;
    }

    public String getSpaceName() {
        return spaceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SpaceId spaceId = (SpaceId) o;

        if (!tags.equals(spaceId.tags)) {
            return false;
        }

        return Objects.equals(spaceName, spaceId.spaceName);
    }

    @Override
    public int hashCode() {
        int result = tags.hashCode();
        result = 31 * result + spaceName.hashCode();
        return result;
    }

    @Override
    public String toString() {

        if (tags.size() == 0) {
            return spaceName;
        }

        StringBuilder sb = new StringBuilder(spaceName);
        sb.append("[");

        Iterator<Map.Entry<String, String>> iterator = tags.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
