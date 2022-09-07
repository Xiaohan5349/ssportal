package com.ssportal.be.controller;

import com.ssportal.be.config.JwtTokenUtil;
import com.ssportal.be.ldaps.LdapOperation;
import com.ssportal.be.ldaps.LdapOperationWithoutPing;
import com.ssportal.be.ldaps.LdapUser;
import com.ssportal.be.model.User;
import com.ssportal.be.pingid.model.MailForm;
import com.ssportal.be.pingid.model.Operation;
import com.ssportal.be.pingid.model.PingIdProperties;
import com.ssportal.be.pingid.service.PingIdOperationService;
import com.ssportal.be.service.MailService;
import com.ssportal.be.utilility.Constants;
import org.apache.commons.lang3.StringUtils;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.naming.NamingException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.util.HashMap;

@RestController
public class MailController {
    private final static Logger log = LoggerFactory.getLogger(MailController.class);
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PingIdOperationService pingIdOperationService;
    @Autowired
    MailService mailService;
    @Autowired
    PingIdController pingIdController;


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public JSONObject testLdap(@RequestHeader("accept-language") HashMap<String, String> header, @RequestParam(name = "user") String userName) throws Exception {
        //permission check

        String authToken  = header.get("authorization").toString ().replace( Constants.TOKEN_PREFIX, "");
        String usernameFromToken = jwtTokenUtil.getUsernameFromToken(authToken);
        String role = jwtTokenUtil.getStringFromToken(authToken, "admin");
        if(!(role.equals("admin") || role.equals("helpdesk"))){
            HashMap responseMap = new HashMap();
            responseMap.put("errorId",401);
            responseMap.put("description", "Unauthorized");
            return new JSONObject(responseMap);
        }

        LdapOperationWithoutPing ldapOperation = new LdapOperationWithoutPing ();
        LdapUser user = ldapOperation.searchUser ( userName );
        log.info ( "found User"+user.getUsername () );
        User user_add = new User ( user );
        try{
            //register user in pingID;

            if (user == null){
                log.error ( "User not found in AD!" );
                throw new Exception("User not Found");
            }
            PingIdProperties pingIdProperties = new PingIdProperties ();
            pingIdProperties.setProperties ( 0 );
            Operation operation = new Operation ( pingIdProperties.getOrgAlias (), pingIdProperties.getPingid_token (), pingIdProperties.getPingid_use_base64_key (), pingIdProperties.getApi_url () );
            operation.setTargetUser ( user.getUsername () );
            log.info ( "start looking for user in pingID" );
            JSONObject userDetails = pingIdOperationService.getUserDetails ( operation );
            if (userDetails == null) {
                log.info ( "user not found, register new user in PingID" );
                pingIdOperationService.addUser ( user_add, false, operation );
                log.info ( "user created in PingID" );
            }

            JSONObject jsonUser = new JSONObject (  );
            jsonUser.put ( "firstName", user.getFirstName () );
            jsonUser.put ( "lastName", user.getLastName ());
            jsonUser.put ( "mail", user.getEmailAddress () );
            jsonUser.put ( "username", userName );
            jsonUser.put ( "hardToken", user.isHardTokenUser ());
            jsonUser.put ( "desktopToken", user.isDesktopTokenUser ());
            jsonUser.put ( "SMSToken", user.isSMSTokenUser () );
            jsonUser.put ( "softToken", user.isSoftTokenUser ());
            jsonUser.put ( "otpToken", user.isOtpTokenUser ());

            return jsonUser;

        } catch (Exception e){
            return null;
        }
    }
    @RequestMapping(value = "/mail", method = RequestMethod.GET)
    public String sendMail(@RequestHeader("accept-language") HashMap<String, String> header, @RequestParam(name = "task") String task, @RequestParam(name = "user") String userName, @RequestParam(name = "admin") String adminUsername) throws Exception {
        //permission check
        String authToken  = header.get("authorization").toString ().replace( Constants.TOKEN_PREFIX, "");
        String usernameFromToken = jwtTokenUtil.getUsernameFromToken(authToken);
        String role = jwtTokenUtil.getStringFromToken(authToken, "admin");
        if(!(role.equals("admin") || role.equals("helpdesk"))){
            return "Unauthorized";
        }
        LdapOperationWithoutPing ldapOperation = new LdapOperationWithoutPing ();
        User user = new User(ldapOperation.searchUser ( userName ));
        User adminUser = new User();
        adminUser.setEmail ( ldapOperation.searchUser_manager ( user.getManager ()) );
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

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public void TestEndpoint(@RequestParam(name = "region", required = false) String region, @RequestParam(name = "dent", required = false) String dent){

      String test = "test1";
        String abc = region;
        int a = Integer.parseInt ( dent );
        System.out.println (a);
        System.out.println (abc);
        System.out.println ("test completed");
    }

    @RequestMapping(value = "/mail/self", method = RequestMethod.GET)
    public String sendSelfMail(@RequestHeader("accept-language") HashMap<String, String> header, @RequestParam(name = "task") String task, @RequestParam(name = "user") String userName) throws IOException {

        User user = new User(userName);
        //permission check
        if(pingIdController.checkPermission (userName, jwtTokenUtil.getUsernameFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, "")), jwtTokenUtil.getStringFromToken(header.get("authorization").toString ().replace(Constants.TOKEN_PREFIX, ""), "admin"))){
            return "Unauthorized";
        }

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