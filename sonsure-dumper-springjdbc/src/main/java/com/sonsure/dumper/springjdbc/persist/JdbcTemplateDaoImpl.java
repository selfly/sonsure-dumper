package com.sonsure.dumper.springjdbc.persist;


import com.sonsure.dumper.core.config.JdbcEngineImpl;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.persist.AbstractJdbcDaoImpl;
import com.sonsure.dumper.springjdbc.config.JdbcTemplateEngineConfigImpl;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by liyd on 17/4/12.
 */
public class JdbcTemplateDaoImpl extends AbstractJdbcDaoImpl implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        if (defaultJdbcEngine == null) {
            if (dataSource == null) {
                throw new SonsureJdbcException("defaultJdbcEngine和dataSource不能同时为空");
            }
            JdbcTemplateEngineConfigImpl jdbcTemplateEngineConfig = new JdbcTemplateEngineConfigImpl();
            jdbcTemplateEngineConfig.setDataSource(getDataSource());
            defaultJdbcEngine = new JdbcEngineImpl(jdbcTemplateEngineConfig);
        }
        if (isGlobalJdbc()) {
            this.enableGlobalJdbc();
        }
    }
}
