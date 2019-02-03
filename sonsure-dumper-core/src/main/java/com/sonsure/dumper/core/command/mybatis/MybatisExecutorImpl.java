package com.sonsure.dumper.core.command.mybatis;


import com.sonsure.dumper.core.command.simple.AbstractSimpleCommandExecutor;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyd on 17/4/25.
 */
public class MybatisExecutorImpl extends AbstractSimpleCommandExecutor<MybatisExecutor> implements MybatisExecutor {

    private Map<String, Object> parameters;

    public MybatisExecutorImpl(MappingHandler mappingHandler, PageHandler pageHandler, KeyGenerator keyGenerator, PersistExecutor persistExecutor, boolean commandUppercase) {
        super(mappingHandler, pageHandler, keyGenerator, persistExecutor, commandUppercase);
        parameters = new HashMap<>();
    }

    @Override
    public MybatisExecutor parameters(Map<String, Object> parameters) {
        if (parameters != null) {
            this.parameters.putAll(parameters);
        }
        return this;
    }

    @Override
    public MybatisExecutor parameter(String name, Object value) {
        this.parameters.put(name, value);
        return this;
    }

    @Override
    protected Object getParameters() {
        return this.parameters;
    }
}
