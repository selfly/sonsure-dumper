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
import com.sonsure.dumper.core.management.CommandClass;
import com.sonsure.dumper.core.management.CommandField;
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

    /**
     * The constant COMMAND_PARAMETER.
     */
    private static final String COMMAND_PARAMETERS = "commandParameters";

    /**
     * The Context.
     */
    private Map<String, Object> context = new HashMap<>();

    /**
     * The Jdbc engine config.
     */
    private JdbcEngineConfig jdbcEngineConfig;

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
        this.jdbcEngineConfig = jdbcEngineConfig;
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
        this.addInsertField(field, value, null);
    }

    /**
     * Add insert field.
     *
     * @param field the field
     * @param value the value
     * @param cls   the cls
     */
    public void addInsertField(String field, Object value, Class<?> cls) {
        List<CommandField> insertFields = this.getInsertFields();
        if (insertFields == null) {
            insertFields = new ArrayList<>();
            context.put(INSERT_FIELDS, insertFields);
        }
        CommandField.Type type = cls == null ? CommandField.Type.MANUAL_FIELD : CommandField.Type.ENTITY_FIELD;
        CommandField commandField = this.createCommandClassField(field, false, type, cls);
        commandField.setValue(value);
        insertFields.add(commandField);
    }

    /**
     * Gets insert fields.
     *
     * @return the insert fields
     */
    public List<CommandField> getInsertFields() {
        return (List<CommandField>) context.get(INSERT_FIELDS);
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
     * Gets jdbc engine config.
     *
     * @return the jdbc engine config
     */
    public JdbcEngineConfig getJdbcEngineConfig() {
        return jdbcEngineConfig;
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
     * @param type              the type
     * @return the class field
     */
    public CommandField createCommandClassField(String name, boolean analyseTableAlias, CommandField.Type type) {
        return this.createCommandClassField(name, analyseTableAlias, type, null);
    }

    /**
     * Create class field class field.
     *
     * @param name              the name
     * @param analyseTableAlias the analyse table alias
     * @return the class field
     */
    public CommandField createCommandClassField(String name, boolean analyseTableAlias, CommandField.Type type, Class<?> cls) {
        return new CommandField(name, analyseTableAlias, type, cls);
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
            List<CommandField> selectFields = this.getSelectFields();
            for (String field : fields) {
                selectFields.add(CommandExecutorContext.this.createCommandClassField(field, true, CommandField.Type.MANUAL_FIELD));
            }
        }

        public List<CommandField> getSelectFields() {
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

        public List<CommandField> getExcludeFields() {
            return CommandExecutorContext.this.getOrInitIfAbsentList(EXCLUDE_FIELDS);
        }

        public void addExcludeFields(String... fields) {
            final List<CommandField> excludeFields = this.getExcludeFields();
            for (String field : fields) {
                excludeFields.add(CommandExecutorContext.this.createCommandClassField(field, true, CommandField.Type.MANUAL_FIELD));
            }
        }

        public List<CommandField> getGroupByFields() {
            return CommandExecutorContext.this.getOrInitIfAbsentList(GROUP_BY_FIELDS);
        }

        public void addGroupByField(String... fields) {
            final List<CommandField> groupByFields = this.getGroupByFields();
            for (String field : fields) {
                groupByFields.add(CommandExecutorContext.this.createCommandClassField(field, true, CommandField.Type.MANUAL_FIELD));
            }
        }

        public List<CommandField> getOrderByFields() {
            return CommandExecutorContext.this.getOrInitIfAbsentList(ORDER_BY_FIELDS);
        }

        public void addOrderByField(String... fields) {
            final List<CommandField> orderByFields = this.getOrderByFields();
            for (String field : fields) {
                orderByFields.add(CommandExecutorContext.this.createCommandClassField(field, true, CommandField.Type.MANUAL_FIELD));
            }
        }

        public void setOrderByType(String type) {
            final List<CommandField> orderByFields = this.getOrderByFields();
            if (orderByFields.isEmpty()) {
                throw new SonsureJdbcException("请先指定需要排序的属性");
            }
            int size = orderByFields.size();
            for (int i = size - 1; i >= 0; i--) {
                CommandField commandField = orderByFields.get(i);
                if (StringUtils.isNotBlank(commandField.getFieldOperator())) {
                    //已经指定了，跳出
                    break;
                }
                commandField.setFieldOperator(type);
            }
        }


        /**
         * 是否黑名单
         *
         * @param field
         * @return
         */
        public boolean isExcludeField(String field) {
            final List<CommandField> excludeFields = this.getExcludeFields();
            if (excludeFields == null || excludeFields.isEmpty()) {
                return false;
            }
            CommandField commandField = CommandExecutorContext.this.createCommandClassField(field, true, CommandField.Type.MANUAL_FIELD);
            for (CommandField excludeField : excludeFields) {
                if (StringUtils.equals(commandField.getTableAlias(), excludeField.getTableAlias()) && StringUtils.equals(commandField.getFieldName(), excludeField.getFieldName())) {
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
        protected List<CommandField> setFields;

        public UpdateContext() {
            setFields = new ArrayList<>();
        }

        public void addSetField(String field, Object value) {
            CommandField commandField = CommandExecutorContext.this.createCommandClassField(field, true, CommandField.Type.MANUAL_FIELD);
            commandField.setValue(value);
            this.setFields.add(commandField);
        }

        public boolean isIgnoreNull() {
            return isIgnoreNull;
        }

        public void setIgnoreNull(boolean ignoreNull) {
            isIgnoreNull = ignoreNull;
        }

        public List<CommandField> getSetFields() {
            return setFields;
        }

    }

    public class EntityWhereContext {

        /**
         * The constant WHERE_FIELDS.
         */
        private static final String WHERE_FIELDS = "whereFields";


        public void addWhereField(String logicalOperator, String name, String fieldOperator, Object value, CommandField.Type type) {
            boolean analyseTableAlias = CommandField.Type.isAnalyseTableAlias(type);
            CommandField commandField = CommandExecutorContext.this.createCommandClassField(name, analyseTableAlias, CommandField.Type.MANUAL_FIELD);
            commandField.setLogicalOperator(logicalOperator);
            commandField.setFieldOperator(fieldOperator);
            commandField.setValue(value);
            commandField.setType(type);
            this.getWhereFields().add(commandField);
        }

        public void addWhereFields(List<CommandField> commandFields) {
            this.getWhereFields().addAll(commandFields);
        }

        public void addWhereField(String logicalOperator, String name, String fieldOperator, Object value) {
            this.addWhereField(logicalOperator, name, fieldOperator, value, null);
        }

        public List<CommandField> getWhereFields() {
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
