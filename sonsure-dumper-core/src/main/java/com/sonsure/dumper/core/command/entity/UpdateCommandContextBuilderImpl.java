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
import com.sonsure.dumper.core.command.ExecutorContext;
import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.management.ClassField;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author liyd
 * @date 17/4/14
 */
public class UpdateCommandContextBuilderImpl extends AbstractCommandContextBuilder {

    private static final String COMMAND_OPEN = "update ";

    @Override
    public CommandContext doBuild(ExecutorContext executorContext, JdbcEngineConfig jdbcEngineConfig) {

        UpdateContext updateContext = (UpdateContext) executorContext;
        CommandContext commandContext = getCommonCommandContext(updateContext);

        StringBuilder command = new StringBuilder(COMMAND_OPEN);
        command.append(this.getModelAliasName(updateContext.getModelClass(), null)).append(" set ");

        String pkField = this.getPkField(updateContext.getModelClass(), jdbcEngineConfig.getMappingHandler());
        for (ClassField classField : updateContext.getSetFields()) {
            //主键 不管怎么更新都不更新主键
            if (StringUtils.equals(pkField, classField.getName())) {
                continue;
            }
            //null值
            if (classField.getValue() == null && updateContext.isIgnoreNull()) {
                continue;
            }

            command.append(classField.getName()).append(" = ");
            if (classField.getValue() == null) {
                command.append("null");
            } else if (classField.isNative()) {
                command.append(classField.getValue());
            } else {
                command.append("?");
                commandContext.addParameter(classField.getValue());
            }
            command.append(",");
        }
        command.deleteCharAt(command.length() - 1);

        CommandContext whereCommandContext = this.buildWhereSql(updateContext);
        if (whereCommandContext != null) {
            command.append(whereCommandContext.getCommand());
            commandContext.addParameters(whereCommandContext.getParameters());
        }

        commandContext.setCommand(command.toString());

        return commandContext;
    }
}
