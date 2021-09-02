package com.ssportal.be.pingid.service;

import com.ssportal.be.pingid.model.Operation;
import org.json.simple.JSONObject;

public interface PingIdOperationService {
    JSONObject AddUser(Boolean activateUser);
    JSONObject GetUserDetails(Operation operation);
}
