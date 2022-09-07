package com.ssportal.be.service;

import com.ssportal.be.ldaps.LdapUser;
import com.ssportal.be.model.User;
import com.ssportal.be.pingid.model.MailForm;

import javax.validation.constraints.Email;
import java.util.Map;

public interface MailService {

    public void sendEmail(MailForm mail);
    public MailForm buildForm();
    public MailForm buildFormForSelfRegister(User user, LdapUser manager);
    public MailForm buildFormForSelfUnregister(User user, LdapUser manager);
    public MailForm buildFormForRegister(LdapUser adminUser, LdapUser manager, User user);
    public MailForm buildFormForUnregister(LdapUser adminUser,  LdapUser manager, User user);
    public MailForm buildFormForBypass(LdapUser adminUser,  LdapUser manager, User user);
    public MailForm buildFormForEnableUser(LdapUser adminUser,  LdapUser manager, User user);
    public MailForm buildFormForDisableUser(LdapUser adminUser,  LdapUser manager, User user);
    public String getContentFromTemplate(Map<String, Object> model, String temlpateName);

}
