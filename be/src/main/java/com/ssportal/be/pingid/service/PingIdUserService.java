package com.ssportal.be.pingid.service;

import com.ssportal.be.model.User;

public interface PingIdUserService {
    User getUserDetails(String username);
}
