package com.sonsure.dumper.springjdbc.persist;

import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.GenerateKey;
import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.persist.AbstractPersistExecutor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 17/4/12.
 */
public class JdbcTemplatePersistExecutor extends AbstractPersistExecutor {

    private JdbcOperations jdbcOperations;

    public JdbcTemplatePersistExecutor(JdbcOperations jdbcOperations, JdbcEngineConfig jdbcEngineConfig) {
        super(jdbcEngineConfig);
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    protected String doGetDialect() {
        return jdbcOperations.execute(new ConnectionCallback<String>() {
            public String doInConnection(Connection con) throws SQLException, DataAccessException {
                return con.getMetaData().getDatabaseProductName().toLowerCase();
            }
        });
    }

    @Override
    public Object insert(final CommandContext commandContext) {
        final GenerateKey generateKey = commandContext.getGenerateKey();
        //数据库生成 或没有设置主键值 处理
        if (commandContext.isPkValueByDb()) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcOperations.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(commandContext.getCommand(), new String[]{generateKey.getColumn()});
                    ArgumentPreparedStatementSetter pss = new ArgumentPreparedStatementSetter(commandContext.getParameters()
                            .toArray());
                    pss.setValues(ps);
                    return ps;
                }
            }, keyHolder);
            Number number = keyHolder.getKey();
            //可能显示设置了主键值，没有生成
            return number == null ? null : number.longValue();
        } else {
            jdbcOperations.update(commandContext.getCommand(), commandContext.getParameters().toArray());
            return generateKey.getValue();
        }
    }

    @Override
    public List<?> queryForList(CommandContext commandContext) {
        return jdbcOperations.query(commandContext.getCommand(), commandContext.getParameters().toArray(), JdbcRowMapper.newInstance(commandContext.getResultType(), getJdbcEngineConfig().getMappingHandler()));
    }

    @Override
    public Object querySingleResult(CommandContext commandContext) {
        //采用list方式查询，当记录不存在时返回null而不会抛出异常,多于一条时会抛异常
        List<?> list = jdbcOperations.query(commandContext.getCommand(), commandContext.getParameters().toArray(), JdbcRowMapper.newInstance(commandContext.getResultType(), getJdbcEngineConfig().getMappingHandler()));
        return DataAccessUtils.singleResult(list);
    }

    @Override
    public Map<String, Object> queryForMap(CommandContext commandContext) {
        //直接queryForMap没有记录时会抛出异常，采用list方式查询，当记录不存在时返回null而不会抛出异常,多于一条时会抛异常
        List<Map<String, Object>> maps = jdbcOperations.queryForList(commandContext.getCommand(), commandContext.getParameters().toArray());
        return DataAccessUtils.singleResult(maps);
    }

    @Override
    public List<Map<String, Object>> queryForMapList(CommandContext commandContext) {
        return jdbcOperations.queryForList(commandContext.getCommand(), commandContext.getParameters().toArray());
    }

    @Override
    public Object queryOneCol(CommandContext commandContext) {
        return jdbcOperations.queryForObject(commandContext.getCommand(), commandContext.getParameters().toArray(), commandContext.getResultType());
    }

    @Override
    public List<?> queryOneColList(CommandContext commandContext) {
        return jdbcOperations.queryForList(commandContext.getCommand(), commandContext.getResultType(), commandContext.getParameters().toArray());
    }

    @Override
    public int update(CommandContext commandContext) {
        return jdbcOperations.update(commandContext.getCommand(), commandContext.getParameters().toArray());
    }

    @Override
    public int delete(CommandContext commandContext) {
        return jdbcOperations.update(commandContext.getCommand(), commandContext.getParameters().toArray());
    }

    @Override
    public Object doExecute(CommandContext commandContext) {
        return this.update(commandContext);
    }
}
