package com.sonsure.dumper.core.config;

import com.sonsure.dumper.core.command.CommandExecutor;
import com.sonsure.dumper.core.exception.SonsureJdbcException;

import java.util.ArrayList;
import java.util.List;

public class CommandExecutorFactoryImpl implements CommandExecutorFactory {

    protected List<CommandExecutorBuilder> defaultCommandExecutorBuilders;

    protected List<CommandExecutorBuilder> commandExecutorBuilders;

    public CommandExecutorFactoryImpl() {
        defaultCommandExecutorBuilders = new ArrayList<>();
        defaultCommandExecutorBuilders.add(new CommandExecutorBuilderImpl());
    }

    @Override
    public CommandExecutor getCommandExecutor(Class<?> modelClass, Class<?> commandExecutorClass, JdbcEngineConfig jdbcEngineConfig) {
        CommandExecutorBuilder commandExecutorBuilder = this.getCommandExecutorBuilder(modelClass, commandExecutorClass, jdbcEngineConfig);
        CommandExecutor commandExecutor = commandExecutorBuilder.build(modelClass, commandExecutorClass, jdbcEngineConfig);
        return commandExecutor;
    }

    protected CommandExecutorBuilder getCommandExecutorBuilder(Class<?> modelClass, Class<?> commandExecutorClass, JdbcEngineConfig jdbcEngineConfig) {
        if (this.commandExecutorBuilders != null) {
            for (CommandExecutorBuilder ceb : this.commandExecutorBuilders) {
                if (ceb.support(modelClass, commandExecutorClass, jdbcEngineConfig)) {
                    return ceb;
                }
            }
        }
        for (CommandExecutorBuilder ceb : this.defaultCommandExecutorBuilders) {
            if (ceb.support(modelClass, commandExecutorClass, jdbcEngineConfig)) {
                return ceb;
            }
        }
        throw new SonsureJdbcException(String.format("没有找到对应的CommandExecutorBuilder,modelClass:%s,commandExecutorClass:%s", modelClass.getName(), commandExecutorClass.getName()));
    }

    public void setCommandExecutorBuilders(List<CommandExecutorBuilder> commandExecutorBuilders) {
        this.commandExecutorBuilders = commandExecutorBuilders;
    }
}
