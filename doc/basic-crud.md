# 基本增删改查

## 添加

    //方式一，实体类保存
    UserInfo user = new UserInfo();
    user.setLoginName("liyd2017");
    user.setPassword("2017");
    user.setUserAge(18);
    user.setGmtCreate(new Date());
    Long id = (Long) Jdbc.executeInsert(user);

    //方式二，单个设置属性值
    Long id = (Long)Jdbc.insertInto(UserInfo.class)
            .set("loginName", "selfly")
            .set("password", "2019")
            .set("userAge", 18)
            .execute();
    
## 修改

    //方式一，实体类更新
    UserInfo user = new UserInfo();
    user.setUserInfoId(20L);
    user.setPassword("666666");
    user.setLoginName("666777");
    user.setGmtModify(new Date());
    int count = Jdbc.executeUpdate(user);
    
    //方式二，单个设置属性更新
    int count = Jdbc.update(UserInfo.class)
                    .set("loginName", "selfly666")
                    .set("password", "666999")
                    .set("gmtModify", new Date())
                    .where("userInfoId", 20L)
                    .execute();
                    
## 删除
    
    //根据主键删除
    int count = Jdbc.executeDelete(UserInfo.class, 38L);
    
    //实体类方式，不为空的属性为where条件
    UserInfo user = new UserInfo();
    user.setLoginName("name-17");
    int count = Jdbc.executeDelete(user);
    
    //删除对应表所表数据，即没有where条件
    int count = Jdbc.executeDelete(UserInfo.class);

## 查询

    //查询所有列表
    List<UserInfo> users = Jdbc.find(UserInfo.class);

    //实体类方式，不为空的属性为where条件
    UserInfo user = new UserInfo();
    user.setUserAge(10);
    List<UserInfo> users = Jdbc.find(user);

    //查询分页列表 UserInfo 继承了Pageable类
    UserInfo user = new UserInfo();
    user.setPageSize(10); //每页10条
    user.setUserAge(10);
    Page<UserInfo> page = Jdbc.pageResult(user);
    
    //执行count(*) 所有记录数
    long count = Jdbc.findCount(UserInfo.class);
    
    //执行count(*),不为空的属性为where条件
    UserInfo user = new UserInfo();
    user.setUserAge(10);
    long count = Jdbc.findCount(user);
    
    //查询单个结果，多于一条时抛出异常，无记录时返回null
    UserInfo tmp = new UserInfo();
    tmp.setUserAge(10);
    UserInfo user = Jdbc.singleResult(tmp);

    //获取第一条记录，多于一条时取第一条，无记录时返回null，有做分页处理不用担心满足条件的数据返回过多
    UserInfo tmp = new UserInfo();
    tmp.setUserAge(10);
    UserInfo user = Jdbc.firstResult(tmp);
