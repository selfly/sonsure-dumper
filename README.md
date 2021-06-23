# Sonsure-Dumper

简单、快速、易用的jdbc持久化操作层。

**4.0.0版本主要更新内容：**

- 更新最低jdk版本到1.8，参数设置增加lambda方式支持
- 支持parameterNamed方式
- 增加DaoTemplate抽象层，当仍然想要规范化的建立与实体类对应的dao时，可以使用该DaoTemplate
- 增加批量更新方法
- 增加脚本执行方法
- 增加自定义executor执行方法
- 支持OSGi环境
- 升级jsqlparser到3.2


## 示例

    //根据主键获取
    User user = jdbcDao.get(User.class, 177);
    
    //查询所有列表
    List<User> users = jdbcDao.find(User.class);
    
    //查询分页列表
    Page<User> page = jdbcDao.pageResult(user);
    
    //会根据user对象中不为null的属性做为条件查询
    long count = jdbcDao.findCount(user);
    
    //会根据user对象中不为null的属性做为条件查询 没有数据时返回null，超过一条时会抛出异常
    User user = jdbcDao.singleResult(user);
    
    //超过一条时取第一条，有经过分页处理，全表查取第一条也不用担心返回数据过多
    User user = jdbcDao.firstResult(user);
    
    //会根据主键策略自动处理主键，如果实体对象中设置了主键值则不再处理
    //返回的主键值类型根据生成的主键不同可自行转换
    Long id = (Long)jdbcDao.executeInsert(user);
    
    //根据主键更新实体对象
    jdbcDao.executeUpdate(user);
    
    //根据主键删除实体对象
    jdbcDao.executeDelete(User.class, 1800081L);
    
    //SQL: update User set user_age = user_age + 1 where user_info_id = 17
    jdbcDao.update(User.class)
            .set("{{userAge}}", "userAge+1")
            .where("userInfoId", 17L)
            .execute();
    
    //SQL: select user_age, count(*) num from user_info group by user_age order by num desc limit 0,10
    Page<Object> page = jdbcDao.select("userAge,count(*) Num")
            .from(UserInfo.class)
            .groupBy(UserInfo::getUserAge()) //4.0版开始属性推荐使用lambda方式，防止拼错
            .orderBy("Num").desc()
            .paginate(1, 10)
            .isCount(false)
            .pageResult();
 
## 特点

- 省去各类dao或baseDao，甚至不需要声明或注入dao
- 学习成本低，api跟sql高度一致，会sql即会使用
- 条件设置支持各类符号，=、!=、or、in、not in甚至是执行函数
- 允许指定排序字段，可以指定多个组合升降自由排序
- 支持显示指定或排除某个字段，以及添加额外的函数类字段，只取想要的数据
- 内置分页功能，自动进行分页处理无需再写分页代码
- 可以使用`{{}}`符号完成一些特殊的sql，例如`user_age = user_age + 1`这种不适合传参的情况
- 支持native方式执行自定义sql
- 支持整合Mybatis，以Mybatis的方式书写sql
- 易扩展，各组件如主键生成器、分页器、命令构建器(sql/hql等)、持久化实现等均可扩展或重写

> 组件本质上也只是封装了一个通用dao，只不过更加方便易用。当发现组件缺少某项功能或不能满足需求时仍可以用本来原生的方式执行，完全无影响。

#### 使用

添加依赖，默认使用`Spring Jdbc`实现，可更换成自己想要的实现：

    <dependency>
        <groupId>com.sonsure</groupId>
        <artifactId>sonsure-dumper-springjdbc</artifactId>
        <version>${version}</version>
    </dependency>
    
声明Bean，更多参数详见相关配置文档：

    <bean id="jdbcDao" class="com.sonsure.dumper.springjdbc.persist.SpringJdbcDaoTemplateImpl">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    
传统注入方式使用JdbcDao：

    //不管哪个实体对象都使用该JdbcDao就可以
    @Autowired
    private JdbcDao jdbcDao;
    
    jdbcDao.get(User.class, 177);
    
如果开启了全局模式，可以直接使用Jdbc操作：

    User user = Jdbc.get(User.class, 177);

## 相关文档

说明文档中可能会有xml方式配置或JavaConfig方式配置，两种方式效果是一样的，可根据情况自行转换。

1. [约定](doc/usage.md)
2. [初始化配置](doc/init-config.md)  
3. [基本增删改查](doc/basic-crud.md)  
4. [Insert|Update|Delete|Select用法](doc/executor-crud.md)
5. [不传参{{ }}符号的使用](doc/not-param.md)
6. [注解的使用](doc/use-annotation.md)
7. [执行自定义sql](doc/native-sql.md)
8. [整合Mybatis执行sql MybatisSqlSessionFactory](doc/mybatis-sql.md)
9. [映射转换处理 MappingHandler](doc/mapping-handler.md)
10. [分页处理 PageHandler](doc/page-handler.md)
11. [主键生成 KeyGenerator](doc/key-generator.md)
12. [持久化实现 PersistExecutor，扩展Hibernate示例抛砖引玉](doc/persist-executor-hibernate.md)
13. [sql的解析转换 CommandConversionHandler](doc/conversion-handler.md)
14. [扩展自定义的执行方式 CommandExecutor](doc/ext-executor.md)
15. [多数据源的使用](doc/multi-ds.md)
16. [执行脚本](doc/execute-script.md)

## 参与贡献

github：[https://github.com/selfly/sonsure-dumper](https://github.com/selfly/sonsure-dumper)  
gitee: [https://gitee.com/selfly/sonsure-dumper](https://gitee.com/selfly/sonsure-dumper)

1. Fork 本项目
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request

