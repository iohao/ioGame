package com.iohao.game.common.kit.adapter;


/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuStrFinder extends HuTextFinder {

    private final CharSequence strToFind;
    private final boolean caseInsensitive;

    public HuStrFinder(CharSequence strToFind, boolean caseInsensitive) {
        HuAssert.notEmpty(strToFind);
        this.strToFind = strToFind;
        this.caseInsensitive = caseInsensitive;
    }

    @Override
    public int start(int from) {
        HuAssert.notNull(this.text, "Text to find must be not null!");
        final int subLen = strToFind.length();

        if (from < 0) {
            from = 0;
        }
        int endLimit = getValidEndIndex();
        if (negative) {
            for (int i = from; i > endLimit; i--) {
                if (HuStrUtil.isSubEquals(text, i, strToFind, 0, subLen, caseInsensitive)) {
                    return i;
                }
            }
        } else {
            endLimit = endLimit - subLen + 1;
            for (int i = from; i < endLimit; i++) {
                if (HuStrUtil.isSubEquals(text, i, strToFind, 0, subLen, caseInsensitive)) {
                    return i;
                }
            }
        }

        return INDEX_NOT_FOUND;
    }

    @Override
    public int end(int start) {
        if (start < 0) {
            return -1;
        }
        return start + strToFind.length();
    }
}
