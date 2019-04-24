package com.sonsure.dumper.core.persist;


import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;
import com.sonsure.dumper.core.command.entity.Delete;
import com.sonsure.dumper.core.command.entity.Insert;
import com.sonsure.dumper.core.command.entity.Select;
import com.sonsure.dumper.core.command.entity.Update;
import com.sonsure.dumper.core.command.mybatis.MybatisExecutor;
import com.sonsure.dumper.core.command.natives.NativeExecutor;
import com.sonsure.dumper.core.config.JdbcEngine;
import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.exception.SonsureJdbcException;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;

/**
 * Created by liyd on 17/4/13.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractJdbcDaoImpl implements JdbcDao {

    protected DataSource dataSource;

    protected JdbcEngine jdbcEngine;

    @Override
    public <T> T get(Class<T> entityClass, Serializable id) {
        return this.getJdbcEngine().get(entityClass, id);
    }

    @Override
    public <T> List<T> find(Class<T> entityClass) {
        return this.getJdbcEngine().find(entityClass);
    }

    @Override
    public <T> List<T> find(T entity) {
        return this.getJdbcEngine().find(entity);
    }

    @Override
    public <T extends Pageable> Page<T> pageResult(T entity) {
        return (Page<T>) this.getJdbcEngine().pageResult(entity);
    }

    @Override
    public long findCount(Object entity) {
        return this.getJdbcEngine().findCount(entity);
    }

    @Override
    public long findCount(Class<?> cls) {
        return this.getJdbcEngine().findCount(cls);
    }

    public <T> T singleResult(T entity) {
        return (T) this.getJdbcEngine().singleResult(entity);
    }

    public <T> T firstResult(T entity) {
        return (T) this.getJdbcEngine().firstResult(entity);
    }

    public Object executeInsert(Object entity) {
        return this.getJdbcEngine().executeInsert(entity);
    }

    @Override
    public Insert insertInto(Class<?> cls) {
        return this.getJdbcEngine().insertInto(cls);
    }

    @Override
    public int executeDelete(Class<?> entityClass, Serializable id) {
        return this.getJdbcEngine().executeDelete(entityClass, id);
    }

    @Override
    public int executeDelete(Object entity) {
        return this.getJdbcEngine().executeDelete(entity);
    }

    @Override
    public int executeDelete(Class<?> cls) {
        return this.getJdbcEngine().executeDelete(cls);
    }

    @Override
    public int executeUpdate(Object entity) {
        return this.getJdbcEngine().executeUpdate(entity);
    }

    @Override
    public Update update(Class<?> cls) {
        return this.getJdbcEngine().update(cls);
    }

    @Override
    public Select selectFrom(Class<?> cls) {
        return this.getJdbcEngine().selectFrom(cls);
    }

    public Select select() {
        return this.getJdbcEngine().select();
    }

    @Override
    public Select select(String... fields) {
        return this.getJdbcEngine().select(fields);
    }

    public Insert insert() {
        return this.getJdbcEngine().insert();
    }

    public Delete delete() {
        return this.getJdbcEngine().delete();
    }

    @Override
    public Delete deleteFrom(Class<?> cls) {
        return this.getJdbcEngine().deleteFrom(cls);
    }

    public Update update() {
        return this.getJdbcEngine().update();
    }

    public NativeExecutor nativeExecutor() {
        return this.getJdbcEngine().createExecutor(NativeExecutor.class);
    }

    @Override
    public MybatisExecutor myBatisExecutor() {
        return this.getJdbcEngine().createExecutor(MybatisExecutor.class);
    }

    public JdbcEngine getJdbcEngine() {
        if (this.jdbcEngine == null) {
            throw new SonsureJdbcException("jdbcEngine不能为空");
        }
        return this.jdbcEngine;
    }

    /**
     * 获取默认的JdbcEngineConfig，由子类实现返回对应的实现配置
     *
     * @return
     */
    public abstract JdbcEngineConfig getDefaultJdbcEngineConfig();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setJdbcEngine(JdbcEngine jdbcEngine) {
        this.jdbcEngine = jdbcEngine;
    }
}
