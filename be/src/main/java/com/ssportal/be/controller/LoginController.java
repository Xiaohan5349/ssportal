package com.ssportal.be.controller;

import com.ssportal.be.config.JwtTokenUtil;
import com.ssportal.be.model.User;
import com.ssportal.be.repository.RoleRepository;
import com.ssportal.be.repository.UserRepository;
import com.ssportal.be.service.UserService;
import com.ssportal.be.utilility.ApiResponse;
import com.ssportal.be.utilility.AuthToken;
import com.ssportal.be.utilility.HelperUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.json.simple.parser.JSONParser;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.HashMap;

@RestController
public class LoginController {
    private static final Logger LOG = LoggerFactory.getLogger ( LoginController.class );

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    private final UserService userService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public LoginController(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public Object authenticate(
            @RequestParam(name = "REF") String RefID
    ) throws IOException {

        if (StringUtils.isNotBlank ( RefID )) {
            RefID = RefID.replaceAll ( "[^A-Za-z0-9]", "" );
        }
        System.out.println ( RefID );
        String base_url = "https://localhost:9031";
        String pickupLocation = base_url + "/ext/ref/pickup?REF=" + RefID;
        LOG.debug ( pickupLocation );
        URL pickUrl = new URL ( pickupLocation );
        HttpURLConnection httpURLConn = (HttpURLConnection) pickUrl.openConnection ();
        httpURLConn.setRequestProperty ( "ping.uname", "admin" );
        httpURLConn.setRequestProperty ( "ping.pwd", "Password1!!" );
        // ping.instanceId is optional and only needs to be specified if multiple instances of ReferenceId adapter are configured.
        httpURLConn.setRequestProperty ( "ping.instanceId", "ssoSPadapter" );
        String encoding = httpURLConn.getContentEncoding ();

//        PF Part
        try (InputStream is = httpURLConn.getInputStream()) {
            InputStreamReader streamReader = new InputStreamReader(is, encoding != null ? encoding : "UTF-8");

            JSONParser parser = new JSONParser();
                JSONObject spUserAttributes = (JSONObject) parser.parse(streamReader);

            String username = spUserAttributes.get( "subject" ).toString ();
            System.out.println ( username );

        if (username != null) {
            username = username.toLowerCase ();
            try {
                User user = new User (spUserAttributes);
                final String token = jwtTokenUtil.generateToken (user);
                System.out.println ( token );
                return new ApiResponse<> ( 200, "success", new AuthToken ( token, username ) );
            } catch (Exception ex) {
                LOG.error ( "", ex );
                return HelperUtil.handleException ( ex );
            }
        }
        } catch (ParseException e) {
            System.out.println ( "Certificate Error!!" );
            LOG.error("Error processing user details, Please contact Administrator", e);
        }
        return null;
    }
}

