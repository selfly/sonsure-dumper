package com.sonsure.dumper.core.command.natives;

import com.sonsure.dumper.core.command.simple.SimpleExecutorContext;

import java.util.ArrayList;
import java.util.List;

public class NativeExecutorContext extends SimpleExecutorContext {

    protected List<Object> parameters;

    public NativeExecutorContext() {
        parameters = new ArrayList<>();
    }

    public void addParameter(Object value) {
        this.parameters.add(value);
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }
}
