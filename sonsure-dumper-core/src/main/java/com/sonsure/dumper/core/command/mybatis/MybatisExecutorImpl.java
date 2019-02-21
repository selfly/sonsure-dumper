package com.sonsure.dumper.core.command.mybatis;


import com.sonsure.dumper.core.command.simple.AbstractSimpleCommandExecutor;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;

import java.util.Map;

/**
 * Created by liyd on 17/4/25.
 */
public class MybatisExecutorImpl extends AbstractSimpleCommandExecutor<MybatisExecutor> implements MybatisExecutor {


    public MybatisExecutorImpl(MappingHandler mappingHandler, PageHandler pageHandler, KeyGenerator keyGenerator, PersistExecutor persistExecutor, String commandCase) {
        super(mappingHandler, pageHandler, keyGenerator, persistExecutor, commandCase);
    }

    @Override
    public MybatisExecutor parameters(Map<String, Object> parameters) {
        if (parameters != null) {
            this.parameters.putAll(parameters);
        }
        return this;
    }

}
