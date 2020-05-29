/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.persist;


import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.command.batch.BatchCommandContext;
import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * The type Abstract persist executor.
 *
 * @author liyd
 * @date 17 /4/12
 */
public abstract class AbstractPersistExecutor implements PersistExecutor {

    protected String dialect;

    protected boolean forceObtainDialect = false;

    protected JdbcEngineConfig jdbcEngineConfig;

    public AbstractPersistExecutor() {

    }

    public AbstractPersistExecutor(JdbcEngineConfig jdbcEngineConfig) {
        this.jdbcEngineConfig = jdbcEngineConfig;
    }

    @Override
    public String getDialect() {
        if (StringUtils.isBlank(dialect) || forceObtainDialect) {
            dialect = this.doGetDialect();
            return dialect;
        }
        return dialect;
    }


    @Override
    public Object execute(CommandContext commandContext, CommandType commandType) {
        switch (commandType) {
            case INSERT:
                return this.insert(commandContext);
            case QUERY_FOR_LIST:
                return this.queryForList(commandContext);
            case QUERY_SINGLE_RESULT:
                return this.querySingleResult(commandContext);
            case QUERY_FOR_MAP:
                return this.queryForMap(commandContext);
            case QUERY_FOR_MAP_LIST:
                return this.queryForMapList(commandContext);
            case QUERY_ONE_COL:
                return this.queryOneCol(commandContext);
            case QUERY_ONE_COL_LIST:
                return this.queryOneColList(commandContext);
            case UPDATE:
                return this.update(commandContext);
            case BATCH_UPDATE:
                return this.batchUpdate(((BatchCommandContext<?>) commandContext));
            case DELETE:
                return this.delete(commandContext);
            case EXECUTE:
                return this.doExecute(commandContext);
            case EXECUTE_SCRIPT:
                return this.doExecuteScript(commandContext);
            default:
                throw new SonsureJdbcException("不支持的CommandType:" + commandType);
        }
    }


    /**
     * insert操作，返回主键值
     *
     * @param commandContext the command context
     * @return object
     */
    protected abstract Object insert(CommandContext commandContext);

    /**
     * 列表查询，泛型object为某个实体对象
     *
     * @param commandContext the command context
     * @return list
     */
    protected abstract List<?> queryForList(CommandContext commandContext);

    /**
     * 查询单个结果对象，返回结果为某个实体对象
     *
     * @param commandContext the command context
     * @return object
     */
    protected abstract Object querySingleResult(CommandContext commandContext);

    /**
     * 查询单条记录的map结果集，key=列名，value=列值
     *
     * @param commandContext the command context
     * @return map
     */
    protected abstract Map<String, Object> queryForMap(CommandContext commandContext);

    /**
     * 查询列表记录的map结果集，key=列名，value=列值
     *
     * @param commandContext the command context
     * @return list
     */
    protected abstract List<Map<String, Object>> queryForMapList(CommandContext commandContext);

    /**
     * 查询某一列的值
     *
     * @param commandContext the command context
     * @return object
     */
    protected abstract Object queryOneCol(CommandContext commandContext);

    /**
     * 查询某一列的值列表
     *
     * @param commandContext the command context
     * @return list list
     */
    protected abstract List<?> queryOneColList(CommandContext commandContext);

    /**
     * 更新操作
     *
     * @param commandContext the command context
     * @return int
     */
    protected abstract int update(CommandContext commandContext);

    /**
     * 批量更新操作
     *
     * @param <T>            the type parameter
     * @param commandContext the command context
     * @return int int
     */
    protected abstract <T> Object batchUpdate(BatchCommandContext<T> commandContext);

    /**
     * 删除操作
     *
     * @param commandContext the command context
     * @return int
     */
    protected abstract int delete(CommandContext commandContext);

    /**
     * 执行代码
     *
     * @param commandContext the command context
     * @return object
     */
    protected abstract Object doExecute(CommandContext commandContext);

    /**
     * 执行代码
     *
     * @param commandContext the command context
     * @return object
     */
    protected abstract Object doExecuteScript(CommandContext commandContext);


    /**
     * Do get dialect string.
     *
     * @return the string
     */
    protected abstract String doGetDialect();

    public JdbcEngineConfig getJdbcEngineConfig() {
        return jdbcEngineConfig;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public boolean isForceObtainDialect() {
        return forceObtainDialect;
    }

    public void setForceObtainDialect(boolean forceObtainDialect) {
        this.forceObtainDialect = forceObtainDialect;
    }
}
