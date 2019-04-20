package com.sonsure.dumper.core.command.entity;

import com.sonsure.commons.utils.ClassUtils;
import com.sonsure.dumper.core.annotation.Transient;
import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

import java.util.Map;

/**
 * Created by liyd on 17/4/14.
 */
public class InsertImpl extends AbstractCommandExecutor implements Insert {

    private InsertContext insertContext;

    public InsertImpl(JdbcEngineConfig jdbcEngineConfig) {
        super(jdbcEngineConfig);
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
        CommandContext commandContext = this.commandContextBuilder.build(insertContext, getJdbcEngineConfig());
        return getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.INSERT);
    }
}
