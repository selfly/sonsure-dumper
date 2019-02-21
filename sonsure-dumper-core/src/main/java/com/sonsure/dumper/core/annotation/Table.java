package com.sonsure.dumper.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by liyd on 2016-1-4.
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface Table {

    String value();
}
