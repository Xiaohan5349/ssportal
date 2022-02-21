package com.ssportal.be.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssportal.be.model.security.Authority;
import com.ssportal.be.model.security.UserRole;
import com.ssportal.be.pingid.model.Operation;
import com.ssportal.be.pingid.model.PingIdProperties;
import com.ssportal.be.pingid.service.PingIdOperationService;
import com.ssportal.be.pingid.service.impl.PingIdOperationServiceImpl;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;



public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = -9138461153733765604L;
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String hardToken;
    private String softToken;
    private String desktopToken;

    private String otpToken;


    private PingIdOperationService pingIdOperationService;
    private PingIdProperties pingIdProperties;


    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date joinDate;
    private String password;

    @JsonIgnore
    private String userRoles;

    private String company;
    private String phone;
    private String fax;
    private String username;
    private String deviceId;


    private String admin;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date birthday;

    public User(){}

    public User(String Username) throws IOException {

        PingIdProperties pingidprops = new PingIdProperties();
        pingidprops.setProperties ( 0 );
        pingIdOperationService = new PingIdOperationServiceImpl ();
        Operation operation = new Operation(pingidprops.getOrgAlias(), pingidprops.getPingid_token(), pingidprops.getPingid_use_base64_key(), pingidprops.getApi_url());
        operation.setTargetUser(Username);
        JSONObject userDetails = pingIdOperationService.getUserDetails(operation);

        this.firstName = userDetails.get ( "fname" ).toString ();
        this.lastName = userDetails.get ( "lname" ).toString ();
        this.email = userDetails.get ( "email" ).toString ();


    }
    public User(JSONObject userAttributes){
        this.username = userAttributes.get ( "subject" ).toString ();
        this.email = userAttributes.get ( "mail" ).toString ();
        this.admin = userAttributes.get ( "admin" ).toString ();
        this.firstName = userAttributes.get ( "firstName" ).toString ();
        this.lastName = userAttributes.get ( "lastName" ).toString ();
        this.hardToken = userAttributes.get ( "hardToken" ).toString ();
        this.softToken = userAttributes.get ( "softToken" ).toString ();
        this.desktopToken = userAttributes.get ( "desktopToken" ).toString ();
        this.otpToken = userAttributes.get("otpToken").toString ();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getUserRoles() {
        if(this.admin.equals ( "true" )){
            return "ADMIN";
        }else {
            return "REGULAR";
        }
    }

    public void setUserRoles(String userRoles) {
        this.userRoles = userRoles;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getHardToken() {
        return hardToken;
    }

    public void setHardToken(String hardToken) {
        this.hardToken = hardToken;
    }

    public String getSoftToken() {
        return softToken;
    }

    public void setSoftToken(String softToken) {
        this.softToken = softToken;
    }

    public String getDesktopToken() {
        return desktopToken;
    }

    public void setDesktopToken(String desktopToken) {
        this.desktopToken = desktopToken;
    }


    public String getOtpToken() { return otpToken; }

    public void setOtpToken(String otpToken) { this.otpToken = otpToken; }


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
