package com.sonsure.dumper.core.command.entity;

import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandContextBuilder;
import com.sonsure.dumper.core.command.sql.CommandConversionHandler;
import com.sonsure.dumper.core.management.CommandField;
import com.sonsure.dumper.core.management.CommandTable;
import com.sonsure.dumper.core.management.ModelClassCache;
import com.sonsure.dumper.core.management.ModelFieldMeta;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 17/4/12.
 */
public abstract class AbstractCommandContextBuilder implements CommandContextBuilder {

    /**
     * native内容前后包围符号
     */
    protected static final String NATIVE_CONTENT_OPEN_TOKEN = "{";
    protected static final String NATIVE_CONTENT_CLOSE_TOKEN = "}";


    /**
     * value也需要native内容前后包围符号
     */
    protected static final String NATIVE_VALUE_CONTENT_OPEN_TOKEN = "{{";
    protected static final String NATIVE_VALUE_CONTENT_CLOSE_TOKEN = "}}";

    /**
     * 执行器
     */
    protected AbstractCommandExecutor commandExecutor;

    /**
     * command解析器
     */
    protected CommandConversionHandler commandConversionHandler;

    public AbstractCommandContextBuilder(AbstractCommandExecutor commandExecutor, CommandConversionHandler commandConversionHandler) {
        this.commandExecutor = commandExecutor;
        this.commandConversionHandler = commandConversionHandler;
    }

    public CommandContext build(CommandTable commandTable) {
        CommandContext commandContext = this.doBuild(commandTable);
        commandContext.setMappingHandler(this.commandExecutor.getMappingHandler());
        commandContext.setCommandCase(commandTable.getCommandCase());
        commandContext.setResultType(commandTable.getResultType());
        String resolvedCommand = commandContext.getCommand();
        if (!commandTable.isForceNative()) {
            resolvedCommand = commandConversionHandler.convert(commandContext.getCommand(), commandContext.getParameterMap());
        }
        commandContext.setCommand(resolvedCommand);
        return commandContext;
    }

    /**
     * 构建执行内容
     *
     * @param commandTable the command table
     * @return command context
     */
    public abstract CommandContext doBuild(CommandTable commandTable);

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

//    /**
//     * 获取带表别名的原生属性列
//     *
//     * @param commandTable
//     * @param nativeField
//     * @return
//     */
//    protected String getTableAliasNativeField(CommandTable commandTable, String nativeField) {
//        if (StringUtils.isNotBlank(commandTable.getTableAlias())) {
//            return new StringBuilder(commandTable.getTableAlias()).append(".").append(nativeField).toString();
//        }
//        return nativeField;
//    }

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

    protected String getPkField(CommandTable commandTable) {
        return this.getCommandExecutor().getMappingHandler().getPkField(commandTable.getModelClass());
    }

    protected String getTableName(CommandTable commandTable, Map<String, Object> params) {
        return this.getCommandExecutor().getMappingHandler().getTable(commandTable.getModelClass(), params);
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
     * @param commandTable the command table
     * @return generic command context
     */
    protected CommandContext getGenericCommandContext(CommandTable commandTable) {
        CommandContext commandContext = new CommandContext();
        commandContext.setModelClass(commandTable.getModelClass());
        commandContext.setResultType(commandTable.getResultType());
        String pkField = this.getPkField(commandTable);
        commandContext.setPkField(pkField);
        String pkColumn = this.getCommandExecutor().getMappingHandler().getColumn(commandTable.getModelClass(), pkField);
        commandContext.setPkColumn(pkColumn);
        if (this.commandExecutor.getKeyGenerator() == null || this.commandExecutor.getKeyGenerator().isPkValueByDb()) {
            commandContext.setPkValueByDb(true);
        } else {
            commandContext.setPkValueByDb(false);
        }
        return commandContext;
    }

    /**
     * 判断是否原生属性
     *
     * @param commandTable the command table
     * @param commandField the command field
     * @return object[] 元素见下说明
     * 0 是否原生属性
     * 1 是否原生value
     * 2 field名
     * 3 带表别名的field名 如果没有表别名，field名一致
     * 4 解析过的value 只对String有效
     */
    protected Object[] decideNativeField(CommandTable commandTable, CommandField commandField) {

        String field = commandField.getName();
        Object val = commandField.getValue();
        boolean isNativeContent = false;
        boolean isNativeValue = false;
        if (StringUtils.startsWith(field, NATIVE_VALUE_CONTENT_OPEN_TOKEN) && StringUtils.endsWith(field, NATIVE_VALUE_CONTENT_CLOSE_TOKEN)) {
            isNativeContent = true;
            isNativeValue = true;
            field = StringUtils.substring(field, NATIVE_VALUE_CONTENT_OPEN_TOKEN.length(), field.length() - NATIVE_VALUE_CONTENT_CLOSE_TOKEN.length());
        } else if (StringUtils.startsWith(field, NATIVE_CONTENT_OPEN_TOKEN) && StringUtils.endsWith(field, NATIVE_CONTENT_CLOSE_TOKEN)) {
            isNativeContent = true;
            field = StringUtils.substring(field, NATIVE_CONTENT_OPEN_TOKEN.length(), field.length() - NATIVE_CONTENT_CLOSE_TOKEN.length());
        }

        String aliasField = this.getTableAliasField(commandTable.getTableAlias(), field);

        return new Object[]{isNativeContent, isNativeValue, field, aliasField, val};

    }

    /**
     * 构建where部分sql
     *
     * @param commandTable the command table
     * @return string
     */
    protected CommandContext buildWhereSql(CommandTable commandTable) {
        CommandContext commandContext = new CommandContext();
        List<CommandField> whereFields = commandTable.getWhereFields();
        if (whereFields == null || whereFields.isEmpty()) {
            return commandContext;
        }

        StringBuilder whereCommand = new StringBuilder(" ");
        for (CommandField commandField : whereFields) {

            //在前面处理，有单独where or and 的情况
            if (StringUtils.isNotBlank(commandField.getLogicalOperator())) {
                //没有where不管如何and or等操作符都换成where
                if (whereCommand.length() < 5) {
                    whereCommand.append("where ");
                } else {
                    whereCommand.append(commandField.getLogicalOperator()).append(" ");
                }
            }
            //只有where or and 的情况
            if (StringUtils.isBlank(commandField.getName())) {
                continue;
            }

            Object[] objects = this.decideNativeField(commandTable, commandField);

            if (objects[4] == null) {
                String operator = StringUtils.isBlank(commandField.getFieldOperator()) ? "is" : commandField.getFieldOperator();
                whereCommand.append(objects[3]).append(" ").append(operator).append(" null ");
            } else if (objects[4] instanceof Object[]) {
                this.processArrayArgs(whereCommand, commandField, objects[2].toString(), objects[3].toString(), BooleanUtils.toBoolean(objects[1].toString()), commandContext);
            } else {
                whereCommand.append(objects[3])
                        .append(" ")
                        .append(commandField.getFieldOperator())
                        .append(" ");

                //native 不传参方式
                if (BooleanUtils.toBoolean(objects[1].toString())) {
                    whereCommand.append(commandField.isFieldOperatorNeedBracket() ? String.format(" ( %s ) ", objects[4]) : String.format(" %s ", objects[4]));
                } else {
                    whereCommand.append(commandField.isFieldOperatorNeedBracket() ? " ( ? ) " : " ? ");
                    commandContext.addParameter((String) objects[2], objects[4]);
                }
            }
        }
        //只有where的情况
        if (whereCommand.length() < 8) {
            whereCommand.delete(0, whereCommand.length());
        }
        commandContext.setCommand(whereCommand.toString());
//        commandContext.addParameters(parameters);
        return commandContext;
    }

    /**
     * 构建group by部分sql
     *
     * @param commandTable
     * @return
     */
    protected String buildGroupBySql(CommandTable commandTable) {
        List<CommandField> groupByFields = commandTable.getGroupByFields();
        if (groupByFields == null || groupByFields.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" group by ");
        for (CommandField groupByField : groupByFields) {
            String aliasField = this.getTableAliasField(commandTable.getTableAlias(), groupByField.getName());
            sb.append(aliasField).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 构建order by部分sql
     *
     * @param commandTable
     * @return
     */
    protected String buildOrderBySql(CommandTable commandTable) {

        List<CommandField> orderByFields = commandTable.getOrderByFields();
        if (orderByFields == null || orderByFields.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" order by ");
        for (CommandField orderByField : orderByFields) {
            String aliasField = this.getTableAliasField(commandTable.getTableAlias(), orderByField.getName());
            sb.append(aliasField).append(" ").append(orderByField.getFieldOperator()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }


    /**
     * 处理数组参数
     */
    protected void processArrayArgs(StringBuilder whereCommand, CommandField commandField, String field, String tableAliasColumn, boolean isNativeValue, CommandContext commandContext) {
        Object[] args = (Object[]) commandField.getValue();
        if (commandField.isFieldOperatorNeedBracket()) {
            whereCommand.append(tableAliasColumn).append(" ").append(commandField.getFieldOperator()).append(" (");
            for (int i = 0; i < args.length; i++) {
                if (isNativeValue) {
                    whereCommand.append(args[i]);
                } else {
                    whereCommand.append("?");
                    commandContext.addParameter(field, args[i]);
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
                whereCommand.append(tableAliasColumn).append(" ").append(commandField.getFieldOperator());
                if (isNativeValue) {
                    whereCommand.append(String.format(" %s ", args[i]));
                } else {
                    whereCommand.append(" ? ");
                    commandContext.addParameter(field, args[i]);
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

    public AbstractCommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public void setCommandExecutor(AbstractCommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }
}
