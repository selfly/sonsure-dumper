package com.sonsure.dumper.core.config;


import com.sonsure.dumper.core.command.sql.CommandConversionHandler;
import com.sonsure.dumper.core.command.sql.JSqlParserCommandConversionHandler;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.mapping.DefaultMappingHandler;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.NegotiatingPageHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.sql.DataSource;

/**
 * Created by liyd on 17/4/11.
 */
public abstract class AbstractJdbcEngineConfig implements JdbcEngineConfig {

    /**
     * 数据源
     */
    protected DataSource dataSource;

    /**
     * 名称
     */
    protected String name;

    /**
     * 是否默认
     */
    private boolean isDefault = true;

    /**
     * 执行器构建factory
     */
    protected CommandExecutorFactory commandExecutorFactory;

    /**
     * 默认映射处理
     */
    protected MappingHandler mappingHandler;

    /**
     * 分页处理器
     */
    protected PageHandler pageHandler;

    /**
     * 默认主键生成器
     */
    protected KeyGenerator keyGenerator;

    /**
     * 默认持久化处理
     */
    protected PersistExecutor persistExecutor;

    /**
     * 解析器
     */
    protected CommandConversionHandler commandConversionHandler;

    /**
     * mybatis SqlSessionFactory
     */
    protected SqlSessionFactory mybatisSqlSessionFactory;

    /**
     * command大小写
     */
    protected String commandCase;

    /**
     * 初始化
     */
    protected void init() {
        if (dataSource == null) {
            throw new SonsureJdbcException("dataSource不能为空");
        }
        if (commandExecutorFactory == null) {
            commandExecutorFactory = new CommandExecutorFactoryImpl();
        }
        if (mappingHandler == null) {
            mappingHandler = new DefaultMappingHandler();
        }
        if (pageHandler == null) {
            pageHandler = new NegotiatingPageHandler();
        }
        if (commandConversionHandler == null) {
            commandConversionHandler = new JSqlParserCommandConversionHandler(this.mappingHandler);
        }

        this.initPersistExecutor();

        if (persistExecutor == null) {
            throw new SonsureJdbcException("JdbcEngineConfig实现类未初始化PersistExecutor,class:" + this.getClass().getName());
        }
    }

    @Override
    public CommandExecutorFactory getCommandExecutorFactory() {
        if (commandExecutorFactory == null) {
            commandExecutorFactory = new CommandExecutorFactoryImpl();
        }
        return commandExecutorFactory;
    }

    @Override
    public MappingHandler getMappingHandler() {
        if (mappingHandler == null) {
            mappingHandler = new DefaultMappingHandler();
        }
        return mappingHandler;
    }

    @Override
    public PageHandler getPageHandler() {
        if (pageHandler == null) {
            pageHandler = new NegotiatingPageHandler();
        }
        return pageHandler;
    }


    @Override
    public CommandConversionHandler getCommandConversionHandler() {
        if (commandConversionHandler == null) {
            commandConversionHandler = new JSqlParserCommandConversionHandler(this.getMappingHandler());
        }
        return commandConversionHandler;
    }

    @Override
    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Override
    public PersistExecutor getPersistExecutor() {
        if (persistExecutor == null) {
            this.initPersistExecutor();
        }
        return persistExecutor;
    }

    public void setPersistExecutor(PersistExecutor persistExecutor) {
        this.persistExecutor = persistExecutor;
    }

    @Override
    public String getCommandCase() {
        return commandCase;
    }

    public void setCommandCase(String commandCase) {
        this.commandCase = commandCase;
    }

    @Override
    public SqlSessionFactory getMybatisSqlSessionFactory() {
        return mybatisSqlSessionFactory;
    }

    public void setMappingHandler(MappingHandler mappingHandler) {
        this.mappingHandler = mappingHandler;
    }

    public void setPageHandler(PageHandler pageHandler) {
        this.pageHandler = pageHandler;
    }

    public void setCommandConversionHandler(CommandConversionHandler commandConversionHandler) {
        this.commandConversionHandler = commandConversionHandler;
    }

    public void setMybatisSqlSessionFactory(SqlSessionFactory mybatisSqlSessionFactory) {
        this.mybatisSqlSessionFactory = mybatisSqlSessionFactory;
    }

    public void setCommandExecutorFactory(CommandExecutorFactory commandExecutorFactory) {
        this.commandExecutorFactory = commandExecutorFactory;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    /**
     * 初始化persistExecutor，具体由选型的子类执行
     */
    protected abstract void initPersistExecutor();

}
