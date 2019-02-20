package com.sonsure.dumper.core.command.simple;


import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.sql.CommandConversionHandler;
import com.sonsure.dumper.core.management.CommandTable;

import java.util.Map;

/**
 * Created by liyd on 17/4/25.
 */
public class SimpleCommandContextBuilder extends AbstractSimpleCommandContextBuilder {

    public SimpleCommandContextBuilder(AbstractCommandExecutor commandExecutor, CommandConversionHandler commandConversionHandler) {
        super(commandExecutor, commandConversionHandler);
    }

    public CommandContext doBuild(CommandTable commandTable) {
        String command = (String) commandTable.getExtendData(CommandTable.ExtendDataKey.COMMAND.name());
        Map<String, Object> parameters = (Map<String, Object>) commandTable.getExtendData(CommandTable.ExtendDataKey.PARAMETERS.name());
        command = this.resolveCommand(command, commandTable, commandExecutor.getMappingHandler());
        CommandContext commandContext = new CommandContext();
        commandContext.setCommand(command);
        if (parameters != null) {
            commandContext.addParameters(parameters);
        }
        return commandContext;
    }
}
