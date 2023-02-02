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
package com.iohao.game.common.log.factory;

import ch.qos.logback.classic.LoggerContext;
import com.iohao.game.common.log.LogSpace;
import com.iohao.game.common.log.space.SpaceId;

import java.net.URL;

/**
 * @author 渔民小镇
 * @date 2023-01-31
 */
public class LoggerSpaceFactory4LogbackBuilder extends AbstractLoggerSpaceFactoryBuilder {

    public LoggerSpaceFactory4LogbackBuilder(SpaceId spaceId, LogSpace logSpace) {
        super(spaceId, logSpace);
    }

    @Override
    public AbstractLoggerSpaceFactory doBuild(String spaceName, ClassLoader spaceClassloader,
                                              URL url) {
        return new LogbackLoggerSpaceFactory(getSpaceId(), new LoggerContext(), getProperties(),
                url, getLoggingToolName());
    }

    @Override
    protected String getLoggingToolName() {
        return "logback";
    }
}
