package com.sonsure.dumper.core.command.sql;


import java.util.Map;

public interface CommandConversionHandler {

    /**
     * command转换
     *
     * @param command
     * @param params  仅在分表时mappingHandler会用到
     * @return
     */
    String convert(String command, Map<String, Object> params);


}
