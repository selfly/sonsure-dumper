package com.sonsure.dumper.core.command.simple;


import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;
import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 17/4/25.
 */
public abstract class AbstractSimpleCommandExecutor<T extends SimpleCommandExecutor<T>> extends AbstractCommandExecutor implements SimpleCommandExecutor<T> {

    protected ResultHandler<?> resultHandler;

    public AbstractSimpleCommandExecutor(JdbcEngineConfig jdbcEngineConfig) {
        super(jdbcEngineConfig);
    }

    @Override
    public T command(String command) {
        this.getSimpleExecutorContext().setCommand(command);
        return (T) this;
    }

    @Override
    public T nativeSql(boolean nativeSql) {
        this.getSimpleExecutorContext().setNativeSql(nativeSql);
        return (T) this;
    }

//    @Override
//    public <E> T resultClass(Class<E> clazz) {
//        this.resultHandler = DefaultResultHandler.newInstance(clazz);
////        MappingHandler mappingHandler = this.getMappingHandler();
////        if (mappingHandler instanceof AbstractMappingHandler) {
////            ((AbstractMappingHandler) mappingHandler).addClassMapping(clazz);
////        }
//        return (T) this;
//    }
//
//    @Override
//    public <E> T resultHandler(ResultHandler<E> resultHandler) {
//        this.resultHandler = resultHandler;
//        return (T) this;
//    }

    @Override
    public long count() {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        commandContext.setResultType(Long.class);
        Object result = getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_ONE_COL);
        return (Long) result;
    }

    public Object singleResult() {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        Object result = getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_FOR_MAP);
        if (resultHandler != null) {
            return this.handleResult(result, resultHandler);
        }
        return result;
    }

    @Override
    public <E> E oneColResult(Class<E> clazz) {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        commandContext.setResultType(clazz);
        return (E) getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_ONE_COL);
    }

    @Override
    public List<Object> list() {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        List<Object> result = (List<Object>) getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_FOR_MAP_LIST);
        return result;
    }

//    public Page<?> pageList(Pageable pageable, boolean isCount) {
//        return this.pageList(pageable.getPageNum(), pageable.getPageSize(), isCount);
//    }
//
//    public Page<?> pageList(int pageNum, int pageSize, boolean isCount) {
////        Page<?> page = this.doPageList(pageNum, pageSize, isCount, new PageQueryHandler() {
////            @Override
////            public List<?> queryList(CommandContext commandContext) {
////                return (List<?>) persistExecutor.execute(commandContext, CommandType.QUERY_FOR_MAP_LIST);
////            }
////        });
////        if (this.resultHandler != null && page.getList() != null) {
//            List<?> list = this.handleResult(page.getList(), resultHandler);
////            return new Page<>(list, page.getPagination());
////        }
////        return page;
//        return null;
//    }

//    @Override
//    public Page<?> pageList(Pageable pageable) {
//        return this.pageList(pageable.getPageNum(), pageable.getPageSize(), true);
//    }
//
//    @Override
//    public Page<?> pageList(int pageNum, int pageSize) {
//        return this.pageList(pageNum, pageSize, true);
//    }


    @Override
    public T paginate(int pageNum, int pageSize) {
        this.getSimpleExecutorContext().paginate(pageNum, pageSize);
        return (T) this;
    }

    @Override
    public T paginate(Pageable pageable) {
        this.getSimpleExecutorContext().paginate(pageable);
        return (T) this;
    }

    @Override
    public T limit(int offset, int size) {
        getSimpleExecutorContext().limit(offset, size);
        return (T) this;
    }

    @Override
    public T isCount(boolean isCount) {
        getSimpleExecutorContext().setCount(isCount);
        return (T) this;
    }

    @Override
    public <T> T singleResult(Class<T> cls) {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        Object result = getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_FOR_MAP);
        return this.handleResult(result, getResultHandler(cls));
    }

    @Override
    public <T> T firstResult(Class<T> cls) {
        this.paginate(1, 1).isCount(false);
        Page<T> page = this.pageResult(cls);
        return page.getList() != null && !page.getList().isEmpty() ? page.getList().iterator().next() : null;
    }

    @Override
    public Object firstResult() {
        this.paginate(1, 1).isCount(false);
        Page<Object> page = this.pageResult();
        return page.getList() != null && !page.getList().isEmpty() ? page.getList().iterator().next() : null;
    }

    @Override
    public <T> List<T> oneColList(Class<T> clazz) {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        commandContext.setResultType(clazz);
        return (List<T>) this.getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_ONE_COL_LIST);

    }

    @Override
    public <T> List<T> list(Class<T> cls) {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        List<Map<String, Object>> mapList = (List<Map<String, Object>>) this.getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_FOR_MAP_LIST);
        List<T> result = this.handleResult(mapList, getResultHandler(cls));
        return result;
    }

    @Override
    public <T> Page<T> pageResult(Class<T> cls) {
        Page<Object> page = this.pageResult();
        return this.handleResult(page, getResultHandler(cls));
    }

    @Override
    public Page<Object> pageResult() {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        Page<?> page = this.doPageResult(commandContext, getSimpleExecutorContext().getPagination(), getSimpleExecutorContext().isCount(), new PageQueryHandler() {
            @Override
            public List<?> queryList(CommandContext commandContext) {
                return (List<?>) getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_FOR_MAP_LIST);
            }
        });
        Page<Object> resultPage = new Page<>(page.getPagination());
        if (page.getList() != null) {
            List<Object> list = new ArrayList<>(page.getList());
            resultPage.setList(list);
        }
        return resultPage;
    }

    @Override
    public Object objResult() {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        Object result = this.getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_FOR_MAP);
        return result;
    }

    @Override
    public List<Object> objList() {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        List<Object> result = (List<Object>) this.getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_FOR_MAP_LIST);
        return result;
    }

    @Override
    public void insert() {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        commandContext.setPkValueByDb(false);
        getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.INSERT);
    }

    @Override
    public Long insert(String pkColumn) {
//        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext);
//        commandContext.setPkValueByDb(true);
//        commandContext.setPkColumn(pkColumn);
//        return (Long) this.persistExecutor.execute(commandContext, CommandType.INSERT);
        return null;
    }

    public int update() {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        return (Integer) getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.UPDATE);
    }

    public void execute() {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.EXECUTE);
    }

    protected <T> ResultHandler<T> getResultHandler(Class<T> cls) {
        if (this.resultHandler == null) {
            return DefaultResultHandler.newInstance(cls);
        }
        return (ResultHandler<T>) this.resultHandler;
    }

    protected abstract SimpleExecutorContext getSimpleExecutorContext();
}
