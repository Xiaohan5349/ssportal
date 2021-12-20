package com.ssportal.be.service.impl;

import com.ssportal.be.pingid.model.MailForm;
import com.ssportal.be.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;
import java.util.Map;
import freemarker.template.Configuration;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    Configuration mailConfiguration;

    @Override
    public void sendEmail(MailForm mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage ();
        try{

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper ( mimeMessage, true );
            mimeMessageHelper.setSubject ( mail.getSubject () );
            mimeMessageHelper.setFrom ( mail.getFrom () );
            mimeMessageHelper.setTo ( mail.getTo () );
            mimeMessageHelper.setCc ( mail.getCc () );

            mail.setContent ( getContentFromTemplate(mail.getModel ()));
            mimeMessageHelper.setText ( mail.getContent (), true );

            mailSender.send ( mimeMessageHelper.getMimeMessage () );

        } catch (MessagingException e) {
            e.printStackTrace ();
        }
    }

    public String getContentFromTemplate(Map<String, Object> model) {
        StringBuffer content = new StringBuffer ();
        try {
            content.append ( FreeMarkerTemplateUtils.processTemplateIntoString ( mailConfiguration.getTemplate ( "template.html" ), model ) );
        } catch (Exception e) {
            System.out.println ( "error" );
            e.printStackTrace ();
        }
        return content.toString ();
    }

}
