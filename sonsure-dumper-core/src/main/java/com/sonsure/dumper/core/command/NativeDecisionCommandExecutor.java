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
 * @author liyd
 */
public interface NativeDecisionCommandExecutor<T extends NativeDecisionCommandExecutor> {

    /**
     * 是否禁止转换，command不做任何加工
     *
     * @return t t
     */
    T nativeCommand();
}
