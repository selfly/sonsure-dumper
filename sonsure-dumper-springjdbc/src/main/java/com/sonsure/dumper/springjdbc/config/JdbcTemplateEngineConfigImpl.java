package com.sonsure.dumper.springjdbc.config;

import com.sonsure.dumper.core.config.AbstractJdbcEngineConfig;
import com.sonsure.dumper.springjdbc.persist.JdbcTemplatePersistExecutor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by liyd on 17/4/12.
 */
public class JdbcTemplateEngineConfigImpl extends AbstractJdbcEngineConfig {

    private JdbcOperations jdbcOperations;

    protected void doInit() {

        if (this.persistExecutor == null) {
            if (jdbcOperations == null) {
                jdbcOperations = new JdbcTemplate(dataSource);
            }
            this.persistExecutor = new JdbcTemplatePersistExecutor(this.jdbcOperations);
        }
    }

    public void setJdbcOperations(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }
}
