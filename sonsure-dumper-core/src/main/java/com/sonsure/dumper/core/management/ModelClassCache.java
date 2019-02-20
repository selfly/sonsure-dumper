package com.sonsure.dumper.core.management;

import com.sonsure.commons.utils.ClassUtils;
import com.sonsure.dumper.core.annotation.Column;
import com.sonsure.dumper.core.annotation.Id;
import com.sonsure.dumper.core.annotation.Table;
import com.sonsure.dumper.core.annotation.Transient;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class ModelClassCache {

    private static final Map<Class<?>, ModelClassMeta> CACHE = new WeakHashMap<Class<?>, ModelClassMeta>();


    /**
     * 获取class的属性
     *
     * @return class fields
     */
    public static Set<ModelFieldMeta> getClassFields(Class<?> clazz) {
        ModelClassMeta modelClassMeta = CACHE.get(clazz);
        if (modelClassMeta == null) {
            modelClassMeta = initCache(clazz);
        }
        return modelClassMeta.getModelFieldMetas();
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
            modelFieldMeta.setIdAnnotation(field.getAnnotation(Id.class));
            modelFieldMeta.setColumnAnnotation(field.getAnnotation(Column.class));

            modelClassMeta.addModelFieldMeta(modelFieldMeta);
        }
        CACHE.put(clazz, modelClassMeta);

        return modelClassMeta;
    }


}
