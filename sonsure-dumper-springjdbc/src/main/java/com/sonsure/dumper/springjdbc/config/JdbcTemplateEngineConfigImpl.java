package com.sonsure.dumper.springjdbc.config;

import com.sonsure.dumper.core.config.AbstractJdbcEngineConfig;
import com.sonsure.dumper.core.persist.PersistExecutor;
import com.sonsure.dumper.springjdbc.persist.JdbcTemplatePersistExecutor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by liyd on 17/4/12.
 */
public class JdbcTemplateEngineConfigImpl extends AbstractJdbcEngineConfig {

    private JdbcOperations jdbcOperations;

    @Override
    protected PersistExecutor initPersistExecutor() {
        if (jdbcOperations == null) {
            jdbcOperations = new JdbcTemplate(dataSource);
        }
        JdbcTemplatePersistExecutor jdbcTemplatePersistExecutor = new JdbcTemplatePersistExecutor(this.jdbcOperations, this);
        return jdbcTemplatePersistExecutor;
    }

    public void setJdbcOperations(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }
}
