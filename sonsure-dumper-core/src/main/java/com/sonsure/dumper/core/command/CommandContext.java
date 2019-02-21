package com.sonsure.dumper.core.command;


import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 执行的命令内容
 * <p>
 * Created by liyd on 17/4/12.
 */
public class CommandContext {

    /**
     * 对应实体类，如果是native多表联查等操作可能为null
     */
    private Class<?> modelClass;

    /**
     * 返回值类型，如果是native操作又不指定，可能为null
     */
    private Class<?> resultType;

    /**
     * 主键对应field
     */
    private String pkField;

    /**
     * 主键列
     */
    private String pkColumn;

    /**
     * 命令，一般指sql
     */
    private String command;

    /**
     * 参数名称列表
     */
    private Map<String, Object> parameterMap;

    /**
     * 主键值是否有数据库生成
     */
    private boolean pkValueByDb = true;

    /**
     * 主键值，pkValueByDb=false才有
     */
    private Object pkValue;

    private String commandCase;

    public CommandContext() {
        command = "";
        //必须是有序map
        parameterMap = new LinkedHashMap<>();
    }

    public Class<?> getResolvedResultType() {
        if (resultType != null) {
            return resultType;
        }
        return modelClass;
    }

    public String getResolvedCommand() {
        if (StringUtils.equalsIgnoreCase(commandCase, "upper")) {
            return command.toUpperCase();
        } else if (StringUtils.equalsIgnoreCase(commandCase, "lower")) {
            return command.toLowerCase();
        } else {
            return command;
        }
    }

    public Set<String> getParameterNames() {
        return this.parameterMap.keySet();
    }

    public List<Object> getParameterValues() {
        List<Object> parameterValues = new ArrayList<>();
        Collection<Object> values = this.parameterMap.values();
        for (Object value : values) {
            if (value instanceof List) {
                parameterValues.addAll(((List<Object>) value));
            } else {
                parameterValues.add(value);
            }
        }
        return parameterValues;
    }


    public void addParameters(Map<String, Object> parameters) {
        this.parameterMap.putAll(parameters);
    }

    public void addParameter(String name, Object value) {
        Object param = this.parameterMap.get(name);
        if (param == null) {
            this.parameterMap.put(name, value);
        } else {
            if (param instanceof List) {
                ((List<Object>) param).add(value);
            } else {
                List<Object> list = new ArrayList<>();
                list.add(param);
                list.add(value);
                this.parameterMap.put(name, list);
            }
        }
    }

    public void setParameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public String getCommandCase() {
        return commandCase;
    }

    public void setCommandCase(String commandCase) {
        this.commandCase = commandCase;
    }

    public String getPkField() {
        return pkField;
    }

    public void setPkField(String pkField) {
        this.pkField = pkField;
    }

    public String getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(String pkColumn) {
        this.pkColumn = pkColumn;
    }

    public Class<?> getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class<?> modelClass) {
        this.modelClass = modelClass;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isPkValueByDb() {
        return pkValueByDb;
    }

    public void setPkValueByDb(boolean pkValueByDb) {
        this.pkValueByDb = pkValueByDb;
    }

    public Object getPkValue() {
        return pkValue;
    }

    public void setPkValue(Object pkValue) {
        this.pkValue = pkValue;
    }

}
