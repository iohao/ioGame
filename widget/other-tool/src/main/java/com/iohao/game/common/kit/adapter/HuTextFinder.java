package com.iohao.game.common.kit.adapter;


import java.io.Serializable;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
abstract class HuTextFinder implements HuFinder, Serializable {

    protected CharSequence text;
    protected int endIndex = -1;
    protected boolean negative;

    public HuTextFinder setText(CharSequence text) {
        this.text = HuAssert.notNull(text, "Text must be not null!");
        return this;
    }


    protected int getValidEndIndex() {
        if (negative && -1 == endIndex) {
            // 反向查找模式下，-1表示0前面的位置，即字符串反向末尾的位置
            return -1;
        }
        final int limit;
        if (endIndex < 0) {
            limit = endIndex + text.length() + 1;
        } else {
            limit = Math.min(endIndex, text.length());
        }
        return limit;
    }
}
