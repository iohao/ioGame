package com.iohao.game.common.kit.adapter;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
 abstract class HuComputeIter<T> implements Iterator<T> {

    private T next;
    private boolean finished;

    protected abstract T computeNext();

    @Override
    public boolean hasNext() {
        if (null != next) {
            // 用户读取了节点，但是没有使用
            return true;
        } else if (finished) {
            // 读取结束
            return false;
        }

        T result = computeNext();
        if (null == result) {
            // 不再有新的节点，结束
            this.finished = true;
            return false;
        } else {
            this.next = result;
            return true;
        }

    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more lines");
        }

        T result = this.next;
        // 清空cache，表示此节点读取完毕，下次计算新节点
        this.next = null;
        return result;
    }

}
