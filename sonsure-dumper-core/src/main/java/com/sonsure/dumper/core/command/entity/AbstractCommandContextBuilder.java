package com.sonsure.dumper.core.command.entity;

import com.sonsure.commons.parser.GenericTokenParser;
import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandContextBuilder;
import com.sonsure.dumper.core.command.FieldTokenHandler;
import com.sonsure.dumper.core.management.CommandField;
import com.sonsure.dumper.core.management.CommandTable;
import com.sonsure.dumper.core.management.MappingCache;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
     * native内容中属性前后包围符号
     */
    protected static final String NATIVE_FIELD_OPEN_TOKEN = "[";
    protected static final String NATIVE_FIELD_CLOSE_TOKEN = "]";

    protected AbstractCommandExecutor commandExecutor;

    public AbstractCommandContextBuilder(AbstractCommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public CommandContext build(CommandTable commandTable) {
        CommandContext commandContext = this.doBuild(commandTable);
        commandContext.setCommandUppercase(commandTable.isCommandUppercase());
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
     * 获取主键属性
     *
     * @param commandTable
     * @return
     */
    protected String getPkField(CommandTable commandTable) {
        return MappingCache.getPkField(commandTable, commandExecutor.getMappingHandler());
    }

    /**
     * 获取主键列
     *
     * @param commandTable
     * @return
     */
    protected String getPkColumn(CommandTable commandTable) {
        return MappingCache.getPkColumn(commandTable, commandExecutor.getMappingHandler());
    }

    /**
     * 根据属性获取列
     *
     * @param commandTable the command table
     * @param field        the field
     * @return column
     */
    protected String getColumn(CommandTable commandTable, String field) {
        return MappingCache.getColumn(commandTable, field, commandExecutor.getMappingHandler());
    }

    /**
     * 根据属性获取带表别名的列
     *
     * @param commandTable the command table
     * @param field        the field
     * @return column
     */
    protected String getTableAliasColumn(CommandTable commandTable, String field) {
        String column = this.getColumn(commandTable, field);
        if (StringUtils.isNotBlank(commandTable.getTableAlias())) {
            return new StringBuilder(commandTable.getTableAlias()).append(".").append(column).toString();
        }
        return column;
    }

    /**
     * 获取带表别名的原生属性列
     *
     * @param commandTable
     * @param nativeField
     * @return
     */
    protected String getTableAliasNativeField(CommandTable commandTable, String nativeField) {
        if (StringUtils.isNotBlank(commandTable.getTableAlias())) {
            return new StringBuilder(commandTable.getTableAlias()).append(".").append(nativeField).toString();
        }
        return nativeField;
    }

    /**
     * 获取表名
     *
     * @param commandTable
     * @return
     */
    protected String getTableName(CommandTable commandTable) {
        if (StringUtils.isBlank(commandTable.getTableName())) {
            String tableName = MappingCache.getTableName(commandTable, commandExecutor.getMappingHandler());
            commandTable.setTableName(tableName);
        }
        return commandTable.getTableName();
    }


    /**
     * 根据属性获取带表别名的列
     *
     * @param commandTable the command table
     * @return column table alias name
     */
    protected String getTableAliasName(CommandTable commandTable) {
        String tableName = this.getTableName(commandTable);
        StringBuilder sb = new StringBuilder(tableName);
        if (StringUtils.isNotBlank(commandTable.getTableAlias())) {
            sb.append(" ").append(commandTable.getTableAlias());
        }
        return sb.toString();
    }

    /**
     * 获取class的属性
     *
     * @return
     */
    protected Set<String> getClassFields(CommandTable commandTable) {
        return MappingCache.getClassFields(commandTable, commandExecutor.getMappingHandler());
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
        commandContext.setPkField(this.getPkField(commandTable));
        commandContext.setPkColumn(this.getPkColumn(commandTable));
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
     * @param commandTable
     * @param name
     * @return object[] 元素见下说明
     * 0 是否原生属性
     * 1 是否原生value
     * 2 列名 如果是原生属性，则不做转换
     * 3 带表别名的列名 如果没有表别名，和列名一致
     * 4 解析过的value 只对String有效
     */
    protected Object[] decideNativeField(CommandTable commandTable, String name, Object value) {

        String column = name;
        Object val = value;
        boolean isNativeContent = false;
        boolean isNativeValue = false;
        GenericTokenParser tokenParser = new GenericTokenParser(NATIVE_FIELD_OPEN_TOKEN, NATIVE_FIELD_CLOSE_TOKEN, new FieldTokenHandler(commandTable, commandExecutor.getMappingHandler()));
        if (StringUtils.startsWith(column, NATIVE_VALUE_CONTENT_OPEN_TOKEN) && StringUtils.endsWith(column, NATIVE_VALUE_CONTENT_CLOSE_TOKEN)) {
            isNativeContent = true;
            isNativeValue = true;
            column = StringUtils.substring(column, NATIVE_VALUE_CONTENT_OPEN_TOKEN.length(), column.length() - NATIVE_VALUE_CONTENT_CLOSE_TOKEN.length());
            if (val != null && val instanceof String) {
                val = tokenParser.parse(val.toString());
            }
        } else if (StringUtils.startsWith(column, NATIVE_CONTENT_OPEN_TOKEN) && StringUtils.endsWith(column, NATIVE_CONTENT_CLOSE_TOKEN)) {
            isNativeContent = true;
            column = StringUtils.substring(column, NATIVE_CONTENT_OPEN_TOKEN.length(), column.length() - NATIVE_CONTENT_CLOSE_TOKEN.length());
        }

        String tableAliasColumn;
        if (!isNativeContent) {
            column = this.getColumn(commandTable, column);
            tableAliasColumn = this.getTableAliasColumn(commandTable, column);
        } else {
            column = tokenParser.parse(column);
            tableAliasColumn = column;
        }

        return new Object[]{isNativeContent, isNativeValue, column, tableAliasColumn, val};

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
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
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

            Object[] objects = this.decideNativeField(commandTable, commandField.getName(), commandField.getValue());

            if (objects[4] == null) {
                String operator = StringUtils.isBlank(commandField.getFieldOperator()) ? "is" : commandField.getFieldOperator();
                whereCommand.append(objects[3]).append(" ").append(operator).append(" null ");
            } else if (objects[4] instanceof Object[]) {
                this.processArrayArgs(whereCommand, commandField, objects[2].toString(), objects[3].toString(), BooleanUtils.toBoolean(objects[1].toString()), paramNames, paramValues);
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
                    paramNames.add(objects[2].toString());
                    paramValues.add(objects[4]);
                }
            }
        }
        //只有where的情况
        if (whereCommand.length() < 8) {
            whereCommand.delete(0, whereCommand.length());
        }
        commandContext.setCommand(whereCommand.toString());
        commandContext.addParameterNames(paramNames);
        commandContext.addParameters(paramValues);
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
            String columnName = this.getTableAliasColumn(commandTable, groupByField.getName());
            sb.append(columnName).append(",");
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
            String columnName = this.getTableAliasColumn(commandTable, orderByField.getName());
            sb.append(columnName).append(" ").append(orderByField.getFieldOperator()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }


    /**
     * 处理数组参数
     */
    protected void processArrayArgs(StringBuilder whereCommand, CommandField commandField, String column, String tableAliasColumn, boolean isNativeValue, List<String> paramNames, List<Object> paramValues) {
        Object[] args = (Object[]) commandField.getValue();
        if (commandField.isFieldOperatorNeedBracket()) {
            whereCommand.append(tableAliasColumn).append(" ").append(commandField.getFieldOperator()).append(" (");
            for (int i = 0; i < args.length; i++) {
                if (isNativeValue) {
                    whereCommand.append(args[i]);
                } else {
                    whereCommand.append("?");
                    paramNames.add(column);
                    paramValues.add(args[i]);
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
                    paramNames.add(column);
                    paramValues.add(args[i]);
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
