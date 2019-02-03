package com.sonsure.dumper.core.command.sql;


import com.sonsure.dumper.core.mapping.MappingHandler;

import java.util.Map;

public interface CommandToSqlTranslator {


    String getSql(String command, MappingHandler mappingHandler, Map<String, String> classMapping, Map<String, String> customClassMapping);

}
