package com.ssportal.be.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MailProperties {

    private String subjectPairDeviceSelf;
    private String subjectPairDevice;
    private String subjectUnpairDeviceSelf;
    private String subjectUnpairDevice;
    private String subjectBypass;
    private String subjectTestMFA;
    private String subjectEanbleUser;
    private String subjectDisableUser;

    public String getSubjectPairDeviceSelf() {
        return subjectPairDeviceSelf;
    }

    public void setSubjectPairDeviceSelf(String subjectPairDeviceSelf) {
        this.subjectPairDeviceSelf = subjectPairDeviceSelf;
    }

    public String getSubjectPairDevice() {
        return subjectPairDevice;
    }

    public void setSubjectPairDevice(String subjectPairDevice) {
        this.subjectPairDevice = subjectPairDevice;
    }

    public String getSubjectUnpairDeviceSelf() {
        return subjectUnpairDeviceSelf;
    }

    public void setSubjectUnpairDeviceSelf(String subjectUnpairDeviceSelf) {
        this.subjectUnpairDeviceSelf = subjectUnpairDeviceSelf;
    }

    public String getSubjectUnpairDevice() {
        return subjectUnpairDevice;
    }

    public void setSubjectUnpairDevice(String subjectUnpairDevice) {
        this.subjectUnpairDevice = subjectUnpairDevice;
    }

    public String getSubjectBypass() {
        return subjectBypass;
    }

    public void setSubjectBypass(String subjectBypass) {
        this.subjectBypass = subjectBypass;
    }

    public String getSubjectTestMFA() {
        return subjectTestMFA;
    }

    public void setSubjectTestMFA(String subjectTestMFA) {
        this.subjectTestMFA = subjectTestMFA;
    }

    public String getSubjectEanbleUser() {
        return subjectEanbleUser;
    }

    public void setSubjectEanbleUser(String subjectEanbleUser) {
        this.subjectEanbleUser = subjectEanbleUser;
    }

    public String getSubjectDisableUser() {
        return subjectDisableUser;
    }

    public void setSubjectDisableUser(String subjectDisableUser) {
        this.subjectDisableUser = subjectDisableUser;
    }


    public void setProperties (int i) throws IOException {

        java.util.Properties prop = new java.util.Properties();
        String propFileName = "mail.properties";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            subjectPairDeviceSelf = prop.getProperty("subjectPairDeviceSelf");
            subjectPairDevice = prop.getProperty("subjectPairDevice");
            subjectUnpairDeviceSelf = prop.getProperty("subjectUnpairDeviceSelf");
            subjectUnpairDevice = prop.getProperty("subjectUnpairDevice");
            subjectBypass = prop.getProperty("subjectBypass");
            subjectTestMFA = prop.getProperty("subjectTestMFA");
            subjectEanbleUser = prop.getProperty("subjectEanbleUser");
            subjectDisableUser = prop.getProperty("subjectDisableUser");

        }

    }
}
