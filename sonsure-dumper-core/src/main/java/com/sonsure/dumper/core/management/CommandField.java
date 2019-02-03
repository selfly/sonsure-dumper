package com.sonsure.dumper.core.management;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by liyd on 17/4/11.
 */
public class CommandField {

    /**
     * 名称
     */
    private String name;

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
     * 类型
     */
    private Type type;

    /**
     * 来源
     */
    private Orig orig;

    /**
     * fieldOperator是否需要括号
     *
     * @return
     */
    public boolean isFieldOperatorNeedBracket() {
        return StringUtils.indexOf(StringUtils.upperCase(fieldOperator), "IN") != -1;
    }

    public static CommandFieldBuilder builder() {
        return new CommandFieldBuilder();
    }


    public static class CommandFieldBuilder {
        private CommandField commandField;

        CommandFieldBuilder() {
            commandField = new CommandField();
        }

        public CommandFieldBuilder name(String name) {
            commandField.name = name;
            return this;
        }

        public CommandFieldBuilder logicalOperator(String logicalOperator) {
            commandField.logicalOperator = logicalOperator;
            return this;
        }

        public CommandFieldBuilder fieldOperator(String fieldOperator) {
            commandField.fieldOperator = fieldOperator;
            return this;
        }

        public CommandFieldBuilder value(Object value) {
            commandField.value = value;
            return this;
        }

        public CommandFieldBuilder type(Type type) {
            commandField.type = type;
            return this;
        }

        public CommandFieldBuilder orig(Orig orig) {
            commandField.orig = orig;
            return this;
        }

        public CommandField build() {
            return this.commandField;
        }

    }


    /**
     * 字段类型
     */
    public enum Type {

        INSERT,

        INSERT_PK_NATIVE,

        UPDATE,

        CUSTOM_SELECT_FIELD,

        GROUP_BY,

        ORDER_BY,

        WHERE_ONLY,

        WHERE_FIELD,

        WHERE_CONDITION,

        WHERE_AND_ONLY,

        WHERE_AND_FIELD,

        WHERE_OR_ONLY,

        WHERE_OR_FIELD,

        WHERE_BEGIN,

        WHERE_END

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Orig getOrig() {
        return orig;
    }

    public void setOrig(Orig orig) {
        this.orig = orig;
    }
}
