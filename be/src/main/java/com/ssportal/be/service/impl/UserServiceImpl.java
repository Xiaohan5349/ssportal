package com.ssportal.be.service.impl;

import com.ssportal.be.config.SecurityUtility;
import com.ssportal.be.model.User;
import com.ssportal.be.model.security.Role;
import com.ssportal.be.model.security.UserRole;
import com.ssportal.be.repository.UserRepository;
import com.ssportal.be.service.UserService;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private BCryptPasswordEncoder passwordEncoder(){
        return SecurityUtility.passwordEncoder();
    }

    @Override
    public User createUser(User user) {
        User localUser = userRepository.findByUsername(user.getUsername());

        if(localUser != null) {
            LOG.info("User with username {} already exist. Nothing will be done. ", user.getUsername());
        } else {

            Set<UserRole> userRoles = new HashSet<>();
            Role localRole = new Role();
            localRole.setRoleId(1);
            userRoles.add(new UserRole(user, localRole));
            user.getUserRoles().addAll(userRoles);

            Date today = new Date();
            user.setJoinDate(today);

            String encryptedPassword = SecurityUtility.passwordEncoder().encode(user.getPassword());
            user.setPassword(encryptedPassword);

            localUser = userRepository.save(user);
        }

        return localUser;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User updateUser(User user) throws NotFoundException {
        User localUser = getUserById(user.getId());
        if(localUser==null) {
            throw new NotFoundException("User not found");
        }

        localUser.setFirstName(user.getFirstName());
        localUser.setLastName(user.getLastName());
        localUser.setEmail(user.getEmail());
        localUser.setPhone(user.getPhone());

        localUser = userRepository.save(localUser);

        return localUser;
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        String encodedPassword = passwordEncoder().encode(newPassword);

        user.setPassword(encodedPassword);
        userRepository.save(user);
    }


}
