package com.sonsure.dumper.core.command;


import com.sonsure.commons.bean.BeanKit;
import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pagination;
import com.sonsure.dumper.core.command.simple.ResultHandler;
import com.sonsure.dumper.core.management.CommandTable;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyd on 17/4/19.
 */
public abstract class AbstractCommandExecutor implements CommandExecutor {

    protected CommandTable commandTable;

    protected CommandContextBuilder commandContextBuilder;

    /**
     * 以下属性若不设置，默认使用jdbcEngineConfig中配置
     **/

    protected MappingHandler mappingHandler;

    protected PageHandler pageHandler;

    protected KeyGenerator keyGenerator;

    protected PersistExecutor persistExecutor;

    public AbstractCommandExecutor(MappingHandler mappingHandler, PageHandler pageHandler, KeyGenerator keyGenerator, PersistExecutor persistExecutor, String commandCase) {
        commandTable = new CommandTable();
        commandTable.setCommandCase(commandCase);
        this.setMappingHandler(mappingHandler);
        this.setPageHandler(pageHandler);
        this.setKeyGenerator(keyGenerator);
        this.setPersistExecutor(persistExecutor);
    }

    public void setModelClass(Class<?> modelClass) {
        this.commandTable.setModelClass(modelClass);
    }

    public void setMappingHandler(MappingHandler mappingHandler) {
        this.mappingHandler = mappingHandler;
    }

    public void setCommandContextBuilder(CommandContextBuilder commandContextBuilder) {
        this.commandContextBuilder = commandContextBuilder;
    }

    public void setPersistExecutor(PersistExecutor persistExecutor) {
        this.persistExecutor = persistExecutor;
    }

    public void setPageHandler(PageHandler pageHandler) {
        this.pageHandler = pageHandler;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected Page<?> doPageList(int pageNum, int pageSize, boolean isCount, PageQueryHandler pageQueryHandler) {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);

        String dialect = persistExecutor.getDialect();
        long count = Long.MAX_VALUE;
        if (isCount) {
            String countCommand = this.pageHandler.getCountCommand(commandContext.getCommand(), dialect);
            CommandContext countCommandContext = BeanKit.copyProperties(new CommandContext(), commandContext);
            countCommandContext.setCommand(countCommand);
            countCommandContext.setResultType(Long.class);
            Object result = this.persistExecutor.execute(countCommandContext, CommandType.QUERY_ONE_COL);
            count = (Long) result;
        }


        Pagination pagination = new Pagination();
        pagination.setPageSize(pageSize);
        pagination.setPageNum(pageNum);
        pagination.setTotalItems((int) count);

        String pageCommand = this.pageHandler.getPageCommand(commandContext.getCommand(), pagination, dialect);
        CommandContext pageCommandContext = BeanKit.copyProperties(new CommandContext(), commandContext);
        pageCommandContext.setCommand(pageCommand);
        List<?> list = pageQueryHandler.queryList(pageCommandContext);

        return new Page<>(list, pagination);
    }

    protected <E> E handleResult(Object result, ResultHandler<E> resultHandler) {
        if (result == null) {
            return null;
        }
        return resultHandler.handle(result);
    }

    protected <E> List<E> handleResult(List<?> result, ResultHandler<E> resultHandler) {
        if (result == null) {
            return null;
        }
        List<E> resultList = new ArrayList<>();
        for (Object obj : result) {
            E e = this.handleResult(obj, resultHandler);
            resultList.add(e);
        }
        return resultList;
    }


    public CommandTable getCommandTable() {
        return commandTable;
    }

    public void setCommandTable(CommandTable commandTable) {
        this.commandTable = commandTable;
    }

    public CommandContextBuilder getCommandContextBuilder() {
        return commandContextBuilder;
    }

    public PersistExecutor getPersistExecutor() {
        return persistExecutor;
    }

    public MappingHandler getMappingHandler() {
        return mappingHandler;
    }

    public PageHandler getPageHandler() {
        return pageHandler;
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    protected interface PageQueryHandler {

        List<?> queryList(CommandContext commandContext);
    }
}
