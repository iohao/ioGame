package com.iohao.game.common.kit.adapter;


/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
 class HuCharUtil {

    public static final char SPACE = ' ';

    public static final char SLASH = '/';

    public static final char BACKSLASH = '\\';

    public static boolean isBlankChar(char c) {
        return isBlankChar((int) c);
    }

    public static boolean equals(char c1, char c2, boolean caseInsensitive) {
        if (caseInsensitive) {
            return Character.toLowerCase(c1) == Character.toLowerCase(c2);
        }
        return c1 == c2;
    }

    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c)
                || Character.isSpaceChar(c)
                || c == '\ufeff'
                || c == '\u202a'
                || c == '\u0000'
                // issue#I5UGSQ，Hangul Filler
                || c == '\u3164'
                // Braille Pattern Blank
                || c == '\u2800'
                // MONGOLIAN VOWEL SEPARATOR
                || c == '\u180e';
    }

    public static boolean isFileSeparator(char c) {
        return SLASH == c || BACKSLASH == c;
    }
}
