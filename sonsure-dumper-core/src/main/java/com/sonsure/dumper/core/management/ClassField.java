/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.management;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author liyd
 * @date 17/4/11
 */
public class ClassField {

    /**
     * value需要native内容前后包围符号
     */
    public static final String NATIVE_FIELD_OPEN_TOKEN = "{{";
    public static final String NATIVE_FIELD_CLOSE_TOKEN = "}}";

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

    private boolean isNative = false;

    /**
     * 类型
     */
    private Type type;

    public ClassField(String name, boolean analyseTableAlias) {
        this.name = name;
        if (StringUtils.startsWith(name, ClassField.NATIVE_FIELD_OPEN_TOKEN) && StringUtils.endsWith(name, ClassField.NATIVE_FIELD_CLOSE_TOKEN)) {
            isNative = true;
            this.name = StringUtils.substring(name, ClassField.NATIVE_FIELD_OPEN_TOKEN.length(), name.length() - ClassField.NATIVE_FIELD_CLOSE_TOKEN.length());
        }
        if (analyseTableAlias && StringUtils.indexOf(this.name, ".") != -1) {
            String[] fieldInfo = StringUtils.split(this.name, ".");
            this.tableAlias = fieldInfo[0];
            this.name = fieldInfo[1];
        }
    }

    /**
     * fieldOperator是否需要括号
     *
     * @return
     */
    public boolean isFieldOperatorNeedBracket() {
        return StringUtils.indexOf(StringUtils.upperCase(this.getFieldOperator()), "IN") != -1;
    }


    /**
     * 字段来源
     */
    public enum Type {

        /**
         * Where append type.
         */
        WHERE_APPEND;

        /**
         * field名字是否需要解析表别名
         *
         * @param type
         * @return
         */
        public static boolean isAnalyseTableAlias(Type type) {
            //目前只有一个，其实有点多余，后期会扩展
            return type != WHERE_APPEND;
        }
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

    public boolean isNative() {
        return isNative;
    }

    public void setNative(boolean aNative) {
        isNative = aNative;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
