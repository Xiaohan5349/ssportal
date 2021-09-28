package com.ssportal.be.controller;


import com.ssportal.be.ldaps.LdapOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AcsController {


//    @Bean
//    public WebClient localApiClient() {
//        return WebClient.create("http://localhost:4040/ssoportal/fe");
//    }

    @RequestMapping(value = "/saml-acs", method = RequestMethod.GET)
    public String getRefID(@RequestParam(name = "REF") String RefID, Model model) {
        Logger LOG = Logger.getLogger( LdapOperation.class);
        if (StringUtils.isNotBlank ( RefID )) {
            RefID = RefID.replaceAll ( "[^A-Za-z0-9]", "" );
        }
        model.addAttribute ( "RefID", RefID );
        //localApiClient ().get ().uri ( "?REF="+RefID );
        return "redirect";
//        attr.addAttribute ( "RefID", RefID );
//        return "redirect:/test.html";

    }

}
