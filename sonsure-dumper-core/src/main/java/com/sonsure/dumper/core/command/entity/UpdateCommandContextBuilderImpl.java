package com.sonsure.dumper.core.command.entity;

import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.ExecutorContext;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/14.
 */
public class UpdateCommandContextBuilderImpl extends AbstractCommandContextBuilder {

    private static final String COMMAND_OPEN = "update ";

    public CommandContext doBuild(ExecutorContext executorContext, JdbcEngineConfig jdbcEngineConfig) {

        CommandContext commandContext = getCommonCommandContext(executorContext);

//        StringBuilder command = new StringBuilder(COMMAND_OPEN);
//        command.append(this.getModelAliasName(commandTable.getModelClass(), commandTable.getTableAlias())).append(" set ");
//
//        String pkField = this.getPkField(commandTable);
//        for (ClassField commandField : commandTable.getOperationFields()) {
//            //主键 不管怎么更新都不更新主键
//            if (StringUtils.equals(pkField, commandField.getName())) {
//                continue;
//            }
//            //null值
//            if (commandField.getValue() == null && commandTable.isIgnoreNull()) {
//                continue;
//            }
//
//            Object[] objects = this.decideNativeField(commandTable, commandField);
//
//            command.append(objects[3]).append(" = ");
//            if (commandField.getValue() == null) {
//                command.append("null");
//            } else if (BooleanUtils.toBoolean(objects[1].toString())) {
//                command.append(objects[4]);
//            } else {
//                command.append("?");
//                commandContext.addParameter(((String) objects[2]), commandField.getValue());
//            }
//            command.append(",");
//        }
//        command.deleteCharAt(command.length() - 1);
//
//        CommandContext whereCommandContext = this.buildWhereSql(commandTable);
//        command.append(whereCommandContext.getCommand());
//
//        commandContext.setCommand(command.toString());
//        commandContext.addParameters(whereCommandContext.getParameterMap());

        return commandContext;
    }
}
