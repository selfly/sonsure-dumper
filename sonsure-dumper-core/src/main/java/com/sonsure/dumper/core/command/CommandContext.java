package com.sonsure.dumper.core.command;


import java.util.ArrayList;
import java.util.List;

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
    private List<String> parameterNames;

    /**
     * 参数值列表
     */
    private List<Object> parameters;

    /**
     * 主键值是否有数据库生成
     */
    private boolean pkValueByDb = true;

    /**
     * 主键值，pkValueByDb=false才有
     */
    private Object pkValue;

    /**
     * 大小写
     */
    private boolean commandUppercase;

    public CommandContext() {
        parameterNames = new ArrayList<String>();
        parameters = new ArrayList<Object>();
    }

    public Class<?> getResolvedResultType() {
        if (resultType != null) {
            return resultType;
        }
        return modelClass;
    }

    public String getResolvedCommand() {
        if (command == null) {
            command = "";
        }
        if (isCommandUppercase()) {
            return command.toUpperCase();
        } else {
            return command.toLowerCase();
        }
    }

    public void addParameterNames(List<String> parameterNames) {
        this.parameterNames.addAll(parameterNames);
    }

    public void addParameters(List<Object> parameters) {
        this.parameters.addAll(parameters);
    }

    public void setParameterNames(List<String> parameterNames) {
        this.parameterNames = parameterNames;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
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

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public List<Object> getParameters() {
        return parameters;
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

    public boolean isCommandUppercase() {
        return commandUppercase;
    }

    public void setCommandUppercase(boolean commandUppercase) {
        this.commandUppercase = commandUppercase;
    }
}
