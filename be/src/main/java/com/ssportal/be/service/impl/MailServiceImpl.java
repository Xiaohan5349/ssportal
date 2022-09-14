package com.ssportal.be.service.impl;

import com.ssportal.be.ldaps.LdapUser;
import com.ssportal.be.model.User;
import com.ssportal.be.pingid.model.MailForm;
import com.ssportal.be.service.MailService;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    private final static Logger log = LoggerFactory.getLogger( MailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    Configuration mailConfiguration;


    private MailProperties mailProperties;

    public MailServiceImpl() throws IOException {
        mailProperties = new MailProperties ();
        this.mailProperties.setProperties ( 0 );
    }


    @Override
    public MailForm buildForm(){
        MailForm mailForm = new MailForm ();
        mailForm.setName ( "test" );
        mailForm.setTemplateName ( "test.html" );
        String[] tos = new String[]{"lixiaohan5349@gmail.com"};
        String subject = "test template";
        HashMap<String, Object> model = new HashMap<> (  );
        model.put ( "firstName", "Xiaohan" );
        model.put ( "lastName", "Li" );


        mailForm.setTo ( tos );
        mailForm.setSubject ( subject );
        mailForm.setModel ( model );

        return mailForm;

    }

    @Override
    public MailForm buildFormForSelfRegister(User user, LdapUser manager){
        MailForm mailForm = new MailForm ();
        mailForm.setName ( "pairdeviceself" );
        mailForm.setTemplateName ( "pairdeviceself.html" );
        String[] tos = new String[]{user.getEmail ()};
        String[] ccs = new String[]{manager.getEmailAddress ()};
        HashMap<String, Object> model = new HashMap<> (  );
        model.put ( "firstName", user.getFirstName () );
        model.put ( "lastName", user.getLastName () );

        mailForm.setTo ( tos );
        mailForm.setCc ( ccs );
        mailForm.setSubject ( mailProperties.getSubjectPairDeviceSelf ());
        mailForm.setModel ( model );

        return mailForm;

    }


    @Override
    public MailForm buildFormForSelfUnregister(User user, LdapUser manager){
        MailForm mailForm = new MailForm ();
        mailForm.setName ( "unpairdeviceself" );
        mailForm.setTemplateName ( "unpairdeviceself.html" );
        String[] tos = new String[]{user.getEmail ()};
        String[] ccs = new String[]{manager.getEmailAddress ()};

        HashMap<String, Object> model = new HashMap<> (  );
        model.put ( "firstName", user.getFirstName () );
        model.put ( "lastName", user.getLastName () );


        mailForm.setTo ( tos );
        mailForm.setCc ( ccs );
        mailForm.setSubject ( mailProperties.getSubjectUnpairDeviceSelf ());
        mailForm.setModel ( model );

        return mailForm;

    }


    @Override
    public MailForm buildFormForRegister(LdapUser adminUser,  LdapUser manager, User user){
        MailForm mailForm = new MailForm ();
        mailForm.setName ( "pairdevice" );
        mailForm.setTemplateName ( "pairdevice.html" );
        String[] tos = new String[]{user.getEmail ()};
        String[] ccs = new String[]{manager.getEmailAddress ()};
        HashMap<String, Object> model = new HashMap<> (  );
        model.put ( "firstName", user.getFirstName () );
        model.put ( "lastName", user.getLastName () );
        model.put("adminFirstName", adminUser.getFirstName ());
        model.put("adminLastName", adminUser.getLastName ());


        mailForm.setTo ( tos );
        mailForm.setCc ( ccs );
        mailForm.setSubject (  mailProperties.getSubjectPairDevice () +user.getFirstName ()+" "+user.getLastName ()+"!");
        mailForm.setModel ( model );

        return mailForm;

    }


    @Override
    public MailForm buildFormForUnregister(LdapUser adminUser,  LdapUser manager, User user){
        MailForm mailForm = new MailForm ();
        mailForm.setName ( "unpairdevice" );
        //todo do we need to send email to admin? which event need to send to manager
        mailForm.setTemplateName ( "unpairdevice.html" );
        String[] tos = new String[]{user.getEmail ()};
        String[] ccs = new String[]{manager.getEmailAddress ()};
        HashMap<String, Object> model = new HashMap<> (  );
        //model.put ("activationCode", activationCode);
        model.put ( "firstName", user.getFirstName () );
        model.put ( "lastName", user.getLastName () );
        model.put("adminFirstName", adminUser.getFirstName ());
        model.put("adminLastName", adminUser.getLastName ());

        mailForm.setTo ( tos );
        mailForm.setCc ( ccs );
        mailForm.setSubject (  mailProperties.getSubjectUnpairDevice () +user.getFirstName ()+" "+user.getLastName ()+"!" );
        mailForm.setModel ( model );

        return mailForm;

    }

    @Override
    public MailForm buildFormForBypass(LdapUser adminUser,  LdapUser manager, User user){
        MailForm mailForm = new MailForm ();
        mailForm.setName ( "bypass" );
        mailForm.setTemplateName ( "bypass.html" );
        String[] tos = new String[]{user.getEmail ()};
        String[] ccs = new String[]{manager.getEmailAddress ()};
        HashMap<String, Object> model = new HashMap<> (  );
        model.put ( "firstName", user.getFirstName () );
        model.put ( "lastName", user.getLastName () );
        model.put("adminFirstName", adminUser.getFirstName ());
        model.put("adminLastName", adminUser.getLastName ());


        mailForm.setTo ( tos );
        mailForm.setCc ( ccs );
        mailForm.setSubject ( mailProperties.getSubjectBypass ()+user.getFirstName ()+" "+user.getLastName ()+"!");
        mailForm.setModel ( model );

        return mailForm;

    }


    @Override
    public MailForm buildFormForEnableUser(LdapUser adminUser,  LdapUser manager, User user){

        MailForm mailForm = new MailForm ();
        mailForm.setName ( "enable" );
        mailForm.setTemplateName ( "enable.html" );
        String[] tos = new String[]{user.getEmail ()};
        String[] ccs = new String[]{manager.getEmailAddress ()};
        HashMap<String, Object> model = new HashMap<> (  );
        model.put ( "firstName", user.getFirstName () );
        model.put ( "lastName", user.getLastName () );
        model.put("adminFirstName", adminUser.getFirstName ());
        model.put("adminLastName", adminUser.getLastName ());


        mailForm.setTo ( tos );
        mailForm.setCc ( ccs );
        mailForm.setSubject ( mailProperties.getSubjectEanbleUser () +user.getFirstName ()+" "+user.getLastName ()+"!");
        mailForm.setModel ( model );

        return mailForm;

    }


    @Override
    public MailForm buildFormForDisableUser(LdapUser adminUser,  LdapUser manager, User user){
        MailForm mailForm = new MailForm ();
        mailForm.setName ( "disable" );
        mailForm.setTemplateName ( "disable.html" );
        String[] tos = new String[]{user.getEmail ()};
        String[] ccs = new String[]{manager.getEmailAddress ()};
        HashMap<String, Object> model = new HashMap<> (  );
        model.put ( "firstName", user.getFirstName () );
        model.put ( "lastName", user.getLastName () );
        model.put("adminFirstName", adminUser.getFirstName ());
        model.put("adminLastName", adminUser.getLastName ());


        mailForm.setTo ( tos );
        mailForm.setCc ( ccs );
        mailForm.setSubject ( mailProperties.getSubjectDisableUser () +user.getFirstName ()+" "+user.getLastName ()+"!");
        mailForm.setModel ( model );

        return mailForm;

    }


    @Override
    public void sendEmail(MailForm mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage ();
        try{
            mail.setFrom ( "IAMSupport@bedbath.com" );
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper ( mimeMessage, true );
            mimeMessageHelper.setSubject ( mail.getSubject () );
            mimeMessageHelper.setFrom ( mail.getFrom () );
            mimeMessageHelper.setTo ( mail.getTo () );
            mimeMessageHelper.setCc ( mail.getCc () );


            mail.setContent ( getContentFromTemplate ( mail.getModel (), mail.getTemplateName () ));
            mimeMessageHelper.setText ( mail.getContent (), true );
            mailSender.send ( mimeMessageHelper.getMimeMessage () );

        } catch (MessagingException e) {
            e.printStackTrace ();
        }
    }



    @Override
    public String getContentFromTemplate(Map<String, Object> model, String temlpateName) {
        StringBuffer content = new StringBuffer ();
        try {
            content.append ( FreeMarkerTemplateUtils.processTemplateIntoString ( mailConfiguration.getTemplate ( temlpateName ), model ) );
        } catch (Exception e) {
            log.error( "error when get Content from Mail template" );
            e.printStackTrace ();
        }
        return content.toString ();
    }



}
