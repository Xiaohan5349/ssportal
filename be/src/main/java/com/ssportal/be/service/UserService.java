package com.ssportal.be.service;

import com.ssportal.be.model.User;
import javassist.NotFoundException;

public interface UserService {
    User createUser(User user);

    User getUserByEmail(String email);

    User getUserByUsername(String username);

    User getUserById(Long id);

    User updateUser(User user) throws NotFoundException;

    void updatePassword(User user, String newPassword);
}
