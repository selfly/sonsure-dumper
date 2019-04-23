package com.sonsure.dumper.test.jdbc;

import com.sonsure.commons.model.Page;
import com.sonsure.dumper.core.command.entity.Select;
import com.sonsure.dumper.core.persist.Jdbc;
import com.sonsure.dumper.test.model.KUserInfo;
import com.sonsure.dumper.test.model.UserInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by liyd on 17/4/12.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class JdbcTest {

//    @Before
//    public void before() {
//        //初始化测试数据
//        jdbcDao.createDelete(UserInfo.class)
//                .execute();
//        for (int i = 1; i < 51; i++) {
//            UserInfo user = new UserInfo();
//            user.setUserInfoId(Long.valueOf(i));
//            user.setLoginName("name-" + i);
//            user.setPassword("123456-" + i);
//            user.setUserAge(i);
//            user.setGmtCreate(new Date());
//
//            jdbcDao.insert(user);
//        }
//    }

    @Test
    public void get() {
        UserInfo user = Jdbc.get(UserInfo.class, 30L);
        Assert.assertNotNull(user);
        Assert.assertTrue(user.getUserInfoId().equals(30L));
        Assert.assertTrue(user.getLoginName().equals("name-30"));
        Assert.assertTrue(user.getPassword().equals("123456-30"));
    }

    @Test
    public void insert() {

        UserInfo user = new UserInfo();
        user.setLoginName("liyd2017");
        user.setPassword("2017");
        user.setUserAge(18);
        user.setGmtCreate(new Date());

        Long id = (Long) Jdbc.insert(user);

        Assert.assertTrue(id > 0);

        UserInfo userInfo = Jdbc.get(UserInfo.class, id);
        Assert.assertTrue(user.getLoginName().equals(userInfo.getLoginName()));
        Assert.assertTrue(user.getPassword().equals(userInfo.getPassword()));
    }

    @Test
    public void update() {
        UserInfo user = new UserInfo();
        user.setUserInfoId(21L);
        user.setPassword("666666");
        user.setLoginName("666777");
        user.setGmtModify(new Date());

        int count = Jdbc.update(user);

        Assert.assertTrue(count == 1);

        UserInfo user1 = Jdbc.get(UserInfo.class, 20L);
        Assert.assertNotNull(user1);
        Assert.assertTrue(user1.getUserInfoId().equals(20L));
        Assert.assertTrue(user1.getLoginName().equals("666777"));
        Assert.assertTrue(user1.getPassword().equals("666666"));
        Assert.assertNotNull(user1.getGmtModify());
    }

    @Test
    public void delete() {
        int count = Jdbc.delete(UserInfo.class, 39L);
        Assert.assertTrue(count == 1);
        UserInfo user = Jdbc.get(UserInfo.class, 39L);
        Assert.assertNull(user);
    }


    @Test
    public void find() {
        List<UserInfo> users = Jdbc.find(UserInfo.class);
        Assert.assertNotNull(users);
        Assert.assertTrue(users.size() == 50);
    }

    @Test
    public void find1() {
        UserInfo user = new UserInfo();
        user.setUserAge(10);
        List<UserInfo> users = Jdbc.find(user);
        Assert.assertNotNull(users);
        Assert.assertTrue(users.size() == 1);
    }

    @Test
    public void jdbcPageResult() {
        UserInfo user = new UserInfo();
        user.setPageSize(10);
        Page<UserInfo> page = Jdbc.pageResult(user);
        Assert.assertNotNull(page);
        Assert.assertTrue(page.getList().size() == 10);
    }

    @Test
    public void jdbcPageResult2() {
        UserInfo user = new UserInfo();
        user.setPageSize(10);
        user.setUserAge(10);
        Page<UserInfo> page = Jdbc.pageResult(user);
        Assert.assertNotNull(page);
        Assert.assertTrue(page.getList().size() == 1);
    }

    @Test
    public void jdbcCount() {
        UserInfo user = new UserInfo();
        user.setUserAge(10);
        long count = Jdbc.findCount(user);
        Assert.assertTrue(count == 1);
    }

    @Test
    public void jdbcCount2() {
        long count = Jdbc.findCount(UserInfo.class);
        Assert.assertTrue(count == 50);
    }

    @Test
    public void jdbcSingleResult() {
        UserInfo tmp = new UserInfo();
        tmp.setUserAge(10);
        UserInfo user = Jdbc.singleResult(tmp);
        Assert.assertNotNull(user);
        Assert.assertTrue(user.getUserInfoId().equals(10L));
        Assert.assertTrue(user.getLoginName().equals("name-10"));
        Assert.assertTrue(user.getUserAge().equals(10));
        Assert.assertNotNull(user.getGmtCreate());
    }

    @Test
    public void jdbcFirstResult() {
        UserInfo tmp = new UserInfo();
        tmp.setUserAge(10);
        UserInfo user = Jdbc.firstResult(tmp);
        Assert.assertNotNull(user);
        Assert.assertTrue(user.getUserInfoId().equals(10L));
        Assert.assertTrue(user.getLoginName().equals("name-10"));
        Assert.assertTrue(user.getUserAge().equals(10));
        Assert.assertNotNull(user.getGmtCreate());
    }

    @Test
    public void jdbcDelete2() {
        UserInfo user = new UserInfo();
        user.setLoginName("name-18");
        int count = Jdbc.executeDelete(user);
        Assert.assertTrue(count == 1);

        UserInfo tmp = new UserInfo();
        tmp.setLoginName("name-18");
        UserInfo user1 = Jdbc.singleResult(tmp);
        Assert.assertNull(user1);
    }

    @Test
    public void jdbcDelete3() {
        int count = Jdbc.executeDelete(UserInfo.class);
        Assert.assertTrue(count > 0);
        long result = Jdbc.findCount(UserInfo.class);
        Assert.assertTrue(result == 0);
    }

    @Test
    public void jdbcInsert() {

        UserInfo user = new UserInfo();
        user.setLoginName("name-60");
        user.setPassword("606060");
        user.setUserAge(60);
        user.setGmtCreate(new Date());

        Long id = (Long) Jdbc.insert(user);

        UserInfo user1 = Jdbc.get(UserInfo.class, id);
        Assert.assertNotNull(user1);
        Assert.assertTrue(user1.getUserInfoId().equals(id));
        Assert.assertTrue(user1.getLoginName().equals("name-60"));
        Assert.assertTrue(user1.getPassword().equals("606060"));
    }

    @Test
    public void insert2() {

        Long id = (Long) Jdbc.insertInto(UserInfo.class)
                .set("loginName", "name123")
                .set("password", "123321")
                .execute();

        UserInfo user1 = Jdbc.get(UserInfo.class, id);
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

        Long id = (Long) Jdbc.insert(ku);

        KUserInfo kUserInfo = Jdbc.get(KUserInfo.class, id);
        Assert.assertEquals("selfly", kUserInfo.getLoginName());
        Assert.assertEquals("123456", kUserInfo.getPassword());
    }

    @Test
    public void select() {

        Jdbc.executeDelete(UserInfo.class);
        for (int i = 60; i < 70; i++) {
            UserInfo user = new UserInfo();
            user.setUserInfoId(Long.valueOf(i));
            user.setLoginName("name2-19");
            user.setPassword("123456-" + i);
            user.setUserAge(19);
            user.setGmtCreate(new Date());
            Jdbc.insert(user);
        }

        Select select1 = Jdbc.selectFrom(UserInfo.class)
                .where("userAge", 19);
        long count1 = select1.count();
        Assert.assertTrue(count1 == 10);

        List<UserInfo> list1 = select1.list(UserInfo.class);
        Assert.assertTrue(list1.size() == 10);

        Page<UserInfo> page1 = select1.paginate(1, 5).pageResult(UserInfo.class);
        Assert.assertTrue(page1.getList().size() == 5);

        Select select2 = Jdbc.selectFrom(UserInfo.class)
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

        Jdbc.executeDelete(UserInfo.class);
        for (int i = 1; i < 3; i++) {
            UserInfo user = new UserInfo();
            user.setUserInfoId(Long.valueOf(i));
            user.setLoginName("name-19");
            user.setPassword("123456-" + i);
            user.setUserAge(21);
            user.setGmtCreate(new Date());
            Jdbc.insert(user);
        }
        try {
            UserInfo user = Jdbc.selectFrom(UserInfo.class)
                    .where()
                    .begin()
                    .condition("loginName", "name-19")
                    .or("userAge", 21)
                    .end()
                    .singleResult(UserInfo.class);
        } catch (IncorrectResultSizeDataAccessException e) {
            Assert.assertTrue("Incorrect result size: expected 1, actual 2".equals(e.getMessage()));
        }

        UserInfo user2 = Jdbc.selectFrom(UserInfo.class)
                .where()
                .begin()
                .condition("loginName", "name-19")
                .or("userAge", 21)
                .end()
                .and("password", "123456-1")
                .singleResult(UserInfo.class);
        Assert.assertNotNull(user2);
    }

}
