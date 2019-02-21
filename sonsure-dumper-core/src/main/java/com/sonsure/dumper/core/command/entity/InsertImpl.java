package com.sonsure.dumper.core.command.entity;

import com.sonsure.commons.utils.ClassUtils;
import com.sonsure.dumper.core.annotation.Transient;
import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.management.CommandField;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;

import java.util.Map;

/**
 * Created by liyd on 17/4/14.
 */
public class InsertImpl<T extends Object> extends AbstractCommandExecutor implements Insert<T> {

    public InsertImpl(MappingHandler mappingHandler, PageHandler pageHandler, KeyGenerator keyGenerator, PersistExecutor persistExecutor, String commandCase) {
        super(mappingHandler, pageHandler, keyGenerator, persistExecutor, commandCase);
    }

    public Insert<T> set(String field, Object value) {
        CommandField commandField = CommandField.builder()
                .name(field)
                .value(value)
                .type(CommandField.Type.INSERT)
                .orig(CommandField.Orig.MANUAL)
                .build();
        this.commandTable.addOperationField(commandField);
        return this;
    }

    public Insert<T> setForEntity(Object entity) {
        Map<String, Object> beanPropMap = ClassUtils.getSelfBeanPropMap(entity, Transient.class);

        for (Map.Entry<String, Object> entry : beanPropMap.entrySet()) {

            //忽略掉null
            if (entry.getValue() == null) {
                continue;
            }

            CommandField commandField = CommandField.builder()
                    .name(entry.getKey())
                    .value(entry.getValue())
                    .type(CommandField.Type.INSERT)
                    .orig(CommandField.Orig.ENTITY)
                    .build();
            this.commandTable.addOperationField(commandField);
        }
        return this;
    }

    public Object execute() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        return this.persistExecutor.execute(commandContext, CommandType.INSERT);
    }
}
