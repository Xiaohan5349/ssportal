package com.ssportal.be.utilility;

import java.util.Base64;

public class Constants {
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 20 * 60;
    public static final String SIGNING_KEY = "2z4ZmA&$w$$c";
    public static final String SIGNING_KEY_BASE64 = Base64.getEncoder().encodeToString(SIGNING_KEY.getBytes());
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}

