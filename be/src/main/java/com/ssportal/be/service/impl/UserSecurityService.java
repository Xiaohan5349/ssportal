package com.ssportal.be.service.impl;

import com.ssportal.be.model.User;
import com.ssportal.be.model.security.Role;
import com.ssportal.be.model.security.UserRole;
import com.ssportal.be.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service(value = "userService")
public class UserSecurityService implements UserDetailsService {
    private static final Logger LOG = LoggerFactory.getLogger(UserSecurityService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user1 = new User (  );
        Set<UserRole> userRoles = new HashSet<> (  );
        Role role1 = new Role ();
        role1.setName ( "Role_ADMIN" );
        userRoles.add ( new UserRole ( user1, role1 ) );
        user1.getUserRoles ().addAll ( userRoles );

        return user1;
    }
}
