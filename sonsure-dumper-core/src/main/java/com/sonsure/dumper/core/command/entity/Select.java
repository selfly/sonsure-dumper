package com.sonsure.dumper.core.command.entity;


import com.sonsure.dumper.core.command.QueryCommandExecutor;
import com.sonsure.dumper.core.command.lambda.Consumer;

/**
 * Created by liyd on 17/4/12.
 */
public interface Select extends QueryCommandExecutor<Select>, ConditionCommandExecutor<Select> {

    /**
     * from表
     *
     * @param cls
     * @return
     */
    Select from(Class<?> cls);

    /**
     * from表
     *
     * @param cls
     * @param alias
     * @param clsAndAlias
     * @return
     */
    Select from(Class<?> cls, String alias, Object... clsAndAlias);

    /**
     * 黑名单
     *
     * @param fields
     * @return
     */
    Select exclude(String... fields);

    /**
     * 添加 group by属性
     *
     * @param fields
     * @return
     */
    Select groupBy(String... fields);

    /**
     * 排序属性
     *
     * @param fields
     * @return
     */
    Select orderBy(String... fields);

    /**
     * 属性条件
     *
     * @param consumers
     * @return
     */
    <E> Select orderBy(Consumer<E>... consumers);

    /**
     * asc排序
     *
     * @return
     */
    Select asc();

    /**
     * desc 排序
     *
     * @return
     */
    Select desc();
}
