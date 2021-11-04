package com.ssportal.be.service;

import com.ssportal.be.model.User;
import com.ssportal.be.model.security.UserRole;

import java.util.List;
import java.util.Set;

public interface UserService {


    User createUser(User user, Set<UserRole> userRoles) throws Exception;

}
