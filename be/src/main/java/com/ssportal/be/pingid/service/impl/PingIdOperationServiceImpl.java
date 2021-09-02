package com.ssportal.be.pingid.service.impl;

import com.ssportal.be.pingid.model.Operation;
import org.apache.commons.io.IOUtils;
import org.jose4j.base64url.Base64;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PingIdOperationServiceImpl {
    // public methods
    public JSONObject AddUser(Boolean activateUser) {
        Operation opera = new Operation();

        opera.setName("AddUser");
        opera.setEndpoint(opera.getApiUrl() + "/rest/4/adduser/do");

        JSONObject reqBody = new JSONObject();
        reqBody.put("activateUser", activateUser);
        if (opera.getPingIdUser().getEmail() != null) {
            reqBody.put("email", opera.getPingIdUser().getEmail());
        }
        if (opera.getPingIdUser().getfName() != null) {
            reqBody.put("fName", opera.getPingIdUser().getfName());
        }
        if (opera.getPingIdUser().getlName() != null) {
            reqBody.put("lname", opera.getPingIdUser().getlName());
        }
        reqBody.put("username", opera.getPingIdUser().getUserName());
        reqBody.put("role", opera.getPingIdUser().getRole().getValue());
        reqBody.put("clientData", opera.getClientData());

        opera.setRequestToken(opera.(reqBody));

        sendRequest();
        JSONObject response = parseResponse();
        values.clear();
        if (activateUser) {
            this.lastActivationCode = (String) response.get("activationCode");
        }
        return response;
    }


}
