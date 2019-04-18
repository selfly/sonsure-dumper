package com.sonsure.dumper.core.command.entity;


import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.ExecutorContext;
import com.sonsure.dumper.core.command.sql.CommandConversionHandler;

/**
 * Created by liyd on 17/4/14.
 */
public class DeleteCommandContextBuilderImpl extends AbstractCommandContextBuilder {

    private static final String COMMAND_OPEN = "delete from ";

    public DeleteCommandContextBuilderImpl(AbstractCommandExecutor commandExecutor, CommandConversionHandler commandConversionHandler) {
        super(commandExecutor, commandConversionHandler);
    }

    public CommandContext doBuild(ExecutorContext commandTable) {
        StringBuilder command = new StringBuilder(COMMAND_OPEN);
        command.append(this.getModelAliasName(commandTable.getModelClass(), commandTable.getTableAlias()));

        CommandContext whereCommandContext = this.buildWhereSql(commandTable);
        command.append(whereCommandContext.getCommand());
        CommandContext commandContext = getCommonCommandContext(commandTable);
        commandContext.setCommand(command.toString());
        commandContext.addParameters(whereCommandContext.getParameterMap());

        return commandContext;
    }
}
