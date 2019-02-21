package com.sonsure.dumper.core.command.entity;


import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;

/**
 * Created by liyd on 17/4/14.
 */
public class DeleteImpl<T> extends AbstractConditionBuilder<Delete<T>> implements Delete<T> {

    public DeleteImpl(MappingHandler mappingHandler, PageHandler pageHandler, KeyGenerator keyGenerator, PersistExecutor persistExecutor, String commandCase) {
        super(mappingHandler, pageHandler, keyGenerator, persistExecutor, commandCase);
    }

    public int execute() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        return (Integer) this.persistExecutor.execute(commandContext, CommandType.DELETE);
    }
}
