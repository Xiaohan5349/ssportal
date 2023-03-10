package com.ssportal.be.controller;

import java.io.IOException;
import java.security.Principal;
import java.security.Timestamp;
import java.util.HashMap;
import javax.servlet.ServletException;

import com.ssportal.be.config.JwtTokenUtil;
import com.ssportal.be.pingid.model.*;
import com.ssportal.be.pingid.service.PingIdOperationService;
import com.ssportal.be.utilility.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pingid")
public class PingIdController {
    private static final Logger LOG = LogManager.getLogger(PingIdController.class);
    private PingIdProperties pingIdProperties;

    public PingIdController() throws IOException {
        this.pingIdProperties = new PingIdProperties();
        pingIdProperties.setProperties(0);
    }

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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
    public ResponseEntity getUserDetailsByUsername(@RequestHeader("accept-language") HashMap<String, String> header, @RequestBody HashMap<String, String> mapper) throws IOException, ServletException {
        //permission check
//        String authToken  = header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "");
//        String usernameFromToken = jwtTokenUtil.getUsernameFromToken(authToken);
//        String role = jwtTokenUtil.getStringFromToken(authToken, "admin");
        String username = mapper.get("username");
        if(checkPermission (mapper.get("username"), jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            return new ResponseEntity<>("No Sufficient Permission", HttpStatus.FORBIDDEN);
        }

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
    public JSONObject unpairDevice(@RequestHeader("accept-language") HashMap<String, String> header, @RequestBody HashMap<String, String> mapper) {
        String deviceId = (String) mapper.get("deviceId");
        String username = mapper.get("username");
        //permission check
        if(checkPermission (mapper.get("username"), jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",401);
            responseMap.put("description", "Unauthorized");
            return new JSONObject(responseMap);
        }

            Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser(username);
        JSONObject response = pingIdOperationService.unpairDevice(deviceId, operation);

        return response;
    }

    @RequestMapping(value = "/getActivationCode", method = RequestMethod.POST)
    public JSONObject getActivationCode(@RequestHeader("accept-language") HashMap<String, String> header, @RequestBody HashMap<String, String> mapper) {
        String type = mapper.get("type");
        String username = mapper.get("username");
        //permission check
        if(checkPermission (mapper.get("username"), jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",401);
            responseMap.put("description", "Unauthorized");
            return new JSONObject(responseMap);
        }

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser(username);
        String activationCode = pingIdOperationService.getActivationCode(type, operation);
        JSONObject response = new JSONObject();
        response.put("activationCode", activationCode);
        response.put("type", type);

        return response;
    }

    @RequestMapping(value = "/startOfflinePairing", method = RequestMethod.POST)
    public JSONObject startOfflinePairing(@RequestHeader("accept-language") HashMap<String, String> header, @RequestBody HashMap<String, String> mapper) {
        String username = mapper.get("username");
        String phoneNumber = mapper.get("phone");
        //permission check
        if(checkPermission (mapper.get("username"), jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",401);
            responseMap.put("description", "Unauthorized");
            return new JSONObject(responseMap);
        }

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser ( username );

        JSONObject response = pingIdOperationService.startOfflinePairing ( operation, phoneNumber );

        return response;
    }

    @RequestMapping(value = "/finalizeOfflinePairing", method = RequestMethod.POST)
    public JSONObject finalizeOfflinePairing(@RequestBody HashMap<String, String> mapper){
        String sessionId = mapper.get ( "sessionId" );
        String otp = mapper.get ( "otp" );
        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        JSONObject response = pingIdOperationService.finalizeOfflinePairing ( operation, sessionId, otp );
        return response;
    }

    @RequestMapping(value = "/AuthenticatorAppStartPairing", method = RequestMethod.POST)
    public JSONObject AuthenticatorAppStartPairing(@RequestHeader("accept-language") HashMap<String, String> header, @RequestBody HashMap<String, String> mapper) {
        String username = mapper.get("username");
        //permission check
        if(checkPermission (mapper.get("username"), jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",401);
            responseMap.put("description", "Unauthorized");
            return new JSONObject(responseMap);
        }

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
    @RequestMapping(value = "/pairYubiKey", method = RequestMethod.POST)
    public JSONObject pairYubiKey(@RequestHeader("accept-language") HashMap<String, String> header, @RequestBody HashMap<String, String> mapper) throws IOException, ServletException {
        String otp = mapper.get ( "otp" );
        String username = mapper.get ( "username" );
        //permission check
        if(checkPermission (mapper.get("username"), jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",401);
            responseMap.put("description", "Unauthorized");
            return new JSONObject(responseMap);
        }

        Operation operation = new Operation ( pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser ( username );
        JSONObject response = pingIdOperationService.pairYubiKey ( otp, operation );

        return response;

    }
    @RequestMapping(value = "/getPairingStatus", method = RequestMethod.POST)
    public JSONObject getPairingStatus(@RequestHeader("accept-language") HashMap<String, String> header, @RequestBody HashMap<String, String> mapper) {
        String activationCode = mapper.get("activationCode");
        String username = mapper.get("username");
        //permission check
        if(checkPermission (mapper.get("username"), jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",401);
            responseMap.put("description", "Unauthorized");
            return new JSONObject(responseMap);
        }

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser(username);
        JSONObject response = pingIdOperationService.getPairingStatus(activationCode, operation);
        return response;
    }

    @RequestMapping(value = "/authenticateOnline", method = RequestMethod.POST)
    public JSONObject authenticateOnline(@RequestHeader("accept-language") HashMap<String, String> header, @RequestBody HashMap<String, String> mapper) {
        String deviceId = mapper.get("deviceId");
        String username = mapper.get("username");
        //permission check
        if(checkPermission (mapper.get("username"), jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",401);
            responseMap.put("description", "Unauthorized");
            return new JSONObject(responseMap);
        }

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser(username);
        JSONObject response = pingIdOperationService.authenticateOnline(new Application("MFA"), "OTP", deviceId, operation);

        return response;
    }

    @RequestMapping(value = "/backupAuthentication", method = RequestMethod.POST)
    public JSONObject backupAuthentication(@RequestHeader("accept-language") HashMap<String, String> header, @RequestBody HashMap<String, String> mapper) {
        LOG.info("backupAuthentication endpoint entry");

        String authType = "ONE_TIME_DEVICE";
        String spAlias = "rescuecode";
        String username = mapper.get("username");
        String deviceType = mapper.get("deviceType");
        String deviceData;


        RequestData requestData = new RequestData ();
        requestData.setUsername ( username );
        requestData.setAuthType ( authType );
        requestData.setSpAlias ( spAlias );
        requestData.setDeviceType ( deviceType );
        requestData.setName ( "MFA" );

        //permission check
        if(checkPermission (mapper.get("username"), jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",401);
            responseMap.put("description", "Unauthorized");
            return new JSONObject(responseMap);
        }


        if( deviceType.equals ( "Email" )){
            deviceData = mapper.get ( "mail" );
        }else if(deviceType.equals ( "SMS" ) || deviceType.equals ( "Voice" )){
            deviceData = mapper.get ( "deviceData" );
        }else {
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",400);
            responseMap.put("description", "Invalid deviceType");
            return new JSONObject(responseMap);
        }
        requestData.setDeviceData ( deviceData );


        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser(username);
        JSONObject response = pingIdOperationService.backupOnline(requestData, authType, operation);

        return response;
    }
    @RequestMapping(value = "/authenticationOffline", method = RequestMethod.POST)
    public JSONObject authenticationOffline(@RequestHeader("accept-language") HashMap<String, String> header, @RequestBody HashMap<String, String> mapper){
        String otp = mapper.get("otp");
        String sessionId = mapper.get("sessionId");
        String username = mapper.get("username");
        String spAlias = mapper.get ( "spAlias" );
        //permission check
        if(checkPermission (mapper.get("username"), jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",401);
            responseMap.put("description", "Unauthorized");
            return new JSONObject(responseMap);
        }

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser ( username );
        JSONObject response = pingIdOperationService.authenticationOffline ( sessionId, otp, operation, spAlias);

        return response;

    }


    @RequestMapping(value = "/makeDevicePrimary", method = RequestMethod.POST)
    public JSONObject makeDevicePrimary(@RequestHeader("accept-language") HashMap<String, String> header, @RequestBody HashMap<String, String> mapper) {
        String deviceId = mapper.get("deviceId");
        String username = mapper.get("username");
        //permission check
        if(checkPermission (mapper.get("username"), jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",401);
            responseMap.put("description", "Unauthorized");
            return new JSONObject(responseMap);
        }

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
    public JSONObject ToggleUserBypass(@RequestHeader("accept-language") HashMap<String, String> header, @RequestBody HashMap<String, String> mapper){
        //permission check
        LOG.info ( "Class ToggleUserBypass Endpoint Entry" );

        String username = mapper.get("username");

        if(checkPermissionAdmin (mapper.get("username"), jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",401);
            responseMap.put("description", "Unauthorized");
            return new JSONObject(responseMap);
        }


        LOG.info ( "Permission Check Exit" );

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser ( username );

        JSONObject response = pingIdOperationService.ToggleUserBypass ( operation );
        return response;
    }

    @RequestMapping(value = "/SuspendUser", method = RequestMethod.POST)
    public JSONObject SuspendUser(@RequestHeader("accept-language") HashMap<String, String> header, @RequestBody HashMap<String, String> mapper){
        //permission check

        String username = mapper.get("username");

        if(checkPermissionAdmin (mapper.get("username"), jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",401);
            responseMap.put("description", "Unauthorized");
            return new JSONObject(responseMap);
        }

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser ( username );

        JSONObject response = pingIdOperationService.SuspendUser ( operation );
        return response;
    }

    @RequestMapping(value = "/ActivateUser", method = RequestMethod.POST)
    public JSONObject ActivateUser(@RequestHeader("accept-language") HashMap<String, String> header, @RequestBody HashMap<String, String> mapper){
        //permission check

        String username = mapper.get("username");

        if(checkPermissionAdmin (mapper.get("username"), jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",401);
            responseMap.put("description", "Unauthorized");
            return new JSONObject(responseMap);
        }

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser ( username );

        JSONObject response = pingIdOperationService.ActivateUser ( operation );
        return response;
    }

    protected boolean checkPermission(String username, String usernameFromToken, String role){
        if((!username.equals(usernameFromToken)) && !(role.equals("admin") || role.equals("helpdesk"))){
            return true;
        }else return false;
    }

    protected boolean checkPermissionAdmin(String username, String usernameFromToken, String role){
        LOG.info ( "checkPermissionAdmin Entry" );
        LOG.info ( "role: "+ role  );
        if(!(role.equals("admin") || role.equals("helpdesk"))){
            return true;
        }else return false;
    }

    protected boolean checkPermissionHelpDesk(String username, String usernameFromToken, String role){
        if(!role.equals("helpdesk")){
            return true;
        }else return false;
    }



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