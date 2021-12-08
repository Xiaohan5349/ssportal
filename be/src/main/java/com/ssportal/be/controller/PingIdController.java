package com.ssportal.be.controller;

import java.io.IOException;
import java.security.Principal;
import java.security.Timestamp;
import java.util.HashMap;
import javax.servlet.ServletException;

import com.ssportal.be.pingid.model.Application;
import com.ssportal.be.pingid.model.Operation;
import com.ssportal.be.pingid.model.PingIdProperties;
import com.ssportal.be.pingid.model.PingIdUser;
import com.ssportal.be.pingid.service.PingIdOperationService;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pingid")
public class PingIdController {
    private static final Logger LOG = Logger.getLogger(PingIdController.class);
    private PingIdProperties pingIdProperties;

    public PingIdController() throws IOException {
        this.pingIdProperties = new PingIdProperties();
        pingIdProperties.setProperties(0);
    }

    @Autowired
    private PingIdOperationService pingIdOperationService;

    @RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
    public JSONObject getUserDetails(@RequestBody HashMap<String, String> mapper) throws IOException, ServletException {
        String username = mapper.get("username");

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser(username);
        JSONObject userDetails = pingIdOperationService.getUserDetails(operation);

        return userDetails;
    }

    @RequestMapping(value = "/getUserDetailsByUsername", method = RequestMethod.POST)
    public ResponseEntity getUserDetailsByUsername(@RequestBody HashMap<String, String> mapper) throws IOException, ServletException {
        String username = mapper.get("username");
        JSONObject userDetails = null;

        if (username != null && !username.trim().equalsIgnoreCase("")) {
            Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
            operation.setTargetUser(username);
            userDetails = pingIdOperationService.getUserDetails(operation);
        } else {
            return new ResponseEntity<>("Username can not be empty.", HttpStatus.BAD_REQUEST);
        }

        if (userDetails != null) {
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found.", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/unpairDevice", method = RequestMethod.POST)
    public JSONObject unpairDevice(Principal principal, @RequestBody HashMap<String, String> mapper) {
        String deviceId = (String) mapper.get("deviceId");
        String username = mapper.get("username");

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser(username);
        JSONObject response = pingIdOperationService.unpairDevice(deviceId, operation);

        return response;
    }

    @RequestMapping(value = "/getActivationCode", method = RequestMethod.POST)
    public JSONObject getActivationCode(@RequestBody HashMap<String, String> mapper) {
        String type = mapper.get("type");
        String username = mapper.get("username");

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser(username);
        String activationCode = pingIdOperationService.getActivationCode(type, operation);
        JSONObject response = new JSONObject();
        response.put("activationCode", activationCode);
        response.put("type", type);

        return response;
    }

    @RequestMapping(value = "/AuthenticatorAppStartPairing", method = RequestMethod.POST)
    public JSONObject AuthenticatorAppStartPairing(@RequestBody HashMap<String, String> mapper) {
        String username = mapper.get("username");
        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser ( username );

        JSONObject response = pingIdOperationService.AuthenticatorAppStartPairing ( operation );

        return response;
    }

    @RequestMapping(value = "/AuthenticatorAppFinishPairing", method = RequestMethod.POST)
    public JSONObject AuthenticatorAppFinishPairing(@RequestBody HashMap<String, String> mapper){
        String sessionId = mapper.get ( "sessionId" );
        String otp = mapper.get ( "otp" );
        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        JSONObject response = pingIdOperationService.AuthenticatorAppFinishPairing ( operation, sessionId, otp );

        return response;
    }

    @RequestMapping(value = "/getPairingStatus", method = RequestMethod.POST)
    public JSONObject getPairingStatus(@RequestBody HashMap<String, String> mapper) {
        String activationCode = mapper.get("activationCode");
        String username = mapper.get("username");

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser(username);
        JSONObject response = pingIdOperationService.getPairingStatus(activationCode, operation);
        System.out.println(response.get ( "errorId" ));
        return response;
    }

    @RequestMapping(value = "/authenticateOnline", method = RequestMethod.POST)
    public JSONObject authenticateOnline(@RequestBody HashMap<String, String> mapper) {
        String deviceId = mapper.get("deviceId");
        String username = mapper.get("username");

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser(username);
        JSONObject response = pingIdOperationService.authenticateOnline(new Application("MFA"), "CONFIRM", deviceId, operation);

        return response;
    }

    @RequestMapping(value = "/makeDevicePrimary", method = RequestMethod.POST)
    public JSONObject makeDevicePrimary(@RequestBody HashMap<String, String> mapper) {
        String deviceId = mapper.get("deviceId");
        String username = mapper.get("username");

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser(username);
        JSONObject response = pingIdOperationService.makeDevicePrimary(deviceId, operation);

        return response;
    }

    @RequestMapping(value = "/webAuthnStartAuth", method = RequestMethod.POST)
    public JSONObject webAuthnStartAuth(@RequestBody HashMap<String, String> mapper) {
        String deviceId = mapper.get("deviceId");
        String username = mapper.get("username");

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser(username);

        JSONObject response = new JSONObject();
        response.put("deviceId", deviceId);
        response.put("idpAccountId", pingIdProperties.getOrgAlias());
        response.put("ppm", pingIdOperationService.webAuthnAuthentication(deviceId, pingIdProperties.getAppUrl(), operation));
        response.put("postUrl", pingIdProperties.getWebAuthnAuthUrl());
        LOG.debug("Redirecting to PingOne for operation = WebAuthnStartAuth for user = " + username);

        return response;
    }


    @RequestMapping(value = "/ToggleUserBypass", method = RequestMethod.POST)
    public JSONObject ToggleUserBypass(@RequestBody HashMap<String, String> mapper){
        String username = mapper.get ( "username" );
        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser ( username );

        JSONObject response = pingIdOperationService.ToggleUserBypass ( operation );
        return response;
    }

//    @RequestMapping(value = "/pairYubiKey", method = RequestMethod.POST)
//    public void pairYubiKey(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//    }
//

//
//    @RequestMapping(value = "/authenticateOffline", method = RequestMethod.POST)
//    public void authenticateOffline(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//    }
//
//    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
//    public void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//    }
//
//

//
//    @RequestMapping(value = "/updateDeviceNickname", method = RequestMethod.POST)
//    public void updateDeviceNickname(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//    }
//
//    @RequestMapping(value = "/webAuthnStartPairing", method = RequestMethod.POST)
//    public void webAuthnStartPairing(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//    }
//

}