package com.sonsure.dumper.core.config;


import com.sonsure.dumper.core.command.CommandExecutor;
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

    protected CommandExecutorFactory commandExecutorFactory;

    /**
     * 数据源
     */
    protected DataSource dataSource;

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
     * 实体类所在包，多个英文逗号分隔
     */
    protected String modelPackages;

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
    protected boolean commandUppercase;

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
            mappingHandler = new DefaultMappingHandler(this.modelPackages);
        }
        if (pageHandler == null) {
            pageHandler = new NegotiatingPageHandler();
        }
        if (commandConversionHandler == null) {
            commandConversionHandler = new JSqlParserCommandConversionHandler(this.mappingHandler);
        }

        this.doInit();

        if (persistExecutor == null) {
            throw new SonsureJdbcException("JdbcEngineConfig实现类未初始化PersistExecutor,class:" + this.getClass().getName());
        }
    }

    /**
     * 子类初始化
     */
    protected abstract void doInit();

    public CommandExecutor getCommandExecutor(Class<?> modelClass, Class<?> commandExecutorClass) {

        CommandExecutor commandExecutor = this.commandExecutorFactory.getCommandExecutor(modelClass, commandExecutorClass, this);

        if (commandExecutor == null) {
            throw new SonsureJdbcException(String.format("未找到相应的CommandExecutor,modelClass:%s,commandExecutorClass:%s", modelClass.getName(), commandExecutorClass.getName()));
        }
        return commandExecutor;
    }

    public JdbcEngine buildJdbcEngine(DataSource dataSource) {
        this.dataSource = dataSource;
        this.init();
        return new JdbcEngineImpl(this);
    }

    public CommandExecutorFactory getCommandExecutorFactory() {
        return commandExecutorFactory;
    }

    public void setCommandExecutorFactory(CommandExecutorFactory commandExecutorFactory) {
        this.commandExecutorFactory = commandExecutorFactory;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public CommandConversionHandler getCommandConversionHandler() {
        return commandConversionHandler;
    }

    public void setCommandConversionHandler(CommandConversionHandler commandConversionHandler) {
        this.commandConversionHandler = commandConversionHandler;
    }

    @Override
    public MappingHandler getMappingHandler() {
        return mappingHandler;
    }

    public void setMappingHandler(MappingHandler mappingHandler) {
        this.mappingHandler = mappingHandler;
    }

    public PageHandler getPageHandler() {
        return pageHandler;
    }

    public void setPageHandler(PageHandler pageHandler) {
        this.pageHandler = pageHandler;
    }

    @Override
    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public PersistExecutor getPersistExecutor() {
        return persistExecutor;
    }

    public void setPersistExecutor(PersistExecutor persistExecutor) {
        this.persistExecutor = persistExecutor;
    }

    @Override
    public String getModelPackages() {
        return modelPackages;
    }

    public void setModelPackages(String modelPackages) {
        this.modelPackages = modelPackages;
    }

    @Override
    public boolean isCommandUppercase() {
        return commandUppercase;
    }

    public void setCommandUppercase(boolean commandUppercase) {
        this.commandUppercase = commandUppercase;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public SqlSessionFactory getMybatisSqlSessionFactory() {
        return mybatisSqlSessionFactory;
    }

    public void setMybatisSqlSessionFactory(SqlSessionFactory mybatisSqlSessionFactory) {
        this.mybatisSqlSessionFactory = mybatisSqlSessionFactory;
    }
}
