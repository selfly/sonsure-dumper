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

    private static final String DEFAULT_PARAM_PREFIX = "param";

    private int paramCount = 0;

    public NativeExecutorImpl(MappingHandler mappingHandler, PageHandler pageHandler, KeyGenerator keyGenerator, PersistExecutor persistExecutor, boolean commandUppercase) {
        super(mappingHandler, pageHandler, keyGenerator, persistExecutor, commandUppercase);
    }

    public NativeExecutor parameters(Object... values) {
        for (Object value : values) {
            this.parameter(DEFAULT_PARAM_PREFIX + (paramCount++), value);
        }
        return this;
    }

}
