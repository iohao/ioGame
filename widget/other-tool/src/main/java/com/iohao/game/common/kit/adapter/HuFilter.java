package com.iohao.game.common.kit.adapter;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
@FunctionalInterface
 interface HuFilter<T> {
    /**
     * 是否接受对象
     *
     * @param t 检查的对象
     * @return 是否接受对象
     */
    boolean accept(T t);
}
