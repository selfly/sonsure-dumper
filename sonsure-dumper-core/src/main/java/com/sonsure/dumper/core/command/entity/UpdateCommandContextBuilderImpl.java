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
import com.sonsure.dumper.core.management.ClassField;
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
        final List<ClassField> setFields = updateContext.getSetFields();
        final boolean ignoreNull = updateContext.isIgnoreNull();
        for (ClassField classField : setFields) {
            //主键 不管怎么更新都不更新主键
            if (StringUtils.equals(pkField, classField.getName())) {
                continue;
            }
            //null值
            if (classField.getValue() == null && ignoreNull) {
                continue;
            }

            command.append(classField.getName()).append(" = ");
            if (classField.getValue() == null) {
                command.append("null");
            } else if (classField.isNative()) {
                command.append(classField.getValue());
            } else {
                final String placeholder = this.createParameterPlaceholder(classField.getName(), executorContext.isNamedParameter());
                command.append(placeholder);
                commandContext.addCommandParameter(classField.getName(), classField.getValue());
            }
            command.append(",");
        }
        command.deleteCharAt(command.length() - 1);

        CommandContext whereCommandContext = this.buildWhereSql(executorContext.entityWhereContext(), executorContext.isNamedParameter());
        if (whereCommandContext != null) {
            command.append(whereCommandContext.getCommand());
            commandContext.addCommandParameters(whereCommandContext.getCommandParameters());
        }

        commandContext.setCommand(command.toString());

        return commandContext;
    }
}
