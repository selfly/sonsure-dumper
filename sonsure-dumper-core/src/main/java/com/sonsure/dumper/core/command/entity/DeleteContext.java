package com.sonsure.dumper.core.command.entity;

public class DeleteContext extends WhereContext {

    protected Class<?> modelClass;

    public Class<?> getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class<?> modelClass) {
        this.modelClass = modelClass;
    }

    @Override
    public Class<?>[] getModelClasses() {
        return new Class<?>[]{modelClass};
    }
}
