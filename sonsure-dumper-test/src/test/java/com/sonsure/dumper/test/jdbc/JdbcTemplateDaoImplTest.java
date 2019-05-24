package com.sonsure.dumper.test.jdbc;

import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;
import com.sonsure.dumper.core.command.entity.Select;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.persist.JdbcDao;
import com.sonsure.dumper.test.model.AuthCode;
import com.sonsure.dumper.test.model.KUserInfo;
import com.sonsure.dumper.test.model.UidUser;
import com.sonsure.dumper.test.model.UserInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;
import java.util.*;

/**
 * Created by liyd on 17/4/12.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class JdbcTemplateDaoImplTest {

    @Autowired
    protected JdbcDao jdbcDao;

    @Before
    public void before() {
        //初始化测试数据
        jdbcDao.deleteFrom(UserInfo.class)
                .execute();
        for (int i = 1; i < 51; i++) {
            UserInfo user = new UserInfo();
            user.setUserInfoId(Long.valueOf(i));
            user.setLoginName("name-" + i);
            user.setPassword("123456-" + i);
            user.setUserAge(i);
            user.setGmtCreate(new Date());

            jdbcDao.executeInsert(user);
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

        Long id = (Long) jdbcDao.executeInsert(user);
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
        int count = jdbcDao.executeUpdate(user);
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
        int count = jdbcDao.executeDelete(UserInfo.class, 38L);
        Assert.assertTrue(count == 1);
        UserInfo user = jdbcDao.get(UserInfo.class, 38L);
        Assert.assertNull(user);
    }


    @Test
    public void jdbcDaoFind() {
        List<UserInfo> users = jdbcDao.find(UserInfo.class);
        Assert.assertNotNull(users);
        Assert.assertTrue(users.size() == 50);
    }

    @Test
    public void jdbcDaoFind1() {
        UserInfo user = new UserInfo();
        user.setUserAge(10);
        List<UserInfo> users = jdbcDao.find(user);
        Assert.assertNotNull(users);
        Assert.assertTrue(users.size() == 1);
    }

    @Test
    public void jdbcDaoPageResult() {
        UserInfo user = new UserInfo();
        user.setPageSize(10);
        Page<UserInfo> page = jdbcDao.pageResult(user);
        Assert.assertNotNull(page);
        Assert.assertTrue(page.getList().size() == 10);
    }


    @Test
    public void jdbcDaoPageResult2() {
        UserInfo user = new UserInfo();
        user.setPageSize(10);
        user.setUserAge(10);
        Page<UserInfo> page = jdbcDao.pageResult(user);
        Assert.assertNotNull(page);
        Assert.assertTrue(page.getList().size() == 1);
    }

    @Test
    public void jdbcDaoPageResult3() {
        Page<UserInfo> page = jdbcDao.selectFrom(UserInfo.class)
                .orderBy("userInfoId").asc()
                .limit(15, 10)
                .pageResult(UserInfo.class);
        Assert.assertTrue(page.getList().get(0).getUserInfoId().equals(11L));
        Assert.assertTrue(page.getList().get(9).getUserInfoId().equals(20L));
    }


    @Test
    public void jdbcDaoCount() {
        UserInfo user = new UserInfo();
        user.setUserAge(10);
        long count = jdbcDao.findCount(user);
        Assert.assertTrue(count == 1);
    }

    @Test
    public void jdbcDaoQueryCount2() {
        long count = jdbcDao.findCount(UserInfo.class);
        Assert.assertTrue(count == 50);
    }

    @Test
    public void jdbcDaoSingleResult() {
        UserInfo tmp = new UserInfo();
        tmp.setUserAge(10);
        UserInfo user = jdbcDao.singleResult(tmp);
        Assert.assertNotNull(user);
        Assert.assertTrue(user.getUserInfoId().equals(10L));
        Assert.assertTrue(user.getLoginName().equals("name-10"));
        Assert.assertTrue(user.getUserAge().equals(10));
        Assert.assertNotNull(user.getGmtCreate());
    }


    @Test
    public void jdbcDaoFirstResult() {
        UserInfo tmp = new UserInfo();
        tmp.setUserAge(10);
        UserInfo user = jdbcDao.firstResult(tmp);
        Assert.assertNotNull(user);
        Assert.assertTrue(user.getUserInfoId().equals(10L));
        Assert.assertTrue(user.getLoginName().equals("name-10"));
        Assert.assertTrue(user.getUserAge().equals(10));
        Assert.assertNotNull(user.getGmtCreate());
    }

    @Test
    public void jdbcDaoDelete2() {
        UserInfo user = new UserInfo();
        user.setLoginName("name-17");
        int count = jdbcDao.executeDelete(user);
        Assert.assertTrue(count == 1);

        UserInfo tmp = new UserInfo();
        tmp.setLoginName("name-17");
        UserInfo user1 = jdbcDao.singleResult(tmp);
        Assert.assertNull(user1);
    }

    @Test
    public void jdbcDaoDelete3() {
        int count = jdbcDao.executeDelete(UserInfo.class);
        Assert.assertTrue(count > 0);
        long result = jdbcDao.findCount(UserInfo.class);
        Assert.assertTrue(result == 0);
    }

    @Test
    public void insert() {

        UserInfo user = new UserInfo();
        user.setLoginName("name-60");
        user.setPassword("606060");
        user.setUserAge(60);
        user.setGmtCreate(new Date());

        Long id = (Long) jdbcDao.executeInsert(user);

        UserInfo user1 = jdbcDao.get(UserInfo.class, id);
        Assert.assertNotNull(user1);
        Assert.assertTrue(user1.getUserInfoId().equals(id));
        Assert.assertTrue(user1.getLoginName().equals("name-60"));
        Assert.assertTrue(user1.getPassword().equals("606060"));
    }

    @Test
    public void insert2() {

        Long id = (Long) jdbcDao.insertInto(UserInfo.class)
                .set("loginName", "name123")
                .set("password", "123321")
                .execute();

        UserInfo user1 = jdbcDao.get(UserInfo.class, id);
        Assert.assertNotNull(user1);
        Assert.assertTrue(user1.getUserInfoId().equals(id));
        Assert.assertTrue(user1.getLoginName().equals("name123"));
        Assert.assertTrue(user1.getPassword().equals("123321"));
    }

    @Test
    public void insertForAnnotation() {

        KUserInfo ku = new KUserInfo();
        ku.setLoginName("selfly");
        ku.setPassword("123456");
        ku.setUserAge(18);
        ku.setGmtCreate(new Date());
        ku.setGmtModify(new Date());

        Long id = (Long) jdbcDao.executeInsert(ku);

        KUserInfo kUserInfo = jdbcDao.get(KUserInfo.class, id);
        Assert.assertEquals("selfly", kUserInfo.getLoginName());
        Assert.assertEquals("123456", kUserInfo.getPassword());
    }

    @Test
    public void select() {

        jdbcDao.executeDelete(UserInfo.class);
        for (int i = 60; i < 70; i++) {
            UserInfo user = new UserInfo();
            user.setUserInfoId(Long.valueOf(i));
            user.setLoginName("name2-19");
            user.setPassword("123456-" + i);
            user.setUserAge(19);
            user.setGmtCreate(new Date());
            jdbcDao.executeInsert(user);
        }

        Select select1 = jdbcDao.selectFrom(UserInfo.class)
                .where("userAge", 19);
        long count1 = select1.count();
        Assert.assertTrue(count1 == 10);

        List<UserInfo> list1 = select1.list(UserInfo.class);
        Assert.assertTrue(list1.size() == 10);

        Page<UserInfo> page1 = select1.paginate(1, 5).pageResult(UserInfo.class);
        Assert.assertTrue(page1.getList().size() == 5);

        Select select2 = jdbcDao.selectFrom(UserInfo.class)
                .where("userAge", 19)
                .and("loginName", "name2-19");
        long count2 = select2.count();
        Assert.assertTrue(count2 == 10);

        List<UserInfo> list2 = select2.list(UserInfo.class);
        Assert.assertTrue(list2.size() == 10);

        Page<UserInfo> page2 = select2.paginate(1, 5).pageResult(UserInfo.class);
        Assert.assertTrue(page2.getList().size() == 5);
    }

    @Test
    public void select2() {

        jdbcDao.executeDelete(UserInfo.class);
        for (int i = 1; i < 3; i++) {
            UserInfo user = new UserInfo();
            user.setUserInfoId(Long.valueOf(i));
            user.setLoginName("name-19");
            user.setPassword("123456-" + i);
            user.setUserAge(21);
            user.setGmtCreate(new Date());
            jdbcDao.executeInsert(user);
        }
        try {
            UserInfo user = jdbcDao.selectFrom(UserInfo.class)
                    .where()
                    .begin()
                    .condition("loginName", "name-19")
                    .or("userAge", 21)
                    .end()
                    .singleResult(UserInfo.class);
        } catch (IncorrectResultSizeDataAccessException e) {
            Assert.assertTrue("Incorrect result size: expected 1, actual 2".equals(e.getMessage()));
        }

        UserInfo user2 = jdbcDao.selectFrom(UserInfo.class)
                .where()
                .begin()
                .condition("loginName", "name-19")
                .or("userAge", 21)
                .end()
                .and("password", "123456-1")
                .singleResult(UserInfo.class);
        Assert.assertNotNull(user2);
    }

    @Test
    public void select3() {

        List<UserInfo> list = jdbcDao.select("userInfoId", "password").from(UserInfo.class)
                .where("userAge", "<=", 10)
                .list(UserInfo.class);
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() == 10);
        for (UserInfo user : list) {
            Assert.assertNotNull(user.getUserInfoId());
            Assert.assertNotNull(user.getPassword());
            Assert.assertNull(user.getLoginName());
            Assert.assertNull(user.getUserAge());
            Assert.assertNull(user.getGmtCreate());
        }
    }

    @Test
    public void select4() {

        List<UserInfo> list = jdbcDao.selectFrom(UserInfo.class)
                .exclude("userInfoId", "password")
                .orderBy("userAge").asc()
                .list(UserInfo.class);
        Assert.assertNotNull(list);
        int preAge = 0;
        for (UserInfo user : list) {
            Assert.assertNull(user.getUserInfoId());
            Assert.assertNull(user.getPassword());
            Assert.assertNotNull(user.getLoginName());
            Assert.assertNotNull(user.getUserAge());
            Assert.assertNotNull(user.getGmtCreate());
            Assert.assertTrue(user.getUserAge() > preAge);
            preAge = user.getUserAge();
        }
    }

    @Test
    public void select5() {

        Long maxId = jdbcDao.select("max(userInfoId) maxid").from(UserInfo.class)
                .oneColResult(Long.class);
        Assert.assertTrue(maxId == 50);
    }

    @Test
    public void select6() {
        List<Long> ids = jdbcDao.select("userInfoId").from(UserInfo.class)
                .oneColList(Long.class);
        Assert.assertNotNull(ids);
    }


    @Test
    public void select8() {
        Object result = jdbcDao.selectFrom(UserInfo.class)
                .where("userInfoId", 15L)
                .singleResult();
        Assert.assertTrue(result instanceof Map);
    }

    @Test
    public void select10() {
        Object result = jdbcDao.selectFrom(UserInfo.class)
                .list();
        Assert.assertTrue(result instanceof List);
        Assert.assertTrue(((List) result).get(0) instanceof Map);
    }

    @Test
    public void select11() {
        UserInfo pageable = new UserInfo();
        pageable.setPageSize(5);
        Page<?> result = jdbcDao.selectFrom(UserInfo.class)
                .paginate(pageable)
                .pageResult();
        Assert.assertTrue(result.getList().size() == 5);
        Assert.assertTrue(result.getList().get(0) instanceof Map);
    }

    @Test
    public void select13() {
        List<UserInfo> users = jdbcDao.select().from(UserInfo.class, "t1")
                .where("t1.userInfoId", new Object[]{11L, 12L, 13L})
                .and("t1.loginName", new Object[]{"name-11", "name-12", "name-13"})
                .and()
                .condition("t1.userAge", new Object[]{11L, 12L, 13L})
                .list(UserInfo.class);
        Assert.assertTrue(users.size() == 3);
    }

    @Test
    public void select14() {
        List<UserInfo> users = jdbcDao.selectFrom(UserInfo.class)
                .where("userInfoId", ">", 10L)
                .and()
                .begin()
                .condition("userAge", 5)
                .or()
                .condition("userAge", 8)
                .or("userAge", new Object[]{7L, 9L})
                .end()
                .list(UserInfo.class);
        Assert.assertNotNull(users);
    }


    @Test
    public void select15() {


        Select select = jdbcDao.selectFrom(UserInfo.class);
        select.where("userAge", 5);

        UserInfo user = new UserInfo();
        user.setUserInfoId(10L);
        user.setLoginName("liyd");
        select.andConditionEntity(user);

        List<UserInfo> users = select.list(UserInfo.class);
        Assert.assertNotNull(users);
    }


    @Test
    public void select16() {


        Select select = jdbcDao.selectFrom(UserInfo.class);
        //自动处理where情况
        select.and("userAge", 15);

        select.and("userInfoId", 10L);

        List<UserInfo> users = select.list(UserInfo.class);
        Assert.assertNotNull(users);
    }


    @Test
    public void select17() {


        Select select = jdbcDao.selectFrom(UserInfo.class);
        select.where("userAge", 5);

        UserInfo user = new UserInfo();
        user.setUserInfoId(10L);
        user.setLoginName("liyd");
        select.conditionEntity(user, "and", "or");

        List<UserInfo> users = select.list(UserInfo.class);
        Assert.assertNotNull(users);
    }


    @Test
    public void select18() {
        try {
            Object result = jdbcDao.select()
                    .from(UserInfo.class)
                    .asc()
                    .firstResult();
        } catch (SonsureJdbcException e) {
            Assert.assertTrue("请先指定需要排序的属性".equals(e.getMessage()));
        }
    }

    @Test
    public void select19() {
        Object result = jdbcDao.select()
                .from(UserInfo.class)
                .orderBy("userInfoId").asc()
                .firstResult();
        Assert.assertTrue(result instanceof Map);
        Map<String, Object> map = (Map<String, Object>) result;
        Assert.assertTrue(map.get("USER_INFO_ID").equals(1L));
    }

    @Test
    public void select20() {
        Long result = jdbcDao.select("userInfoId")
                .from(UserInfo.class)
                .orderBy("userInfoId").asc()
                .oneColFirstResult(Long.class);
        Assert.assertTrue(result.equals(1L));
    }

    @Test
    public void select21() {
        Page<Long> page = jdbcDao.select("userInfoId")
                .from(UserInfo.class)
                .orderBy("userInfoId").asc()
                .paginate(1, 10)
                .oneColPageResult(Long.class);
        Assert.assertTrue(page.getList().size() == 10);
        Assert.assertTrue(page.getList().get(0).equals(1L));
    }


    @Test
    public void singleResultObject() {

        Object object = jdbcDao.select("loginName", "password")
                .from(UserInfo.class)
                .where("userInfoId", 5L)
                .singleResult();

        Assert.assertNotNull(object instanceof Map);
    }

    @Test
    public void singleResultObject2() {

        Object object = jdbcDao.select().from(UserInfo.class, "t1", UidUser.class, "t2")
                .where("{{t1.userInfoId}}", "t2.uidUserId")
                .list();

        Assert.assertNotNull(object instanceof Map);


        object = jdbcDao.select("t1.loginName as name1", "t2.loginName as name2").from(UserInfo.class, "t1", UidUser.class, "t2")
                .where()
                .append("t1.userInfoId = t2.uidUserId")
                .list();

        Assert.assertNotNull(object instanceof Map);
    }

    @Test
    public void selectForAnnotation() {
        Page<KUserInfo> page = jdbcDao.selectFrom(KUserInfo.class)
                .paginate(1, 10)
                .pageResult(KUserInfo.class);
        Assert.assertTrue(page.getList().size() > 0);
        Assert.assertNotNull(page.getList().get(0).getLoginName());
    }


    @Test
    public void groupByAndOrderBy() {
        UserInfo user = new UserInfo();
        user.setLoginName("name2-19");
        user.setPassword("123456-");
        user.setUserAge(19);
        user.setGmtCreate(new Date());
        jdbcDao.executeInsert(user);

        Select select = jdbcDao.select("count(*) Num").from(UserInfo.class)
                .groupBy("userAge")
                .orderBy("Num").desc();
        Page<Object> page = select.paginate(1, 5).pageResult();

        Assert.assertTrue(page.getList().size() == 5);

        List<Object> objects = select.list();
        Assert.assertTrue(objects.size() == 50);
    }

    @Test
    public void append() {
        UserInfo userInfo = jdbcDao.selectFrom(UserInfo.class)
                .where("userAge", ">", 5)
                .append("and userInfoId = (select max(t2.userInfoId) from UserInfo t2 where t2.userInfoId < ?)", 40)
                .singleResult(UserInfo.class);
        Assert.assertNotNull(userInfo);
    }

    @Test
    public void updateSet() {

        jdbcDao.update(UserInfo.class)
                .set("loginName", "newName")
                .where("userInfoId", 15L)
                .execute();

        UserInfo user1 = jdbcDao.get(UserInfo.class, 15L);
        Assert.assertTrue(user1.getLoginName().equals("newName"));
    }

    @Test
    public void updateForEntity() {

        UserInfo user = new UserInfo();
        user.setUserInfoId(17L);
        user.setLoginName("newName22");
        user.setPassword("abc");
        //没有设置where条件，将更新所有
        jdbcDao.update(UserInfo.class)
                .setForEntity(user)
                .execute();

        UserInfo user1 = jdbcDao.get(UserInfo.class, 17L);
        Assert.assertTrue(user1.getLoginName().equals("newName22"));
        Assert.assertTrue(user1.getPassword().equals("abc"));
    }

    @Test
    public void updateNull() {

        UserInfo user = new UserInfo();
        user.setUserInfoId(17L);
        jdbcDao.update(UserInfo.class)
                .setForEntityWhereId(user)
                .updateNull()
                .execute();

        UserInfo user1 = jdbcDao.get(UserInfo.class, 17L);
        Assert.assertNull(user1.getLoginName());
        Assert.assertNull(user1.getPassword());
        Assert.assertNull(user1.getUserAge());
        Assert.assertNull(user1.getGmtCreate());
    }


    @Test
    public void updatePkNull() {

        try {
            UserInfo user = new UserInfo();
            user.setLoginName("newName22");
            user.setPassword("abc");
            //没有设置where条件，将更新所有
            jdbcDao.update(UserInfo.class)
                    .setForEntityWhereId(user)
                    .execute();
        } catch (Exception e) {
            Assert.assertEquals("主键属性值不能为空:userInfoId", e.getMessage());
        }
    }

    @Test
    public void insertAnnotation() {

        KUserInfo ku = new KUserInfo();
        ku.setLoginName("insertName");
        ku.setPassword("123456");
        ku.setUserAge(18);
        ku.setGmtCreate(new Date());
        Long id = (Long) jdbcDao.executeInsert(ku);

        KUserInfo kUserInfo = jdbcDao.get(KUserInfo.class, id);
        Assert.assertTrue(ku.getLoginName().equals(kUserInfo.getLoginName()));
        Assert.assertTrue(ku.getPassword().equals(kUserInfo.getPassword()));
    }

    @Test
    public void updateAnnotation() {

        KUserInfo ku = new KUserInfo();
        ku.setRowId(36L);
        ku.setLoginName("777777");
        ku.setPassword("787878");
        ku.setGmtModify(new Date());
        int count = jdbcDao.executeUpdate(ku);
        Assert.assertTrue(count == 1);
    }

    @Test
    public void deleteAnnotation() {

        KUserInfo ku = new KUserInfo();
        ku.setLoginName("liyd");
        Serializable id = (Serializable) jdbcDao.executeInsert(ku);

        int count = jdbcDao.executeDelete(KUserInfo.class, id);
        Assert.assertTrue(count == 1);
    }


    @Test
    public void updateNative() {

        jdbcDao.update(UserInfo.class)
                .set("{{userAge}}", "userAge+1")
                .where("userInfoId", 17L)
                .execute();

        UserInfo user = jdbcDao.get(UserInfo.class, 17L);
        Assert.assertTrue(user.getUserAge() == 18);
    }


    @Test
    public void nativeExecutor() {
        int count = jdbcDao.nativeExecutor()
                .command("update UserInfo set loginName = ? where userInfoId = ?")
                .parameters(new Object[]{"newName", 39L})
                .update();
        Assert.assertTrue(count == 1);
        UserInfo user = jdbcDao.get(UserInfo.class, 39L);
        Assert.assertEquals(user.getLoginName(), "newName");
    }


    @Test
    public void nativeExecutor4() {
        int count = jdbcDao.nativeExecutor()
                .command("update UserInfo set loginName = ? where userInfoId = ?")
                .parameters("newName", 39L)
                .update();
        Assert.assertTrue(count == 1);
        UserInfo user = jdbcDao.get(UserInfo.class, 39L);
        Assert.assertEquals(user.getLoginName(), "newName");
    }


    @Test
    public void nativeExecutor5() {

        Long result = jdbcDao.nativeExecutor()
                .command("select count(*) from UserInfo")
                .count();
        Assert.assertTrue(result == 50);
    }

    @Test
    public void nativeExecutor7() {

        jdbcDao.nativeExecutor()
                .command("update user_info set user_age = 18 where user_age < 18")
                .nativeSql(true)
                .execute();

        long count = jdbcDao.selectFrom(UserInfo.class)
                .where("userAge", "<", 18)
                .count();
        Assert.assertTrue(count == 0);
    }

    @Test
    public void nativeExecutor8() {

        jdbcDao.nativeExecutor()
                .command("update UserInfo set userAge = 18 where userAge < 18")
                .execute();

        long count = jdbcDao.selectFrom(UserInfo.class)
                .where("userAge", "<", 18)
                .count();
        Assert.assertTrue(count == 0);
    }

    @Test
    public void nativeOneColResult() {
        Integer integer = jdbcDao.nativeExecutor()
                .command("select sum(userAge) from UserInfo")
                .oneColResult(Integer.class);
        Assert.assertTrue(integer > 0);
    }

    @Test
    public void nativeFirstResult() {
        UserInfo userInfo = jdbcDao.nativeExecutor()
                .command("select * from UserInfo order by userInfoId asc")
                .firstResult(UserInfo.class);

        Assert.assertTrue(userInfo.getUserInfoId().equals(1L));
    }

    @Test
    public void nativeOneColFirstResult() {
        Long id = jdbcDao.nativeExecutor()
                .command("select userInfoId from UserInfo order by userInfoId asc")
                .oneColFirstResult(Long.class);

        Assert.assertTrue(id.equals(1L));
    }

    @Test
    public void nativeInsert() {
        jdbcDao.nativeExecutor()
                .command("insert into UserInfo(userInfoId,loginName,password,userAge) values(?,?,?,?)")
                .parameters(100L, "100user", "123321", 18)
                .insert();

        UserInfo userInfo = jdbcDao.get(UserInfo.class, 100L);
        Assert.assertTrue(userInfo.getUserInfoId().equals(100L));
        Assert.assertTrue(userInfo.getLoginName().equals("100user"));
        Assert.assertTrue(userInfo.getPassword().equals("123321"));
        Assert.assertTrue(userInfo.getUserAge().equals(18));
    }

    @Test
    public void nativeInsert2() {
        Serializable id = jdbcDao.nativeExecutor()
                .command("insert into UserInfo(loginName,password,userAge) values(?,?,?)")
                .parameters("100user", "123321", 18)
                .insert(UserInfo.class);

        UserInfo userInfo = jdbcDao.get(UserInfo.class, id);
        Assert.assertTrue(userInfo.getLoginName().equals("100user"));
        Assert.assertTrue(userInfo.getPassword().equals("123321"));
        Assert.assertTrue(userInfo.getUserAge().equals(18));
    }

    @Test
    public void nativeList() {
        List<UserInfo> list = jdbcDao.nativeExecutor()
                .command("select * from UserInfo")
                .list(UserInfo.class);

        Assert.assertTrue(list != null && !list.isEmpty());
    }

    @Test
    public void nativeOneColList() {
        List<Long> list = jdbcDao.nativeExecutor()
                .command("select userInfoId from UserInfo")
                .oneColList(Long.class);

        Assert.assertTrue(list != null && !list.isEmpty());
    }

    @Test
    public void nativeFirstList() {
        Object result = jdbcDao.nativeExecutor()
                .command("select userInfoId from UserInfo")
                .firstResult();
        Assert.assertTrue(result instanceof Map);
        Assert.assertTrue(((Map) result).size() == 1);
    }

    @Test
    public void nativePage() {
        Pageable pageable = new UserInfo();
        pageable.setPageNum(2);
        pageable.setPageSize(10);
        Page<Object> page = jdbcDao.nativeExecutor()
                .command("select userInfoId from UserInfo order by userInfoId asc")
                .paginate(pageable)
                .pageResult();
        Assert.assertTrue(page.getList().size() == 10);
        Assert.assertTrue(page.getList().get(0) instanceof Map);
        Map<String, Object> map = (Map<String, Object>) page.getList().get(0);
        Assert.assertTrue(map.get("USER_INFO_ID").equals(11L));
        Map<String, Object> map2 = (Map<String, Object>) page.getList().get(9);
        Assert.assertTrue(map2.get("USER_INFO_ID").equals(20L));
    }

    @Test
    public void nativeResultHandler() {
        //这里只演示把结果转为AuthCode类
        Page<AuthCode> page = jdbcDao.nativeExecutor()
                .command("select * from UserInfo order by userInfoId asc")
                .paginate(2, 5)
                .resultHandler(new CustomResultHandler())
                .pageResult(AuthCode.class);
        Assert.assertTrue(page.getList().size() == 5);
        Assert.assertTrue(page.getList().get(0).getCode().equals("name-6"));
        Assert.assertTrue(page.getList().get(4).getCode().equals("name-10"));
    }

    @Test
    public void nativeLimitPage() {
        Page<Object> page = jdbcDao.nativeExecutor()
                .command("select userInfoId from UserInfo order by userInfoId asc")
                .limit(15, 10)
                .pageResult();
        Assert.assertTrue(page.getList().size() == 10);
        Assert.assertTrue(page.getList().get(0) instanceof Map);
        Map<String, Object> map = (Map<String, Object>) page.getList().get(0);
        Assert.assertTrue(map.get("USER_INFO_ID").equals(11L));
        Map<String, Object> map2 = (Map<String, Object>) page.getList().get(9);
        Assert.assertTrue(map2.get("USER_INFO_ID").equals(20L));
    }

    @Test
    public void mybatisExecutor1() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 9L);
        params.put("loginName", "name-9");

        Object user = jdbcDao.myBatisExecutor()
                .command("getUser")
                .parameters(params)
                .nativeSql(true)
                .singleResult();

        Assert.assertNotNull(user);
    }


    @Test
    public void mybatisExecutor2() {

        Object user = jdbcDao.myBatisExecutor()
                .command("getUser")
                .parameter("id", 9L)
                .parameter("loginName", "name-9")
                .nativeSql(true)
                .singleResult();

        Assert.assertNotNull(user);
    }

    @Test
    public void mybatisExecutor3() {

        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(10L);
        userInfo.setLoginName("name-10");

        Object user = jdbcDao.myBatisExecutor()
                .command("getUser2")
                .parameter("user", userInfo)
                .nativeSql(true)
                .singleResult();

        Assert.assertNotNull(user);
    }

    @Test
    public void mybatisExecutor4() {

        List<String> names = new ArrayList<>();
        names.add("name-8");
        names.add("name-9");
        names.add("name-10");

        List<?> list = jdbcDao.myBatisExecutor()
                .command("queryUserList")
                .parameter("names", names)
                .nativeSql(true)
                .list();

        Assert.assertTrue(list.size() == 3);
    }

    @Test
    public void mybatisExecutor5() {

        int update = jdbcDao.myBatisExecutor()
                .command("updateUser")
                .parameter("loginName", "newName")
                .parameter("userInfoId", 9L)
                .nativeSql(true)
                .update();

        Assert.assertTrue(update == 1);
        UserInfo userInfo = jdbcDao.get(UserInfo.class, 9L);
        Assert.assertTrue("newName".equals(userInfo.getLoginName()));
    }


    @Test
    public void mybatisExecutor6() {
        Object user = jdbcDao.myBatisExecutor()
                .command("getUser3")
                .parameter("userInfoId", 9L)
                .parameter("loginName", "name-9")
                .singleResult();

        Assert.assertNotNull(user);
    }


    @Test
    public void mybatisExecutor7() {
        UserInfo userInfo = (UserInfo) jdbcDao.myBatisExecutor()
                .command("getUser3")
                .parameter("userInfoId", 9L)
                .parameter("loginName", "name-9")
                .singleResult(UserInfo.class);

        Assert.assertTrue("name-9".equals(userInfo.getLoginName()));
    }

    @Test
    public void mybatisExecutor8() {
        Page<UserInfo> page = jdbcDao.myBatisExecutor()
                .command("queryUserList2")
                .paginate(1, 10)
                .pageResult(UserInfo.class);

        Assert.assertTrue(page.getList().size() == 10);
    }

}
