package com.ssportal.be.service;

import com.ssportal.be.pingid.model.MailForm;

import javax.validation.constraints.Email;

public interface MailService {

    void sendEmail(MailForm mail);
    MailForm buildForm();
    MailForm buildForm(String toEmail, String subject);
}
