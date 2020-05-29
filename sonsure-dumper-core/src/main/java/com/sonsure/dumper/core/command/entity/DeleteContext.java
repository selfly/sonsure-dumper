/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.entity;

/**
 * @author liyd
 */
public class DeleteContext extends AbstractWhereContext {

    protected Class<?> modelClass;

    public Class<?> getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class<?> modelClass) {
        this.modelClass = modelClass;
    }

    @Override
    public Class<?>[] getModelClasses() {
        return new Class<?>[]{modelClass};
    }
}
