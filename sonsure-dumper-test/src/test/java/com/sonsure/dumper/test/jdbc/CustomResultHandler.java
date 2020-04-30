/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.test.jdbc;

import com.sonsure.dumper.core.command.simple.ResultHandler;
import com.sonsure.dumper.test.model.AuthCode;

import java.util.Map;

public class CustomResultHandler implements ResultHandler<AuthCode> {

    @Override
    public AuthCode handle(Object object) {
        Map<String, Object> map = (Map<String, Object>) object;
        AuthCode authCode = new AuthCode();
        authCode.setCode((String) map.get("LOGIN_NAME"));
        authCode.setName((String) map.get("PASSWORD"));
        return authCode;
    }
}
