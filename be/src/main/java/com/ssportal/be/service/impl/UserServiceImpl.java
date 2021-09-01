package com.ssportal.be.service.impl;

import com.ssportal.be.config.SecurityUtility;
import com.ssportal.be.model.User;
import com.ssportal.be.model.security.UserRole;
import com.ssportal.be.repository.RoleRepository;
import com.ssportal.be.repository.UserRepository;
import com.ssportal.be.repository.UserRoleRepository;
import com.ssportal.be.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public User create(User user) throws Exception {
        return createUser(user, null);
    }

    @Override
    @Transactional
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {
        if (findByUsername(user.getUsername().toLowerCase()) != null) {
            LOG.info("User with username " + user.getUsername() + " already exist. User can't be created. ");
        } else
        if (findByEmail(user.getEmail()) != null) {
            LOG.info("User with email " + user.getEmail() + " already exist. User can't be created. ");
        } else {

            user.setUsername(user.getUsername().toLowerCase());
            if (!StringUtils.isEmpty(user.getPassword())) {
                String encryptedPassword = SecurityUtility.passwordEncoder().encode(user.getPassword());
                user.setPassword(encryptedPassword);
            }

            if (userRoles != null && userRoles.size() > 0) {
                user.getUserRoles().addAll(userRoles);
            }
            User localUser = userRepository.save(user);

            return localUser;
        }

        return null;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username.toLowerCase());
    }


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public List<User> findAll() {
        List<User> userList = userRepository.findAll();
        return userList;
    }
}