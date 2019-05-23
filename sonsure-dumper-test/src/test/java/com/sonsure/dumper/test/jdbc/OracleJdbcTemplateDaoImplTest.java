package com.sonsure.dumper.test.jdbc;

import com.sonsure.dumper.core.persist.JdbcDao;
import com.sonsure.dumper.test.model.TestUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-oracle.xml"})
public class OracleJdbcTemplateDaoImplTest {

    @Autowired
    protected JdbcDao jdbcDao;

    @Test
    public void jdbcDaoInsert() {

        TestUser testUser = new TestUser();
        testUser.setUsername("liyd");

        String id = (String) jdbcDao.executeInsert(testUser);

        System.out.println(id);
    }

}
