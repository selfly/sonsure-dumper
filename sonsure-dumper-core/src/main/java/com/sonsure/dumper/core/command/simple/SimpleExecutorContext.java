package com.sonsure.dumper.core.command.simple;

import com.sonsure.dumper.core.command.ExecutorContext;

import java.util.ArrayList;
import java.util.List;

public class SimpleExecutorContext extends ExecutorContext {

    protected String command;
    protected List<Object> parameters;

    public SimpleExecutorContext() {
        this.parameters = new ArrayList<>();
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
