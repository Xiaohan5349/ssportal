package com.ssportal.be.controller;

import com.ssportal.be.pingid.model.MailForm;
import com.ssportal.be.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class MailController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    MailService mailService;

    @RequestMapping(value = "/mail", method = RequestMethod.GET)
    public String sendMail(){
        MailForm mailForm = mailService.buildForm ();
        mailService.sendEmail ( mailForm );

        return "test";

    }




}
