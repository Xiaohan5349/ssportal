package com.ssportal.be;

import com.ssportal.be.model.User;
import com.ssportal.be.model.security.Role;
import com.ssportal.be.model.security.UserRole;
import com.ssportal.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
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
    public void run(String... args) throws Exception {
//        User user1 = new User();
//        user1.setFirstName("Admin1");
//        user1.setLastName("Admin1");
//        user1.setUsername("Admin1");
//        user1.setPassword("password");
//        user1.setEmail("admin1@example.com");
//        Set<UserRole> userRoles = new HashSet<>();
//        Role role1 = new Role();
//        role1.setName("ROLE_ADMIN");
//        userRoles.add(new UserRole(user1, role1));
//        userService.createUser(user1, userRoles);
//
//        userRoles.clear();
//        User user2 = new User();
//        user2.setFirstName("Le");
//        user2.setLastName("Deng");
//        user2.setUsername("ldeng");
//        user2.setPassword("password");
//        user2.setEmail("le.deng@authright.com");
//        Role role2 = new Role();
//        role2.setName("ROLE_USER");
//        userRoles.add(new UserRole(user2, role2));
//        userService.createUser(user2, userRoles);
//
//        userRoles.clear();
//        User user3 = new User();
//        user3.setFirstName("lPing");
//        user3.setLastName("Deng");
//        user3.setUsername("ldengpingproject");
//        user3.setPassword("password");
//        user3.setEmail("ray.deng83@authright.com");
//        userRoles.add(new UserRole(user3, role2));
//        userService.createUser(user3, userRoles);
    }
}
