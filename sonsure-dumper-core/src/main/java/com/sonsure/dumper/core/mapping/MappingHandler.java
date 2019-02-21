package com.sonsure.dumper.core.mapping;


import com.sonsure.dumper.core.management.CommandField;

import java.util.Map;

/**
 * 实体映射处理
 * <p>
 * Created by liyd on 17/4/11.
 */
public interface MappingHandler {

    /**
     * 根据实体名获取表名
     *
     * @param className the class name
     * @param fieldMap  the field map
     * @return table name
     */
    String getTable(String className, Map<String, CommandField> fieldMap);

    /**
     * 根据实体名获取表名
     *
     * @param clazz    the clazz
     * @param fieldMap the field map
     * @return table name
     */
    String getTable(Class<?> clazz, Map<String, CommandField> fieldMap);

    /**
     * 根据类名获取主键字段名
     *
     * @param clazz the clazz
     * @return pK name
     */
    String getPkField(Class<?> clazz);

    /**
     * 根据属性名获取列名
     *
     * @param clazzName the clazz name
     * @param fieldName the field name
     * @return column name
     */
    String getColumn(String clazzName, String fieldName);

    /**
     * 根据属性名获取列名
     *
     * @param clazz     the clazz
     * @param fieldName the field name
     * @return column name
     */
    String getColumn(Class<?> clazz, String fieldName);

    /**
     * 根据列获取属性
     *
     * @param clazz
     * @param columnName
     * @return
     */
    String getField(Class<?> clazz, String columnName);
}
