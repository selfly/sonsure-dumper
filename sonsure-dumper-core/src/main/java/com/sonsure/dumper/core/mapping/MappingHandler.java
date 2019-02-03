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
     * @param entityClass the entity class
     * @param fieldMap    the field map
     * @return table name
     */
    String getTableName(Class<?> entityClass, Map<String, CommandField> fieldMap);

    /**
     * 根据类名获取主键字段名
     *
     * @param entityClass the entity class
     * @return pK name
     */
    String getPkFieldName(Class<?> entityClass);

    /**
     * 根据属性名获取列名
     *
     * @param entityClass the entity class
     * @param fieldName   the field name
     * @return column name
     */
    String getColumnName(Class<?> entityClass, String fieldName);
}
