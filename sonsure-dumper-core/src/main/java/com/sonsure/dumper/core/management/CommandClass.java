/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.management;

public class CommandClass {

    /**
     * 对应实体类
     */
    private Class<?> cls;

    /**
     * 表别名
     */
    private String aliasName;

    public CommandClass(Class<?> cls) {
        this(cls, null);
    }

    public CommandClass(Class<?> cls, String aliasName) {
        this.cls = cls;
        this.aliasName = aliasName;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}
