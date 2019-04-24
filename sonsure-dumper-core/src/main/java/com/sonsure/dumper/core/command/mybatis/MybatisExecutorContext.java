package com.sonsure.dumper.core.command.mybatis;

import com.sonsure.dumper.core.command.simple.SimpleExecutorContext;

import java.util.HashMap;
import java.util.Map;

public class MybatisExecutorContext extends SimpleExecutorContext {

    protected Map<String, Object> parameters;

    public MybatisExecutorContext() {
        parameters = new HashMap<>();
    }

    public void addParameter(String name, Object value) {
        this.parameters.put(name, value);
    }

    public void addParameters(Map<String, Object> parameters) {
        this.parameters.putAll(parameters);
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
