package com.sonsure.dumper.core.command.entity;

import com.sonsure.commons.utils.ClassUtils;
import com.sonsure.dumper.core.annotation.Transient;
import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;

import java.util.Map;

/**
 * Created by liyd on 17/4/14.
 */
public class InsertImpl extends AbstractCommandExecutor implements Insert {

    private InsertContext insertContext;

    public InsertImpl(MappingHandler mappingHandler, PageHandler pageHandler, KeyGenerator keyGenerator, PersistExecutor persistExecutor, String commandCase) {
        super(mappingHandler, pageHandler, keyGenerator, persistExecutor, commandCase);
        insertContext = new InsertContext();
    }

    @Override
    public Insert into(Class<?> cls) {
        this.insertContext.setModelClass(cls);
        return this;
    }

    public Insert set(String field, Object value) {
        this.insertContext.addInsertField(field, value);
        return this;
    }

    public Insert forEntity(Object entity) {
        this.into(entity.getClass());
        Map<String, Object> beanPropMap = ClassUtils.getSelfBeanPropMap(entity, Transient.class);
        for (Map.Entry<String, Object> entry : beanPropMap.entrySet()) {
            //忽略掉null
            if (entry.getValue() == null) {
                continue;
            }
            this.insertContext.addInsertField(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public Object execute() {
        CommandContext commandContext = this.commandContextBuilder.build(insertContext);
        return this.persistExecutor.execute(commandContext, CommandType.INSERT);
    }
}
