/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.entity;


import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

/**
 * The type Delete.
 *
 * @author liyd
 * @date 17 /4/14
 */
public class DeleteImpl extends AbstractEntityConditionCommandExecutor<Delete> implements Delete {

    public DeleteImpl(JdbcEngineConfig jdbcEngineConfig) {
        super(jdbcEngineConfig);
    }

    @Override
    public Delete from(Class<?> cls) {
        getCommandExecutorContext().addModelClass(cls);
        return this;
    }

    @Override
    public int execute() {
        CommandContext commandContext = this.getCommandContextBuilder().build(getCommandExecutorContext(), getJdbcEngineConfig());
        return (Integer) getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.DELETE);
    }

}
