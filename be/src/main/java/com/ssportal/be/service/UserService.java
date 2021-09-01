package com.ssportal.be.service;

import com.ssportal.be.model.User;
import com.ssportal.be.model.security.UserRole;

import java.util.List;
import java.util.Set;

public interface UserService {

    User create(User user) throws Exception;

    User createUser(User user, Set<UserRole> userRoles) throws Exception;

    User save(User user);

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();
}
