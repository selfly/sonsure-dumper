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

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;

/**
 * Created by liyd on 17/4/13.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractJdbcDaoImpl implements JdbcDao {

    protected DataSource dataSource;

    protected JdbcEngineConfig jdbcEngineConfig;

    protected JdbcEngine jdbcEngine;

    public <T> T get(Class<T> entityClass, Serializable id) {
        //return this.createSelect(entityClass).where().conditionId(id).singleResult();
        return null;
    }

    public <T> List<T> queryAll(Class<T> entityClass) {
//        return this.createSelect(entityClass).orderById().desc().list();
        return null;
    }

    public <T> List<T> queryList(T entity) {
//        return (List<T>) this.createSelect(entity.getClass()).where().conditionEntity(entity).orderById().desc().list();
        return null;
    }

    public <T extends Pageable> Page<T> queryPageList(T entity) {
//        return (Page<T>) this.createSelect(entity.getClass()).where().conditionEntity(entity).orderById().desc().pageList(entity);
        return null;
    }

    public <T> long queryCount(T entity) {
//        return this.createSelect(entity.getClass()).where().conditionEntity(entity).count();
        return 0;
    }

    public <T> T querySingleResult(T entity) {
        return (T) this.createSelect(entity.getClass()).where().conditionEntity(entity).singleResult();
    }

    public <T> T queryFirstResult(T entity) {
        return (T) this.createSelect(entity.getClass()).where().conditionEntity(entity).firstResult();
    }

    public <T> Object insert(T entity) {
        return this.createInsert(entity.getClass()).setForEntity(entity).execute();
    }

    public <T> int delete(Class<T> entityClass, Serializable id) {
        return this.createDelete(entityClass).where().conditionId(id).execute();
    }

    public <T> int delete(T entity) {
        return this.createDelete(entity.getClass()).where().conditionEntity(entity).execute();
    }

    public <T> int update(T entity) {
        return this.createUpdate(entity.getClass()).setForEntityWhereId(entity).execute();
    }

    public <T> Select createSelect(Class<T> entityClass) {
        return this.getJdbcEngine().createExecutor(entityClass, Select.class);
    }

    public <T> Insert<T> createInsert(Class<T> entityClass) {
        return this.getJdbcEngine().createExecutor(entityClass, Insert.class);
    }

    public <T> Delete<T> createDelete(Class<T> entityClass) {
        return this.getJdbcEngine().createExecutor(entityClass, Delete.class);
    }

    public <T> Update<T> createUpdate(Class<T> entityClass) {
        return this.getJdbcEngine().createExecutor(entityClass, Update.class);
    }

    public NativeExecutor createNativeExecutor() {
        return this.getJdbcEngine().createExecutor(NativeExecutor.class);
    }

    @Override
    public MybatisExecutor createMyBatisExecutor() {
        return this.getJdbcEngine().createExecutor(MybatisExecutor.class);
    }

    public JdbcEngine getJdbcEngine() {
        if (this.jdbcEngine == null) {
            if (jdbcEngineConfig == null) {
                jdbcEngineConfig = this.getDefaultJdbcEngineConfig();
            }
            this.jdbcEngine = jdbcEngineConfig.buildJdbcEngine(getDataSource());
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

    public JdbcEngineConfig getJdbcEngineConfig() {
        return jdbcEngineConfig;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setJdbcEngineConfig(JdbcEngineConfig jdbcEngineConfig) {
        this.jdbcEngineConfig = jdbcEngineConfig;
    }
}
