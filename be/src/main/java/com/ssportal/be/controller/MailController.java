package com.ssportal.be.controller;

import com.ssportal.be.ldaps.LdapOperation;
import com.ssportal.be.ldaps.LdapOperationWithoutPing;
import com.ssportal.be.ldaps.LdapUser;
import com.ssportal.be.model.User;
import com.ssportal.be.pingid.model.MailForm;
import com.ssportal.be.pingid.service.PingIdOperationService;
import com.ssportal.be.service.MailService;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;

@RestController
public class MailController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    MailService mailService;


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public JSONObject testLdap(@RequestParam(name = "user") String userName) throws IOException, GeneralSecurityException, NamingException {


        LdapOperationWithoutPing ldapOperation = new LdapOperationWithoutPing ();
        LdapUser user = ldapOperation.searchUser ( userName );
        JSONObject jsonUser = new JSONObject (  );
        jsonUser.put ( "firstName", user.getFirstName () );
        jsonUser.put ( "lastName", user.getLastName ());
        jsonUser.put ( "mail", user.getEmailAddress () );
        jsonUser.put ( "username", userName );
        jsonUser.put ( "hardToken", user.isHardTokenUser ());
        jsonUser.put ( "desktopToken", user.isDesktopTokenUser ());
        jsonUser.put ( "softToken", user.isSoftTokenUser ());
        jsonUser.put ( "otpToken", user.isOtpTokenUser ());

        return jsonUser;
    }
        @RequestMapping(value = "/mail", method = RequestMethod.GET)
    public String sendMail(@RequestParam(name = "task") String task, @RequestParam(name = "user") String userName, @RequestParam(name = "admin") String adminUsername) throws IOException {

        User user = new User(userName);
        User adminUser = new User(adminUsername);
        MailForm mailForm;
        switch(task) {
            case "pairdevice":
                mailForm = mailService.buildFormForRegister ( adminUser, user );
                break;
            case "unpairdevice":
                mailForm = mailService.buildFormForUnregister ( adminUser, user );
                break;
            case "bypass":
                mailForm = mailService.buildFormForBypass ( adminUser, user );
                break;
            case "enable":
                mailForm = mailService.buildFormForEnableUser ( adminUser, user );
                break;
            case "disable":
                mailForm = mailService.buildFormForDisableUser ( adminUser, user );
                break;
            default:
                throw new IllegalArgumentException ( "Invalid task " + task );
        }

        mailService.sendEmail ( mailForm );

        return "test";

    }

    @RequestMapping(value = "/mail/self", method = RequestMethod.GET)
    public String sendSelfMail(@RequestParam(name = "task") String task, @RequestParam(name = "user") String userName) throws IOException {

        User user = new User(userName);
        MailForm mailForm;
        switch(task) {
            case "pairdeviceself":
                mailForm = mailService.buildFormForSelfRegister ( user );
                break;
            case "unpairdeviceself":
                mailForm = mailService.buildFormForSelfUnregister ( user );
                break;
            default:
                throw new IllegalArgumentException ( "Invalid task " + task );
        }

        mailService.sendEmail ( mailForm );

        return "test";

    }


}
