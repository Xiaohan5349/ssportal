package com.ssportal.be.pingid.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PingIdUser {

    private String userName;
    private String fName;
    private String lName;
    private String email;
    private PingIdUserRole role;
    private JSONArray devicesDetails;

    private String phoneNumber;
    private DeviceDetail deviceDetail;

    private Date lastAuthentication;
    private Boolean enabled;
    private PingIdUserStatus status;

    private Map<String, Date> bypassInfo;

    public PingIdUser() {

        this.userName = null;
        this.fName = null;
        this.lName = null;
        this.email = null;
        this.phoneNumber = null;
        this.devicesDetails = null;

        this.role = PingIdUserRole.REGULAR;
        this.bypassInfo = new HashMap<String, Date>();
    }

    public PingIdUser(String userName) {

        this.userName = userName;
        this.fName = null;
        this.lName = null;
        this.email = null;
        this.phoneNumber = null;
        this.devicesDetails = null;

        this.role = PingIdUserRole.REGULAR;
        this.bypassInfo = new HashMap<String, Date>();
    }

    public PingIdUser(JSONObject userDetailsJSON) {

        if (userDetailsJSON != null) {

            this.email = (String) userDetailsJSON.get("email");
            this.fName = (String) userDetailsJSON.get("fname");
            this.lName = (String) userDetailsJSON.get("lname");
            this.userName = (String) userDetailsJSON.get("userName");
            this.enabled = (Boolean) userDetailsJSON.get("userEnabled");
            this.lastAuthentication = parseDateFromEpoch((Long) userDetailsJSON.get("lastLogin"));
            this.role = PingIdUserRole.REGULAR;
            this.status = PingIdUserStatus.valueOf((String) userDetailsJSON.get("status"));
            this.devicesDetails = (JSONArray) userDetailsJSON.get("devicesDetails");

            this.bypassInfo = new HashMap<String, Date>();
            JSONArray spList = (JSONArray) userDetailsJSON.get("spList");
            for (Object spObject : spList) {
                JSONObject sp = (JSONObject) spObject;
                String spAlias = (String) sp.get("spAlias");
                if (sp.get("bypassExpiration") != null) {
                    Date bypassExpiration = parseDateFromEpoch((long) sp.get("bypassExpiration"));
                    if (bypassExpiration.after(new Date())) {
                        this.bypassInfo.put(spAlias, bypassExpiration);
                    }
                } else {
                    this.bypassInfo.remove(spAlias);
                }
            }

        }
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PingIdUserRole getRole() {
        return role;
    }

    public void setRole(PingIdUserRole role) {
        this.role = role;
    }

    public JSONArray getDevicesDetails() {
        return devicesDetails;
    }

    public void setDevicesDetails(JSONArray devicesDetails) {
        this.devicesDetails = devicesDetails;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public DeviceDetail getDeviceDetail() {
        return deviceDetail;
    }

    public void setDeviceDetail(DeviceDetail deviceDetail) {
        this.deviceDetail = deviceDetail;
    }

    public Date getLastAuthentication() {
        return lastAuthentication;
    }

    public void setLastAuthentication(Date lastAuthentication) {
        this.lastAuthentication = lastAuthentication;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public PingIdUserStatus getStatus() {
        return status;
    }

    public void setStatus(PingIdUserStatus status) {
        this.status = status;
    }

    public Map<String, Date> getBypassInfo() {
        return bypassInfo;
    }

    public void setBypassInfo(Map<String, Date> bypassInfo) {
        this.bypassInfo = bypassInfo;
    }

    private Date parseDateFromEpoch(Long unixEpoch) {

        Date date = new Date();

        if (unixEpoch != null) {
            date.setTime(unixEpoch);
        } else {
            date.setTime(0);
        }

        return date;
    }
}
