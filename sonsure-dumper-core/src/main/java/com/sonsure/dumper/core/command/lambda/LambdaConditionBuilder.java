package com.sonsure.dumper.core.command.lambda;

import com.sonsure.commons.utils.ClassUtils;
import com.sonsure.dumper.core.command.entity.ConditionCommandExecutor;

import java.lang.reflect.Method;

public class LambdaConditionBuilder<O, E extends ConditionCommandExecutor<E>> {

    private O obj;

    private E conditionCommandExecutor;

    public LambdaConditionBuilder(O obj, E conditionCommandExecutor) {
        this.obj = obj;
        this.conditionCommandExecutor = conditionCommandExecutor;
    }

    public LambdaConditionBuilder<O, E> and(Consumer<O> consumer) {
        Method method = LambdaMethod.createMethod(consumer);
        Object value = ClassUtils.invokeMethod(method, obj);
        String filed = LambdaMethod.getMethodField(method);
        conditionCommandExecutor.and(filed, value);
        return this;
    }

    public LambdaConditionBuilder<O, E> or(Consumer<O> consumer) {
        Method method = LambdaMethod.createMethod(consumer);
        Object value = ClassUtils.invokeMethod(method, obj);
        String filed = LambdaMethod.getMethodField(method);
        conditionCommandExecutor.or(filed, value);
        return this;
    }


    public E lambdaWithEnd() {
        return conditionCommandExecutor;
    }


}

