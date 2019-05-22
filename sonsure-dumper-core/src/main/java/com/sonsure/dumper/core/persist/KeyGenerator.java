package com.sonsure.dumper.core.persist;

/**
 * Created by liyd on 17/4/12.
 */
public interface KeyGenerator {

    /**
     * 主键值是否传参
     * false:类似于oracle的序列,下面的generateKeyValue方法只是返回了序列名,真正的主键值是sql执行时获取的序列值
     * true:如UUID主键,下面的generateKeyValue方法返回一个UUID,这个UUID就已经是实际的主键值
     *
     * @return
     */
    boolean isParameter();

    /**
     * 生成主键值
     *
     * @param clazz the clazz
     * @return serializable serializable
     */
    Object generateKeyValue(Class<?> clazz);
}
