package com.sonsure.dumper.core.command.entity;


import com.sonsure.commons.utils.ClassUtils;
import com.sonsure.dumper.core.annotation.Transient;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.management.CommandField;
import com.sonsure.dumper.core.management.MappingCache;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;

import java.util.Map;

/**
 * Created by liyd on 17/4/14.
 */
public class UpdateImpl<T> extends AbstractConditionBuilder<Update<T>> implements Update<T> {

    public UpdateImpl(MappingHandler mappingHandler, PageHandler pageHandler, KeyGenerator keyGenerator, PersistExecutor persistExecutor, boolean commandUppercase) {
        super(mappingHandler, pageHandler, keyGenerator, persistExecutor, commandUppercase);
    }

    public Update<T> set(String field, Object value) {
        CommandField commandField = CommandField.builder()
                .name(field)
                .value(value)
                .type(CommandField.Type.UPDATE)
                .orig(CommandField.Orig.MANUAL)
                .build();
        this.commandTable.addOperationField(commandField);
        return this;
    }

    public Update<T> setForEntityWhereId(Object entity) {
        commandTable.setModelClass(entity.getClass());
        String pkField = MappingCache.getPkField(this.commandTable, getMappingHandler());

        Map<String, Object> beanPropMap = ClassUtils.getSelfBeanPropMap(entity, Transient.class);
        //处理主键成where条件
        Object pkValue = beanPropMap.get(pkField);
        if (pkValue == null) {
            throw new SonsureJdbcException("主键属性值不能为空:" + pkField);
        }

        CommandField pkCommandField = CommandField.builder()
                .logicalOperator("where")
                .name(pkField)
                .fieldOperator("=")
                .value(pkValue)
                .type(CommandField.Type.WHERE_FIELD)
                .orig(CommandField.Orig.ENTITY)
                .build();
        this.commandTable.addWhereField(pkCommandField);
        //移除
        beanPropMap.remove(pkField);

        for (Map.Entry<String, Object> entry : beanPropMap.entrySet()) {
            //不忽略null，最后构建时根据updateNull设置处理null值
            CommandField commandField = CommandField.builder()
                    .name(entry.getKey())
                    .value(entry.getValue())
                    .type(CommandField.Type.UPDATE)
                    .orig(CommandField.Orig.ENTITY)
                    .build();
            this.commandTable.addOperationField(commandField);
        }
        return this;
    }

    public Update<T> setForEntity(Object entity) {
        Map<String, Object> beanPropMap = ClassUtils.getSelfBeanPropMap(entity, Transient.class);
        for (Map.Entry<String, Object> entry : beanPropMap.entrySet()) {
            //不忽略null，最后构建时根据updateNull设置处理null值
            CommandField commandField = CommandField.builder()
                    .name(entry.getKey())
                    .value(entry.getValue())
                    .type(CommandField.Type.UPDATE)
                    .orig(CommandField.Orig.ENTITY)
                    .build();
            this.commandTable.addOperationField(commandField);
        }
        return this;
    }

    public Update<T> updateNull() {
        this.commandTable.setIgnoreNull(false);
        return this;
    }

    public int execute() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        return (Integer) this.persistExecutor.execute(commandContext, CommandType.EXECUTE);
    }
}
