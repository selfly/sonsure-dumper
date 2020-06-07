/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command;

import org.apache.commons.lang3.ArrayUtils;

/**
 * The interface Executor context.
 *
 * @author liyd
 * @date 17 /4/11
 */
public abstract class AbstractCommonCommandExecutorContext {

    /**
     * The Native command.
     */
    private boolean nativeCommand;

    /**
     * The Named parameter.
     */
    private boolean namedParameter;

    /**
     * 获取实体类
     *
     * @return class [ ]
     */
    public Class<?>[] getModelClasses() {
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    }

    public boolean isNativeCommand() {
        return nativeCommand;
    }

    public void setNativeCommand(boolean nativeCommand) {
        this.nativeCommand = nativeCommand;
    }

    public boolean isNamedParameter() {
        return namedParameter;
    }

    public void setNamedParameter(boolean namedParameter) {
        this.namedParameter = namedParameter;
    }

}
