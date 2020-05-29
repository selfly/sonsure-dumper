/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.entity;

import com.sonsure.dumper.core.command.ExecutorContext;

/**
 * The type Base entity executor context.
 *
 * @author liyd
 */
public abstract class AbstractEntityExecutorContext implements ExecutorContext {

    @Override
    public boolean isNativeCommand() {
        return false;
    }
}
