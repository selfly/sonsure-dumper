package com.sonsure.dumper.core.exception;


import com.sonsure.commons.enums.IEnum;
import com.sonsure.commons.exception.SonsureException;

/**
 * Created by liyd on 17/4/11.
 */
public class SonsureJdbcException extends SonsureException {

    private static final long serialVersionUID = -5833705110587887140L;

    /**
     * Instantiates a new KtanxJdbcException.
     *
     * @param e the e
     */
    public SonsureJdbcException(IEnum e) {
        super(e);
    }

    public SonsureJdbcException(String message, Throwable e) {
        super(message, e);
    }

    public SonsureJdbcException(IEnum msgEnum, Throwable e) {
        super(msgEnum, e);
    }

    /**
     * Instantiates a new KtanxJdbcException.
     *
     * @param e the e
     */
    public SonsureJdbcException(Throwable e) {
        super(e);
    }

    /**
     * Constructor
     *
     * @param message the message
     */
    public SonsureJdbcException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param code    the code
     * @param message the message
     */
    public SonsureJdbcException(String code, String message) {
        super(code, message);
    }


}
