package com.ssportal.be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {
    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping(value = "/mail", method = RequestMethod.GET)
    public String sendMail(){
       String from = "testSender@gmail.com";
       String to = "lixiaohan5349@gmail.com";
       String cc = "lixiaohan5349@outlook.com";

        SimpleMailMessage message = new SimpleMailMessage (  );

        message.setFrom ( from );
        message.setCc ( cc );
        message.setTo ( to );
        message.setSubject ( "just a test" );
        message.setText ( "hello, team! this is just a test email" );

        mailSender.send ( message );

        return "test";

    }




}
