package com.iohao.game.widget.light.profile;

import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * @Author: caochaojie
 * @Date: 2026-01-2215:29
 */
public class ProfileStaticBinder {

    // Spring 标准类型转换器：支持 String -> int/long/boolean/Enum/Array 等
    private static final DefaultConversionService CONVERTER = new DefaultConversionService();

    public static void bind(Class<?> targetClass, Map<?, ?> configMap) {
        if (configMap == null || configMap.isEmpty()) {
            return;
        }

        // 获取类中定义的全部字段
        Field[] fields = targetClass.getDeclaredFields();

        configMap.forEach((key, value) -> {
            if (key == null || value == null) return;

            String configKey = key.toString();
            // 归一化处理：去掉下划线并转为小写
            String normalizedConfigKey = normalize(configKey);

            for (Field field : fields) {
                int mod = field.getModifiers();

                // 仅处理 public static 字段
                if (Modifier.isPublic(mod) && Modifier.isStatic(mod)) {
                    String fieldName = field.getName();

                    // 匹配规则：原始名一致 OR 归一化后一致
                    if (fieldName.equalsIgnoreCase(configKey) || normalize(fieldName).equals(normalizedConfigKey)) {
                        try {
                            field.setAccessible(true);
                            Object convertedValue = CONVERTER.convert(value, field.getType());
                            field.set(null, convertedValue);
                        } catch (Exception e) {
                            System.err.printf("[Config] 字段 [%s] 赋值失败: %s%n", fieldName, e.getMessage());
                        }
                        break; // 匹配到一个字段后跳出内层循环
                    }
                }
            }
        });
    }

    /**
     * 归一化处理：移除所有下划线并转为小写
     * 例如: SERVER_ID -> serverid, serverId -> serverid, server_id -> serverid
     */
    private static String normalize(String name) {
        return name.replace("_", "").toLowerCase();
    }
}