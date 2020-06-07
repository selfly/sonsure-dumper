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
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.management.ClassField;
import com.sonsure.dumper.core.management.CommandClass;
import com.sonsure.dumper.core.management.ModelFieldMeta;

import java.util.Collection;
import java.util.List;

/**
 * @author liyd
 * @date 17/4/12
 */
public class SelectCommandContextBuilderImpl extends AbstractCommandContextBuilder {

    private static final String COMMAND_OPEN = "select ";

    @Override
    public CommandContext doBuild(CommandExecutorContext executorContext, JdbcEngineConfig jdbcEngineConfig) {

        StringBuilder command = new StringBuilder(COMMAND_OPEN);

        final CommandExecutorContext.SelectContext selectContext = executorContext.selectContext();
        List<ClassField> selectFields = selectContext.getSelectFields();
        List<CommandClass> fromClasses = selectContext.getFromClasses();
        if (fromClasses.isEmpty()) {
            throw new SonsureJdbcException("from class必须指定");
        }
        //如果为空没有指定，获取class的属性
        if (selectFields.isEmpty()) {
            for (CommandClass fromClass : fromClasses) {
                Collection<ModelFieldMeta> classFields = this.getClassFields(fromClass.getCls());
                for (ModelFieldMeta fieldMeta : classFields) {
                    //黑名单
                    if (selectContext.isExcludeField(fieldMeta.getName())) {
                        continue;
                    }
                    String field = this.getTableAliasField(fromClass.getAliasName(), fieldMeta.getName());
                    command.append(field).append(",");
                }
            }
        } else {
            for (ClassField selectField : selectFields) {
                String field = this.getTableAliasField(selectField.getTableAlias(), selectField.getName());
                command.append(field).append(",");
            }
        }
        command.deleteCharAt(command.length() - 1);

        command.append(" from ");
        for (CommandClass fromClass : fromClasses) {
            command.append(this.getModelAliasName(fromClass.getCls(), fromClass.getAliasName()))
                    .append(",");
        }
        command.deleteCharAt(command.length() - 1);

        CommandContext commandContext = getCommonCommandContext(executorContext);

        CommandContext whereCommandContext = this.buildWhereSql(executorContext.entityWhereContext(), executorContext.isNamedParameter());
        if (whereCommandContext != null) {
            command.append(whereCommandContext.getCommand());
            commandContext.addCommandParameters(whereCommandContext.getCommandParameters());
        }

        String groupBySql = this.buildGroupBySql(selectContext);
        command.append(groupBySql);

        String orderBySql = this.buildOrderBySql(selectContext);
        command.append(orderBySql);

        commandContext.setCommand(command.toString());

        return commandContext;
    }
}
