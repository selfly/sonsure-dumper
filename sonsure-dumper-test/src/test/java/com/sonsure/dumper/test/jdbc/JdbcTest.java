/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.test.jdbc;

import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.persist.Jdbc;
import com.sonsure.dumper.core.persist.JdbcDao;
import com.sonsure.dumper.test.model.UserInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by liyd on 17/4/12.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class JdbcTest {

    @Before
    public void before() {
        //初始化测试数据
        Jdbc.executeDelete(UserInfo.class);
        for (int i = 1; i < 51; i++) {
            UserInfo user = new UserInfo();
            user.setUserInfoId(Long.valueOf(i));
            user.setLoginName("name-" + i);
            user.setPassword("123456-" + i);
            user.setUserAge(i);
            user.setGmtCreate(new Date());

            Jdbc.executeInsert(user);
        }
    }

    @Test
    public void get() {
        //方法实现都是一样的，只要一个执行成功就表示成功了，剩余的就是校验方法是否一致，都有实现
        UserInfo user = Jdbc.get(UserInfo.class, 30L);
        Assert.assertNotNull(user);
        Assert.assertTrue(user.getUserInfoId().equals(30L));
        Assert.assertTrue(user.getLoginName().equals("name-30"));
        Assert.assertTrue(user.getPassword().equals("123456-30"));
    }

    @Test
    public void compareMethod() {

        String separator = "$";
        Method[] jdbcMethods = Jdbc.class.getDeclaredMethods();
        Set<String> jdbcMethodSet = new HashSet<>();
        for (Method method : jdbcMethods) {
            StringBuilder methodName = new StringBuilder(method.getName());
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                methodName.append(separator)
                        .append(parameterType.getName());
            }
            Class<?> returnType = method.getReturnType();
            methodName.append(separator)
                    .append(returnType.getName());

            jdbcMethodSet.add(methodName.toString());
        }

        Method[] daoMethods = JdbcDao.class.getDeclaredMethods();
        for (Method daoMethod : daoMethods) {
            StringBuilder methodName = new StringBuilder(daoMethod.getName());
            Class<?>[] parameterTypes = daoMethod.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                methodName.append(separator)
                        .append(parameterType.getName());
            }
            Class<?> returnType = daoMethod.getReturnType();
            methodName.append(separator)
                    .append(returnType.getName());
            String name = methodName.toString();
            if (!jdbcMethodSet.contains(name)) {
                System.out.println("jdbc未实现方法:" + name);
                throw new SonsureJdbcException("jdbc未实现方法:" + name);
            }
        }
    }

}
