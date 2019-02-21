package com.sonsure.dumper.core.mapping;

import com.sonsure.commons.utils.NameUtils;
import com.sonsure.dumper.core.annotation.Column;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.management.ModelClassCache;
import com.sonsure.dumper.core.management.ModelClassMeta;
import com.sonsure.dumper.core.management.ModelFieldMeta;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 默认名称处理handler
 * <p>
 * User: liyd
 * Date: 2/12/14
 * Time: 4:51 PM
 */
public class DefaultMappingHandler extends AbstractMappingHandler {

    public DefaultMappingHandler() {
        this(null);
    }

    public DefaultMappingHandler(String modelPackages) {
        super(modelPackages);
    }


    public String getTable(Class<?> entityClass, Map<String, Object> params) {

        ModelClassMeta classMeta = ModelClassCache.getClassMeta(entityClass);
        Object annotation = classMeta.getAnnotation();
        String tableName = null;
        if (annotation != null) {
            tableName = ModelClassCache.getTableAnnotationName(annotation);
        } else {
            if (tablePreFixMap == null) {
                //默认Java属性的骆驼命名法转换回数据库下划线“_”分隔的格式
                tableName = NameUtils.getUnderlineName(entityClass.getSimpleName());
            } else {
                String tablePreFix = "";
                for (Map.Entry<String, String> entry : tablePreFixMap.entrySet()) {
                    if (StringUtils.startsWith(entityClass.getName(), entry.getKey())) {
                        tablePreFix = entry.getValue();
                        break;
                    }
                }
                tableName = tablePreFix + NameUtils.getUnderlineName(entityClass.getSimpleName());
            }
        }

        if (StringUtils.isBlank(tableName)) {
            throw new SonsureJdbcException("没有找到对应的表名:" + entityClass);
        }

        return tableName;
    }

    public String getPkField(Class<?> entityClass) {

        ModelClassMeta classMeta = ModelClassCache.getClassMeta(entityClass);
        ModelFieldMeta pkFieldMeta = classMeta.getPkFieldMeta();
        if (pkFieldMeta != null) {
            return pkFieldMeta.getName();
        }
        String firstLowerName = NameUtils.getFirstLowerName(entityClass.getSimpleName());
        //主键以类名加上“Id” 如user表主键属性即userId
        return firstLowerName + PRI_FIELD_SUFFIX;
    }

    public String getColumn(Class<?> entityClass, String fieldName) {
        ModelFieldMeta classFieldMeta = ModelClassCache.getClassFieldMeta(entityClass, fieldName);

        //count(*) as num  num是没有的
        if (classFieldMeta == null) {
            return fieldName;
        }

        Object columnAnnotation = classFieldMeta.getColumnAnnotation();
        if (columnAnnotation != null) {
            if (columnAnnotation instanceof Column) {
                return ((Column) columnAnnotation).value();
            }
        }
        return NameUtils.getUnderlineName(fieldName);
    }

    @Override
    public String getField(Class<?> clazz, String columnName) {
        ModelFieldMeta mappedFieldMeta = ModelClassCache.getMappedFieldMeta(clazz, columnName);
        if (mappedFieldMeta != null) {
            return mappedFieldMeta.getName();
        }
        return NameUtils.getCamelName(columnName);
    }
}
