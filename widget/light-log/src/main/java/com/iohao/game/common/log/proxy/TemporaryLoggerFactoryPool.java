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
package com.iohao.game.common.log.proxy;

import com.iohao.game.common.log.*;
import com.iohao.game.common.log.space.SpaceId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 渔民小镇
 * @date 2023-01-31
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TemporaryLoggerFactoryPool {

    static final ConcurrentHashMap<SpaceIdWithClassloader, TemporaryLoggerFactory> temporaryILoggerFactoryMap = new ConcurrentHashMap<SpaceIdWithClassloader, TemporaryLoggerFactory>();
    static final Logger logger = LoggerFactory.getLogger(LoggerSpaceManager.class);

    public static TemporaryLoggerFactory get(String space, ClassLoader spaceClassLoader) {
        return get(new SpaceId(space), spaceClassLoader);
    }

    public static TemporaryLoggerFactory get(SpaceId spaceId, ClassLoader spaceClassLoader) {
        // get from local cache by key {spaceClassLoader + spacename};
        TemporaryLoggerFactory temporaryLoggerFactory = temporaryILoggerFactoryMap
                .get(new SpaceIdWithClassloader(spaceId, spaceClassLoader));

        if (temporaryLoggerFactory != null) {
            return temporaryLoggerFactory;
        }

        // create new one
        temporaryLoggerFactory = new TemporaryLoggerFactory(spaceId, spaceClassLoader, logger);

        // put it to cache ;
        var spaceIdWithClassloader = new SpaceIdWithClassloader(spaceId, spaceClassLoader);
        temporaryILoggerFactoryMap.putIfAbsent(spaceIdWithClassloader, temporaryLoggerFactory);

        return temporaryLoggerFactory;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class SpaceIdWithClassloader {
        SpaceId spaceId;
        ClassLoader classLoader;

        public SpaceIdWithClassloader() {
        }

        public SpaceIdWithClassloader(SpaceId spaceId, ClassLoader classLoader) {
            this.spaceId = spaceId;
            this.classLoader = classLoader;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            SpaceIdWithClassloader that = (SpaceIdWithClassloader) o;

            if (!Objects.equals(spaceId, that.spaceId)) {
                return false;
            }

            return Objects.equals(classLoader, that.classLoader);

        }

        @Override
        public int hashCode() {
            int result = spaceId != null ? spaceId.hashCode() : 0;
            result = 31 * result + (classLoader != null ? classLoader.hashCode() : 0);
            return result;
        }
    }
}
