/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.natives;

import com.sonsure.dumper.core.command.simple.SimpleExecutorContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liyd
 */
public class NativeExecutorContext extends SimpleExecutorContext {

    protected List<Object> parameters;

    public NativeExecutorContext() {
        parameters = new ArrayList<>();
    }

    public void addParameter(Object value) {
        this.parameters.add(value);
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }
}
