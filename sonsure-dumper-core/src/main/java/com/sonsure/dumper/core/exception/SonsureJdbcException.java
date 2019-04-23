package com.sonsure.dumper.core.exception;


import com.sonsure.commons.exception.SonsureException;

/**
 * Created by liyd on 17/4/11.
 */
public class SonsureJdbcException extends SonsureException {

    private static final long serialVersionUID = -5833705110587887140L;

    public SonsureJdbcException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * Constructor
     *
     * @param message the message
     */
    public SonsureJdbcException(String message) {
        super(message);
    }

}
