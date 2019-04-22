package com.sonsure.dumper.core.command.simple;

import com.sonsure.commons.model.Pageable;
import com.sonsure.commons.model.Pagination;
import com.sonsure.dumper.core.command.ExecutorContext;
import org.apache.commons.lang3.ArrayUtils;

public class SimpleExecutorContext implements ExecutorContext {

    protected String command;

    protected boolean isNativeSql;

    /**
     * 分页查询时是否用count查询总记录数,默认true
     */
    protected boolean isCount = true;

    protected Pagination pagination;


    public void paginate(int pageNum, int pageSize) {
        this.pagination = new Pagination();
        this.pagination.setPageNum(pageNum);
        this.pagination.setPageSize(pageSize);
    }

    public void paginate(Pageable pageable) {
        this.paginate(pageable.getPageNum(), pageable.getPageSize());
    }

    public void limit(int offset, int size) {
        this.pagination = new Pagination();
        this.pagination.setOffset(offset);
        this.pagination.setPageSize(size);
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
    public boolean isNativeSql() {
        return isNativeSql;
    }

    public void setNativeSql(boolean nativeSql) {
        isNativeSql = nativeSql;
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
