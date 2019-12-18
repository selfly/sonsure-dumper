package com.sonsure.dumper.core.command.lambda;

/**
 * 使用 Consumer 会有Result of 'xx.getXxx()' is ignored警告
 *
 * @param <T>
 * @param <R>
 */
@FunctionalInterface
public interface Function<T, R> extends SuperFunction {
    R getDeclaredMethod(T t) throws Exception;
}
