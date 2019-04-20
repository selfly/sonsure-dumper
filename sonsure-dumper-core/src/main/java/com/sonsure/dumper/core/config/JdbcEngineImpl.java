package com.sonsure.dumper.core.config;


import com.sonsure.dumper.core.command.CommandExecutor;
import com.sonsure.dumper.core.persist.Jdbc;
import com.sonsure.dumper.core.persist.JdbcEngineFacade;

/**
 * Created by liyd on 17/4/12.
 */
public class JdbcEngineImpl implements JdbcEngine {

    private JdbcEngineConfig jdbcEngineConfig;

    public JdbcEngineImpl(JdbcEngineConfig jdbcEngineConfig) {
        this.jdbcEngineConfig = jdbcEngineConfig;
        JdbcEngineFacade jdbcEngineFacade = new JdbcEngineFacade(this);
        Jdbc.addJdbcFacade(jdbcEngineFacade);
    }

    @Override
    public String getName() {
        return jdbcEngineConfig.getName();
    }

    @Override
    public boolean isDefault() {
        return jdbcEngineConfig.isDefault();
    }

    public <T extends CommandExecutor> T createExecutor(Class<T> commandExecutorClass) {
        return (T) this.jdbcEngineConfig.getCommandExecutorFactory().getCommandExecutor(commandExecutorClass, this.jdbcEngineConfig);
    }

    @Override
    public JdbcEngineConfig getJdbcEngineConfig() {
        return jdbcEngineConfig;
    }
}
