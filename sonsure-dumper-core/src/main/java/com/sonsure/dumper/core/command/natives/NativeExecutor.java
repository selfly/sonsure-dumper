package com.sonsure.dumper.core.command.natives;


import com.sonsure.dumper.core.command.simple.SimpleCommandExecutor;

/**
 * Created by liyd on 17/4/25.
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
