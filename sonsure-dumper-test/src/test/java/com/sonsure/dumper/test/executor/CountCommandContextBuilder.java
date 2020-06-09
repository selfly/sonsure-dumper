///*
// * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
// * You may obtain more information at
// *
// *   http://www.sonsure.com
// *
// * Designed By Selfly Lee (selfly@live.com)
// */
//
//package com.sonsure.dumper.test.executor;
//
//import com.sonsure.dumper.core.command.CommandContext;
//import com.sonsure.dumper.core.command.entity.AbstractCommandContextBuilder;
//import com.sonsure.dumper.core.config.JdbcEngineConfig;
//
//public class CountCommandContextBuilder extends AbstractCommandContextBuilder {
//
//    @Override
//    public CommandContext doBuild(CommandExecutorContext executorContext, JdbcEngineConfig jdbcEngineConfig) {
//        Class<?> clazz = executorContext.getModelClasses()[0];
//        CommandContext commandContext = new CommandContext();
//        commandContext.setCommand("select count(*) from " + clazz.getSimpleName());
//        return commandContext;
//    }
//}
