package com.sonsure.dumper.core.config;

import com.sonsure.dumper.core.command.CommandExecutor;
import com.sonsure.dumper.core.mapping.AbstractMappingHandler;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;

import java.util.Map;

public abstract class AbstractCommandExecutorBuilder implements CommandExecutorBuilder {

    protected Map<Class<?>, MappingHandler> mappingHandlers;

    protected Map<Class<?>, PageHandler> pageHandlers;

    protected Map<Class<?>, KeyGenerator> keyGenerators;

    protected Map<Class<?>, PersistExecutor> persistExecutors;


    protected MappingHandler getExecutorMappingHandler(Class<?> modelClass, JdbcEngineConfig jdbcEngineConfig) {
        MappingHandler mappingHandler = jdbcEngineConfig.getMappingHandler();
        if (mappingHandlers != null && mappingHandlers.containsKey(modelClass)) {
            mappingHandler = mappingHandlers.get(modelClass);
        }
        if (mappingHandler instanceof AbstractMappingHandler && !CommandExecutor.class.isAssignableFrom(modelClass)) {
            ((AbstractMappingHandler) mappingHandler).addClassMapping(modelClass);
        }
        return mappingHandler;
    }

    protected PageHandler getExecutorPageHandler(Class<?> modelClass, JdbcEngineConfig jdbcEngineConfig) {
        if (pageHandlers != null && pageHandlers.containsKey(modelClass)) {
            return pageHandlers.get(modelClass);
        }
        return jdbcEngineConfig.getPageHandler();
    }

    protected KeyGenerator getExecutorKeyGenerator(Class<?> modelClass, JdbcEngineConfig jdbcEngineConfig) {
        if (keyGenerators != null && keyGenerators.containsKey(modelClass)) {
            return keyGenerators.get(modelClass);
        }
        return jdbcEngineConfig.getKeyGenerator();
    }

    protected PersistExecutor getExecutorPersistExecutor(Class<?> modelClass, JdbcEngineConfig jdbcEngineConfig) {
        if (persistExecutors != null && persistExecutors.containsKey(modelClass)) {
            return persistExecutors.get(modelClass);
        }
        return jdbcEngineConfig.getPersistExecutor();
    }


    public Map<Class<?>, MappingHandler> getMappingHandlers() {
        return mappingHandlers;
    }

    public void setMappingHandlers(Map<Class<?>, MappingHandler> mappingHandlers) {
        this.mappingHandlers = mappingHandlers;
    }

    public Map<Class<?>, PageHandler> getPageHandlers() {
        return pageHandlers;
    }

    public void setPageHandlers(Map<Class<?>, PageHandler> pageHandlers) {
        this.pageHandlers = pageHandlers;
    }

    public Map<Class<?>, KeyGenerator> getKeyGenerators() {
        return keyGenerators;
    }

    public void setKeyGenerators(Map<Class<?>, KeyGenerator> keyGenerators) {
        this.keyGenerators = keyGenerators;
    }

    public Map<Class<?>, PersistExecutor> getPersistExecutors() {
        return persistExecutors;
    }

    public void setPersistExecutors(Map<Class<?>, PersistExecutor> persistExecutors) {
        this.persistExecutors = persistExecutors;
    }
}
