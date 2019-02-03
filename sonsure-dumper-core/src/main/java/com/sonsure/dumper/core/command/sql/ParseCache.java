package com.sonsure.dumper.core.command.sql;


import com.sonsure.commons.utils.ClassUtils;
import com.sonsure.dumper.core.management.CommandTable;
import com.sonsure.dumper.core.management.MappingCache;
import com.sonsure.dumper.core.mapping.MappingHandler;

import java.util.HashMap;
import java.util.Map;

public final class ParseCache {

    private static final Map<String, Class<?>> CLASS_MAP = new HashMap<>();

    public static Class<?> getClass(String className) {
        Class<?> clazz = CLASS_MAP.get(className);
        if (clazz == null) {
            clazz = ClassUtils.loadClass(className);
            CLASS_MAP.put(className, clazz);
        }
        return clazz;
    }

    /**
     * 根据类名获取表名
     *
     * @param referenceClass
     * @return
     */
    public static String getClassTableName(String referenceClass, MappingHandler mappingHandler) {

        Class<?> clazz = getClass(referenceClass);
        CommandTable commandTable = new CommandTable();
        commandTable.setModelClass(clazz);
        String tableName = MappingCache.getTableName(commandTable, mappingHandler);
        return tableName;
    }


}
