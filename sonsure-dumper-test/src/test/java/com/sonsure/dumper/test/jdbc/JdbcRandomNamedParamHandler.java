package com.sonsure.dumper.test.jdbc;

import com.sonsure.dumper.core.command.named.NamedParamHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * @author liyd
 */
public class JdbcRandomNamedParamHandler implements NamedParamHandler {

    private static final Random RANDOM = new Random();

    private static final String PLUS = "z";

    private static final String MINUS = "f";

    @Override
    public Object getValue(String paramName) {
        final String[] values = StringUtils.split(paramName, "_");
        int min = this.getNumValue(values[1]);
        int max = this.getNumValue(values[2]);
        int range = max - min;
        final int i = RANDOM.nextInt(range);
        return i + min;
    }

    private int getNumValue(String str) {
        if (StringUtils.startsWithIgnoreCase(str, MINUS)) {
            return Integer.parseInt(StringUtils.substring(str, 1)) * -1;
        } else if (StringUtils.startsWithIgnoreCase(str, PLUS)) {
            return Integer.parseInt(StringUtils.substring(str, 1));
        } else {
            return Integer.parseInt(str);
        }
    }
}
