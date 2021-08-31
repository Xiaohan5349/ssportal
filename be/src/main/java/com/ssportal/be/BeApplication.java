package com.ssportal.be;

import com.ssportal.be.model.User;
import com.ssportal.be.model.security.Role;
import com.ssportal.be.model.security.UserRole;
import com.ssportal.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BeApplication implements CommandLineRunner {
    public final UserService userService;

    @Autowired
    public BeApplication(UserService userService) {
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BeApplication.class, args);
    }

    @Override
    public void run(String... args)  {
        User user1 = new User();
        user1.setFirstName("Admin1");
        user1.setLastName("Admin1");
        user1.setUsername("Admin1");
        user1.setPassword("password");
        user1.setEmail("admin1@example.com");
        Set<UserRole> userRoles = new HashSet<>();
        Role role1 = new Role();
        role1.setName("ROLE_ADMIN");
        userRoles.add(new UserRole(user1, role1));
        userService.createUser(user1);
    }
}
