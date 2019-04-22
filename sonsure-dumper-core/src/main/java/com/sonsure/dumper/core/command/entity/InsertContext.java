package com.sonsure.dumper.core.command.entity;

import com.sonsure.dumper.core.management.ClassField;

import java.util.ArrayList;
import java.util.List;

public class InsertContext extends EntityExecutorContext {

    /**
     * 对应实体类
     */
    protected Class<?> modelClass;

    /**
     * 操作的属性
     */
    protected List<ClassField> insertFields;

    public InsertContext() {
        insertFields = new ArrayList<>();
    }

    @Override
    public Class<?>[] getModelClasses() {
        return new Class<?>[]{modelClass};
    }

    public void addInsertField(String field, Object value) {
        ClassField classField = new ClassField(field);
        classField.setValue(value);
        this.insertFields.add(classField);
    }

    public List<ClassField> getInsertFields() {
        return insertFields;
    }

    public Class<?> getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class<?> modelClass) {
        this.modelClass = modelClass;
    }
}
