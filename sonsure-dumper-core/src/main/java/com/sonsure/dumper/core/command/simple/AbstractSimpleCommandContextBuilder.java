package com.sonsure.dumper.core.command.simple;

import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.entity.AbstractCommandContextBuilder;
import com.sonsure.dumper.core.command.sql.CommandConversionHandler;
import com.sonsure.dumper.core.management.CommandTable;
import com.sonsure.dumper.core.mapping.MappingHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractSimpleCommandContextBuilder extends AbstractCommandContextBuilder {

    protected static final Map<String, String> COMMAND_CACHE = new WeakHashMap<>(new ConcurrentHashMap<String, String>());

    /**
     * command解析器
     */
    protected CommandConversionHandler commandConversionHandler;

    public AbstractSimpleCommandContextBuilder(AbstractCommandExecutor commandExecutor, CommandConversionHandler commandConversionHandler) {
        super(commandExecutor);
        this.commandConversionHandler = commandConversionHandler;
    }

    protected String resolveCommand(String command, CommandTable commandTable, MappingHandler mappingHandler) {
        if (commandTable.isForceNative()) {
            return command;
        }
        String resolvedCommand = COMMAND_CACHE.get(command);
        if (StringUtils.isBlank(resolvedCommand)) {
            resolvedCommand = commandConversionHandler.convert(command);
            COMMAND_CACHE.put(command, resolvedCommand);
        }
        return resolvedCommand;
    }
}
