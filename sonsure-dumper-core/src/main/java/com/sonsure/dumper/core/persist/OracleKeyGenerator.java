package com.sonsure.dumper.core.persist;


import com.sonsure.commons.utils.NameUtils;

/**
 * Created by liyd on 16/8/25.
 */
public class OracleKeyGenerator implements KeyGenerator {

    public boolean isParameter() {
        return false;
    }

    public Object generateKeyValue(Class<?> clazz) {
        //根据实体名获取主键序列名
        String tableName = NameUtils.getUnderlineName(clazz.getSimpleName());
        return String.format("`{{SEQ_%s.NEXTVAL}}`", tableName);
    }
}
