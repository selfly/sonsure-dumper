package com.sonsure.dumper.test.jdbc;

import com.sonsure.dumper.core.persist.Jdbc;
import com.sonsure.dumper.test.model.UserInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

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

}
