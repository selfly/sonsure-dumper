/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.simple;

import com.sonsure.commons.model.Pageable;
import com.sonsure.commons.model.Pagination;
import com.sonsure.dumper.core.command.ExecutorContext;
import org.apache.commons.lang3.ArrayUtils;

public class SimpleExecutorContext implements ExecutorContext {

    protected String command;

    protected boolean nativeCommand = false;

    /**
     * 分页查询时是否用count查询总记录数,默认true
     */
    protected boolean isCount = true;

    protected Pagination pagination;


    public void paginate(int pageNum, int pageSize) {
        this.pagination = new Pagination();
        this.pagination.setPageSize(pageSize);
        this.pagination.setPageNum(pageNum);
    }

    public void paginate(Pageable pageable) {
        this.paginate(pageable.getPageNum(), pageable.getPageSize());
    }

    public void limit(int offset, int size) {
        this.pagination = new Pagination();
        this.pagination.setPageSize(size);
        this.pagination.setOffset(offset);
    }

    @Override
    public Class<?>[] getModelClasses() {
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public boolean isNativeCommand() {
        return nativeCommand;
    }

    public void setNativeCommand(boolean nativeCommand) {
        this.nativeCommand = nativeCommand;
    }

    public boolean isCount() {
        return isCount;
    }

    public void setCount(boolean count) {
        isCount = count;
    }

    public Pagination getPagination() {
        return pagination;
    }

}
