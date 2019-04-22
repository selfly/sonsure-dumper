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
    public CommandExecutor getCommandExecutor(Class<? extends CommandExecutor> commandExecutorClass, Object param, JdbcEngineConfig jdbcEngineConfig) {
        CommandExecutorBuilder commandExecutorBuilder = this.getCommandExecutorBuilder(commandExecutorClass, param, jdbcEngineConfig);
        CommandExecutor commandExecutor = commandExecutorBuilder.build(commandExecutorClass, param, jdbcEngineConfig);
        return commandExecutor;
    }

    protected CommandExecutorBuilder getCommandExecutorBuilder(Class<? extends CommandExecutor> commandExecutorClass, Object param, JdbcEngineConfig jdbcEngineConfig) {
        if (this.commandExecutorBuilders != null) {
            for (CommandExecutorBuilder ceb : this.commandExecutorBuilders) {
                if (ceb.support(commandExecutorClass, param, jdbcEngineConfig)) {
                    return ceb;
                }
            }
        }
        for (CommandExecutorBuilder ceb : this.defaultCommandExecutorBuilders) {
            if (ceb.support(commandExecutorClass, param, jdbcEngineConfig)) {
                return ceb;
            }
        }
        throw new SonsureJdbcException(String.format("没有找到对应的CommandExecutorBuilder,commandExecutorClass:%s", commandExecutorClass.getName()));
    }

    public void setCommandExecutorBuilders(List<CommandExecutorBuilder> commandExecutorBuilders) {
        this.commandExecutorBuilders = commandExecutorBuilders;
    }
}
