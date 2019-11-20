package com.sonsure.dumper.core.persist;


import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;
import com.sonsure.dumper.core.command.entity.Delete;
import com.sonsure.dumper.core.command.entity.Insert;
import com.sonsure.dumper.core.command.entity.Select;
import com.sonsure.dumper.core.command.entity.Update;
import com.sonsure.dumper.core.command.lambda.Consumer;
import com.sonsure.dumper.core.command.lambda.LambdaMethod;
import com.sonsure.dumper.core.command.mybatis.MybatisExecutor;
import com.sonsure.dumper.core.command.natives.NativeExecutor;
import com.sonsure.dumper.core.config.JdbcEngine;
import com.sonsure.dumper.core.exception.SonsureJdbcException;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 17/4/13.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractJdbcDaoImpl implements JdbcDao {

    protected DataSource dataSource;

    protected JdbcEngine defaultJdbcEngine;

    protected Map<String, JdbcEngine> jdbcEngineMap;

    protected boolean globalJdbc = false;

    @Override
    public JdbcDao use(String name) {
        if (jdbcEngineMap == null) {
            throw new SonsureJdbcException("使用多数据源模式请先初始化jdbcEngineMap属性");
        }
        JdbcEngine jdbcEngine = jdbcEngineMap.get(name);
        if (jdbcEngine == null) {
            throw new SonsureJdbcException("指定的数据源操作对象不存在");
        }
        return new FlexibleJdbcDao(jdbcEngine);
    }

    @Override
    public <T> T get(Class<T> entityClass, Serializable id) {
        return this.getDefaultJdbcEngine().get(entityClass, id);
    }

    @Override
    public <T> List<T> find(Class<T> entityClass) {
        return this.getDefaultJdbcEngine().find(entityClass);
    }

    @Override
    public <T> List<T> find(T entity) {
        return this.getDefaultJdbcEngine().find(entity);
    }

    @Override
    public <T extends Pageable> Page<T> pageResult(T entity) {
        return (Page<T>) this.getDefaultJdbcEngine().pageResult(entity);
    }

    @Override
    public long findCount(Object entity) {
        return this.getDefaultJdbcEngine().findCount(entity);
    }

    @Override
    public long findCount(Class<?> cls) {
        return this.getDefaultJdbcEngine().findCount(cls);
    }

    public <T> T singleResult(T entity) {
        return (T) this.getDefaultJdbcEngine().singleResult(entity);
    }

    public <T> T firstResult(T entity) {
        return (T) this.getDefaultJdbcEngine().firstResult(entity);
    }

    public Object executeInsert(Object entity) {
        return this.getDefaultJdbcEngine().executeInsert(entity);
    }

    @Override
    public Insert insertInto(Class<?> cls) {
        return this.getDefaultJdbcEngine().insertInto(cls);
    }

    @Override
    public int executeDelete(Class<?> entityClass, Serializable id) {
        return this.getDefaultJdbcEngine().executeDelete(entityClass, id);
    }

    @Override
    public int executeDelete(Object entity) {
        return this.getDefaultJdbcEngine().executeDelete(entity);
    }

    @Override
    public int executeDelete(Class<?> cls) {
        return this.getDefaultJdbcEngine().executeDelete(cls);
    }

    @Override
    public int executeUpdate(Object entity) {
        return this.getDefaultJdbcEngine().executeUpdate(entity);
    }

    @Override
    public Update update(Class<?> cls) {
        return this.getDefaultJdbcEngine().update(cls);
    }

    @Override
    public Select selectFrom(Class<?> cls) {
        return this.getDefaultJdbcEngine().selectFrom(cls);
    }

    public Select select() {
        return this.getDefaultJdbcEngine().select();
    }

    @Override
    public Select select(String... fields) {
        return this.getDefaultJdbcEngine().select(fields);
    }

    @Override
    public <E> Select select(Consumer<E>... consumers) {
        String[] fields = LambdaMethod.getFields(consumers);
        return this.select(fields);
    }

    public Insert insert() {
        return this.getDefaultJdbcEngine().insert();
    }

    public Delete delete() {
        return this.getDefaultJdbcEngine().delete();
    }

    @Override
    public Delete deleteFrom(Class<?> cls) {
        return this.getDefaultJdbcEngine().deleteFrom(cls);
    }

    public Update update() {
        return this.getDefaultJdbcEngine().update();
    }

    public NativeExecutor nativeExecutor() {
        return this.getDefaultJdbcEngine().createExecutor(NativeExecutor.class);
    }

    @Override
    public MybatisExecutor myBatisExecutor() {
        return this.getDefaultJdbcEngine().createExecutor(MybatisExecutor.class);
    }

    public JdbcEngine getDefaultJdbcEngine() {
        if (this.defaultJdbcEngine == null) {
            throw new SonsureJdbcException("jdbcEngine不能为空");
        }
        return this.defaultJdbcEngine;
    }

    public void enableGlobalJdbc() {
        this.globalJdbc = true;
        Jdbc.setDefaultJdbcEngine(this.defaultJdbcEngine);
        if (this.jdbcEngineMap != null) {
            Jdbc.addJdbcEngine(this.jdbcEngineMap);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDefaultJdbcEngine(JdbcEngine jdbcEngine) {
        this.defaultJdbcEngine = jdbcEngine;
    }

    public Map<String, JdbcEngine> getJdbcEngineMap() {
        return jdbcEngineMap;
    }

    public void setJdbcEngineMap(Map<String, JdbcEngine> jdbcEngineMap) {
        this.jdbcEngineMap = jdbcEngineMap;
    }

    public boolean isGlobalJdbc() {
        return globalJdbc;
    }

    public void setGlobalJdbc(boolean globalJdbc) {
        this.globalJdbc = globalJdbc;
    }
}
