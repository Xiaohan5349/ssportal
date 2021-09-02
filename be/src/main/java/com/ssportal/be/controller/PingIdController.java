package com.ssportal.be.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ssportal.be.ldaps.LdapOperation;
import com.ssportal.be.pingid.model.Operation;
import com.ssportal.be.pingid.model.PingIdProperties;
import com.ssportal.be.pingid.service.PingIdOperationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springdoc.core.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public JSONObject getUserDetails(Principal principal) throws IOException, ServletException {

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        String username = principal.getName();
        operation.setTargetUser(username);
        JSONObject userDetails = pingIdOperationService.getUserDetails(operation);

        return userDetails;
    }

    @RequestMapping(value = "/unpairDevice", method = RequestMethod.POST)
    public JSONObject unpairDevice(Principal principal, @RequestBody HashMap<String, String> mapper) throws IOException, ServletException {
        String deviceId = (String) mapper.get("deviceId");

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        String username = principal.getName();
        operation.setTargetUser(username);
        JSONObject response = pingIdOperationService.unpairDevice(deviceId, operation);

        return response;
    }

    @RequestMapping(value = "/pairYubiKey", method = RequestMethod.POST)
    public void pairYubiKey(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    @RequestMapping(value = "/authenticateOnline", method = RequestMethod.POST)
    public void authenticateOnline(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    @RequestMapping(value = "/authenticateOffline", method = RequestMethod.POST)
    public void authenticateOffline(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }


    @RequestMapping(value = "/makeDevicePrimary", method = RequestMethod.POST)
    public void makeDevicePrimary(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    @RequestMapping(value = "/updateDeviceNickname", method = RequestMethod.POST)
    public void updateDeviceNickname(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    @RequestMapping(value = "/getActivationCode", method = RequestMethod.POST)
    public void getActivationCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    @RequestMapping(value = "/getPairingStatus", method = RequestMethod.POST)
    public void getPairingStatus(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    @RequestMapping(value = "/webAuthnStartPairing", method = RequestMethod.POST)
    public void webAuthnStartPairing(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    @RequestMapping(value = "/webAuthnStartAuth", method = RequestMethod.POST)
    public void webAuthnStartAuth(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }
}
