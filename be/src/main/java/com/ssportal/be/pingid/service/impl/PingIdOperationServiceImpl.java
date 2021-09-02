package com.ssportal.be.pingid.service.impl;

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

@Service
public class PingIdOperationServiceImpl implements PingIdOperationService {
    private static final Logger LOG = Logger.getLogger(PingIdOperationServiceImpl.class);

    // public methods
    public JSONObject addUser(Boolean activateUser) {
        Operation operation = new Operation();

        operation.setName("AddUser");
        operation.setEndpoint(operation.getApiUrl() + "/rest/4/adduser/do");

        JSONObject reqBody = new JSONObject();
        reqBody.put("activateUser", activateUser);
        if (operation.getPingIdUser().getEmail() != null) {
            reqBody.put("email", operation.getPingIdUser().getEmail());
        }
        if (operation.getPingIdUser().getfName() != null) {
            reqBody.put("fName", operation.getPingIdUser().getfName());
        }
        if (operation.getPingIdUser().getlName() != null) {
            reqBody.put("lname", operation.getPingIdUser().getlName());
        }
        reqBody.put("username", operation.getPingIdUser().getUserName());
        reqBody.put("role", operation.getPingIdUser().getRole().getValue());
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
}
