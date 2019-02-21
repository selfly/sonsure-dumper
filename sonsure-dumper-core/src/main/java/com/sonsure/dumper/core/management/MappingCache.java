//package com.sonsure.dumper.core.management;
//
//import com.sonsure.commons.utils.ClassUtils;
//import com.sonsure.dumper.core.annotation.Column;
//import com.sonsure.dumper.core.annotation.Id;
//import com.sonsure.dumper.core.annotation.Table;
//import com.sonsure.dumper.core.annotation.Transient;
//import com.sonsure.dumper.core.exception.SonsureJdbcException;
//import com.sonsure.dumper.core.mapping.MappingHandler;
//import org.apache.commons.lang3.StringUtils;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.WeakHashMap;
//
///**
// * Created by liyd on 17/4/11.
// */
//public class MappingCache {
//
//    private static final Map<Class<?>, TableMetaData> CACHE = new WeakHashMap<Class<?>, TableMetaData>();
//
//
//    /**
//     * 初始化实体类缓存
//     *
//     * @param commandTable
//     */
//    private static TableMetaData initCache(CommandTable commandTable, MappingHandler mappingHandler) {
//
//        TableMetaData tableMetaData = new TableMetaData();
//        Class<?> modelClass = commandTable.getModelClass();
//        tableMetaData.setEntityClass(modelClass);
//
//        Table aTable = modelClass.getAnnotation(Table.class);
//        String pkField = null;
//        if (aTable != null) {
//            tableMetaData.setTableName(aTable.value());
//            pkField = aTable.id();
//        } else {
//            //如果有分表数据，不能使用缓存
//            tableMetaData.setTableName(mappingHandler.getTable(modelClass, null));
//        }
//
//        Map<String, String> classFields = new HashMap<String, String>();
//        Field[] beanFields = ClassUtils.getSelfFields(modelClass);
//        for (Field field : beanFields) {
//
//            if (Modifier.isStatic(field.getModifiers())) {
//                continue;
//            }
//
//            if (field.getAnnotation(Transient.class) != null) {
//                continue;
//            }
//
//            if (field.getAnnotation(Id.class) != null) {
//                pkField = field.getName();
//            }
//
//            Column column = field.getAnnotation(Column.class);
//            if (column == null) {
//                classFields.put(field.getName(), mappingHandler.getColumn(modelClass, field.getName()));
//            } else {
//                classFields.put(field.getName(), column.value());
//            }
//        }
//
//        if (StringUtils.isBlank(pkField)) {
//            pkField = mappingHandler.getPkField(modelClass);
//        }
//        tableMetaData.setPkField(pkField);
//
//        tableMetaData.setPkColumn(classFields.get(pkField));
//        tableMetaData.setClassFields(classFields);
//        CACHE.put(modelClass, tableMetaData);
//
//        return tableMetaData;
//    }
//
//    public static TableMetaData getTableMetaData(CommandTable commandTable, MappingHandler mappingHandler) {
//        if (commandTable.getModelClass() == null) {
//            throw new SonsureJdbcException("CommandTable中的ModelClass不能为空");
//        }
//        TableMetaData tableMetaData = CACHE.get(commandTable.getModelClass());
//        if (tableMetaData == null) {
//            tableMetaData = initCache(commandTable, mappingHandler);
//        }
//        return tableMetaData;
//    }
//
//    /**
//     * 获取表名
//     *
//     * @param commandTable   the command table
//     * @param mappingHandler the mapping handler
//     * @return table name
//     */
//    public static String getTableName(CommandTable commandTable, MappingHandler mappingHandler) {
//        return getTableMetaData(commandTable, mappingHandler).getTableName();
//    }
//
//    /**
//     * 获取主键属性
//     *
//     * @param commandTable   the command table
//     * @param mappingHandler the mapping handler
//     * @return pk field
//     */
//    public static String getPkField(CommandTable commandTable, MappingHandler mappingHandler) {
//        return getTableMetaData(commandTable, mappingHandler).getPkField();
//    }
//
//    /**
//     * 获取主键列
//     *
//     * @param commandTable   the command table
//     * @param mappingHandler the mapping handler
//     * @return pk column
//     */
//    public static String getPkColumn(CommandTable commandTable, MappingHandler mappingHandler) {
//        return getTableMetaData(commandTable, mappingHandler).getPkColumn();
//    }
//
//    /**
//     * 根据属性获取列
//     *
//     * @param commandTable   the command table
//     * @param field          the field
//     * @param mappingHandler the mapping handler
//     * @return column
//     */
//    public static String getColumn(CommandTable commandTable, String field, MappingHandler mappingHandler) {
//        String column = getTableMetaData(commandTable, mappingHandler).getClassFieldMetas().get(field);
//        return column != null ? column : field;
//    }
//
//    /**
//     * 获取class的属性
//     *
//     * @param commandTable   the command table
//     * @param mappingHandler the mapping handler
//     * @return class fields
//     */
//    public static Set<String> getClassFieldMetas(CommandTable commandTable, MappingHandler mappingHandler) {
//        return getTableMetaData(commandTable, mappingHandler).getClassFieldMetas().keySet();
//    }
//
//
//}
