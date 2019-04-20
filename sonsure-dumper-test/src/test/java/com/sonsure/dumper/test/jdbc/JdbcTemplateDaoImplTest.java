package com.sonsure.dumper.test.jdbc;

import com.sonsure.dumper.core.persist.JdbcDao;
import com.sonsure.dumper.test.model.UserInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by liyd on 17/4/12.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class JdbcTemplateDaoImplTest {

    @Autowired
    protected JdbcDao jdbcDao;

    //    @Before
    public void before() {
        //初始化测试数据
        jdbcDao.createDelete().from(UserInfo.class)
                .execute();
        for (int i = 1; i < 51; i++) {
            UserInfo user = new UserInfo();
            user.setUserInfoId(Long.valueOf(i));
            user.setLoginName("name-" + i);
            user.setPassword("123456-" + i);
            user.setUserAge(i);
            user.setGmtCreate(new Date());

            jdbcDao.insert(user);
        }
    }


    @Test
    public void jdbcDaoGet() {
        UserInfo user = jdbcDao.get(UserInfo.class, 30L);
        Assert.assertNotNull(user);
        Assert.assertTrue(user.getUserInfoId().equals(30L));
        Assert.assertTrue(user.getLoginName().equals("name-30"));
        Assert.assertTrue(user.getPassword().equals("123456-30"));
    }


    @Test
    public void jdbcDaoInsert() {

        UserInfo user = new UserInfo();
        user.setLoginName("liyd2017");
        user.setPassword("2017");
        user.setUserAge(18);
        user.setGmtCreate(new Date());

        Long id = (Long) jdbcDao.insert(user);
        Assert.assertTrue(id > 0);

        UserInfo userInfo = jdbcDao.get(UserInfo.class, id);
        Assert.assertTrue(user.getLoginName().equals(userInfo.getLoginName()));
        Assert.assertTrue(user.getPassword().equals(userInfo.getPassword()));
    }


    @Test
    public void jdbcDaoUpdate() {
        UserInfo user = new UserInfo();
        user.setUserInfoId(20L);
        user.setPassword("666666");
        user.setLoginName("666777");
        user.setGmtModify(new Date());
        int count = jdbcDao.update(user);
        Assert.assertTrue(count == 1);

        UserInfo user1 = jdbcDao.get(UserInfo.class, 20L);
        Assert.assertNotNull(user1);
        Assert.assertTrue(user1.getUserInfoId().equals(20L));
        Assert.assertTrue(user1.getLoginName().equals("666777"));
        Assert.assertTrue(user1.getPassword().equals("666666"));
        Assert.assertNotNull(user1.getGmtModify());
    }

    @Test
    public void jdbcDaoDelete() {
        int count = jdbcDao.delete(UserInfo.class, 38L);
        Assert.assertTrue(count == 1);
        UserInfo user = jdbcDao.get(UserInfo.class, 38L);
        Assert.assertNull(user);
    }
//
//
//    @Test
//    public void jdbcDaoQueryAll() {
//        List<UserInfo> users = jdbcDao.queryAll(UserInfo.class);
//        Assert.assertNotNull(users);
//        Assert.assertTrue(users.size() == 50);
//    }
//
//    @Test
//    public void jdbcDaoQueryList() {
//        UserInfo user = new UserInfo();
//        user.setUserAge(10);
//        List<UserInfo> users = jdbcDao.queryList(user);
//        Assert.assertNotNull(users);
//        Assert.assertTrue(users.size() == 1);
//    }
//
//    @Test
//    public void jdbcDaoQueryPageList() {
//        UserInfo user = new UserInfo();
//        user.setPageSize(10);
//        Page<UserInfo> page = jdbcDao.queryPageList(user);
//        Assert.assertNotNull(page);
//        Assert.assertTrue(page.getList().size() == 10);
//    }
//
//
//    @Test
//    public void jdbcDaoQueryPageList2() {
//        UserInfo user = new UserInfo();
//        user.setPageSize(10);
//        user.setUserAge(10);
//        Page<UserInfo> page = jdbcDao.queryPageList(user);
//        Assert.assertNotNull(page);
//        Assert.assertTrue(page.getList().size() == 1);
//    }
//
//
//    @Test
//    public void jdbcDaoQueryCount() {
//        UserInfo user = new UserInfo();
//        user.setUserAge(10);
//        long count = jdbcDao.queryCount(user);
//        Assert.assertTrue(count == 1);
//    }
//
//    @Test
//    public void jdbcDaoQueryCount2() {
//        UserInfo user = new UserInfo();
//        long count = jdbcDao.queryCount(user);
//        Assert.assertTrue(count == 50);
//    }
//
//    @Test
//    public void jdbcDaoQuerySingleResult() {
//        UserInfo tmp = new UserInfo();
//        tmp.setUserAge(10);
//        UserInfo user = jdbcDao.querySingleResult(tmp);
//        Assert.assertNotNull(user);
//        Assert.assertTrue(user.getUserInfoId().equals(10L));
//        Assert.assertTrue(user.getLoginName().equals("name-10"));
//        Assert.assertTrue(user.getUserAge().equals(10));
//        Assert.assertNotNull(user.getGmtCreate());
//    }
//
//
//    @Test
//    public void jdbcDaoQueryFirstResult() {
//        UserInfo tmp = new UserInfo();
//        tmp.setUserAge(10);
//        UserInfo user = jdbcDao.queryFirstResult(tmp);
//        Assert.assertNotNull(user);
//        Assert.assertTrue(user.getUserInfoId().equals(10L));
//        Assert.assertTrue(user.getLoginName().equals("name-10"));
//        Assert.assertTrue(user.getUserAge().equals(10));
//        Assert.assertNotNull(user.getGmtCreate());
//    }
//
//    @Test
//    public void jdbcDaoDelete2() {
//        UserInfo user = new UserInfo();
//        user.setLoginName("name-17");
//        int count = jdbcDao.delete(user);
//        Assert.assertTrue(count == 1);
//
//        UserInfo tmp = new UserInfo();
//        tmp.setLoginName("name-17");
//        UserInfo user1 = jdbcDao.querySingleResult(tmp);
//        Assert.assertNull(user1);
//    }
//
//    @Test
//    public void insert() {
//
//        UserInfo user = new UserInfo();
//        user.setLoginName("name-60");
//        user.setPassword("606060");
//        user.setUserAge(60);
//        user.setGmtCreate(new Date());
//
//        Long id = (Long) jdbcDao.createInsert(UserInfo.class).setForEntity(user).execute();
//
//        UserInfo user1 = jdbcDao.get(UserInfo.class, id);
//        Assert.assertNotNull(user1);
//        Assert.assertTrue(user1.getUserInfoId().equals(id));
//        Assert.assertTrue(user1.getLoginName().equals("name-60"));
//        Assert.assertTrue(user1.getPassword().equals("606060"));
//    }
//
//    @Test
//    public void insert2() {
//
//        Long id = (Long) jdbcDao.createInsert(UserInfo.class)
//                .set("loginName", "name123")
//                .set("password", "123321")
//                .execute();
//
//        UserInfo user1 = jdbcDao.get(UserInfo.class, id);
//        Assert.assertNotNull(user1);
//        Assert.assertTrue(user1.getUserInfoId().equals(id));
//        Assert.assertTrue(user1.getLoginName().equals("name123"));
//        Assert.assertTrue(user1.getPassword().equals("123321"));
//    }
//
//    @Test
//    public void insertForAnnotation() {
//
//        KUserInfo ku = new KUserInfo();
//        ku.setLoginName("selfly");
//        ku.setPassword("123456");
//        ku.setUserAge(18);
//        ku.setGmtCreate(new Date());
//        ku.setGmtModify(new Date());
//
//        jdbcDao.insert(ku);
//    }
//
//    @Test
//    public void select() {
//
//        for (int i = 60; i < 70; i++) {
//            UserInfo user = new UserInfo();
//            user.setUserInfoId(Long.valueOf(i));
//            user.setLoginName("name2-19");
//            user.setPassword("123456-" + i);
//            user.setUserAge(19);
//            user.setGmtCreate(new Date());
//            jdbcDao.insert(user);
//        }
//
//        Select<UserInfo> select1 = jdbcDao.createSelect(UserInfo.class)
//                .where("userAge", 19);
//        long count1 = select1.count();
//        Assert.assertTrue(count1 == 11);
//
//        List<UserInfo> list1 = select1.list();
//        Assert.assertTrue(list1.size() == 11);
//
//        Page<UserInfo> page1 = select1.pageList(1, 5);
//        Assert.assertTrue(page1.getList().size() == 5);
//
//        Select<UserInfo> select2 = jdbcDao.createSelect(UserInfo.class)
//                .where("userAge", 19)
//                .and("loginName", "name2-19");
//        long count2 = select2.count();
//        Assert.assertTrue(count2 == 10);
//
//        List<UserInfo> list2 = select2.list();
//        Assert.assertTrue(list2.size() == 10);
//
//        Page<UserInfo> page2 = select2.pageList(1, 5);
//        Assert.assertTrue(page2.getList().size() == 5);
//    }
//
//    @Test
//    public void select2() {
//
//        try {
//            UserInfo user = jdbcDao.createSelect(UserInfo.class)
//                    .where()
//                    .begin()
//                    .condition("loginName", "name-19")
//                    .or("userAge", 21)
//                    .end()
//                    .singleResult();
//        } catch (IncorrectResultSizeDataAccessException e) {
//            Assert.assertTrue("Incorrect result size: expected 1, actual 2".equals(e.getMessage()));
//        }
//
//        UserInfo user2 = jdbcDao.createSelect(UserInfo.class)
//                .where()
//                .begin()
//                .condition("loginName", "name-19")
//                .or("userAge", 21)
//                .end()
//                .and("password", "123456-19")
//                .singleResult();
//        Assert.assertNotNull(user2);
//    }
//
//    @Test
//    public void select3() {
//
//        List<UserInfo> list = jdbcDao.createSelect(UserInfo.class)
//                .include("userInfoId", "password")
//                .where("userAge", "<=", 10)
//                .list();
//        Assert.assertNotNull(list);
//        Assert.assertTrue(list.size() == 10);
//        for (UserInfo user : list) {
//            Assert.assertNotNull(user.getUserInfoId());
//            Assert.assertNotNull(user.getPassword());
//            Assert.assertNull(user.getLoginName());
//            Assert.assertNull(user.getUserAge());
//            Assert.assertNull(user.getGmtCreate());
//        }
//    }
//
//    @Test
//    public void select4() {
//
//        List<UserInfo> list = jdbcDao.createSelect(UserInfo.class)
//                .exclude("userInfoId", "password")
//                .orderBy("userAge").asc()
//                .list();
//        Assert.assertNotNull(list);
//        int preAge = 0;
//        for (UserInfo user : list) {
//            Assert.assertNull(user.getUserInfoId());
//            Assert.assertNull(user.getPassword());
//            Assert.assertNotNull(user.getLoginName());
//            Assert.assertNotNull(user.getUserAge());
//            Assert.assertNotNull(user.getGmtCreate());
//            Assert.assertTrue(user.getUserAge() > preAge);
//            preAge = user.getUserAge();
//        }
//    }
//
//    @Test
//    public void select5() {
//
//        Long maxId = jdbcDao.createSelect(UserInfo.class)
//                .extraField("max(userInfoId) maxid")
//                .notSelectEntityField()
//                .oneColResult(Long.class);
//        Assert.assertTrue(maxId == 50);
//    }
//
//    @Test
//    public void select6() {
//        List<Long> ids = jdbcDao.createSelect(UserInfo.class)
//                .include("userInfoId")
//                .oneColList(Long.class);
//        Assert.assertNotNull(ids);
//    }
//
//
//    @Test
//    public void select8() {
//        Object result = jdbcDao.createSelect(UserInfo.class)
//                .where("userInfoId", 15L)
//                .objResult();
//        Assert.assertTrue(result instanceof Map);
//    }
//
//    @Test
//    public void select9() {
//        Object result = jdbcDao.createSelect(UserInfo.class)
//                .where("userInfoId", 15L)
//                .forceResultToModel()
//                .objResult();
//        Assert.assertTrue(result instanceof UserInfo);
//    }
//
//    @Test
//    public void select10() {
//        Object result = jdbcDao.createSelect(UserInfo.class)
//                .objList();
//        Assert.assertTrue(result instanceof List);
//        Assert.assertTrue(((List) result).get(0) instanceof Map);
//    }
//
//    @Test
//    public void select11() {
//        UserInfo pageable = new UserInfo();
//        pageable.setPageSize(5);
//        Page<?> result = jdbcDao.createSelect(UserInfo.class)
//                .objPageList(pageable);
//        Assert.assertTrue(result.getList().size() == 5);
//        Assert.assertTrue(result.getList().get(0) instanceof Map);
//    }
//
//    @Test
//    public void select13() {
//        List<UserInfo> users = jdbcDao.createSelect(UserInfo.class)
//                .tableAlias("t1")
//                .where("userInfoId", new Object[]{11L, 12L, 13L})
//                .and("loginName", new Object[]{"name-11", "name-12", "name-13"})
//                .and()
//                .condition("userAge", new Object[]{11L, 12L, 13L})
//                .list();
//        Assert.assertTrue(users.size() == 3);
//    }
//
//    @Test
//    public void select14() {
//        List<UserInfo> users = jdbcDao.createSelect(UserInfo.class)
//                .where("userInfoId", ">", 10L)
//                .and()
//                .begin()
//                .condition("userAge", 5)
//                .or()
//                .condition("userAge", 8)
//                .or("userAge", new Object[]{7L, 9L})
//                .end()
//                .list();
//        Assert.assertNotNull(users);
//    }
//
//
//    @Test
//    public void select15() {
//
//
//        Select<UserInfo> select = jdbcDao.createSelect(UserInfo.class);
//        select.where("userAge", 5);
//
//        UserInfo user = new UserInfo();
//        user.setUserInfoId(10L);
//        user.setLoginName("liyd");
//        select.andConditionEntity(user);
//
//        List<UserInfo> users = select.list();
//        Assert.assertNotNull(users);
//    }
//
//
//    @Test
//    public void select16() {
//
//
//        Select<UserInfo> select = jdbcDao.createSelect(UserInfo.class);
//        //自动处理where情况
//        select.and("userAge", 15);
//
//        select.and("userInfoId", 10L);
//
//        List<UserInfo> users = select.list();
//        Assert.assertNotNull(users);
//    }
//
//
//    @Test
//    public void select17() {
//
//
//        Select<UserInfo> select = jdbcDao.createSelect(UserInfo.class);
//        select.where("userAge", 5);
//
//        UserInfo user = new UserInfo();
//        user.setUserInfoId(10L);
//        user.setLoginName("liyd");
//        select.conditionEntity(user, "or", "or");
//
//        List<UserInfo> users = select.list();
//        Assert.assertNotNull(users);
//    }
//
//    @Test
//    public void selectForAnnotation() {
//        Page<KUserInfo> page = jdbcDao.createSelect(KUserInfo.class)
//                .pageList(1, 10);
//        Assert.assertTrue(page.getList().size() > 0);
//        Assert.assertNotNull(page.getList().get(0).getLoginName());
//    }
//
//
//    @Test
//    public void groupByAndOrderBy() {
//        UserInfo user = new UserInfo();
//        user.setLoginName("name2-19");
//        user.setPassword("123456-");
//        user.setUserAge(19);
//        user.setGmtCreate(new Date());
//        jdbcDao.insert(user);
//
//        Select<UserInfo> select = jdbcDao.createSelect(UserInfo.class)
//                .extraField("count(*) Num")
//                .include("userAge")
//                .groupBy("userAge")
//                .orderBy("Num").desc()
//                .forceResultToModel();
//        Page<?> page = select.objPageList(1, 5);
//
//        Assert.assertTrue(page.getList().size() == 5);
//
//        List<?> objects = select.objList();
//        Assert.assertTrue(objects.size() == 50);
//    }
//
////    @Test
////    public void append() {
//
////        GenericTokenParser tokenParser = new GenericTokenParser(":", " ", new TokenHandler() {
////            @Override
////            public String handleToken(String s) {
////                return " ? ";
////            }
////        });
////
////        String sql = "select t.* from UserInfo t where t.userInfoId = ::userInfoId and userAge = :userAge ";
////
////        String parse = tokenParser.parse(sql);
////
////        System.out.println(parse);
//
//
////        Select<UserInfo> select = jdbcDao.createSelect(UserInfo.class);
////        select.where("userAge", ">", 5)
////                .append("and userInfoId = (select max(userInfoId) from UserInfo)");
////        UserInfo userInfo = select.singleResult();
////        Assert.assertNotNull(userInfo);
////    }
//
//    @Test
//    public void updateSet() {
//
//        jdbcDao.createUpdate(UserInfo.class)
//                .set("loginName", "newName")
//                .where("userInfoId", 15L)
//                .execute();
//
//        UserInfo user1 = jdbcDao.get(UserInfo.class, 15L);
//        Assert.assertTrue(user1.getLoginName().equals("newName"));
//    }
//
//    @Test
//    public void updateForEntity() {
//
//        UserInfo user = new UserInfo();
//        user.setUserInfoId(17L);
//        user.setLoginName("newName22");
//        user.setPassword("abc");
//        //没有设置where条件，将更新所有
//        jdbcDao.createUpdate(UserInfo.class)
//                .setForEntity(user)
//                .execute();
//
//        UserInfo user1 = jdbcDao.get(UserInfo.class, 17L);
//        Assert.assertTrue(user1.getLoginName().equals("newName22"));
//        Assert.assertTrue(user1.getPassword().equals("abc"));
//    }
//
//    @Test
//    public void updateNull() {
//
//        UserInfo user = new UserInfo();
//        user.setUserInfoId(17L);
//        jdbcDao.createUpdate(UserInfo.class)
//                .setForEntityWhereId(user)
//                .updateNull()
//                .execute();
//
//        UserInfo user1 = jdbcDao.get(UserInfo.class, 17L);
//        Assert.assertNull(user1.getLoginName());
//        Assert.assertNull(user1.getPassword());
//        Assert.assertNull(user1.getUserAge());
//        Assert.assertNull(user1.getGmtCreate());
//    }
//
//
//    @Test
//    public void updatePkNull() {
//
//        try {
//            UserInfo user = new UserInfo();
//            user.setLoginName("newName22");
//            user.setPassword("abc");
//            //没有设置where条件，将更新所有
//            jdbcDao.createUpdate(UserInfo.class)
//                    .setForEntityWhereId(user)
//                    .execute();
//        } catch (Exception e) {
//            Assert.assertEquals("主键属性值不能为空:userInfoId", e.getMessage());
//        }
//    }
//
//    @Test
//    public void updateAnnotation() {
//
//        KUserInfo ku = new KUserInfo();
//        ku.setUserInfoId(7L);
//        ku.setLoginName("777777");
//        ku.setPassword("787878");
//        ku.setGmtModify(new Date());
//        int count = jdbcDao.update(ku);
//        Assert.assertTrue(count == 1);
//    }
//
//
//    @Test
//    public void updateNative() {
//
//        jdbcDao.createUpdate(UserInfo.class)
//                .set("{{userAge}}", "userAge+1")
//                .where("userInfoId", 17L)
//                .execute();
//
//        UserInfo user = jdbcDao.get(UserInfo.class, 17L);
//        Assert.assertTrue(user.getUserAge() == 18);
//    }
//
//
//    @Test
//    public void nativeExecutor() {
//        int count = jdbcDao.createNativeExecutor()
//                .command("update UserInfo set loginName = ? where userInfoId = ?")
//                .parameters(new Object[]{"newName", 39L})
//                .update();
//        Assert.assertTrue(count == 1);
//        UserInfo user = jdbcDao.get(UserInfo.class, 39L);
//        Assert.assertEquals(user.getLoginName(), "newName");
//    }
//
//
//    @Test
//    public void nativeExecutor4() {
//        int count = jdbcDao.createNativeExecutor()
//                .command("update UserInfo set loginName = ? where userInfoId = ?")
//                .parameter("loginName", "newName")
//                .parameter("userInfoId", 39L)
//                .update();
//        Assert.assertTrue(count == 1);
//        UserInfo user = jdbcDao.get(UserInfo.class, 39L);
//        Assert.assertEquals(user.getLoginName(), "newName");
//    }
//
//
//    @Test
//    public void nativeExecutor5() {
//
//        Long result = jdbcDao.createNativeExecutor()
//                .command("select count(*) from UserInfo")
//                .count();
//        Assert.assertTrue(result == 50);
//    }
//
//    @Test
//    public void nativeExecutor7() {
//
//        jdbcDao.createNativeExecutor()
//                .command("update user_info set user_age = 18 where user_age < 18")
//                .forceNative(true)
//                .execute();
//
//        long count = jdbcDao.createSelect(UserInfo.class)
//                .where("userAge", "<", 18)
//                .count();
//        Assert.assertTrue(count == 0);
//    }
//
//    @Test
//    public void nativeExecutor71() {
//
//        jdbcDao.createNativeExecutor()
//                .command("update UserInfo set userAge = 18 where userAge < 18")
//                .execute();
//
//        long count = jdbcDao.createSelect(UserInfo.class)
//                .where("userAge", "<", 18)
//                .count();
//        Assert.assertTrue(count == 0);
//    }
//
//    @Test
//    public void nativeOneColResult() {
//        Integer integer = Jdbc.nativeExecutor()
//                .command("select sum(userAge) from UserInfo")
//                .oneColResult(Integer.class);
//        Assert.assertTrue(integer > 0);
//    }
//
//    @Test
//    public void mybatisExecutor1() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("id", 9L);
//        params.put("loginName", "name-9");
//
//        Object user = jdbcDao.createMyBatisExecutor()
//                .command("getUser")
//                .parameters(params)
//                .forceNative(true)
//                .singleResult();
//
//        Assert.assertNotNull(user);
//    }
//
//
//    @Test
//    public void mybatisExecutor2() {
//
//        Object user = jdbcDao.createMyBatisExecutor()
//                .command("getUser")
//                .parameter("id", 9L)
//                .parameter("loginName", "name-9")
//                .forceNative(true)
//                .singleResult();
//
//        Assert.assertNotNull(user);
//    }
//
//    @Test
//    public void mybatisExecutor3() {
//
//        UserInfo userInfo = new UserInfo();
//        userInfo.setUserInfoId(10L);
//        userInfo.setLoginName("name-10");
//
//        Object user = jdbcDao.createMyBatisExecutor()
//                .command("getUser2")
//                .parameter("user", userInfo)
//                .forceNative(true)
//                .singleResult();
//
//        Assert.assertNotNull(user);
//    }
//
//    @Test
//    public void mybatisExecutor4() {
//
//        List<String> names = new ArrayList<>();
//        names.add("name-8");
//        names.add("name-9");
//        names.add("name-10");
//
//        List<?> list = jdbcDao.createMyBatisExecutor()
//                .command("queryUserList")
//                .parameter("names", names)
//                .forceNative(true)
//                .list();
//
//        Assert.assertTrue(list.size() == 3);
//    }
//
//    @Test
//    public void mybatisExecutor5() {
//
//        int update = jdbcDao.createMyBatisExecutor()
//                .command("updateUser")
//                .parameter("loginName", "newName")
//                .parameter("userInfoId", 9L)
//                .forceNative(true)
//                .update();
//
//        Assert.assertTrue(update == 1);
//        UserInfo userInfo = jdbcDao.get(UserInfo.class, 9L);
//        Assert.assertTrue("newName".equals(userInfo.getLoginName()));
//    }
//
//
//    @Test
//    public void mybatisExecutor6() {
//        Object user = jdbcDao.createMyBatisExecutor()
//                .command("getUser3")
//                .parameter("userInfoId", 9L)
//                .parameter("loginName", "name-9")
//                .singleResult();
//
//        Assert.assertNotNull(user);
//    }
//
//
//    @Test
//    public void mybatisExecutor7() {
//        UserInfo userInfo = (UserInfo) jdbcDao.createMyBatisExecutor()
//                .command("getUser3")
//                .parameter("userInfoId", 9L)
//                .parameter("loginName", "name-9")
//                .resultClass(UserInfo.class)
//                .singleResult();
//
//        Assert.assertTrue("name-9".equals(userInfo.getLoginName()));
//    }
//
//    @Test
//    public void mybatisExecutor8() {
//        Page<?> page = jdbcDao.createMyBatisExecutor()
//                .command("queryUserList2")
//                .resultClass(UserInfo.class)
//                .pageList(1, 10);
//
//        Assert.assertTrue(page.getList().size() == 10);
//    }

}
