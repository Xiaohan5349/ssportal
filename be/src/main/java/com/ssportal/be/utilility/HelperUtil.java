package com.ssportal.be.utilility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssportal.be.config.exceptions.UnauthorizedException;
import com.ssportal.be.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

public class HelperUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setTimeZone(TimeZone.getDefault());
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static User me() throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        } else {
            throw new UnauthorizedException("User NOT logged in");
        }
    }

    public static User silentMe() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        return null;
    }

    public static String myUsername() {
        User me = silentMe();
        if (me != null) {
            return me.getUsername();
        }
        return null;
    }



    public static String getCurrentUsername() throws Exception {
        return me().getUsername();
    }

    public static ResponseEntity handleException(Throwable ex) {
        HttpStatus httpStatus;
        if (ex instanceof UnauthorizedException) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(Collections.singletonMap("Error", ex.getMessage()), httpStatus);
    }

    public static ResponseEntity handleErrorMessage(String message) {
        return new ResponseEntity<>(Collections.singletonMap("Error", message), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity handleMessage(String message) {
        return new ResponseEntity<>(Collections.singletonMap("Message", message), HttpStatus.OK);
    }

}

