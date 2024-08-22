package com.iohao.game.common.kit.adapter;


import java.io.Serial;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuCharFinder extends HuTextFinder {
    @Serial
    private static final long serialVersionUID = 1L;

    private final char c;
    private final boolean caseInsensitive;

    /**
     * 构造，不忽略字符大小写
     *
     * @param c 被查找的字符
     */
    public HuCharFinder(char c) {
        this(c, false);
    }

    /**
     * 构造
     *
     * @param c               被查找的字符
     * @param caseInsensitive 是否忽略大小写
     */
    public HuCharFinder(char c, boolean caseInsensitive) {
        this.c = c;
        this.caseInsensitive = caseInsensitive;
    }

    @Override
    public int start(int from) {
        HuAssert.notNull(this.text, "Text to find must be not null!");
        final int limit = getValidEndIndex();
        if (negative) {
            for (int i = from; i > limit; i--) {
                if (HuCharUtil.equals(c, text.charAt(i), caseInsensitive)) {
                    return i;
                }
            }
        } else {
            for (int i = from; i < limit; i++) {
                if (HuCharUtil.equals(c, text.charAt(i), caseInsensitive)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int end(int start) {
        if (start < 0) {
            return -1;
        }
        return start + 1;
    }
}
