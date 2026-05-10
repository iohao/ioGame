package com.iohao.game.widget.light.profile;

import org.springframework.core.convert.support.DefaultConversionService;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public abstract class AbstractStaticBinder {

    // Spring 标准类型转换器
    protected static final DefaultConversionService CONVERTER = new DefaultConversionService();

    /**
     * 绑定数据到目标类的静态字段
     *
     * @param targetClass 目标类
     */
    public void bind(Class<?> targetClass) {
        if (!isValidDataSource()) {
            return;
        }

        Field[] fields = targetClass.getDeclaredFields();

        Map<String, Object> dataMap = getDataMap();
        dataMap.forEach((key, value) -> {
            if (key == null || value == null) {
                return;
            }

            String normalizedKey = normalize(key.toString());

            for (Field field : fields) {
                int mod = field.getModifiers();

                if (Modifier.isPublic(mod) && Modifier.isStatic(mod)) {
                    String fieldName = field.getName();

                    if (fieldName.equalsIgnoreCase(key.toString()) || normalize(fieldName).equals(normalizedKey)) {
                        try {
                            Object convertedValue = CONVERTER.convert(value, field.getType());
                            field.set(null, convertedValue);
                        } catch (Exception e) {
                            System.err.printf("[%s] 字段 [%s] 赋值失败: %s%n", getSourceName(), fieldName, e.getMessage());
                        }
                        break;
                    }
                }
            }
        });
    }

    /**
     * 数据源是否有效
     */
    protected abstract boolean isValidDataSource();

    /**
     * 获取数据映射（键值对）
     */
    protected abstract Map<String, Object> getDataMap();

    /**
     * 获取数据源名称（用于日志输出）
     */
    protected abstract String getSourceName();

    /**
     * 归一化处理：移除所有下划线并转为小写
     */
    private String normalize(String name) {
        return name.replace("_", "").toLowerCase();
    }
}
