package com.sonsure.dumper.core.command.entity;

import com.sonsure.dumper.core.management.ClassField;

import java.util.ArrayList;
import java.util.List;

public class UpdateContext extends WhereContext {

    /**
     * 实体类型
     */
    protected Class<?> modelClass;

    /**
     * 是否忽略null值
     */
    private boolean isIgnoreNull = true;

    /**
     * set的属性
     */
    protected List<ClassField> setFields;

    public UpdateContext() {
        setFields = new ArrayList<>();
    }

    public void addSetField(String field, Object value) {
        ClassField classField = new ClassField(field, true);
        classField.setValue(value);
        this.setFields.add(classField);
    }

    @Override
    public Class<?>[] getModelClasses() {
        return new Class<?>[]{modelClass};
    }

    public Class<?> getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class<?> modelClass) {
        this.modelClass = modelClass;
    }

    public boolean isIgnoreNull() {
        return isIgnoreNull;
    }

    public void setIgnoreNull(boolean ignoreNull) {
        isIgnoreNull = ignoreNull;
    }

    public List<ClassField> getSetFields() {
        return setFields;
    }
}
