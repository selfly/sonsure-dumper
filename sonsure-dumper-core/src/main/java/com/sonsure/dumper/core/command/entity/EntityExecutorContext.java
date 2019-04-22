package com.sonsure.dumper.core.command.entity;

import com.sonsure.dumper.core.command.ExecutorContext;

public abstract class EntityExecutorContext implements ExecutorContext {

    @Override
    public boolean isNativeSql() {
        return false;
    }
}
