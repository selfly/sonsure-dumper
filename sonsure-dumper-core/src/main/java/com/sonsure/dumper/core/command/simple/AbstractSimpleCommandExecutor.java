package com.sonsure.dumper.core.command.simple;


import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;
import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.mapping.AbstractMappingHandler;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;

import java.util.List;

/**
 * Created by liyd on 17/4/25.
 */
public abstract class AbstractSimpleCommandExecutor<T extends SimpleCommandExecutor<T>> extends AbstractCommandExecutor implements SimpleCommandExecutor<T> {

    protected SimpleExecutorContext simpleExecutorContext;

    protected ResultHandler<?> resultHandler;

    public AbstractSimpleCommandExecutor(MappingHandler mappingHandler, PageHandler pageHandler, KeyGenerator keyGenerator, PersistExecutor persistExecutor, String commandCase) {
        super(mappingHandler, pageHandler, keyGenerator, persistExecutor, commandCase);
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
        MappingHandler mappingHandler = this.getMappingHandler();
        if (mappingHandler instanceof AbstractMappingHandler) {
            ((AbstractMappingHandler) mappingHandler).addClassMapping(clazz);
        }
        return (T) this;
    }

    @Override
    public <E> T resultHandler(ResultHandler<E> resultHandler) {
        this.resultHandler = resultHandler;
        return (T) this;
    }

    public long count() {
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext);
        commandContext.setResultType(Long.class);
        Object result = this.persistExecutor.execute(commandContext, CommandType.QUERY_ONE_COL);
        return (Long) result;
    }

    public Object singleResult() {
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext);
        Object result = this.persistExecutor.execute(commandContext, CommandType.QUERY_FOR_MAP);
        if (resultHandler != null) {
            return this.handleResult(result, resultHandler);
        }
        return result;
    }

    @Override
    public <E> E oneColResult(Class<E> clazz) {
        this.simpleExecutorContext.setResultType(clazz);
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext);
        return (E) this.persistExecutor.execute(commandContext, CommandType.QUERY_ONE_COL);
    }

    public List<?> list() {
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext);
        List<?> result = (List<?>) this.persistExecutor.execute(commandContext, CommandType.QUERY_FOR_MAP_LIST);
        if (resultHandler != null) {
            return this.handleResult(result, resultHandler);
        }
        return result;
    }

    public Page<?> pageList(Pageable pageable, boolean isCount) {
        return this.pageList(pageable.getPageNum(), pageable.getPageSize(), isCount);
    }

    public Page<?> pageList(int pageNum, int pageSize, boolean isCount) {
        Page<?> page = this.doPageList(pageNum, pageSize, isCount, new PageQueryHandler() {
            @Override
            public List<?> queryList(CommandContext commandContext) {
                return (List<?>) persistExecutor.execute(commandContext, CommandType.QUERY_FOR_MAP_LIST);
            }
        });
        if (this.resultHandler != null && page.getList() != null) {
            List<?> list = this.handleResult(page.getList(), resultHandler);
            return new Page<>(list, page.getPagination());
        }
        return page;
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
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext);
        commandContext.setPkValueByDb(false);
        this.persistExecutor.execute(commandContext, CommandType.INSERT);
    }

    @Override
    public Long insert(String pkColumn) {
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext);
        commandContext.setPkValueByDb(true);
        commandContext.setPkColumn(pkColumn);
        return (Long) this.persistExecutor.execute(commandContext, CommandType.INSERT);
    }

    public int update() {
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext);
        return (Integer) this.persistExecutor.execute(commandContext, CommandType.UPDATE);
    }

    public void execute() {
        CommandContext commandContext = this.commandContextBuilder.build(this.simpleExecutorContext);
        this.persistExecutor.execute(commandContext, CommandType.EXECUTE);
    }
}
