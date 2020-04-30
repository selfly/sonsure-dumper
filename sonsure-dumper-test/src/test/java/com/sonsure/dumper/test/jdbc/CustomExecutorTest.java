/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.test.jdbc;

import com.sonsure.dumper.core.persist.JdbcDao;
import com.sonsure.dumper.test.executor.CountCommandExecutor;
import com.sonsure.dumper.test.model.UserInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-executor.xml"})
public class CustomExecutorTest {

    @Autowired
    protected JdbcDao jdbcDao;

    @Test
    public void countExecutorTest() {

        long count = jdbcDao.executor(CountCommandExecutor.class)
                .clazz(UserInfo.class)
                .getCount();

        Assert.assertTrue(count > 0);
    }
}
