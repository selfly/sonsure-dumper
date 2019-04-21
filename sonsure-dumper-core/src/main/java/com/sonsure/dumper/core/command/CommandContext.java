package com.sonsure.dumper.core.command;


import java.util.ArrayList;
import java.util.List;

/**
 * 执行的命令内容
 * <p>
 * Created by liyd on 17/4/12.
 */
public class CommandContext {

//    /**
//     * 主键对应field
//     */
//    private String pkField;

    /**
     * 命令，一般指sql
     */
    private String command;

    private List<String> parameterNames;

    /**
     * 参数名称列表
     */
    private List<Object> parameters;

    /**
     * 返回值类型，如果是native操作又不指定，可能为null
     */
    private Class<?> resultType;

    /**
     * 主键值是否有数据库生成
     */
    private boolean pkValueByDb = true;

    /**
     * 主键值，pkValueByDb=false才有
     */
    private GenerateKey generateKey;

    //
//    private String commandCase;
//
    public CommandContext() {
        parameterNames = new ArrayList<>();
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

    public List<String> getParameterNames() {
        return parameterNames;
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

    //    public MappingHandler getMappingHandler() {
//        return mappingHandler;
//    }
//
//    public void setMappingHandler(MappingHandler mappingHandler) {
//        this.mappingHandler = mappingHandler;
//    }

    //
//    public Class<?> getResolvedResultType() {
//        if (resultType != null) {
//            return resultType;
//        }
//        return modelClass;
//    }
//
    public String getResolvedCommand() {
//        if (StringUtils.equalsIgnoreCase(commandCase, "upper")) {
//            return command.toUpperCase();
//        } else if (StringUtils.equalsIgnoreCase(commandCase, "lower")) {
//            return command.toLowerCase();
//        } else {
//            return command;
//        }
        return command;
    }

    //
//    public List<String> getParameterNames() {
//        return this.parameterNames;
//    }
//
//    public List<Object> getParameters() {
//        return parameters;
//    }
//
////    public void addParameters(Map<String, Object> parameters) {
////        this.parameterMap.putAll(parameters);
////    }
//
//    public void addParameter(String name, Object value) {
//        Object param = this.parameterMap.get(name);
//        if (param == null) {
//            this.parameterMap.put(name, value);
//        } else {
//            if (param instanceof List) {
//                ((List<Object>) param).add(value);
//            } else {
//                List<Object> list = new ArrayList<>();
//                list.add(param);
//                list.add(value);
//                this.parameterMap.put(name, list);
//            }
//        }
//    }
//
//    public void setParameterMap(Map<String, Object> parameterMap) {
//        this.parameterMap = parameterMap;
//    }
//
//    public String getCommandCase() {
//        return commandCase;
//    }
//
//    public void setCommandCase(String commandCase) {
//        this.commandCase = commandCase;
//    }
//
//    public String getPkField() {
//        return pkField;
//    }
//
//    public void setPkField(String pkField) {
//        this.pkField = pkField;
//    }
//
//    public String getPkColumn() {
//        return pkColumn;
//    }
//
//    public void setPkColumn(String pkColumn) {
//        this.pkColumn = pkColumn;
//    }
//
//    public Class<?> getModelClass() {
//        return modelClass;
//    }
//
//    public void setModelClass(Class<?> modelClass) {
//        this.modelClass = modelClass;
//    }
//
//    public Class<?> getResultType() {
//        return resultType;
//    }
//
//    public void setResultType(Class<?> resultType) {
//        this.resultType = resultType;
//    }
//
//    public Map<String, Object> getParameterMap() {
//        return parameterMap;
//    }
//
//    public String getCommand() {
//        return command;
//    }
//
//    public void setCommand(String command) {
//        this.command = command;
//    }
//
    public boolean isPkValueByDb() {
        return pkValueByDb;
    }

    public void setPkValueByDb(boolean pkValueByDb) {
        this.pkValueByDb = pkValueByDb;
    }
//
//    public Object getPkValue() {
//        return pkValue;
//    }
//
//    public void setPkValue(Object pkValue) {
//        this.pkValue = pkValue;
//    }
//
//    public MappingHandler getMappingHandler() {
//        return mappingHandler;
//    }
//
//    public void setMappingHandler(MappingHandler mappingHandler) {
//        this.mappingHandler = mappingHandler;
//    }
}
