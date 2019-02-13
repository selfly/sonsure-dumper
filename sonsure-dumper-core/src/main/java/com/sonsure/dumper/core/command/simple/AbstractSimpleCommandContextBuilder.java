package com.sonsure.dumper.core.command.simple;

import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.entity.AbstractCommandContextBuilder;
import com.sonsure.dumper.core.command.sql.CommandResolver;
import com.sonsure.dumper.core.management.CommandTable;
import com.sonsure.dumper.core.mapping.MappingHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractSimpleCommandContextBuilder extends AbstractCommandContextBuilder {

    protected static final Map<String, String> COMMAND_CACHE = new ConcurrentHashMap<>();

    /**
     * command解析器
     */
    protected CommandResolver commandResolver;

    public AbstractSimpleCommandContextBuilder(AbstractCommandExecutor commandExecutor, CommandResolver commandResolver) {
        super(commandExecutor);
        this.commandResolver = commandResolver;
    }

    protected String resolveCommand(String command, CommandTable commandTable, MappingHandler mappingHandler) {
        if (commandTable.isForceNative()) {
            return command;
        }
        COMMAND_CACHE.clear();
        String resolvedCommand = COMMAND_CACHE.get(command);
        if (StringUtils.isBlank(resolvedCommand)) {
            resolvedCommand = commandResolver.resolve(command, commandTable, mappingHandler);
            COMMAND_CACHE.put(command, resolvedCommand);
        }
        return resolvedCommand;
    }
}
