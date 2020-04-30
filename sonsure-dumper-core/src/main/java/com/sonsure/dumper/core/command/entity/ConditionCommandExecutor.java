/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.entity;

import com.sonsure.dumper.core.command.lambda.Function;

/**
 * 条件构建
 * 实现标识接口
 * <p>
 * Created by liyd on 17/4/11.
 */
public interface ConditionCommandExecutor<C extends ConditionCommandExecutor<C>> extends EntityCommandExecutor {

    /**
     * where 关键字
     *
     * @return
     */
    C where();

    /**
     * where 属性条件
     *
     * @param field
     * @param value
     * @return
     */
    C where(String field, Object value);

    /**
     * where 属性条件
     *
     * @param function
     * @param value
     * @return
     */
    <E, R> C where(Function<E, R> function, Object value);

    /**
     * where 属性条件
     *
     * @param field
     * @param value
     * @return
     */
    C where(String field, Object[] value);

    /**
     * where 属性条件
     *
     * @param function
     * @param value
     * @return
     */
    <E, R> C where(Function<E, R> function, Object[] value);

    /**
     * where 属性条件，指定操作符
     *
     * @param field    the field
     * @param operator the operator
     * @param values   the values
     * @return c
     */
    C where(String field, String operator, Object... values);

    /**
     * where 属性条件，指定操作符
     *
     * @param function the field
     * @param operator the operator
     * @param values   the values
     * @return c
     */
    <E, R> C where(Function<E, R> function, String operator, Object... values);

    /**
     * 属性条件
     *
     * @param field
     * @param value
     * @return
     */
    C condition(String field, Object value);

    /**
     * 属性条件
     *
     * @param function
     * @param value
     * @return
     */
    <E, R> C condition(Function<E, R> function, Object value);

    /**
     * 属性条件
     *
     * @param field
     * @param value
     * @return
     */
    C condition(String field, Object[] value);

    /**
     * 属性条件
     *
     * @param function
     * @param value
     * @return
     */
    <E, R> C condition(Function<E, R> function, Object[] value);

    /**
     * 属性条件，指定操作符
     *
     * @param field    the field
     * @param operator the operator
     * @param values   the values
     * @return c
     */
    C condition(String field, String operator, Object... values);

    /**
     * 属性条件，指定操作符
     *
     * @param function the field
     * @param operator the operator
     * @param values   the values
     * @return c
     */
    <E, R> C condition(Function<E, R> function, String operator, Object... values);

    /**
     * 实体属性条件
     *
     * @param entity
     * @return
     */
    C conditionEntity(Object entity);

    /**
     * and 属性where条件
     *
     * @param entity
     * @return
     */
    C andConditionEntity(Object entity);

    /**
     * 拼装entity属性条件
     *
     * @param entity
     * @param wholeLogicalOperator 全局操作符
     * @param fieldLogicalOperator 属性操作符
     * @return
     */
    C conditionEntity(Object entity, String wholeLogicalOperator, String fieldLogicalOperator);

    /**
     * and
     *
     * @return
     */
    C and();

    /**
     * and 属性条件
     *
     * @param field
     * @param value
     * @return
     */
    C and(String field, Object value);

    /**
     * and 属性条件
     *
     * @param function
     * @param value
     * @return
     */
    <E, R> C and(Function<E, R> function, Object value);

    /**
     * and 属性条件
     *
     * @param field
     * @param value
     * @return
     */
    C and(String field, Object[] value);

    /**
     * and 属性条件
     *
     * @param function
     * @param value
     * @return
     */
    <E, R> C and(Function<E, R> function, Object[] value);

    /**
     * and 属性条件 指定操作符
     *
     * @param field    the field
     * @param operator the operator
     * @param values   the values
     * @return c
     */
    C and(String field, String operator, Object... values);

    /**
     * and 属性条件 指定操作符
     *
     * @param function the field
     * @param operator the operator
     * @param values   the values
     * @return c
     */
    <E, R> C and(Function<E, R> function, String operator, Object... values);

    /**
     * or
     *
     * @return
     */
    C or();

    /**
     * or 属性条件
     *
     * @param field
     * @param value
     * @return
     */
    C or(String field, Object value);

    /**
     * or 属性条件
     *
     * @param function
     * @param value
     * @return
     */
    <E, R> C or(Function<E, R> function, Object value);

    /**
     * or 属性条件
     *
     * @param field
     * @param value
     * @return
     */
    C or(String field, Object[] value);

    /**
     * or 属性条件
     *
     * @param function
     * @param value
     * @return
     */
    <E, R> C or(Function<E, R> function, Object[] value);

    /**
     * or 属性条件 指定操作符
     *
     * @param field    the field
     * @param operator the operator
     * @param values   the values
     * @return c
     */
    C or(String field, String operator, Object... values);

    /**
     * or 属性条件 指定操作符
     *
     * @param function the field
     * @param operator the operator
     * @param values   the values
     * @return c
     */
    <E, R> C or(Function<E, R> function, String operator, Object... values);

    /**
     * 括号开始
     *
     * @return
     */
    C begin();

    /**
     * 括号结束
     *
     * @return
     */
    C end();

    /**
     * append sql片断
     *
     * @param segment the segment
     * @param params  the params
     * @return c
     */
    C append(String segment, Object... params);

}
