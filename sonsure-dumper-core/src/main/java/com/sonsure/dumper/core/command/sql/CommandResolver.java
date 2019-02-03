package com.sonsure.dumper.core.command.sql;


import com.sonsure.dumper.core.management.CommandTable;
import com.sonsure.dumper.core.mapping.MappingHandler;

public interface CommandResolver {


    String resolve(String command, CommandTable commandTable, MappingHandler mappingHandler);


}
