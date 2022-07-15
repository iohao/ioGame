/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.common.kit.abc;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2022-03-10
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BirdAttrOptionDynamic implements AttrOptionDynamic {
    /** 动态属性 */
    final AttrOptions options = new AttrOptions();
}

interface BirdAttrOption {
    AttrOption<String> name = AttrOption.valueOf("name");
    AttrOption<Integer> age = AttrOption.valueOf("age");
}

record AttrOption<T>(String name) {
    /** 构建属性项 */
    public static <T> AttrOption<T> valueOf(String name) {
        return new AttrOption<T>(name);
    }
}

class AttrOptions {
    /** 动态成员属性 */
    final Map<AttrOption<?>, Object> options = new HashMap<>();

    /** 获取值 */
    @SuppressWarnings("unchecked")
    public <T> T option(AttrOption<T> option) {
        return (T) options.get(option);
    }

    /** 设置值 */
    public <T> void option(AttrOption<T> option, T value) {
        options.put(option, value);
    }
}

interface AttrOptionDynamic {
    /** 动态成员属性 */
    AttrOptions getOptions();

    /** 获取值 */
    default <T> T option(AttrOption<T> option) {
        return this.getOptions().option(option);
    }

    /** 设置值 */
    default <T> void option(AttrOption<T> option, T value) {
        this.getOptions().option(option, value);
    }
}

class BirdAttrOptionDynamicTest {
    public static void main(String[] args) {
//        BirdAttrOptionDynamic bird = new BirdAttrOptionDynamic();
//
//        // 设置属性
//        bird.option(BirdAttrOption.name, "塔姆");
//        bird.option(BirdAttrOption.age, 19);
//
//        // 获取属性
//        String name = bird.option(BirdAttrOption.name);
//        int age = bird.option(BirdAttrOption.age);
//
//        System.out.println(name);
//        System.out.println(age);


        AttrOption<Long> love = AttrOption.valueOf("love");

        final AttrOptions options = new AttrOptions();
        options.option(love, 777L);
        Long loveValue = options.option(love);
        System.out.println(loveValue);
    }
}
