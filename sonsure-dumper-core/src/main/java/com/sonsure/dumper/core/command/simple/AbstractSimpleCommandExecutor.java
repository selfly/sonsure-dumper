package com.sonsure.dumper.core.command.simple;


import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;
import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.command.GenerateKey;
import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.mapping.MappingHandler;

import java.io.Serializable;
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
    public T nativeCommand() {
        this.getSimpleExecutorContext().setNativeCommand(true);
        return (T) this;
    }

    @Override
    public <E> T resultHandler(ResultHandler<E> resultHandler) {
        this.resultHandler = resultHandler;
        return (T) this;
    }

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
    public <E> E oneColFirstResult(Class<E> clazz) {
        this.paginate(1, 1).isCount(false);
        Page<E> page = this.oneColPageResult(clazz);
        return page.getList() != null && !page.getList().isEmpty() ? page.getList().iterator().next() : null;
    }

    @Override
    public <E> Page<E> oneColPageResult(Class<E> clazz) {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        commandContext.setResultType(clazz);
        return (Page<E>) this.doPageResult(commandContext, getSimpleExecutorContext().getPagination(), getSimpleExecutorContext().isCount(), new PageQueryHandler() {
            @Override
            public List<E> queryList(CommandContext commandContext) {
                return (List<E>) getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.QUERY_ONE_COL_LIST);
            }
        });
    }

    @Override
    public void insert() {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        GenerateKey generateKey = new GenerateKey();
        generateKey.setParameter(false);
        commandContext.setGenerateKey(generateKey);
        getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.INSERT);
    }

    @Override
    public Serializable insert(Class<?> clazz) {
        CommandContext commandContext = this.commandContextBuilder.build(this.getSimpleExecutorContext(), getJdbcEngineConfig());
        MappingHandler mappingHandler = getJdbcEngineConfig().getMappingHandler();
        String pkField = mappingHandler.getPkField(clazz);
        String pkColumn = mappingHandler.getColumn(clazz, pkField);
        GenerateKey generateKey = new GenerateKey();
        generateKey.setClazz(clazz);
        generateKey.setColumn(pkColumn);
        generateKey.setParameter(false);
        commandContext.setGenerateKey(generateKey);
        return (Long) this.getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.INSERT);
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
