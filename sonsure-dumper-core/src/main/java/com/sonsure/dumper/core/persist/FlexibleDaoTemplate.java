/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.persist;

import com.sonsure.dumper.core.config.AbstractJdbcEngineConfig;
import com.sonsure.dumper.core.config.JdbcEngine;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

import javax.sql.DataSource;

/**
 * The type Flexible dao template.
 *
 * @author liyd
 */
public class FlexibleDaoTemplate extends AbstractDaoTemplateImpl {

    public FlexibleDaoTemplate(JdbcEngine jdbcEngine) {
        this.setDefaultJdbcEngine(jdbcEngine);
    }

    @Override
    public DaoTemplate use(String name) {
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
