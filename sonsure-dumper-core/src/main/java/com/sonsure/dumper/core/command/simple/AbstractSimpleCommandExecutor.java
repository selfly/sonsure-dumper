package com.sonsure.dumper.core.command.simple;


import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;
import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

import java.util.List;

/**
 * Created by liyd on 17/4/25.
 */
public abstract class AbstractSimpleCommandExecutor<T extends SimpleCommandExecutor<T>> extends AbstractCommandExecutor implements SimpleCommandExecutor<T> {

    protected SimpleExecutorContext simpleExecutorContext;

    protected ResultHandler<?> resultHandler;

    public AbstractSimpleCommandExecutor(JdbcEngineConfig jdbcEngineConfig) {
        super(jdbcEngineConfig);
        simpleExecutorContext = new SimpleExecutorContext();
    }

    @Override
    public T command(String command) {
        this.simpleExecutorContext.setCommand(command);
        return (T) this;
    }

    @Override
    public T parameters(Object... value) {
        for (Object val : value) {
            this.simpleExecutorContext.addParameter(value);
        }
        return (T) this;
    }

//    @Override
//    public T forceNative(boolean isForceNative) {
//        this.commandTable.setForceNative(isForceNative);
//        return (T) this;
//    }

    @Override
    public <E> T resultClass(Class<E> clazz) {
        this.resultHandler = DefaultResultHandler.newInstance(clazz);
//        MappingHandler mappingHandler = this.getMappingHandler();
//        if (mappingHandler instanceof AbstractMappingHandler) {
//            ((AbstractMappingHandler) mappingHandler).addClassMapping(clazz);
//        }
        return (T) this;
    }

    @Override
    public <E> T resultHandler(ResultHandler<E> resultHandler) {
        this.resultHandler = resultHandler;
        return (T) this;
    }

    public long count() {
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext, getJdbcEngineConfig());
        commandContext.setResultType(Long.class);
        Object result = getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_ONE_COL);
        return (Long) result;
    }

    public Object singleResult() {
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext, getJdbcEngineConfig());
        Object result = getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_FOR_MAP);
        if (resultHandler != null) {
            return this.handleResult(result, resultHandler);
        }
        return result;
    }

    @Override
    public <E> E oneColResult(Class<E> clazz) {
//        this.simpleExecutorContext.setResultType(clazz);
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext, getJdbcEngineConfig());
        return (E) getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_ONE_COL);
    }

    public List<?> list() {
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext, getJdbcEngineConfig());
        List<?> result = (List<?>) getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_FOR_MAP_LIST);
        if (resultHandler != null) {
            return this.handleResult(result, resultHandler);
        }
        return result;
    }

    public Page<?> pageList(Pageable pageable, boolean isCount) {
        return this.pageList(pageable.getPageNum(), pageable.getPageSize(), isCount);
    }

    public Page<?> pageList(int pageNum, int pageSize, boolean isCount) {
//        Page<?> page = this.doPageList(pageNum, pageSize, isCount, new PageQueryHandler() {
//            @Override
//            public List<?> queryList(CommandContext commandContext) {
//                return (List<?>) persistExecutor.execute(commandContext, CommandType.QUERY_FOR_MAP_LIST);
//            }
//        });
//        if (this.resultHandler != null && page.getList() != null) {
//            List<?> list = this.handleResult(page.getList(), resultHandler);
//            return new Page<>(list, page.getPagination());
//        }
//        return page;
        return null;
    }

    @Override
    public Page<?> pageList(Pageable pageable) {
        return this.pageList(pageable.getPageNum(), pageable.getPageSize(), true);
    }

    @Override
    public Page<?> pageList(int pageNum, int pageSize) {
        return this.pageList(pageNum, pageSize, true);
    }

    @Override
    public void insert() {
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext, getJdbcEngineConfig());
        commandContext.setPkValueByDb(false);
        getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.INSERT);
    }

    @Override
    public Long insert(String pkColumn) {
//        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext);
//        commandContext.setPkValueByDb(true);
//        commandContext.setPkColumn(pkColumn);
//        return (Long) this.persistExecutor.execute(commandContext, CommandType.INSERT);
        return null;
    }

    public int update() {
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext, getJdbcEngineConfig());
        return (Integer) getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.UPDATE);
    }

    public void execute() {
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext, getJdbcEngineConfig());
        getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.EXECUTE);
    }
}
