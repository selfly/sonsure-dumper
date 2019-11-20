package com.sonsure.dumper.core.command.lambda;

import com.sonsure.commons.utils.NameUtils;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import org.apache.commons.lang3.StringUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class LambdaMethod {

    public static <T> String getField(Consumer<T> lambda) {
        Method method = createMethod(lambda);
        return getMethodField(method);
    }

    public static <T> String[] getFields(Consumer<T>... consumers) {
        String[] fields = new String[consumers.length];
        for (int i = 0; i < consumers.length; i++) {
            fields[i] = getField(consumers[i]);
        }
        return fields;
    }

    public static String getMethodField(Method getter) {
        String name = getter.getName();
        if (!StringUtils.startsWith(name, "get")) {
            throw new SonsureJdbcException("只能是JavaBean的get方法");
        }
        return NameUtils.getFirstLowerName(StringUtils.substring(name, 3));
    }

    public static <T> Method createMethod(Consumer<T> lambda) {
        SerializedLambda serializedLambda = getSerializedLambda(lambda);
        return getMethod(serializedLambda);
    }

    private static SerializedLambda getSerializedLambda(Object lambda) {
        try {
            Method m = lambda.getClass().getDeclaredMethod("writeReplace");
            m.setAccessible(true);
            return (SerializedLambda) m.invoke(lambda);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Method getMethod(SerializedLambda serializedLambda) {
        if (serializedLambda == null) {
            return null;
        }
        String className = StringUtils.replace(serializedLambda.getImplClass(), "/", ".");
        try {
            return LambdaMethod.class.getClassLoader().loadClass(className).getDeclaredMethod(serializedLambda.getImplMethodName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
