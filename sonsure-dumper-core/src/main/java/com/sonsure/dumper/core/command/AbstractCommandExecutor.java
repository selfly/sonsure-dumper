package com.sonsure.dumper.core.command;


import com.sonsure.dumper.core.command.simple.ResultHandler;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

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

//    @SuppressWarnings({"rawtypes", "unchecked"})
//    protected Page<?> doPageList(int pageNum, int pageSize, boolean isCount, PageQueryHandler pageQueryHandler) {
//        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
//
//        String dialect = persistExecutor.getDialect();
//        long count = Long.MAX_VALUE;
//        if (isCount) {
//            String countCommand = this.pageHandler.getCountCommand(commandContext.getCommand(), dialect);
//            CommandContext countCommandContext = BeanKit.copyProperties(new CommandContext(), commandContext);
//            countCommandContext.setCommand(countCommand);
//            countCommandContext.setResultType(Long.class);
//            Object result = this.persistExecutor.execute(countCommandContext, CommandType.QUERY_ONE_COL);
//            count = (Long) result;
//        }
//
//
//        Pagination pagination = new Pagination();
//        pagination.setPageSize(pageSize);
//        pagination.setPageNum(pageNum);
//        pagination.setTotalItems((int) count);
//
//        String pageCommand = this.pageHandler.getPageCommand(commandContext.getCommand(), pagination, dialect);
//        CommandContext pageCommandContext = BeanKit.copyProperties(new CommandContext(), commandContext);
//        pageCommandContext.setCommand(pageCommand);
//        List<?> list = pageQueryHandler.queryList(pageCommandContext);
//
//        return new Page<>(list, pagination);
//    }

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
