package com.sonsure.dumper.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 主键标识
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface Id {

}
