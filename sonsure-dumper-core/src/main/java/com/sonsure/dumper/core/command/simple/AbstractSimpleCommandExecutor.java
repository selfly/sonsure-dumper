package com.sonsure.dumper.core.command.simple;


import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;
import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.management.CommandTable;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;

import java.util.List;

/**
 * Created by liyd on 17/4/25.
 */
public abstract class AbstractSimpleCommandExecutor<T extends SimpleCommandExecutor<T>> extends AbstractCommandExecutor implements SimpleCommandExecutor<T> {

    protected String command;

    protected ResultHandler<?> resultHandler;

    public AbstractSimpleCommandExecutor(MappingHandler mappingHandler, PageHandler pageHandler, KeyGenerator keyGenerator, PersistExecutor persistExecutor, boolean commandUppercase) {
        super(mappingHandler, pageHandler, keyGenerator, persistExecutor, commandUppercase);
    }

    @Override
    public T command(String command) {
        this.command = command;
        return (T) this;
    }

    @Override
    public T forceNative(boolean isForceNative) {
        this.commandTable.setForceNative(isForceNative);
        return (T) this;
    }

    @Override
    public <E> T resultClass(Class<E> clazz) {
        this.resultHandler = DefaultResultHandler.newInstance(clazz);
        return (T) this;
    }

    @Override
    public <E> T resultHandler(ResultHandler<E> resultHandler) {
        this.resultHandler = resultHandler;
        return (T) this;
    }

    public long count() {
        this.setNativeData();
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setResultType(Long.class);
        Object result = this.persistExecutor.execute(commandContext, CommandType.QUERY_ONE_COL);
        return (Long) result;
    }

    public Object singleResult() {
        this.setNativeData();
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        Object result = this.persistExecutor.execute(commandContext, CommandType.QUERY_FOR_MAP);
        if (resultHandler != null) {
            return this.handleResult(result, resultHandler);
        }
        return result;
    }

    public List<?> list() {
        this.setNativeData();
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
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
        this.setNativeData();
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
        this.setNativeData();
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setPkValueByDb(false);
        this.persistExecutor.execute(commandContext, CommandType.INSERT);
    }

    @Override
    public Long insert(String pkColumn) {
        this.setNativeData();
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setPkValueByDb(true);
        commandContext.setPkColumn(pkColumn);
        return (Long) this.persistExecutor.execute(commandContext, CommandType.INSERT);
    }

    public int update() {
        this.setNativeData();
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        return (Integer) this.persistExecutor.execute(commandContext, CommandType.UPDATE);
    }

    public void execute() {
        this.setNativeData();
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        this.persistExecutor.execute(commandContext, CommandType.EXECUTE);
    }

    protected void setNativeData() {
        this.commandTable.addExtendData(CommandTable.ExtendDataKey.COMMAND.name(), this.command);
        this.commandTable.addExtendData(CommandTable.ExtendDataKey.PARAMETERS.name(), getParameters());
    }

    protected abstract Object getParameters();
}
