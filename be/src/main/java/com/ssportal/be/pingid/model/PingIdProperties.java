package com.ssportal.be.pingid.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PingIdProperties {

    private String pingid_org_alias;
    private String pingid_token;
    private String pingid_use_base64_key;
    private String base_url;
    private String login_url;
    private String app_url;
    private String logout_path;
    private String refid_user;
    private String refid_pwd;
    private String instance_id;
    private int max_devices_count;
    private String api_url;
    private String clientIP;
    private boolean desktopEnabled;
    private boolean mfaBypassEnabled;
    private boolean activateUserEnabled;
    private long bypassDuration;
    private String webAuthnRegUrl;
    private String webAuthnAuthUrl;

    public PingIdProperties() {
        pingid_org_alias = "";
        pingid_token = "";
        pingid_use_base64_key = "";
        base_url = "";
        login_url = "";
        api_url = "";
        app_url = "";
        logout_path = "";
        refid_user = "";
        refid_pwd = "";
        instance_id = "";
        max_devices_count = 5;
        clientIP = "";
        desktopEnabled = false;
        mfaBypassEnabled = false;
        activateUserEnabled = false;
        bypassDuration = 0;
        webAuthnRegUrl = "";
        webAuthnAuthUrl = "";
    }

    public void setProperties(int i) throws IOException {
        java.util.Properties prop = new java.util.Properties();
        String propFileName = "pingid.properties";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            pingid_org_alias = prop.getProperty("org_alias");
            pingid_token = prop.getProperty("token");
            pingid_use_base64_key = prop.getProperty("use_base64_key");
            api_url = prop.getProperty("admin_url");
        }
        propFileName = "application.properties";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            base_url = prop.getProperty("base_url");
            login_url = prop.getProperty("login_url");
            app_url = prop.getProperty("application_url");
            logout_path = prop.getProperty("logout_path");
            refid_user = prop.getProperty("refid_user");
            refid_pwd = prop.getProperty("refid_pwd");
            instance_id = prop.getProperty("instance_id");
            if (prop.containsKey("max_devices_count")) {
                max_devices_count = Integer.parseInt(prop.getProperty("max_devices_count"));
            }
            if (prop.containsKey("client_ip")) {
                clientIP = prop.getProperty("client_ip");
            }
            if (prop.containsKey("desktop_enabled")) {
                desktopEnabled = Boolean.valueOf(prop.getProperty("desktop_enabled"));
            }
            if (prop.containsKey("mfa_bypass_enabled")) {
                mfaBypassEnabled = Boolean.valueOf(prop.getProperty("mfa_bypass_enabled"));
            }
            if (prop.containsKey("bypass_duration")) {
                bypassDuration = Integer.parseInt(prop.getProperty("bypass_duration"));
            }
            if (prop.containsKey("webauthn_reg_url")) {
                webAuthnRegUrl = prop.getProperty("webauthn_reg_url");
            }
            if (prop.containsKey("webauthn_auth_url")) {
                webAuthnAuthUrl = prop.getProperty("webauthn_auth_url");
            }
            if (prop.containsKey("activate_user_enabled")) {
                activateUserEnabled = Boolean.valueOf(prop.getProperty("activate_user_enabled"));
            }
        }
    }

    public String getOrgAlias() {
        return pingid_org_alias;
    }

    public String getToken() {
        return pingid_token;
    }

    public String getBase64Key() {
        return pingid_use_base64_key;
    }

    public String getLoginUrl() {
        return login_url;
    }

    public String getApiUrl() {
        return api_url;
    }

    public String getAppUrl() {
        return app_url;
    }

    public String getLogoutPath() {
        return logout_path;
    }

    public String getBaseUrl() {
        return base_url;
    }

    public String getRefidUser() {
        return refid_user;
    }

    public String getRefidPwd() {
        return refid_pwd;
    }

    public String getInstanceid() {
        return instance_id;
    }

    public int getMaxDevicesCount() {
        return max_devices_count;
    }

    public String getClientIP() {
        return clientIP;
    }

    public boolean isDesktopEnabled() {
        return desktopEnabled;
    }

    public boolean isMfaBypassEnabled() {
        return mfaBypassEnabled;
    }

    public long getBypassDuration() {
        return bypassDuration;
    }

    public String getWebAuthnRegUrl() {
        return webAuthnRegUrl;
    }

    public String getWebAuthnAuthUrl() {
        return webAuthnAuthUrl;
    }

    public boolean isActivateUserEnabled() {
        return activateUserEnabled;
    }

    public String getPingid_org_alias() {
        return pingid_org_alias;
    }

    public void setPingid_org_alias(String pingid_org_alias) {
        this.pingid_org_alias = pingid_org_alias;
    }

    public String getPingid_token() {
        return pingid_token;
    }

    public void setPingid_token(String pingid_token) {
        this.pingid_token = pingid_token;
    }

    public String getPingid_use_base64_key() {
        return pingid_use_base64_key;
    }

    public void setPingid_use_base64_key(String pingid_use_base64_key) {
        this.pingid_use_base64_key = pingid_use_base64_key;
    }

    public String getBase_url() {
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public String getLogin_url() {
        return login_url;
    }

    public void setLogin_url(String login_url) {
        this.login_url = login_url;
    }

    public String getApp_url() {
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

    public String getLogout_path() {
        return logout_path;
    }

    public void setLogout_path(String logout_path) {
        this.logout_path = logout_path;
    }

    public String getRefid_user() {
        return refid_user;
    }

    public void setRefid_user(String refid_user) {
        this.refid_user = refid_user;
    }

    public String getRefid_pwd() {
        return refid_pwd;
    }

    public void setRefid_pwd(String refid_pwd) {
        this.refid_pwd = refid_pwd;
    }

    public String getInstance_id() {
        return instance_id;
    }

    public void setInstance_id(String instance_id) {
        this.instance_id = instance_id;
    }

    public int getMax_devices_count() {
        return max_devices_count;
    }

    public void setMax_devices_count(int max_devices_count) {
        this.max_devices_count = max_devices_count;
    }

    public String getApi_url() {
        return api_url;
    }

    public void setApi_url(String api_url) {
        this.api_url = api_url;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public void setDesktopEnabled(boolean desktopEnabled) {
        this.desktopEnabled = desktopEnabled;
    }

    public void setMfaBypassEnabled(boolean mfaBypassEnabled) {
        this.mfaBypassEnabled = mfaBypassEnabled;
    }

    public void setActivateUserEnabled(boolean activateUserEnabled) {
        this.activateUserEnabled = activateUserEnabled;
    }

    public void setBypassDuration(long bypassDuration) {
        this.bypassDuration = bypassDuration;
    }

    public void setWebAuthnRegUrl(String webAuthnRegUrl) {
        this.webAuthnRegUrl = webAuthnRegUrl;
    }

    public void setWebAuthnAuthUrl(String webAuthnAuthUrl) {
        this.webAuthnAuthUrl = webAuthnAuthUrl;
    }
}
