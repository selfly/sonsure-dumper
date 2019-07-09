package com.sonsure.dumper.core.persist;


import com.sonsure.commons.utils.UUIDUtils;

/**
 * Created by liyd on 16/8/25.
 */
public class UUIDKeyGenerator implements KeyGenerator {

    @Override
    public Object generateKeyValue(Class<?> clazz) {
        return UUIDUtils.getUUID32();
    }
}
