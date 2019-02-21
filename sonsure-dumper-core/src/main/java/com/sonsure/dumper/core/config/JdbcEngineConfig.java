package com.sonsure.dumper.core.config;


import com.sonsure.dumper.core.command.CommandExecutor;
import com.sonsure.dumper.core.command.sql.CommandConversionHandler;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.sql.DataSource;

/**
 * jdbc配置类
 * <p>
 * Created by liyd on 17/4/11.
 */
public interface JdbcEngineConfig {

    /**
     * 获取CommandExecutor
     *
     * @param modelClass           the model class
     * @param commandExecutorClass the command executor class
     * @return command executor
     */
    CommandExecutor getCommandExecutor(Class<?> modelClass, Class<?> commandExecutorClass);

    /**
     * 获取MappingHandler
     * 先根据type获取，如果没有再根据topInterfaceClass获取
     *
     * @return mapping handler
     */
    MappingHandler getMappingHandler();

    /**
     * 获取PageHandler
     * 先根据type获取，如果没有再根据topInterfaceClass获取
     *
     * @return page handler
     */
    PageHandler getPageHandler();

    /**
     * 获取KeyGenerator
     * 先根据type获取，如果没有再根据topInterfaceClass获取
     *
     * @return key generator
     */
    KeyGenerator getKeyGenerator();

    /**
     * 获取持久化执行器
     *
     * @return
     */
    PersistExecutor getPersistExecutor();

    /**
     * command大小写
     *
     * @return
     */
    String getCommandCase();

    /**
     * 获取实体所在包，native方式时使用
     *
     * @return
     */
    String getModelPackages();

    /**
     * 获取mybatis SqlSessionFactory
     *
     * @return
     */
    SqlSessionFactory getMybatisSqlSessionFactory();


    /**
     * 获取command解析器
     *
     * @return
     */
    CommandConversionHandler getCommandConversionHandler();

    /**
     * 获取JdbcEngine
     *
     * @param dataSource the data source
     * @return jdbc engine
     */
    JdbcEngine buildJdbcEngine(DataSource dataSource);

}
