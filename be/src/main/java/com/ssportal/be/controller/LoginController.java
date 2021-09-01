package com.ssportal.be.controller;

import com.ssportal.be.config.JwtTokenUtil;
import com.ssportal.be.model.User;
import com.ssportal.be.repository.RoleRepository;
import com.ssportal.be.repository.UserRepository;
import com.ssportal.be.service.UserService;
import com.ssportal.be.utilility.ApiResponse;
import com.ssportal.be.utilility.AuthToken;
import com.ssportal.be.utilility.HelperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;

@RestController
public class LoginController {
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    private final UserService userService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public LoginController(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public Object authenticate(
            @RequestBody HashMap<String, String> mapper
    ){
        String username = mapper.get("username");
        String password = mapper.get("password");
        if (username != null && password != null) {
            username = username.toLowerCase();
            try {
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                final String token = jwtTokenUtil.generateToken(authentication.getPrincipal());
                return new ApiResponse<>(200, "success", new AuthToken(token, username));
            } catch (Exception ex) {
                LOG.error("", ex);
                return HelperUtil.handleException(ex);
            }
        }

        return null;
    }
}
