package com.sonsure.dumper.core.command.entity;


import com.sonsure.commons.bean.BeanKit;
import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.command.simple.DefaultResultHandler;
import com.sonsure.dumper.core.management.CommandField;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;

import java.util.List;

/**
 * Created by liyd on 17/4/12.
 */
public class SelectImpl<T extends Object> extends AbstractConditionBuilder<Select<T>> implements Select<T> {

    public SelectImpl(MappingHandler mappingHandler, PageHandler pageHandler, KeyGenerator keyGenerator, PersistExecutor persistExecutor, String commandCase) {
        super(mappingHandler, pageHandler, keyGenerator, persistExecutor, commandCase);
    }

    /**
     * 是否强制转换结果到实体模型
     */
    protected boolean isForceResultToModel = false;

    public Select<T> include(String... fields) {
        this.commandTable.addIncludeFields(fields);
        return this;
    }

    public Select<T> exclude(String... fields) {
        this.commandTable.addExcludeFields(fields);
        return this;
    }

    public Select<T> addSelectField(String... fields) {
        for (String field : fields) {
            CommandField commandField = CommandField.builder()
                    .name(field)
                    .orig(CommandField.Orig.MANUAL)
                    .type(CommandField.Type.CUSTOM_SELECT_FIELD)
                    .build();
            this.commandTable.addOperationField(commandField);
        }
        return this;
    }

    public Select<T> notSelectEntityField() {
        this.commandTable.setNotSelectEntityField(true);
        return this;
    }

    public Select<T> groupBy(String... fields) {
        for (String field : fields) {
            CommandField commandField = CommandField.builder()
                    .name(field)
                    .orig(CommandField.Orig.MANUAL)
                    .type(CommandField.Type.GROUP_BY)
                    .build();
            this.commandTable.addGroupByField(commandField);
        }
        return this;
    }

    public Select<T> orderBy(String... fields) {
        for (String field : fields) {
            CommandField commandField = CommandField.builder()
                    .name(field)
                    .orig(CommandField.Orig.MANUAL)
                    .type(CommandField.Type.ORDER_BY)
                    .build();
            this.commandTable.addOrderByField(commandField);
        }
        return this;
    }

    public Select<T> orderById() {
        String pkField = mappingHandler.getPkField(this.commandTable.getModelClass());
        CommandField commandField = CommandField.builder()
                .name(pkField)
                .orig(CommandField.Orig.MANUAL)
                .type(CommandField.Type.ORDER_BY)
                .build();
        this.commandTable.addOrderByField(commandField);
        return this;
    }

    public Select<T> asc() {
        this.commandTable.setOrderBy("asc");
        return this;
    }

    public Select<T> desc() {
        this.commandTable.setOrderBy("desc");
        return this;
    }

    @Override
    public Select<T> forceResultToModel() {
        this.isForceResultToModel = true;
        return this;
    }

    public long count() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        String countCommand = this.pageHandler.getCountCommand(commandContext.getCommand(), persistExecutor.getDialect());
        CommandContext countCommandContext = BeanKit.copyProperties(new CommandContext(), commandContext);
        countCommandContext.setCommand(countCommand);
        countCommandContext.setResultType(Long.class);
        Object result = this.persistExecutor.execute(countCommandContext, CommandType.QUERY_ONE_COL);
        return (Long) result;
    }

    public Object objResult() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        Object result = this.persistExecutor.execute(commandContext, CommandType.QUERY_FOR_MAP);
        if (isForceResultToModel) {
            return this.handleResult(result, DefaultResultHandler.newInstance(commandContext.getModelClass()));
        }
        return result;
    }

    @Override
    public Object objFirstResult() {
        Page<?> page = this.objPageList(1, 1, false);
        return page != null && page.getList() != null && !page.getList().isEmpty() ? page.getList().iterator().next() : null;
    }

    public List<?> objList() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        List<?> result = (List<?>) this.persistExecutor.execute(commandContext, CommandType.QUERY_FOR_MAP_LIST);
        if (isForceResultToModel) {
            return this.handleResult(result, DefaultResultHandler.newInstance(commandContext.getModelClass()));
        }
        return result;
    }

    @Override
    public Page<?> objPageList(Pageable pageable) {
        return this.objPageList(pageable, true);
    }

    @Override
    public Page<?> objPageList(Pageable pageable, boolean isCount) {
        return this.objPageList(pageable.getPageNum(), pageable.getPageSize(), isCount);
    }

    @Override
    public Page<?> objPageList(int pageNum, int pageSize) {
        return this.objPageList(pageNum, pageSize, true);
    }

    @Override
    public Page<?> objPageList(int pageNum, int pageSize, boolean isCount) {
        Page<?> page = this.doPageList(pageNum, pageSize, isCount, new PageQueryHandler() {
            @Override
            public List<?> queryList(CommandContext commandContext) {
                return (List<?>) persistExecutor.execute(commandContext, CommandType.QUERY_FOR_MAP_LIST);
            }
        });
        if (isForceResultToModel) {
            List<?> list = this.handleResult(page.getList(), DefaultResultHandler.newInstance(commandTable.getModelClass()));
            return new Page<>(list, page.getPagination());
        }
        return page;
    }

    @Override
    public <E> E oneColResult(Class<E> clazz) {
        this.commandTable.setResultType(clazz);
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        return (E) this.persistExecutor.execute(commandContext, CommandType.QUERY_ONE_COL);
    }

    @Override
    public <E> E oneColFirstResult(Class<E> clazz) {
        Page<E> page = this.oneColPageList(clazz, 1, 1, false);
        return page != null && page.getList() != null && !page.getList().isEmpty() ? page.getList().iterator().next() : null;
    }

    @Override
    public <E> List<E> oneColList(Class<E> clazz) {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setResultType(clazz);
        return (List<E>) this.persistExecutor.execute(commandContext, CommandType.QUERY_ONE_COL_LIST);
    }

    @Override
    public <E> Page<E> oneColPageList(Class<E> clazz, Pageable pageable, boolean isCount) {
        return this.oneColPageList(clazz, pageable.getPageNum(), pageable.getPageSize(), isCount);
    }

    @Override
    public <E> Page<E> oneColPageList(Class<E> clazz, int pageNum, int pageSize, boolean isCount) {
        this.commandTable.setResultType(clazz);
        return (Page<E>) this.doPageList(pageNum, pageSize, isCount, new PageQueryHandler() {
            @Override
            public List<?> queryList(CommandContext commandContext) {
                return (List<E>) persistExecutor.execute(commandContext, CommandType.QUERY_ONE_COL_LIST);
            }
        });
    }

    @Override
    public <E> Page<E> oneColPageList(Class<E> clazz, Pageable pageable) {
        return this.oneColPageList(clazz, pageable, true);
    }

    @Override
    public <E> Page<E> oneColPageList(Class<E> clazz, int pageNum, int pageSize) {
        return this.oneColPageList(clazz, pageNum, pageSize, true);
    }

    @SuppressWarnings("unchecked")
    public T singleResult() {
        commandTable.setResultType(this.commandTable.getModelClass());
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        return (T) this.persistExecutor.execute(commandContext, CommandType.QUERY_SINGLE_RESULT);
    }

    @Override
    public T firstResult() {
        Page<T> page = this.pageList(1, 1, false);
        return page != null && page.getList() != null && !page.getList().isEmpty() ? page.getList().iterator().next() : null;
    }

    @SuppressWarnings("unchecked")
    public List<T> list() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        return (List<T>) this.persistExecutor.execute(commandContext, CommandType.QUERY_FOR_LIST);
    }

    @Override
    public Page<T> pageList(Pageable pageable, boolean isCount) {
        return this.pageList(pageable.getPageNum(), pageable.getPageSize(), isCount);
    }

    public Page<T> pageList(Pageable pageable) {
        return this.pageList(pageable.getPageNum(), pageable.getPageSize(), true);
    }

    @SuppressWarnings("unchecked")
    public Page<T> pageList(int pageNum, int pageSize) {
        return this.pageList(pageNum, pageSize, true);
    }

    @Override
    public Page<T> pageList(int pageNum, int pageSize, boolean isCount) {
        return (Page<T>) this.doPageList(pageNum, pageSize, isCount, new PageQueryHandler() {
            @Override
            public List<?> queryList(CommandContext commandContext) {
                return (List<?>) persistExecutor.execute(commandContext, CommandType.QUERY_FOR_LIST);
            }
        });
    }

}
