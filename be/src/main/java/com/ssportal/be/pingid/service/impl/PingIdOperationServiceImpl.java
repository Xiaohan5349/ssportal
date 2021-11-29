package com.ssportal.be.pingid.service.impl;

import com.ssportal.be.model.User;
import com.ssportal.be.pingid.model.Application;
import com.ssportal.be.pingid.model.DeviceDetail;
import com.ssportal.be.pingid.model.Operation;
import com.ssportal.be.pingid.model.PingIdUser;
import com.ssportal.be.pingid.service.PingIdOperationService;
import com.ssportal.be.pingid.utils.OperationHelpers;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jose4j.base64url.Base64;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Service
public class PingIdOperationServiceImpl implements PingIdOperationService {
    private static final Logger LOG = Logger.getLogger(PingIdOperationServiceImpl.class);

    // public methods
    public JSONObject addUser(User user, Boolean activateUser, Operation operation) {
        //Operation operation = new Operation();

        operation.setName("AddUser");
        operation.setEndpoint(operation.getApiUrl() + "/rest/4/adduser/do");

        JSONObject reqBody = new JSONObject();
        reqBody.put("activateUser", activateUser);
        if (user.getEmail () != null) {
            reqBody.put("email", user.getEmail ());
        }
        if (user.getFirstName () != null) {
            reqBody.put("fName", user.getFirstName ());
        }
        if (user.getLastName () != null) {
            reqBody.put("lname", user.getLastName ());
        }
        reqBody.put("username", user.getUsername ());
        reqBody.put("role", user.getUserRoles ());
        reqBody.put("clientData", operation.getClientData());

        operation.setRequestToken(OperationHelpers.buildRequestToken(reqBody, operation));
        OperationHelpers.sendRequest(operation);
        JSONObject response = OperationHelpers.parseResponse(operation);
        operation.getValues().clear();
        if (activateUser) {
            operation.setLastActivationCode((String) response.get("activationCode"));
        }
        return response;
    }

    // public methods
    public JSONObject editUser(User user, Boolean activateUser, Operation operation) {
        //Operation operation = new Operation();

        operation.setName("EditUser");
        operation.setEndpoint(operation.getApiUrl() + "/rest/4/edituser/do");

        JSONObject reqBody = new JSONObject();
        reqBody.put("activateUser", activateUser);
        if (user.getEmail () != null) {
            reqBody.put("email", user.getEmail ());
        }
        if (user.getFirstName () != null) {
            reqBody.put("fName", user.getFirstName ());
        }
        if (user.getLastName () != null) {
            reqBody.put("lName", user.getLastName ());
        }
        reqBody.put("userName", user.getUsername ());
        reqBody.put("role", user.getUserRoles ());
        reqBody.put("clientData", operation.getClientData());

        operation.setRequestToken(OperationHelpers.buildRequestToken(reqBody, operation));
        OperationHelpers.sendRequest(operation);
        JSONObject response = OperationHelpers.parseResponse(operation);
        operation.getValues().clear();
        if (activateUser) {
            operation.setLastActivationCode((String) response.get("activationCode"));
        }
        return response;
    }

    @SuppressWarnings("unchecked")
    public JSONObject getUserDetails(Operation operation) {
        operation.setName("GetUserDetails");
        operation.setEndpoint(operation.getApiUrl() + "/rest/4/getuserdetails/do");
        JSONObject reqBody = new JSONObject();
        reqBody.put("getSameDeviceUsers", false);
        reqBody.put("userName", operation.getPingIdUser().getUserName());
        reqBody.put("clientData", operation.getClientData());
        operation.setRequestToken(OperationHelpers.buildRequestToken(reqBody, operation));
        OperationHelpers.sendRequest(operation);
        operation.getValues().clear();
        JSONObject response = OperationHelpers.parseResponse(operation);
        LOG.debug("API response from operation = GetUserDetails for user = " + operation.getPingIdUser().getUserName() + ": " + response);

        JSONObject userDetails = (JSONObject) response.get("userDetails");
        if (userDetails == null) {
            LOG.warn("User not found.");
            return null;
        }

        operation.setPingIdUser(new PingIdUser(userDetails));
        if (userDetails.get("deviceDetails") != null) {
            DeviceDetail deviceDetail = new DeviceDetail((JSONObject) userDetails.get("deviceDetails"));
            operation.getPingIdUser().setDeviceDetail(deviceDetail);
        }
        return userDetails;
    }

    @SuppressWarnings("unchecked")
    public JSONObject unpairDevice(String deviceId, Operation operation) {

        operation.setName("UnpairDevice");
        operation.setEndpoint(operation.getApiUrl()+ "/rest/4/unpairdevice/do");

        JSONObject reqBody = new JSONObject();
        reqBody.put("userName", operation.getPingIdUser().getUserName());
        reqBody.put("deviceId", deviceId);
        reqBody.put("clientData", operation.getClientData());

        operation.setRequestToken(OperationHelpers.buildRequestToken(reqBody, operation));

        OperationHelpers.sendRequest(operation);
        JSONObject response = OperationHelpers.parseResponse(operation);
        operation.getValues().clear();
        return response;
    }

    @SuppressWarnings("unchecked")
    public String getActivationCode(String type, Operation operation) {

        operation.setName("GetActivationCode");
        operation.setEndpoint(operation.getApiUrl() + "/rest/4/getactivationcode/do");

        JSONObject reqBody = new JSONObject();
        reqBody.put("userName", operation.getPingIdUser().getUserName());
        reqBody.put("clientData", operation.getClientData());
        reqBody.put("deviceType", type);
        operation.setRequestToken(OperationHelpers.buildRequestToken(reqBody, operation));

        OperationHelpers.sendRequest(operation);
        JSONObject response = OperationHelpers.parseResponse(operation);
        operation.getValues().clear();
        operation.setLastActivationCode((String) response.get("activationCode"));
        return operation.getLastActivationCode();
    }

    @SuppressWarnings("unchecked")
    public JSONObject getPairingStatus(String activationCode, Operation operation) {

        operation.setName("GetPairingStatus");
        operation.setEndpoint(operation.getApiUrl() + "/rest/4/pairingstatus/do");

        JSONObject reqBody = new JSONObject();
        reqBody.put("activationCode", activationCode);
        reqBody.put("clientData", operation.getClientData());

        operation.setRequestToken(OperationHelpers.buildRequestToken(reqBody, operation));

        OperationHelpers.sendRequest(operation);
        JSONObject response = OperationHelpers.parseResponse(operation);
        return response;
    }

    @SuppressWarnings("unchecked")
    public JSONObject authenticateOnline(Application application, String authType, String deviceId, Operation operation) {

        operation.setName("AuthenticateOnline");
        operation.setEndpoint(operation.getApiUrl() + "/rest/4/authonline/do");

        JSONObject reqBody = new JSONObject();
        reqBody.put("authType", authType);
        reqBody.put("spAlias", application.getSpAlias());
        reqBody.put("userName", operation.getPingIdUser().getUserName());
        reqBody.put("clientData", operation.getClientData());
        if (!"primary".equals(deviceId)) {
            reqBody.put("deviceId", deviceId);
        }
        JSONObject formParameters = new JSONObject();
        formParameters.put("sp_name", application.getName());
        /*		if (application.getLogoUrl() != null || !application.getLogoUrl().isEmpty()) {
			formParameters.put("sp_logo", application.getLogoUrl());
		}
         */
        reqBody.put("formParameters", formParameters);

        operation.setRequestToken(OperationHelpers.buildRequestToken(reqBody, operation));

        OperationHelpers.sendRequest(operation);
        JSONObject response = OperationHelpers.parseResponse(operation);

        if (operation.getWasSuccessful()) {
            operation.getValues().clear();
            operation.setLastSessionId((String) response.get("sessionId"));
        }
        return response;
    }


    public JSONObject makeDevicePrimary(String deviceID, Operation operation) {
        operation.setName("UpdateDeviceAttributes");
        operation.setEndpoint(operation.getApiUrl() + "/rest/4/updatedeviceattr/do");

        JSONObject reqBody = new JSONObject();
        reqBody.put("userName", operation.getPingIdUser().getUserName());
        reqBody.put("attributeName", "SET_PRIMARY");
        reqBody.put("attributeValue", "true");
        reqBody.put("deviceId", deviceID);
        reqBody.put("clientData", operation.getClientData());

        operation.setRequestToken(OperationHelpers.buildRequestToken(reqBody, operation));

        OperationHelpers.sendRequest(operation);
        JSONObject response = OperationHelpers.parseResponse(operation);

        return response;
    }

    @SuppressWarnings("unchecked")
    public String webAuthnAuthentication(String deviceId,  String returnUrl, Operation operation) {

        operation.setName("WebAuthnAuthentication");

        JSONObject reqBody = new JSONObject();
        reqBody.put("iss", "MFA_Auth");
        reqBody.put("sub", operation.getPingIdUser().getUserName());
        reqBody.put("aud", "pingidauthenticator");
        Calendar calendar = Calendar.getInstance();
        reqBody.put("iat", calendar.getTimeInMillis());
        calendar.add(Calendar.MINUTE, 5);
        reqBody.put("exp", calendar.getTimeInMillis());
        reqBody.put("nonce", "hiqasdfhkj");
        reqBody.put("idpAccountId", operation.getOrgAlias());
        JSONArray attributes = new JSONArray();
        JSONObject pingSpAliasObj = new JSONObject();
        pingSpAliasObj.put("name", "pingidSpAlias");
        pingSpAliasObj.put("value", "web");
        attributes.add(pingSpAliasObj);
        JSONObject webAuthnDeviceObj = new JSONObject();
        webAuthnDeviceObj.put("name", "webAuthnDeviceId");
        webAuthnDeviceObj.put("value", deviceId);
        attributes.add(webAuthnDeviceObj);
        reqBody.put("attributes", attributes);
        reqBody.put("returnUrl", returnUrl);
        operation.setRequestToken(OperationHelpers.signAuthnRequest(reqBody, operation));

        return operation.getRequestToken();
    }


}