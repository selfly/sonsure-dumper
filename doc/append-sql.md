# 拼接sql片断

某些场景下，使用类名或属性名这种规范的方式只能方便的组装部分sql，还有一部分sql需要用到函数等无法组装，

这时候就可以使用拼接sql片断的方式来实现。

## 示例一，传统方式

    UserInfo userInfo = daoTemplate.selectFrom(UserInfo.class)
            .where("userAge", ">", 5)
            .append("and userInfoId = (select max(t2.userInfoId) from UserInfo t2 where t2.userInfoId < ?)", 40)
            .singleResult(UserInfo.class);
    Assert.assertNotNull(userInfo);

## 示例二，也支持named方式

    Map<String, Object> params = new HashMap<>();
    params.put("userInfoId", 40L);
    UserInfo userInfo = daoTemplate.selectFrom(UserInfo.class)
            .namedParameter()
            .where("userAge", ">", 5)
            .append("and userInfoId = (select max(t2.userInfoId) from UserInfo t2 where t2.userInfoId < :userInfoId)", params)
            .singleResult(UserInfo.class);
    Assert.assertNotNull(userInfo);

