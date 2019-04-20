package com.sonsure.dumper.core.command.simple;


import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.ExecutorContext;
import com.sonsure.dumper.core.command.entity.AbstractCommandContextBuilder;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/25.
 */
public class SimpleCommandContextBuilder extends AbstractCommandContextBuilder {

    public CommandContext doBuild(ExecutorContext commandTable, JdbcEngineConfig jdbcEngineConfig) {
//        String command = (String) commandTable.getExtendData(ExecutorContext.ExtendDataKey.COMMAND.name());
//        Map<String, Object> parameters = (Map<String, Object>) commandTable.getExtendData(ExecutorContext.ExtendDataKey.PARAMETERS.name());
//        CommandContext commandContext = new CommandContext();
//        commandContext.setCommand(command);
//        if (parameters != null) {
//            commandContext.addParameters(parameters);
//        }
//        return commandContext;
        return null;
    }
}
