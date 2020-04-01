package com.sonsure.dumper.core.command.simple;

/**
 * @author liyd
 */
public interface ResultHandler<T> {

    /**
     * 处理结果
     *
     * @param object
     * @return
     */
    T handle(Object object);
}
