package com.sonsure.dumper.springjdbc.persist;


import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.config.JdbcEngineImpl;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.persist.AbstractJdbcDaoImpl;
import com.sonsure.dumper.springjdbc.config.JdbcTemplateEngineConfigImpl;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by liyd on 17/4/12.
 */
public class JdbcTemplateDaoImpl extends AbstractJdbcDaoImpl implements InitializingBean {

    public JdbcEngineConfig getDefaultJdbcEngineConfig() {
        JdbcTemplateEngineConfigImpl jdbcTemplateEngineConfig = new JdbcTemplateEngineConfigImpl();
        jdbcTemplateEngineConfig.setDataSource(getDataSource());
        return jdbcTemplateEngineConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (jdbcEngine == null) {
            if (dataSource == null) {
                throw new SonsureJdbcException("dataSource不能为空");
            }
            jdbcEngine = new JdbcEngineImpl(this.getDefaultJdbcEngineConfig());
        }
    }
}
