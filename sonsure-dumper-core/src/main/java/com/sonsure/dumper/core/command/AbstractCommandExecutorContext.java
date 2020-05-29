/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   https://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command;

import org.apache.commons.lang3.ArrayUtils;

/**
 * The type Abstract executor context.
 *
 * @author liyd
 */
public abstract class AbstractCommandExecutorContext extends AbstractPaginationExecutorContext {

    /**
     * The Command.
     */
    protected String command;

    @Override
    public Class<?>[] getModelClasses() {
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
