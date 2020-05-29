/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command;

/**
 * The interface Executor context.
 *
 * @author liyd
 * @date 17 /4/11
 */
public interface ExecutorContext {

    /**
     * 获取实体类
     *
     * @return class [ ]
     */
    Class<?>[] getModelClasses();

    /**
     * Is native command boolean.
     *
     * @return the boolean
     */
    boolean isNativeCommand();

}
