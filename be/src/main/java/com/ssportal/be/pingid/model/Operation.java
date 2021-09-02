package com.ssportal.be.pingid.model;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Operation {

    private String name;
    private String endpoint;
    private String requestToken;
    private String responseToken;
    private int responseCode;
    private Boolean wasSuccessful;
    private long errorId;
    private String errorMsg;
    private String uniqueMsgId;
    private String orgAlias;
    private String token;
    private String useBase64Key;
    private String apiUrl;
    private String lastActivationCode;
    private String lastSessionId;
    private Map<String, Object> values;

    private String clientData;
    private PingIdUser pingIdUser;

    private final String apiVersion = "4.9";
    private static final Logger LOG = Logger.getLogger(Operation.class);

    public Operation() {
    }

    public Operation(String orgAlias, String token, String useBase64Key, String apiUrl) {
        this.orgAlias = orgAlias;
        this.token = token;
        this.useBase64Key = useBase64Key;
        this.apiUrl = apiUrl;
        this.values = new HashMap<String, Object>();
    }

    public String getName() {
        return name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public String getResponseToken() {
        return responseToken;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public Boolean getWasSuccessful() {
        return wasSuccessful;
    }

    public String getLastActivationCode() {
        return this.lastActivationCode;
    }

    public String getLastSessionId() {
        return this.lastSessionId;
    }

    public long getErrorId() {
        return errorId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getUniqueMsgId() {
        return uniqueMsgId;
    }

    public Map<String, Object> getReturnValues() {
        return values;
    }

    public PingIdUser getPingIdUser() {
        return pingIdUser;
    }

    public void setTargetUser(PingIdUser pingIdUser) {
        this.pingIdUser = pingIdUser;
    }

    public void setTargetUser(String username) {
        this.pingIdUser = new PingIdUser(username);
    }

    public void setLastActivationCode(String activationCode) {
        this.lastActivationCode = activationCode;
    }

    public void setLastSessionId(String sessionId) {
        this.lastSessionId = sessionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public void setResponseToken(String responseToken) {
        this.responseToken = responseToken;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setWasSuccessful(Boolean wasSuccessful) {
        this.wasSuccessful = wasSuccessful;
    }

    public void setErrorId(long errorId) {
        this.errorId = errorId;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setUniqueMsgId(String uniqueMsgId) {
        this.uniqueMsgId = uniqueMsgId;
    }

    public String getOrgAlias() {
        return orgAlias;
    }

    public void setOrgAlias(String orgAlias) {
        this.orgAlias = orgAlias;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUseBase64Key() {
        return useBase64Key;
    }

    public void setUseBase64Key(String useBase64Key) {
        this.useBase64Key = useBase64Key;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public String getClientData() {
        return clientData;
    }

    public void setClientData(String clientData) {
        this.clientData = clientData;
    }

    public void setPingIdUser(PingIdUser pingIdUser) {
        this.pingIdUser = pingIdUser;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public static Logger getLOG() {
        return LOG;
    }
}
