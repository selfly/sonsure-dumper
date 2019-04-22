package com.sonsure.dumper.core.config;


import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;
import com.sonsure.dumper.core.command.CommandExecutor;
import com.sonsure.dumper.core.command.entity.Delete;
import com.sonsure.dumper.core.command.entity.Insert;
import com.sonsure.dumper.core.command.entity.Select;
import com.sonsure.dumper.core.command.entity.Update;
import com.sonsure.dumper.core.persist.Jdbc;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liyd on 17/4/12.
 */
public class JdbcEngineImpl implements JdbcEngine {

    private JdbcEngineConfig jdbcEngineConfig;

    public JdbcEngineImpl(JdbcEngineConfig jdbcEngineConfig) {
        this.jdbcEngineConfig = jdbcEngineConfig;
        Jdbc.addJdbcEngine(this);
    }

    @Override
    public String getName() {
        return jdbcEngineConfig.getName();
    }

    @Override
    public boolean isDefault() {
        return jdbcEngineConfig.isDefault();
    }

    public <T extends CommandExecutor> T createExecutor(Class<T> commandExecutorClass, Object param) {
        return (T) this.jdbcEngineConfig.getCommandExecutorFactory().getCommandExecutor(commandExecutorClass, param, this.jdbcEngineConfig);
    }

    @Override
    public <T extends CommandExecutor> T createExecutor(Class<T> commandExecutorClass) {
        return this.createExecutor(commandExecutorClass, null);
    }

    @Override
    public JdbcEngineConfig getJdbcEngineConfig() {
        return jdbcEngineConfig;
    }

    @Override
    public Select select() {
        return this.createExecutor(Select.class);
    }

    @Override
    public Select select(String... fields) {
        return this.createExecutor(Select.class, fields);
    }

    @Override
    public Select selectFrom(Class<?> cls) {
        return this.select().from(cls);
    }

    @Override
    public <T> List<T> find(Class<T> cls) {
        String pkField = this.getJdbcEngineConfig().getMappingHandler().getPkField(cls);
        return this.selectFrom(cls).orderBy(pkField).desc().list(cls);
    }

    @Override
    public <T> List<T> find(T entity) {
        String pkField = this.getJdbcEngineConfig().getMappingHandler().getPkField(entity.getClass());
        return (List<T>) this.selectFrom(entity.getClass()).where().conditionEntity(entity).orderBy(pkField).desc().list(entity.getClass());
    }

    @Override
    public <T extends Pageable> Page<T> pageResult(T entity) {
        String pkField = this.getJdbcEngineConfig().getMappingHandler().getPkField(entity.getClass());
        return (Page<T>) this.selectFrom(entity.getClass()).where().conditionEntity(entity).paginate(entity).orderBy(pkField).desc().pageResult(entity.getClass());
    }

    @Override
    public long findCount(Object entity) {
        return this.selectFrom(entity.getClass()).where().conditionEntity(entity).count();
    }

    @Override
    public long findCount(Class<?> cls) {
        return this.selectFrom(cls).count();
    }

    @Override
    public <T> T singleResult(T entity) {
        return (T) this.selectFrom(entity.getClass()).where().conditionEntity(entity).singleResult(entity.getClass());
    }

    @Override
    public <T> T firstResult(T entity) {
        return (T) this.selectFrom(entity.getClass()).where().conditionEntity(entity).firstResult(entity.getClass());
    }

    @Override
    public <T> T get(Class<T> cls, Serializable id) {
        String pkField = this.getJdbcEngineConfig().getMappingHandler().getPkField(cls);
        return this.selectFrom(cls).where(pkField, id).singleResult(cls);
    }

    @Override
    public Insert insert() {
        return this.createExecutor(Insert.class);
    }

    @Override
    public Object insert(Object entity) {
        return this.insertInto(entity.getClass()).forEntity(entity).execute();
    }

    @Override
    public Insert insertInto(Class<?> cls) {
        return this.insert().into(cls);
    }

    @Override
    public Update update() {
        return this.createExecutor(Update.class);
    }

    @Override
    public Update update(Class<?> cls) {
        return this.update().table(cls);
    }

    @Override
    public Delete delete() {
        return this.createExecutor(Delete.class);
    }

    @Override
    public Delete deleteFrom(Class<?> cls) {
        return this.delete().from(cls);
    }

    @Override
    public int delete(Object entity) {
        return this.delete().from(entity.getClass()).where().conditionEntity(entity).execute();
    }

    @Override
    public int delete(Class<?> cls) {
        return this.delete().from(cls).execute();
    }
}
