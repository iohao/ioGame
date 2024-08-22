/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - present double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.common.validation.processor;

import autovalue.shaded.com.google.common.auto.service.AutoService;
import com.iohao.game.common.validation.annotation.EnableValidation;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;

/**
 * 在 META-INF 生成 ioGame/com.iohao.game.common.validation.Validator 用于游戏服务支持 javax.validation
 * 使用示例：
 * 在 DemoApplication 上加入注解：@EnableValidation("com.iohao.game.common.validation.support.JakartaValidator")
 *
 * @author shenjk
 * @date 2022-09-26
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.iohao.game.common.validation.annotation.EnableValidation")
public class ValidationProcessor extends AbstractProcessor {

    boolean created = false;

    /**
     * {@inheritDoc}
     *
     * @param annotations
     * @param roundEnv
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver() && !annotations.isEmpty() && !created) {
            created = true;

            EnableValidation enableValidation = getEnableValidation(roundEnv);
            if (enableValidation == null) {
                return true;
            }

            String className = enableValidation.value();
            // 生成META-INF/ioGame/com.iohao.game.common.validation.Validator 配置文件
            createMetaInf(processingEnv, className);
        }

        return true;
    }

    /**
     * 获取EnableValidation注解对象
     *
     * @param roundEnv roundEnv
     */
    private static EnableValidation getEnableValidation(RoundEnvironment roundEnv) {
        Set<? extends Element> rootElements = roundEnv.getElementsAnnotatedWith(EnableValidation.class);
        if (rootElements != null && !rootElements.isEmpty()) {
            Element element = rootElements.stream().findFirst().get();
            return element.getAnnotation(EnableValidation.class);
        }

        return null;
    }


    /**
     * 生成 META-INF 下的配置文件
     *
     * @param processingEnv processingEnv
     * @param content       content
     */
    private static void createMetaInf(ProcessingEnvironment processingEnv, String content) {
        try {

            FileObject f = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT,
                    "",
                    "META-INF/ioGame/com.iohao.game.common.validation.Validator");

            try (Writer w = f.openWriter()) {
                PrintWriter pw = new PrintWriter(w);
                pw.println(content);
                pw.flush();
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }
}
