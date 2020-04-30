/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.management;

public class ModelFieldMeta {

    private String name;

    private Object idAnnotation;

    private Object columnAnnotation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getIdAnnotation() {
        return idAnnotation;
    }

    public void setIdAnnotation(Object idAnnotation) {
        this.idAnnotation = idAnnotation;
    }

    public Object getColumnAnnotation() {
        return columnAnnotation;
    }

    public void setColumnAnnotation(Object columnAnnotation) {
        this.columnAnnotation = columnAnnotation;
    }
}
