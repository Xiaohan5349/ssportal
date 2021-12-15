package com.ssportal.be.pingid.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DeviceDetail {
    
    private final String appVersion;
    private final long availableClaimedSms;
    private final long availableNotClaimedSms;
    private final String countryCode;
    private final String email;
    private final String displayID;
    private final Date enrollment;
    private final Boolean hasWatch;
    private final String osVersion;
    private final String phoneNumber;
    private final Boolean pushEnabled;
    private final String type;
    private final String nickname;
    private static final Logger LOG = LogManager.getLogger(DeviceDetail.class);

    public DeviceDetail() {
        
        appVersion = "";
        displayID = "";
        availableClaimedSms = 0;
        availableNotClaimedSms = 0;
        countryCode = "";
        email = "";
        enrollment = new Date();
        hasWatch = false;
        osVersion = "";
        phoneNumber = "";
        pushEnabled = false;
        type = "";
        nickname = "";
    }
    
    public DeviceDetail(JSONObject deviceDetailsJSON) {
        
        if (deviceDetailsJSON != null) {
            appVersion = (deviceDetailsJSON.get("appVersion") != null ? (String) deviceDetailsJSON.get("appVersion") : "");
            availableClaimedSms = (deviceDetailsJSON.get("availableClaimedSms") != null ? (long) deviceDetailsJSON.get("availableClaimedSms") : 0);
            availableNotClaimedSms = (deviceDetailsJSON.get("availableNotClaimedSms") != null ? (long) deviceDetailsJSON.get("availableNotClaimedSms") : 0);
            countryCode = (String) deviceDetailsJSON.get("countryCode");
            email = (String) deviceDetailsJSON.get("email");
            enrollment = parseDate((String) deviceDetailsJSON.get("enrollment"));
            hasWatch = (Boolean) deviceDetailsJSON.get("hasWatch");
            osVersion = (String) deviceDetailsJSON.get("osVersion");
            phoneNumber = (String) deviceDetailsJSON.get("phoneNumber");
            pushEnabled = (Boolean) deviceDetailsJSON.get("pushEnabled");
            type = (String) deviceDetailsJSON.get("type");
            nickname = (String) deviceDetailsJSON.get("nickname");
            displayID = (deviceDetailsJSON.get("displayID") != null ? (String) deviceDetailsJSON.get("displayID") : "");
        } else {
            appVersion = "";
            availableClaimedSms = 0;
            availableNotClaimedSms = 0;
            countryCode = "";
            email = "";
            displayID = "";
            enrollment = new Date();
            hasWatch = false;
            osVersion = "";
            phoneNumber = "";
            pushEnabled = false;
            type = "";
            nickname = "";
        }
    }
    
    public String getAppVersion() {
        return appVersion;
    }
    
    public long getAvailableClaimedSms() {
        return availableClaimedSms;
    }
    
    public long getAvailableNotClaimedSms() {
        return availableNotClaimedSms;
    }
    
    public String getCountryCode() {
        return countryCode;
    }
    
    public String getEmail() {
        return email;
    }
    
    public Date getEnrollment() {
        return enrollment;
    }
    
    public Boolean getHasWatch() {
        return hasWatch;
    }
    
    public String getOsVersion() {
        return osVersion;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public Boolean getPushEnabled() {
        return pushEnabled;
    }
    
    public String getType() {
        return type;
    }
    
    public String getdisplayID() {
        return displayID;
    }
    
    public String getnickName() {
        return nickname;
    }
    
    private Date parseDate(String dateToParse) {
        
        SimpleDateFormat PingIDDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        PingIDDateFormat.setTimeZone(TimeZone.getTimeZone("America/Denver"));
        
        if (dateToParse != null) {
            try {
                return PingIDDateFormat.parse(dateToParse);
            } catch (ParseException e) {
                LOG.error(e.getMessage());
            }
        }
        
        return null;
    }
    
}
