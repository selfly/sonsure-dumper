package com.sonsure.dumper.core.persist;

import com.sonsure.dumper.core.config.AbstractJdbcEngineConfig;
import com.sonsure.dumper.core.config.JdbcEngine;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

import javax.sql.DataSource;

public class FlexibleJdbcDao extends AbstractJdbcDaoImpl {

    public FlexibleJdbcDao(JdbcEngine jdbcEngine) {
        this.setDefaultJdbcEngine(jdbcEngine);
    }

    @Override
    public JdbcEngineConfig getDefaultJdbcEngineConfig() {
        return getDefaultJdbcEngine().getJdbcEngineConfig();
    }

    @Override
    public JdbcDao use(String name) {
        throw new UnsupportedOperationException("不支持的方法");
    }

    @Override
    public DataSource getDataSource() {
        JdbcEngineConfig jdbcEngineConfig = getDefaultJdbcEngine().getJdbcEngineConfig();
        if (jdbcEngineConfig instanceof AbstractJdbcEngineConfig) {
            return ((AbstractJdbcEngineConfig) jdbcEngineConfig).getDataSource();
        }
        throw new UnsupportedOperationException("不支持的方法");
    }

}
