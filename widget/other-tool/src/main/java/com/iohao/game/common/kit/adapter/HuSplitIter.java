package com.iohao.game.common.kit.adapter;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuSplitIter extends HuComputeIter<String> implements Serializable {
    private final String text;
    private final HuTextFinder finder;
    private final int limit;
    private final boolean ignoreEmpty;
    private int offset;
    private int count;

    public HuSplitIter(CharSequence text, HuTextFinder separatorFinder, int limit, boolean ignoreEmpty) {
        HuAssert.notNull(text, "Text must be not null!");
        this.text = text.toString();
        this.finder = separatorFinder.setText(text);
        this.limit = limit > 0 ? limit : Integer.MAX_VALUE;
        this.ignoreEmpty = ignoreEmpty;
    }

    @Override
    protected String computeNext() {
        // 达到数量上限或末尾，结束
        if (count >= limit || offset > text.length()) {
            return null;
        }

        // 达到数量上限
        if (count == (limit - 1)) {
            // 当到达限制次数时，最后一个元素为剩余部分
            if (ignoreEmpty && offset == text.length()) {
                // 最后一个是空串
                return null;
            }

            // 结尾整个作为一个元素
            count++;
            return text.substring(offset);
        }

        final int start = finder.start(offset);
        // 无分隔符，结束
        if (start < 0) {
            // 如果不再有分隔符，但是遗留了字符，则单独作为一个段
            if (offset <= text.length()) {
                final String result = text.substring(offset);
                if (!ignoreEmpty || !result.isEmpty()) {
                    // 返回非空串
                    offset = Integer.MAX_VALUE;
                    return result;
                }
            }
            return null;
        }

        // 找到新的分隔符位置
        final String result = text.substring(offset, start);
        offset = finder.end(start);

        if (ignoreEmpty && result.isEmpty()) {
            // 发现空串且需要忽略时，跳过之
            return computeNext();
        }

        count++;
        return result;
    }

    /**
     * 重置
     */
    public void reset() {
        this.finder.reset();
        this.offset = 0;
        this.count = 0;
    }

    public List<String> toList(boolean trim) {
        return toList((str) -> trim ? HuStrUtil.trim(str) : str);
    }

    public <T> List<T> toList(Function<String, T> mapping) {
        final List<T> result = new ArrayList<>();
        while (this.hasNext()) {
            final T apply = mapping.apply(this.next());
            if (ignoreEmpty && HuStrUtil.isEmptyIfStr(apply)) {
                // 对于mapping之后依旧是String的情况，ignoreEmpty依旧有效
                continue;
            }
            result.add(apply);
        }
        if (result.isEmpty()) {
            return new ArrayList<>(0);
        }
        return result;
    }
}
