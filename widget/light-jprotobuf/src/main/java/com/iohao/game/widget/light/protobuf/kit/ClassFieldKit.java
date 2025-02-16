package com.iohao.game.widget.light.protobuf.kit;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 类属性获取工具
 *
 * @author DBhat
 * @date 2025-02-16
 */
public class ClassFieldKit {

    /**
     * 获取类的所有属性集合
     *
     * @param javaClass javaClass
     * @return 类的所有JavaField
     */
    public static List<JavaField> getAllFields(JavaClass javaClass) {
        List<JavaField> allFields = new ArrayList<>(javaClass.getFields());
        JavaClass superClass = javaClass.getSuperJavaClass();
        while (superClass != null) {
            allFields.addAll(superClass.getFields());
            superClass = superClass.getSuperJavaClass();
        }
        return allFields;
    }

    /**
     * 获取类的所有属性集合
     *
     * @param javaClass javaClass
     * @return 类的所有JavaField
     */
    public static Map<String, JavaField> getAllFieldMap(JavaClass javaClass) {
        Map<String, JavaField> allFieldMap = javaClass.getFields().stream()
                .collect(Collectors.toMap(JavaField::getName, Function.identity()));
        JavaClass superClass = javaClass.getSuperJavaClass();
        while (superClass != null) {
            allFieldMap.putAll(superClass.getFields().stream()
                    .collect(Collectors.toMap(JavaField::getName, Function.identity())));
            superClass = superClass.getSuperJavaClass();
        }
        return allFieldMap;
    }
}
