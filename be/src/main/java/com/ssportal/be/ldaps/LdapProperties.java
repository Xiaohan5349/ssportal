package com.ssportal.be.ldaps;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class LdapProperties {

    private String ldapId;
    private String searchBaseDN;
    private String searchFilter;
    private String firstName;
    private String lastName;
    private String mail;
    private String memberOf;
    private String serviceDeskFilter;
    private String mfaGroupDN;
    private String authenticationMethod;
    private boolean useSSL;
    private String principal;
    private String serverUrl;
    private String credentials;
    private String softTokenGroup;
    private String hardTokenGroup;
    private String desktopTokenGroup;
    private String otpTokenGroup;
    private String adminGroup;

    public String getLdapId() {
        return ldapId;
    }

    public String getSearchBaseDN() {
        return searchBaseDN;
    }

    public String getSearchFilter() {
        return searchFilter;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMail() {
        return mail;
    }

    public String getMemberOf() {
        return memberOf;
    }

    public String getServiceDeskFilter() {
        return serviceDeskFilter;
    }

    public String getMfaGroupDN() {
        return mfaGroupDN;
    }

    public String getAuthenticationMethod() {
        return authenticationMethod;
    }

    public boolean getUseSSL() {
        return useSSL;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getCredentials() {
        return credentials;
    }

    public String getSoftTokenGroup() {
        return softTokenGroup;
    }

    public String getHardTokenGroup() {
        return hardTokenGroup;
    }

    public String getDesktopTokenGroup() {
        return desktopTokenGroup;
    }

    public String getOtpTokenGroup() { return otpTokenGroup; }

    public void setOtpTokenGroup(String otpTokenGroup) { this.otpTokenGroup = otpTokenGroup; }

    public String getAdminGroup() {
        return adminGroup;
    }

    public void setAdminGroup(String adminGroup) {
        this.adminGroup = adminGroup;
    }

    public void loadProperties() throws IOException {
        Properties prop = new Properties();
        String propFileName = "ldap.properties";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            if (inputStream != null) {
                prop.load(inputStream);
                inputStream.close();
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            ldapId = prop.getProperty("ldapId");
            searchBaseDN = prop.getProperty("searchBaseDN");
            searchFilter = prop.getProperty("searchFilter");
            firstName = prop.getProperty("firstName");
            lastName = prop.getProperty("lastName");
            mail = prop.getProperty("mail");
            memberOf = prop.getProperty("memberOf");
            serviceDeskFilter = prop.getProperty("serviceDeskFilter");
            mfaGroupDN = prop.getProperty("mfaGroupDN");
            authenticationMethod = prop.getProperty("authenticationMethod");
            useSSL = Boolean.valueOf(prop.getProperty("useSSL"));
            principal = prop.getProperty("principal");
            serverUrl = prop.getProperty("serverUrl");
            credentials = prop.getProperty("credentials");
            hardTokenGroup = prop.getProperty("hardTokenGroup");
            softTokenGroup = prop.getProperty("softTokenGroup");
            desktopTokenGroup = prop.getProperty("desktopTokenGroup");
            otpTokenGroup = prop.getProperty ( "otpTokenGroup" );
            adminGroup = prop.getProperty ( "adminGroup" );
        }
    }

}
