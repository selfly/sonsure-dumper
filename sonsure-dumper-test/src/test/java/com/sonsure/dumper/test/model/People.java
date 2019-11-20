package com.sonsure.dumper.test.model;

import java.util.Date;

public class People {

    private String peopleId;
    private String peopleName;
    private Integer peopleAge;
    private Date gmtCreate;

    public String getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(String peopleId) {
        this.peopleId = peopleId;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public Integer getPeopleAge() {
        return peopleAge;
    }

    public void setPeopleAge(Integer peopleAge) {
        this.peopleAge = peopleAge;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
