package com.sonsure.dumper.springjdbc.persist;


import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.persist.AbstractJdbcDaoImpl;
import com.sonsure.dumper.springjdbc.config.JdbcTemplateEngineConfigImpl;

/**
 * Created by liyd on 17/4/12.
 */
public class JdbcTemplateDaoImpl extends AbstractJdbcDaoImpl {

    public JdbcEngineConfig getDefaultJdbcEngineConfig() {
        return new JdbcTemplateEngineConfigImpl();
    }
}
