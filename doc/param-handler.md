# 特殊参数处理

使用此功能时，传入的参数不是某个具体的值，而是一个`NamedParamHandler`的实现类。

参考以下实际使用场景：

某个业务具体要更新的字段数不定，由前端业务人员指定，且更新的值在一定范围内取随机值，也由前端业务人员指定(前端开放书写类似sql的方式)，

且每行更新的数据值不能是同一个，例如更新1000行数据，需要1000个随机值。

此时无法在后台代码中预先处理与生成某个值，因为更新逻辑由业务人员在不同时间段的不同需求而来。

## `NamedParamHandler`接口定义

此种类型只支持named操作方式，`paramName`即在sql中定义的参数名，实现类可以根据此名称生成自己想要的值。

    public interface NamedParamHandler {
    
        /**
         * named方式时支持自定义生成参数值，例如随机数，
         * 返回的值只能是支持jdbc传参的简单类型，不能是list、数组等复杂对象
         *
         * @param paramName the param name
         * @return the value
         */
        Object getValue(String paramName);
    }

## 示例

以下实现了一个随机数生成器`JdbcRandomNamedParamHandler`，实际的值在10-30之间生成，具体可以查看`JdbcRandomNamedParamHandler`类的源码：

    String sql = "insert into UserInfo(userInfoId,loginName,password,userAge) values(:userInfoId,:loginName,:password,:random_z10_z30)";

    for (int i = 0; i < 10; i++) {
        jdbcDao.nativeExecutor()
                .namedParameter()
                .command(sql)
                .parameter("userInfoId", 99L + i)
                .parameter("loginName", "newName")
                .parameter("password", "123456")
                .parameter("random_z10_z30", new JdbcRandomNamedParamHandler())
                .insert();
    }