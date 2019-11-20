package com.sonsure.dumper.core.command.lambda;

@FunctionalInterface
public interface Consumer<T> extends SuperConsumer {
    void getDeclaredMethod(T t) throws Exception;
}
