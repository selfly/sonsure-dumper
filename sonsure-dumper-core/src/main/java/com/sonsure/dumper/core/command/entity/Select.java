package com.sonsure.dumper.core.command.entity;


import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;

import java.util.List;

/**
 * Created by liyd on 17/4/12.
 */
public interface Select<T extends Object> extends ConditionBuilder<Select<T>> {

    /**
     * 白名单
     *
     * @param fields
     * @return
     */
    Select<T> include(String... fields);

    /**
     * 黑名单
     *
     * @param fields
     * @return
     */
    Select<T> exclude(String... fields);

    /**
     * 添加查询字段
     *
     * @param fields
     * @return
     */
    Select<T> addSelectField(String... fields);

    /**
     * 不查询实体类属性
     *
     * @return
     */
    Select<T> notSelectEntityField();

    /**
     * 添加 group by属性
     *
     * @param fields
     * @return
     */
    Select<T> groupBy(String... fields);

    /**
     * 排序属性
     *
     * @param fields
     * @return
     */
    Select<T> orderBy(String... fields);

    /**
     * 主键排序
     *
     * @return
     */
    Select<T> orderById();

    /**
     * asc排序
     *
     * @return
     */
    Select<T> asc();

    /**
     * desc 排序
     *
     * @return
     */
    Select<T> desc();

    /**
     * 强制处理结果成泛型实体模型
     * 仅对objectResult、objList、objectPageList方法结果有效
     * 调用该方法后将对返回的Object类型(一般为Map<String,Object>)强制转换成泛型实体，
     * 泛型实体不包含的属性如果继承了{@link com.sonsure.commons.model.Model}将放入ExtensionProperty，否则将丢失
     *
     * @return
     */
    Select<T> forceResultToModel();

    /**
     * count查询
     *
     * @return
     */
    long count();

    /**
     * 单个结果
     *
     * @return
     */
    T singleResult();

    /**
     * 第一条结果
     *
     * @return
     */
    T firstResult();

    /**
     * 列表查询
     *
     * @return
     */
    List<T> list();

    /**
     * 分页列表查询
     *
     * @param pageable
     * @return
     */
    Page<T> pageList(Pageable pageable);

    /**
     * 分页列表查询
     *
     * @param pageable the pageable
     * @param isCount  the is count
     * @return page list
     */
    Page<T> pageList(Pageable pageable, boolean isCount);

    /**
     * 分页列表查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T> pageList(int pageNum, int pageSize);

    /**
     * 分页列表查询
     *
     * @param pageNum  the page num
     * @param pageSize the page size
     * @param isCount  the is count
     * @return page list
     */
    Page<T> pageList(int pageNum, int pageSize, boolean isCount);

    /**
     * 查询结果，返回结果会有多个值，多数情况下为Map<column,value>
     *
     * @return
     */
    Object objResult();

    /**
     * 返回第一条记录的结果
     *
     * @return
     */
    Object objFirstResult();

    /**
     * 查询结果，objectResult方法的列表查询，返回结果多数情况下为List<Map<column,value>>
     *
     * @return
     */
    List<?> objList();

    /**
     * objectList分页查询
     *
     * @param pageable
     * @return
     */
    Page<?> objPageList(Pageable pageable);

    /**
     * objectList分页查询
     *
     * @param pageable the pageable
     * @param isCount  the is count
     * @return page list
     */
    Page<?> objPageList(Pageable pageable, boolean isCount);

    /**
     * objectList分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<?> objPageList(int pageNum, int pageSize);

    /**
     * objectList分页查询
     *
     * @param pageNum  the page num
     * @param pageSize the page size
     * @param isCount  the is count
     * @return page list
     */
    Page<?> objPageList(int pageNum, int pageSize, boolean isCount);

    /**
     * 简单查询，返回单一的结果，例如Long、Integer、String等
     *
     * @return
     */
    <E> E oneColResult(Class<E> clazz);

    /**
     * 简单查询，返回单一的结果，只取第一条
     *
     * @return
     */
    <E> E oneColFirstResult(Class<E> clazz);

    /**
     * 查询结果，返回单一的结果列表，例如List<Long>
     *
     * @return
     */
    <E> List<E> oneColList(Class<E> clazz);


    /**
     * singleColumnList分页查询
     *
     * @param <E>      the type parameter
     * @param clazz    the clazz
     * @param pageable the pageable
     * @return page list
     */
    <E> Page<E> oneColPageList(Class<E> clazz, Pageable pageable);


    /**
     * singleColumnList分页查询
     *
     * @param <E>      the type parameter
     * @param clazz    the clazz
     * @param pageable the pageable
     * @param isCount  the is count
     * @return page list
     */
    <E> Page<E> oneColPageList(Class<E> clazz, Pageable pageable, boolean isCount);

    /**
     * singleColumnList分页查询
     *
     * @param <E>      the type parameter
     * @param clazz    the clazz
     * @param pageNum  the page num
     * @param pageSize the page size
     * @return page list
     */
    <E> Page<E> oneColPageList(Class<E> clazz, int pageNum, int pageSize);

    /**
     * singleColumnList分页查询
     *
     * @param <E>      the type parameter
     * @param clazz    the clazz
     * @param pageNum  the page num
     * @param pageSize the page size
     * @param isCount  the is count
     * @return page list
     */
    <E> Page<E> oneColPageList(Class<E> clazz, int pageNum, int pageSize, boolean isCount);

}
