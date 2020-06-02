/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.sql;


import java.util.Map;

/**
 * @author liyd
 */
public interface CommandConversionHandler {

    /**
     * command转换
     *
     * @param command the command
     * @param params  仅在分表时mappingHandler会用到
     * @return string string
     */
    String convert(String command, Map<String, Object> params);


}
