package com.sonsure.dumper.core.command.simple;


import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.ExecutorContext;
import com.sonsure.dumper.core.command.entity.AbstractCommandContextBuilder;
import com.sonsure.dumper.core.command.sql.CommandConversionHandler;

import java.util.Map;

/**
 * Created by liyd on 17/4/25.
 */
public class SimpleCommandContextBuilder extends AbstractCommandContextBuilder {

    public SimpleCommandContextBuilder(AbstractCommandExecutor commandExecutor, CommandConversionHandler commandConversionHandler) {
        super(commandExecutor, commandConversionHandler);
    }

    public CommandContext doBuild(ExecutorContext commandTable) {
        String command = (String) commandTable.getExtendData(ExecutorContext.ExtendDataKey.COMMAND.name());
        Map<String, Object> parameters = (Map<String, Object>) commandTable.getExtendData(ExecutorContext.ExtendDataKey.PARAMETERS.name());
        CommandContext commandContext = new CommandContext();
        commandContext.setCommand(command);
        if (parameters != null) {
            commandContext.addParameters(parameters);
        }
        return commandContext;
    }
}
