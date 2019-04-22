package com.sonsure.dumper.core.command.simple;


import com.sonsure.dumper.core.command.QueryCommandExecutor;

/**
 * Created by liyd on 17/4/25.
 */
public interface SimpleCommandExecutor<T extends SimpleCommandExecutor<T>> extends QueryCommandExecutor<T> {

    /**
     * 命令
     *
     * @param command
     * @return
     */
    T command(String command);

    /**
     * 是否强制native，command不做任何加工
     *
     * @param nativeSql the is force native
     * @return t
     */
    T nativeSql(boolean nativeSql);

//    /**
//     * 返回结果类对象
//     *
//     * @param clazz
//     * @param <E>
//     * @return
//     */
//    <E> T resultClass(Class<E> clazz);
//
//    /**
//     * 结果处理器
//     *
//     * @param resultHandler
//     * @param <E>
//     * @return
//     */
//    <E> T resultHandler(ResultHandler<E> resultHandler);
//
//
//    /**
//     * count查询
//     *
//     * @return
//     */
//    long count();
//
//    /**
//     * 单个结果
//     *
//     * @return
//     */
//    Object singleResult();
//
//    /**
//     * 简单查询，返回单一的结果，例如Long、Integer、String等
//     *
//     * @return
//     */
//    <E> E oneColResult(Class<E> clazz);
//
//    /**
//     * 列表查询
//     *
//     * @return
//     */
//    List<?> list();
//
//    /**
//     * 分页列表查询
//     *
//     * @param pageable the pageable
//     * @param isCount  the is count
//     * @return page list
//     */
//    Page<?> pageList(Pageable pageable, boolean isCount);
//
//    /**
//     * 分页列表查询
//     *
//     * @param pageable
//     * @return
//     */
//    Page<?> pageList(Pageable pageable);
//
//    /**
//     * 分页列表查询
//     *
//     * @param pageNum  the page num
//     * @param pageSize the page size
//     * @return page list
//     */
//    Page<?> pageList(int pageNum, int pageSize);
//
//    /**
//     * 分页列表查询
//     *
//     * @param pageNum  the page num
//     * @param pageSize the page size
//     * @param isCount  the is count
//     * @return page list
//     */
//    Page<?> pageList(int pageNum, int pageSize, boolean isCount);

    /**
     * 插入
     */
    void insert();

    /**
     * 插入 返回自增主键
     *
     * @param pkColumn
     * @return
     */
    Long insert(String pkColumn);

    /**
     * 更新
     *
     * @return
     */
    int update();

    /**
     * 执行
     */
    void execute();
}
