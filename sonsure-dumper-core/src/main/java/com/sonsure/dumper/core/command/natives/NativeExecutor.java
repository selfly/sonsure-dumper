/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.natives;


import com.sonsure.dumper.core.command.simple.SimpleCommandExecutor;

/**
 * The interface Native executor.
 *
 * @author liyd
 * @date 17 /4/25
 */
public interface NativeExecutor extends SimpleCommandExecutor<NativeExecutor> {

    /**
     * 参数
     *
     * @param parameters
     * @return
     */
    NativeExecutor parameters(Object... parameters);

}
