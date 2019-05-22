package com.sonsure.dumper.test.jdbc;

import com.sonsure.dumper.core.persist.JdbcDao;
import com.sonsure.dumper.test.model.TestUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liyd on 17/4/12.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-uuid.xml"})
public class UuidJdbcTemplateDaoImplTest {

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
