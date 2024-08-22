package com.iohao.game.common.kit.adapter;

import java.util.Arrays;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuArrayUtil {
    public static final int INDEX_NOT_FOUND = -1;

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }


    public static boolean isArray(Object obj) {
        return null != obj && obj.getClass().isArray();
    }

    public static String toString(Object obj) {
        if (null == obj) {
            return null;
        }

        if (obj instanceof long[] value) {
            return Arrays.toString(value);
        } else if (obj instanceof int[] value) {
            return Arrays.toString(value);
        } else if (obj instanceof short[] value) {
            return Arrays.toString(value);
        } else if (obj instanceof char[] value) {
            return Arrays.toString(value);
        } else if (obj instanceof byte[] value) {
            return Arrays.toString(value);
        } else if (obj instanceof boolean[] value) {
            return Arrays.toString(value);
        } else if (obj instanceof float[] value) {
            return Arrays.toString(value);
        } else if (obj instanceof double[] value) {
            return Arrays.toString(value);
        } else if (HuArrayUtil.isArray(obj)) {
            // 对象数组
            try {
                return Arrays.deepToString((Object[]) obj);
            } catch (Exception ignore) {
                //ignore
            }
        }

        return obj.toString();
    }

    public static boolean contains(char[] array, char value) {
        return indexOf(array, value) > INDEX_NOT_FOUND;
    }

    public static int indexOf(char[] array, char value) {
        if (null != array) {
            for (int i = 0; i < array.length; i++) {
                if (value == array[i]) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }
}
