package com.sonsure.dumper.core.config;

import com.sonsure.dumper.core.exception.SonsureJdbcException;
import org.springframework.beans.factory.FactoryBean;

/**
 * JdbcEngine FactoryBean，使用Spring时使用
 * 使用该基础FactoryBean必须注入persistExecutor
 * @author liyd
 */
public class DefaultJdbcEngineFactoryBean extends DefaultJdbcEngineConfigImpl implements FactoryBean<JdbcEngine> {

    @Override
    public JdbcEngine getObject() throws Exception {
        if (this.getPersistExecutor() == null) {
            throw new SonsureJdbcException("persistExecutor不能为空");
        }
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
