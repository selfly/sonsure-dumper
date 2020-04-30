/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.test.executor;

import com.sonsure.dumper.core.command.ExecutorContext;

public class CountExecutorContext implements ExecutorContext {

    private Class<?> clazz;

    @Override
    public Class<?>[] getModelClasses() {
        return new Class<?>[]{clazz};
    }

    @Override
    public boolean isNativeCommand() {
        return false;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
