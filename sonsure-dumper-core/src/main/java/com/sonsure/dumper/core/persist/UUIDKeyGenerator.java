package com.sonsure.dumper.core.persist;


import com.sonsure.commons.utils.UUIDUtils;

/**
 * Created by liyd on 16/8/25.
 */
public class UUIDKeyGenerator implements KeyGenerator {


    public boolean isParameter() {
        return true;
    }

    public Object generateKeyValue(Class<?> clazz) {
        return UUIDUtils.getUUID32();
    }
}
