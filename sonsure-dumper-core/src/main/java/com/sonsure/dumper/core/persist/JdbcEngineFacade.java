package com.sonsure.dumper.core.persist;

import com.sonsure.dumper.core.command.entity.Delete;
import com.sonsure.dumper.core.command.entity.Insert;
import com.sonsure.dumper.core.command.entity.Select;
import com.sonsure.dumper.core.command.entity.Update;
import com.sonsure.dumper.core.config.JdbcEngine;

import java.io.Serializable;

public class JdbcEngineFacade {

    private JdbcEngine jdbcEngine;

    public JdbcEngineFacade(JdbcEngine jdbcEngine) {
        this.jdbcEngine = jdbcEngine;
    }

    public String getName() {
        return jdbcEngine.getName();
    }

    public boolean isDefault() {
        return jdbcEngine.isDefault();
    }

    public Select select() {
        return jdbcEngine.createExecutor(Select.class);
    }

    public Select selectFrom(Class<?> cls) {
        return this.select().from(cls);
    }

    public <T> T get(Class<T> cls, Serializable id) {
        String pkField = this.jdbcEngine.getJdbcEngineConfig().getMappingHandler().getPkField(cls);
        return this.selectFrom(cls).where(pkField, id).singleResult(cls);
    }

    public Insert insert() {
        return jdbcEngine.createExecutor(Insert.class);
    }

    public Insert insertInto(Class<?> cls) {
        return this.insert().into(cls);
    }

    public Update update() {
        return jdbcEngine.createExecutor(Update.class);
    }

    public Delete delete() {
        return jdbcEngine.createExecutor(Delete.class);
    }

    public JdbcEngine getJdbcEngine() {
        return jdbcEngine;
    }
}
