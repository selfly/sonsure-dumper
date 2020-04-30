/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.test.model;

import java.util.Date;

public class RelTag {

    /**
     * 主键id
     */
    private Long relTagId;

    /**
     * 内容id
     */
    private Long contentId;

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 创建时间
     */
    private Date gmtCreate;


    public Long getRelTagId() {
        return relTagId;
    }

    public void setRelTagId(Long relTagId) {
        this.relTagId = relTagId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
