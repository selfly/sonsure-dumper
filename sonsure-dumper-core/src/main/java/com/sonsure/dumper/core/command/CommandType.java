/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command;

public enum CommandType {

    INSERT,

    QUERY_FOR_LIST,

    QUERY_SINGLE_RESULT,

    QUERY_FOR_MAP,

    QUERY_FOR_MAP_LIST,

    QUERY_ONE_COL,

    QUERY_ONE_COL_LIST,

    UPDATE,

    DELETE,

    EXECUTE,

    EXECUTE_SCRIPT;
}
