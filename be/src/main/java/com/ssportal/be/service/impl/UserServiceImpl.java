package com.ssportal.be.service.impl;

import com.ssportal.be.config.SecurityUtility;
import com.ssportal.be.model.User;
import com.ssportal.be.model.security.UserRole;
import com.ssportal.be.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);


    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {
        user.setUsername ( user.getUsername ().toLowerCase () );
        if (!StringUtils.hasText ( user.getPassword () )) {
            String encryptedPassword = SecurityUtility.passwordEncoder ().encode ( user.getPassword () );
            user.setPassword ( encryptedPassword );
        }
        if (userRoles != null && userRoles.size () > 0) {
            user.getUserRoles ().addAll ( userRoles );
        }
        return user;
    }
}