package com.sonsure.dumper.test.model;


import com.sonsure.commons.model.Pageable;

/**
 * Created by liyd on 17/6/7.
 */
public class UidUser extends Pageable {

	private static final long serialVersionUID = -188967944235979673L;

	String uidUserId;

    String loginName;

    String password;

    Integer userAge;

    public String getUidUserId() {
        return uidUserId;
    }

    public void setUidUserId(String uidUserId) {
        this.uidUserId = uidUserId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }
}
