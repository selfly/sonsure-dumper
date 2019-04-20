package com.sonsure.dumper.core.command.entity;

import com.sonsure.dumper.core.command.ExecutorContext;
import com.sonsure.dumper.core.management.ClassField;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class WhereContext implements ExecutorContext {

    /**
     * where的属性/列
     */
    protected List<ClassField> whereFields;

    public WhereContext() {
        whereFields = new ArrayList<>();
    }

    public void addWhereField(String logicalOperator, String name, String fieldOperator, Object value) {
        ClassField classField = this.getClassField(name);
        classField.setLogicalOperator(logicalOperator);
        classField.setFieldOperator(fieldOperator);
        classField.setValue(value);
        this.whereFields.add(classField);
    }

    public List<ClassField> getWhereFields() {
        return whereFields;
    }

    protected ClassField getClassField(String field) {
        if (StringUtils.indexOf(field, ".") != -1) {
            String[] fieldInfo = StringUtils.split(field, ".");
            return new ClassField(fieldInfo[1], fieldInfo[0]);
        } else {
            return new ClassField(field);
        }
    }
}
