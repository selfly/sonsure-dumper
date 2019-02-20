//package com.sonsure.dumper.core.command;
//
//
//import com.sonsure.commons.parser.TokenHandler;
//import com.sonsure.dumper.core.management.CommandTable;
//import com.sonsure.dumper.core.management.MappingCache;
//import com.sonsure.dumper.core.mapping.MappingHandler;
//
///**
// * native中的属性转成列
// * <p>
// * Created by liyd on 17/4/14.
// */
//public class FieldTokenHandler implements TokenHandler {
//
//    private CommandTable commandTable;
//    private MappingHandler mappingHandler;
//
//    public FieldTokenHandler(CommandTable commandTable, MappingHandler mappingHandler) {
//        this.commandTable = commandTable;
//        this.mappingHandler = mappingHandler;
//    }
//
//    public String handleToken(String content) {
//        return MappingCache.getColumn(this.commandTable, content, mappingHandler);
//    }
//}
