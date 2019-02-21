//package com.sonsure.dumper.core.command.simple;
//
//import com.sonsure.dumper.core.command.AbstractCommandExecutor;
//import com.sonsure.dumper.core.command.entity.AbstractCommandContextBuilder;
//import com.sonsure.dumper.core.command.sql.CommandConversionHandler;
//import com.sonsure.dumper.core.management.CommandTable;
//import com.sonsure.dumper.core.mapping.MappingHandler;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.Map;
//import java.util.WeakHashMap;
//import java.util.concurrent.ConcurrentHashMap;
//
//public abstract class AbstractSimpleCommandContextBuilder extends AbstractCommandContextBuilder {
//
//    protected static final Map<String, String> COMMAND_CACHE = new WeakHashMap<>(new ConcurrentHashMap<String, String>());
//
//    public AbstractSimpleCommandContextBuilder(AbstractCommandExecutor commandExecutor, CommandConversionHandler commandConversionHandler) {
//        super(commandExecutor,commandConversionHandler);
//    }
//
//
//}
