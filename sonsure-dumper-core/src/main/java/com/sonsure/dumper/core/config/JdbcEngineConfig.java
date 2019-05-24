package com.sonsure.dumper.core.config;


import com.sonsure.dumper.core.command.sql.CommandConversionHandler;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * jdbc配置类
 * <p>
 * Created by liyd on 17/4/11.
 */
public interface JdbcEngineConfig {

    /**
     * 名称
     *
     * @return
     */
    String getName();

    /**
     * 是否默认
     *
     * @return
     */
    boolean isDefault();

    /**
     * 获取CommandExecutorFactory
     *
     * @return
     */
    CommandExecutorFactory getCommandExecutorFactory();

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
     * 获取command解析器
     *
     * @return
     */
    CommandConversionHandler getCommandConversionHandler();

    /**
     * 获取mybatis SqlSessionFactory
     *
     * @return
     */
    SqlSessionFactory getMybatisSqlSessionFactory();

    /**
     * command大小写
     *
     * @return
     */
    String getCommandCase();

    /**
     * 是否启用全局jdbc
     *
     * @return
     */
    boolean isEnableGlobalJdbc();

}
