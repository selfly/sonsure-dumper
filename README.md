# sonsure-dumper

简单、快速、易用的jdbc持久化操作层。

#### 示例

    //根据主键获取
    User user = Jdbc.get(User.class, 177);
    
    //查询所有列表
    List<User> users = Jdbc.find(User.class);
    
    //查询分页列表
    Page<User> page = Jdbc.pageResult(user);
    
    //会根据user对象中不为null的属性做为条件查询
    long count = Jdbc.findCount(user);
    
    //会根据user对象中不为null的属性做为条件查询 没有数据时返回null，超过一条时会抛出异常
    User user = Jdbc.singleResult(user);
    
    //超过一条时取第一条，有经过分页处理，全表查取第一条也不用担心返回数据过多
    User user = Jdbc.firstResult(user);
    
    //会根据主键策略自动处理主键，如果实体对象中设置了主键值则不再处理
    //返回的主键值类型根据生成的主键不同可自行转换
    Long id = (Long)Jdbc.executeInsert(user);
    
    //根据主键更新实体对象
    Jdbc.executeUpdate(user);
    
    //根据主键删除实体对象
    Jdbc.executeDelete(User.class, 1800081L);
    
    //SQL: update User set user_age = user_age + 1 where user_age = 17
    Jdbc.update(User.class)
            .set("{{userAge}}", "userAge+1")
            .where("userInfoId", 17L)
            .execute();
    
    //SQL: select user_age, count(*) num from user_info group by user_age order by num desc limit 0,10
    Page<Object> page = Jdbc.select("userAge,count(*) Num")
            .from(UserInfo.class)
            .groupBy("userAge")
            .orderBy("Num").desc()
            .paginate(1, 10)
            .isCount(false)
            .pageResult();
 
#### 特点

- 省去各类dao或baseDao，甚至不需要声明或注入dao
- 学习成本低，api跟sql高度一致，会sql即会使用
- 条件设置支持各类符号，=、!=、or、in、not in甚至是执行函数
- 允许指定排序字段，可以指定多个组合升降自由排序
- 支持显示指定或排除某个字段，以及添加额外的函数类字段，只取想要的数据
- 内置分页功能，自动进行分页处理无需再写分页代码
- 可能使用`{{}}`符号完成一些特殊的sql，例如`user_age = user_age + 1`这种不适合传参的情况
- 支持native方式执行自定义sql
- 支持整合Mybatis，以Mybatis的方式书写sql
- 易扩展，各组件如主键生成器、分页器、命令构建器(sql/hql等)、持久化实现等均可扩展或重写

#### 使用

添加依赖，默认使用`Spring jdbc`实现，可更换成自己想要的实现：

    <dependency>
        <groupId>com.sonsure</groupId>
        <artifactId>sonsure-dumper-springjdbc</artifactId>
        <version>${version}</version>
    </dependency>
    
声明Bean，如果不需要以传统dao注入方式使用，可以省略dao声明，详见相关配置文档：

    <bean id="jdbcDao" class="com.sonsure.dumper.springjdbc.persist.JdbcTemplateDaoImpl">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    
传统注入方式使用JdbcDao：

    //不管哪个实体对象都使用该JdbcDao就可以
    @Autowired
    private JdbcDao jdbcDao;
    
    jdbcDao.get(User.class, 177);
    
直接使用Jdbc操作：

    User user = Jdbc.get(User.class, 177);

#### 相关文档



#### 参与贡献

github：[https://github.com/selfly/sonsure-dumper](https://github.com/selfly/sonsure-dumper)

gitee: [https://gitee.com/selfly/sonsure-dumper](https://gitee.com/selfly/sonsure-dumper)

1. Fork 本项目
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request

