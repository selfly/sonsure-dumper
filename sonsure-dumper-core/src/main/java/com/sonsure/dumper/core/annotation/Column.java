package com.sonsure.dumper.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by liyd on 2016-1-4.
 */
@Target({ FIELD })
@Retention(RUNTIME)
public @interface Column {

    String value();
}
