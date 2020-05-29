/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   https://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command;

/**
 * The type Abstract native decision executor context.
 *
 * @author liyd
 */
public abstract class AbstractNativeDecisionExecutorContext implements ExecutorContext {

    /**
     * The Native command.
     */
    protected boolean nativeCommand = false;

    @Override
    public boolean isNativeCommand() {
        return nativeCommand;
    }

    public void setNativeCommand(boolean nativeCommand) {
        this.nativeCommand = nativeCommand;
    }
}
