/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.test.jdbc;

import com.sonsure.commons.model.Page;
import com.sonsure.dumper.core.persist.JdbcDao;
import com.sonsure.dumper.test.model.UserInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-postgres.xml"})
public class PostgresJdbcTemplateDaoImplTest {

    @Autowired
    private JdbcDao jdbcDao;

    @Before
    public void before() {
        for (int i = 1; i < 51; i++) {
            UserInfo user = new UserInfo();
            user.setLoginName("name-" + i);
            user.setPassword("123456-" + i);
            user.setUserAge(i);
            user.setGmtCreate(new Date());
            jdbcDao.executeInsert(user);
        }
    }

    @Test
    public void findPage() {

        Page<UserInfo> page = jdbcDao.selectFrom(UserInfo.class)
                .orderBy("userInfoId").asc()
                .paginate(1, 20)
                .pageResult(UserInfo.class);

        System.out.println(page.getPagination().getTotalItems());

        for (UserInfo userInfo : page.getList()) {
            System.out.println(userInfo.getUserInfoId());
        }
    }

}
