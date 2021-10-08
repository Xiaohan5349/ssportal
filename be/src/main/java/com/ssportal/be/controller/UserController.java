package com.ssportal.be.controller;

import com.ssportal.be.model.User;
import com.ssportal.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;


@RestController

@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping("/getCurrentUser")
    public User getUser(Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);

        return user;
    }


    @RequestMapping("/getUserByUsername")
    public User getUser(@RequestBody HashMap<String, String> mapper) {
        String username = mapper.get("username");

        User user = userService.findByUsername(username);

        return user;
    }


}
