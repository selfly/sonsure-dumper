package com.sonsure.dumper.core.command;

public class GenerateKey {

    private Class<?> clazz;

    private String column;

    private Object value;

    /**
     * 主键值是否有数据库生成
     */
    private boolean isParameter;

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isParameter() {
        return isParameter;
    }

    public void setParameter(boolean parameter) {
        isParameter = parameter;
    }
}
