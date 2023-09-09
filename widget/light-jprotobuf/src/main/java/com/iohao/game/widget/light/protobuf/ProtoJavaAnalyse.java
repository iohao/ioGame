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
package com.iohao.game.widget.light.protobuf;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.esotericsoftware.reflectasm.FieldAccess;
import com.iohao.game.common.kit.ClassScanner;
import com.iohao.game.common.kit.StrKit;
import com.iohao.game.common.kit.io.FileKit;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author 渔民小镇
 * @date 2022-01-25
 */
@Slf4j
public class ProtoJavaAnalyse {

    final Map<ProtoJavaRegionKey, ProtoJavaRegion> protoJavaRegionMap = new HashMap<>();

    final Map<Class<?>, ProtoJava> protoJavaMap = new HashMap<>();

    final Map<String, JavaClass> protoJavaSourceFileMap = new HashMap<>();

    public Map<ProtoJavaRegionKey, ProtoJavaRegion> analyse(String protoPackagePath, String protoSourcePath) {
        return this.analyse(protoPackagePath, protoSourcePath, this.predicateFilter);
    }

    public Map<ProtoJavaRegionKey, ProtoJavaRegion> analyse(String protoPackagePath, String protoSourcePath, Predicate<Class<?>> predicateFilter) {

        javaProjectBuilder(protoSourcePath);

        ClassScanner classScanner = new ClassScanner(protoPackagePath, predicateFilter);

        List<Class<?>> classList = classScanner.listScan();

        if (classList.isEmpty()) {
            return protoJavaRegionMap;
        }

        List<ProtoJava> protoJavaList = this.convert(classList);

        for (ProtoJava protoJava : protoJavaList) {
            this.analyseField(protoJava);
        }

        return protoJavaRegionMap;
    }

    private void javaProjectBuilder(String protoSourcePath) {
        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
        javaProjectBuilder.addSourceTree(FileKit.file(protoSourcePath));

        Collection<JavaClass> javaClassCollection = javaProjectBuilder.getClasses();
        for (JavaClass javaClass : javaClassCollection) {
            List<JavaAnnotation> annotations = javaClass.getAnnotations();

            if (annotations.size() < 2) {
                continue;
            }

            long count = annotations.stream().filter(annotation -> {
                String string = annotation.getType().toString();
                return string.contains(ProtobufClass.class.getName())
                        || string.contains(ProtoFileMerge.class.getName());
            }).count();

            if (count < 2) {
                continue;
            }

            protoJavaSourceFileMap.put(javaClass.toString(), javaClass);
            log.info("javaClass: {}", javaClass);
        }
    }

    private List<ProtoJava> convert(List<Class<?>> classList) {

        return classList.stream().map(clazz -> {
            ProtoFileMerge annotation = clazz.getAnnotation(ProtoFileMerge.class);
            String fileName = annotation.fileName();
            String filePackage = annotation.filePackage();
            JavaClass javaClass = protoJavaSourceFileMap.get(clazz.toString());

            ProtoJava protoJava = new ProtoJava()
                    .setClassName(clazz.getSimpleName())
                    .setComment(javaClass.getComment())
                    .setClazz(clazz)
                    .setFileName(fileName)
                    .setFilePackage(filePackage)
                    .setJavaClass(javaClass);

            protoJavaMap.put(clazz, protoJava);

            ProtoJavaRegionKey regionKey = new ProtoJavaRegionKey(fileName, filePackage);
            ProtoJavaRegion protoJavaRegion = this.getProtoJavaRegion(regionKey);
            protoJavaRegion.addProtoJava(protoJava);

            return protoJava;
        }).collect(Collectors.toList());
    }

    private void analyseField(ProtoJava protoJava) {
        Class<?> clazz = protoJava.getClazz();
        Field[] fields;
        int order;

        if (clazz.isEnum()) {
            // 如果是枚举类型，需要单独处理，因为FieldAccess.get(clazz)方法中的Modifier.isStatic(modifiers)判断会过滤枚举内的属性
            Field[] enumFields = clazz.getDeclaredFields();
            List<Field> fieldList = new ArrayList<>(enumFields.length);

            for (Field enumField : enumFields) {
                if (Modifier.isPrivate(enumField.getModifiers())) {
                    // java的Enum有一个隐藏的$VALUES(private)，需要过滤掉---枚举其他属性默认public static
                    continue;
                }
                fieldList.add(enumField);
            }

            fields = new Field[fieldList.size()];
            for (int i = 0; i < fieldList.size(); i++) {
                fields[i] = fieldList.get(i);
            }

            // 枚举enum的下标从0开始
            order = 0;
        } else {
            fields = FieldAccess.get(clazz).getFields();
            // message的下标从1开始
            order = 1;
        }

        JavaClass javaClass = protoJava.getJavaClass();

        for (Field field : fields) {

            String fieldName = field.getName();
            Class<?> fieldTypeClass = field.getType();

            boolean repeated = List.class.equals(fieldTypeClass);

            JavaField javaField = javaClass.getFieldByName(fieldName);

            ProtoJavaField protoJavaField = new ProtoJavaField()
                    .setRepeated(repeated)
                    .setFieldName(fieldName)
                    .setComment(javaField.getComment())
                    .setOrder(order++)
                    .setFieldTypeClass(fieldTypeClass)
                    .setField(field)
                    .setProtoJavaParent(protoJava);

            protoJava.addProtoJavaFiled(protoJavaField);

            String fieldProtoType = ProtoFieldTypeHolder.me().getProtoType(fieldTypeClass);

            if (Objects.nonNull(fieldProtoType)) {
                protoJavaField.setFieldProtoType(fieldProtoType);
                continue;
            }

            if (protoJavaField.isMap()) {
                processMapFieldProtoJava(protoJavaField);
            } else if (protoJavaField.isList()) {
                processListFieldProtoJava(protoJavaField);
            } else {
                processFieldProtoJava(protoJavaField);
            }

        }
    }

    private String fieldProtoTypeToString(ProtoJavaField protoJavaField, Class<?> fieldTypeClass) {
        String fieldName = protoJavaField.getFieldName();
        // 这个字段是一个 proto 对象类型
        ProtoJava protoJavaFieldType = this.getFieldProtoJava(fieldTypeClass, fieldName, protoJavaField);
        ProtoJava protoJavaParent = protoJavaField.getProtoJavaParent();

        String filePackage = protoJavaFieldType.getFilePackage();
        String className = protoJavaFieldType.getClassName();

        String fieldProtoType;

        if (protoJavaParent.inThisFile(protoJavaFieldType)) {
            // 同一个文件的 proto 对象
            fieldProtoType = className;
        } else {
            ProtoJavaRegionKey regionKey = protoJavaParent.getProtoJavaRegionKey();
            ProtoJavaRegion protoJavaRegion = this.getProtoJavaRegion(regionKey);
            protoJavaRegion.addOtherProtoFile(protoJavaFieldType);
            // 不在同一个文件中
            fieldProtoType = StrKit.format("{}.{}", filePackage, className);
        }

        return fieldProtoType;
    }

    private void processFieldProtoJava(ProtoJavaField protoJavaField) {
        // 这个字段是一个 proto 对象类型

        Class<?> fieldTypeClass = protoJavaField.getFieldTypeClass();
        String fieldName = protoJavaField.getFieldName();

        if (Objects.isNull(protoJavaField.getComment())) {
            ProtoJava protoJavaFieldType = this.getFieldProtoJava(fieldTypeClass, fieldName, protoJavaField);
            protoJavaField.setComment(protoJavaFieldType.getComment());
        }

        String fieldProtoType = this.fieldProtoTypeToString(protoJavaField, fieldTypeClass);
        protoJavaField.setFieldProtoType(fieldProtoType);
    }


    private void processListFieldProtoJava(ProtoJavaField protoJavaField) {
        // map 类型
        Field field = protoJavaField.getField();

        // 获取 map 的 <k,v> 类型
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        Type[] actualTypeArguments = genericType.getActualTypeArguments();

        Class<?> firstClass = (Class<?>) actualTypeArguments[0];
        String fieldProtoType = ProtoFieldTypeHolder.me().getProtoType(firstClass);

        if (Objects.isNull(fieldProtoType)) {
            fieldProtoType = this.fieldProtoTypeToString(protoJavaField, firstClass);
        }

        protoJavaField
                .setRepeated(true)
                .setFieldProtoType(fieldProtoType)
        ;

    }

    private void processMapFieldProtoJava(ProtoJavaField protoJavaField) {

        Map<String, String> map = new HashMap<>();


        // map 类型
        Field field = protoJavaField.getField();

        // 获取 map 的 <k,v> 类型
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        Type[] actualTypeArguments = genericType.getActualTypeArguments();

        Class<?> keyClass = (Class<?>) actualTypeArguments[0];
        String keyFieldProtoType = ProtoFieldTypeHolder.me().getProtoType(keyClass);
        map.put("keyStr", keyFieldProtoType);

        if (Objects.isNull(keyFieldProtoType)) {
            // key 是一个 proto 对象类型
            String keyStr = this.fieldProtoTypeToString(protoJavaField, keyClass);
            map.put("keyStr", keyStr);
        }

        Class<?> valueClass = (Class<?>) actualTypeArguments[1];
        String valueFieldProtoType = ProtoFieldTypeHolder.me().getProtoType(valueClass);
        map.put("valueStr", valueFieldProtoType);
        if (Objects.isNull(valueFieldProtoType)) {
            // value 是一个 proto 对象类型
            String valueStr = this.fieldProtoTypeToString(protoJavaField, valueClass);
            map.put("valueStr", valueStr);
        }

        String fieldProtoType = StrKit.format("map<{keyStr},{valueStr}>", map);
        protoJavaField.setFieldProtoType(fieldProtoType);
    }

    private ProtoJava getFieldProtoJava(Class<?> fieldTypeClass, String fieldName, ProtoJavaField protoJavaField) {

        if (!predicateFilter.test(fieldTypeClass)) {

            String templateErr = """
                    {}.{} class type not is protobuf !
                    class must import annotation {}
                    class must import annotation {}
                    """;

            String errorMsg = StrKit.format(templateErr
                    , protoJavaField.getProtoJavaParent().getClassName()
                    , fieldName
                    , ProtobufClass.class
                    , ProtoFileMerge.class
            );

            throw new RuntimeException(errorMsg);
        }

        return this.protoJavaMap.get(fieldTypeClass);
    }

    private final Predicate<Class<?>> predicateFilter = (clazz) -> {
        ProtobufClass annotation = clazz.getAnnotation(ProtobufClass.class);
        ProtoFileMerge protoFileMerge = clazz.getAnnotation(ProtoFileMerge.class);

        return Objects.nonNull(annotation) && Objects.nonNull(protoFileMerge);
    };

    private ProtoJavaRegion getProtoJavaRegion(ProtoJavaRegionKey key) {
        ProtoJavaRegion protoJavaRegion = protoJavaRegionMap.get(key);

        if (Objects.isNull(protoJavaRegion)) {

            protoJavaRegion = new ProtoJavaRegion();

            protoJavaRegion = protoJavaRegionMap.putIfAbsent(key, protoJavaRegion);

            if (Objects.isNull(protoJavaRegion)) {
                protoJavaRegion = protoJavaRegionMap.get(key);
            }

            protoJavaRegion.setFileName(key.fileName());
            protoJavaRegion.setFilePackage(key.filePackage());
        }

        return protoJavaRegion;
    }
}
