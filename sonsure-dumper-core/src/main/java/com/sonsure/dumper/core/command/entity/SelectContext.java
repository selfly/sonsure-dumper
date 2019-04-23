package com.sonsure.dumper.core.command.entity;

import com.sonsure.commons.model.Pageable;
import com.sonsure.commons.model.Pagination;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.management.ClassField;
import com.sonsure.dumper.core.management.CommandClass;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectContext extends WhereContext {

    /**
     * 实体类型
     */
    protected List<CommandClass> fromClasses;

    /**
     * 返回结果类型
     */
    protected Class<?> resultType;

    /**
     * 操作的属性
     */
    protected List<ClassField> selectFields;

    /**
     * 黑名单
     */
    protected List<ClassField> excludeFields;

    /**
     * group by属性
     */
    protected List<ClassField> groupByFields;

    /**
     * 排序属性
     */
    protected List<ClassField> orderByFields;

    /**
     * 分页查询时是否用count查询总记录数,默认true
     */
    protected boolean isCount = true;

    protected Pagination pagination;

    public SelectContext() {
        fromClasses = new ArrayList<>();
        selectFields = new ArrayList<>();
        excludeFields = new ArrayList<>();
        groupByFields = new ArrayList<>();
        orderByFields = new ArrayList<>();
    }

    @Override
    public Class<?>[] getModelClasses() {
        List<Class<?>> classes = new ArrayList<>();
        for (CommandClass fromClass : fromClasses) {
            classes.add(fromClass.getCls());
        }
        return classes.toArray(new Class<?>[classes.size()]);
    }

    public void addSelectFields(String[] fields) {
        if (ArrayUtils.isEmpty(fields)) {
            return;
        }
        for (String field : fields) {
            this.selectFields.add(new ClassField(field, true));
        }
    }

    public void addFromClass(Class<?> cls) {
        this.addFromClass(cls, null);
    }

    public void addFromClass(Class<?> cls, String aliasName) {
        fromClasses.add(new CommandClass(cls, aliasName));
    }

    /**
     * 添加黑名单属性
     *
     * @param fields
     */
    public void addExcludeFields(String... fields) {
        for (String field : fields) {
            this.excludeFields.add(new ClassField(field, true));
        }
    }

    /**
     * 添加group by属性
     *
     * @param fields the fields
     */
    public void addGroupByField(String... fields) {
        for (String field : fields) {
            this.groupByFields.add(new ClassField(field, true));
        }
    }


    /**
     * 添加排序属性
     *
     * @param fields the fields
     */
    public void addOrderByField(String... fields) {
        for (String field : fields) {
            this.orderByFields.add(new ClassField(field, true));
        }
    }

    /**
     * 设置排序类型
     *
     * @param type
     */
    public void setOrderByType(String type) {
        if (orderByFields.isEmpty()) {
            throw new SonsureJdbcException("请先指定需要排序的属性");
        }
        int size = orderByFields.size();
        for (int i = size - 1; i >= 0; i--) {
            ClassField classField = orderByFields.get(i);
            if (StringUtils.isNotBlank(classField.getFieldOperator())) {
                //已经指定了，跳出
                break;
            }
            classField.setFieldOperator(type);
        }
    }

    public void isCount(boolean isCount) {
        this.isCount = isCount;
    }

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

    public List<CommandClass> getFromClasses() {
        return fromClasses;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public List<ClassField> getSelectFields() {
        return selectFields;
    }

    public List<ClassField> getExcludeFields() {
        return excludeFields;
    }

    public List<ClassField> getGroupByFields() {
        return groupByFields;
    }

    public List<ClassField> getOrderByFields() {
        return orderByFields;
    }

    public boolean isCount() {
        return isCount;
    }

    public Pagination getPagination() {
        return pagination;
    }

    /**
     * 是否黑名单
     *
     * @param field
     * @return
     */
    public boolean isExcludeField(String field) {
        if (this.excludeFields == null || this.excludeFields.isEmpty()) {
            return false;
        }
        ClassField classField = new ClassField(field, true);
        for (ClassField excludeField : excludeFields) {
            if (StringUtils.equals(classField.getTableAlias(), excludeField.getTableAlias()) && StringUtils.equals(classField.getName(), excludeField.getName())) {
                return true;
            }
        }
        return false;
    }
}
