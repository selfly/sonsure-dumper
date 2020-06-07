///*
// * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
// * You may obtain more information at
// *
// *   http://www.sonsure.com
// *
// * Designed By Selfly Lee (selfly@live.com)
// */
//
//package com.sonsure.dumper.core.command.entity;
//
//import com.sonsure.dumper.core.config.JdbcEngineConfig;
//import com.sonsure.dumper.core.management.ClassField;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author liyd
// */
//public class InsertContext extends AbstractEntityExecutorContext {
//
//    /**
//     * 对应实体类
//     */
//    protected Class<?> modelClass;
//
//    /**
//     * 操作的属性
//     */
//    protected List<ClassField> insertFields;
//
//    public InsertContext() {
//        insertFields = new ArrayList<>();
//    }
//
//    @Override
//    public Class<?>[] getModelClasses() {
//        return new Class<?>[]{modelClass};
//    }
//
//    public void addInsertField(String field, Object value) {
//        ClassField classField = new ClassField(field, false);
//        classField.setValue(value);
//        this.insertFields.add(classField);
//    }
//
//    public List<ClassField> getInsertFields() {
//        return insertFields;
//    }
//
//    public Class<?> getModelClass() {
//        return modelClass;
//    }
//
//    public void setModelClass(Class<?> modelClass) {
//        this.modelClass = modelClass;
//    }
//}
