package com.sonsure.dumper.core.command;

public enum LogicalOperator {

    AND("and"),

    OR("or");

    private final String code;

    LogicalOperator(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
