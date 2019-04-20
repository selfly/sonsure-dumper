package com.sonsure.dumper.core.command.simple;

import com.sonsure.dumper.core.command.ExecutorContext;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class SimpleExecutorContext implements ExecutorContext {

    protected String command;
    protected List<Object> parameters;

    public SimpleExecutorContext() {
        this.parameters = new ArrayList<>();
    }

    @Override
    public Class<?>[] getModelClasses() {
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    }

    public void addParameter(Object value) {
        this.parameters.add(value);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }
}
