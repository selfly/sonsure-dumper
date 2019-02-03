package com.sonsure.dumper.core.management;

import java.util.Map;

/**
 * Created by liyd on 17/4/11.
 */
public class TableMetaData {


    private Class<?> entityClass;


    private String tableName;


    private String pkField;


    private String pkColumn;


    private Map<String, String> classFields;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPkField() {
        return pkField;
    }

    public void setPkField(String pkField) {
        this.pkField = pkField;
    }

    public String getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(String pkColumn) {
        this.pkColumn = pkColumn;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public Map<String, String> getClassFields() {
        return classFields;
    }

    public void setClassFields(Map<String, String> classFields) {
        this.classFields = classFields;
    }
}
