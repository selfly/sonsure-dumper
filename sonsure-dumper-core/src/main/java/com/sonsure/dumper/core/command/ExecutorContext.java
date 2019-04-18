package com.sonsure.dumper.core.command;

/**
 * Created by liyd on 17/4/11.
 */
public class ExecutorContext {

//    /**
//     * 实体类型
//     */
//    private Class<?> modelClass;
//
//    /**
//     * 返回结果类型
//     */
//    private Class<?> resultType;
//
//    /**
//     * 表名
//     */
//    private String tableName;
//
//    /**
//     * 表别名
//     */
//    private String tableAlias;
//
//    /**
//     * 操作的属性
//     */
//    private List<ClassField> operationFields;
//
//    /**
//     * where的属性/列
//     */
//    private List<ClassField> whereFields;
//
//    /**
//     * 白名单
//     */
//    private List<String> includeFields;
//
//    /**
//     * 黑名单
//     */
//    private List<String> excludeFields;
//
//    /**
//     * group by属性
//     */
//    private List<ClassField> groupByFields;
//
//    /**
//     * 排序属性
//     */
//    private List<ClassField> orderByFields;
//
//    /**
//     * 是否不查询实体属性
//     */
//    private boolean isNotSelectEntityField = false;
//
//    /**
//     * 是否忽略null值
//     */
//    private boolean isIgnoreNull = true;
//
//    /**
//     * 是否强制native simple command 有用
//     */
//    private boolean isForceNative = false;
//
//    /**
//     * 一些扩展的数据
//     */
//    private Map<String, Object> extendData;
//
//    private String commandCase;
//
//    public ExecutorContext() {
//        operationFields = new ArrayList<ClassField>();
//        whereFields = new ArrayList<ClassField>();
//        includeFields = new ArrayList<String>();
//        excludeFields = new ArrayList<String>();
//        groupByFields = new ArrayList<ClassField>();
//        orderByFields = new ArrayList<ClassField>();
//    }
//
//    public enum ExtendDataKey {
//
//        COMMAND,
//
//        PARAMETERS
//    }
//
//    /**
//     * 添加数据
//     *
//     * @param key
//     * @param value
//     */
//    public void addExtendData(String key, Object value) {
//        if (this.extendData == null) {
//            this.extendData = new HashMap<String, Object>();
//        }
//        this.extendData.put(key, value);
//    }
//
//    /**
//     * 获取数据
//     *
//     * @param key
//     * @return
//     */
//    public Object getExtendData(String key) {
//        return this.extendData == null ? null : this.extendData.get(key);
//    }
//
//    /**
//     * 添加操作的属性
//     *
//     * @param commandField
//     */
//    public void addOperationField(ClassField commandField) {
//        this.operationFields.add(commandField);
//    }
//
//    /**
//     * 添加操作属性
//     */
//    public void addWhereField(ClassField commandField) {
//        this.whereFields.add(commandField);
//    }
//
//    /**
//     * 设置排序
//     *
//     * @param sort the sort
//     */
//    public void setOrderBy(String sort) {
//        List<ClassField> orderByFields = this.getOrderByFields();
//        if (orderByFields.size() == 0) {
//            throw new SonsureJdbcException("请先指定需要排序的属性");
//        }
//        for (int i = orderByFields.size() - 1; i >= 0; i--) {
//            ClassField commandField = orderByFields.get(i);
//            if (commandField.getType() != ClassField.Type.ORDER_BY || StringUtils.isNotBlank(commandField.getFieldOperator())) {
//                break;
//            }
//            commandField.setFieldOperator(sort);
//        }
//    }
//
//    /**
//     * 添加排序属性
//     *
//     * @param commandField
//     */
//    public void addOrderByField(ClassField commandField) {
//        this.orderByFields.add(commandField);
//    }
//
//    /**
//     * 添加group by属性
//     *
//     * @param commandField
//     */
//    public void addGroupByField(ClassField commandField) {
//        this.groupByFields.add(commandField);
//    }
//
//    /**
//     * 添加白名单属性
//     *
//     * @param fields
//     */
//    public void addIncludeFields(String... fields) {
//        for (String field : fields) {
//            this.includeFields.add(field);
//        }
//    }
//
//    /**
//     * 添加黑名单属性
//     *
//     * @param fields
//     */
//    public void addExcludeFields(String... fields) {
//        for (String field : fields) {
//            this.excludeFields.add(field);
//        }
//    }
//
//    /**
//     * 是否白名单属性
//     *
//     * @param field
//     * @return
//     */
//    public boolean isIncludeField(String field) {
//        if (this.includeFields == null || this.includeFields.isEmpty()) {
//            //名单为空，默认全是白名单
//            return true;
//        }
//        return this.includeFields.contains(field);
//    }
//
//    /**
//     * 是否黑名单
//     *
//     * @param field
//     * @return
//     */
//    public boolean isExcludeField(String field) {
//        if (this.excludeFields == null || this.excludeFields.isEmpty()) {
//            return false;
//        }
//        return this.excludeFields.contains(field);
//    }
//
//    public String getTableName() {
//        return tableName;
//    }
//
//    public void setTableName(String tableName) {
//        this.tableName = tableName;
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
//    public List<ClassField> getOperationFields() {
//        return operationFields;
//    }
//
//    public List<ClassField> getWhereFields() {
//        return whereFields;
//    }
//
//    public boolean isNotSelectEntityField() {
//        return isNotSelectEntityField;
//    }
//
//    public void setNotSelectEntityField(boolean notSelectEntityField) {
//        isNotSelectEntityField = notSelectEntityField;
//    }
//
//    public boolean isIgnoreNull() {
//        return isIgnoreNull;
//    }
//
//    public void setIgnoreNull(boolean ignoreNull) {
//        isIgnoreNull = ignoreNull;
//    }
//
//    public String getTableAlias() {
//        return tableAlias;
//    }
//
//    public List<ClassField> getGroupByFields() {
//        return groupByFields;
//    }
//
//    public List<ClassField> getOrderByFields() {
//        return orderByFields;
//    }
//
//    public void setTableAlias(String tableAlias) {
//        this.tableAlias = tableAlias;
//    }
//
//    public boolean isForceNative() {
//        return isForceNative;
//    }
//
//    public void setForceNative(boolean forceNative) {
//        isForceNative = forceNative;
//    }
//
//
//    public String getCommandCase() {
//        return commandCase;
//    }
//
//    public void setCommandCase(String commandCase) {
//        this.commandCase = commandCase;
//    }
}
