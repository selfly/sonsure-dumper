/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command;

public enum Comparison {

    EQ("="),

    NEQ("!="),

    GT(">"),

    GTE(">="),

    LT("<"),

    LTE("<="),

    IN("in"),

    NOT_IN("not in");

    private final String code;

    Comparison(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
