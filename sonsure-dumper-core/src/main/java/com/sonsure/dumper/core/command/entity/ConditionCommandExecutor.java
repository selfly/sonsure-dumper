package com.sonsure.dumper.core.command.entity;

import com.sonsure.dumper.core.command.lambda.Consumer;
import com.sonsure.dumper.core.command.lambda.LambdaConditionBuilder;

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
     * 特定对象lambda
     *
     * @param obj
     * @param <O>
     * @return
     */
    <O> LambdaConditionBuilder<O, C> lambdaWith(O obj);

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
     * @param consumer
     * @param value
     * @return
     */
    <E> C where(Consumer<E> consumer, Object value);

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
     * @param consumer
     * @param value
     * @return
     */
    <E> C where(Consumer<E> consumer, Object[] value);

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
     * @param consumer the field
     * @param operator the operator
     * @param values   the values
     * @return c
     */
    <E> C where(Consumer<E> consumer, String operator, Object... values);

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
     * @param consumer
     * @param value
     * @return
     */
    <E> C condition(Consumer<E> consumer, Object value);

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
     * @param consumer
     * @param value
     * @return
     */
    <E> C condition(Consumer<E> consumer, Object[] value);

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
     * @param consumer the field
     * @param operator the operator
     * @param values   the values
     * @return c
     */
    <E> C condition(Consumer<E> consumer, String operator, Object... values);

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
     * @param consumer
     * @param value
     * @return
     */
    <E> C and(Consumer<E> consumer, Object value);

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
     * @param consumer
     * @param value
     * @return
     */
    <E> C and(Consumer<E> consumer, Object[] value);

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
     * @param consumer the field
     * @param operator the operator
     * @param values   the values
     * @return c
     */
    <E> C and(Consumer<E> consumer, String operator, Object... values);

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
     * @param consumer
     * @param value
     * @return
     */
    <E> C or(Consumer<E> consumer, Object value);

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
     * @param consumer
     * @param value
     * @return
     */
    <E> C or(Consumer<E> consumer, Object[] value);

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
     * @param consumer the field
     * @param operator the operator
     * @param values   the values
     * @return c
     */
    <E> C or(Consumer<E> consumer, String operator, Object... values);

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
