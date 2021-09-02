package com.ssportal.be.pingid.service;

import com.ssportal.be.pingid.model.Operation;
import org.json.simple.JSONObject;

public interface PingIdOperationService {
    JSONObject addUser(Boolean activateUser);
    JSONObject getUserDetails(Operation operation);
    JSONObject unpairDevice(String deviceId, Operation operation);
}
