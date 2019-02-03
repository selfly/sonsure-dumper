package com.sonsure.dumper.core.command.sql;

import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.mapping.MappingHandler;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandTableHandler {

    /**
     * 没有表别名时的默认别名 如单表查询时
     */
    private static final String DEFAULT_ALIAS = "_default_alias";

    /**
     * 特殊符
     */
    private static final String SPECIAL_CHAR = "`";

    /**
     * 映射处理器
     */
    private MappingHandler mappingHandler;

    /**
     * 类名称映射
     */
    protected Map<String, String> classMapping;

    /**
     * 自定义类名称映射
     */
    protected Map<String, String> customClassMapping;


    private final Map<String, String> aliasTableMap = new HashMap<>();

    public CommandTableHandler(MappingHandler mappingHandler, Map<String, String> classMapping, Map<String, String> customClassMapping) {
        this.mappingHandler = mappingHandler;
        this.classMapping = classMapping;
        this.customClassMapping = customClassMapping;
    }

    public void extractTable(Insert insert) {
        this.extractTable(insert.getTable());
    }

    public void extractTable(Update update) {
        if (update.getTables() != null) {
            List<Table> tables = update.getTables();
            for (Table table : tables) {
                this.extractTable(table);
            }
        }
        if (update.getExpressions() != null) {
            List<Expression> expressions = update.getExpressions();
            for (Expression expression : expressions) {
                if (expression instanceof SubSelect) {
                    this.extractTable(((SubSelect) expression));
                } else if (expression instanceof BinaryExpression) {
                    this.extractTable(((BinaryExpression) expression));
                }
            }
        }

    }

    public void extractTable(Delete delete) {
        if (delete.getTables() != null && delete.getTables().size() > 0) {
            for (Table table : delete.getTables()) {
                this.extractTable(table);
            }
        }
        this.extractTable(delete.getTable());
        if (delete.getJoins() != null) {
            for (Join join : delete.getJoins()) {
                FromItem rightItem = join.getRightItem();
                this.extractTable(rightItem);
            }
        }

        if (delete.getWhere() != null) {
            Expression expression = delete.getWhere();
            this.extractTable(((BinaryExpression) expression));
        }
    }

    public void extractTable(PlainSelect plainSelect) {

        if (plainSelect.getFromItem() != null) {
            FromItem fromItem = plainSelect.getFromItem();
            this.extractTable(fromItem);
        }

        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                FromItem rightItem = join.getRightItem();
                this.extractTable(rightItem);
            }
        }

        if (plainSelect.getWhere() != null) {
            Expression expression = plainSelect.getWhere();
            if (expression instanceof BinaryExpression) {
                this.extractTable(((BinaryExpression) expression));
            } else if (expression instanceof InExpression) {
                this.extractTable((InExpression) expression);
            } else {
                throw new SonsureJdbcException("当前不支持的SQL转换");
            }
        }
    }

    public void extractTable(FromItem fromItem) {
        if (fromItem instanceof SubSelect) {
            SelectBody selectBody = ((SubSelect) fromItem).getSelectBody();
            if (selectBody instanceof PlainSelect) {
                FromItem fi = ((PlainSelect) selectBody).getFromItem();
                this.extractTable(fi);
                Expression where = ((PlainSelect) selectBody).getWhere();
                if (where != null) {
                    this.extractTable(((BinaryExpression) where));
                }
            }
        } else if (fromItem instanceof Table) {
            Table table = (Table) fromItem;
            String tableClass = this.getTableClass(table.getName());
            String aliasName = table.getAlias() == null ? DEFAULT_ALIAS : table.getAlias().getName();
            aliasTableMap.put(aliasName, tableClass);
        }
    }

    public void extractTable(InExpression expression) {
//        Expression leftExpression = expression.getLeftExpression();
//        if (leftExpression instanceof FromItem) {
//            extractTable(((FromItem) leftExpression));
//        }
        ItemsList rightItemsList = expression.getRightItemsList();
        if (rightItemsList instanceof SubSelect) {
            extractTable(((SubSelect) rightItemsList));
        }
    }

    public void extractTable(BinaryExpression expression) {
        Expression leftExpression = expression.getLeftExpression();
        if (leftExpression instanceof FromItem) {
            extractTable(((FromItem) leftExpression));
        }
        Expression rightExpression = expression.getRightExpression();
        if (rightExpression instanceof FromItem) {
            extractTable(((FromItem) rightExpression));
        }
    }

    public String getTableName(String commandTbName) {
        String tableClass = this.getTableClass(commandTbName);
        String tbName = ParseCache.getClassTableName(tableClass, this.mappingHandler);
        return tbName;
    }

    public Class<?> getTableClassForAlias(String alias) {
        String key = alias;
        if (StringUtils.isBlank(alias)) {
            key = DEFAULT_ALIAS;
        }
        String tableName = aliasTableMap.get(key);

        String tableClass = StringUtils.indexOf(tableName, ".") != -1 ? tableName : getTableClass(tableName);

        return ParseCache.getClass(tableClass);
    }

    public String getTableClass(String tableName) {
        if (StringUtils.startsWith(tableName, SPECIAL_CHAR)) {
            return StringUtils.substring(tableName, 1, tableName.length() - 1);
        }
        String tableClass = null;
        if (customClassMapping != null) {
            tableClass = customClassMapping.get(tableName);
        }
        if (tableClass == null && classMapping != null) {
            tableClass = classMapping.get(tableName);
        }
        if (tableClass == null) {
            throw new SonsureJdbcException("实体类不存在：" + tableName);
        }
        return tableClass;
    }

    public MappingHandler getMappingHandler() {
        return mappingHandler;
    }
}
