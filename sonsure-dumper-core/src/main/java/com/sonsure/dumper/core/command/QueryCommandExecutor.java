package com.sonsure.dumper.core.command;

import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;

import java.util.List;

public interface QueryCommandExecutor<C> extends CommandExecutor {

    /**
     * 分页信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    C paginate(int pageNum, int pageSize);

    /**
     * 分页信息
     *
     * @param pageable the pageable
     * @return select
     */
    C paginate(Pageable pageable);

    /**
     * 分页信息
     *
     * @param offset the offset
     * @param size   the size
     * @return select select
     */
    C limit(int offset, int size);

    /**
     * 是否count查询
     *
     * @return select
     */
    C isCount(boolean isCount);


    /**
     * count查询
     *
     * @return
     */
    long count();

    /**
     * 单个结果
     *
     * @param <T> the type parameter
     * @param cls the cls
     * @return t
     */
    <T> T singleResult(Class<T> cls);

    /**
     * 单个结果
     *
     * @return t object
     */
    Object singleResult();

    /**
     * 第一条结果
     *
     * @param <T> the type parameter
     * @param cls the cls
     * @return t
     */
    <T> T firstResult(Class<T> cls);

    /**
     * 第一条结果
     *
     * @return t object
     */
    Object firstResult();

    /**
     * 简单查询，返回单一的结果，例如Long、Integer、String等
     *
     * @return
     */
    <T> T oneColResult(Class<T> clazz);

    /**
     * 查询结果，返回单一的结果列表，例如List<Long>
     *
     * @return
     */
    <T> List<T> oneColList(Class<T> clazz);


    /**
     * 列表查询
     *
     * @param <T> the type parameter
     * @param cls the cls
     * @return list
     */
    <T> List<T> list(Class<T> cls);

    /**
     * 列表查询
     *
     * @return list
     */
    List<Object> list();

    /**
     * 分页列表查询
     *
     * @param <T> the type parameter
     * @param cls the cls
     * @return page
     */
    <T> Page<T> pageResult(Class<T> cls);

    /**
     * 分页列表查询
     *
     * @return page page
     */
    Page<Object> pageResult();

    /**
     * 查询结果，返回结果会有多个值，多数情况下为Map<column,value>
     *
     * @return
     */
    Object objResult();

//    /**
//     * 返回第一条记录的结果
//     *
//     * @return
//     */
//    Object objFirstResult();

    /**
     * 查询结果，objectResult方法的列表查询，返回结果多数情况下为List<Map<column,value>>
     *
     * @return
     */
    List<Object> objList();

//    /**
//     * 简单查询，返回单一的结果，只取第一条
//     *
//     * @return
//     */
//    <T> T oneColFirstResult(Class<T> clazz);


//    /**
//     * singleColumnList分页查询
//     *
//     * @param <T>      the type parameter
//     * @param clazz    the clazz
//     * @param pageable the pageable
//     * @return page list
//     */
//    <T> Page<T> oneColPageList(Class<T> clazz, Pageable pageable);
}
