/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   https://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command;

import com.sonsure.commons.model.Pageable;
import com.sonsure.commons.model.Pagination;
import com.sonsure.dumper.core.command.batch.ParameterizedSetter;
import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.management.ClassField;
import com.sonsure.dumper.core.management.CommandClass;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The interface Command executor context.
 *
 * @author liyd
 */
public class CommandExecutorContext {

    /**
     * The constant IS_NATIVE_COMMAND.
     */
    private static final String IS_NATIVE_COMMAND = "isNativeCommand";

    /**
     * The constant IS_NAMED_PARAMETER.
     */
    private static final String IS_NAMED_PARAMETER = "isNamedParameter";

    /**
     * The constant MODEL_CLASSES.
     */
    private static final String MODEL_CLASSES = "modelClasses";

    /**
     * The constant INSERT_FIELDS.
     */
    private static final String INSERT_FIELDS = "insertFields";

    /**
     * The constant COMMAND.
     */
    private static final String COMMAND = "command";

    /**
     * 分页查询时是否用count查询总记录数,默认true
     */
    private static final String IS_COUNT = "isCount";

    /**
     * The constant pagination.
     */
    private static final String PAGINATION = "pagination";
//
//    /**
//     * The constant PARAMETERS.
//     */
//    private static final String PARAMETERS = "parameters";
//
//    /**
//     * The constant NAMED_PARAMETERS.
//     */
//    private static final String NAMED_PARAMETERS = "namedParameters";

    /**
     * The constant COMMAND_PARAMETER.
     */
    private static final String COMMAND_PARAMETERS = "commandParameters";

    /**
     * The Context.
     */
    private Map<String, Object> context = new HashMap<>();

//    /**
//     * The Command parameters.
//     */
//    private List<CommandParameter> commandParameters;

    /**
     * The Select context.
     */
    private SelectContext selectContext;

    /**
     * The Update context.
     */
    private UpdateContext updateContext;

    /**
     * The Entity where context.
     */
    private EntityWhereContext entityWhereContext;

    /**
     * The Batch update executor context.
     */
    private BatchUpdateExecutorContext batchUpdateExecutorContext;

    public CommandExecutorContext(JdbcEngineConfig jdbcEngineConfig) {
        this.setNativeCommand(jdbcEngineConfig.isNativeCommand());
        this.setNamedParameter(jdbcEngineConfig.isNamedParameter());
    }


    /**
     * Sets command.
     *
     * @param command the command
     */
    public void setCommand(String command) {
        context.put(COMMAND, command);
    }

    /**
     * Gets command.
     *
     * @return the command
     */
    public String getCommand() {
        return ((String) context.get(COMMAND));
    }

    /**
     * Add insert field.
     *
     * @param field the field
     * @param value the value
     */
    public void addInsertField(String field, Object value) {
        List<ClassField> insertFields = this.getInsertFields();
        if (insertFields == null) {
            insertFields = new ArrayList<>();
            context.put(INSERT_FIELDS, insertFields);
        }
        ClassField classField = this.createCommandClassField(field, false);
        classField.setValue(value);
        insertFields.add(classField);
    }

    /**
     * Gets insert fields.
     *
     * @return the insert fields
     */
    public List<ClassField> getInsertFields() {
        return (List<ClassField>) context.get(INSERT_FIELDS);
    }

    /**
     * 获取实体类
     *
     * @return model classes
     */
    public Set<Class<?>> getModelClasses() {
        return (Set<Class<?>>) context.get(MODEL_CLASSES);
    }

    /**
     * Gets unique model class.
     *
     * @return the unique model class
     */
    public Class<?> getUniqueModelClass() {
        final Set<Class<?>> modelClasses = this.getModelClasses();
        if (modelClasses == null || modelClasses.size() != 1) {
            throw new SonsureJdbcException("当前执行业务不止一个Model Class");
        }
        return modelClasses.iterator().next();
    }


    /**
     * Add model class.
     *
     * @param cls the cls
     */
    public void addModelClass(Class<?> cls) {
        Set<Class<?>> classes = this.getModelClasses();
        if (classes == null) {
            classes = new HashSet<>();
            context.put(MODEL_CLASSES, classes);
        }
        classes.add(cls);
    }

    /**
     * Is native command boolean.
     *
     * @return the boolean
     */
    public boolean isNativeCommand() {
        return this.getBooleanVal(IS_NATIVE_COMMAND);
    }

    /**
     * Sets native command.
     *
     * @param nativeCommand the native command
     */
    public void setNativeCommand(boolean nativeCommand) {
        context.put(IS_NATIVE_COMMAND, nativeCommand);
    }

    /**
     * Is named parameter boolean.
     *
     * @return the boolean
     */
    public boolean isNamedParameter() {
        return this.getBooleanVal(IS_NAMED_PARAMETER);
    }

    /**
     * Sets named parameter.
     *
     * @param namedParameter the named parameter
     */
    public void setNamedParameter(boolean namedParameter) {
        context.put(IS_NAMED_PARAMETER, namedParameter);
    }


    /**
     * Paginate.
     *
     * @param pageNum  the page num
     * @param pageSize the page size
     */
    public void paginate(int pageNum, int pageSize) {
        Pagination pagination = new Pagination();
        pagination.setPageSize(pageSize);
        pagination.setPageNum(pageNum);
        context.put(PAGINATION, pagination);
    }

    /**
     * Paginate.
     *
     * @param pageable the pageable
     */
    public void paginate(Pageable pageable) {
        this.paginate(pageable.getPageNum(), pageable.getPageSize());
    }

    /**
     * Limit.
     *
     * @param offset the offset
     * @param size   the size
     */
    public void limit(int offset, int size) {
        Pagination pagination = new Pagination();
        pagination.setPageSize(size);
        pagination.setOffset(offset);
        context.put(PAGINATION, pagination);
    }

    /**
     * Is count boolean.
     *
     * @return the boolean
     */
    public boolean isCount() {
        final Boolean isCount = (Boolean) context.get(IS_COUNT);
        return isCount == null ? true : isCount;
    }

    /**
     * Sets count.
     *
     * @param count the count
     */
    public void setCount(boolean count) {
        context.put(IS_COUNT, count);
    }

    /**
     * Gets pagination.
     *
     * @return the pagination
     */
    public Pagination getPagination() {
        return ((Pagination) context.get(PAGINATION));
    }


    public void addCommandParameter(String name, Object value) {
        final List<CommandParameter> commandParameters = this.getCommandParameters();
        commandParameters.add(new CommandParameter(name, value));
    }

//    public void addCommandParameter(Map<String, Object> parameters) {
//        Map<String, Object> namedParams = this.getNamedParameters();
//        if (namedParams == null) {
//            namedParams = new HashMap<>();
//            context.put(NAMED_PARAMETERS, namedParams);
//        }
//        namedParams.putAll(parameters);
//    }

    public Object getParameters() {
        if (isNamedParameter()) {
            return getCommandParameters().stream()
                    .collect(Collectors.toMap(CommandParameter::getName, CommandParameter::getValue, (v1, v2) -> v1));
        } else {
            return getCommandParameters().stream()
                    .map(CommandParameter::getValue)
                    .collect(Collectors.toList());
        }
    }

    public List<CommandParameter> getCommandParameters() {
        return this.getOrInitIfAbsentList(COMMAND_PARAMETERS);
    }

    public SelectContext selectContext() {
        if (this.selectContext == null) {
            this.selectContext = new SelectContext();
        }
        return this.selectContext;
    }

    public UpdateContext updateContext() {
        if (this.updateContext == null) {
            this.updateContext = new UpdateContext();
        }
        return this.updateContext;
    }

    public EntityWhereContext entityWhereContext() {
        if (this.entityWhereContext == null) {
            this.entityWhereContext = new EntityWhereContext();
        }
        return this.entityWhereContext;
    }

    public <T> BatchUpdateExecutorContext<T> batchUpdateExecutorContext() {
        if (this.batchUpdateExecutorContext == null) {
            this.batchUpdateExecutorContext = new BatchUpdateExecutorContext<>();
        }
        return this.batchUpdateExecutorContext;
    }

    private <T> List<T> getOrInitIfAbsentList(String key) {
        List<T> list = (List<T>) context.get(key);
        if (list == null) {
            list = new ArrayList<>();
            context.put(key, list);
        }
        return list;
    }

    /**
     * Gets boolean val.
     *
     * @param key the key
     * @return the boolean val
     */
    private boolean getBooleanVal(String key) {
        final Boolean value = (Boolean) context.get(key);
        return value != null && value;
    }

    /**
     * Create command class command class.
     *
     * @param cls       the cls
     * @param aliasName the alias name
     * @return the command class
     */
    private CommandClass createCommandClass(Class<?> cls, String aliasName) {
        return new CommandClass(cls, aliasName);
    }

    /**
     * Create class field class field.
     *
     * @param name              the name
     * @param analyseTableAlias the analyse table alias
     * @return the class field
     */
    public ClassField createCommandClassField(String name, boolean analyseTableAlias) {
        return new ClassField(name, analyseTableAlias);
    }

    public class SelectContext {

        /**
         * The constant SELECT_FIELDS.
         */
        private static final String SELECT_FIELDS = "selectFields";

        /**
         * The constant FROM_CLASSES.
         */
        private static final String FROM_CLASSES = "fromClasses";

        /**
         * The constant EXCLUDE_FIELDS.
         */
        private static final String EXCLUDE_FIELDS = "excludeFields";

        /**
         * The constant GROUP_BY_FIELDS.
         */
        private static final String GROUP_BY_FIELDS = "groupByFields";

        /**
         * The constant ORDER_BY_FIELD.
         */
        private static final String ORDER_BY_FIELDS = "orderByFields";

        public void addSelectFields(String[] fields) {
            if (ArrayUtils.isEmpty(fields)) {
                return;
            }
            List<ClassField> selectFields = this.getSelectFields();
            for (String field : fields) {
                selectFields.add(new ClassField(field, true));
            }
        }

        public List<ClassField> getSelectFields() {
            return CommandExecutorContext.this.getOrInitIfAbsentList(SELECT_FIELDS);
        }

        public void addFromClass(Class<?> cls) {
            this.addFromClass(cls, null);
        }

        public void addFromClass(Class<?> cls, String aliasName) {
            final List<CommandClass> fromClasses = this.getFromClasses();
            fromClasses.add(CommandExecutorContext.this.createCommandClass(cls, aliasName));
        }

        public List<CommandClass> getFromClasses() {
            return CommandExecutorContext.this.getOrInitIfAbsentList(FROM_CLASSES);
        }

        public List<ClassField> getExcludeFields() {
            return CommandExecutorContext.this.getOrInitIfAbsentList(EXCLUDE_FIELDS);
        }

        public void addExcludeFields(String... fields) {
            final List<ClassField> excludeFields = this.getExcludeFields();
            for (String field : fields) {
                excludeFields.add(CommandExecutorContext.this.createCommandClassField(field, true));
            }
        }

        public List<ClassField> getGroupByFields() {
            return CommandExecutorContext.this.getOrInitIfAbsentList(GROUP_BY_FIELDS);
        }

        public void addGroupByField(String... fields) {
            final List<ClassField> groupByFields = this.getGroupByFields();
            for (String field : fields) {
                groupByFields.add(CommandExecutorContext.this.createCommandClassField(field, true));
            }
        }

        public List<ClassField> getOrderByFields() {
            return CommandExecutorContext.this.getOrInitIfAbsentList(ORDER_BY_FIELDS);
        }

        public void addOrderByField(String... fields) {
            final List<ClassField> orderByFields = this.getOrderByFields();
            for (String field : fields) {
                orderByFields.add(new ClassField(field, true));
            }
        }

        public void setOrderByType(String type) {
            final List<ClassField> orderByFields = this.getOrderByFields();
            if (orderByFields.isEmpty()) {
                throw new SonsureJdbcException("请先指定需要排序的属性");
            }
            int size = orderByFields.size();
            for (int i = size - 1; i >= 0; i--) {
                ClassField classField = orderByFields.get(i);
                if (StringUtils.isNotBlank(classField.getFieldOperator())) {
                    //已经指定了，跳出
                    break;
                }
                classField.setFieldOperator(type);
            }
        }


        /**
         * 是否黑名单
         *
         * @param field
         * @return
         */
        public boolean isExcludeField(String field) {
            final List<ClassField> excludeFields = this.getExcludeFields();
            if (excludeFields == null || excludeFields.isEmpty()) {
                return false;
            }
            ClassField classField = new ClassField(field, true);
            for (ClassField excludeField : excludeFields) {
                if (StringUtils.equals(classField.getTableAlias(), excludeField.getTableAlias()) && StringUtils.equals(classField.getName(), excludeField.getName())) {
                    return true;
                }
            }
            return false;
        }
    }

    public class UpdateContext {

        /**
         * 是否忽略null值
         */
        private boolean isIgnoreNull = true;

        /**
         * set的属性
         */
        protected List<ClassField> setFields;

        public UpdateContext() {
            setFields = new ArrayList<>();
        }

        public void addSetField(String field, Object value) {
            ClassField classField = CommandExecutorContext.this.createCommandClassField(field, true);
            classField.setValue(value);
            this.setFields.add(classField);
        }

        public boolean isIgnoreNull() {
            return isIgnoreNull;
        }

        public void setIgnoreNull(boolean ignoreNull) {
            isIgnoreNull = ignoreNull;
        }

        public List<ClassField> getSetFields() {
            return setFields;
        }

    }

    public class EntityWhereContext {

        /**
         * The constant WHERE_FIELDS.
         */
        private static final String WHERE_FIELDS = "whereFields";


        public void addWhereField(String logicalOperator, String name, String fieldOperator, Object value, ClassField.Type type) {
            boolean analyseTableAlias = ClassField.Type.isAnalyseTableAlias(type);
            ClassField classField = CommandExecutorContext.this.createCommandClassField(name, analyseTableAlias);
            classField.setLogicalOperator(logicalOperator);
            classField.setFieldOperator(fieldOperator);
            classField.setValue(value);
            classField.setType(type);
            this.getWhereFields().add(classField);
        }

        public void addWhereFields(List<ClassField> classFields) {
            this.getWhereFields().addAll(classFields);
        }

        public void addWhereField(String logicalOperator, String name, String fieldOperator, Object value) {
            this.addWhereField(logicalOperator, name, fieldOperator, value, null);
        }

        public List<ClassField> getWhereFields() {
            return CommandExecutorContext.this.getOrInitIfAbsentList(WHERE_FIELDS);
        }

    }

    public class BatchUpdateExecutorContext<T> {

        /**
         * The Batch data.
         */
        private Collection<T> batchData;

        /**
         * The Batch size.
         */
        private int batchSize;

        /**
         * The Parameterized setter.
         */
        private ParameterizedSetter<T> parameterizedSetter;

        public Collection<T> getBatchData() {
            return batchData;
        }

        public void setBatchData(Collection<T> batchData) {
            this.batchData = batchData;
        }

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }

        public ParameterizedSetter<T> getParameterizedSetter() {
            return parameterizedSetter;
        }

        public void setParameterizedSetter(ParameterizedSetter<T> parameterizedSetter) {
            this.parameterizedSetter = parameterizedSetter;
        }
    }
}
