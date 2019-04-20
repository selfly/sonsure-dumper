package com.sonsure.dumper.springjdbc.config;

import com.sonsure.dumper.core.config.JdbcEngine;
import com.sonsure.dumper.core.config.JdbcEngineImpl;
import org.springframework.beans.factory.FactoryBean;

public class JdbcTemplateEngineFactoryBean extends JdbcTemplateEngineConfigImpl implements FactoryBean<JdbcEngine> {

    @Override
    public JdbcEngine getObject() throws Exception {
        return new JdbcEngineImpl(this);
    }

    @Override
    public Class<?> getObjectType() {
        return JdbcEngine.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
