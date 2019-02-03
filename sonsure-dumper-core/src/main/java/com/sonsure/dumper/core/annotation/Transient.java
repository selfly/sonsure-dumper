package com.sonsure.dumper.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by liyd on 2015-12-31.
 */
@Target({ METHOD })
@Retention(RUNTIME)
public @interface Transient {
}
