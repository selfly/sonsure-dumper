package com.sonsure.dumper.core.management;

import com.sonsure.commons.utils.ClassUtils;
import com.sonsure.dumper.core.annotation.Column;
import com.sonsure.dumper.core.annotation.Id;
import com.sonsure.dumper.core.annotation.Table;
import com.sonsure.dumper.core.annotation.Transient;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

public class ModelClassCache {

    private static final Map<Class<?>, ModelClassMeta> CACHE = new WeakHashMap<Class<?>, ModelClassMeta>();

    public static ModelClassMeta getClassMeta(Class<?> clazz) {
        ModelClassMeta modelClassMeta = CACHE.get(clazz);
        if (modelClassMeta == null) {
            modelClassMeta = initCache(clazz);
        }
        return modelClassMeta;
    }

    public static String getColumnAnnotationName(Object annotation) {
        if (annotation instanceof Column) {
            return ((Column) annotation).value();
        } else {
            return null;
        }
    }

    /**
     * 获取class的属性
     *
     * @return class fields
     */
    public static Collection<ModelFieldMeta> getClassFieldMetas(Class<?> clazz) {
        ModelClassMeta modelClassMeta = getClassMeta(clazz);
        return modelClassMeta.getModelFieldMetas();
    }

    public static ModelFieldMeta getClassFieldMeta(Class<?> clazz, String fieldName) {
        ModelClassMeta classMeta = getClassMeta(clazz);
        return classMeta.getModelFieldMeta(fieldName);
    }

    public static ModelFieldMeta getMappedFieldMeta(Class<?> clazz, String columnName) {
        ModelClassMeta classMeta = getClassMeta(clazz);
        return classMeta.getMappedFieldMeta(columnName);
    }


    /**
     * 初始化实体类缓存
     *
     * @param clazz
     */
    private static ModelClassMeta initCache(Class<?> clazz) {

        ModelClassMeta modelClassMeta = new ModelClassMeta();

        Table table = clazz.getAnnotation(Table.class);
        modelClassMeta.setAnnotation(table);

        Field[] beanFields = ClassUtils.getSelfFields(clazz);
        for (Field field : beanFields) {

            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (field.getAnnotation(Transient.class) != null) {
                continue;
            }

            ModelFieldMeta modelFieldMeta = new ModelFieldMeta();
            modelFieldMeta.setName(field.getName());
            modelFieldMeta.setIdAnnotation(field.getAnnotation(Id.class));
            modelFieldMeta.setColumnAnnotation(field.getAnnotation(Column.class));

            modelClassMeta.addModelFieldMeta(modelFieldMeta);
        }
        CACHE.put(clazz, modelClassMeta);

        return modelClassMeta;
    }


}
