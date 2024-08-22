package com.iohao.game.common.kit.adapter;


/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
abstract class HuIoCopier<S, T> {

    protected final int bufferSize;
    protected final long count;

    protected boolean flushEveryBuffer;

    public HuIoCopier(int bufferSize, long count) {
        this.bufferSize = bufferSize > 0 ? bufferSize : HuIoUtil.DEFAULT_BUFFER_SIZE;
        this.count = count <= 0 ? Long.MAX_VALUE : count;
    }

    public abstract long copy(S source, T target);

    protected int bufferSize(long count) {
        return (int) Math.min(this.bufferSize, count);
    }

}
