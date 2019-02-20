package com.sonsure.dumper.core.management;

import java.util.HashSet;
import java.util.Set;

public class ModelClassMeta {

    private Object annotation;

    private ModelFieldMeta pkFieldMeta;

    private Set<ModelFieldMeta> modelFieldMetas;

    public ModelClassMeta() {
        modelFieldMetas = new HashSet<>();
    }

    public void addModelFieldMeta(ModelFieldMeta modelFieldMeta) {
        this.modelFieldMetas.add(modelFieldMeta);
    }


    public ModelFieldMeta getPkFieldMeta() {
        return pkFieldMeta;
    }

    public void setPkFieldMeta(ModelFieldMeta pkFieldMeta) {
        this.pkFieldMeta = pkFieldMeta;
    }

    public Object getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Object annotation) {
        this.annotation = annotation;
    }

    public Set<ModelFieldMeta> getModelFieldMetas() {
        return modelFieldMetas;
    }

    public void setModelFieldMetas(Set<ModelFieldMeta> modelFieldMetas) {
        this.modelFieldMetas = modelFieldMetas;
    }
}
