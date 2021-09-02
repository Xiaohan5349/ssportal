package com.ssportal.be.controller;

import com.ssportal.be.model.User;
import com.ssportal.be.pingid.service.PingIdMobileService;
import com.ssportal.be.pingid.service.PingIdUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/pingid")
public class PingIdController {
    private final PingIdUserService pingIdUserService;
    private final PingIdMobileService pingIdMobileService;

    public PingIdController(PingIdUserService pingIdUserService, PingIdMobileService pingIdMobileService) {
        this.pingIdUserService = pingIdUserService;
        this.pingIdMobileService = pingIdMobileService;
    }

    @RequestMapping("/getUserDetails")
    public User getUser(Principal principal) {
        String username = principal.getName();
        User user = pingIdUserService.getUserDetails(username);

        return user;
    }
}
