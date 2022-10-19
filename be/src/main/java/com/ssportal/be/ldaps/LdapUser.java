package com.ssportal.be.ldaps;

public class LdapUser {

    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String role;
    private boolean softTokenUser;
    private boolean hardTokenUser;
    private boolean desktopTokenUser;
    private boolean otpTokenUser;
    private String manager;
    private boolean SMSTokenUser;

    public LdapUser(){
        this.role = "REGULAR";
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isSoftTokenUser() {
        return softTokenUser;
    }

    public void setSoftTokenUser(boolean softTokenUser) {
        this.softTokenUser = softTokenUser;
    }

    public boolean isHardTokenUser() {
        return hardTokenUser;
    }

    public void setHardTokenUser(boolean hardTokenUser) {
        this.hardTokenUser = hardTokenUser;
    }

    public boolean isDesktopTokenUser() {
        return desktopTokenUser;
    }

    public boolean isSMSTokenUser() {
        return SMSTokenUser;
    }

    public void setSMSTokenUser(boolean SMSTokenUser){
        this.SMSTokenUser = SMSTokenUser;
    }

    public void setDesktopTokenUser(boolean desktopTokenUser) {
        this.desktopTokenUser = desktopTokenUser;
    }

    public boolean isOtpTokenUser() { return otpTokenUser; }

    public void setOtpTokenUser(boolean otpTokenUser) {this.otpTokenUser = otpTokenUser; }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "LdapUser{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", role='" + role + '\'' +
                ", softTokenUser=" + softTokenUser +
                ", hardTokenUser=" + hardTokenUser +
                ", desktopTokenUser=" + desktopTokenUser +
                ", otpTokenUser=" + otpTokenUser +
                ", manager='" + manager + '\'' +
                ", SMSTokenUser=" + SMSTokenUser +
                '}';
    }
}
