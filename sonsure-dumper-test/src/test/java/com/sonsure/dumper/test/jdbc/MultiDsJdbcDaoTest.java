package com.sonsure.dumper.test.jdbc;

import com.sonsure.dumper.core.persist.Jdbc;
import com.sonsure.dumper.core.persist.JdbcDao;
import com.sonsure.dumper.test.model.TestUser;
import com.sonsure.dumper.test.model.UserInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-multi-ds.xml"})
public class MultiDsJdbcDaoTest {

    @Autowired
    private JdbcDao jdbcDao;

    @Test
    public void defaultDsJdbcDaoTest() {

        jdbcDao.executeDelete(UserInfo.class);

        //默认
        for (int i = 0; i < 5; i++) {
            UserInfo user = new UserInfo();
            user.setLoginName("name-" + i);
            user.setUserAge(i);
            user.setPassword("123456-" + i);
            user.setGmtCreate(new Date());
            jdbcDao.executeInsert(user);
        }
        long count = jdbcDao.findCount(UserInfo.class);
        Assert.assertEquals(count, 5);
    }

    @Test
    public void mysqlDsJdbcDaoTest() {

        jdbcDao.use("mysql").executeDelete(UserInfo.class);

        //默认
        for (int i = 0; i < 5; i++) {
            UserInfo user = new UserInfo();
            user.setLoginName("name-" + i);
            user.setUserAge(i);
            user.setPassword("123456-" + i);
            user.setGmtCreate(new Date());
            jdbcDao.use("mysql").executeInsert(user);
        }
        long count = jdbcDao.use("mysql").findCount(UserInfo.class);
        Assert.assertEquals(count, 5);
    }

    @Test
    public void oracleDsJdbcDaoTest() {

        jdbcDao.use("oracle").executeDelete(TestUser.class);

        //默认
        for (int i = 0; i < 5; i++) {
            TestUser testUser = new TestUser();
            testUser.setUsername("liyd");

            jdbcDao.use("oracle").executeInsert(testUser);
        }
        long count = jdbcDao.use("oracle").findCount(TestUser.class);
        Assert.assertEquals(count, 5);
    }

    @Test
    public void jdbcDefaultDsJdbcDaoTest() {

        Jdbc.executeDelete(UserInfo.class);

        //默认
        for (int i = 0; i < 5; i++) {
            UserInfo user = new UserInfo();
            user.setLoginName("name-" + i);
            user.setUserAge(i);
            user.setPassword("123456-" + i);
            user.setGmtCreate(new Date());
            Jdbc.executeInsert(user);
        }
        long count = Jdbc.findCount(UserInfo.class);
        Assert.assertEquals(count, 5);
    }

    @Test
    public void jdbcMysqlDsJdbcDaoTest() {

        Jdbc.use("mysql").executeDelete(UserInfo.class);

        //默认
        for (int i = 0; i < 5; i++) {
            UserInfo user = new UserInfo();
            user.setLoginName("name-" + i);
            user.setUserAge(i);
            user.setPassword("123456-" + i);
            user.setGmtCreate(new Date());
            Jdbc.use("mysql").executeInsert(user);
        }
        long count = Jdbc.use("mysql").findCount(UserInfo.class);
        Assert.assertEquals(count, 5);
    }

    @Test
    public void jdbcOracleDsJdbcDaoTest() {

        Jdbc.use("oracle").executeDelete(TestUser.class);

        //默认
        for (int i = 0; i < 5; i++) {
            TestUser testUser = new TestUser();
            testUser.setUsername("liyd");

            Jdbc.use("oracle").executeInsert(testUser);
        }
        long count = Jdbc.use("oracle").findCount(TestUser.class);
        Assert.assertEquals(count, 5);
    }
}
