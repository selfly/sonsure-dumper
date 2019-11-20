package com.sonsure.dumper.core.command.entity;


import com.sonsure.commons.utils.ClassUtils;
import com.sonsure.dumper.core.annotation.Transient;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.exception.SonsureJdbcException;

import java.util.Map;

/**
 * Created by liyd on 17/4/14.
 */
public class UpdateImpl extends AbstractConditionCommandExecutor<Update> implements Update {

    protected UpdateContext updateContext;

    public UpdateImpl(JdbcEngineConfig jdbcEngineConfig) {
        super(jdbcEngineConfig);
        updateContext = new UpdateContext();
    }

    public Update table(Class<?> cls) {
        updateContext.setModelClass(cls);
        return this;
    }

    public Update set(String field, Object value) {
        updateContext.addSetField(field, value);
        return this;
    }

    public Update setForEntityWhereId(Object entity) {
        updateContext.setModelClass(entity.getClass());
        String pkField = getJdbcEngineConfig().getMappingHandler().getPkField(entity.getClass());
        Map<String, Object> beanPropMap = ClassUtils.getSelfBeanPropMap(entity, Transient.class);
        //处理主键成where条件
        Object pkValue = beanPropMap.get(pkField);
        if (pkValue == null) {
            throw new SonsureJdbcException("主键属性值不能为空:" + pkField);
        }
        this.where(pkField, pkValue);
        //移除
        beanPropMap.remove(pkField);

        for (Map.Entry<String, Object> entry : beanPropMap.entrySet()) {
            //不忽略null，最后构建时根据updateNull设置处理null值
            updateContext.addSetField(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public Update setForEntity(Object entity) {
        Map<String, Object> beanPropMap = ClassUtils.getSelfBeanPropMap(entity, Transient.class);
        for (Map.Entry<String, Object> entry : beanPropMap.entrySet()) {
            //不忽略null，最后构建时根据updateNull设置处理null值
            updateContext.addSetField(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public Update updateNull() {
        updateContext.setIgnoreNull(false);
        return this;
    }

    public int execute() {
        CommandContext commandContext = this.commandContextBuilder.build(this.updateContext, getJdbcEngineConfig());
        return (Integer) this.getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.UPDATE);
    }

    @Override
    protected WhereContext getWhereContext() {
        return this.updateContext;
    }
}
