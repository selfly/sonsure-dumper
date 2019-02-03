package com.sonsure.dumper.core.command.natives;


import com.sonsure.dumper.core.command.simple.AbstractSimpleCommandExecutor;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.page.PageHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import com.sonsure.dumper.core.persist.PersistExecutor;

/**
 * Created by liyd on 17/4/25.
 */
public class NativeExecutorImpl extends AbstractSimpleCommandExecutor<NativeExecutor> implements NativeExecutor {

    private Object[] parameters;

    public NativeExecutorImpl(MappingHandler mappingHandler, PageHandler pageHandler, KeyGenerator keyGenerator, PersistExecutor persistExecutor, boolean commandUppercase) {
        super(mappingHandler, pageHandler, keyGenerator, persistExecutor, commandUppercase);
    }

    public NativeExecutor parameters(Object... values) {
        this.parameters = values;
        return this;
    }

    @Override
    protected Object getParameters() {
        return this.parameters;
    }
}
