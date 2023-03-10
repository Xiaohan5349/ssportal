package com.ssportal.be.pingid.service.impl;

import com.cedarsoftware.util.io.JsonObject;
import com.ssportal.be.model.User;
import com.ssportal.be.pingid.model.*;
import com.ssportal.be.pingid.service.PingIdOperationService;
import com.ssportal.be.pingid.utils.OperationHelpers;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger LOG = LogManager.getLogger(PingIdOperationServiceImpl.class);
    // public methods
    public JSONObject addUser(User user, Boolean activateUser, Operation operation) {
        //Operation operation = new Operation();
        LOG.info ( "addUser service entry" );
        operation.setName("AddUser");
        operation.setEndpoint(operation.getApiUrl() + "/rest/4/adduser/do");

        JSONObject reqBody = new JSONObject();
        reqBody.put("activateUser", activateUser);
        if (user.getEmail () != null) {
            reqBody.put("email", user.getEmail ());
        }
        if (user.getFirstName () != null) {
            reqBody.put("fname", user.getFirstName ());
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

        LOG.info ( "addUser service exit" );
        return response;
    }

    // public methods
    public JSONObject editUser(User user, Boolean activateUser, Operation operation) {
        //Operation operation = new Operation();
        LOG.info ( "editUser service entry" );

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

        LOG.info ( "editUser service exit" );
        return response;
    }

    @SuppressWarnings("unchecked")
    public JSONObject getUserDetails(Operation operation) {
        LOG.info ( "getUserDetails service entry" );


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

        LOG.info ( "getUserDetails service exit" );
        return userDetails;
    }

    public JSONObject pairYubiKey(String otp, Operation operation){

        LOG.info ( "pairYubiKey service entry" );

        operation.setName ( "pairYubiKey" );
        operation.setEndpoint ( operation.getApiUrl () + "/rest/4/pairyubikey/do " );

        JSONObject resBody = new JSONObject (  );
        resBody.put ( "otp", otp );
        resBody.put ( "userName", operation.getPingIdUser ().getUserName () );

        operation.setRequestToken ( OperationHelpers.buildRequestToken ( resBody, operation ) );

        OperationHelpers.sendRequest ( operation );
        JSONObject response = OperationHelpers.parseResponse ( operation );
        operation.getValues ().clear ();

        LOG.info ( "pairYubiKey service exit" );
        return response;

    }

    @SuppressWarnings("unchecked")
    public JSONObject unpairDevice(String deviceId, Operation operation) {

        LOG.info ( "unpairDevice service entry" );

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

        LOG.info ( "unpairDevice service exit" );
        return response;
    }

    @SuppressWarnings("unchecked")
    public String getActivationCode(String type, Operation operation) {

        LOG.info ( "getActivationCode service entry" );

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

        LOG.info ( "getActivationCode service exit" );
        return operation.getLastActivationCode();
    }

    public JSONObject startOfflinePairing(Operation operation, String phoneNumber){

        LOG.info ( "startOfflinePairing service entry" );

        operation.setName ( "StartOfflinePairing" );
        operation.setEndpoint ( operation.getApiUrl ()+"/rest/4/startofflinepairing/do" );

        JSONObject reqBody = new JSONObject (  );
        phoneNumber = "1"+phoneNumber;
        LOG.info ( "the phone number is "+phoneNumber );
        reqBody.put ( "username", operation.getPingIdUser ().getUserName () );
        reqBody.put ( "type", "SMS" );
        reqBody.put("pairingData", phoneNumber);

        operation.setRequestToken ( OperationHelpers.buildRequestToken ( reqBody, operation ) );

        OperationHelpers.sendRequest ( operation );
        JSONObject response = OperationHelpers.parseResponse ( operation );
        LOG.info ( "StartOfflinePairing response from PingID: " + response.get ( "sessionId" ));
        operation.getValues ().clear ();
        JSONObject authenticator = new JSONObject (  );
        authenticator.put ( "sessionId", response.get ( "sessionId" ));

        LOG.info ( "startOfflinePairing service exit" );
        return authenticator;
    }

    public JSONObject finalizeOfflinePairing(Operation operation, String sessionId, String otp){

        LOG.info ( "finalizeOfflinePairing service entry" );

        operation.setName ( "FinalizeOfflinePairing" );
        operation.setEndpoint ( operation.getApiUrl ()+"/rest/4/finalizeofflinepairing/do" );

        JSONObject repBody = new JSONObject (  );
        repBody.put ( "sessionId", sessionId );
        repBody.put ( "otp", otp );
        operation.setRequestToken ( OperationHelpers.buildRequestToken ( repBody, operation ));

        OperationHelpers.sendRequest ( operation );
        JSONObject response = OperationHelpers.parseResponse ( operation );
        LOG.info ( "FinalizeOfflinePairing response from PingID: " + response.get ( "sessionId" ));
        operation.getValues ().clear ();

        LOG.info ( "finalizeOfflinePairing service exit" );
        return response;

    }


    @SuppressWarnings("unchecked")
    public JSONObject AuthenticatorAppStartPairing(Operation operation){

        LOG.info ( "AuthenticatorAppStartPairing service entry" );

        operation.setName ( "AuthenticatorAppStartPairing" );
        operation.setEndpoint ( operation.getApiUrl ()+"/rest/4/authenticatorappstartpairing/do" );

        JSONObject reqBody = new JSONObject (  );
        reqBody.put ( "username", operation.getPingIdUser ().getUserName () );
        reqBody.put ( "pairingType", "TOTP" );
        operation.setRequestToken ( OperationHelpers.buildRequestToken ( reqBody, operation ) );

        OperationHelpers.sendRequest ( operation );
        JSONObject response = OperationHelpers.parseResponse ( operation );
        operation.getValues ().clear ();
        operation.setPairingKey(response.get ( "pairingKey" ).toString ().replaceAll ( "\\s+", "" ));
        JSONObject authenticator = new JSONObject (  );
        authenticator.put ( "sessionId", response.get ( "sessionId" ));
        authenticator.put ( "pairingKeyUri", response.get ( "pairingKeyUri" ) );
        authenticator.put ( "pairingKey", operation.getPairingKey () );

        LOG.info ( "AuthenticatorAppStartPairing service exit" );
        return authenticator;
    }

    public JSONObject AuthenticatorAppFinishPairing(Operation operation, String sessionId, String otp){

        LOG.info ( "AuthenticatorAppFinishPairing service entry" );

        operation.setName ( "AuthenticatorAppFinishPairing" );
        operation.setEndpoint ( operation.getApiUrl ()+"/rest/4/authenticatorappfinishpairing/do" );

        JSONObject repBody = new JSONObject (  );
        repBody.put ( "sessionId", sessionId );
        repBody.put ( "otp", otp );
        operation.setRequestToken ( OperationHelpers.buildRequestToken ( repBody, operation ));

        OperationHelpers.sendRequest ( operation );
        JSONObject response = OperationHelpers.parseResponse ( operation );
        operation.getValues ().clear ();

        LOG.info ( "AuthenticatorAppFinishPairing service exit" );
        return response;

    }

    public JSONObject ToggleUserBypass(Operation operation){

        LOG.info ( "Class ToggleUserBypass Entry" );

        operation.setName ( "ToggleUserBypass" );
        operation.setEndpoint ( operation.getApiUrl () + "/rest/4/userbypass/do" );

        long date = System.currentTimeMillis();
        long bypass_date = date + 30 * 60 * 1000;

        JSONObject resBody = new JSONObject (  );
        resBody.put ( "bypassUntil", bypass_date );
        resBody.put ( "userName", operation.getPingIdUser ().getUserName () );
        operation.setRequestToken ( OperationHelpers.buildRequestToken ( resBody, operation ) );

        OperationHelpers.sendRequest ( operation );
        JSONObject response = OperationHelpers.parseResponse ( operation );
        operation.getValues ().clear ();
        if(!(response.get ( "errorId" ).toString ().equals ( "200" ))){
            LOG.info ( "test true" );
        }
        LOG.info ( response.get ( "errorId" ).toString () );
        LOG.error(response.get ( "errorMsg" ).toString ());
        LOG.info ("response Content:" + response.toString ());

        LOG.info ( "Class ToggleUserBypass Exit" );
        return  response;
    }

    public JSONObject SuspendUser(Operation operation){

        LOG.info ( "SuspendUser service entry" );

        operation.setName ( "SuspendUser" );
        operation.setEndpoint ( operation.getApiUrl () + "/rest/4/suspenduser/do" );

        JSONObject resBody = new JSONObject ( );
        resBody.put ( "userName", operation.getPingIdUser ().getUserName () );
        resBody.put ( "clientData", "suspend the user" );
        operation.setRequestToken ( OperationHelpers.buildRequestToken ( resBody, operation ) );

        OperationHelpers.sendRequest ( operation );
        JSONObject response = OperationHelpers.parseResponse ( operation );
        operation.getValues ().clear ();

        LOG.info ( "SuspendUser service exit" );
        return response;
    }


    public JSONObject ActivateUser(Operation operation){

        LOG.info ( "ActivateUser service entry" );

        operation.setName ( "ActivateUser" );
        operation.setEndpoint ( operation.getApiUrl ()+ "/rest/4/activateuser/do" );

        JSONObject resBody = new JSONObject (  );
        resBody.put ( "userName", operation.getPingIdUser ().getUserName () );
        resBody.put ( "clientData", "active this user" );
        operation.setRequestToken ( OperationHelpers.buildRequestToken ( resBody, operation ) );

        OperationHelpers.sendRequest ( operation );
        JSONObject response = OperationHelpers.parseResponse ( operation );
        operation.getValues ().clear ();

        LOG.info ( "ActivateUser service exit" );
        return response;
    }



    @SuppressWarnings("unchecked")
    public JSONObject getPairingStatus(String activationCode, Operation operation) {

        LOG.info ( "getPairingStatus service entry" );

        operation.setName("GetPairingStatus");
        operation.setEndpoint(operation.getApiUrl() + "/rest/4/pairingstatus/do");

        JSONObject reqBody = new JSONObject();
        reqBody.put("activationCode", activationCode);
        reqBody.put("clientData", operation.getClientData());

        operation.setRequestToken(OperationHelpers.buildRequestToken(reqBody, operation));

        OperationHelpers.sendRequest(operation);
        JSONObject response = OperationHelpers.parseResponse(operation);

        LOG.info ( "getPairingStatus service exit" );
        return response;
    }

    @SuppressWarnings("unchecked")
    public JSONObject authenticateOnline(Application application, String authType, String deviceId, Operation operation) {

        LOG.info ( "authenticateOnline service entry" );

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

        LOG.info ( "authenticateOnline service exit" );
        return response;
    }

    @SuppressWarnings("unchecked")
    public JSONObject backupOnline(RequestData requestData, String authType, Operation operation) {

        LOG.info("backupOnline service entry");

        operation.setName("AuthenticateOnline");
        operation.setEndpoint(operation.getApiUrl() + "/rest/4/authonline/do");

        JSONObject reqBody = new JSONObject();
        reqBody.put("authType", authType);
        reqBody.put("spAlias", requestData.getSpAlias());
        reqBody.put ( "deviceType", requestData.getDeviceType () );
        reqBody.put ( "deviceData", requestData.getDeviceData () );



        operation.setRequestToken(OperationHelpers.buildRequestToken(reqBody, operation));

        OperationHelpers.sendRequest(operation);
        JSONObject response = OperationHelpers.parseResponse(operation);

        if (operation.getWasSuccessful()) {
            operation.getValues().clear();
            operation.setLastSessionId((String) response.get("sessionId"));
        }

        LOG.info("backupOnline service exit");
        return response;
    }



    public JSONObject authenticationOffline(String sessionId, String otp, Operation operation, String spAlias){

        LOG.info("authenticationOffline service entry");

        operation.setName ( "AuthenticationOffline" );
        operation.setEndpoint ( operation.getApiUrl () + "/rest/4/authoffline/do" );

        JSONObject reqBody = new JSONObject();
        //todo: need add a parameter to decide user web or rescuecode
        reqBody.put ( "spAlias", spAlias );
        reqBody.put ( "otp", otp );
        reqBody.put ( "userName", operation.getPingIdUser ().getUserName () );
        reqBody.put ( "sessionId", sessionId );

        operation.setRequestToken ( OperationHelpers.buildRequestToken ( reqBody, operation ) );
        OperationHelpers.sendRequest ( operation );

        JSONObject response = OperationHelpers.parseResponse ( operation );

        if ((long)response.get ( "errorId" ) == 200){
            operation.getValues ().clear ();
        }

        LOG.info("authenticationOffline service exit");
        return response;
    }



    public JSONObject makeDevicePrimary(String deviceID, Operation operation) {

        LOG.info("makeDevicePrimary service entry");

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

        LOG.info("makeDevicePrimary service exit");
        return response;
    }

    @SuppressWarnings("unchecked")
    public String webAuthnAuthentication(String deviceId,  String returnUrl, Operation operation) {

        LOG.info("webAuthnAuthentication service entry");

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

        LOG.info("webAuthnAuthentication service exit");
        return operation.getRequestToken();
    }


}