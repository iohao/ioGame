package com.iohao.game.common.kit.adapter;


import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuStrUtil {

    public static final char C_SLASH = HuCharUtil.SLASH;


    public static final char C_BACKSLASH = HuCharUtil.BACKSLASH;

    public static final String DOT = ".";


    public static final String DOUBLE_DOT = "..";


    public static final String SLASH = "/";


    public static final String COLON = ":";


    public static final String EMPTY_JSON = "{}";

    public static final String EMPTY = "";

    public static final int INDEX_NOT_FOUND = -1;
    public static final String NULL = "null";

    public static String utf8Str(Object obj) {
        return str(obj, HuCharsetUtil.CHARSET_UTF_8);
    }

    public static String str(Object obj, Charset charset) {
        if (null == obj) {
            return null;
        }

        if (obj instanceof String value) {
            return value;
        } else if (obj instanceof byte[] value) {
            return str(value, charset);
        } else if (obj instanceof Byte[] value) {
            return str(value, charset);
        } else if (obj instanceof ByteBuffer value) {
            return str(value, charset);
        } else if (HuArrayUtil.isArray(obj)) {
            return HuArrayUtil.toString(obj);
        }

        return obj.toString();
    }

    public static String str(Byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }

        byte[] bytes = new byte[data.length];
        Byte dataByte;
        for (int i = 0; i < data.length; i++) {
            dataByte = data[i];
            bytes[i] = (null == dataByte) ? -1 : dataByte;
        }

        return str(bytes, charset);
    }

    public static String str(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }

        if (null == charset) {
            return new String(data);
        }
        return new String(data, charset);
    }

    public static String str(ByteBuffer data, Charset charset) {
        if (null == charset) {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }

    public static boolean isBlank(CharSequence str) {
        final int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (!HuCharUtil.isBlankChar(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    public static String format(CharSequence template, Object... params) {
        if (null == template) {
            return NULL;
        }
        if (HuArrayUtil.isEmpty(params) || isBlank(template)) {
            return template.toString();
        }
        return HuStrFormatter.format(template.toString(), params);
    }

    public static String format(CharSequence template, Map<?, ?> map) {
        return HuStrFormatter.format(template, map, true);
    }

    public static String cleanBlank(CharSequence str) {
        return filter(str, c -> !HuCharUtil.isBlankChar(c));
    }

    public static String filter(CharSequence str, final HuFilter<Character> filter) {
        if (str == null || filter == null) {
            return str(str);
        }

        int len = str.length();
        final StringBuilder sb = new StringBuilder(len);
        char c;
        for (int i = 0; i < len; i++) {
            c = str.charAt(i);
            if (filter.accept(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String replace(CharSequence str, CharSequence searchStr, CharSequence replacement) {
        return replace(str, 0, searchStr, replacement, false);
    }

    public static boolean contains(CharSequence str, CharSequence searchStr) {
        if (null == str || null == searchStr) {
            return false;
        }
        return str.toString().contains(searchStr);
    }

    public static String replace(CharSequence str, int fromIndex, CharSequence searchStr, CharSequence replacement, boolean ignoreCase) {
        if (isEmpty(str) || isEmpty(searchStr)) {
            return str(str);
        }
        if (null == replacement) {
            replacement = EMPTY;
        }

        final int strLength = str.length();
        final int searchStrLength = searchStr.length();
        if (strLength < searchStrLength) {
            // issue#I4M16G@Gitee
            return str(str);
        }

        if (fromIndex > strLength) {
            return str(str);
        } else if (fromIndex < 0) {
            fromIndex = 0;
        }

        final StringBuilder result = new StringBuilder(strLength - searchStrLength + replacement.length());
        if (0 != fromIndex) {
            result.append(str.subSequence(0, fromIndex));
        }

        int preIndex = fromIndex;
        int index;
        while ((index = indexOf(str, searchStr, preIndex, ignoreCase)) > -1) {
            result.append(str.subSequence(preIndex, index));
            result.append(replacement);
            preIndex = index + searchStrLength;
        }

        if (preIndex < strLength) {
            // 结尾部分
            result.append(str.subSequence(preIndex, strLength));
        }
        return result.toString();
    }

    public static int indexOf(CharSequence text, CharSequence searchStr, int from, boolean ignoreCase) {
        if (isEmpty(text) || isEmpty(searchStr)) {
            if (HuStrUtil.equals(text, searchStr)) {
                return 0;
            } else {
                return INDEX_NOT_FOUND;
            }
        }
        return new HuStrFinder(searchStr, ignoreCase).setText(text).start(from);
    }

    public static boolean equals(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, false);
    }

    public static List<String> split(CharSequence text, char separator) {

        if (null == text) {
            return new ArrayList<>(0);
        }

        final HuSplitIter splitIter = new HuSplitIter(text, new HuCharFinder(separator), 0, false);

        UnaryOperator<String> mapping = str -> str;
        return splitIter.toList(mapping);
    }

    public static String nullToEmpty(CharSequence str) {
        return nullToDefault(str, EMPTY);
    }

    public static String trim(CharSequence str) {
        return (null == str) ? null : trim(str, 0);
    }


    public static boolean isEmptyIfStr(Object obj) {
        if (null == obj) {
            return true;
        } else if (obj instanceof CharSequence cs) {
            return 0 == cs.length();
        }

        return false;
    }


    public static String removePrefix(CharSequence str, CharSequence prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return str(str);
        }

        final String str2 = str.toString();
        if (str2.startsWith(prefix.toString())) {
            return subSuf(str2, prefix.length());// 截取后半段
        }
        return str2;
    }


    public static String nullToDefault(CharSequence str, String defaultStr) {
        return (str == null) ? defaultStr : str.toString();
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String trimStart(CharSequence str) {
        return trim(str, -1);
    }

    public static String trim(CharSequence str, int mode) {
        return trim(str, mode, HuCharUtil::isBlankChar);
    }

    public static String trim(CharSequence str, int mode, Predicate<Character> predicate) {
        String result;
        if (str == null) {
            result = null;
        } else {
            int length = str.length();
            int start = 0;
            int end = length;// 扫描字符串头部
            if (mode <= 0) {
                while ((start < end) && (predicate.test(str.charAt(start)))) {
                    start++;
                }
            }// 扫描字符串尾部
            if (mode >= 0) {
                while ((start < end) && (predicate.test(str.charAt(end - 1)))) {
                    end--;
                }
            }
            if ((start > 0) || (end < length)) {
                result = str.toString().substring(start, end);
            } else {
                result = str.toString();
            }
        }

        return result;
    }

    public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return str(str);
        }

        final String str2 = str.toString();
        if (startWithIgnoreCase(str, prefix)) {
            return subSuf(str2, prefix.length());// 截取后半段
        }
        return str2;
    }

    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    public static boolean startWithIgnoreCase(CharSequence str, CharSequence prefix) {
        return startWith(str, prefix, true);
    }

    public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
        return startWith(str, prefix, ignoreCase, false);
    }

    public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase, boolean ignoreEquals) {
        if (null == str || null == prefix) {
            if (ignoreEquals) {
                return false;
            }
            return null == str && null == prefix;
        }

        boolean isStartWith = str.toString()
                .regionMatches(ignoreCase, 0, prefix.toString(), 0, prefix.length());

        if (isStartWith) {
            return (!ignoreEquals) || (!equals(str, prefix, ignoreCase));
        }
        return false;
    }


    public static boolean startWith(CharSequence str, char c) {
        if (isEmpty(str)) {
            return false;
        }
        return c == str.charAt(0);
    }

    public static String subSuf(CharSequence string, int fromIndex) {
        if (isEmpty(string)) {
            return null;
        }
        return sub(string, fromIndex, string.length());
    }

    public static String sub(CharSequence str, int fromIndexInclude, int toIndexExclude) {
        if (isEmpty(str)) {
            return str(str);
        }
        int len = str.length();

        if (fromIndexInclude < 0) {
            fromIndexInclude = len + fromIndexInclude;
            if (fromIndexInclude < 0) {
                fromIndexInclude = 0;
            }
        } else if (fromIndexInclude > len) {
            fromIndexInclude = len;
        }

        if (toIndexExclude < 0) {
            toIndexExclude = len + toIndexExclude;
            if (toIndexExclude < 0) {
                toIndexExclude = len;
            }
        } else if (toIndexExclude > len) {
            toIndexExclude = len;
        }

        if (toIndexExclude < fromIndexInclude) {
            int tmp = fromIndexInclude;
            fromIndexInclude = toIndexExclude;
            toIndexExclude = tmp;
        }

        if (fromIndexInclude == toIndexExclude) {
            return EMPTY;
        }

        return str.toString().substring(fromIndexInclude, toIndexExclude);
    }

    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (null == str1) {
            // 只有两个都为null才判断相等
            return str2 == null;
        }
        if (null == str2) {
            // 字符串2空，字符串1非空，直接false
            return false;
        }

        if (ignoreCase) {
            return str1.toString().equalsIgnoreCase(str2.toString());
        } else {
            return str1.toString().contentEquals(str2);
        }
    }

    public static boolean isSubEquals(CharSequence str1, int start1, CharSequence str2, int start2, int length, boolean ignoreCase) {
        if (null == str1 || null == str2) {
            return false;
        }

        return str1.toString().regionMatches(ignoreCase, start1, str2.toString(), start2, length);
    }
}
