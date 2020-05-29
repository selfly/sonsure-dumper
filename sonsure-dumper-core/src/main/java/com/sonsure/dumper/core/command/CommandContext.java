/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command;


import java.util.ArrayList;
import java.util.List;

/**
 * 执行的命令内容
 * <p>
 *
 * @author liyd
 * @date 17/4/12
 */
public class CommandContext {

    /**
     * 命令，一般指sql
     */
    private String command;

    /**
     * 参数列表
     */
    private List<Object> parameters;

    /**
     * 返回值类型，如果是native操作又不指定，可能为null
     */
    private Class<?> resultType;

    /**
     * 主键值，pkValueByDb=false才有
     */
    private GenerateKey generateKey;

    public CommandContext() {
        parameters = new ArrayList<>();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void addParameter(Object parameter) {
        this.parameters.add(parameter);
    }

    public void addParameters(List<Object> parameters) {
        this.parameters.addAll(parameters);
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

    public GenerateKey getGenerateKey() {
        return generateKey;
    }

    public void setGenerateKey(GenerateKey generateKey) {
        this.generateKey = generateKey;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

}
