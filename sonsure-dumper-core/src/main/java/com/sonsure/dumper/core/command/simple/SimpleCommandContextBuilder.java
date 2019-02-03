package com.sonsure.dumper.core.command.simple;


import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.sql.CommandResolver;
import com.sonsure.dumper.core.management.CommandTable;

import java.util.Arrays;

/**
 * Created by liyd on 17/4/25.
 */
public class SimpleCommandContextBuilder extends AbstractSimpleCommandContextBuilder {

    public SimpleCommandContextBuilder(AbstractCommandExecutor commandExecutor, CommandResolver commandResolver) {
        super(commandExecutor, commandResolver);
    }

    public CommandContext doBuild(CommandTable commandTable) {
        String command = (String) commandTable.getExtendData(CommandTable.ExtendDataKey.COMMAND.name());
        Object[] parameters = (Object[]) commandTable.getExtendData(CommandTable.ExtendDataKey.PARAMETERS.name());
        command = this.resolveCommand(command, commandTable, commandExecutor.getMappingHandler());
        CommandContext commandContext = new CommandContext();
        commandContext.setCommand(command);
        if (parameters != null) {
            commandContext.addParameters(Arrays.asList(parameters));
        }
        return commandContext;
    }
}
