package com.sonsure.dumper.core.command.entity;


import com.sonsure.commons.bean.BeanKit;
import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.persist.PersistExecutor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * Created by liyd on 17/4/12.
 */
public class SelectImpl extends AbstractConditionBuilder<Select> implements Select {

    protected SelectContext selectContext;

    public SelectImpl(JdbcEngineConfig jdbcEngineConfig) {
        super(jdbcEngineConfig);
        selectContext = new SelectContext();
    }


    public SelectImpl(JdbcEngineConfig jdbcEngineConfig, String[] fields) {
        super(jdbcEngineConfig);
        selectContext = new SelectContext();
        if (fields != null) {
            selectContext.addSelectFields(fields);
        }
    }

    @Override
    public Select from(Class<?> cls) {
        selectContext.addFromClass(cls);
        return this;
    }

    @Override
    public Select from(Class<?> cls, String alias, Object... clsAndAlias) {
        selectContext.addFromClass(cls, alias);
        if (ArrayUtils.isNotEmpty(clsAndAlias)) {
            if (ArrayUtils.getLength(clsAndAlias) % 2 != 0) {
                throw new SonsureJdbcException("指定多表必须一个class一个别名对应");
            }
            for (int i = 0; i < clsAndAlias.length; i++) {
                Object clazz = clsAndAlias[i];
                Object aliasName = clsAndAlias[++i];
                if (!(clazz instanceof Class) || !(aliasName instanceof String)) {
                    throw new SonsureJdbcException("指定多表必须一个class(Class类型)一个别名(String类型)对应");
                }
                selectContext.addFromClass(((Class) clazz), ((String) aliasName));
            }
        }
        return this;
    }

    @Override
    public Select exclude(String... fields) {
        selectContext.addExcludeFields(fields);
        return this;
    }

    @Override
    public Select extraField(String... fields) {
        throw new UnsupportedOperationException("extraField方法暂不支持");
    }


    @Override
    public Select groupBy(String... fields) {
        selectContext.addGroupByField(fields);
        return this;
    }

    @Override
    public Select orderBy(String... fields) {
        selectContext.addOrderByField(fields);
        return this;
    }

    @Override
    public Select asc() {
        this.selectContext.setOrderByType("asc");
        return this;
    }

    @Override
    public Select desc() {
        this.selectContext.setOrderByType("desc");
        return this;
    }

    @Override
    public Select paginate(int pageNum, int pageSize) {
        this.selectContext.paginate(pageNum, pageSize);
        return this;
    }

    @Override
    public Select paginate(Pageable pageable) {
        this.selectContext.paginate(pageable);
        return this;
    }

    @Override
    public Select limit(int offset, int size) {
        this.selectContext.limit(offset, size);
        return this;
    }

    @Override
    public Select isCount(boolean isCount) {
        selectContext.isCount(isCount);
        return this;
    }

    @Override
    public long count() {
        CommandContext commandContext = this.commandContextBuilder.build(this.selectContext, getJdbcEngineConfig());
        PersistExecutor persistExecutor = this.jdbcEngineConfig.getPersistExecutor();
        String countCommand = this.jdbcEngineConfig.getPageHandler().getCountCommand(commandContext.getCommand(), persistExecutor.getDialect());
        CommandContext countCommandContext = BeanKit.copyProperties(new CommandContext(), commandContext);
        countCommandContext.setCommand(countCommand);
        countCommandContext.setResultType(Long.class);
        Object result = persistExecutor.execute(countCommandContext, CommandType.QUERY_ONE_COL);
        return (Long) result;
    }

    @Override
    public <T> T singleResult(Class<T> cls) {
        CommandContext commandContext = this.commandContextBuilder.build(this.selectContext, getJdbcEngineConfig());
        commandContext.setResultType(cls);
        return (T) this.getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_SINGLE_RESULT);
    }

    @Override
    public Object singleResult() {

//        commandTable.setResultType(this.commandTable.getModelClass());
////        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
////        return (T) this.persistExecutor.execute(commandContext, CommandType.QUERY_SINGLE_RESULT);
        return null;
    }

    @Override
    public <T> T firstResult(Class<T> cls) {
        this.paginate(1, 1).isCount(false);
        Page<T> page = this.pageResult(cls);
        return page != null && page.getList() != null && !page.getList().isEmpty() ? page.getList().iterator().next() : null;
    }

    @Override
    public Object firstResult() {
        return null;
    }

    @Override
    public <E> E oneColResult(Class<E> clazz) {
        CommandContext commandContext = this.commandContextBuilder.build(this.selectContext, getJdbcEngineConfig());
        commandContext.setResultType(clazz);
        return (E) this.getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_ONE_COL);
    }

    @Override
    public <E> List<E> oneColList(Class<E> clazz) {
        CommandContext commandContext = this.commandContextBuilder.build(this.selectContext, getJdbcEngineConfig());
        commandContext.setResultType(clazz);
        return (List<E>) this.getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_ONE_COL_LIST);
    }


    @Override
    public <T> List<T> list(Class<T> cls) {
        CommandContext commandContext = this.commandContextBuilder.build(this.selectContext, getJdbcEngineConfig());
        commandContext.setResultType(cls);
        return (List<T>) this.getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_FOR_LIST);
    }

    @Override
    public List<Object> list() {
        return null;
    }

    @Override
    public <T> Page<T> pageResult(Class<T> cls) {
        CommandContext commandContext = this.commandContextBuilder.build(this.selectContext, getJdbcEngineConfig());
        commandContext.setResultType(cls);
        return (Page<T>) this.doPageResult(commandContext, selectContext.getPagination(), selectContext.isCount(), new PageQueryHandler() {
            @Override
            public List<?> queryList(CommandContext commandContext) {
                return (List<?>) getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_FOR_LIST);
            }
        });
    }

    @Override
    public Page<Object> pageResult() {
        CommandContext commandContext = this.commandContextBuilder.build(this.selectContext, getJdbcEngineConfig());
        return (Page<Object>) this.doPageResult(commandContext, selectContext.getPagination(), selectContext.isCount(), new PageQueryHandler() {
            @Override
            public List<Object> queryList(CommandContext commandContext) {
                return (List<Object>) getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_FOR_MAP_LIST);
            }
        });
    }

    @Override
    protected WhereContext getWhereContext() {
        return this.selectContext;
    }

    public Object objResult() {
        CommandContext commandContext = this.commandContextBuilder.build(this.selectContext, getJdbcEngineConfig());
        Object result = this.getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_FOR_MAP);
        return result;
    }

    //
//    @Override
//    public Object objFirstResult() {
//        Page<?> page = this.objPageList(1, 1, false);
//        return page != null && page.getList() != null && !page.getList().isEmpty() ? page.getList().iterator().next() : null;
//    }
//
    public List<Object> objList() {
        CommandContext commandContext = this.commandContextBuilder.build(this.selectContext, getJdbcEngineConfig());
        List<Object> result = (List<Object>) this.getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_FOR_MAP_LIST);
        return result;
    }
//
//    @Override
//    public Page<?> objPageList(Pageable pageable) {
//        return this.objPageList(pageable, true);
//    }
//
//    @Override
//    public Page<?> objPageList(Pageable pageable, boolean isCount) {
//        return this.objPageList(pageable.getPageNum(), pageable.getPageSize(), isCount);
//    }
//
//    @Override
//    public Page<?> objPageList(int pageNum, int pageSize) {
//        return this.objPageList(pageNum, pageSize, true);
//    }
//
//    @Override
//    public Page<?> objPageList(int pageNum, int pageSize, boolean isCount) {
//        Page<?> page = this.doPageList(pageNum, pageSize, isCount, new PageQueryHandler() {
//            @Override
//            public List<?> queryList(CommandContext commandContext) {
//                return (List<?>) persistExecutor.execute(commandContext, CommandType.QUERY_FOR_MAP_LIST);
//            }
//        });
//        if (isForceResultToModel) {
//            List<?> list = this.handleResult(page.getList(), DefaultResultHandler.newInstance(commandTable.getModelClass()));
//            return new Page<>(list, page.getPagination());
//        }
//        return page;
//    }
//
//
//    @Override
//    public <E> E oneColFirstResult(Class<E> clazz) {
//        Page<E> page = this.oneColPageList(clazz, 1, 1, false);
//        return page != null && page.getList() != null && !page.getList().isEmpty() ? page.getList().iterator().next() : null;
//    }
//

//    @Override
//    public <E> Page<E> oneColPageList(Class<E> clazz, Pageable pageable, boolean isCount) {
//        return this.oneColPageList(clazz, pageable.getPageNum(), pageable.getPageSize(), isCount);
//    }
//
//    @Override
//    public <E> Page<E> oneColPageList(Class<E> clazz, int pageNum, int pageSize, boolean isCount) {
//        this.commandTable.setResultType(clazz);
//        return (Page<E>) this.doPageList(pageNum, pageSize, isCount, new PageQueryHandler() {
//            @Override
//            public List<?> queryList(CommandContext commandContext) {
//                return (List<E>) persistExecutor.execute(commandContext, CommandType.QUERY_ONE_COL_LIST);
//            }
//        });
//    }
//
//    @Override
//    public <E> Page<E> oneColPageList(Class<E> clazz, Pageable pageable) {
//        return this.oneColPageList(clazz, pageable, true);
//    }
//
//    @Override
//    public <E> Page<E> oneColPageList(Class<E> clazz, int pageNum, int pageSize) {
//        return this.oneColPageList(clazz, pageNum, pageSize, true);
//    }
//
//
//    @Override
//    public T firstResult() {
//        Page<T> page = this.pageList(1, 1, false);
//        return page != null && page.getList() != null && !page.getList().isEmpty() ? page.getList().iterator().next() : null;
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<T> list() {
//        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
//        return (List<T>) this.persistExecutor.execute(commandContext, CommandType.QUERY_FOR_LIST);
//    }
//
//    @Override
//    public Page<T> pageList(Pageable pageable, boolean isCount) {
//        return this.pageList(pageable.getPageNum(), pageable.getPageSize(), isCount);
//    }
//
//    public Page<T> pageList(Pageable pageable) {
//        return this.pageList(pageable.getPageNum(), pageable.getPageSize(), true);
//    }
//
//    @SuppressWarnings("unchecked")
//    public Page<T> pageList(int pageNum, int pageSize) {
//        return this.pageList(pageNum, pageSize, true);
//    }
//
//    @Override
//    public Page<T> pageList(int pageNum, int pageSize, boolean isCount) {
//        return (Page<T>) this.doPageList(pageNum, pageSize, isCount, new PageQueryHandler() {
//            @Override
//            public List<?> queryList(CommandContext commandContext) {
//                return (List<?>) persistExecutor.execute(commandContext, CommandType.QUERY_FOR_LIST);
//            }
//        });
//    }

}
