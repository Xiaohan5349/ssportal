package com.ssportal.be.pingid;

import com.ssportal.be.model.User;

public interface PingIdUserService {
    User getUserDetails(String username);
}
