package com.ssportal.be.pingid.service;

import com.ssportal.be.model.User;
import com.ssportal.be.pingid.model.Application;
import com.ssportal.be.pingid.model.Operation;
import com.ssportal.be.pingid.model.RequestData;
import org.json.simple.JSONObject;

public interface PingIdOperationService {
    JSONObject addUser(User user, Boolean activateUser, Operation operation);
    JSONObject editUser(User user, Boolean activateUser, Operation operation);
    JSONObject getUserDetails(Operation operation);
    JSONObject unpairDevice(String deviceId, Operation operation);
    String getActivationCode(String type, Operation operation);
    JSONObject AuthenticatorAppStartPairing(Operation operation);
    JSONObject AuthenticatorAppFinishPairing(Operation operation, String sessionId, String otp);
    JSONObject ToggleUserBypass(Operation operation);
    JSONObject getPairingStatus(String activationCode, Operation operation);
    JSONObject authenticateOnline(Application application, String authType, String deviceId, Operation operation);

    JSONObject backupOnline(RequestData requestData, String authType, String deviceId, Operation operation);

    JSONObject startOfflinePairing(Operation operation, String phoneNumber);

    JSONObject finalizeOfflinePairing(Operation operation, String sessionId, String otp);

    JSONObject authenticationOffline(String sessionId, String otp, Operation operation);
    JSONObject makeDevicePrimary(String deviceID, Operation operation);
    JSONObject SuspendUser(Operation operation);
    JSONObject ActivateUser(Operation operation);
    JSONObject pairYubiKey(String otp, Operation operation);
    String webAuthnAuthentication(String deviceId,  String returnUrl, Operation operation);

}