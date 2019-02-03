package com.sonsure.dumper.core.mapping;

import com.sonsure.commons.utils.NameUtils;
import com.sonsure.dumper.core.management.CommandField;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 默认名称处理handler
 * <p>
 * User: liyd
 * Date: 2/12/14
 * Time: 4:51 PM
 */
public class DefaultMappingHandler implements MappingHandler {

    protected Map<String, String> tablePreFixMap;

    /**
     * 主键属性后缀
     */
    private static final String PRI_FIELD_SUFFIX = "Id";

    public String getTableName(Class<?> entityClass, Map<String, CommandField> fieldMap) {

        if (tablePreFixMap == null) {
            //默认Java属性的骆驼命名法转换回数据库下划线“_”分隔的格式
            return NameUtils.getUnderlineName(entityClass.getSimpleName());
        }

        String tablePreFix = "";
        for (Map.Entry<String, String> entry : tablePreFixMap.entrySet()) {
            if (StringUtils.startsWith(entityClass.getName(), entry.getKey())) {
                tablePreFix = entry.getValue();
                break;
            }
        }
        return tablePreFix + NameUtils.getUnderlineName(entityClass.getSimpleName());
    }

    public String getPkFieldName(Class<?> entityClass) {
        String firstLowerName = NameUtils.getFirstLowerName(entityClass.getSimpleName());
        //主键以类名加上“Id” 如user表主键属性即userId
        return firstLowerName + PRI_FIELD_SUFFIX;
    }

    public String getColumnName(Class<?> entityClass, String fieldName) {
        return NameUtils.getUnderlineName(fieldName);
    }

    public Map<String, String> getTablePreFixMap() {
        return tablePreFixMap;
    }

    public void setTablePreFixMap(Map<String, String> tablePreFixMap) {
        this.tablePreFixMap = tablePreFixMap;
    }
}
