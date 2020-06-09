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
import com.sonsure.dumper.core.command.CommandExecutorContext;
import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.management.CommandField;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author liyd
 * @date 17/4/14
 */
public class UpdateCommandContextBuilderImpl extends AbstractCommandContextBuilder {

    private static final String COMMAND_OPEN = "update ";

    @Override
    public CommandContext doBuild(CommandExecutorContext executorContext, JdbcEngineConfig jdbcEngineConfig) {

        CommandContext commandContext = getCommonCommandContext(executorContext);

        StringBuilder command = new StringBuilder(COMMAND_OPEN);
        final Class<?> modelClass = executorContext.getUniqueModelClass();
        command.append(this.getModelAliasName(modelClass, null)).append(" set ");

        String pkField = this.getPkField(modelClass, jdbcEngineConfig.getMappingHandler());
        final CommandExecutorContext.UpdateContext updateContext = executorContext.updateContext();
        final List<CommandField> setFields = updateContext.getSetFields();
        final boolean ignoreNull = updateContext.isIgnoreNull();
        for (CommandField commandField : setFields) {
            //主键 不管怎么更新都不更新主键
            if (StringUtils.equals(pkField, commandField.getFieldName())) {
                continue;
            }
            //null值
            if (commandField.getValue() == null && ignoreNull) {
                continue;
            }
            final String filedCommandName = this.getFiledCommandName(commandField, executorContext);
            command.append(filedCommandName).append(" = ");
            if (commandField.getValue() == null) {
                command.append("null");
            } else if (commandField.isNative()) {
                command.append(commandField.getValue());
            } else {
                final String placeholder = this.createParameterPlaceholder(commandField.getFieldName(), executorContext.isNamedParameter());
                command.append(placeholder);
                commandContext.addCommandParameter(commandField.getFieldName(), commandField.getValue());
            }
            command.append(",");
        }
        command.deleteCharAt(command.length() - 1);

        CommandContext whereCommandContext = this.buildWhereSql(executorContext);
        if (whereCommandContext != null) {
            command.append(whereCommandContext.getCommand());
            commandContext.addCommandParameters(whereCommandContext.getCommandParameters());
        }

        commandContext.setCommand(command.toString());

        return commandContext;
    }
}
