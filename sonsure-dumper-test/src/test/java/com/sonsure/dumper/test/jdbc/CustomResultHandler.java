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
