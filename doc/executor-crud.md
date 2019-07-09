# Insert|Update|Delete|Select条件自行组装

insert、update、delete、select都有对应的操作对象，当基本操作无法满足时可自行组装，也可以同样方式设置where条件。

注意：这里操作的都应该是实体类里面的属性名，而非数据库表的列名。

## insert操作

insert操作比较简单，没有什么复杂的where条件等设置。

    jdbcDao.insert() //or insertInto(UserInfo.class)
        .into(UserInfo.class)
        .set("loginName", "selfly")
        .set("password", "2019")
        .set("userAge", 18)
        .execute();
        
## update操作

实体类更新方式，注意这里虽然设置了主键值，但是没有设置where条件，将更新所有记录，主键值被忽略。

    UserInfo user = new UserInfo();
    //主键字段
    user.setUserInfoId(17L);
    user.setLoginName("newName22");
    user.setPassword("abc");
    
    //虽然设置了主键，但没有设置where条件，将更新所有
    jdbcDao.update(UserInfo.class)
            .setForEntity(user)
            .execute();
            
同样实体类更新，主键被设置成where条件，如果主键值为空将抛出异常。仅更新LoginName字段，null值属性被忽略。

    UserInfo user = new UserInfo();
    user.setUserInfoId(17L);
    user.setLoginName("newName22");
    jdbcDao.update(UserInfo.class)
            .setForEntityWhereId(user)
            .updateNull()
            .execute();
            
也可以这样混合使用

    jdbcDao.update(UserInfo.class)
        .set("loginName", "newName")
        .setForEntity(user)
        .where("userInfoId", 15L)
        .execute();
        
有时候可能确实想要将属性更新成null值，这时候可以显式指定。

    UserInfo user = new UserInfo();
    user.setUserInfoId(17L);
    user.setLoginName("newName22");
    jdbcDao.update(UserInfo.class)
            .setForEntityWhereId(user)
            .updateNull() //更新null值属性
            .execute();

## delete操作

也比较简单，没什么特别的，主要是where条件的设置。

    jdbcDao.delete()
        .from(UserInfo.class) //or deleteFrom(UserInfo.class)
        .where("userInfoId",17L)
        .execute();
        
## select操作

select操作时如果不指定排序默认按主键desc排序。

不指定select的属性，默认返回该实体类的所有属性。

    List<UserInfo> list = jdbcDao.selectFrom(UserInfo.class)
            .orderBy("userAge").asc()
            .list(UserInfo.class);
            
当只是不需要实体类的一二个属性又不想一个个在select里面写时，可以使用exclude排除：

    List<UserInfo> list = jdbcDao.selectFrom(UserInfo.class)
            .exclude("userInfoId", "password") //不返回的属性
            .orderBy("userAge").asc()
            .list(UserInfo.class);
            
select中不仅仅可以放属性，也可以放置一些在sql中支持查询的函数等：

    Page<Long> result = jdbcDao.select("count(*) as num").from(UserInfo.class)
            .groupBy("userAge")
            .orderBy("num").desc()
            .paginate(1, 5)
            .isCount(false)
            .oneColPageResult(Long.class);
            
分页查询，指定页码和每页条数：

    Page<UserInfo> page = jdbcDao.select("userInfoId")
                                .from(UserInfo.class)
                                .orderBy("userInfoId").asc()
                                .paginate(1, 10)
                                .pageResult(UserInfo.class);
                                
`paginate`方法支持传入页码、页大小，或者直接传入`Pageable`对象实例，这就表示如果你的实体或vo等对象继承自`Pageable`可以直接传入。

同时提供了`limit`方法，和`paginate`方法区别在于传入的是从第几条数据开始，查询多少条。

这里注意查询多少条也是页大小，返回的是所在页的数据而非精确的指定起始行开始的数据。例如下面示例指定了从15条开始查，指定了页大小为10，实际返回的是所在页第2页11条到20条的数据。

    Page<UserInfo> page = jdbcDao.select("userInfoId")
                                .from(UserInfo.class)
                                .orderBy("userInfoId").asc()
                                .limit(15,10)
                                .isCount(false)
                                .pageResult(UserInfo.class);
                       
当`paginate`和`limit`两个方法都被调用，以后调用的为准，即后面的覆盖前面的。
         
`isCount`可以指定是否进行count查询，即是否查询分页数据的总记录数。false情况下将只查询数据不查询记录数，减少一次sql执行。

            
## 查询返回结果方法说明

- long count() 对应sql的count，返回的是数字。
- <T> T singleResult(Class<T> cls) 返回单个结果，T一般是实体类class。
- Object singleResult() 返回结果不定没有对应的实体类情况下使用，一般返回类型为Map<String,Object>。注意该方法返回的是一行数据不是列数据。
- <T> T firstResult(Class<T> cls) 取结果集的第一条数据，T一般是实体类class。有经过分页处理因此全表查取第一条也毫无问题。
- Object firstResult() 取数据逻辑同上，返回结果同 singleResult().
- <T> T oneColResult(Class<T> clazz) 返回单列结果。T是单个值的基本类型，如Long、Integer、String。
- <T> List<T> oneColList(Class<T> clazz) 同上，只不过返回的结果是列表。
- <T> T oneColFirstResult(Class<T> clazz) 取数据逻辑同firstResult，T是单个值的基本类型。
- <T> List<T> list(Class<T> cls) 返回一个列表，T一般是实体类class。
- List<Object> list() 同上，返回结果不定没有对应的实体类情况下使用，一般返回类型为Map<String,Object>。
- <T> Page<T> pageResult(Class<T> cls) 返回分页的结果列表，调用之前需要先调用方法`paginate`或`limit`指定分页信息。
- Page<Object> pageResult() 同上，返回结果类型不定没有对应的实体类情况下使用。
- <T> Page<T> oneColPageResult(Class<T> clazz) 分页同上，返回结果类型为单个值的基本类型。


## where条件设置说明

where条件的设置常用的and、or等都与sql相对应一看就能理解。

以下是一些方法的说明。

- where() 仅仅添加一个where
- where(String field, Object value) 添加一个包含where关键字的条件
- where(String field, Object[] value) 添加一个包含where关键字的条件，value是数组，sql将被组装成 in (?,?,?)
- where(String field, String operator, Object... values) 指定了属性操作符，可以是=、!=、in、not in、like等。
- condition(String field, Object value) 添加一个条件，不带and、or等任何操作符
- condition(String field, Object[] value) 同上，将组装成in
- condition(String field, String operator, Object... values) 同上，指定了属性操作符
- conditionEntity(Object entity) 根据一个实体对象中不为null的属性组装where条件
- andConditionEntity(Object entity) 同上，会在组装的条件前面添加and
- conditionEntity(Object entity, String wholeLogicalOperator, String fieldLogicalOperator) 同上，指定了逻辑连接操作符及属性操作符。可以组装出 `and (name = ? or password = ?)`或`or (name = ? and password = ?)`类似sql。
- and() 仅仅添加一个and
- and(String field, Object value) and一个条件
- and(String field, Object[] value) and一个条件，将组装成in
- and(String field, String operator, Object... values) 同上，指定了属性操作符
- or() 仅仅添加一个or
- or(String field, Object value) or一个条件
- or(String field, Object[] value) or一个条件，将组装成in
- or(String field, String operator, Object... values) 同上，指定了属性操作符
- begin() 添加一个开始的括号
- end() 添加一个结束的括号
- append(String segment, Object... params) 拼接一个sql片断