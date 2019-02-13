package com.sonsure.dumper.core.command.sql;

import net.sf.jsqlparser.schema.Table;

public class TableMapping {

    private Table table;

    private String mappingName;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }
}
