package com.sonsure.dumper.test.sql;

import com.sonsure.dumper.core.command.sql.CommandToSqlTranslator;
import com.sonsure.dumper.core.command.sql.JSqlParserCommandToSqlTranslator;
import com.sonsure.dumper.core.mapping.DefaultMappingHandler;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.test.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CommandToSqlTranslatorTest {

    private MappingHandler mappingHandler = new DefaultMappingHandler();

    private Map<String, Class<?>> classMapping = new HashMap<>();

    private CommandToSqlTranslator commandToSqlTranslator = new JSqlParserCommandToSqlTranslator();

    public CommandToSqlTranslatorTest() {
        classMapping.put("RelTag", RelTag.class);
        classMapping.put("Tag", Tag.class);
        classMapping.put("User", User.class);
        classMapping.put("UserInfo", UserInfo.class);
        classMapping.put("Content", Content.class);
        classMapping.put("Series", Series.class);
        classMapping.put("RelSeries", RelSeries.class);
    }

    @Test
    public void commandToSql() throws Exception {

        String command = "select t1.*,t2.contentNum,t2.contentType from Tag t1 inner join (select count(*) as contentNum,rt.tagId,rt.contentType from RelTag rt where rt.contentType = ? group by rt.tagId order by contentNum desc limit 0,?) t2 on t1.tagId = t2.tagId";
        String sql = commandToSqlTranslator.getSql(command, mappingHandler, classMapping, null);
        System.out.println(sql.toLowerCase());
        String result = "SELECT t1.*, t2.contentNum, t2.CONTENT_TYPE FROM TAG t1 INNER JOIN (SELECT count(*) AS contentNum, rt.TAG_ID, rt.CONTENT_TYPE FROM REL_TAG rt WHERE rt.CONTENT_TYPE = ? GROUP BY rt.TAG_ID ORDER BY contentNum DESC LIMIT 0, ?) t2 ON t1.TAG_ID = t2.TAG_ID";
        Assert.assertTrue(result.toLowerCase().equals(sql.toLowerCase()));

        String command2 = "select t1.*,count(*) as contentNum from User t1 inner join Content t2 on t1.userId = t2.userId and t2.gmtCreate > ? and t2.gmtCreate < ? and t2.status in ( ?, ?, ?) group by t2.userId order by contentNum desc limit 0,?";
        String sql2 = commandToSqlTranslator.getSql(command2, mappingHandler, classMapping, null);
        String result2 = "select t1.*, count(*) as contentnum from user t1 inner join content t2 on t1.user_id = t2.user_id and t2.gmt_create > ? and t2.gmt_create < ? and t2.status in (?, ?, ?) group by t2.user_id order by contentnum desc limit 0, ?";
        Assert.assertTrue(sql2.toLowerCase().equals(result2));
        System.out.println(sql2.toLowerCase());

        String command3 = "select loginName,password from UserInfo where userInfoId = ?";
        String sql3 = commandToSqlTranslator.getSql(command3, mappingHandler, classMapping, null);
        String result3 = "select login_name, password from user_info where user_info_id = ?";
        Assert.assertTrue(sql3.toLowerCase().equals(result3));
        System.out.println(sql3.toLowerCase());

        String command4 = "select t1.*,t2.loginName,t2.userAge from Tag t1,UserInfo t2";
        String sql4 = commandToSqlTranslator.getSql(command4, mappingHandler, classMapping, null);
        String result4 = "select t1.*, t2.login_name, t2.user_age from tag t1, user_info t2";
        Assert.assertTrue(sql4.toLowerCase().equals(result4));
        System.out.println(sql4.toLowerCase());

        String command5 = "Insert into Tag(tagName,tagType) select loginName,userAge from UserInfo";
        String sql5 = commandToSqlTranslator.getSql(command5, mappingHandler, classMapping, null);
        String result5 = "insert into tag (tag_name, tag_type) select login_name, user_age from user_info";
        Assert.assertTrue(sql5.toLowerCase().equals(result5));
        System.out.println(sql5.toLowerCase());

        String command6 = "update UserInfo set loginName = ?,password = ? where userInfoId = ?";
        String sql6 = commandToSqlTranslator.getSql(command6, mappingHandler, classMapping, null);
        String result6 = "update user_info set login_name = ?, password = ? where user_info_id = ?";
        Assert.assertTrue(sql6.toLowerCase().equals(result6));
        System.out.println(sql6.toLowerCase());

        String command7 = "delete from UserInfo where loginName = ? and password = ? and userInfoId = ?";
        String sql7 = commandToSqlTranslator.getSql(command7, mappingHandler, classMapping, null);
        String result7 = "delete from user_info where login_name = ? and password = ? and user_info_id = ?";
        Assert.assertTrue(sql7.toLowerCase().equals(result7));
        System.out.println(sql7.toLowerCase());
    }

    @Test
    public void commandToSql8() {
        String command = "select t1.* from Series t1 inner join RelSeries t2 inner JOIN Content t3 on t1.seriesId = t2.seriesId and t2.contentId = t3.contentId group by t1.seriesId order by sum(t3.clickCount) desc";
        String sql = commandToSqlTranslator.getSql(command, mappingHandler, classMapping, null);
        String result = "select t1.* from series t1 inner join rel_series t2 inner join content t3 on t1.series_id = t2.series_id and t2.content_id = t3.content_id group by t1.series_id order by sum(t3.click_count) desc";
        Assert.assertTrue(sql.toLowerCase().equals(result));
        System.out.println(sql.toLowerCase());

    }
}
