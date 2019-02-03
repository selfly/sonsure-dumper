package com.sonsure.dumper.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 加载spring配置文件
 * <p>
 * UserInfo: liyd
 * Date: 2/13/14
 * Time: 5:33 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Rollback
@Transactional()
public class BaseTest {

    /**
     * 必须有个运行方法,不然maven install会通不过
     */
    @Test
    public void out() {
        System.out.printf("==============================");
    }

}
