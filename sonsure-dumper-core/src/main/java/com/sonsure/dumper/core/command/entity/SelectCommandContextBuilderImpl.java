package com.sonsure.dumper.core.command.entity;


import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.sql.CommandConversionHandler;
import com.sonsure.dumper.core.management.CommandField;
import com.sonsure.dumper.core.management.CommandTable;
import com.sonsure.dumper.core.management.ModelFieldMeta;

import java.util.Collection;

/**
 * Created by liyd on 17/4/12.
 */
public class SelectCommandContextBuilderImpl extends AbstractCommandContextBuilder {

    private static final String COMMAND_OPEN = "select ";

    public SelectCommandContextBuilderImpl(AbstractCommandExecutor commandExecutor, CommandConversionHandler commandConversionHandler) {
        super(commandExecutor, commandConversionHandler);
    }

    public CommandContext doBuild(CommandTable commandTable) {

        StringBuilder command = new StringBuilder(COMMAND_OPEN);
        //处理操作属性,select中手动添加的都是native的
        for (CommandField commandField : commandTable.getOperationFields()) {

            //手动添加的部分，不处理黑白名单
            String field = this.getTableAliasField(commandTable.getTableAlias(), commandField.getName());
            command.append(field).append(",");
        }

        if (!commandTable.isNotSelectEntityField()) {

            Collection<ModelFieldMeta> classFields = this.getClassFields(commandTable.getModelClass());
            for (ModelFieldMeta fieldMeta : classFields) {
                //白名单 黑名单
                if (!commandTable.isIncludeField(fieldMeta.getName())) {
                    continue;
                } else if (commandTable.isExcludeField(fieldMeta.getName())) {
                    continue;
                }
                //手动添加的部分，不处理黑白名单
                String column = this.getTableAliasField(commandTable.getTableAlias(), fieldMeta.getName());
                command.append(column).append(",");
            }
        }

        command.deleteCharAt(command.length() - 1);
        command.append(" from ").append(this.getModelAliasName(commandTable.getModelClass(), commandTable.getTableAlias()));

        CommandContext whereCommandContext = this.buildWhereSql(commandTable);
        command.append(whereCommandContext.getCommand());

        String groupBySql = this.buildGroupBySql(commandTable);
        command.append(groupBySql);

        String orderBySql = this.buildOrderBySql(commandTable);
        command.append(orderBySql);

        CommandContext commandContext = getGenericCommandContext(commandTable);

        commandContext.setCommand(command.toString());
        commandContext.addParameters(whereCommandContext.getParameterMap());

        return commandContext;
    }
}
