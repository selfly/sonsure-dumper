package com.sonsure.dumper.core.management;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ModelClassMeta {

    private Object annotation;

    private ModelFieldMeta pkFieldMeta;

    private Map<String, ModelFieldMeta> modelFieldMetas;

    public ModelClassMeta() {
        modelFieldMetas = new HashMap<>();
    }

    public void addModelFieldMeta(ModelFieldMeta modelFieldMeta) {
        this.modelFieldMetas.put(modelFieldMeta.getName(), modelFieldMeta);
        if (modelFieldMeta.getIdAnnotation() != null) {
            this.pkFieldMeta = modelFieldMeta;
        }
    }


    public ModelFieldMeta getModelFieldMeta(String fileName) {
        return modelFieldMetas.get(fileName);
    }

    public Collection<ModelFieldMeta> getModelFieldMetas() {
        return modelFieldMetas.values();
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


}
