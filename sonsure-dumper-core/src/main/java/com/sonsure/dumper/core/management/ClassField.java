package com.sonsure.dumper.core.management;

/**
 * Created by liyd on 17/4/11.
 */
public class ClassField {

    /**
     * 名称
     */
    private String name;

    /**
     * 表别名
     */
    private String tableAlias;

    /**
     * 逻辑操作符 and or
     */
    private String logicalOperator;

    /**
     * 属性操作符 大于、小于、in等
     */
    private String fieldOperator;

    /**
     * 值
     */
    private Object value;

    /**
     * 来源
     */
    private Orig orig;

    public ClassField(String name) {
        this(name, null);
    }

    public ClassField(String name, String tableAlias) {
        this.name = name;
        this.tableAlias = tableAlias;
    }

    /**
     * 字段来源
     */
    public enum Orig {

        /**
         * 自动创建 例如使用主键生成器
         */
        GENERATOR,

        /**
         * 手动添加
         */
        MANUAL,

        /**
         * 来自实体类
         */
        ENTITY
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public String getFieldOperator() {
        return fieldOperator;
    }

    public void setFieldOperator(String fieldOperator) {
        this.fieldOperator = fieldOperator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Orig getOrig() {
        return orig;
    }

    public void setOrig(Orig orig) {
        this.orig = orig;
    }
}
