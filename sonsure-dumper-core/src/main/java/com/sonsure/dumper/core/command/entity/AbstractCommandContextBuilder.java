/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.entity;

import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandContextBuilder;
import com.sonsure.dumper.core.command.CommandExecutorContext;
import com.sonsure.dumper.core.command.CommandParameter;
import com.sonsure.dumper.core.command.named.NamedParameterUtils;
import com.sonsure.dumper.core.command.named.ParsedSql;
import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.management.ClassField;
import com.sonsure.dumper.core.management.ModelClassCache;
import com.sonsure.dumper.core.management.ModelFieldMeta;
import com.sonsure.dumper.core.mapping.AbstractMappingHandler;
import com.sonsure.dumper.core.mapping.MappingHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Abstract command context builder.
 *
 * @author liyd
 * @date 17 /4/12
 */
public abstract class AbstractCommandContextBuilder implements CommandContextBuilder {

    @Override
    public CommandContext build(CommandExecutorContext executorContext, JdbcEngineConfig jdbcEngineConfig) {

        CommandContext commandContext = this.doBuild(executorContext, jdbcEngineConfig);
        MappingHandler mappingHandler = jdbcEngineConfig.getMappingHandler();
        if (mappingHandler instanceof AbstractMappingHandler) {
            Set<Class<?>> modelClasses = executorContext.getModelClasses();
            ((AbstractMappingHandler) mappingHandler).addClassMapping(modelClasses);
        }

        String resolvedCommand = commandContext.getCommand();
        if (!executorContext.isNativeCommand()) {
            // todo 需要收集参数信息，待完成
            Map<String, Object> params = Collections.emptyMap();
            resolvedCommand = jdbcEngineConfig.getCommandConversionHandler().convert(commandContext.getCommand(), params);
        }
        if (StringUtils.isNotBlank(jdbcEngineConfig.getCommandCase())) {
            resolvedCommand = this.convertCase(resolvedCommand, jdbcEngineConfig.getCommandCase());
        }

        if (executorContext.isNamedParameter()) {
            final ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(resolvedCommand);
            final Map<String, Object> paramMap = commandContext.getCommandParameters().stream()
                    .collect(Collectors.toMap(CommandParameter::getName, CommandParameter::getValue));
            final String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramMap);
            final Object[] objects = NamedParameterUtils.buildValueArray(parsedSql, paramMap);
            commandContext.setCommand(sqlToUse);
            commandContext.setParameters(Arrays.asList(objects));
        } else {
            final List<Object> objects = commandContext.getCommandParameters().stream()
                    .map(CommandParameter::getValue)
                    .collect(Collectors.toList());
            commandContext.setCommand(resolvedCommand);
            commandContext.setParameters(objects);
        }
        return commandContext;
    }

    /**
     * 构建执行内容
     *
     * @param executorContext  the executor context
     * @param jdbcEngineConfig the jdbc engine config
     * @return command context
     */
    public abstract CommandContext doBuild(CommandExecutorContext executorContext, JdbcEngineConfig jdbcEngineConfig);

    /**
     * 转换大小写
     *
     * @param content     the content
     * @param commandCase the command case
     * @return string
     */
    protected String convertCase(String content, String commandCase) {
        if (StringUtils.equalsIgnoreCase(commandCase, "upper")) {
            content = content.toUpperCase();
        } else if (StringUtils.equalsIgnoreCase(commandCase, "lower")) {
            content = content.toLowerCase();
        }
        return content;
    }

    /**
     * 获取带别名的field
     *
     * @param tableAlias the table alias
     * @param field      the field
     * @return table alias field
     */
    protected String getTableAliasField(String tableAlias, String field) {
        if (StringUtils.isNotBlank(tableAlias)) {
            return new StringBuilder(tableAlias).append(".").append(field).toString();
        }
        return field;
    }

    /**
     * 获取带别名的model名
     *
     * @param modelClass the model class
     * @param tableAlias the table alias
     * @return column table alias name
     */
    protected String getModelAliasName(Class<?> modelClass, String tableAlias) {
        StringBuilder sb = new StringBuilder(modelClass.getSimpleName());
        if (StringUtils.isNotBlank(tableAlias)) {
            sb.append(" ").append(tableAlias);
        }
        return sb.toString();
    }

    protected String getModelName(Class<?> modelClass) {
        return modelClass.getSimpleName();
    }

    protected String getPkField(Class<?> modelClass, MappingHandler mappingHandler) {
        return mappingHandler.getPkField(modelClass);
    }

    /**
     * 获取class的属性
     *
     * @return
     */
    protected Collection<ModelFieldMeta> getClassFields(Class<?> clazz) {
        return ModelClassCache.getClassFieldMetas(clazz);
    }

    /**
     * 获取设置了通用参数的CommandContext
     *
     * @param executorContext the executor context
     * @return generic command context
     */
    protected CommandContext getCommonCommandContext(CommandExecutorContext executorContext) {
        return new CommandContext();
    }

    /**
     * 构建where部分sql
     *
     * @param entityWhereContext the entity where context
     * @return string command context
     */
    protected CommandContext buildWhereSql(CommandExecutorContext.EntityWhereContext entityWhereContext, boolean isNamedParameter) {
        List<ClassField> whereFields = entityWhereContext.getWhereFields();
        if (whereFields == null || whereFields.isEmpty()) {
            return null;
        }

        StringBuilder whereCommand = new StringBuilder(" ");
        List<CommandParameter> commandParameters = new ArrayList<>();
        for (ClassField classField : whereFields) {

            //在前面处理，有单独where or and 的情况
            if (StringUtils.isNotBlank(classField.getLogicalOperator())) {
                //没有where不管如何and or等操作符都换成where
                if (whereCommand.length() < 5) {
                    whereCommand.append("where ");
                } else {
                    whereCommand.append(classField.getLogicalOperator()).append(" ");
                }
            }
            //只有where or and 的情况
            if (StringUtils.isBlank(classField.getName())) {
                continue;
            }

            if (classField.getType() == ClassField.Type.WHERE_APPEND) {
                whereCommand.append(classField.getName());
                if (classField.getValue() != null) {
                    if (classField.getValue() instanceof Object[]) {
                        Object[] values = (Object[]) classField.getValue();
                        for (int i = 0; i < values.length; i++) {
                            commandParameters.add(new CommandParameter(classField.getName() + i, values[i]));
                        }
                    } else if (classField.getValue() instanceof Map) {
                        final Map<String, Object> valueMap = (Map<String, Object>) classField.getValue();
                        for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
                            commandParameters.add(new CommandParameter(entry.getKey(), entry.getValue()));
                        }
                    } else {
                        throw new SonsureJdbcException("不支持的参数类型");
                    }
                }
            } else if (classField.getValue() == null) {
                String operator = StringUtils.isBlank(classField.getFieldOperator()) ? "is" : classField.getFieldOperator();
                whereCommand.append(this.getTableAliasField(classField.getTableAlias(), classField.getName()))
                        .append(" ")
                        .append(operator)
                        .append(" null ");
            } else if (classField.getValue() instanceof Object[]) {
                this.processArrayArgs(classField, whereCommand, commandParameters, isNamedParameter);
            } else {
                whereCommand.append(this.getTableAliasField(classField.getTableAlias(), classField.getName()))
                        .append(" ")
                        .append(classField.getFieldOperator())
                        .append(" ");

                //native 不传参方式
                if (classField.isNative()) {
                    whereCommand.append(classField.isFieldOperatorNeedBracket() ? String.format(" ( %s ) ", classField.getValue()) : String.format(" %s ", classField.getValue()));
                } else {
                    final String placeholder = this.createParameterPlaceholder(classField.getName(), isNamedParameter);
                    whereCommand.append(classField.isFieldOperatorNeedBracket() ? String.format(" ( %s ) ", placeholder) : String.format(" %s ", placeholder));
                    commandParameters.add(new CommandParameter(classField.getName(), classField.getValue()));
                }
            }
        }
        //只有where的情况
        if (whereCommand.length() < 8) {
            whereCommand.delete(0, whereCommand.length());
        }
        CommandContext commandContext = new CommandContext();
        commandContext.setCommand(whereCommand.toString());
        commandContext.addCommandParameters(commandParameters);
        return commandContext;
    }

    /**
     * 构建group by部分sql
     *
     * @param selectContext the select context
     * @return string
     */
    protected String buildGroupBySql(CommandExecutorContext.SelectContext selectContext) {
        List<ClassField> groupByFields = selectContext.getGroupByFields();
        if (groupByFields == null || groupByFields.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" group by ");
        for (ClassField groupByField : groupByFields) {
            String aliasField = this.getTableAliasField(groupByField.getTableAlias(), groupByField.getName());
            sb.append(aliasField).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 构建order by部分sql
     *
     * @param selectContext the select context
     * @return string
     */
    protected String buildOrderBySql(CommandExecutorContext.SelectContext selectContext) {

        List<ClassField> orderByFields = selectContext.getOrderByFields();
        if (orderByFields == null || orderByFields.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" order by ");
        for (ClassField orderByField : orderByFields) {
            String aliasField = this.getTableAliasField(orderByField.getTableAlias(), orderByField.getName());
            sb.append(aliasField).append(" ").append(orderByField.getFieldOperator()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }


    /**
     * 处理数组参数
     */
    protected void processArrayArgs(ClassField classField, StringBuilder whereCommand, List<CommandParameter> parameters, boolean isNamedParameter) {
        String aliasField = this.getTableAliasField(classField.getTableAlias(), classField.getName());
        Object[] args = (Object[]) classField.getValue();
        if (classField.isFieldOperatorNeedBracket()) {
            whereCommand.append(aliasField).append(" ").append(classField.getFieldOperator()).append(" (");
            for (int i = 0; i < args.length; i++) {
                if (classField.isNative()) {
                    whereCommand.append(args[i]);
                } else {
                    final String name = classField.getName() + i;
                    String placeholder = this.createParameterPlaceholder(name, isNamedParameter);
                    whereCommand.append(placeholder);
                    parameters.add(new CommandParameter(name, args[i]));
                }
                if (i != args.length - 1) {
                    whereCommand.append(",");
                }
            }
            whereCommand.append(") ");
        } else {
            if (ArrayUtils.getLength(args) > 1) {
                whereCommand.append(" (");
            }
            for (int i = 0; i < args.length; i++) {
                whereCommand.append(aliasField).append(" ").append(classField.getFieldOperator());
                if (classField.isNative()) {
                    whereCommand.append(String.format(" %s ", args[i]));
                } else {
                    final String name = classField.getName() + i;
                    String placeholder = this.createParameterPlaceholder(name, isNamedParameter);
                    whereCommand.append(" ").append(placeholder).append(" ");
                    parameters.add(new CommandParameter(name, args[i]));
                }
                if (i != args.length - 1) {
                    whereCommand.append(" or ");
                }
            }
            if (ArrayUtils.getLength(args) > 1) {
                whereCommand.append(") ");
            }
        }
    }

    protected String createParameterPlaceholder(String name, boolean isNamedParameter) {
        return isNamedParameter ? ":" + name : "?";
    }
}
