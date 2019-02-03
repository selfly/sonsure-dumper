package com.sonsure.dumper.core.command.entity;

import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.management.CommandField;
import com.sonsure.dumper.core.management.CommandTable;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyd on 17/4/14.
 */
public class UpdateCommandContextBuilderImpl extends AbstractCommandContextBuilder {

    private static final String COMMAND_OPEN = "update ";

    public UpdateCommandContextBuilderImpl(AbstractCommandExecutor commandExecutor) {
        super(commandExecutor);
    }


    public CommandContext doBuild(CommandTable commandTable) {

        StringBuilder command = new StringBuilder(COMMAND_OPEN);
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        command.append(this.getTableAliasName(commandTable)).append(" set ");

        String pkField = this.getPkField(commandTable);
        for (CommandField commandField : commandTable.getOperationFields()) {
            //主键 不管怎么更新都不更新主键
            if (StringUtils.equals(pkField, commandField.getName())) {
                continue;
            }
            //null值
            if (commandField.getValue() == null && commandTable.isIgnoreNull()) {
                continue;
            }

            Object[] objects = this.decideNativeField(commandTable, commandField.getName(), commandField.getValue());

            command.append(objects[3]).append(" = ");
            if (commandField.getValue() == null) {
                command.append("null");
            } else if (BooleanUtils.toBoolean(objects[1].toString())) {
                command.append(objects[4]);
            } else {
                command.append("?");
                paramNames.add(objects[2].toString());
                paramValues.add(commandField.getValue());
            }
            command.append(",");
        }
        command.deleteCharAt(command.length() - 1);

        CommandContext whereCommandContext = this.buildWhereSql(commandTable);
        command.append(whereCommandContext.getResolvedCommand());

        paramNames.addAll(whereCommandContext.getParameterNames());
        paramValues.addAll(whereCommandContext.getParameters());

        CommandContext commandContext = getGenericCommandContext(commandTable);
        commandContext.setCommand(command.toString());
        commandContext.addParameterNames(paramNames);
        commandContext.addParameters(paramValues);

        return commandContext;
    }
}
