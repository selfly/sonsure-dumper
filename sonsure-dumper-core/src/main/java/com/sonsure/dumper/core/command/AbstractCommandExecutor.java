package com.sonsure.dumper.core.command;


import com.sonsure.commons.bean.BeanKit;
import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pagination;
import com.sonsure.dumper.core.command.simple.ResultHandler;
import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.exception.SonsureJdbcException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyd on 17/4/19.
 */
public abstract class AbstractCommandExecutor implements CommandExecutor {

    protected CommandContextBuilder commandContextBuilder;

    protected JdbcEngineConfig jdbcEngineConfig;

    public AbstractCommandExecutor(JdbcEngineConfig jdbcEngineConfig) {
        this.jdbcEngineConfig = jdbcEngineConfig;
    }

    public void setCommandContextBuilder(CommandContextBuilder commandContextBuilder) {
        this.commandContextBuilder = commandContextBuilder;
    }

    protected Page<?> doPageResult(CommandContext commandContext, Pagination pagination, boolean isCount, PageQueryHandler pageQueryHandler) {
        if (pagination == null) {
            throw new SonsureJdbcException("查询分页列表请设置分页信息");
        }
        String dialect = getJdbcEngineConfig().getPersistExecutor().getDialect();
        long count = Long.MAX_VALUE;
        if (isCount) {
            String countCommand = getJdbcEngineConfig().getPageHandler().getCountCommand(commandContext.getCommand(), dialect);
            CommandContext countCommandContext = BeanKit.copyProperties(new CommandContext(), commandContext);
            countCommandContext.setCommand(countCommand);
            countCommandContext.setResultType(Long.class);
            Object result = getJdbcEngineConfig().getPersistExecutor().execute(countCommandContext, CommandType.QUERY_ONE_COL);
            count = (Long) result;
        }
        pagination.setTotalItems((int) count);
        String pageCommand = getJdbcEngineConfig().getPageHandler().getPageCommand(commandContext.getCommand(), pagination, dialect);
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

    protected <E> Page<E> handleResult(Page<?> page, ResultHandler<E> resultHandler) {
        Page<E> newPage = new Page<>(page.getPagination());
        if (page.getList() == null || page.getList().isEmpty()) {
            return newPage;
        }
        List<E> resultList = new ArrayList<>();
        for (Object obj : page.getList()) {
            E e = this.handleResult(obj, resultHandler);
            resultList.add(e);
        }
        newPage.setList(resultList);
        return newPage;
    }

    public CommandContextBuilder getCommandContextBuilder() {
        return commandContextBuilder;
    }

    public JdbcEngineConfig getJdbcEngineConfig() {
        return jdbcEngineConfig;
    }

    protected interface PageQueryHandler {

        List<?> queryList(CommandContext commandContext);
    }
}
