package com.ssportal.be.service;

import com.ssportal.be.model.User;
import com.ssportal.be.pingid.model.MailForm;

import javax.validation.constraints.Email;
import java.util.Map;

public interface MailService {

    public void sendEmail(MailForm mail);
    public MailForm buildForm();
    public MailForm buildFormForSelfRegister(User user, User manager);
    public MailForm buildFormForSelfUnregister(User user, User manager);
    public MailForm buildFormForRegister(User adminUser,  User manager, User user);
    public MailForm buildFormForUnregister(User adminUser,  User manager, User user);
    public MailForm buildFormForBypass(User adminUser,  User manager, User user);
    public MailForm buildFormForEnableUser(User adminUser,  User manager, User user);
    public MailForm buildFormForDisableUser(User adminUser,  User manager, User user);
    public String getContentFromTemplate(Map<String, Object> model, String temlpateName);

}
