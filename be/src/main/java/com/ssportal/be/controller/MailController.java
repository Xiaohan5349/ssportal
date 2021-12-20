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
        MailForm mailForm = new MailForm ();
        String to = "lixiaohan5349@gmail.com";
        String subject = "test template";
        HashMap<String, Object> model = new HashMap<> (  );
        model.put ( "firstName", "Xiaohan" );
        model.put ( "lastName", "Li" );


        mailForm.setTo ( to );
        mailForm.setSubject ( subject );
        mailForm.setModel ( model );

        mailService.sendEmail ( mailForm );



        return "test";

    }




}
