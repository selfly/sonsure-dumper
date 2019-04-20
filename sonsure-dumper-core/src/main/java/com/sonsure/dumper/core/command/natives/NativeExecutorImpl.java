package com.sonsure.dumper.core.command.natives;


import com.sonsure.dumper.core.command.simple.AbstractSimpleCommandExecutor;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/25.
 */
public class NativeExecutorImpl extends AbstractSimpleCommandExecutor<NativeExecutor> implements NativeExecutor {

    private static final String DEFAULT_PARAM_PREFIX = "_param";

    public NativeExecutorImpl(JdbcEngineConfig jdbcEngineConfig) {
        super(jdbcEngineConfig);
    }

    public NativeExecutor parameters(Object... values) {
        for (Object value : values) {
//            this.parameter(DEFAULT_PARAM_PREFIX + (paramCount++), value);
        }
        return this;
    }

}
