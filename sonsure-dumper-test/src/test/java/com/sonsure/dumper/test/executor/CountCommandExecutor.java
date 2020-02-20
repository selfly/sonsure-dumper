package com.sonsure.dumper.test.executor;

import com.sonsure.dumper.core.command.CommandExecutor;

public interface CountCommandExecutor extends CommandExecutor {

    CountCommandExecutor clazz(Class<?> clazz);

    long getCount();
}
