package com.ssportal.be.controller;

import com.ssportal.be.model.User;
import com.ssportal.be.pingid.service.PingIdMobileService;
import com.ssportal.be.pingid.service.PingIdUserService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/pingid")
public class PingIdController {
    private static final Logger LOG = Logger.getLogger(PingIdController.class);


}
