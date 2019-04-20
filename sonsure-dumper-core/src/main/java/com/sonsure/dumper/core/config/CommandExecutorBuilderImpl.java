package com.sonsure.dumper.core.config;

import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContextBuilder;
import com.sonsure.dumper.core.command.CommandExecutor;
import com.sonsure.dumper.core.command.entity.*;
import com.sonsure.dumper.core.command.mybatis.MybatisCommandContextBuilder;
import com.sonsure.dumper.core.command.mybatis.MybatisExecutor;
import com.sonsure.dumper.core.command.mybatis.MybatisExecutorImpl;
import com.sonsure.dumper.core.command.natives.NativeExecutor;
import com.sonsure.dumper.core.command.natives.NativeExecutorImpl;
import com.sonsure.dumper.core.command.simple.SimpleCommandContextBuilder;

import java.util.Arrays;
import java.util.List;

public class CommandExecutorBuilderImpl extends AbstractCommandExecutorBuilder {

    protected List<Class<?>> commandExecutorClasses;

    public CommandExecutorBuilderImpl() {
        commandExecutorClasses = Arrays.asList(new Class<?>[]{Insert.class, Select.class, Update.class, Delete.class, NativeExecutor.class, MybatisExecutor.class});
    }

    @Override
    public boolean support(Class<? extends CommandExecutor> commandExecutorClass, JdbcEngineConfig jdbcEngineConfig) {
        return commandExecutorClasses.contains(commandExecutorClass);
    }

    @Override
    public CommandExecutor build(Class<? extends CommandExecutor> commandExecutorClass, JdbcEngineConfig jdbcEngineConfig) {

        AbstractCommandExecutor commandExecutor = null;
        CommandContextBuilder commandContextBuilder = null;

        if (commandExecutorClass == Insert.class) {
            commandExecutor = new InsertImpl(jdbcEngineConfig);
            commandContextBuilder = new InsertCommandContextBuilderImpl();
        } else if (commandExecutorClass == Select.class) {
            commandExecutor = new SelectImpl(jdbcEngineConfig);
            commandContextBuilder = new SelectCommandContextBuilderImpl();
        } else if (commandExecutorClass == Update.class) {
            commandExecutor = new UpdateImpl<>(jdbcEngineConfig);
            commandContextBuilder = new UpdateCommandContextBuilderImpl();
        } else if (commandExecutorClass == Delete.class) {
            commandExecutor = new DeleteImpl<>(jdbcEngineConfig);
            commandContextBuilder = new DeleteCommandContextBuilderImpl();
        } else if (commandExecutorClass == NativeExecutor.class) {
            commandExecutor = new NativeExecutorImpl(jdbcEngineConfig);
            commandContextBuilder = new SimpleCommandContextBuilder();
        } else if (commandExecutorClass == MybatisExecutor.class) {
            commandExecutor = new MybatisExecutorImpl(jdbcEngineConfig);
            commandContextBuilder = new MybatisCommandContextBuilder();
        }

        commandExecutor.setCommandContextBuilder(commandContextBuilder);
        return commandExecutor;
    }
}
