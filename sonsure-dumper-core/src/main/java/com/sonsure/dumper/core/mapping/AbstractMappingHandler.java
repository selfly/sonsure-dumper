package com.sonsure.dumper.core.mapping;

import com.sonsure.commons.spring.scan.ClassPathBeanScanner;
import com.sonsure.commons.utils.ClassUtils;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractMappingHandler implements MappingHandler {

    protected static final Logger LOG = LoggerFactory.getLogger(MappingHandler.class);

    /**
     * 主键属性后缀
     */
    protected static final String PRI_FIELD_SUFFIX = "Id";


    protected Map<String, String> tablePreFixMap;

    protected Map<String, Class<?>> loadedClass;

    /**
     * 扫描的包
     */
    protected String modelPackages;

    /**
     * 类名称映射
     */
    protected Map<String, Class<?>> classMapping;

    /**
     * 自定义类名称映射
     */
    protected Map<String, Class<?>> customClassMapping;

    public AbstractMappingHandler(String modelPackages) {
        loadedClass = new HashMap<>();
        classMapping = new HashMap<>();
        customClassMapping = new HashMap<>();
        this.modelPackages = modelPackages;
        this.init();
    }

    public void addClassMapping(Class<?> clazz) {
        String simpleName = clazz.getSimpleName();
        if (!classMapping.containsKey(simpleName)) {
            classMapping.put(clazz.getSimpleName(), clazz);
        }
    }

    @Override
    public String getTable(String className, Map<String, Object> params) {
        Class<?> tableClass = this.getTableClass(className);
        return this.getTable(tableClass, params);
    }

    @Override
    public String getColumn(String clazzName, String fieldName) {
        Class<?> tableClass = this.getTableClass(clazzName);
        return this.getColumn(tableClass, fieldName);
    }

    protected Class<?> getTableClass(String className) {

        if (StringUtils.isBlank(className)) {
            throw new SonsureJdbcException("className不能为空");
        }
        Class<?> clazz = null;
        if (StringUtils.indexOf(className, ".") != -1) {
            clazz = loadedClass.get(className);
            if (clazz == null) {
                clazz = ClassUtils.loadClass(className);
                loadedClass.put(className, clazz);
            }
        }
        if (clazz == null && !customClassMapping.isEmpty()) {
            clazz = customClassMapping.get(className);
        }
        if (clazz == null && !classMapping.isEmpty()) {
            clazz = classMapping.get(className);
        }
        if (clazz == null) {
            throw new SonsureJdbcException("没有找到对应的class:" + className);
        }

        return clazz;
    }

    /**
     * 初始化类，容忍多次初始化无不良后果，并不需要严格的线程安全，
     */
    protected void init() {

        if (StringUtils.isBlank(this.modelPackages)) {
            return;
        }
        String[] pks = StringUtils.split(modelPackages, ",");
        for (String pk : pks) {
            List<String> classes = ClassPathBeanScanner.scanClasses(pk);
            for (String clazz : classes) {

                int index = StringUtils.lastIndexOf(clazz, ".");
                String simpleName = StringUtils.substring(clazz, index + 1);

                if (classMapping.containsKey(simpleName)) {
                    LOG.warn("短类名相同，使用时请自定义短类名或使用完整类名:class1:{},class2:{}", classMapping.get(simpleName), clazz);
                } else {
                    Class<?> aClass = ClassUtils.loadClass(clazz);
                    classMapping.put(simpleName, aClass);
                }
            }
        }
    }

    public String getModelPackages() {
        return modelPackages;
    }

    public Map<String, Class<?>> getClassMapping() {
        return classMapping;
    }

    public Map<String, Class<?>> getCustomClassMapping() {
        return customClassMapping;
    }

    public void setCustomClassMapping(Map<String, Class<?>> customClassMapping) {
        this.customClassMapping = customClassMapping;
    }

    public Map<String, String> getTablePreFixMap() {
        return tablePreFixMap;
    }

    public void setTablePreFixMap(Map<String, String> tablePreFixMap) {
        this.tablePreFixMap = tablePreFixMap;
    }
}
