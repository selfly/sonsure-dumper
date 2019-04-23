//package com.sonsure.dumper.core.management;
//
//import org.apache.commons.lang3.StringUtils;
//
//public class ClassFieldWrapper {
//
//    /**
//     * value需要native内容前后包围符号
//     */
//    protected static final String NATIVE_FIELD_OPEN_TOKEN = "{{";
//    protected static final String NATIVE_FIELD_CLOSE_TOKEN = "}}";
//
//    private ClassField classField;
//
//    /**
//     * 是否原生属性
//     */
//    private boolean isNative;
//
//    /**
//     * field名
//     */
//    private String fieldName;
//
//    public ClassFieldWrapper(ClassField classField) {
//        this.classField = classField;
//        this.isNative = false;
//        this.fieldName = classField.getName();
//        if (StringUtils.startsWith(fieldName, NATIVE_FIELD_OPEN_TOKEN) && StringUtils.endsWith(fieldName, NATIVE_FIELD_CLOSE_TOKEN)) {
//            this.isNative = true;
//            this.fieldName = StringUtils.substring(fieldName, NATIVE_FIELD_OPEN_TOKEN.length(), fieldName.length() - NATIVE_FIELD_CLOSE_TOKEN.length());
//        }
//    }
//
//    /**
//     * fieldOperator是否需要括号
//     *
//     * @return
//     */
//    public boolean isFieldOperatorNeedBracket() {
//        return StringUtils.indexOf(StringUtils.upperCase(this.getFieldOperator()), "IN") != -1;
//    }
//
//    public String getLogicalOperator() {
//        return this.classField.getLogicalOperator();
//    }
//
//    public String getFieldOperator() {
//        return this.classField.getFieldOperator();
//    }
//
//    public String getTableAlias() {
//        return this.classField.getTableAlias();
//    }
//
//    public Object getValue() {
//        return this.classField.getValue();
//    }
//
//    public boolean isNative() {
//        return isNative;
//    }
//
//    public String getFieldName() {
//        return fieldName;
//    }
//}
